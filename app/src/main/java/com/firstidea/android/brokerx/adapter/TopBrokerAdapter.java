package com.firstidea.android.brokerx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.http.model.User;

import java.util.ArrayList;

/**
 * Created by user on 10/3/2016.
 */

public class TopBrokerAdapter extends RecyclerView.Adapter<TopBrokerAdapter.BrokerViewHolder> {
    private ArrayList<User> mList;
    private Context mContext;
    public class BrokerViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mParentlayout;
        private TextView no_of_deals;
        private TextView mTotal_Charge;
        private TextView mBroker_name;
        public BrokerViewHolder(View itemView) {
            super(itemView);
            mParentlayout = (LinearLayout) itemView.findViewById(R.id.TopBrokerlinear_parent);
            no_of_deals = (TextView) itemView.findViewById(R.id.no_of_deals);
            mTotal_Charge = (TextView) itemView.findViewById(R.id.totalChargeT);
            mBroker_name = (TextView) itemView.findViewById(R.id.brokerNameT);
        }
    }
    public TopBrokerAdapter(Context mContext, ArrayList<User> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }
    @Override
    public TopBrokerAdapter.BrokerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_top_broker_layout, parent, false);
        BrokerViewHolder ViewHolder = new BrokerViewHolder(v);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(TopBrokerAdapter.BrokerViewHolder holder, int position) {
        final User mItem = mList.get(position);
        holder.no_of_deals.setText(mItem.getLeadCount()+"");
        holder.mTotal_Charge.setText(mItem.getLeadAmount()+" RS.");
        holder.mBroker_name.setText(mItem.getFullName());
        holder.mParentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
