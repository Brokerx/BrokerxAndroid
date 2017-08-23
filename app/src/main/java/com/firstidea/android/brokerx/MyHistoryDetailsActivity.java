package com.firstidea.android.brokerx;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firstidea.android.brokerx.adapter.MyHistoryAdapter;
import com.firstidea.android.brokerx.enums.ExciseType;
import com.firstidea.android.brokerx.enums.LeadType;
import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.SingletonRestClient;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.LeadStatusHistory;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.model.MyHistroyItem;
import com.firstidea.android.brokerx.utils.ApptDateUtils;
import com.firstidea.android.brokerx.widget.AppProgressDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyHistoryDetailsActivity extends AppCompatActivity {

    private TextView Chemical_Name, InvoiceNo, Total_Brokerage, Date,
            basic_charge, Total_charges,
            units_available, created_user_name, created_user_type, broker_name,
            assigned_user_name, assigned_user_type, created_user_brokerage, assigned_user_brokerage;

    private ImageView created_user_image, broker_image, assigned_user_image;
    private LinearLayout layout_created_user_brokerage, layout_assigned_user_brokerage;
    private User me;
    private Context mContext;
    private Lead mLead;
    private String[] mUnits, mPackings;
    private LeadStatusHistory mLeadStatusHistory;
    private boolean isSeller = false;
    ;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static final String OPEN_DETAILS_SCREEN = "openDetailsScreen";
    private boolean isOpenDetailsScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mContext = this;
        me = User.getSavedUser(this);

        if (getIntent().hasExtra(OPEN_DETAILS_SCREEN)) {
            isOpenDetailsScreen = true;
        }
        if (getIntent().hasExtra(Lead.KEY_LEAD)) {
            mLead = getIntent().getExtras().getParcelable(Lead.KEY_LEAD);
            getLeadStatusHistory();
        } else {
            Integer leadID = getIntent().getExtras().getInt(Lead.KEY_LEAD_ID);
            getLeadByID(leadID);
        }
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark,
                R.color.colorPrimaryLight,
                R.color.teal,
                R.color.teal);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getLeadStatusHistory();
            }
        });


    }

    private void getLeadByID(Integer leadID) {
        final Dialog dialog = AppProgressDialog.show(this);
        ObjectFactory.getInstance().getLeadServiceInstance().getLeadsByID(leadID, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if (messageDTO.isSuccess()) {
                    List<Lead> leads = Lead.createListFromJson(messageDTO.getData());
                    mLead = leads.get(0);
                    getLeadStatusHistory();
                }
                dialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MyHistoryDetailsActivity.this, "Unable to get Lead Details...", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
            }
        });
    }

    private void getLeadStatusHistory() {
        final Dialog dialog = AppProgressDialog.show(mContext);
        ObjectFactory.getInstance().getLeadServiceInstance().getLeadStatusHistory(mLead.getLeadID(), new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if (messageDTO.isSuccess()) {
                    mLeadStatusHistory = LeadStatusHistory.createFromJSON(messageDTO.getData());
                    initScreen();
                    dialog.dismiss();
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (isOpenDetailsScreen) {
                        Intent intent = new Intent(MyHistoryDetailsActivity.this, LeadStatusHistoryDetailsActivity.class);
                        intent.putExtra(LeadStatusHistory.KEY_STATUS_HISTORY, mLeadStatusHistory);
                        intent.putExtra("IsSeller", isSeller);
                        startActivity(intent);
                        isOpenDetailsScreen = false;
                    }
                } else {
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
            }
        });
    }

    private void initScreen() {
        mUnits = getResources().getStringArray(R.array.qty_options);
        mPackings = getResources().getStringArray(R.array.packing);

        Chemical_Name = (TextView) findViewById(R.id.chemicalName2);
        InvoiceNo = (TextView) findViewById(R.id.InvoiceNo2);
        Total_Brokerage = (TextView) findViewById(R.id.total_brokerage);
        Date = (TextView) findViewById(R.id.date2);
        basic_charge = (TextView) findViewById(R.id.basic_charage);
        Total_charges = (TextView) findViewById(R.id.total_charges);
        units_available = (TextView) findViewById(R.id.units_available);
        created_user_brokerage = (TextView) findViewById(R.id.created_user_brokerage);
        assigned_user_brokerage = (TextView) findViewById(R.id.assigned_user_brokerage);

        created_user_name = (TextView) findViewById(R.id.created_user_name);
        created_user_type = (TextView) findViewById(R.id.created_user_type);
        broker_name = (TextView) findViewById(R.id.broker_name);
        assigned_user_name = (TextView) findViewById(R.id.assigned_user_name);
        assigned_user_type = (TextView) findViewById(R.id.assigned_user_type);

        created_user_image = (ImageView) findViewById(R.id.created_user_image);
        assigned_user_image = (ImageView) findViewById(R.id.assigned_user_image);
        broker_image = (ImageView) findViewById(R.id.broker_image);

        layout_created_user_brokerage = (LinearLayout) findViewById(R.id.layout_created_user_brokerage);
        layout_assigned_user_brokerage = (LinearLayout) findViewById(R.id.layout_assigned_user_brokerage);

        String availableLabel = mLead.getType().startsWith("B")?" to Buy":" to Sell";
        String unitsAvailable = mLead.getQty() + " " + mUnits[mLead.getQtyUnit()] + availableLabel;
        units_available.setText(unitsAvailable);
        Chemical_Name.setText(mLead.getItemName());
        if (!TextUtils.isEmpty(mLeadStatusHistory.getInvoiceNumber())) {
            InvoiceNo.setText(Html.fromHtml("<b> Invoice No: </b>" + mLeadStatusHistory.getInvoiceNumber()));
        } else {
            InvoiceNo.setVisibility(View.GONE);
        }
        if(TextUtils.isEmpty(mLead.getDealDoneDttm())) {
            Date.setText(mLead.getLastUpdDateTime());
        } else {
            Date.setText(mLead.getDealDoneDttm());
        }
        String basicPriceString = mLead.getBasicPrice() + " Rs/" + mUnits[mLead.getBasicPriceUnit()];
        basic_charge.setText(basicPriceString);
        float basicPriceAmt = mLead.getBasicPrice() * mLead.getQty();
        float excisePriceAmt = mLead.getExciseDuty() * mLead.getQty();
        float taxperc = mLead.getTax();
        float taxAmountrs = basicPriceAmt * (taxperc / 100);
        float totalAmt = basicPriceAmt + taxAmountrs +  mLead.getTransportCharges() + mLead.getMiscCharges();
        Integer exciseType = mLead.getExcisetype() != null ? mLead.getExcisetype(): ExciseType.INCLUSIVE.getType();
        if(exciseType.equals(ExciseType.INCLUSIVE)) {
        } else {
            totalAmt += excisePriceAmt;
        }
        Total_charges.setText(totalAmt + " Rs");

        User createdUser = mLead.getCreatedUser();
        User brokerUser = mLead.getBroker();
        User assignedUser = mLead.getAssignedToUser();
        if (mLead.getType().equals(LeadType.BUYER.getType())) {
            created_user_type.setText("(Buyer)");
            assigned_user_type.setText("(Seller)");
            if (me.getUserID().equals(mLead.getCreatedUserID())) {
                Total_Brokerage.setText(mLead.getBuyerBrokerage() + " RS");
            } else {
                Total_Brokerage.setText(mLead.getSellerBrokerage() + " RS");
            }
        } else {
            created_user_type.setText("(Seller)");
            assigned_user_type.setText("(Buyer)");
            if (me.getUserID().equals(mLead.getCreatedUserID())) {
                Total_Brokerage.setText(mLead.getSellerBrokerage() + " RS");
            } else {
                Total_Brokerage.setText(mLead.getBuyerBrokerage() + " RS");
            }
        }
        created_user_name.setText(createdUser.getFullName());
        assigned_user_name.setText(assignedUser.getFullName());
        broker_name.setText(brokerUser.getFullName());
        if (me.getUserID().equals(mLead.getCreatedUserID())) {
            created_user_name.setText("You");
        } else if (assignedUser.getUserID().equals(mLead.getCreatedUserID())) {
            assigned_user_name.setText("You");
        }

        if (me.isBroker()) {
            if (mLead.getType().equals(LeadType.BUYER.getType())) {
                assigned_user_brokerage.setText(mLead.getSellerBrokerage() + " Rs");
                created_user_brokerage.setText(mLead.getBuyerBrokerage() + " Rs");
            } else {
                assigned_user_brokerage.setText(mLead.getSellerBrokerage() + " Rs");
                created_user_brokerage.setText(mLead.getBuyerBrokerage() + " Rs");
            }
            float totalBrokerage = mLead.getBuyerBrokerage() + mLead.getSellerBrokerage();
            Total_Brokerage.setText(totalBrokerage + " RS");
            broker_name.setText("You");
        } else {
            layout_created_user_brokerage.setVisibility(View.INVISIBLE);
            layout_assigned_user_brokerage.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(createdUser.getImageURL())) {
            String imgUrl = SingletonRestClient.BASE_PROFILE_IMAGE_URL + "thumb_" + createdUser.getImageURL();
            Picasso.with(mContext).load(imgUrl)
                    .error(R.drawable.ic_personal_circular_symbol)
                    .placeholder(R.drawable.ic_personal_circular_symbol)
                    .into(created_user_image);
        }

        if (!TextUtils.isEmpty(assignedUser.getImageURL())) {
            String imgUrl = SingletonRestClient.BASE_PROFILE_IMAGE_URL + "thumb_" + assignedUser.getImageURL();
            Picasso.with(mContext).load(imgUrl)
                    .error(R.drawable.ic_personal_circular_symbol)
                    .placeholder(R.drawable.ic_personal_circular_symbol)
                    .into(assigned_user_image);
        }

        if (!TextUtils.isEmpty(brokerUser.getImageURL())) {
            String imgUrl = SingletonRestClient.BASE_PROFILE_IMAGE_URL + "thumb_" + brokerUser.getImageURL();
            Picasso.with(mContext).load(imgUrl)
                    .error(R.drawable.ic_personal_circular_symbol)
                    .placeholder(R.drawable.ic_personal_circular_symbol)
                    .into(broker_image);
        }

        TextView lblViewDetails = (TextView) findViewById(R.id.lbl_view_details);
        lblViewDetails.setText(Html.fromHtml("<u><i>View Details</i></u>"));
        lblViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyHistoryDetailsActivity.this, LeadStatusHistoryDetailsActivity.class);
                intent.putExtra(LeadStatusHistory.KEY_STATUS_HISTORY, mLeadStatusHistory);
                intent.putExtra("IsSeller", isSeller);
                startActivity(intent);
            }
        });
        switch (mLeadStatusHistory.getCurrentStatus()) {
            case 6:
                ((ImageView) findViewById(R.id.circle_status6)).setImageResource(R.drawable.accept_circle);
            case 5:
                ((ImageView) findViewById(R.id.circle_status5)).setImageResource(R.drawable.accept_circle);
            case 4:
                ((ImageView) findViewById(R.id.circle_status4)).setImageResource(R.drawable.accept_circle);
            case 3:
                ((ImageView) findViewById(R.id.circle_status3)).setImageResource(R.drawable.accept_circle);
            case 2:
                ((ImageView) findViewById(R.id.circle_status2)).setImageResource(R.drawable.accept_circle);
            case 1:
                ((ImageView) findViewById(R.id.circle_status1)).setImageResource(R.drawable.accept_circle);
                break;

        }

        if (me.isBroker()) {
            isSeller = false;
        } else if (mLead.getType().equals(LeadType.BUYER.getType())) {
            if (mLead.getAssignedToUserID().equals(me.getUserID())) {
                isSeller = true;
            }
        } else {
            if (mLead.getCreatedUserID() == me.getUserID()) {
                isSeller = true;
            }
        }
        if (isSeller) {
            switch (mLeadStatusHistory.getCurrentStatus()) {
                case 1:
                    findViewById(R.id.layout_staus2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeLeadStatus(2, "Goods lifted");
                        }
                    });
                    break;
                case 2:
                    findViewById(R.id.layout_staus3).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeLeadStatus(3, "Documents Attached");
                        }
                    });
                    break;
                case 3:
                    findViewById(R.id.layout_staus4).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeLeadStatus(4, "Payment Received");
                        }
                    });
                    break;
                case 4:
                    findViewById(R.id.layout_staus5).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeLeadStatus(5, "Documents Received");
                        }
                    });
                    break;
                case 5:
                    findViewById(R.id.layout_staus6).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeLeadStatus(6, "Deal Cleared");
                        }
                    });
                    break;
            }
        }
    }

    private void changeLeadStatus(final int currentStatus, String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Status ?");
        builder.setMessage(Html.fromHtml("Are you sure, do you want to change status of this deal to <b>" + status + " ?</b> "));
        // Set up the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mLeadStatusHistory.setCurrentStatus(currentStatus);
                switch (currentStatus) {
                    case 2:
                        askForInvoiceNo();
                        break;
                    case 3:
                        mLeadStatusHistory.setDocumentsAttachedDateTime(ApptDateUtils.getCurrentFormatedDateAndTimeString());
                        callLSaveLeadStatusHistoryService();
                        break;
                    case 4:
                        mLeadStatusHistory.setPaymentReceivedDateTime(ApptDateUtils.getCurrentFormatedDateAndTimeString());
                        callLSaveLeadStatusHistoryService();
                        break;
                    case 5:
                        mLeadStatusHistory.setDocumentsreceivedDateTime(ApptDateUtils.getCurrentFormatedDateAndTimeString());
                        callLSaveLeadStatusHistoryService();
                        break;
                    case 6:
                        mLeadStatusHistory.setDealClearedDateTime(ApptDateUtils.getCurrentFormatedDateAndTimeString());
                        callLSaveLeadStatusHistoryService();
                        break;
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void askForInvoiceNo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Invoice No");
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_input_box, null, false);
        // Set up the input
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String invoiceNo = input.getText().toString();
                mLeadStatusHistory.setInvoiceNumber(invoiceNo);
                mLeadStatusHistory.setGoodsLiftedDateTime(ApptDateUtils.getCurrentFormatedDateAndTimeString());
                callLSaveLeadStatusHistoryService();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void callLSaveLeadStatusHistoryService() {
        final Dialog dialog = AppProgressDialog.show(mContext);
        ObjectFactory.getInstance().getLeadServiceInstance().saveLeadStatusHistory(mLeadStatusHistory, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if (messageDTO.isSuccess()) {
                    mLeadStatusHistory = LeadStatusHistory.createFromJSON(messageDTO.getData());
                    initScreen();
                    dialog.dismiss();
                } else {
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
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
