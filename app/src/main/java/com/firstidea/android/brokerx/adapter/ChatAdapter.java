package com.firstidea.android.brokerx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firstidea.android.brokerx.R;
import com.firstidea.android.brokerx.enums.ChatType;
import com.firstidea.android.brokerx.http.SingletonRestClient;
import com.firstidea.android.brokerx.http.model.Chat;
import com.firstidea.android.brokerx.http.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Govind on 19-Jan-17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private ArrayList<Chat> mHList;
    private Context mContext;
    private ChatAdapter.ChatCardListener mHListner;
    private User me;

    private final int VIEW_TYPE_ME = 0;
    private final int VIEW_TYPE_OTHER = 1;

    /**
     * <b>public interface OnCardListener</b>
     * <br>Interface to implement the OnClick on Card</br>
     */
    public interface ChatCardListener {
        void OnCardClick(Chat item);
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView msg;
        private TextView dttm;
        private ImageView img;

        public ChatViewHolder(View itemView) {
            super(itemView);

            msg = (TextView) itemView.findViewById(R.id.msg);
            dttm = (TextView) itemView.findViewById(R.id.date_time);
            img = (ImageView) itemView.findViewById(R.id.img);

        }
    }

    public ChatAdapter(Context mContext, ArrayList<Chat> mHList) {
        this.mContext = mContext;
        this.mHList = mHList;
        this.me = User.getSavedUser(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = mHList.get(position);
        if(chat.getFromUserID().equals(me.getUserID())) {
            return VIEW_TYPE_ME;
        } else {
            return  VIEW_TYPE_OTHER;
        }
    }

    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType == VIEW_TYPE_ME) {
            v = LayoutInflater.from(mContext).inflate(R.layout.row_conversation_user, parent, false);
        } else {
            v = LayoutInflater.from(mContext).inflate(R.layout.row_conversation_other, parent, false);
        }
        ChatAdapter.ChatViewHolder ChatViewHolder = new ChatAdapter.ChatViewHolder(v);
        return ChatViewHolder;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ChatViewHolder holder, int position) {

        final Chat chat = mHList.get(position);
        holder.dttm.setText(chat.getCreatedDttm());
        if(chat.getType().equals(ChatType.IMAGE.getType())) {
            holder.img.setVisibility(View.VISIBLE);
            holder.msg.setVisibility(View.GONE);
            String imgUrl = SingletonRestClient.BASE_IMAGE_URL +  chat.getMessage(); //"thumb_" +
            Picasso.with(mContext).load(chat.getMessage())
                    .placeholder(R.drawable.ic_img_stub)
                    .error(R.drawable.ic_img_stub)
                    .into(holder.img);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO open activity/dialog to display fullscreen iamge with pinch effect. user advancedimageview lib
                    //mHListner.OnCardClick(Item);
                }
            });
        } else {
            holder.img.setVisibility(View.GONE);
            holder.msg.setVisibility(View.VISIBLE);
            holder.msg.setText(chat.getMessage());
        }



    }

    @Override
    public int getItemCount() {
        return mHList.size();
    }
}