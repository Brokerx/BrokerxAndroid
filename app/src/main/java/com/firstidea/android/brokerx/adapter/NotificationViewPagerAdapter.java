package com.firstidea.android.brokerx.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.firstidea.android.brokerx.fragment.NotificationFragment;
import com.firstidea.android.brokerx.fragment.broker.BrokerHomeBuyerFragment;
import com.firstidea.android.brokerx.fragment.broker.BrokerHomeSellerFragment;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.NotificationListDTO;

import java.util.ArrayList;

/**
 * Created by jwyn on 19/5/16.
 */
public class NotificationViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[] = {"Buyer", "Seller","Other"};
    ArrayList<NotificationListDTO> buyerNotificationListDTOs, sellerNotificationListDTOs, historyNotificationListDTOs;

    public NotificationViewPagerAdapter(FragmentManager fm, ArrayList<NotificationListDTO> buyerNotificationListDTOs,
                                        ArrayList<NotificationListDTO> sellerNotificationListDTOs,  ArrayList<NotificationListDTO> historyNotificationListDTOs) {
        super(fm);
        this.buyerNotificationListDTOs = buyerNotificationListDTOs;
        this.sellerNotificationListDTOs = sellerNotificationListDTOs;
        this.historyNotificationListDTOs = historyNotificationListDTOs;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = NotificationFragment.newInstance(buyerNotificationListDTOs);
        } else if (position == 1) {
            fragment = NotificationFragment.newInstance(sellerNotificationListDTOs);
        } else {
            fragment = NotificationFragment.newInstance(historyNotificationListDTOs);
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return 3;           // As there are only 3 Tabs
    }


}