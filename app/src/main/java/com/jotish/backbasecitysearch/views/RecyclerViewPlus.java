package com.jotish.backbasecitysearch.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jotishsuthar on 13/06/17.
 */


public class RecyclerViewPlus extends RecyclerView {

  private final AdapterDataObserver mObserver = new AdapterDataObserver() {
    @Override
    public void onChanged() {
      checkIfEmpty();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
      checkIfEmpty();
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
      checkIfEmpty();
    }
  };

  private View mEmptyView;

  public RecyclerViewPlus(Context context) {
    super(context);
  }

  public RecyclerViewPlus(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public RecyclerViewPlus(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  private void checkIfEmpty() {
    if (mEmptyView != null && getAdapter() != null) {
      mEmptyView.setVisibility(getAdapter().getItemCount() > 0 ? GONE : VISIBLE);
    }
  }

  @Override
  public void setAdapter(Adapter adapter) {
    final Adapter oldAdapter = getAdapter();
    if (oldAdapter != null) {
      oldAdapter.unregisterAdapterDataObserver(mObserver);
    }
    super.setAdapter(adapter);
    if (adapter != null) {
      adapter.registerAdapterDataObserver(mObserver);
    }
    checkIfEmpty();
  }

  @Override
  public void setVisibility(int visibility) {
    super.setVisibility(visibility);
    if (null != mEmptyView && (visibility == GONE || visibility == INVISIBLE)) {
      mEmptyView.setVisibility(GONE);
    } else {
      checkIfEmpty();
    }
  }

  public void setEmptyView(View emptyView) {
    this.mEmptyView = emptyView;
    checkIfEmpty();
  }
}