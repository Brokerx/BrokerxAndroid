package com.firstidea.android.brokerx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firstidea.android.brokerx.R;
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

    public interface OnCardClickListener {
        void onCardClick(Lead lead);
    }
    public BrokerHomeEnquiriesAdapter(Context context,List<Lead> items, OnCardClickListener mOnCardClickListener) {
        mValues = items;
        qtys = context.getResources().getStringArray(R.array.qty_options);
        this.mOnCardClickListener = mOnCardClickListener;

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
        holder.qty.setText(mValues.get(position).getQty()+" "+qtys[mValues.get(position).getQtyUnit()]+" Available");
        final Context context = holder.qty.getContext();
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCardClickListener.onCardClick(holder.mItem);
            }
        });
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
        public Lead mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view.findViewById(R.id.main_view);
            title = (TextView) view.findViewById(R.id.title);
            userName = (TextView) view.findViewById(R.id.userName);
            address = (TextView) view.findViewById(R.id.address);
            brokerage = (TextView) view.findViewById(R.id.brokerage);
            qty = (TextView) view.findViewById(R.id.qty);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + userName.getText() + "'";
        }
    }
}
