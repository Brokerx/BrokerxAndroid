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
import com.firstidea.android.brokerx.model.MyHistroyItem;

import java.util.ArrayList;

/**
 * Created by user on 9/22/2016.
 */

public class MyHistoryAdapter extends RecyclerView.Adapter<MyHistoryAdapter.HistoryViwHolder> {
    private ArrayList<MyHistroyItem> mHList;
    private Context mContext;
    private OnHostoryCardListener mHListner;
    private TextView mViewDetails;


    /**
     * <b>public interface OnCardListener</b>
     * <br>Interface to implement the OnClick on Card</br>
     */
    public interface OnHostoryCardListener {
        void OnCardClick(MyHistroyItem item);
    }

    public class HistoryViwHolder extends RecyclerView.ViewHolder {

        public LinearLayout mParentlayout;
        private TextView mChemicalName;
        private TextView mInvoice_no;
        private TextView mBrokerage;
        private TextView mDate;

        public HistoryViwHolder(View itemView) {
            super(itemView);

            mParentlayout = (LinearLayout) itemView.findViewById(R.id.Historylinear_parent);
            mChemicalName = (TextView) itemView.findViewById(R.id.chemicalName);
            mInvoice_no = (TextView) itemView.findViewById(R.id.InvoiceNo);
            mBrokerage = (TextView) itemView.findViewById(R.id.BrokerageH);
            mDate = (TextView) itemView.findViewById(R.id.DateHis);
            mViewDetails = (TextView)itemView.findViewById(R.id.view_details);
            mViewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ViewHistory = new Intent(mContext,MyHistoryDetailsActivity.class);
                    mContext.startActivity(ViewHistory);
                }
            });
        }
    }
    public MyHistoryAdapter(Context mContext, ArrayList<MyHistroyItem> mList, OnHostoryCardListener mListner) {
        this.mContext = mContext;
        this.mHList = mList;
        this.mHListner = mListner;
    }
    @Override
    public HistoryViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.history_card_layout, parent, false);
        HistoryViwHolder HistoryViewHolder = new HistoryViwHolder(v);
        return HistoryViewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryViwHolder holder, int position) {

        final MyHistroyItem Item = mHList.get(position);
        holder.mChemicalName.setText(Item.getChemical_Name());
        holder.mInvoice_no.setText(Item.getInvoiceNo());
        holder.mBrokerage.setText(Item.getBrokerage());
        holder.mDate.setText(Item.getDate());

        holder.mParentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHListner.OnCardClick(Item);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mHList.size();
    }


}
