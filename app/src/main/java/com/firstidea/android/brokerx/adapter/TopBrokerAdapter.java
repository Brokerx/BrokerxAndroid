package com.firstidea.android.brokerx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.TopBrokersActivity;
import com.firstidea.android.brokerx.model.MyHistroyItem;
import com.firstidea.android.brokerx.model.TopBroketItem;

import java.util.ArrayList;

/**
 * Created by user on 10/3/2016.
 */

public class TopBrokerAdapter extends RecyclerView.Adapter<TopBrokerAdapter.BrokerViewHolder> {
    private ArrayList<TopBroketItem> mList;
    private Context mContext;
    private OnCardListener mListner;



    /**
     * <b>public interface OnCardListener</b>
     * <br>Interface to implement the OnClick on Card</br>
     */
    public interface OnCardListener {
        void OnCardClick(TopBroketItem item);
    }


    public class BrokerViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mParentlayout;
        private TextView mBasic_Charge;
        private TextView mTotal_Charge;
        private TextView mBroker_name;
        public BrokerViewHolder(View itemView) {
            super(itemView);
            mParentlayout = (LinearLayout) itemView.findViewById(R.id.TopBrokerlinear_parent);
            mBasic_Charge = (TextView) itemView.findViewById(R.id.basicChargeT);
            mTotal_Charge = (TextView) itemView.findViewById(R.id.totalChargeT);
            mBroker_name = (TextView) itemView.findViewById(R.id.brokerNameT);
        }
    }
    public TopBrokerAdapter(Context mContext, ArrayList<TopBroketItem> mList, TopBrokerAdapter.OnCardListener mListner) {
        this.mContext = mContext;
        this.mList = mList;
        this.mListner = mListner;
    }
    @Override
    public TopBrokerAdapter.BrokerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_top_broker_layout, parent, false);
        BrokerViewHolder ViewHolder = new BrokerViewHolder(v);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(TopBrokerAdapter.BrokerViewHolder holder, int position) {
        final TopBroketItem mItem = mList.get(position);
        holder.mBasic_Charge.setText(mItem.getBasic_charge());
        holder.mTotal_Charge.setText(mItem.getTotal_charge());
        holder.mBroker_name.setText(mItem.getBroker_name());
        holder.mParentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListner.OnCardClick(mItem);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
