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
 * Created by user on 9/28/2016.
 */

public class AnalysisAdapter extends RecyclerView.Adapter<AnalysisAdapter.AnalysisViewHolder> {

    private ArrayList<AnalysisItem> mHList;
    private Context mContext;
    private OnAnalysisCardListener mHListner;
    private TextView mViewDetails;


    /**
     * <b>public interface OnCardListener</b>
     * <br>Interface to implement the OnClick on Card</br>
     */
    public interface OnAnalysisCardListener {
        void OnCardClick(AnalysisItem item);
    }


    public class AnalysisViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mParentlayout;
        private TextView mChemicalName;
        private TextView mBrokerName;
        private TextView mAvaiblility;
        private TextView mGetunits;
        private TextView mDate;
        public AnalysisViewHolder(View itemView) {
            super(itemView);

            mParentlayout = (LinearLayout) itemView.findViewById(R.id.Analysislinear_parent);
            mChemicalName = (TextView) itemView.findViewById(R.id.chemical_nameA);
            mBrokerName = (TextView) itemView.findViewById(R.id.broker_nameA);
            mAvaiblility = (TextView) itemView.findViewById(R.id.avaibilityA);
            mDate = (TextView) itemView.findViewById(R.id.dateA);
            mGetunits = (TextView)itemView.findViewById(R.id.get_percentageA);


        }
    }

    public AnalysisAdapter(Context mContext, ArrayList<AnalysisItem> mList, OnAnalysisCardListener mListner) {
        this.mContext = mContext;
        this.mHList = mList;
        this.mHListner = mListner;
    }
    @Override
    public AnalysisAdapter.AnalysisViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_analysis_layout, parent, false);
        AnalysisViewHolder AnalysisViewHolder = new AnalysisViewHolder(v);
        return AnalysisViewHolder;
    }

    @Override
    public void onBindViewHolder(AnalysisAdapter.AnalysisViewHolder holder, int position) {

        final AnalysisItem Item = mHList.get(position);
        holder.mChemicalName.setText(Item.getChemical_Name());
        holder.mBrokerName.setText(Item.getBroker_Name());
        holder.mAvaiblility.setText(Item.getAvability());
        holder.mDate.setText(Item.getDate());
        holder.mGetunits.setText(Item.getGetunits());

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
