package com.firstidea.android.brokerx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.enums.LeadCurrentStatus;
import com.firstidea.android.brokerx.enums.LeadType;
import com.firstidea.android.brokerx.http.model.Lead;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Lead} and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class BrokerHomeEnquiriesAdapter extends RecyclerView.Adapter<BrokerHomeEnquiriesAdapter.ViewHolder> {

    private final List<Lead> mValues;
    private String[] qtys;
    private OnCardClickListener mOnCardClickListener;
    String availableLabel;
    String leadType;

    public interface OnCardClickListener {
        void onCardClick(Lead lead);
    }
    public BrokerHomeEnquiriesAdapter(Context context,List<Lead> items, String type, OnCardClickListener mOnCardClickListener) {
        mValues = items;
        qtys = context.getResources().getStringArray(R.array.qty_options);
        this.mOnCardClickListener = mOnCardClickListener;
        this.leadType= type;
        this.availableLabel = type.startsWith("B")?" to Buy":" to Sell";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_buyer_seller_enq_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.title.setText(mValues.get(position).getItemName());
        holder.userName.setText("Posted by "+mValues.get(position).getCreatedUser().getFullName());
        holder.address.setText(mValues.get(position).getLocation());
        holder.brokerage.setText("You get "+mValues.get(position).getBrokerageAmt());
        holder.qty.setText(mValues.get(position).getQty()+" "+qtys[mValues.get(position).getQtyUnit()]+availableLabel);
        holder.dttm.setText(mValues.get(position).getLastUpdDateTime());
        final Context context = holder.qty.getContext();
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCardClickListener.onCardClick(holder.mItem);
            }
        });
        String myStatus ="";
        if (holder.mItem.getType().equals(LeadType.BUYER.getType())) {
            myStatus = holder.mItem.getBuyerStatus();
        } else {
            myStatus = holder.mItem.getSellerStatus();
        }
        if (myStatus.equals(LeadCurrentStatus.Accepted.getStatus())) {
            holder.currentStatus.setImageResource(R.drawable.accept_circle);
        } else if (myStatus.equals(LeadCurrentStatus.Rejected.getStatus())) {
            holder.currentStatus.setImageResource(R.drawable.reject_circle);
        } else if (myStatus.equals(LeadCurrentStatus.Pending.getStatus())) {
            holder.currentStatus.setImageResource(R.drawable.pending_circle);
        } else if (myStatus.equals(LeadCurrentStatus.Reverted.getStatus())) {
            holder.currentStatus.setImageResource(R.drawable.accept_circle_gray);
        } else if (myStatus.equals(LeadCurrentStatus.Waiting.getStatus())) {
            holder.currentStatus.setImageResource(R.drawable.waiting_circle);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView title;
        public final TextView userName;
        public final TextView address;
        public final TextView brokerage;
        public final TextView qty;
        public final TextView dttm;
        public final ImageView currentStatus;
        public Lead mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view.findViewById(R.id.main_view);
            title = (TextView) view.findViewById(R.id.title);
            userName = (TextView) view.findViewById(R.id.userName);
            address = (TextView) view.findViewById(R.id.address);
            brokerage = (TextView) view.findViewById(R.id.brokerage);
            qty = (TextView) view.findViewById(R.id.qty);
            dttm = (TextView) view.findViewById(R.id.dttm);
            currentStatus = (ImageView) view.findViewById(R.id.cur_statusIcon);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + userName.getText() + "'";
        }
    }
}
