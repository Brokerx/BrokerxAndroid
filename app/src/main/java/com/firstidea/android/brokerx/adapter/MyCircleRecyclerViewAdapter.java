package com.firstidea.android.brokerx.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firstidea.android.brokerx.BrokerDetailsActivity;
import com.firstidea.android.brokerx.Constants;
import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.enums.ConnectionStatus;
import com.firstidea.android.brokerx.http.SingletonRestClient;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.widget.SectionedRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link User} and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class MyCircleRecyclerViewAdapter extends SectionedRecyclerViewAdapter<MyCircleRecyclerViewAdapter.ViewHolder> {

    public interface ChangeStatusListener{
        public void onChangeStatus(User user, String status);
    }

    private final List<List<User>> mValues;
    private final boolean isForSelection;
    private final Activity activity;
    private ChangeStatusListener mChangeStatusListener;

    @Override
    public int getSectionCount() {
        return mValues.size();
    }

    public MyCircleRecyclerViewAdapter(List<List<User>> items, boolean isForSelection, Activity activity, ChangeStatusListener changeStatusListener) {
        mValues = items;
        this.isForSelection = isForSelection;
        this.activity = activity;
        this.mChangeStatusListener = changeStatusListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                layout = R.layout.my_circle_list_item_header;
                break;
            default:
                layout = R.layout.fragment_my_circle_item;
                break;
        }

        View v = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder holder, int section) {
        if (isForSelection) {
            holder.title.setText("Brokers in your circle");
        } else if (section == 0) {
            holder.title.setText("Pending Requests");
        } else {
            holder.title.setText("Your Circle");
        }

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int section, int relativePosition, int absolutePosition) {
        holder.mItem = mValues.get(section).get(relativePosition);
        holder.name.setText(holder.mItem.getFullName());
        holder.rating.setText(holder.mItem.getRating());
        holder.address.setText(holder.mItem.getAddress());
        if (holder.mItem.getStatus().equals(ConnectionStatus.PENDING.getStatus()) && !holder.mItem.isMyRequest()) {
            holder.mOverflowIcon.setVisibility(View.VISIBLE);
        } else {
            holder.mOverflowIcon.setVisibility(View.GONE);
        }
       if(!TextUtils.isEmpty(holder.mItem.getImageURL())) {
            String imgUrl = SingletonRestClient.BASE_PROFILE_IMAGE_URL+"thumb_"+holder.mItem.getImageURL();
            Picasso.with(activity).load(imgUrl)
                    .error(R.drawable.user_profile)
                    .placeholder(R.drawable.user_profile)
                    .into(holder.profileImage);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isForSelection) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.KEY_USER_TYPE_BROKER, holder.mItem);
                    activity.setResult(Activity.RESULT_OK, intent);
                    activity.finish();
                } else {
                    Intent intent = new Intent(holder.mOverflowIcon.getContext(), BrokerDetailsActivity.class);
                    intent.putExtra(User.KEY, holder.mItem);
                    holder.mOverflowIcon.getContext().startActivity(intent);
                }
            }
        });
        if (holder.mOverflowIcon.getVisibility() == View.VISIBLE) {
            holder.mOverflowIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(v.getContext(), v);
                        popup.inflate(R.menu.menu_my_circle_list_item);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                if (item.getItemId() == R.id.action_accept) {
                                    mChangeStatusListener.onChangeStatus(holder.mItem,ConnectionStatus.ACCEPTED.getStatus());
                                } else {
                                    mChangeStatusListener.onChangeStatus(holder.mItem,ConnectionStatus.REJECTED.getStatus());
                                }
                                return true;
                            }
                        });
                        popup.show();
                }
            });
        }
    }


    @Override
    public int getItemViewType(int section, int relativePosition, int absolutePosition) {
        if (section == 1)
            return 0; // VIEW_TYPE_HEADER is -2, VIEW_TYPE_ITEM is -1. You can return 0 or greater.
        return super.getItemViewType(section, relativePosition, absolutePosition);
    }

    /* @Override
     public int getItemCount() {
         return mValues.size();
     }*/
    @Override
    public int getItemCount(int section) {
        return mValues.get(section).size(); // odd get 8
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public final RelativeLayout mView;
        public final TextView name;
        public final TextView title;
        public final TextView address;
        public final TextView rating;
        public User mItem;
        private ImageView mOverflowIcon;
        private ImageView profileImage;

        public ViewHolder(View view) {
            super(view);
            mView = (RelativeLayout) view.findViewById(R.id.main_view);
            name = (TextView) view.findViewById(R.id.user_name);
            address = (TextView) view.findViewById(R.id.address);
            title = (TextView) view.findViewById(R.id.title);
            rating = (TextView) view.findViewById(R.id.rating);
            mOverflowIcon = (ImageView) view.findViewById(R.id.mfp_context_menu);
            profileImage = (ImageView) view.findViewById(R.id.user_image);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + rating.getText() + "'";
        }
    }
}
