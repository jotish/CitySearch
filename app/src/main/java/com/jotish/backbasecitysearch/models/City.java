package com.jotish.backbasecitysearch.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by jotishsuthar on 12/06/17.
 */

public class City implements  Parcelable {
    public String country;
    public String name;
    public int  _id;
    public Coordinates coord;

    protected City(Parcel in) {
        country = in.readString();
        name = in.readString();
        _id = in.readInt();
        coord = in.readParcelable(Coordinates.class.getClassLoader());
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeString(country);
        parcel.writeString(name);
        parcel.writeInt(_id);
        parcel.writeParcelable(coord, i);
    }
}
