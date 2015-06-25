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

  private int centerX;
  private int centerY;

  private double val;

  private double slideButtonTension = DEFAULT_TENSION;
  private double slideButtonFriction = DEFAULT_FRICTION;

  private double fabButtonTension = DEFAULT_TENSION;
  private double fabButtonFriction = DEFAULT_FRICTION;

  private static volatile Spring slideButtonSpring;
  private static volatile Spring fabButtonSpring;

  private ViewCallback viewCallback;

  public SpringController() {

  }

  public void slideToCenter() {
    slideButtonSpring().setEndValue(1);
  }

  public void onDetachedFromWindow() {
    slideButtonSpring().removeAllListeners();
    fabButtonSpring().removeAllListeners();
  }

  public void onAttachedToWindow() {
    slideButtonSpring().removeAllListeners().addListener(slideButtonSpringListener);
    fabButtonSpring().removeAllListeners().addListener(fabButtonSpringListener);
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

  private SimpleSpringListener slideButtonSpringListener = new SimpleSpringListener() {
    @Override public void onSpringUpdate(Spring spring) {
      super.onSpringUpdate(spring);
      val = spring.getCurrentValue();
      ViewCompat.setTranslationX(viewCallback.getFabButton(),
          (float) SpringUtil.mapValueFromRangeToRange(val, 0, 1, 0, centerX));
      ViewCompat.setTranslationY(viewCallback.getFabButton(),
          (float) SpringUtil.mapValueFromRangeToRange(val, 0, 1, 0, centerY));
    }
  };

  private SimpleSpringListener fabButtonSpringListener = new SimpleSpringListener() {
    @Override public void onSpringUpdate(Spring spring) {
      super.onSpringUpdate(spring);
    }
  };

}
