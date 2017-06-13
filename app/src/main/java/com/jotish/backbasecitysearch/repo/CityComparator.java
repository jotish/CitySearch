package com.jotish.backbasecitysearch.repo;

import com.jotish.backbasecitysearch.models.City;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by jotishsuthar on 12/06/17.
 */

public class CityComparator implements Comparator<City> {

  @Override
  public int compare(final City city1, final City city2) {
    int c;
    Locale defaultLocale = Locale.getDefault();
    c = city1.country.toUpperCase(defaultLocale).compareTo(city2.country.toUpperCase(defaultLocale));
    if (c == 0)
      c = city1.name.toUpperCase(defaultLocale).compareTo(city2.name.toUpperCase(defaultLocale));
    return c;
  }
}
