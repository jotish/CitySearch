package com.jotish.backbasecitysearch.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.jotish.backbasecitysearch.views.CityAdapter.OnCitySelected;
import com.jotish.backbasecitysearch.R;
import com.jotish.backbasecitysearch.models.City;
import com.jotish.backbasecitysearch.repo.CityRepository;
import com.jotish.backbasecitysearch.trie.TrieMap;
import com.jotish.backbasecitysearch.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class SearchFragment extends Fragment implements OnCitySelected {


  private OnCitySelectedActionListener mListener;
  private TextView mEmptyView;
  private CityAdapter mCityAdapter;
  private Disposable mTextViewDisposable;
  private final int DEBOUNCE_SEARCH_DELAY = 400;
  private final long INITIAL_EMISSION = 1;
  private Disposable mDisposable;
  private List<City> mOriginalList;
  private TrieMap<City> mSearchTree;
  private View mContentView;
  private View mProgressView;
  private String mSearchKey;

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
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
        LinearLayoutManager.VERTICAL, false);
    recyclerViewPlus.setLayoutManager(layoutManager);
    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
        recyclerViewPlus.getContext(), layoutManager.getOrientation());
    recyclerViewPlus.addItemDecoration(dividerItemDecoration);
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
          public void accept(@NonNull final TextViewTextChangeEvent textViewTextChangeEvent)
              throws Exception {
            String searchKey = textViewTextChangeEvent.text().toString();
            List<City> results = CityRepository.onSearch(mOriginalList, mSearchTree, searchKey);
            mSearchKey = searchKey;
            if (results != null) {
              mCityAdapter.clearCityList();
              mCityAdapter.addAllCities(results,searchKey);
            }
          }
        });

    Observable<List<City>> observable = Observable.fromCallable(new Callable<List<City>>() {
      @Override
      public List<City> call() throws Exception {
        return CityRepository.loadSortedCityList(getActivity(), false);
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
              if (mSearchTree == null) {
                mSearchTree = CityRepository.buildCityTrieMap(cities);
              }
              mProgressView.setVisibility(View.GONE);
              mCityAdapter.addAllCities(cities, mSearchKey);
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

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnCitySelectedActionListener) {
      mListener = (OnCitySelectedActionListener) context;
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
  public void onCitySelected(final City city) {
      mListener.onCitySelected(city);
  }

  public interface OnCitySelectedActionListener {
    void onCitySelected(City city);
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
