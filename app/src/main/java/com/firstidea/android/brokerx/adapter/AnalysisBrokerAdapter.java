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

public class AnalysisBrokerAdapter extends RecyclerView.Adapter<AnalysisBrokerAdapter.AnalysisBrokerViewHolder> {
    private ArrayList<AnalysisItem> mHList;
    private Context mContext;
    private AnalysisBrokerAdapter.OnAnalysisBCardListener mHListner;

    /**
     * <b>public interface OnCardListener</b>
     * <br>Interface to implement the OnClick on Card</br>
     */
    public interface OnAnalysisBCardListener {
        void OnCardClick(AnalysisItem item);
    }

    public class AnalysisBrokerViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mParentlayout;
        private TextView mChemicalName;
        private TextView mDate;
        private TextView mTotalCharge;
        public AnalysisBrokerViewHolder(View itemView) {
            super(itemView);
            mParentlayout = (LinearLayout) itemView.findViewById(R.id.AnalysisBrolinear_parent);
            mChemicalName = (TextView) itemView.findViewById(R.id.chemical_nameB);
            mDate = (TextView) itemView.findViewById(R.id.dateB);
            mTotalCharge = (TextView) itemView.findViewById(R.id.totalChargeB);
        }
    }
    public AnalysisBrokerAdapter(Context mContext, ArrayList<AnalysisItem> mList, OnAnalysisBCardListener mListner) {
        this.mContext = mContext;
        this.mHList = mList;
        this.mHListner = mListner;
    }
    @Override
    public AnalysisBrokerAdapter.AnalysisBrokerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_analysis_broker, parent, false);
        return new AnalysisBrokerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AnalysisBrokerAdapter.AnalysisBrokerViewHolder holder, int position) {
        final AnalysisItem Item = mHList.get(position);
        holder.mChemicalName.setText(Item.getChemical_Name());
        holder.mDate.setText(Item.getDate());
        holder.mTotalCharge.setText(Item.getTotal_Charge());
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
