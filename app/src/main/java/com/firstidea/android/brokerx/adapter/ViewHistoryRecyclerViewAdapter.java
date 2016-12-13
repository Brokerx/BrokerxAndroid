package com.firstidea.android.brokerx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.model.TopBroketItem;

import java.util.ArrayList;

/**
 * Created by user on 10/3/2016.
 */

public class ViewHistoryRecyclerViewAdapter extends RecyclerView.Adapter<ViewHistoryRecyclerViewAdapter.HistoryViewHolder> {
    private ArrayList<Lead> mList;
    private Context mContext;
    private OnCardListener mListner;



    /**
     * <b>public interface OnCardListener</b>
     * <br>Interface to implement the OnClick on Card</br>
     */
    public interface OnCardListener {
        void OnCardClick(Lead lead);
    }


    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mParentlayout;
        private TextView date;
        private TextView time;
        private TextView description;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            mParentlayout = (LinearLayout) itemView.findViewById(R.id.TopBrokerlinear_parent);
            date = (TextView) itemView.findViewById(R.id.basicChargeT);
            time = (TextView) itemView.findViewById(R.id.totalChargeT);
            description = (TextView) itemView.findViewById(R.id.brokerNameT);
        }
    }
    public ViewHistoryRecyclerViewAdapter(Context mContext, ArrayList<Lead> mList, ViewHistoryRecyclerViewAdapter.OnCardListener mListner) {
        this.mContext = mContext;
        this.mList = mList;
        this.mListner = mListner;
    }
    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_top_broker_layout, parent, false);
        HistoryViewHolder ViewHolder = new HistoryViewHolder(v);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        final Lead mItem = mList.get(position);
        holder.date.setText(mItem.getCreatedDttm());
        holder.time.setText(mItem.getCreatedDttm());
        holder.description.setText(mItem.getFieldsAltered());
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
