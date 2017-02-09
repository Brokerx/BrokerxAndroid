package com.firstidea.android.brokerx.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firstidea.android.brokerx.MyHistoryDetailsActivity;
import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.enums.LeadType;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.model.TopBroketItem;

import java.util.ArrayList;

/**
 * Created by user on 10/7/2016.
 */

public class Top11BuyerSellerAdapter extends RecyclerView.Adapter<Top11BuyerSellerAdapter.BrokerViewHolder> {
    private ArrayList<Lead> mList;
    private Context mContext;
    private Top11BuyerSellerAdapter.OnCardListener mListner;
    private String leadType;


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
        private TextView userName;
        private TextView chemical_name;
        private TextView totalBrokerage;
        public BrokerViewHolder(View itemView) {
            super(itemView);

            mParentlayout = (LinearLayout) itemView.findViewById(R.id.TopSellerlinear_parent);
            mBasic_Charge = (TextView) itemView.findViewById(R.id.basicChargeS);
            mTotal_Charge = (TextView) itemView.findViewById(R.id.totalChargeS);
            userName = (TextView) itemView.findViewById(R.id.userName);
            chemical_name = (TextView) itemView.findViewById(R.id.chemical_name);
            totalBrokerage = (TextView) itemView.findViewById(R.id.totalBrokerage);
        }
    }
    public Top11BuyerSellerAdapter(Context mContext, ArrayList<Lead> mList,String leadType) {
        this.mContext = mContext;
        this.mList = mList;
        this.mListner = mListner;
        this.leadType = leadType;
    }
    @Override
    public Top11BuyerSellerAdapter.BrokerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_top_11seller_layout, parent, false);
        BrokerViewHolder ViewHolder = new BrokerViewHolder(v);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(Top11BuyerSellerAdapter.BrokerViewHolder holder, int position) {
        final Lead mItem = mList.get(position);
        holder.mBasic_Charge.setText(mItem.getBasicPrice()+" Rs.");
        float basicPriceAmt = mItem.getBasicPrice() * mItem.getQty();
        float excisePriceAmt = mItem.getExciseDuty() * mItem.getQty();
        float totalAmt = basicPriceAmt + excisePriceAmt + mItem.getTransportCharges() + mItem.getMiscCharges();
        holder.mTotal_Charge.setText(totalAmt+" Rs.");
        String userName = "";
        float totalBrokerageAmount;
        if(leadType.equals(LeadType.BUYER.getType())) {
            if(mItem.getType().equals(LeadType.BUYER.getType())) {
                userName = mItem.getCreatedUser().getFullName();
            } else {
                userName = mItem.getAssignedToUser().getFullName();
            }
            totalBrokerageAmount = mItem.getBuyerBrokerage();
        } else {
            if(mItem.getType().equals(LeadType.SELLER.getType())) {
                userName = mItem.getCreatedUser().getFullName();
            } else {
                userName = mItem.getAssignedToUser().getFullName();
            }
            totalBrokerageAmount = mItem.getSellerBrokerage();
        }
        holder.totalBrokerage.setText(totalBrokerageAmount+" Rs");
        holder.userName.setText(userName);
        holder.mParentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyHistoryDetailsActivity.class);
                intent.putExtra(Lead.KEY_LEAD, mItem);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return  mList.size();
    }


}
