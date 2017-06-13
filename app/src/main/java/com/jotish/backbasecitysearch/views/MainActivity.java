package com.jotish.backbasecitysearch.views;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.jotish.backbasecitysearch.R;
import com.jotish.backbasecitysearch.models.City;
import com.jotish.backbasecitysearch.utils.Utils;
import com.jotish.backbasecitysearch.views.CityMapFragment.OnFragmentInteractionListener;
import com.jotish.backbasecitysearch.views.SearchFragment.OnCitySelectedActionListener;

/**
 * Created by jotishsuthar on 12/06/17.
 */

public class MainActivity extends AppCompatActivity implements OnCitySelectedActionListener,
    OnFragmentInteractionListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    loadSearchFragment();
  }

  private void loadSearchFragment() {
    SearchFragment f = (SearchFragment)
        getSupportFragmentManager().findFragmentByTag(SearchFragment.class.getName());
    if(f == null) {
      SearchFragment searchFragment = SearchFragment.newInstance();
      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      transaction.add(R.id.fragment_container, searchFragment);
      transaction.commit();
    }
  }

  @Override
  public void onCitySelected(final City city) {
    Utils.hideKeyboard(this);
    final FragmentTransaction transaction = getSupportFragmentManager()
        .beginTransaction();
    CityMapFragment fragment = CityMapFragment.newInstance(city);
    transaction.add(R.id.fragment_container, fragment);
    transaction.addToBackStack(fragment.getClass().getName());
    transaction.commit();
  }

  @Override
  public void onBackPressedFragment() {
    handleBackPress();
  }
  private void handleBackPress() {
    getSupportActionBar().setTitle(getTitle());
    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    CityMapFragment fragment = (CityMapFragment) getSupportFragmentManager().
        findFragmentByTag(CityMapFragment.class.getName());
    if (fragment != null) {
      getSupportFragmentManager().beginTransaction().remove(fragment);
    }
    if (getSupportFragmentManager().getBackStackEntryCount() > 0){
      getSupportFragmentManager().popBackStack();
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public void onBackPressed() {
    handleBackPress();
  }
}
