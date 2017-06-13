package com.jotish.backbasecitysearch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jotish.backbasecitysearch.models.City;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CityMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CityMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CityMapFragment extends Fragment implements OnMapReadyCallback {


  private GoogleMap mMap;
  public static final String CITY_ARG = "city_arg";
  private City mSelectedCity;


  private OnFragmentInteractionListener mListener;

  public CityMapFragment() {
    // Required empty public constructor
  }


  public static CityMapFragment newInstance(City city) {
    CityMapFragment fragment = new CityMapFragment();
    Bundle args = new Bundle();
    args.putParcelable(CITY_ARG, city);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mSelectedCity = getArguments().getParcelable(CITY_ARG);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    setHasOptionsMenu(true);
    return inflater.inflate(R.layout.fragment_city_map, container, false);
  }

  @Override
  public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initMap();
    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mSelectedCity.name);
    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }


  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnCitySelectedActionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override
  public void onMapReady(final GoogleMap googleMap) {
    mMap = googleMap;

    LatLng sydney = new LatLng(mSelectedCity.coord.lat, mSelectedCity.coord.lon);
    CameraUpdate center=
        CameraUpdateFactory.newLatLng(sydney);
    CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
    mMap.addMarker(new MarkerOptions().position(sydney).title(mSelectedCity.name));
    mMap.moveCamera(center);
    mMap.animateCamera(zoom);
  }

  public interface OnFragmentInteractionListener {
    void onBackPressedFragment();
  }
  private void initMap() {
    SupportMapFragment mapFragment =
        (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
    if (mapFragment == null) {
      mapFragment = SupportMapFragment.newInstance();
      getChildFragmentManager()
          .beginTransaction()
          .add(R.id.map, mapFragment)
          .commitAllowingStateLoss();
    }
    mapFragment.getMapAsync(this);
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        SupportMapFragment mapFragment =
            (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
          getChildFragmentManager().beginTransaction().remove(mapFragment);
        }
        mListener.onBackPressedFragment();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
