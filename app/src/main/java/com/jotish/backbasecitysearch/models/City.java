package com.jotish.backbasecitysearch.models;

import android.support.annotation.NonNull;

/**
 * Created by jotishsuthar on 12/06/17.
 */

public class City implements Comparable {
    public String country;
    public String name;
    public int  _id;
    public Coordinates coord;

    @Override
    public int compareTo(@NonNull final Object another) {
        if(another instanceof  City) {
            City b = (City) another;
            return this.name.compareTo(b.name);
        }
        return -1;
    }
}
