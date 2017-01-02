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
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.model.TopBroketItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by user on 10/3/2016.
 */

public class ViewHistoryRecyclerViewAdapter extends RecyclerView.Adapter<ViewHistoryRecyclerViewAdapter.HistoryViewHolder> {
    private ArrayList<Lead> mList;
    private Context mContext;
    private OnCardListener mListner;
    private SimpleDateFormat SDF = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
    private SimpleDateFormat SDF_DATE = new SimpleDateFormat("dd MMMM yyyy");
    private SimpleDateFormat SDF_TIME = new SimpleDateFormat("hh:mm a");
    private User loggedInUser;

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
        private View divider;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            mParentlayout = (LinearLayout) itemView.findViewById(R.id.main_view);
            date = (TextView) itemView.findViewById(R.id.date);
            divider = itemView.findViewById(R.id.divider);
            time = (TextView) itemView.findViewById(R.id.time);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }

    public ViewHistoryRecyclerViewAdapter(Context mContext, ArrayList<Lead> mList, ViewHistoryRecyclerViewAdapter.OnCardListener mListner) {
        this.mContext = mContext;
        this.mList = mList;
        this.mListner = mListner;
        this.loggedInUser = User.getSavedUser(mContext);
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_enquiry_history, parent, false);
        HistoryViewHolder ViewHolder = new HistoryViewHolder(v);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        final Lead mItem = mList.get(position);
        try {
            Date createdDttm = SDF.parse(mItem.getCreatedDttm());
            holder.date.setText(SDF_DATE.format(createdDttm));
            holder.time.setText(SDF_TIME.format(createdDttm));
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder description = new StringBuilder();
        if(mItem.getCreatedUserID() == loggedInUser.getUserID()) {
            description.append("You have changed the fields: ");
        } else if(mItem.getCreatedUserID() == mItem.getBroker().getUserID()){
            description.append("Broker "+mItem.getBroker().getFullName()+" had requested to change the filelds");
        } else if(mItem.getAssignedToUser() != null &&  mItem.getCreatedUserID() == mItem.getAssignedToUser().getUserID()){
            if(mItem.getType().startsWith("S")) {
                description.append("Buyer requested to change the filelds ");
            } else {
                description.append("Seller requested to change the filelds ");
            }
        }
        description.append(mItem.getFieldsAltered());

        holder.description.setText(description.toString());
        if (position == 0) {
            holder.divider.setVisibility(View.GONE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
        }
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
