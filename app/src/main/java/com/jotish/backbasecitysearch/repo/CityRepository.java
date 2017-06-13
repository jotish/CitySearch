package com.jotish.backbasecitysearch.repo;

import com.jotish.backbasecitysearch.CityComparator;
import com.jotish.backbasecitysearch.Utils;
import com.jotish.backbasecitysearch.models.City;
import com.jotish.backbasecitysearch.trie.TrieMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by jotishsuthar on 13/06/17.
 */

public class CityRepository {

  public static List<City> onSearch(List<City> orignalSortedList,
      TrieMap<City> searchTree, String searchKey) {
    if (searchTree == null) {
      return null;
    }

    if (Utils.isNotEmptyString(searchKey)) {
      searchKey = searchKey.toLowerCase(Locale.getDefault());
      ArrayList<City> citiesList = new ArrayList<>();
      TrieMap<City> result = searchTree.getSubTrie(searchKey);
      if (result != null) {
        Set<Entry<String, City>> cities = result.entrySet();
        for (Map.Entry<String, City> city : cities) {
          citiesList.add(city.getValue());
        }
      }
      Collections.sort(citiesList, new CityComparator());
      return citiesList;
    }
    return orignalSortedList;
  }


}
