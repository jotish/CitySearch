package com.jotish.backbasecitysearch.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jotishsuthar on 12/06/17.
 */

public class Coordinates implements Parcelable {
      public double lon;
      public double lat;

      protected Coordinates(Parcel in) {
            lon = in.readDouble();
            lat = in.readDouble();
      }

      public Coordinates(final double lon, final double lat) {
            this.lon = lon;
            this.lat = lat;
      }

      public static final Creator<Coordinates> CREATOR = new Creator<Coordinates>() {
            @Override
            public Coordinates createFromParcel(Parcel in) {
                  return new Coordinates(in);
            }

            @Override
            public Coordinates[] newArray(int size) {
                  return new Coordinates[size];
            }
      };

      @Override
      public int describeContents() {
            return 0;
      }

      @Override
      public void writeToParcel(final Parcel parcel, final int i) {
            parcel.writeDouble(lon);
            parcel.writeDouble(lat);
      }
}
