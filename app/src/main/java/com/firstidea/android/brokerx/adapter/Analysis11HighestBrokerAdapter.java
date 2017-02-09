package com.firstidea.android.brokerx.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firstidea.android.brokerx.MyHistoryActivity;
import com.firstidea.android.brokerx.MyHistoryDetailsActivity;
import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.enums.LeadType;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.model.AnalysisItem;

import java.util.ArrayList;

/**
 * Created by user on 10/5/2016.
 */

public class Analysis11HighestBrokerAdapter extends RecyclerView.Adapter<Analysis11HighestBrokerAdapter.BrokerViewHolder> {
    private ArrayList<Lead> mHList;
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
        private TextView buyerBrokerage;
        private TextView sellerBrokerage;
        private TextView buyerName;
        private TextView sellerName;
        public BrokerViewHolder(View itemView) {
            super(itemView);

            mParentlayout = (LinearLayout) itemView.findViewById(R.id.Top11HBrokerlinear_parent);
            mChemicalName = (TextView) itemView.findViewById(R.id.chemical_nameBH);
            mTotalNrokerCharges = (TextView) itemView.findViewById(R.id.totalChargeBH);
            buyerBrokerage = (TextView) itemView.findViewById(R.id.buyer_brokerage);
            sellerBrokerage = (TextView) itemView.findViewById(R.id.seller_brokerage);
            buyerName = (TextView) itemView.findViewById(R.id.buyer_name);
            sellerName = (TextView) itemView.findViewById(R.id.seller_name);
        }
    }

    public Analysis11HighestBrokerAdapter(Context mContext, ArrayList<Lead> mHList, OnAnalysisHBCardListener mHListner) {
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
        final Lead lead = mHList.get(position);
        holder.mChemicalName.setText(lead.getItemName());
        float buyerBrokerage = lead.getBuyerBrokerage();
        float sellerBrokerage = lead.getSellerBrokerage();
        float totalBrokerage = buyerBrokerage+sellerBrokerage;
        holder.mTotalNrokerCharges.setText(totalBrokerage+" Rs.");
        String buyerName = "", sellerName = "";
        if(lead.getType().equals(LeadType.BUYER.getType())) {
            buyerName = lead.getCreatedUser().getFullName();
            sellerName = lead.getAssignedToUser().getFullName();
        } else {
            sellerName = lead.getCreatedUser().getFullName();
            buyerName = lead.getAssignedToUser().getFullName();
        }
        buyerName += "\n(Buyer)";
        sellerName += "\n(Seller)";
        holder.buyerName.setText(buyerName);
        holder.sellerName.setText(sellerName);
        holder.buyerBrokerage.setText(buyerBrokerage+" Rs.");
        holder.sellerBrokerage.setText(sellerBrokerage+" Rs.");

        holder.mParentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyHistoryDetailsActivity.class);
                intent.putExtra(Lead.KEY_LEAD, lead);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mHList.size();
    }


}
