package com.jotish.backbasecitysearch.repo;

import com.jotish.backbasecitysearch.models.City;
import java.util.Comparator;

/**
 * Created by jotishsuthar on 12/06/17.
 */

public class CityComparator implements Comparator<City> {

  @Override
  public int compare(final City city1, final City city2) {
    int c;
    c = city1.country.compareTo(city2.country);
    if (c == 0)
      c = city1.name.compareTo(city2.name);
    return c;
  }
}
