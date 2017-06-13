package com.jotish.backbasecitysearch.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jotishsuthar on 12/06/17.
 */

public class City implements  Parcelable {
    public String country;
    public String name;
    public int  _id;
    public Coordinates coord;

    public City(final String country, final String name, final int _id,
        final Coordinates coord) {
        this.country = country;
        this.name = name;
        this._id = _id;
        this.coord = coord;
    }

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final City city = (City) o;

        if (!country.equals(city.country)) {
            return false;
        }

        return name.equals(city.name);
    }

    @Override
    public int hashCode() {
        int result = country.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
