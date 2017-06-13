package com.jotish.backbasecitysearch.repo;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import com.jotish.backbasecitysearch.models.City;
import java.util.List;

/**
 * Created by jotishsuthar on 13/06/17.
 */

public class CityDataLoader extends AsyncTaskLoader<List<City>> {

  private List<City> mCities;

  public CityDataLoader(final Context context) {
    super(context);
  }

  @Override
  public List<City> loadInBackground() {
    return CityRepository.loadSortedCityList(getContext(), false);
  }

  @Override
  protected void onStartLoading() {
    if (mCities != null) {
      deliverResult(mCities);
    }

    if (takeContentChanged() || mCities == null) {
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
    if (mCities != null) {
      mCities = null;
    }
  }
}
