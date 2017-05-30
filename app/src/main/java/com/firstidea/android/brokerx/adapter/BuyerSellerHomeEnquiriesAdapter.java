package com.firstidea.android.brokerx.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firstidea.android.brokerx.AnalysisActivity;
import com.firstidea.android.brokerx.EnquiryDetailsActivity;
import com.firstidea.android.brokerx.MyHistoryActivity;
import com.firstidea.android.brokerx.MycircleActivity;
import com.firstidea.android.brokerx.NewBuyerAnalysisActivity;
import com.firstidea.android.brokerx.PendingEntriesActivity;
import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.enums.LeadCurrentStatus;
import com.firstidea.android.brokerx.enums.LeadType;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.model.HomeHeader;
import com.firstidea.android.brokerx.widget.HeaderRecyclerViewAdapter;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Lead} and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class BuyerSellerHomeEnquiriesAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, HomeHeader, Lead, BuyerSellerHomeEnquiriesAdapter.HomeFooter> {
    //RecyclerView.Adapter<BuyerSellerHomeEnquiriesAdapter.ViewHolder> {

    private final List<Lead> mValues;
    private Context mContext;
    private String[] qtys;
    String availableLabel;
    //    private BuyerSellerHomeEnquiriesAdapter.OnCardListener mListner;
    private OnCardClickListener mOnCardClickListener;

    public interface OnCardClickListener {
        void onCardClick(Lead lead);
    }

    public BuyerSellerHomeEnquiriesAdapter(Context context, List<Lead> items, String type, OnCardClickListener mOnCardClickListener) {
        mValues = items;
        qtys = context.getResources().getStringArray(R.array.qty_options);
        this.mOnCardClickListener = mOnCardClickListener;
        this.availableLabel = type.startsWith("B")?" to Buy":" to Sell";
    }

    private static final String LOG_TAG = BuyerSellerHomeEnquiriesAdapter.class.getSimpleName();

    /**
     * <b>public interface OnCardListener</b>
     * <br>Interface to implement the OnClick on Card</br>
     */
//    public interface OnCardListener {
//        void OnCardClick(Lead item);
//    }
    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View headerView = inflater.inflate(R.layout.buyer_seller_home_header, parent, false);
        return new HeaderViewHolder(headerView);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.fragment_buyer_seller_enq_item, parent, false);
        return new ViewHolder(characterView);
    }
/*
    @Override
    protected RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View footerView = inflater.inflate(R.layout.row_dragon_ball_footer, parent, false);
        return new FooterViewHolder(footerView);
    }*/

    @Override
    protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
//        HomeHeader header = getHeader();
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        headerViewHolder.render();
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        holder.mItem = mValues.get(position);
        holder.title.setText(mValues.get(position).getItemName());
        String userName = "<b>Broker: </b>" + mValues.get(position).getBroker().getFullName();
        holder.userName.setText(Html.fromHtml(userName));
        holder.address.setText(mValues.get(position).getLocation());
        String brokerage = "<b>Brokerage: </b>" + mValues.get(position).getBrokerageAmt();
        holder.brokerage.setText(Html.fromHtml(brokerage));
        holder.qty.setText(mValues.get(position).getQty() + " " + qtys[mValues.get(position).getQtyUnit()] +availableLabel);
        holder.dttm.setText(mValues.get(position).getLastUpdDateTime());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCardClickListener.onCardClick(holder.mItem);
            }
        });
        String myStatus = holder.mItem.getBrokerStatus();
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

   /* @Override protected void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
        DragonBallFooter footer = getFooter();
        FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
        footerViewHolder.render(footer);
    }*/

    private LayoutInflater getLayoutInflater(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext());
    }

    @Override
    protected void onHeaderViewRecycled(RecyclerView.ViewHolder holder) {
        Log.v(LOG_TAG, "onHeaderViewRecycled(RecyclerView.ViewHolder holder)");
    }

    @Override
    protected void onItemViewRecycled(RecyclerView.ViewHolder holder) {
        Log.v(LOG_TAG, "onItemViewRecycled(RecyclerView.ViewHolder holder)");
    }

    @Override
    protected void onFooterViewRecycled(RecyclerView.ViewHolder holder) {
        Log.v(LOG_TAG, "onFooterViewRecycled(RecyclerView.ViewHolder holder)");
    }

    /*

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_buyer_seller_enq_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.title.setText(mValues.get(position).getTitle());
            holder.userName.setText(mValues.get(position).getUserName());
            holder.address.setText(mValues.get(position).getAddress());
            holder.brokerage.setText(mValues.get(position).getBrokerage());
            holder.qty.setText(mValues.get(position).getQty());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO open details activity
                }
            });
        }
*/
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


    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView history;
        private final TextView myCircle;
        private final TextView analysis;
        private final TextView pendingEnq;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.history = (TextView) itemView.findViewById(R.id.history);
            this.myCircle = (TextView) itemView.findViewById(R.id.my_circle);
            this.analysis = (TextView) itemView.findViewById(R.id.analysis);
            this.pendingEnq = (TextView) itemView.findViewById(R.id.pending_enquiries);
        }

        public void render() {
            this.history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HeaderViewHolder.this.history.getContext(), MyHistoryActivity.class);
                    HeaderViewHolder.this.history.getContext().startActivity(intent);
                }
            });
            this.myCircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HeaderViewHolder.this.myCircle.getContext(), MycircleActivity.class);
                    HeaderViewHolder.this.myCircle.getContext().startActivity(intent);
                }
            });
            this.analysis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(HeaderViewHolder.this.analysis.getContext(), AnalysisActivity.class);
                    Intent intent = new Intent(HeaderViewHolder.this.analysis.getContext(), NewBuyerAnalysisActivity.class);
                    HeaderViewHolder.this.analysis.getContext().startActivity(intent);
                }
            });
            this.pendingEnq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HeaderViewHolder.this.pendingEnq.getContext(), PendingEntriesActivity.class);
                    HeaderViewHolder.this.pendingEnq.getContext().startActivity(intent);
                }
            });

        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

//        private final TextView loadMoreMessage;

        public FooterViewHolder(View itemView) {
            super(itemView);
//            this.loadMoreMessage = (TextView) itemView.findViewById(R.id.tv_load_more);
        }

        /*public void render(DragonBallFooter footer) {
            String loadMoreText = footer.getLoadMoreMessage();
            loadMoreMessage.setText(loadMoreText);
        }*/
    }

    public class HomeFooter {

        private final String loadMoreMessage;

        public HomeFooter(String loadMoreMessage) {
            this.loadMoreMessage = loadMoreMessage;
        }

        /*public String getLoadMoreMessage() {
            return loadMoreMessage;
        }*/
    }

}
