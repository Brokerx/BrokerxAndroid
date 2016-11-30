package com.firstidea.android.brokerx.widget;

import android.support.v7.widget.GridLayoutManager;

/**
 * Created by Govind on 03-Sep-16.
 */
public class HeaderSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private final HeaderRecyclerViewAdapter adapter;
    private final GridLayoutManager layoutManager;

    public HeaderSpanSizeLookup(HeaderRecyclerViewAdapter adapter, GridLayoutManager layoutManager) {
        this.adapter = adapter;
        this.layoutManager = layoutManager;
    }

    @Override public int getSpanSize(int position) {
        boolean isHeaderOrFooter =
                adapter.isHeaderPosition(position) || adapter.isFooterPosition(position);
        return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
    }
}
