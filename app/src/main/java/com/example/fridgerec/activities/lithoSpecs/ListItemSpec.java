package com.example.fridgerec.activities.lithoSpecs;

import static com.facebook.yoga.YogaEdge.ALL;

import android.graphics.Color;

import com.facebook.litho.Column;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.widget.Text;

@LayoutSpec
public class ListItemSpec {
  @OnCreateLayout
  static Component onCreateLayout(ComponentContext c) {
    return Column.create(c)
            .paddingDip(ALL, 16)
            .backgroundColor(Color.WHITE)
            .child(
                    Text.create(c)
                            .text("Hello world")
                            .textSizeSp(40))
            .child(
                    Text.create(c)
                            .text("Litho tutorial")
                            .textSizeSp(20))
            .build();
  }
}
