package com.jotish.backbasecitysearch.views;

import android.view.View;
import android.widget.TextView;
import com.jotish.backbasecitysearch.R;

/**
 * Created by jotishsuthar on 13/06/17.
 */

public class CityViewHolder extends RecyclerViewPlus.ViewHolder {

  public TextView mCityName;
  public TextView mCountryName;
  public CityViewHolder(final View itemView) {
    super(itemView);
    mCityName = (TextView) itemView.findViewById(R.id.city_name);
    mCountryName = (TextView) itemView.findViewById(R.id.country_name);
  }
}
