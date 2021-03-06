package com.example.fridgerec.lithoSpecs;

import com.example.fridgerec.interfaces.DatasetViewModel;
import com.example.fridgerec.model.EntryItem;
import com.facebook.litho.Row;
import com.facebook.litho.annotations.FromEvent;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.annotations.PropDefault;
import com.facebook.litho.sections.Children;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.annotations.GroupSectionSpec;
import com.facebook.litho.sections.annotations.OnCreateChildren;
import com.facebook.litho.sections.common.DataDiffSection;
import com.facebook.litho.sections.common.OnCheckIsSameContentEvent;
import com.facebook.litho.sections.common.OnCheckIsSameItemEvent;
import com.facebook.litho.sections.common.RenderEvent;
import com.facebook.litho.sections.common.SingleComponentSection;
import com.facebook.litho.widget.ComponentRenderInfo;
import com.facebook.litho.widget.RenderInfo;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaJustify;

import java.util.List;

@GroupSectionSpec
public class ListSectionSpec {
  public static final String NO_HEADER = "food group filter not selected";

  @PropDefault
  static final String foodCategoryHeaderTitle = NO_HEADER;

  @OnCreateChildren
  public static Children onCreateChildren(
      final SectionContext c,
      @Prop (optional = true) String foodCategoryHeaderTitle,
      @Prop List<EntryItem> entryItems,
      @Prop DatasetViewModel viewModel) {

    DataDiffSection.Builder<EntryItem> entryItem = DataDiffSection.<EntryItem>create(c)
        .data(entryItems)
        .onCheckIsSameItemEventHandler(ListSection.onCheckIsSameItem(c))
        .onCheckIsSameContentEventHandler(ListSection.onCheckIsSameContent(c))
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

  @OnEvent(OnCheckIsSameItemEvent.class)
  public static boolean onCheckIsSameItem(
      SectionContext c,
      @FromEvent EntryItem previousItem,
      @FromEvent EntryItem nextItem) {
    return previousItem.getObjectId().equals(nextItem.getObjectId());
  }

  @OnEvent(OnCheckIsSameContentEvent.class)
  public static boolean onCheckIsSameContent(
      SectionContext c,
      @FromEvent EntryItem previousItem,
      @FromEvent EntryItem nextItem) {
    return EntryItem.compareContent(previousItem, nextItem);
  }

  @OnEvent(RenderEvent.class)
  public static RenderInfo onRender(
      SectionContext c,
      @Prop DatasetViewModel viewModel,
      @FromEvent EntryItem model) {
    return ComponentRenderInfo.create()
        .component(ListItem.create(c)
            .entryItem(model)
            .viewModel(viewModel)
            .build())
        .build();
  }
}
