package com.example.fridgerec.litho;

import com.example.fridgerec.activities.lithoSpecs.ListItem;
import com.example.fridgerec.activities.lithoSpecs.ListSection;
import com.example.fridgerec.model.EntryItem;
import com.facebook.litho.Row;
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
import com.facebook.yoga.YogaJustify;

import java.util.List;
import java.util.Objects;

@GroupSectionSpec
public class ListSectionSpec {
  public static final String NO_HEADER = "no food category";

  @OnCreateChildren
  public static Children onCreateChildren(
      final SectionContext c,
      @Prop (optional = true) String foodCategoryHeaderTitle,
      @Prop List<EntryItem> entryItems) {

    DataDiffSection.Builder<EntryItem> entryItem = DataDiffSection.<EntryItem>create(c)
        .data(entryItems)
        .renderEventHandler(ListSection.onRender(c));

    switch (foodCategoryHeaderTitle)
    {
      case NO_HEADER: //TODO: make this null instead of no header
        return Children.create()
            .child(entryItem)
            .build();
      default:
        return Children.create()
            .child(
                SingleComponentSection.create(c)
                    .component(
                        Row.create(c)
                            .justifyContent(YogaJustify.CENTER)
                            .child(
                                Text.create(c)
                                    .text(foodCategoryHeaderTitle)
                                    .build())))
            .child(entryItem)
            .build();
    }
  }

  @OnEvent(RenderEvent.class)
  public static RenderInfo onRender(
      SectionContext c,
      @FromEvent EntryItem model) {
    return ComponentRenderInfo.create()
        .component(ListItem.create(c)
            .entryItem(model)
            .build())
        .build();
  }
}
