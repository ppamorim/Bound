package com.github.ppamorim.bound;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

public class BoundMenu extends View {

  private int radius;

  private Paint circlePaint = new Paint();

  public BoundMenu(Context context) {
    this(context, null);
  }

  public BoundMenu(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BoundMenu(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int pivot = radius * 2;
    setMeasuredDimension(pivot, pivot);
    ViewCompat.setPivotX(this, radius);
    ViewCompat.setPivotY(this, radius);
    ViewCompat.setScaleX(this, 0);
    ViewCompat.setScaleY(this, 0);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    circlePaint.setStyle(Paint.Style.FILL);
    circlePaint.setColor(Color.BLUE);
    circlePaint.setAntiAlias(true);
    canvas.drawCircle(radius, radius, radius, circlePaint);
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }

}
