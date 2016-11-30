package com.firstidea.android.brokerx.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.firstidea.android.brokerx.fragment.ActivityFragment;
import com.firstidea.android.brokerx.fragment.BuyerFragment;
import com.firstidea.android.brokerx.fragment.CircleFragment;
import com.firstidea.android.brokerx.fragment.PendingFragment;
import com.firstidea.android.brokerx.fragment.SellerFragment;

/**
 * Created by jwyn on 19/5/16.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new CircleFragment();
            return fragment;
        } if (position == 1) {
            fragment = new BuyerFragment();
            return fragment;
        } if (position == 2) {
            fragment = new SellerFragment();
            return fragment;
        } if (position == 3) {
            fragment = new PendingFragment();
            return fragment;
        } else {
            fragment = new ActivityFragment();
            return fragment;
        }
        // Which Fragment should be dislpayed by the viewpager for the given position
        // In my case we are showing up only one fragment in all the three tabs so we are
        // not worrying about the position and just returning the TabFragment
    }

    @Override
    public int getCount() {
        return 5;           // As there are only 3 Tabs
    }


}