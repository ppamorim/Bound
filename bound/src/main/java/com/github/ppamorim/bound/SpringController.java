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

import android.support.v4.view.ViewCompat;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;

public class SpringController {

  private static final int DEFAULT_TENSION = 40;
  private static final int DEFAULT_FRICTION = 6;
  private static final int DEFAULT_SCALE = 2;

  private int centerX;
  private int centerY;

  private double val;
  private int scale = DEFAULT_SCALE;

  private double slideButtonTension = DEFAULT_TENSION;
  private double slideButtonFriction = DEFAULT_FRICTION;

  private double fabButtonTension = DEFAULT_TENSION;
  private double fabButtonFriction = DEFAULT_FRICTION;

  private static volatile Spring slideButtonSpring;
  private static volatile Spring fabButtonSpring;
  private static volatile Spring scaleButtonSpring;

  private ViewCallback viewCallback;

  public SpringController() {

  }

  public void slideToCenter() {
    slideButtonSpring().setEndValue(1);
  }

  public void onDetachedFromWindow() {
    slideButtonSpring().removeAllListeners();
    fabButtonSpring().removeAllListeners();
    scaleButtonSpring().removeAllListeners();
  }

  public void onAttachedToWindow() {
    slideButtonSpring().removeAllListeners().addListener(slideButtonSpringListener);
    //fabButtonSpring().removeAllListeners().addListener(fabButtonSpringListener);
    scaleButtonSpring().removeAllListeners().addListener(scaleButtonSpringListener);
  }

  public int getCenterX() {
    return centerX;
  }

  public void setCenterX(int centerX) {
    this.centerX = centerX;
  }

  public int getCenterY() {
    return centerY;
  }

  public void setCenterY(int centerY) {
    this.centerY = centerY;
  }

  public int getScale() {
    return scale;
  }

  public void setScale(int scale) {
    this.scale = scale;
  }

  public double getSlideButtonTension() {
    return slideButtonTension;
  }

  public void setSlideButtonTension(double slideButtonTension) {
    this.slideButtonTension = slideButtonTension;
  }

  public double getSlideButtonFriction() {
    return slideButtonFriction;
  }

  public void setSlideButtonFriction(double slideButtonFriction) {
    this.slideButtonFriction = slideButtonFriction;
  }

  public double getFabButtonTension() {
    return fabButtonTension;
  }

  public void setFabButtonTension(double fabButtonTension) {
    this.fabButtonTension = fabButtonTension;
  }

  public double getFabButtonFriction() {
    return fabButtonFriction;
  }

  public void setFabButtonFriction(double fabButtonFriction) {
    this.fabButtonFriction = fabButtonFriction;
  }

  public void setViewCallback(ViewCallback viewCallback) {
    this.viewCallback = viewCallback;
  }

  public Spring slideButtonSpring() {
    if(slideButtonSpring == null) {
      synchronized (Spring.class) {
        if(slideButtonSpring == null) {
          slideButtonSpring = SpringSystem
              .create()
              .createSpring()
              .setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(slideButtonTension, slideButtonFriction));
        }
      }
    }
    return slideButtonSpring;
  }

  public Spring fabButtonSpring() {
    if(fabButtonSpring == null) {
      synchronized (Spring.class) {
        if(fabButtonSpring == null) {
          fabButtonSpring = SpringSystem
              .create()
              .createSpring()
              .setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(fabButtonTension, fabButtonFriction));
        }
      }
    }
    return fabButtonSpring;
  }

  public Spring scaleButtonSpring() {
    if(scaleButtonSpring == null) {
      synchronized (Spring.class) {
        if(scaleButtonSpring == null) {
          scaleButtonSpring = SpringSystem
              .create()
              .createSpring()
              .setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(fabButtonTension, fabButtonFriction));
        }
      }
    }
    return scaleButtonSpring;
  }

  private SimpleSpringListener slideButtonSpringListener = new SimpleSpringListener() {
    @Override public void onSpringUpdate(Spring spring) {
      super.onSpringUpdate(spring);
      val = spring.getCurrentValue();
      ViewCompat.setTranslationX(viewCallback.getFabButton(),
          (float) SpringUtil.mapValueFromRangeToRange(val, 0, 1, 0, centerX + 100));
      ViewCompat.setTranslationY(viewCallback.getFabButton(),
          (float) SpringUtil.mapValueFromRangeToRange(val, 0, 1, 0, centerY + 100));
      ViewCompat.setTranslationX(viewCallback.getBoundMenu(),
          (float) SpringUtil.mapValueFromRangeToRange(val, 0, 1, 0, centerX + 100));
      ViewCompat.setTranslationY(viewCallback.getBoundMenu(),
          (float) SpringUtil.mapValueFromRangeToRange(val, 0, 1, 0, centerY ));
    }

    @Override public void onSpringAtRest(Spring spring) {
      super.onSpringAtRest(spring);
      scaleButtonSpring().setEndValue(1);
    }

  };

  private SimpleSpringListener scaleButtonSpringListener = new SimpleSpringListener() {
    @Override public void onSpringUpdate(Spring spring) {
      super.onSpringUpdate(spring);
      float scaleSpring = (float) SpringUtil.mapValueFromRangeToRange(
          spring.getCurrentValue(), 0, 1, 0, scale);
      ViewCompat.setScaleX(viewCallback.getBoundMenu(), scaleSpring);
      ViewCompat.setScaleY(viewCallback.getBoundMenu(), scaleSpring);
    }
  };

}
