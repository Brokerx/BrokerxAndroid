package com.firstidea.android.brokerx.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.firstidea.android.brokerx.fragment.broker.BrokerHomeBuyerFragment;
import com.firstidea.android.brokerx.fragment.broker.BrokerHomeSellerFragment;
import com.firstidea.android.brokerx.http.model.Lead;

import java.util.ArrayList;

/**
 * Created by jwyn on 19/5/16.
 */
public class BrokerHomeViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[] = {"Buyer", "Seller"};
    ArrayList<Lead> buyerLeads, sellerLeads;

    public BrokerHomeViewPagerAdapter(FragmentManager fm, ArrayList<Lead> buyerLeads, ArrayList<Lead> sellerLeads) {
        super(fm);
        this.buyerLeads = buyerLeads;
        this.sellerLeads = sellerLeads;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = BrokerHomeBuyerFragment.newInstance(buyerLeads);
        } else {
            fragment = BrokerHomeSellerFragment.newInstance(sellerLeads);

        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return 2;           // As there are only 3 Tabs
    }


}