package com.example.fridgerec.lithoSpecs;

import com.example.fridgerec.interfaces.DatasetViewModel;
import com.example.fridgerec.model.EntryItem;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.sections.Children;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.annotations.GroupSectionSpec;
import com.facebook.litho.sections.annotations.OnCreateChildren;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@GroupSectionSpec
public class FoodGroupsSectionSpec {
  @OnCreateChildren
  static Children onCreateChildren(
      SectionContext c,
      @Prop HashMap<String, List<EntryItem>> foodGroupMap,
      @Prop DatasetViewModel viewModel) {
    Children.Builder builder = Children.create();

    for(Map.Entry<String, List<EntryItem>> entry : foodGroupMap.entrySet()) {
      builder.child(
          ListSection.create(c)
              .foodCategoryHeaderTitle(entry.getKey())
              .entryItems(entry.getValue())
              .viewModel(viewModel));
    }
    return builder.build();
  }
}
