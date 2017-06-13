package com.jotish.backbasecitysearch;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by jotishsuthar on 13/06/17.
 */

public class CitySearchApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    if (BuildConfig.DEBUG) {
      LeakCanary.install(this);
    }
  }
}
