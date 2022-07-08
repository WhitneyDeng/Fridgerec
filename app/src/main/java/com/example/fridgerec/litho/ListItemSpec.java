package com.example.fridgerec.litho;

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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@LayoutSpec
public class ListItemSpec {
  public static final String SOURCE_DATE_DESC = "source date: ", EXPIRE_DATE_DESC = "expire date: ", EMPTY_DATE = "(N/A)";

  @OnCreateLayout
  public static Component onCreateLayout(
          ComponentContext c,
          @Prop EntryItem entryItem) {

    Date expireDate = entryItem.getExpireDate();
    Date sourceDate = entryItem.getSourceDate();

    String expireDateString = EXPIRE_DATE_DESC + (expireDate == null ? EMPTY_DATE : formatDate(expireDate));
    String sourceDateString = SOURCE_DATE_DESC + (sourceDate == null ? EMPTY_DATE : formatDate(sourceDate));

    try {
      return Row.create(c)
          .justifyContent(YogaJustify.SPACE_BETWEEN)
          .paddingDip(ALL, 16)
          .child(
              Text.create(c)
                  .text(entryItem.getFood().getFoodName())
                  .textSizeSp(20))
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
      return Row.create(c)
          .justifyContent(YogaJustify.SPACE_BETWEEN)
          .paddingDip(ALL, 16)
          .child(
              Text.create(c)
                  .text(entryItem.getFood().getFoodName())
                  .textSizeSp(20))
          .build();
    }
  }

  private static String formatDate(Date date) {
    String pattern = "dd MMMM yyyy";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    return simpleDateFormat.format(date);
  }
}
