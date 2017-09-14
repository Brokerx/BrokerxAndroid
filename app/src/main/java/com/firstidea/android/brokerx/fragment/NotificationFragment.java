package com.firstidea.android.brokerx.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.adapter.NewNotificationADapter;
import com.firstidea.android.brokerx.http.model.Notification;
import com.firstidea.android.brokerx.http.model.NotificationListDTO;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NOTIFICATIONS = "Notifications";

    // TODO: Rename and change types of parameters
    private ArrayList<NotificationListDTO> mNotifications;


    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mNotifications Parameter 1.
     * @return A new instance of fragment NotificationFragment.
     */
    public static NotificationFragment newInstance(ArrayList<NotificationListDTO> mNotifications) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_NOTIFICATIONS, mNotifications);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNotifications = getArguments().getParcelableArrayList(ARG_NOTIFICATIONS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.notification_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        NewNotificationADapter mAdapter = new NewNotificationADapter(getActivity(), mNotifications);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

}
