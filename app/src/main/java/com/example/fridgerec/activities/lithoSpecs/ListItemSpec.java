package com.example.fridgerec.activities.lithoSpecs;

import static com.facebook.yoga.YogaEdge.ALL;

import android.graphics.Color;

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
  @PropDefault
  public static final String sourceDate = "", expireDate = "";

  @OnCreateLayout
  public static Component onCreateLayout(
          ComponentContext c,
//          @Prop int color,
          @Prop String foodName,
          @Prop (optional = true) String expireDate,
          @Prop (optional = true) String sourceDate) {
    return Row.create(c)
        .justifyContent(YogaJustify.SPACE_BETWEEN)
            .paddingDip(ALL, 16)
//            .backgroundColor(color)
            .child(
                Text.create(c)
                    .text("food name")  //todo: replace w/ foodName
                    .textSizeSp(20))
            .child(
                    Column.create(c)
                        .child(
                            Text.create(c)
                                .text("expiration date")) //todo: replace w/ expireDate
                        .child(
                            Text.create(c)
                                .text("source date")) //todo: replace w/ sourceDate
                        .build())
            .build();
  }
}
