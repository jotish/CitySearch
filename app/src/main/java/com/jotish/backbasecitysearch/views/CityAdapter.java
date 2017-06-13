package com.jotish.backbasecitysearch.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.jotish.backbasecitysearch.R;
import com.jotish.backbasecitysearch.models.City;
import com.jotish.backbasecitysearch.views.CityViewHolder;
import com.jotish.backbasecitysearch.views.RecyclerViewPlus;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by jotishsuthar on 13/06/17.
 */

public class CityAdapter extends RecyclerViewPlus.Adapter<CityViewHolder>{

  public ArrayList<City> mCities;
  private OnCitySelected mCallBack;
  private String mSearchKey;


  public CityAdapter(OnCitySelected callBack) {
    mCallBack = callBack;
    mCities = new ArrayList<>();
  }

  public interface OnCitySelected {
    void onCitySelected(City city);
  }


  @Override
  public CityViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
    return new CityViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final CityViewHolder holder, final int position) {
      City city = mCities.get(position);
      Context context = holder.itemView.getContext();
      holder.bind(context, city, mCallBack, mSearchKey);
  }


  @Override
  public int getItemCount() {
    return mCities == null ? 0 : mCities.size();
  }


  public void addAllCities(List<City> cityList, String searchKey){
    mCities.addAll(cityList);
    mSearchKey = searchKey;
    notifyDataSetChanged();
  }

  public void clearCityList(){
    mCities.clear();
    notifyDataSetChanged();
  }
}
