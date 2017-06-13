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
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.jotish.backbasecitysearch.CityAdapter.OnCitySelected;
import com.jotish.backbasecitysearch.models.City;
import com.jotish.backbasecitysearch.trie.TrieFactory;
import com.jotish.backbasecitysearch.trie.TrieMap;
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
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
 private  List<City> mOriginalList;
  private TrieMap<City> mSearchTree;
  private View mContentView;
  private View mProgressView;

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
    mEmptyView.setText(getString(R.string.empty_state_city));
    recyclerViewPlus.setEmptyView(mEmptyView);
    recyclerViewPlus.setLayoutManager(new LinearLayoutManager(getContext(),
        LinearLayoutManager.VERTICAL, false));
    mCityAdapter = new CityAdapter(this);
    mProgressView = view.findViewById(R.id.progress_container);
    mContentView = view.findViewById(R.id.content);
    mProgressView.setVisibility(View.VISIBLE);
    recyclerViewPlus.setAdapter(mCityAdapter);
    final EditText searchTextView = (EditText) view.findViewById(R.id.searchText);
    mTextViewDisposable = RxTextView.textChangeEvents(searchTextView)
        .debounce(DEBOUNCE_SEARCH_DELAY, TimeUnit.MILLISECONDS) // default Scheduler is Computation
        .observeOn(AndroidSchedulers.mainThread())
        .skip(INITIAL_EMISSION) // ignore the first emission
        .subscribe(new Consumer<TextViewTextChangeEvent>() {
          @Override
          public void accept(@NonNull final TextViewTextChangeEvent textViewTextChangeEvent) throws Exception {
             List<City> results = onSearch(textViewTextChangeEvent.text().toString());
             if (results != null) {
               mCityAdapter.clearCityList();
               mCityAdapter.addAllCities(results);
             }
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
             mSearchTree =  TrieFactory.createTrieMapOptimizedForMemory();
         }
         for (City city : cities) {
            mSearchTree.put(city.name.toLowerCase(Locale.getDefault()), city);
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
            mOriginalList = cities;
            if (Utils.isActivityAlive(getActivity())) {
              mProgressView.setVisibility(View.GONE);
               mCityAdapter.addAllCities(cities);
              mContentView.setVisibility(View.VISIBLE);
            }
          }

          @Override
          public void onError(@NonNull final Throwable e) {
            Toast.makeText(getActivity(), getString(R.string.failed_loading_data),
                Toast.LENGTH_SHORT);
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

  public List<City> onSearch(String searchKey) {
    if (mSearchTree == null) {
         return null;
    }

    if(Utils.isNotEmptyString(searchKey)) {
      searchKey = searchKey.toLowerCase(Locale.getDefault());
      ArrayList<City> citiesList = new ArrayList<>();
      TrieMap<City> result = mSearchTree.getSubTrie(searchKey);
      if (result != null) {
        Set<Map.Entry<String, City>> cities = result.entrySet();
        for (Map.Entry<String, City> city : cities) {
          citiesList.add(city.getValue());
        }
      }
      Collections.sort(citiesList, new CityComparator());
      return citiesList;
    }

    return mOriginalList;
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
