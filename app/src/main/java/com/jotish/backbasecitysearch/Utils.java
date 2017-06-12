package com.jotish.backbasecitysearch;

import android.app.Activity;
import android.content.Context;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jotishsuthar on 12/06/17.
 */

public class Utils {

    public static  String loadJSONFromAsset(Context context) {
        String json = null;
        try {
          InputStream is = context.getAssets().open("city.list-short.json");
          int size = is.available();
          byte[] buffer = new byte[size];
          is.read(buffer);
          is.close();
          json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
          ex.printStackTrace();
          return null;
        }
        return json;
    }
  public static boolean isActivityAlive(Activity activity) {
    return !(null == activity || activity.isFinishing() || activity.isDestroyed());
  }

}
