package com.firstidea.android.brokerx.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.firstidea.android.brokerx.fragment.ActivityFragment;
import com.firstidea.android.brokerx.fragment.BuyerFragment;
import com.firstidea.android.brokerx.fragment.CircleFragment;
import com.firstidea.android.brokerx.fragment.LeadHistoryFragment;
import com.firstidea.android.brokerx.fragment.PendingFragment;
import com.firstidea.android.brokerx.fragment.SellerFragment;

/**
 * Created by jwyn on 19/5/16.
 */
public class LeadHistoryPagerAdapter extends FragmentStatePagerAdapter {

    public LeadHistoryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        String details = " Position "+position + "";
        String rate = "10";
        Fragment fragment = LeadHistoryFragment.newInstance(details, rate);

        return fragment;
        // Which Fragment should be dislpayed by the viewpager for the given position
        // In my case we are showing up only one fragment in all the three tabs so we are
        // not worrying about the position and just returning the TabFragment
    }

    @Override
    public int getCount() {
        return 5;           // As there are only 3 Tabs
    }


}