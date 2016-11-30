package com.firstidea.android.brokerx.fragment.broker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firstidea.android.brokerx.BrokerHomeActivity;
import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.adapter.BrokerHomeViewPagerAdapter;
import com.firstidea.android.brokerx.enums.LeadType;
import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.widget.AppProgressDialog;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link BrokerHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrokerHomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BrokerHomeViewPagerAdapter viewPagerAdapter;
    private Activity mContext;

    public BrokerHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BrokerHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrokerHomeFragment newInstance() {
        BrokerHomeFragment fragment = new BrokerHomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_broker_home, container, false);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        getBrokerLeads();

        return rootView;
    }

    private void getBrokerLeads() {
        final Dialog dialog = AppProgressDialog.show(mContext);
        User user = User.getSavedUser(mContext);
        ObjectFactory.getInstance().getLeadServiceInstance().getBrokerLeads(user.getUserID(), null, null, null, null, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if(messageDTO.isSuccess()) {
                    ArrayList<Lead> leads = Lead.createListFromJson(messageDTO.getData());
                    sortLeadsAndInitViews(leads);
                } else {
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void sortLeadsAndInitViews(ArrayList<Lead> leads) {
        ArrayList<Lead> buyerLeads = new ArrayList<>();
        ArrayList<Lead> sellerLeads = new ArrayList<>();
        for(Lead lead: leads) {
            if(lead.getType().equals(LeadType.BUYER.getType())) {
                buyerLeads.add(lead);
            } else {
                sellerLeads.add(lead);
            }
        }
        viewPagerAdapter = new BrokerHomeViewPagerAdapter(((BrokerHomeActivity)mContext).getSupportFragmentManager(), buyerLeads, sellerLeads);
        viewPager.setAdapter(viewPagerAdapter);
        final TabLayout.Tab buyerTab = tabLayout.newTab();
        final TabLayout.Tab sellerTab = tabLayout.newTab();
        tabLayout.addTab(buyerTab, 0);
        tabLayout.addTab(sellerTab, 1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
