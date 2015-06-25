/*
* Copyright (C) 2015 Pedro Paulo de Amorim
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.github.ppamorim.bound;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

/**
 *
 * @author Pedro Paulo Amorim
 *
 */
public class BoundView extends FrameLayout {

  private FloatingActionButton floatingActionButton;
  private BoundMenu boundMenu;
  private SpringController springController;

  public BoundView(Context context) {
    this(context, null);
  }

  public BoundView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BoundView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    if(!isInEditMode()) {
      initializeBoundMenu();
      initializeFabButton();
      initializeSpringController();
    }
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    springController.setCenterX(-w / 2);
    springController.setCenterY(-h / 2);
  }

  @Override protected void onDetachedFromWindow() {
    if(springController != null) {
      springController.onDetachedFromWindow();
    }
    super.onDetachedFromWindow();
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if(springController != null) {
      springController.onAttachedToWindow();
    }
  }

  private void initializeSpringController() {
    if(springController == null) {
      springController = new SpringController();
      springController.setViewCallback(viewCallback);
      springController.setScale(2);
    }
  }

  private void initializeFabButton() {
    if(floatingActionButton == null) {
      floatingActionButton = new FloatingActionButton(getContext());
      floatingActionButton.setRadius(100);
      floatingActionButton.setOnClickListener(onFabButtonClick);
    }
    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
        LayoutParams.WRAP_CONTENT,
        LayoutParams.WRAP_CONTENT);
    params.gravity = Gravity.BOTTOM|Gravity.RIGHT;
    addView(floatingActionButton, params);
  }

  private void initializeBoundMenu() {
    if(boundMenu == null) {
      boundMenu = new BoundMenu(getContext());
      boundMenu.setRadius(100);
    }
    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
        LayoutParams.WRAP_CONTENT,
        LayoutParams.WRAP_CONTENT);
    params.gravity = Gravity.BOTTOM|Gravity.RIGHT;
    addView(boundMenu, params);
  }

  public void slideToCenter() {
    springController.slideToCenter();
  }

  private View.OnClickListener onFabButtonClick = new OnClickListener() {
    @Override public void onClick(View v) {
      slideToCenter();
    }
  };

  private ViewCallback viewCallback = new ViewCallback() {
    @Override public View getFabButton() {
      return floatingActionButton;
    }
    @Override public View getBoundMenu() {
      return boundMenu;
    }
  };

}
