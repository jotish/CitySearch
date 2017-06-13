package com.jotish.backbasecitysearch;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.Locale;

/**
 * Created by jotishsuthar on 12/06/17.
 */

public class Utils {

    public static  String loadJSONFromAsset(Context context) {
        String json = null;
        try {
          InputStream is = context.getAssets().open("city.list.json");
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

  public static boolean isNotEmptyString(String mValue) {
    return mValue != null && !mValue.trim().isEmpty();
  }
  public static CharSequence highlight(Context context, String search, String originalText) {
    // ignore case and accents
    // the same thing should have been done for the search text
    if (!isNotEmptyString(originalText) || !Utils.isNotEmptyString(search)) {
      return originalText;
    }
    String normalizer = Normalizer.normalize(originalText, Normalizer.Form.NFD);
    if (null == normalizer) {
      return originalText;
    }
    String normalizedText = normalizer.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
        .toLowerCase(Locale.getDefault());
    if (isNotEmptyString(search)) {

      int start = normalizedText.indexOf(search);
      if (start < 0) {
        // not found, nothing to to
        return originalText;
      } else {
        // highlight each appearance in the original text
        // while searching in normalized text
        Spannable highlighted = new SpannableString(originalText);
        while (start >= 0) {
          int spanStart = Math.min(start, originalText.length());
          int spanEnd = Math.min(start + search.length(), originalText.length());

          highlighted.setSpan(
              new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent)),
              spanStart, spanEnd,
              Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

          start = normalizedText.indexOf(search, spanEnd);
        }

        return highlighted;
      }
    } else {
      return originalText;
    }
  }
}
