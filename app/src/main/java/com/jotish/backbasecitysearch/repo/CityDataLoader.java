package com.jotish.backbasecitysearch.repo;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import com.jotish.backbasecitysearch.models.City;
import com.jotish.backbasecitysearch.models.Data;
import com.jotish.backbasecitysearch.trie.TrieMap;
import java.util.List;

/**
 * Created by jotishsuthar on 13/06/17.
 */

public class CityDataLoader extends AsyncTaskLoader<Data> {

  private Data mCityData;

  public CityDataLoader(final Context context) {
    super(context);
  }

  @Override
  public Data loadInBackground() {
    List<City> cities =  CityRepository.loadSortedCityList(getContext(), false);
    TrieMap<City> searchTree = CityRepository.buildCityTrieMap(cities);
    return new Data(cities, searchTree) ;
  }

  @Override
  protected void onStartLoading() {
    if (mCityData != null) {
      deliverResult(mCityData);
    }

    if (takeContentChanged() || mCityData == null) {
      forceLoad();
    }
  }
  @Override
  protected void onStopLoading() {
    cancelLoad();
  }

  @Override
  protected void onReset() {
    onStopLoading();
    if (mCityData != null) {
      mCityData = null;
    }
  }
}
