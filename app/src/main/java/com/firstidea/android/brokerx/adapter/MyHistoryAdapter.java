package com.firstidea.android.brokerx.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firstidea.android.brokerx.EnquiryDetailsActivity;
import com.firstidea.android.brokerx.MyHistoryActivity;
import com.firstidea.android.brokerx.MyHistoryDetailsActivity;
import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.enums.LeadType;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.model.MyHistroyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 9/22/2016.
 */

public class MyHistoryAdapter extends RecyclerView.Adapter<MyHistoryAdapter.HistoryViwHolder> {

    private final List<Lead> mValues;
    private String[] qtys;
    private final boolean isBroker;

    public MyHistoryAdapter(Context context,List<Lead> items, boolean isBroker) {
        mValues = items;
        qtys = context.getResources().getStringArray(R.array.qty_options);
        this.isBroker=isBroker;
    }

    /**
     * <b>public interface OnCardListener</b>
     * <br>Interface to implement the OnClick on Card</br>
     */
    public interface OnHostoryCardListener {
        void OnCardClick(MyHistroyItem item);
    }

    @Override
    public HistoryViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_buyer_seller_enq_item, parent, false);
        return new HistoryViwHolder(view);
    }

    @Override
    public void onBindViewHolder(final HistoryViwHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.title.setText(mValues.get(position).getItemName());
        String postedByLabel = "<b>Buyer: </b>"+mValues.get(position).getCreatedUser().getFullName();
        String assignedToLabel = "<b>Seller: </b>"+mValues.get(position).getAssignedToUser().getFullName();
        if(holder.mItem .getType().equals(LeadType.SELLER.getType())) {
            postedByLabel = "<b>Seller: </b>"+mValues.get(position).getCreatedUser().getFullName();
            assignedToLabel = "<b>Buyer: </b>"+mValues.get(position).getAssignedToUser().getFullName();
        }
        if(!isBroker) {
            postedByLabel = "<b>Broker: </b>"+mValues.get(position).getBroker().getFullName();
            holder.brokerage.setText("Brokerage: "+mValues.get(position).getBrokerageAmt());
        } else {
            holder.brokerage.setText("You get "+mValues.get(position).getBrokerageAmt());
        }
        holder.userName.setVisibility(View.GONE);
        holder.layoutUsers.setVisibility(View.VISIBLE);
        holder.createdUserName.setText(Html.fromHtml(postedByLabel));
        holder.assignedUserName.setText(Html.fromHtml(assignedToLabel));
        holder.address.setText(mValues.get(position).getLocation());

        holder.qty.setText(mValues.get(position).getQty()+" "+qtys[mValues.get(position).getQtyUnit()]+" Available");
        final Context context = holder.qty.getContext();
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(context, MyHistoryDetailsActivity.class);
                next.putExtra(Lead.KEY_LEAD,holder.mItem);
                context.startActivity(next);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class HistoryViwHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView title;
        public final TextView userName;
        public final TextView createdUserName;
        public final TextView assignedUserName;
        public final TextView address;
        public final TextView brokerage;
        public final TextView qty;
        public final LinearLayout layoutUsers;

        public Lead mItem;

        public HistoryViwHolder(View view) {
            super(view);
            mView = view.findViewById(R.id.main_view);
            title = (TextView) view.findViewById(R.id.title);
            userName = (TextView) view.findViewById(R.id.userName);
            layoutUsers = (LinearLayout) view.findViewById(R.id.layout_users);
            createdUserName = (TextView) view.findViewById(R.id.createdUserName);
            assignedUserName = (TextView) view.findViewById(R.id.assignedUserName);
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
