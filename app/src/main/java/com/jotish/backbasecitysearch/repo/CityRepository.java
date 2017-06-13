package com.jotish.backbasecitysearch.repo;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotish.backbasecitysearch.models.City;
import com.jotish.backbasecitysearch.trie.TrieFactory;
import com.jotish.backbasecitysearch.trie.TrieMap;
import com.jotish.backbasecitysearch.utils.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
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
    // Search prefixes using the Perfix Search Tree
    if (Utils.isNotEmptyString(searchKey)) {
      searchKey = searchKey.toLowerCase(Locale.getDefault()).trim();
      ArrayList<City> citiesList = new ArrayList<>();
      TrieMap<City> result = searchTree.getSubTrie(searchKey);
      if (result != null) {
        Set<Entry<String, City>> cities = result.entrySet();
        for (Map.Entry<String, City> city : cities) {
          citiesList.add(city.getValue());
        }
      }
      sortList(citiesList);
      return citiesList;
    }
    // return the original sorted list if key is empty
    return orignalSortedList;
  }

  private static String loadCityJSONFromAsset(Context context, boolean small) {
    String json = null;
    try {
      String file = (small) ? "city.list-short.json": "city.list.json";
      InputStream is = context.getAssets().open(file);
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

  public static void sortList(List<City> cities) {
    Collections.sort(cities, new CityComparator());
  }

  public static List<City> parseJson(String cityJson) {
    Gson gson = new Gson();
    Type listType = new TypeToken<List<City>>() {
    }.getType();
    return gson.fromJson(cityJson, listType);
  }
  public static List<City> loadCityList(Context context, boolean smallSet) {
    String cityJson = loadCityJSONFromAsset(context, smallSet);
    return parseJson(cityJson);
  }

  public static List<City> loadSortedCityList(Context context, boolean smallSet) {
    List<City> cities = loadCityList(context, smallSet);
    sortList(cities);
    return cities;
  }

  public static TrieMap<City> buildCityTrieMap(List<City> cities) {
    TrieMap<City> cityTrieMap = TrieFactory.createTrieMapOptimizedForMemory();
    for (City city : cities) {
      cityTrieMap.put(city.name.toLowerCase(Locale.getDefault()), city);
    }
    return cityTrieMap;
  }


}
