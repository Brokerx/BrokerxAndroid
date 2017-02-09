package com.firstidea.android.brokerx.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firstidea.android.brokerx.EnquiryDetailsActivity;
import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.model.PendingEntries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 9/20/2016.
 */
public class PendingEntriesAdapter extends RecyclerView.Adapter<PendingEntriesAdapter.ViewHolder> {

    private final List<Lead> mValues;
    private String[] qtys;
    private String[] mUnits;
    private boolean isBroker;
    private OnCardClickListener mOnCardClickListener;

    public interface OnCardClickListener {
        void onCardClick(Lead lead);
    }
    public PendingEntriesAdapter(Context context,List<Lead> items) {
        mValues = items;
        qtys = context.getResources().getStringArray(R.array.qty_options);
        mUnits = context.getResources().getStringArray(R.array.qty_options);
        User me = User.getSavedUser(context);
        isBroker = me.isBroker();
    }

    @Override
    public PendingEntriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_buyer_seller_enq_item, parent, false);
        return new PendingEntriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PendingEntriesAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.title.setText(mValues.get(position).getItemName());
        if(!isBroker) {
            holder.userName.setText("Broker: " + mValues.get(position).getBroker().getFullName());
        } else {
            holder.userName.setText("Posted by "+mValues.get(position).getCreatedUser().getFullName());
        }
        holder.address.setText(mValues.get(position).getLocation());
        if(!isBroker) {
            String basicPriceString = mValues.get(position).getBasicPrice() + " Rs/" + mUnits[mValues.get(position).getBasicPriceUnit()];
            holder.brokerage.setText(basicPriceString);
        } else {
            holder.brokerage.setText("You get "+mValues.get(position).getBrokerageAmt());
        }
        holder.qty.setText(mValues.get(position).getQty()+" "+qtys[mValues.get(position).getQtyUnit()]+" Available");
        final Context context = holder.qty.getContext();
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(context, EnquiryDetailsActivity.class);
                next.putExtra(Lead.KEY_LEAD,holder.mItem);
                context.startActivity(next);
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
