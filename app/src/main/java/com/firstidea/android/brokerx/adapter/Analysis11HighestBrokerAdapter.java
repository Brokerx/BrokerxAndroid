package com.firstidea.android.brokerx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.model.AnalysisItem;

import java.util.ArrayList;

/**
 * Created by user on 10/5/2016.
 */

public class Analysis11HighestBrokerAdapter extends RecyclerView.Adapter<Analysis11HighestBrokerAdapter.BrokerViewHolder> {
    private ArrayList<AnalysisItem> mHList;
    private Context mContext;
    private Analysis11HighestBrokerAdapter.OnAnalysisHBCardListener mHListner;



    /**
     * <b>public interface OnCardListener</b>
     * <br>Interface to implement the OnClick on Card</br>
     */
    public interface OnAnalysisHBCardListener {
        void OnCardClick(AnalysisItem item);
    }
    public class BrokerViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mParentlayout;
        private TextView mChemicalName;
        private TextView mTotalNrokerCharges;
        private TextView mBrokerName1;
        private TextView BrokerName2;
        public BrokerViewHolder(View itemView) {
            super(itemView);

            mParentlayout = (LinearLayout) itemView.findViewById(R.id.Top11HBrokerlinear_parent);
            mChemicalName = (TextView) itemView.findViewById(R.id.chemical_nameBH);
            mTotalNrokerCharges = (TextView) itemView.findViewById(R.id.totalChargeBH);
            mBrokerName1 = (TextView) itemView.findViewById(R.id.broker1);
            BrokerName2 = (TextView) itemView.findViewById(R.id.broker2);
        }
    }

    public Analysis11HighestBrokerAdapter(Context mContext, ArrayList<AnalysisItem> mHList, OnAnalysisHBCardListener mHListner) {
        this.mContext = mContext;
        this.mHList = mHList;
        this.mHListner = mHListner;
    }
    @Override
    public Analysis11HighestBrokerAdapter.BrokerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_11highest_broker, parent, false);
        BrokerViewHolder AnalysisViewHolder = new BrokerViewHolder(v);
        return AnalysisViewHolder;
    }

    @Override
    public void onBindViewHolder(Analysis11HighestBrokerAdapter.BrokerViewHolder holder, int position) {
        final AnalysisItem Item = mHList.get(position);
        holder.mChemicalName.setText(Item.getChemical_Name());
        holder.mTotalNrokerCharges.setText(Item.getTotal_BrokerCharage());
        holder.mBrokerName1.setText(Item.getBokerR());
        holder.BrokerName2.setText(Item.getBrokerS());

        holder.mParentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHListner.OnCardClick(Item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mHList.size();
    }


}
