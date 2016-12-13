package com.firstidea.android.brokerx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firstidea.android.brokerx.http.SingletonRestClient;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.User;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EnquiryDetailsActivity extends AppCompatActivity {
    private TextView TxviewHistoryA,TxviewHistoryR;

    private LinearLayout Accept, Reject, Pending, Deal_Done,Revert,Rej_Acc_Layout;

    @BindView(R.id.item_name) TextView itemName;
    @BindView(R.id.make) TextView make;
    @BindView(R.id.location) TextView location;
    @BindView(R.id.basic_price_per_unit) TextView basicPricePerUnit;
    @BindView(R.id.qty_unit) TextView qtyUnit;
    @BindView(R.id.item_packing) TextView packing;
    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.user_type) TextView userType;
    @BindView(R.id.brokerage_amt) TextView brokerageAmt;
    @BindView(R.id.basic_price_with_unit) TextView basicPriceWithUnit;
    @BindView(R.id.excise_duty_with_unit) TextView exciseDutyWithUnit;
    @BindView(R.id.transport_charges) TextView transportCharges;
    @BindView(R.id.misc_charges) TextView miscCharges;
    @BindView(R.id.total_charges) TextView totalCharges;
    @BindView(R.id.against_form) TextView againstForm;
    @BindView(R.id.credit_period) TextView creditPeriod;
    @BindView(R.id.free_storage) TextView freeStorage;
    @BindView(R.id.prefered_seller_name) TextView preferedSellerName;
    @BindView(R.id.comments) TextView comments;
    @BindView(R.id.user_image) ImageView userImage;
    @BindView(R.id.btn_reject) LinearLayout btnReject;
    @BindView(R.id.btn_accept) LinearLayout btnAccept;

    private Lead mLead;
    private User me;
    private String [] mUnits,mPackings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        setContentView(R.layout.activity_enquiry_details);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mLead = getIntent().getExtras().getParcelable(Lead.KEY_LEAD);
        me = User.getSavedUser(this);

        initScreen();
        TxviewHistoryA = (TextView) findViewById(R.id.ViewHistory_Accp);
        TxviewHistoryA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ViewAccHistory = new Intent(EnquiryDetailsActivity.this,ViewHistoryActivity.class);
                ViewAccHistory.putExtra(Lead.KEY_LEAD_ID, mLead.getLeadID());
                startActivity(ViewAccHistory);
            }
        });

        TxviewHistoryR = (TextView) findViewById(R.id.viewhistRej);
        TxviewHistoryR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ViewRejHistory = new Intent(EnquiryDetailsActivity.this,ViewHistoryActivity.class);
                ViewRejHistory.putExtra(Lead.KEY_LEAD_ID, mLead.getLeadID());
                startActivity(ViewRejHistory);

            }
        });

        Accept = (LinearLayout) findViewById(R.id.Accept);
        Accept.setVisibility(View.GONE);

        Reject = (LinearLayout) findViewById(R.id.Reject);
        Reject.setVisibility(View.GONE);

        Pending = (LinearLayout) findViewById(R.id.pending);
        Pending.setVisibility(View.GONE);

        Deal_Done = (LinearLayout) findViewById(R.id.Deal_done);
        Deal_Done.setVisibility(View.GONE);

        Revert = (LinearLayout) findViewById(R.id.revert_layout);
        Revert.setVisibility(View.GONE);

        Rej_Acc_Layout = (LinearLayout) findViewById(R.id.acc_rej_layout);
        Rej_Acc_Layout.setVisibility(View.VISIBLE);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Accept = (LinearLayout) findViewById(R.id.Accept);
                Accept.setVisibility(View.VISIBLE);
                Toast.makeText(EnquiryDetailsActivity.this, "Accept", Toast.LENGTH_SHORT).show();
                Revert = (LinearLayout) findViewById(R.id.revert_layout);
                Revert.setVisibility(View.VISIBLE);
                Rej_Acc_Layout.setVisibility(View.GONE);


            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EnquiryDetailsActivity.this, "Rejected", Toast.LENGTH_SHORT).show();
                Reject = (LinearLayout) findViewById(R.id.Reject);
                Reject.setVisibility(View.VISIBLE);
                Accept.setVisibility(View.GONE);
            }
        });
    }

    private void initScreen() {
        mUnits = getResources().getStringArray(R.array.qty_options);
        mPackings = getResources().getStringArray(R.array.packing);
        itemName.setText(mLead.getItemName());
        String makeString = "<b>Make: </b>"+mLead.getMake();
        make.setText(Html.fromHtml(makeString));
        location.setText(mLead.getLocation());
        String basicPriceString = mLead.getBasicPrice() + " Rs/"+ mUnits[mLead.getBasicPriceUnit()];
        basicPricePerUnit.setText(basicPriceString);
        qtyUnit.setText(mLead.getQty()+" "+mUnits[mLead.getQtyUnit()] + " available");
        String packingString = mPackings[mLead.getPacking()];
        packing.setText(packingString);

        String userNameString ="", userImageUrl = "", userTypeString = "";
        if(me.getUserID().equals(mLead.getCreatedUserID())) {
            userNameString = mLead.getBroker().getFullName();
            userImageUrl = mLead.getBroker().getImageURL();
            userTypeString = "Broker";
        } else {
            userNameString = mLead.getCreatedUser().getFullName();
            userImageUrl = mLead.getCreatedUser().getImageURL();
            userTypeString = mLead.getType().toLowerCase().startsWith("B") ? "Buyer": "Seller";
        }

        userName.setText(userNameString);
        userType.setText(userTypeString);
        brokerageAmt.setText(mLead.getBrokerageAmt()+" Rs");
        if(!TextUtils.isEmpty(userImageUrl)) {
            String imgUrl = SingletonRestClient.BASE_PROFILE_IMAGE_URL+"thumb_"+userImageUrl;
            Picasso.with(this).load(imgUrl)
                    .error(R.drawable.user_profile)
                    .placeholder(R.drawable.user_profile)
                    .into(userImage);
        }
        basicPriceWithUnit.setText(basicPriceString);
        exciseDutyWithUnit.setText(mLead.getExciseUnit()+" Rs/" + mUnits[mLead.getExciseUnit()]);
        transportCharges.setText(mLead.getTransportCharges() + " Rs/"+mUnits[mLead.getBasicPriceUnit()]);
        miscCharges.setText(mLead.getMiscCharges()+" Rs");
        float basicPriceAmt = mLead.getBasicPrice() * mLead.getQty();
        float excisePriceAmt = mLead.getExciseDuty() * mLead.getQty();
        float totalAmt = basicPriceAmt + excisePriceAmt+mLead.getTransportCharges()+mLead.getMiscCharges();
        totalCharges.setText(totalAmt+" Rs");
        againstForm.setText(mLead.getAgainstForm());
        creditPeriod.setText(mLead.getCreditPeriod() + " days");
        freeStorage.setText(mLead.getFreeStoragePeriod() + " days");
        preferedSellerName.setText(mLead.getPreferredSellerName());
        comments.setText(mLead.getComments());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, android.R.anim.slide_out_right);
    }


}
