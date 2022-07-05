package com.example.fridgerec.activities.lithoSpecs;

import static com.facebook.yoga.YogaEdge.ALL;

import android.graphics.Color;

import com.example.fridgerec.model.EntryItem;
import com.facebook.litho.Column;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.Row;
import com.facebook.litho.TestItem;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.annotations.PropDefault;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaJustify;

@LayoutSpec
public class ListItemSpec {
  public static final String SOURCE_DATE_DESC = "source date: ", EXPIRE_DATE_DESC = "expire date: ";

  @OnCreateLayout
  public static Component onCreateLayout(
          ComponentContext c,
//          @Prop int color,
          @Prop EntryItem entryItem) {
    try {
      return Row.create(c)
          .justifyContent(YogaJustify.SPACE_BETWEEN)
          .paddingDip(ALL, 16)
//            .backgroundColor(color)
          .child(
              Text.create(c)
                  .text(entryItem.getFood().getFoodName())  //todo: replace w/ foodName
                  .textSizeSp(20))
          .child(
              Column.create(c)
                  .child(
                      Text.create(c)
                          .text(EXPIRE_DATE_DESC + entryItem.getExpireDate().toString()))
                  .child(
                      Text.create(c)
                          .text(SOURCE_DATE_DESC + entryItem.getSourceDate().toString()))
                  .build())
          .build();
    } catch (NullPointerException e) {
      return Row.create(c)
          .justifyContent(YogaJustify.SPACE_BETWEEN)
          .paddingDip(ALL, 16)
//            .backgroundColor(color)
          .child(
              Text.create(c)
                  .text(entryItem.getFood().getFoodName())  //todo: replace w/ foodName
                  .textSizeSp(20))
          .build();
    }
    //todo: what if xor sourceDate or expireDate is null (one of them is nonnull)
  }
}
