package com.firstidea.android.brokerx;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firstidea.android.brokerx.constants.MsgConstants;
import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.SingletonRestClient;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.utils.StringUtils;
import com.firstidea.android.brokerx.widget.AppProgressDialog;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BrokerDetailsActivity extends AppCompatActivity {

    private Context mContext;
    private User mUserProfile;
    private User mUser;
    private boolean isForConnectRequest = false;
    private TextView name,rating,address,phone, about;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Broker Details");

        mContext = this;
        mUserProfile = getIntent().getExtras().getParcelable(User.KEY);
        mUser = User.getSavedUser(this);
        if(getIntent().hasExtra("IsForConnect")) {
            isForConnectRequest = getIntent().getBooleanExtra("IsForConnect", false);
        }
        initScreen();
    }

    private void initScreen() {
        name = (TextView) findViewById(R.id.user_name);
        rating = (TextView) findViewById(R.id.rating);
        address = (TextView) findViewById(R.id.user_address);
        phone = (TextView) findViewById(R.id.user_phone_number);
        about = (TextView) findViewById(R.id.about);
        imageView = (ImageView) findViewById(R.id.user_profile);

        name.setText(mUserProfile.getFullName());
        String ratingVal = "0";
        if(!TextUtils.isEmpty(mUserProfile.getRating())) {
            ratingVal = mUserProfile.getRating();
        }
        rating.setText(ratingVal);
        String addr = mUserProfile.getAddress()+", "+mUserProfile.getCity();
        address.setText(addr);
        phone.setText(mUserProfile.getMobile());
        about.setText(mUserProfile.getAbout());
        if(!TextUtils.isEmpty(mUserProfile.getImageURL())) {
            String imgUrl = SingletonRestClient.BASE_PROFILE_IMAGE_URL+mUserProfile.getImageURL();
            Picasso.with(mContext).load(imgUrl)
                    .error(R.drawable.user_img)
                    .placeholder(R.drawable.user_img)
                    .into(imageView);
        }
        if(!TextUtils.isEmpty(mUserProfile.getBrokerDealsInItems())) {
            populateTags();
        }
    }

    private void populateTags() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout_tags);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        int[] tagBgDrawables = new int[]{R.drawable.tag_bg1, R.drawable.tag_bg2, R.drawable.tag_bg3, R.drawable.tag_bg4, R.drawable.tag_bg5, R.drawable.tag_bg6};
        int tagsCount = 0;
        View view = null;
        int nateTextViewIds[] = new int[]{R.id.tag1, R.id.tag2, R.id.tag3};
        for (String tagNameString : StringUtils.getListOfComaValues(mUserProfile.getBrokerDealsInItems())) {
            boolean isNewRow = tagsCount % 3 == 0;
            if (isNewRow) {
                view = inflater.inflate(R.layout.row_user_profile_tags, null);
            }
            int nameViewID = nateTextViewIds[tagsCount % 3];
            TextView tagName = (TextView) view.findViewById(nameViewID);
            int tagBgId = tagBgDrawables[tagsCount % 6];
            tagName.setBackgroundResource(tagBgId);
            tagName.setText(tagNameString);
            tagName.setVisibility(View.VISIBLE);
            tagsCount++;
            if (isNewRow) {
                linearLayout.addView(view);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isForConnectRequest) {
            getMenuInflater().inflate(R.menu.menu_user_profile_send_request, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if(item.getItemId() == R.id.action_send_request){
            sendRequest();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendRequest() {
        final Dialog dialog = AppProgressDialog.show(this);
        ObjectFactory.getInstance().getUserServiceInstance().sendConnectionRequest(mUser.getUserID(), mUserProfile.getUserID(), new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if(messageDTO.getMessageID().equals(MsgConstants.SUCCESS_ID)) {
                    setResult(RESULT_OK);
                    dialog.dismiss();
                    finish();
                } else {
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.dismiss();
                Toast.makeText(mContext, "Unknown Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
