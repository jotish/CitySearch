package com.jotish.backbasecitysearch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.jotish.backbasecitysearch.CityAdapter.OnCitySelected;
import com.jotish.backbasecitysearch.models.City;
import com.jotish.backbasecitysearch.views.RecyclerViewPlus;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class SearchFragment extends Fragment implements OnCitySelected {


  private OnFragmentInteractionListener mListener;
  private TextView mEmptyView;
  private CityAdapter mCityAdapter;
  private Disposable mTextViewDisposable;
  private final int DEBOUNCE_SEARCH_DELAY = 400;
  private final long INITIAL_EMISSION = 1;
  private Disposable mDisposable;
 // private List<City> mSearchList;
  private Trie mSearchTree;

  public SearchFragment() {
    // Required empty public constructor
  }

  public static SearchFragment newInstance() {
    SearchFragment fragment = new SearchFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_search, null);
    RecyclerViewPlus recyclerViewPlus = (RecyclerViewPlus)
        view.findViewById(R.id.city_recycler_view);
    mEmptyView = (TextView) view.findViewById(R.id.empty_view);
    mEmptyView.setText(getString(R.string.loading));
    recyclerViewPlus.setEmptyView(mEmptyView);
    recyclerViewPlus.setLayoutManager(new LinearLayoutManager(getContext(),
        LinearLayoutManager.VERTICAL, false));
    mCityAdapter = new CityAdapter(this);
    recyclerViewPlus.setAdapter(mCityAdapter);
    final EditText searchTextView = (EditText) view.findViewById(R.id.searchText);
    mTextViewDisposable = RxTextView.textChangeEvents(searchTextView)
        .debounce(DEBOUNCE_SEARCH_DELAY, TimeUnit.MILLISECONDS) // default Scheduler is Computation
        .observeOn(AndroidSchedulers.mainThread())
        .skip(INITIAL_EMISSION) // ignore the first emission
        .subscribe(new Consumer<TextViewTextChangeEvent>() {
          @Override
          public void accept(@NonNull final TextViewTextChangeEvent textViewTextChangeEvent) throws Exception {
            onSearch(textViewTextChangeEvent.text().toString());
          }
        });

    Observable<List<City>> observable = Observable.fromCallable(new Callable<List<City>>() {
      @Override
      public List<City> call() throws Exception {
         String cityJson = Utils.loadJSONFromAsset(getActivity());
         Gson gson = new Gson();
         Type listType = new TypeToken<List<City>>(){}.getType();
         ArrayList<City> cities  = gson.fromJson(cityJson, listType);
         if (mSearchTree == null) {
           mSearchTree = new Trie();
         }
         for (City city : cities) {
            mSearchTree.insert();
         }
         Collections.sort(cities, new CityComparator());
        return cities;
      }
    });
    observable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<City>>() {
          @Override
          public void onSubscribe(@NonNull final Disposable d) {
            mDisposable = d;
          }

          @Override
          public void onNext(@NonNull final List<City> cities) {
            if (Utils.isActivityAlive(getActivity())) {
              if (cities == null || cities.size() == 0) {
                mEmptyView.setText(R.string.empty_state_city);
              } else {
                mCityAdapter.addAllCities(cities);
              }
            }
          }

          @Override
          public void onError(@NonNull final Throwable e) {

          }

          @Override
          public void onComplete() {

          }
        });

    return view;
  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override
  public void onCitySelected(final City city) {

  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {

    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }

  public boolean onSearch(String searchKey) {
    if (mSearchList == null || mSearchList.size() <= 0) {

      return false;
    }
    City searchCity = new City();
    searchCity.name = searchKey;
    Collections.binarySearch(mSearchList, searchCity);
    return true;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (mTextViewDisposable != null && !mTextViewDisposable.isDisposed()) {
       mTextViewDisposable.dispose();
    }

    if (mDisposable != null && !mDisposable.isDisposed()) {
      mDisposable.dispose();
    }
  }
}
