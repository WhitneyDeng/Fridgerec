package com.example.fridgerec.lithoSpecs;

import static com.facebook.yoga.YogaEdge.ALL;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.example.fridgerec.interfaces.DatasetViewModel;
import com.example.fridgerec.model.EntryItem;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.litho.ClickEvent;
import com.facebook.litho.Column;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.LongClickEvent;
import com.facebook.litho.Row;
import com.facebook.litho.StateValue;
import com.facebook.litho.annotations.FromEvent;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateInitialState;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.annotations.OnUpdateState;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.annotations.State;
import com.facebook.litho.fresco.FrescoImage;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaJustify;

import java.text.SimpleDateFormat;
import java.util.Date;

@LayoutSpec
public class ListItemSpec {
  public static final String TAG = "ListItemSpec";
  public static final String SOURCE_DATE_DESC = "source date: ", EXPIRE_DATE_DESC = "expire date: ", EMPTY = "-";
  public static final String AMOUNT_LEFT_DESC = "amount left: ";

  @OnCreateLayout
  public static Component onCreateLayout(
      ComponentContext c,
      @Prop EntryItem entryItem,
      @Prop DatasetViewModel viewModel,
      @State Boolean isChecked) {

    Log.i(TAG, "rendering: " + entryItem + " " + entryItem.getFood().getFoodName());

    String imageUrl = entryItem.getFood().getImageUrl();
    imageUrl = imageUrl == null ? "https://upload.wikimedia.org/wikipedia/commons/5/55/Question_Mark.svg" : imageUrl;

    Date expireDate = entryItem.getExpireDate();
    Date sourceDate = entryItem.getSourceDate();

    String expireDateString = expireDate == null ? EMPTY : formatDate(expireDate);
    String sourceDateString = sourceDate == null ? EMPTY : formatDate(sourceDate);

    String amountLeftString = nullCheckInt(entryItem.getAmount()) + " " + nullcheckString(entryItem.getAmountUnit());

    Row.Builder component = Row.create(c)
        .justifyContent(YogaJustify.SPACE_BETWEEN)
        .paddingDip(ALL, 16)
        .child(
            Row.create(c)
                .justifyContent(YogaJustify.FLEX_START)
                .child(
                    FrescoImage.create(c)
                        .controller(getImageController(c, imageUrl))
                        .maxWidthPx(200)
                        .actualImageScaleType(ScalingUtils.ScaleType.FIT_START)
                )
                .child(
                    Column.create(c)
                        .child(
                            Text.create(c)
                                .text(entryItem.getFood().getFoodName())
                                .textSizeSp(30)
                                .textStyle(Typeface.BOLD)
                        )
                        .child(
                            getDescriptionTextComponent(c, AMOUNT_LEFT_DESC)
                        )
                        .child(
                            Text.create(c)
                                .text(amountLeftString))
                )
        )
        .clickHandler(ListItem.onClick(c))
        .longClickHandler(ListItem.onLongClick(c))
        .backgroundColor(isChecked ? Color.GRAY : Color.WHITE);

    if (viewModel.getContainerList() == EntryItem.CONTAINER_LIST_INVENTORY) {
      return component.child(
          Column.create(c)
              .child(
                  getDescriptionTextComponent(c, EXPIRE_DATE_DESC)
              )
              .child(
                  Text.create(c)
                      .text(expireDateString)
              )
              .child(
                  getDescriptionTextComponent(c, SOURCE_DATE_DESC)
              )
              .child(
                  Text.create(c)
                      .text(sourceDateString)
              )
          )
          .build();
    } else {
      return component.build();
    }
  }

  private static String nullCheckInt(Integer i) {
    return i == 0 ? EMPTY : i.toString();
  }

  private static String nullcheckString(String s) {
    return s == null ? EMPTY : s;
  }

  @OnCreateInitialState
  public static void onCreateInitialState(ComponentContext c, StateValue<Boolean> isChecked) {
    isChecked.set(false);
  }

  @OnUpdateState
  public static void toggleIsCheckedState(StateValue<Boolean> isChecked) {
    isChecked.set(!isChecked.get());
  }

  @OnEvent(ClickEvent.class)
  public static void onClick(ComponentContext c,
                             @Prop DatasetViewModel viewModel,
                             @Prop EntryItem entryItem,
                             @State Boolean isChecked,
                             @FromEvent View view) {
    Log.i(TAG, "click detected: " + entryItem.getFood().getFoodName());

    if (Boolean.TRUE.equals(viewModel.getInDeleteMode().getValue())) {
      if (isChecked) {
        viewModel.getCheckedItemsSet().remove(entryItem);
      } else {
        viewModel.getCheckedItemsSet().add(entryItem);
      }
      Log.i(TAG, "checkedItemsSet: " + viewModel.getCheckedItemsSet().toString());
      ListItem.toggleIsCheckedState(c);
    } else {
      viewModel.setSelectedEntryItem(entryItem);
      viewModel.getInEditMode().setValue(true);
      Log.i(TAG, "selectedItem: " + viewModel.getSelectedEntryItem().getFood().getFoodName());
    }
  }

  @OnEvent(LongClickEvent.class)
  public static boolean onLongClick(ComponentContext c,
                                    @Prop DatasetViewModel viewModel,
                                    @Prop EntryItem entryItem,
                                    @FromEvent View view) {
    Log.i(TAG, "long click detected: " + entryItem.getFood().getFoodName());

    MutableLiveData<Boolean> inDeleteMode = viewModel.getInDeleteMode();
    if (Boolean.FALSE.equals(inDeleteMode.getValue())) {
      inDeleteMode.setValue(true);
    }
    return true;
  }

  private static String formatDate(Date date) {
    String pattern = "dd MMMM yyyy";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    return simpleDateFormat.format(date);
  }

  private static DraweeController getImageController(ComponentContext c, String imageUrl) {
    return Fresco
        .newDraweeControllerBuilder()
        .setUri(imageUrl)
        .build();
  }

  private static Text.Builder getDescriptionTextComponent(ComponentContext c, String string) {
    return Text.create(c)
        .text(string)
        .textStyle(Typeface.ITALIC)
        .textColor(Color.GRAY);
  }
}
