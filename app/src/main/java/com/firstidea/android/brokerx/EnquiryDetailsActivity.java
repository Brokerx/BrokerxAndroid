package com.firstidea.android.brokerx;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firstidea.android.brokerx.Chat.ChatListActivity;
import com.firstidea.android.brokerx.enums.LeadCurrentStatus;
import com.firstidea.android.brokerx.enums.LeadType;
import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.SingletonRestClient;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.widget.AppProgressDialog;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EnquiryDetailsActivity extends AppCompatActivity {

    @BindView(R.id.item_name)
    TextView itemName;
    @BindView(R.id.make)
    TextView make;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.basic_price_per_unit)
    TextView basicPricePerUnit;
    @BindView(R.id.qty_unit)
    TextView qtyUnit;
    @BindView(R.id.item_packing)
    TextView packing;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_type)
    TextView userType;
    @BindView(R.id.brokerage_amt)
    TextView brokerageAmt;
    @BindView(R.id.basic_price_with_unit)
    TextView basicPriceWithUnit;
    @BindView(R.id.excise_duty_with_unit)
    TextView exciseDutyWithUnit;
    @BindView(R.id.transport_charges)
    TextView transportCharges;
    @BindView(R.id.misc_charges)
    TextView miscCharges;
    @BindView(R.id.total_charges)
    TextView totalCharges;
    @BindView(R.id.against_form)
    TextView againstForm;
    @BindView(R.id.credit_period)
    TextView creditPeriod;
    @BindView(R.id.free_storage)
    TextView freeStorage;
    @BindView(R.id.prefered_seller_name)
    TextView preferedSellerName;
    @BindView(R.id.comments)
    TextView comments;
    @BindView(R.id.cur_status)
    TextView currentStatus;
    @BindView(R.id.lbl_accept)
    TextView lblAccept;
    @BindView(R.id.lbl_reject)
    TextView lblReject;
    @BindView(R.id.lbl_chat)
    TextView lblChat;
    @BindView(R.id.lbl_revert)
    TextView lblRevert;
    @BindView(R.id.lbl_cancelled)
    TextView lblCancelled;
    @BindView(R.id.lbl_reopen)
    TextView lblReOpen;
    @BindView(R.id.view_status)
    TextView btnViewHistory;
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.cur_statusIcon)
    ImageView curStatusIcon;
    @BindView(R.id.btn_reject)
    LinearLayout btnReject;
    @BindView(R.id.btn_accept)
    LinearLayout btnAccept;
    @BindView(R.id.revert_enquiery)
    LinearLayout btnRevert;
    @BindView(R.id.chat)
    LinearLayout btnChat;
    @BindView(R.id.btn_reopen)
    LinearLayout btnReOpen;
    @BindView(R.id.btn_pending)
    LinearLayout btnPending;
    @BindView(R.id.acc_rej_layout)
    LinearLayout Rej_Acc_Layout;
    @BindView(R.id.revert_layout)
    LinearLayout Revert_Chat_layout;
    @BindView(R.id.cur_status_layout)
    LinearLayout CurStatusLayout;

    private Lead mLead;
    private User me;
    private String[] mUnits, mPackings;

    private final int ASIGN_USER_REQ_CODE= 100;

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
        mUnits = getResources().getStringArray(R.array.qty_options);
        mPackings = getResources().getStringArray(R.array.packing);
        CurStatusLayout.setVisibility(View.GONE);

        initScreen();
        String viewHistoryLabel = "<u><i>View History</i></u>";
        btnViewHistory.setText(Html.fromHtml(viewHistoryLabel));
        btnViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ViewAccHistory = new Intent(EnquiryDetailsActivity.this, ViewHistoryActivity.class);
                ViewAccHistory.putExtra(Lead.KEY_LEAD_ID, mLead.getLeadID());
                startActivity(ViewAccHistory);
            }
        });


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatus(LeadCurrentStatus.Accepted.getStatus(), LeadCurrentStatus.Accepted.getStatus());
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatus(LeadCurrentStatus.Rejected.getStatus(), LeadCurrentStatus.Rejected.getStatus());
            }
        });
        btnRevert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnquiryDetailsActivity.this, AddEnquiryFirstActivity.class);
                intent.putExtra(Lead.KEY_LEAD, mLead);
                startActivity(intent);
            }
        });
    }

    private void changeStatus(String myStatus, String otherUserStatus) {
        if (me.isBroker()) {
            mLead.setBrokerStatus(myStatus);
            if (mLead.getType().equals(LeadType.BUYER.getType())) {
                mLead.setBuyerStatus(otherUserStatus);
            } else {
                mLead.setSellerStatus(otherUserStatus);
            }
        } else {
            if (mLead.getType().equals(LeadType.BUYER.getType())) {
                mLead.setBuyerStatus(myStatus);
            } else {
                mLead.setSellerStatus(myStatus);
            }
            mLead.setBrokerStatus(otherUserStatus);
        }

        final Dialog dialog = AppProgressDialog.show(this);
       /* LeadService leadService = SingletonRestClient.createService(LeadService.class, this);
        leadService.saveLead(mLead, new Callback<MessageDTO>() {*/
        ObjectFactory.getInstance().getLeadServiceInstance().saveLead(mLead, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                dialog.dismiss();
                initScreen();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(EnquiryDetailsActivity.this, "Some Error Occurred, Please Try again", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void initScreen() {
        itemName.setText(mLead.getItemName());
        String makeString = "<b>Make: </b>" + mLead.getMake();
        make.setText(Html.fromHtml(makeString));
        location.setText(mLead.getLocation());
        String basicPriceString = mLead.getBasicPrice() + " Rs/" + mUnits[mLead.getBasicPriceUnit()];
        basicPricePerUnit.setText(basicPriceString);
        qtyUnit.setText(mLead.getQty() + " " + mUnits[mLead.getQtyUnit()] + " available");
        String packingString = mPackings[mLead.getPacking()];
        packing.setText(packingString);

        String userNameString = "", userImageUrl = "", userTypeString = "";
        if (me.getUserID().equals(mLead.getCreatedUserID())) {
            userNameString = mLead.getBroker().getFullName();
            userImageUrl = mLead.getBroker().getImageURL();
            userTypeString = "Broker";
        } else {
            userNameString = mLead.getCreatedUser().getFullName();
            userImageUrl = mLead.getCreatedUser().getImageURL();
            userTypeString = mLead.getType().toLowerCase().startsWith("B") ? "Buyer" : "Seller";
        }

        userName.setText(userNameString);
        userType.setText(userTypeString);
        brokerageAmt.setText(mLead.getBrokerageAmt() + " Rs");
        if (!TextUtils.isEmpty(userImageUrl)) {
            String imgUrl = SingletonRestClient.BASE_PROFILE_IMAGE_URL + "thumb_" + userImageUrl;
            Picasso.with(this).load(imgUrl)
                    .error(R.drawable.user_profile)
                    .placeholder(R.drawable.user_profile)
                    .into(userImage);
        }
        basicPriceWithUnit.setText(basicPriceString);
        exciseDutyWithUnit.setText(mLead.getExciseUnit() + " Rs/" + mUnits[mLead.getExciseUnit()]);
        transportCharges.setText(mLead.getTransportCharges() + " Rs/" + mUnits[mLead.getBasicPriceUnit()]);
        miscCharges.setText(mLead.getMiscCharges() + " Rs");
        float basicPriceAmt = mLead.getBasicPrice() * mLead.getQty();
        float excisePriceAmt = mLead.getExciseDuty() * mLead.getQty();
        float totalAmt = basicPriceAmt + excisePriceAmt + mLead.getTransportCharges() + mLead.getMiscCharges();
        totalCharges.setText(totalAmt + " Rs");
        againstForm.setText(mLead.getAgainstForm());
        creditPeriod.setText(mLead.getCreditPeriod() + " days");
        freeStorage.setText(mLead.getFreeStoragePeriod() + " days");
        preferedSellerName.setText(mLead.getPreferredSellerName());
        comments.setText(mLead.getComments());
        String myStatus = "";
        String otherUserStatus = "";
        if (me.isBroker()) {
            myStatus = mLead.getBrokerStatus();
            if (mLead.getType().equals(LeadType.BUYER.getType())) {
                otherUserStatus = mLead.getBuyerStatus();
            } else {
                otherUserStatus = mLead.getSellerStatus();
            }
        } else {
            if (mLead.getType().equals(LeadType.BUYER.getType())) {
                myStatus = mLead.getBuyerStatus();
            } else {
                myStatus = mLead.getSellerStatus();
            }
            otherUserStatus = mLead.getBrokerStatus();
        }
        if (TextUtils.isEmpty(myStatus)) {
            Revert_Chat_layout.setVisibility(View.GONE);
            Rej_Acc_Layout.setVisibility(View.VISIBLE);
            return;
        } else {
            if (myStatus.equals(LeadCurrentStatus.Waiting.getStatus())) {
                Revert_Chat_layout.setVisibility(View.VISIBLE);
                Rej_Acc_Layout.setVisibility(View.VISIBLE);
                btnPending.setVisibility(View.VISIBLE);
                btnPending.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeStatus(LeadCurrentStatus.Pending.getStatus(), LeadCurrentStatus.Pending.getStatus());
                    }
                });
            } else if (myStatus.equals(LeadCurrentStatus.Reverted.getStatus())) {
                Revert_Chat_layout.setVisibility(View.VISIBLE);
                Rej_Acc_Layout.setVisibility(View.GONE);
                lblRevert.setText("Move to Pending");
                lblRevert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeStatus(LeadCurrentStatus.Pending.getStatus(), LeadCurrentStatus.Pending.getStatus());
                    }
                });
            } else if (myStatus.equals(LeadCurrentStatus.Rejected.getStatus())) {
                Revert_Chat_layout.setVisibility(View.GONE);
                Rej_Acc_Layout.setVisibility(View.GONE);
                lblCancelled.setVisibility(View.VISIBLE);
            } else if (myStatus.equals(LeadCurrentStatus.Pending.getStatus())) {
                btnReOpen.setVisibility(View.VISIBLE);
                btnReOpen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeStatus(LeadCurrentStatus.Waiting.getStatus(), LeadCurrentStatus.Waiting.getStatus());
                    }
                });
                Revert_Chat_layout.setVisibility(View.GONE);
                Rej_Acc_Layout.setVisibility(View.GONE);
            } else if (myStatus.equals(LeadCurrentStatus.Accepted.getStatus())) {
                if (me.isBroker()) {
                    if (otherUserStatus.equals(LeadCurrentStatus.Accepted.getStatus())
                            && mLead.getParentLeadID() != null) {
                        Revert_Chat_layout.setVisibility(View.GONE);
                        Rej_Acc_Layout.setVisibility(View.GONE);
                        btnReOpen.setVisibility(View.VISIBLE);
                        lblReOpen.setText("Deal Done");

                        btnReOpen.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showDelaDoneConfirmation();

                            }
                        });
                    } else {
                        Revert_Chat_layout.setVisibility(View.VISIBLE);
                        Rej_Acc_Layout.setVisibility(View.VISIBLE);
                        lblAccept.setText("Move To Pending");
                        btnAccept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changeStatus(LeadCurrentStatus.Pending.getStatus(), LeadCurrentStatus.Pending.getStatus());
                            }
                        });
                        btnReOpen.setVisibility(View.VISIBLE);
                        if (mLead.getType().equals(LeadType.BUYER.getType())) {
                            lblReOpen.setText("Assign Seller");
                        } else {
                            lblReOpen.setText("Assign Buyer");
                        }
                        btnReOpen.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(EnquiryDetailsActivity.this, MycircleActivity.class);
                                intent.putExtra(Constants.KEY_IS_FOR_SELECTION, true);
                                startActivityForResult(intent, ASIGN_USER_REQ_CODE);;;
                            }
                        });
                    }
                } else {
                    Revert_Chat_layout.setVisibility(View.GONE);
                    Rej_Acc_Layout.setVisibility(View.GONE);
                    btnReOpen.setVisibility(View.VISIBLE);
                    lblReOpen.setText("Chat");
                    btnReOpen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(EnquiryDetailsActivity.this, ChatListActivity.class);
                            intent.putExtra(Lead.KEY_LEAD_ID, mLead.getLeadID());
                            startActivity(intent);
                        }
                    });
                    btnPending.setVisibility(View.VISIBLE);
                    btnPending.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeStatus(LeadCurrentStatus.Pending.getStatus(), LeadCurrentStatus.Pending.getStatus());
                        }
                    });
                }
            }
        }
        CurStatusLayout.setVisibility(View.VISIBLE);
//        Revert_Chat_layout.setVisibility(View.VISIBLE);
//        Rej_Acc_Layout.setVisibility(View.GONE);

        if (myStatus.equals(LeadCurrentStatus.Accepted.getStatus()) || myStatus.equals(LeadCurrentStatus.Reverted.getStatus())) {
            currentStatus.setText("Accepted");
            curStatusIcon.setImageResource(R.drawable.accept_circle);
        } else if (myStatus.equals(LeadCurrentStatus.Rejected.getStatus())) {
            currentStatus.setText("Rejected");
            curStatusIcon.setImageResource(R.drawable.reject_circle);
        } else if (myStatus.equals(LeadCurrentStatus.Pending.getStatus())) {
            currentStatus.setText("Pending");
            curStatusIcon.setImageResource(R.drawable.pending_circle);
        }

    }

    private void showDelaDoneConfirmation() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setTitle("Dela Done ?");
        builder.setMessage("Are you sure, do you want to mark this dela as DONE ?" +
                "\n You will see this deal in your History onwards.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callDeladDoneService();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void callDeladDoneService() {
        final Dialog dialog = AppProgressDialog.show(this);
       /* LeadService leadService = SingletonRestClient.createService(LeadService.class, this);
        leadService.saveLead(mLead, new Callback<MessageDTO>() {*/
        ObjectFactory.getInstance().getLeadServiceInstance().dealDone(mLead.getLeadID(), new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                dialog.dismiss();
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(EnquiryDetailsActivity.this, "Some Error Occurred, Please Try again", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == ASIGN_USER_REQ_CODE) {
            final User selectedUser = data.getExtras().getParcelable(Constants.KEY_SELECTED_USER);
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);
            builder.setTitle("Please Confirm");
            String createdUserType = "Seller";
            if(mLead.getType().equals(LeadType.BUYER.getType())) {
                createdUserType = "Buyer";
            }
            String msg = "Are you sure, do you want to assign <b>"+selectedUser.getFullName()+"</b> as <b>"+createdUserType+"</b> "+ " to this deal ?" ;
            builder.setMessage(Html.fromHtml(msg));
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    asignUser(selectedUser);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();

        }
    }

    private void asignUser(User selectedUser) {
        mLead.setParentLeadID(mLead.getLeadID());
        mLead.setCreatedUserID(selectedUser.getUserID());
        if(mLead.getType().equals(LeadType.BUYER.getType())) {
            mLead.setType(LeadType.SELLER.getType());
            mLead.setSellerStatus(LeadCurrentStatus.Waiting.getStatus());
            mLead.setBuyerStatus(null);
        } else {
            mLead.setType(LeadType.BUYER.getType());
            mLead.setBuyerStatus(LeadCurrentStatus.Waiting.getStatus());
            mLead.setSellerStatus(null);
        }
        mLead.setLeadID(null);
        mLead.setBrokerStatus(LeadCurrentStatus.Reverted.getStatus());
        final Dialog dialog = AppProgressDialog.show(this);
       /* LeadService leadService = SingletonRestClient.createService(LeadService.class, this);
        leadService.saveLead(mLead, new Callback<MessageDTO>() {*/
        ObjectFactory.getInstance().getLeadServiceInstance().saveLead(mLead, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                dialog.dismiss();
                initScreen();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(EnquiryDetailsActivity.this, "Some Error Occurred, Please Try again", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
