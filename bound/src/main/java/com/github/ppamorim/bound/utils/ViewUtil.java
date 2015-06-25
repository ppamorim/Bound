package com.github.ppamorim.bound.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import com.github.ppamorim.bound.R;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewUtil {

  private static TypedValue value;

  public static final long FRAME_DURATION = 1000 / 60;

  private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

  @SuppressLint("NewApi")
  public static int generateViewId() {
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
      for (;;) {
        final int result = sNextGeneratedId.get();
        // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
        int newValue = result + 1;
        if (newValue > 0x00FFFFFF)
          newValue = 1; // Roll over to 1, not 0.
        if (sNextGeneratedId.compareAndSet(result, newValue))
          return result;
      }
    }
    else
      return android.view.View.generateViewId();
  }

  public static boolean hasState(int[] states, int state){
    if(states == null)
      return false;

    for (int state1 : states)
      if (state1 == state)
        return true;

    return false;
  }

  public static void setBackground(View v, Drawable drawable){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      v.setBackground(drawable);
    } else {
      v.setBackgroundDrawable(drawable);
    }
  }

  public static int dpToPx(Context context, int dp){
    return (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
        context.getResources().getDisplayMetrics()) + 0.5f);
  }

  public static int getType(TypedArray array, int index){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
      return array.getType(index);
    else{
      TypedValue value = array.peekValue(index);
      return value == null ? TypedValue.TYPE_NULL : value.type;
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static int colorControlHighlight(Context context, int defaultValue){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
      return getColor(context, android.R.attr.colorControlHighlight, defaultValue);

    return getColor(context, R.attr.colorControlHighlight, defaultValue);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static int colorAccent(Context context, int defaultValue){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
      return getColor(context, android.R.attr.colorAccent, defaultValue);

    return getColor(context, R.attr.colorAccent, defaultValue);
  }

  private static int getColor(Context context, int id, int defaultValue){
    if(value == null)
      value = new TypedValue();

    try{
      Theme theme = context.getTheme();
      if(theme != null && theme.resolveAttribute(id, value, true)){
        if (value.type >= TypedValue.TYPE_FIRST_INT && value.type <= TypedValue.TYPE_LAST_INT)
          return value.data;
        else if (value.type == TypedValue.TYPE_STRING)
          return context.getResources().getColor(value.resourceId);
      }
    }
    catch(Exception ex){}

    return defaultValue;
  }

}
