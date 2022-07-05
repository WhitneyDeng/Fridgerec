package com.example.fridgerec.activities.lithoSpecs;

import com.example.fridgerec.model.EntryItem;
import com.facebook.litho.annotations.FromEvent;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.sections.Children;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.annotations.GroupSectionSpec;
import com.facebook.litho.sections.annotations.OnCreateChildren;
import com.facebook.litho.sections.common.DataDiffSection;
import com.facebook.litho.sections.common.RenderEvent;
import com.facebook.litho.sections.common.SingleComponentSection;
import com.facebook.litho.widget.ComponentRenderInfo;
import com.facebook.litho.widget.RenderInfo;
import com.facebook.litho.widget.Text;

import java.util.List;
import java.util.Objects;

@GroupSectionSpec
public class ListSectionSpec {
  public static final String NO_HEADER = "no food category";

  @OnCreateChildren
  public static Children onCreateChildren(
      final SectionContext c,
      @Prop String foodCategoryHeaderTitle,
      @Prop List<EntryItem> entryItems) { //todo: String => EntryItem & remove optional marker

    DataDiffSection.Builder<EntryItem> entryItem = DataDiffSection.<EntryItem>create(c)
        .data(entryItems)
        .renderEventHandler(ListSection.onRender(c))
        ;

    switch (foodCategoryHeaderTitle)
    {
      case NO_HEADER:
        return Children.create()
            .child(entryItem)
            .build();
      default:
        return Children.create()
            .child(
                SingleComponentSection.create(c)
                    .component(
                        Text.create(c)
                            .text(foodCategoryHeaderTitle)
                            .build()))
            .child(entryItem)
            .build();
    }
  }

  @OnEvent(RenderEvent.class)
  public static RenderInfo onRender(
      SectionContext c,
      @FromEvent EntryItem entryItem) { //todo: Integer => EntryItem
    return ComponentRenderInfo.create()
        .component(ListItem.create(c)
//            .color((model % 2 == 0 ? Color.WHITE : Color.LTGRAY))
            .foodName(entryItem.getFood().getFoodName()).getThis()
            .build())
        .build();
  }
}
