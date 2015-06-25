package com.github.ppamorim.bound;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

public class OvalShadowDrawable extends Drawable {

  private Paint mShadowPaint;
  private Paint mGlowPaint;
  private Paint mPaint;

  private int mRadius;
  private float mShadowSize;
  private float mShadowOffset;

  private Path mShadowPath;
  private Path mGlowPath;

  private RectF mTempRect = new RectF();

  private int mColor;

  private boolean mNeedBuildShadow = true;

  private static final int COLOR_SHADOW_START = 0x4C000000;
  private static final int COLOR_SHADOW_END = 0x00000000;

  public OvalShadowDrawable(int radius, int color, float shadowSize, float shadowOffset){
    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    mPaint.setStyle(Paint.Style.FILL);

    setColor(color);
    setRadius(radius);
    setShadow(shadowSize, shadowOffset);
  }

  public boolean setRadius(int radius){
    if(mRadius != radius){
      mRadius = radius;
      mNeedBuildShadow = true;
      invalidateSelf();
      return true;
    }
    return false;
  }

  public boolean setShadow(float size, float offset){
    if(mShadowSize != size || mShadowOffset != offset){
      mShadowSize = size;
      mShadowOffset = offset;
      mNeedBuildShadow = true;
      invalidateSelf();
      return true;
    }
    return false;
  }

  public void setColor(int color){
    if(mColor != color){
      mColor = color;
      mPaint.setColor(mColor);
      invalidateSelf();
    }
  }

  public int getColor(){
    return mColor;
  }

  public int getRadius(){
    return mRadius;
  }

  public float getShadowSize(){
    return mShadowSize;
  }

  public float getShadowOffset(){
    return mShadowOffset;
  }

  public float getPaddingLeft(){
    return mShadowSize;
  }

  public float getPaddingTop(){
    return mShadowSize;
  }

  public float getPaddingRight(){
    return mShadowSize;
  }

  public float getPaddingBottom(){
    return mShadowSize + mShadowOffset;
  }

  public float getCenterX(){
    return mRadius + mShadowSize;
  }

  public float getCenterY(){
    return mRadius + mShadowSize;
  }

  public boolean isPointerOver(float x, float y){
    float distance = (float)Math.sqrt(Math.pow(x - getCenterX(), 2) + Math.pow(y - getCenterY(), 2));
    return distance < mRadius;
  }

  @Override public int getIntrinsicWidth() {
    return (int)((mRadius + mShadowSize) * 2 + 0.5f);
  }

  @Override public int getIntrinsicHeight() {
    return (int)((mRadius + mShadowSize) * 2 + mShadowOffset + 0.5f);
  }

  private void buildShadow(){
    if(mShadowSize <= 0)
      return;

    if(mShadowPaint == null){
      mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
      mShadowPaint.setStyle(Paint.Style.FILL);
      mShadowPaint.setDither(true);
    }
    float startRatio = (float)mRadius / (mRadius + mShadowSize + mShadowOffset);
    mShadowPaint.setShader(new RadialGradient(0, 0, mRadius + mShadowSize,
        new int[]{COLOR_SHADOW_START, COLOR_SHADOW_START, COLOR_SHADOW_END},
        new float[]{0f, startRatio, 1f}
        , Shader.TileMode.CLAMP));

    if(mShadowPath == null){
      mShadowPath = new Path();
      mShadowPath.setFillType(Path.FillType.EVEN_ODD);
    }
    else
      mShadowPath.reset();
    float radius = mRadius + mShadowSize;
    mTempRect.set(-radius, -radius, radius, radius);
    mShadowPath.addOval(mTempRect, Path.Direction.CW);
    radius = mRadius - 1;
    mTempRect.set(-radius, -radius - mShadowOffset, radius, radius - mShadowOffset);
    mShadowPath.addOval(mTempRect, Path.Direction.CW);

    if(mGlowPaint == null){
      mGlowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
      mGlowPaint.setStyle(Paint.Style.FILL);
      mGlowPaint.setDither(true);
    }
    startRatio = (mRadius - mShadowSize / 2f) / (mRadius + mShadowSize / 2f);
    mGlowPaint.setShader(new RadialGradient(0, 0, mRadius + mShadowSize / 2f,
        new int[]{COLOR_SHADOW_START, COLOR_SHADOW_START, COLOR_SHADOW_END},
        new float[]{0f, startRatio, 1f}
        , Shader.TileMode.CLAMP));

    if(mGlowPath == null){
      mGlowPath = new Path();
      mGlowPath.setFillType(Path.FillType.EVEN_ODD);
    }
    else
      mGlowPath.reset();

    radius = mRadius + mShadowSize / 2f;
    mTempRect.set(-radius, -radius, radius, radius);
    mGlowPath.addOval(mTempRect, Path.Direction.CW);
    radius = mRadius - 1;
    mTempRect.set(-radius, -radius, radius, radius);
    mGlowPath.addOval(mTempRect, Path.Direction.CW);
  }

  @Override public void draw(Canvas canvas) {
    if(mNeedBuildShadow){
      buildShadow();
      mNeedBuildShadow = false;
    }
    int saveCount;

    if(mShadowSize > 0){
      saveCount = canvas.save();
      canvas.translate(mShadowSize + mRadius,  mShadowSize + mRadius + mShadowOffset);
      canvas.drawPath(mShadowPath, mShadowPaint);
      canvas.restoreToCount(saveCount);
    }

    saveCount = canvas.save();
    canvas.translate(mShadowSize + mRadius, mShadowSize + mRadius);
    if(mShadowSize > 0)
      canvas.drawPath(mGlowPath, mGlowPaint);
    mTempRect.set(-mRadius, -mRadius, mRadius, mRadius);
    canvas.drawOval(mTempRect, mPaint);
    canvas.restoreToCount(saveCount);
  }

  @Override public void setAlpha(int alpha) {
    mShadowPaint.setAlpha(alpha);
    mPaint.setAlpha(alpha);
  }

  @Override public void setColorFilter(ColorFilter cf) {
    mShadowPaint.setColorFilter(cf);
    mPaint.setColorFilter(cf);
  }

  @Override public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
  }

}
