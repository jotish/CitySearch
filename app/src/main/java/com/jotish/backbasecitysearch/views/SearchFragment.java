package com.jotish.backbasecitysearch.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.jotish.backbasecitysearch.R;
import com.jotish.backbasecitysearch.models.City;
import com.jotish.backbasecitysearch.models.Data;
import com.jotish.backbasecitysearch.repo.CityDataLoader;
import com.jotish.backbasecitysearch.repo.CityRepository;
import com.jotish.backbasecitysearch.trie.TrieMap;
import com.jotish.backbasecitysearch.views.CityAdapter.OnCitySelected;
import java.util.List;

public class SearchFragment extends Fragment implements OnCitySelected,LoaderCallbacks<Data> {


  private OnCitySelectedActionListener mListener;
  private TextView mEmptyView;
  private CityAdapter mCityAdapter;
  private List<City> mOriginalList;
  private TrieMap<City> mSearchTree;
  private View mContentView;
  private View mProgressView;
  private String mSearchKey;
  private String mSearchTypingString;
  private Handler mHandler;
  private final int SEARCH_DEBOUNCE = 600; //milliseonds
  private final int LOADER_ID = 99932;

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
    mHandler = new Handler();
  }

  private Runnable mSearchRunnable = new Runnable() {
    @Override
    public void run() {
      Bundle bundle = null;
      if(mSearchTypingString != null) {
        String constraint = mSearchTypingString.toString().trim();
        List<City> results = CityRepository.onSearch(mOriginalList, mSearchTree, constraint);
        mSearchKey = constraint;
        if (results != null) {
          mCityAdapter.clearCityList();
          mCityAdapter.addAllCities(results,constraint);
        }
      }
    }
  };

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
    TextWatcher textWatcher = new TextWatcher() {

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        mSearchTypingString = s.toString().trim();

        if(mSearchTypingString == null) {
          if(mSearchKey != null ) {
            mHandler.removeCallbacks(mSearchRunnable);
            mHandler.postDelayed(mSearchRunnable, SEARCH_DEBOUNCE);
          }
        } else {
          if(!mSearchTypingString.equals(mSearchKey)){
            mHandler.removeCallbacks(mSearchRunnable);
            mHandler.postDelayed(mSearchRunnable, SEARCH_DEBOUNCE);
          }
        }
      }
    };
    searchTextView.addTextChangedListener(textWatcher);
    getLoaderManager().restartLoader(LOADER_ID, null, this);
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

  @Override
  public Loader<Data> onCreateLoader(final int id, final Bundle args) {
    return new CityDataLoader(getActivity());
  }

  @Override
  public void onLoadFinished(final Loader<Data> loader, final Data cityData) {
      mOriginalList = cityData.getCities();
      mSearchTree = cityData.getSearchTree();
      mProgressView.setVisibility(View.GONE);
      mCityAdapter.addAllCities(mOriginalList, mSearchKey);
      mContentView.setVisibility(View.VISIBLE);
  }

  @Override
  public void onLoaderReset(final Loader<Data> loader) {
      mCityAdapter.clearCityList();
  }

  public interface OnCitySelectedActionListener {
    void onCitySelected(City city);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (mHandler != null) {
      mHandler.removeCallbacksAndMessages(null);
    }
    getLoaderManager().destroyLoader(LOADER_ID);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (mHandler != null) {
      mHandler.removeCallbacksAndMessages(null);
    }
    getLoaderManager().destroyLoader(LOADER_ID);
  }
}
