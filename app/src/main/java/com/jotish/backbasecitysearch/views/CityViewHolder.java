package com.jotish.backbasecitysearch.views;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.jotish.backbasecitysearch.views.CityAdapter.OnCitySelected;
import com.jotish.backbasecitysearch.R;
import com.jotish.backbasecitysearch.utils.Utils;
import com.jotish.backbasecitysearch.models.City;

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

  public void bind(Context context, final City city, final OnCitySelected listener, final String
      searchkey) {
    mCityName.setText(Utils.highlight(context, searchkey, city.name));
    mCountryName.setText(city.country);
    itemView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(final View view) {
        listener.onCitySelected(city);
      }
    });
  }
}
