package com.firstidea.android.brokerx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.model.PendingEntries;

import java.util.ArrayList;

/**
 * Created by user on 9/20/2016.
 */
public class PendingEntriesAdapter extends RecyclerView.Adapter<PendingEntriesAdapter.PendingViewHolder> {
    private ArrayList<PendingEntries> mList;
    private Context mContext;
    private OnCardListener mListner;

    /**
     * <b>public interface OnCardListener</b>
     * <br>Interface to implement the OnClick on Card</br>
     */
    public interface OnCardListener {
        void OnCardClick(PendingEntries item);
    }

    public class PendingViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout mParentlayout;
        private TextView mChemicalName;
        private TextView mPostName;
        private TextView mAddress;
        private TextView mGetPercentage;
        private TextView mAvaibility;
        public PendingViewHolder(View itemView) {
            super(itemView);

            mParentlayout = (LinearLayout) itemView.findViewById(R.id.linear_parent);
            mChemicalName = (TextView) itemView.findViewById(R.id.chemical_name);
            mPostName = (TextView) itemView.findViewById(R.id.post_name);
            mAddress = (TextView) itemView.findViewById(R.id.addresspend);
            mGetPercentage = (TextView) itemView.findViewById(R.id.get_percentage);
            mAvaibility = (TextView) itemView.findViewById(R.id.avaibility);
        }
    }

    public PendingEntriesAdapter(Context mContext, ArrayList<PendingEntries> mList, OnCardListener mListner) {
        this.mContext = mContext;
        this.mList = mList;
        this.mListner = mListner;
    }
    @Override
    public PendingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.pending_entries_card, parent, false);
        PendingViewHolder pendingViewHolder = new PendingViewHolder(v);
        return pendingViewHolder;

    }

    @Override
    public void onBindViewHolder(PendingViewHolder holder, int position) {
        final PendingEntries mItem = mList.get(position);
        holder.mChemicalName.setText(mItem.getChemicalName());
        holder.mPostName.setText(mItem.getPostedName());
        holder.mAddress.setText(mItem.getAddress());
        holder.mGetPercentage.setText(mItem.getGetPercentage());
        holder.mAvaibility.setText(mItem.getAvailability());

        holder.mParentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListner.OnCardClick(mItem);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
