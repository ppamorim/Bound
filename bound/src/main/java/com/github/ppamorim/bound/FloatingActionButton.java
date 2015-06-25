package com.github.ppamorim.bound;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

public class FloatingActionButton extends View {

  private static final int DEFAULT_SHADOW_SIZE = 2;

  private int radius;
  private float shadowSize = DEFAULT_SHADOW_SIZE;

  private OvalShadowDrawable mBackground;

  private Paint circlePaint = new Paint();

  public FloatingActionButton(Context context) {
    this(context, null);
  }

  public FloatingActionButton(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int pivot = (int)((radius * 2) + shadowSize);
    setMeasuredDimension(pivot, pivot);
    ViewCompat.setPivotX(this, 0);
    ViewCompat.setPivotY(this, 0);
    initializeBackground();
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    mBackground.draw(canvas);
    circlePaint.setStyle(Paint.Style.FILL);
    circlePaint.setColor(Color.GREEN);
    circlePaint.setAntiAlias(true);
    canvas.drawCircle(radius, radius, radius, circlePaint);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Override
  public float getElevation() {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      return super.getElevation();
    }
    return mBackground.getShadowSize();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Override
  public void setElevation(float elevation) {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      super.setElevation(elevation);
    } else if(mBackground.setShadow(elevation, elevation)) {
      requestLayout();
    }
  }

  public void initializeBackground() {
    mBackground = new OvalShadowDrawable(radius, Color.BLACK, shadowSize, shadowSize);
    mBackground.setBounds(0, 0, getWidth(), getHeight());
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }

  public float getShadowSize() {
    return shadowSize;
  }

  public void setShadowSize(float shadowSize) {
    this.shadowSize = shadowSize;
  }

}
