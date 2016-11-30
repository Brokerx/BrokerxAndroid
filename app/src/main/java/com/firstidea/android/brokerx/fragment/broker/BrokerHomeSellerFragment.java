package com.firstidea.android.brokerx.fragment.broker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.adapter.BrokerHomeEnquiriesAdapter;
import com.firstidea.android.brokerx.fragment.dummy.DummyContent;
import com.firstidea.android.brokerx.http.model.Lead;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class BrokerHomeSellerFragment extends Fragment {
    private ArrayList<Lead> mLeads;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BrokerHomeSellerFragment() {
    }

    public static BrokerHomeSellerFragment newInstance(ArrayList<Lead> leads) {
        BrokerHomeSellerFragment fragment = new BrokerHomeSellerFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(Lead.KEY_LEAD, leads);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mLeads = getArguments().getParcelableArrayList(Lead.KEY_LEAD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
                recyclerView.setLayoutManager(new LinearLayoutManager(context));

            recyclerView.setAdapter(new BrokerHomeEnquiriesAdapter(context, mLeads));
        }
        return view;
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
