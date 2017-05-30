package com.firstidea.android.brokerx;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firstidea.android.brokerx.constants.MsgConstants;
import com.firstidea.android.brokerx.enums.ConnectionStatus;
import com.firstidea.android.brokerx.enums.LeadType;
import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.model.DropDownValuesDTO;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.utils.StringUtils;
import com.firstidea.android.brokerx.widget.AppProgressDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NewBrokerAnalysisActivity extends AppCompatActivity {
    private Spinner  mSpinner2, mSpinner3, buyerSpinner, sellerSpinner, statusSpinner;
    private LinearLayout mTop11Broker, mTop11Broker1;
    private Context mContext = this;

    private TextView mStartDateView, mEndDateView;
    private Calendar cal;
    private int startDay, endDay;
    private int startMonth, endMonth;
    private int startYear, endYear;
    private User me;
    private Date mStartDate, mEndDate;
    private List<User> mUsers;
    SimpleDateFormat SDF = new SimpleDateFormat("dd MMM yyyy");
    private int mSelectedBrokerID, mOtherUserID;// mSelectedBuyerID, mSelectedSellerID;
    private String mSelectedBrokerName = "";
    private DropDownValuesDTO dropDownValues;
    private String brokerageStatus="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_broker_analysis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Analysis");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

//        mSpinner1 = (Spinner) findViewById(R.id.spinner1);
        mSpinner2 = (Spinner) findViewById(R.id.spinner2);
        mSpinner3 = (Spinner) findViewById(R.id.spinner3);
        buyerSpinner = (Spinner) findViewById(R.id.buyer_spinner);
        sellerSpinner = (Spinner) findViewById(R.id.seller_spinner);
        statusSpinner = (Spinner) findViewById(R.id.status_spinner);
/*

        mTop11Broker = (LinearLayout) findViewById(R.id.top11BrokerLinear);
        mTop11Broker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent topBroker = new Intent(mContext, TopBrokersActivity.class);
                topBroker.putExtra("type","S");
                startActivity(topBroker);
            }
        });
        mTop11Broker1 = (LinearLayout) findViewById(R.id.top11BrokerLinear1);
        mTop11Broker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent topBroker = new Intent(mContext, TopBrokersActivity.class);
                topBroker.putExtra("type","B");
                startActivity(topBroker);
            }
        });
*/

        mStartDateView = (TextView) findViewById(R.id.starDate);
        mEndDateView = (TextView) findViewById(R.id.endDate);
        me = User.getSavedUser(this);

        cal = Calendar.getInstance();
        startDay = endDay = cal.get(Calendar.DAY_OF_MONTH);
        startMonth = endMonth = cal.get(Calendar.MONTH);
        startYear = endYear = cal.get(Calendar.YEAR);
        findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = mSpinner2.getSelectedItem().toString();
                String type = "";
                if (mSpinner3.getSelectedItemPosition() != 0) {
                    type = mSpinner3.getSelectedItemPosition() == 1 ? "B" : "S";
                }
                Intent intent = new Intent(mContext, MyHistoryActivity.class);
                intent.putExtra("IS_FROM_ANALYSYS", true);
                intent.putExtra("IS_BROKER_ANALYSYS", true);
                intent.putExtra("BrokerageStatus", brokerageStatus);
                if (item.equals("Any")) {
                    item = "";
                }
                intent.putExtra("ItemName", item);
                if (mStartDate != null) {
                    intent.putExtra("StartDate", mStartDate.getTime());
                }
                if (mEndDate != null) {
                    intent.putExtra("EnndDate", mEndDate.getTime());
                }
                intent.putExtra(LeadType.KEY_LEAD_TYPE, type);
                intent.putExtra("BrokerID", "0");
                intent.putExtra("UserID", me.getUserID());
                intent.putExtra("OtherUsrerID", mOtherUserID);
                startActivity(intent);
            }
        });

        getMyCircle();
    }

    private void getAnalysisDropDownValues() {
        final Dialog dialog = AppProgressDialog.show(mContext);
        ObjectFactory.getInstance().getUserServiceInstance().getAnalysisDropDownValues(me.getUserID(), new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if (messageDTO.getMessageID().equals(MsgConstants.SUCCESS_ID)) {
                    dropDownValues = DropDownValuesDTO.createFromJSON(messageDTO.getData());
                    initialiseSpinnerAdapter();
                } else {
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.dismiss();
            }
        });
    }

    private void getMyCircle() {
        final Dialog dialog = AppProgressDialog.show(mContext);
        ObjectFactory.getInstance().getUserServiceInstance().getUserConnections(me.getUserID(), new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if (messageDTO.getMessageID().equals(MsgConstants.SUCCESS_ID)) {
                    mUsers = User.createListFromJson(messageDTO.getData());
//                    initialiseSpinnerAdapter();
                } else {

                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                getAnalysisDropDownValues();
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.dismiss();
            }
        });
    }


    private void initialiseSpinnerAdapter() {
        final List<User> brokers = new ArrayList<>();
        List<String> brokerNames = new ArrayList<>();
        brokerNames.add("Any");
        for (User user : mUsers) {
            if (user.isBroker() && user.getStatus().equals(ConnectionStatus.ACCEPTED.getStatus())) {
                brokerNames.add(user.getFullName());
                brokers.add(user);
            }
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, brokerNames);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /*mSpinner1.setAdapter(adapter1);

        mSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    position--;
                    List brokerItems = new ArrayList();
                    brokerItems.add("Any");
                    if (brokers.get(position).getBrokerDealsInItems() != null) {
                        brokerItems.addAll(StringUtils.getListOfComaValues(brokers.get(position).getBrokerDealsInItems()));
                    }
                    mSelectedBrokerID = brokers.get(position).getUserID();
                    //Secound Spinner ItemClick Event
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, brokerItems);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpinner2.setAdapter(adapter2);
                } else {
                    mSpinner2.setAdapter(null);
                    List<String> brokerDealsItems = new ArrayList<>();
                    brokerDealsItems.add("Any");
                    if (dropDownValues.getItemsDealWith() != null) {
                        brokerDealsItems.addAll(dropDownValues.getItemsDealWith());
                    }
                    mSelectedBrokerID = 0;
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, brokerDealsItems);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpinner2.setAdapter(adapter2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        List brokerItems = new ArrayList();
        brokerItems.add("Any");
        if(me.getBrokerDealsInItems() != null) {
            brokerItems.addAll(StringUtils.getListOfComaValues(me.getBrokerDealsInItems()));
        }
        //Secound Spinner ItemClick Event
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, brokerItems);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner2.setAdapter(adapter2);

        final String[] spinnerItems = {"Any", "Purchase", "Sales"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, spinnerItems);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner3.setAdapter(adapter3);

        mSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                buyerSpinner.setSelection(0);
                sellerSpinner.setSelection(0);
                if (position == 0) {
                    buyerSpinner.setEnabled(true);
                    sellerSpinner.setEnabled(true);
                } else if (position == 1) {
                    buyerSpinner.setEnabled(false);
                    sellerSpinner.setEnabled(true);
                } else {
                    buyerSpinner.setEnabled(true);
                    sellerSpinner.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final String[] statusSpinnerItems = {"Any", "To be received", "Received"};
        ArrayAdapter<String> statusSpinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, statusSpinnerItems);
        statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusSpinnerAdapter);

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    brokerageStatus = "";
                } else if(position ==1) {
                    brokerageStatus = "pending";
                } else {
                    brokerageStatus = "received";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final List<User> buyers = new ArrayList<>();
        List<String> buyerNames = new ArrayList<>();
        buyerNames.add("Any");
        if (dropDownValues.getBuyers() != null) {
            for (User user : dropDownValues.getBuyers()) {
                buyerNames.add(user.getFullName());
                buyers.add(user);
            }
        }
        ArrayAdapter<String> adapterBuyer = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, buyerNames);
        adapterBuyer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buyerSpinner.setAdapter(adapterBuyer);

        buyerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    mOtherUserID = buyers.get(position - 1).getUserID();
                } else {
                    mOtherUserID = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final List<User> sellers = new ArrayList<>();
        List<String> sellerNames = new ArrayList<>();
        sellerNames.add("Any");
        if (dropDownValues.getSellers() != null) {
            for (User user : dropDownValues.getSellers()) {
                sellerNames.add(user.getFullName());
                sellers.add(user);
            }
        }
        ArrayAdapter<String> adapterSeller = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sellerNames);
        adapterSeller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sellerSpinner.setAdapter(adapterSeller);

        sellerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    mOtherUserID = sellers.get(position - 1).getUserID();
                } else {
                    mOtherUserID = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void setStartDate(View view) {
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startYear = year;
                startMonth = monthOfYear;
                startDay = dayOfMonth;
                mStartDate = calendar.getTime();
                mStartDateView.setText(SDF.format(mStartDate));
            }
        }, startYear, startMonth, startDay);
        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.HOUR_OF_DAY, 1);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);
        dpd.getDatePicker().setMaxDate(c1.getTimeInMillis());
        dpd.show();
    }


    public void setEndDate(View view) {
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endYear = year;
                endMonth = monthOfYear;
                endDay = dayOfMonth;
                mEndDate = calendar.getTime();
                mEndDateView.setText(SDF.format(mEndDate));
            }
        }, endYear, endMonth, endDay);
        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.HOUR_OF_DAY, 1);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);
        dpd.getDatePicker().setMaxDate(c1.getTimeInMillis());
        dpd.show();
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
