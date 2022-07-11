package com.example.fridgerec.lithoSpecs;

import static com.facebook.yoga.YogaEdge.ALL;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.fridgerec.interfaces.DatasetViewModel;
import com.example.fridgerec.model.EntryItem;
import com.facebook.litho.ClickEvent;
import com.facebook.litho.Column;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.LongClickEvent;
import com.facebook.litho.Row;
import com.facebook.litho.annotations.FromEvent;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaJustify;

import java.text.SimpleDateFormat;
import java.util.Date;

@LayoutSpec
public class ListItemSpec {
  public static final String TAG = "ListItemSpec";
  public static final String SOURCE_DATE_DESC = "source date: ", EXPIRE_DATE_DESC = "expire date: ", EMPTY_DATE = "(N/A)";

  @OnCreateLayout
  public static Component onCreateLayout(
          ComponentContext c,
          @Prop EntryItem entryItem,
          @Prop DatasetViewModel viewModel) {

    Row.Builder component = Row.create(c)
        .justifyContent(YogaJustify.SPACE_BETWEEN)
        .paddingDip(ALL, 16)
        .child(
            Text.create(c)
                .text(entryItem.getFood().getFoodName())
                .textSizeSp(20))
        .clickHandler(ListItem.onClick(c))
        .longClickHandler(ListItem.onLongClick(c));

    Date expireDate = entryItem.getExpireDate();
    Date sourceDate = entryItem.getSourceDate();

    String expireDateString = EXPIRE_DATE_DESC + (expireDate == null ? EMPTY_DATE : formatDate(expireDate));
    String sourceDateString = SOURCE_DATE_DESC + (sourceDate == null ? EMPTY_DATE : formatDate(sourceDate));

    try {
      return component
          .child(
              Column.create(c)
                  .child(
                      Text.create(c)
                          .text(expireDateString))
                  .child(
                      Text.create(c)
                          .text(sourceDateString))
                  .build())
          .build();
    } catch (NullPointerException e) {
      return component
          .build();
    }
  }

  @OnEvent(ClickEvent.class)
  public static void onClick(ComponentContext c,
                             @Prop DatasetViewModel viewModel,
                             @Prop EntryItem entryItem,
                             @FromEvent View view) {
    Log.i(TAG, "click detected: " + entryItem.getFood().getFoodName());
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


}
