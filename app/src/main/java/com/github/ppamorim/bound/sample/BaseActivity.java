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
package com.github.ppamorim.bound.sample;

import android.os.Bundle;
import android.os.Handler;
import butterknife.InjectView;
import com.github.ppamorim.bound.BoundView;

public class BaseActivity extends AbstractActivity {

  @InjectView(R.id.bound_view) BoundView boundView;

  @Override protected int getLayoutId() {
    return R.layout.activity_base;
  }

  @Override protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        boundView.slideToCenter();
      }
    }, 1000);
  }
}
