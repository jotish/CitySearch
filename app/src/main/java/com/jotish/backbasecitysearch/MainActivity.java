package com.jotish.backbasecitysearch;

import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.jotish.backbasecitysearch.SearchFragment.OnFragmentInteractionListener;

/**
 * Created by jotishsuthar on 12/06/17.
 */

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    SearchFragment searchFragment = SearchFragment.newInstance();
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.add(R.id.fragment_container, searchFragment);
    transaction.commit();
  }

  @Override
  public void onFragmentInteraction(final Uri uri) {

  }
}
