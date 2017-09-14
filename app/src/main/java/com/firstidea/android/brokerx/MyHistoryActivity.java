package com.firstidea.android.brokerx;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firstidea.android.brokerx.adapter.MyHistoryAdapter;
import com.firstidea.android.brokerx.enums.ExciseType;
import com.firstidea.android.brokerx.enums.LeadType;
import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.utils.AppUtils;
import com.firstidea.android.brokerx.utils.SharedPreferencesUtil;
import com.firstidea.android.brokerx.widget.AppProgressDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyHistoryActivity extends AppCompatActivity {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;

    private Context mContext;
    private ArrayList<Lead> mLeads;
    private User me;
    private boolean mIsFromAnalysis = false, isBrokerAnalysis;
    private Integer brokerID = null;
    private String itemName = null, brokerageStatus = "";
    private String leadType = null;

    private TextView mStartDateView, mEndDateView;
    private int startDay, endDay;
    private int startMonth, endMonth;
    private int startYear, endYear;
    private Date mStartDate, mEndDate;
    SimpleDateFormat SDF = new SimpleDateFormat("dd MMM yyyy");
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int userID, otherUserID;
    private LinearLayout totalAmountLayout;
    private TextView lblTotalAmount;
    private TextView totalAmountValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mContext = this;
        me = User.getSavedUser(this);
        mStartDateView = (TextView) findViewById(R.id.starDate);
        mEndDateView = (TextView) findViewById(R.id.endDate);
        lblTotalAmount = (TextView) findViewById(R.id.lbl_total_amount);
        totalAmountValue = (TextView) findViewById(R.id.total_amount_value);
        totalAmountLayout = (LinearLayout) findViewById(R.id.total_amount_layout);

        if (getIntent().hasExtra("IS_FROM_ANALYSYS")) {
            mIsFromAnalysis = getIntent().getExtras().getBoolean("IS_FROM_ANALYSYS");
            if (getIntent().hasExtra("BrokerageStatus")) {
                brokerageStatus = getIntent().getExtras().getString("BrokerageStatus");
            }
            if (getIntent().hasExtra("IS_BROKER_ANALYSYS")) {
                isBrokerAnalysis = getIntent().getExtras().getBoolean("IS_BROKER_ANALYSYS");
            }
            itemName = getIntent().getExtras().getString("ItemName");
            userID = getIntent().getExtras().getInt("UserID");
            otherUserID = getIntent().getExtras().getInt("OtherUsrerID");
            findViewById(R.id.date_range_layout).setVisibility(View.GONE);
            getSupportActionBar().setTitle("Analysis: " + itemName);
            if (getIntent().hasExtra("BrokerID")) {
                brokerID = getIntent().getExtras().getInt("BrokerID");
            }
            if (getIntent().hasExtra(LeadType.KEY_LEAD_TYPE)) {
                leadType = getIntent().getExtras().getString(LeadType.KEY_LEAD_TYPE);
            }
            if (getIntent().hasExtra("StartDate") && getIntent().hasExtra("EnndDate")) {
                long startDate = getIntent().getExtras().getLong("StartDate");
                long endDate = getIntent().getExtras().getLong("EnndDate");
                mStartDate = new Date(startDate);
                mEndDate = new Date(endDate);
                mStartDateView.setText(SDF.format(mStartDate));
                mEndDateView.setText(SDF.format(mEndDate));
                Calendar endCalendar = Calendar.getInstance();
                endCalendar.setTime(mEndDate);
                endYear = endCalendar.get(Calendar.YEAR);
                endMonth = endCalendar.get(Calendar.MONTH);
                endDay = endCalendar.get(Calendar.DAY_OF_MONTH);

                Calendar startCalendar = Calendar.getInstance();
                startCalendar.setTime(mEndDate);
                startYear = startCalendar.get(Calendar.YEAR);
                startMonth = startCalendar.get(Calendar.MONTH);
                startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
            }

        }

        Calendar cal = Calendar.getInstance();
        startDay = endDay = cal.get(Calendar.DAY_OF_MONTH);
        startMonth = endMonth = cal.get(Calendar.MONTH);
        startYear = endYear = cal.get(Calendar.YEAR);


        mRecyclerView = (RecyclerView) findViewById(R.id.history_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark,
                R.color.colorPrimaryLight,
                R.color.teal,
                R.color.teal);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                if (mIsFromAnalysis) {
                    getLeads();
                } else {
                    getHistory();
                }
            }
        });

        if (mIsFromAnalysis) {
            getLeads();
        } else {
            getHistory();
        }

        findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStartDateView.getText().toString().trim().length() > 0
                        && mEndDateView.getText().toString().trim().length() > 0) {
                    if (mIsFromAnalysis) {
                        getLeads();
                    } else {
                        getHistory();
                    }
                } else {
                    Toast.makeText(mContext, "Please Select Start and End Date", Toast.LENGTH_SHORT).show();
                }
            }
        });
        invalidateOptionsMenu();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!mIsFromAnalysis) {
            return super.onCreateOptionsMenu(menu);
        }
        getMenuInflater().inflate(R.menu.menu_analysis, menu);
        return super.onCreateOptionsMenu(menu);
    }

/*

    private void exportAnalysis() {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Datatypes in Java");
        Object[][] datatypes =new Object[mLeads.size()+1][12];
*/
/*

        {
                {"Item", "Buyer", "Broker", "Seller", "Deal Date","Quantity","Unit","Basic Charges","Transport Charges", "Misc. Chrges", "Tax Amount", "Total Amount"},
                {"int", "Primitive", 2},
                {"float", "Primitive", 4},
                {"double", "Primitive", 8},
                {"char", "Primitive", 1},
                {"String", "Non-Primitive", "No fixed size"}
        };
*//*

        datatypes[0] = new Object[]{"Item", "Buyer", "Broker", "Seller", "Deal Date","Quantity","Unit","Basic Charges","Transport Charges", "Misc. Chrges", "Tax Amount", "Total Amount"};
        for(int i=1; i<=mLeads.size();i++) {
            Lead lead = mLeads.get(i-1);
            int j=0;
            User createdUser = lead.getCreatedUser();
            User brokerUser = lead.getBroker();
            User assignedUser = lead.getAssignedToUser();
            String buyer, broker = brokerUser.getFullName(), seller;
            if (lead.getType().equals(LeadType.BUYER.getType())) {
                buyer = createdUser.getFullName();
                seller = assignedUser.getFullName();
            } else {
                seller = createdUser.getFullName();
                buyer = assignedUser.getFullName();
            }
            datatypes[i][j++] = lead.getItemName();
            datatypes[i][j++] = buyer;
            datatypes[i][j++] = broker;
            datatypes[i][j++] = seller;

        }
        int rowNum = 0;
        System.out.println("Creating excel");

        for (Object[] datatype : datatypes) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : datatype) {
                Cell cell = row.createCell(colNum++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(AppUtils.getAnalysisFilePath(this));
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(mContext, "Excel File Created", Toast.LENGTH_SHORT).show();
        Log.d("EXCEL Writer","Done");
    }
*/

    private void getLeads() {
        final Dialog dialog = AppProgressDialog.show(mContext);
        String startDate = null, enadDate = null;
        if (mStartDate != null && mEndDate != null) {
            SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
            startDate = SDF.format(mStartDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mEndDate);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date newEndDate = calendar.getTime();
            enadDate = SDF.format(newEndDate);
        }
        ObjectFactory.getInstance().getLeadServiceInstance().getAnalysisLeads(me.getUserID(), otherUserID, leadType, brokerageStatus, itemName, brokerID, startDate, enadDate, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if (messageDTO.isSuccess()) {
                    ArrayList<Lead> leads = Lead.createListFromJson(messageDTO.getData());
                    mLeads = new ArrayList<Lead>();
                    mLeads.addAll(leads);
                    float totalLeadAmount = 0;

                    if (isBrokerAnalysis) {
                        for (Lead mLead : mLeads) {
                            totalLeadAmount += mLead.getBuyerBrokerage() + mLead.getSellerBrokerage();
                        }
                    } else {
                        for (Lead mLead : mLeads) {
                            float basicPriceAmt = mLead.getBasicPrice() * mLead.getQty();
                            float excisePriceAmt = mLead.getExciseDuty() * mLead.getQty();
                            float taxperc = mLead.getTax();
                            float taxAmountrs = basicPriceAmt * (taxperc / 100);
                            float totalAmt = basicPriceAmt + taxAmountrs + mLead.getTransportCharges() + mLead.getMiscCharges();
                            Integer exciseType = mLead.getExcisetype() != null ? mLead.getExcisetype() : ExciseType.INCLUSIVE.getType();
                            if (exciseType.equals(ExciseType.INCLUSIVE)) {
                            } else {
                                totalAmt += excisePriceAmt;
                            }
                            totalLeadAmount += totalAmt;
                        }
                    }
                    totalAmountLayout.setVisibility(View.VISIBLE);
                    if (isBrokerAnalysis) {
                        lblTotalAmount.setText("Total Brokerage");
                    }
                    totalAmountValue.setText("Rs. " + totalLeadAmount + "/-");
                    //TODO Show total amount label
                    MyHistoryAdapter mAdapter = new MyHistoryAdapter(mContext, mLeads, me.isBroker());
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getHistory() {
        final Dialog dialog = AppProgressDialog.show(mContext);
        String startDate = null, enadDate = null;
        if (mStartDate != null && mEndDate != null) {
            SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
            startDate = SDF.format(mStartDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mEndDate);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date newEndDate = calendar.getTime();
            enadDate = SDF.format(newEndDate);
        }
        ObjectFactory.getInstance().getLeadServiceInstance().getHistory(me.getUserID(), startDate, enadDate, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if (messageDTO.isSuccess()) {
                    ArrayList<Lead> leads = Lead.createListFromJson(messageDTO.getData());
                    mLeads = new ArrayList<Lead>();
                    mLeads.addAll(leads);
                    MyHistoryAdapter mAdapter = new MyHistoryAdapter(mContext, mLeads, me.isBroker());
                    mRecyclerView.setAdapter(mAdapter);
                    if(getIntent().hasExtra("IS_FROM_NOTIFICATION") && getIntent().getBooleanExtra("IS_FROM_NOTIFICATION", false)) {
                        Integer leadID = getIntent().getIntExtra("NOTIFICATION_ID", 0);
                        for(Lead lead: mLeads) {
                            if(lead.getLeadID().equals(leadID)) {
                                Intent next = new Intent(MyHistoryActivity.this, MyHistoryDetailsActivity.class);
                                next.putExtra(Lead.KEY_LEAD,lead);
                                startActivity(next);
                                getIntent().removeExtra("IS_FROM_NOTIFICATION");
                                break;
                            }
                        }
                    }
                } else {
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
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
        if(item.getItemId() == R.id.action_excel) {
//            exportAnalysis();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, android.R.anim.slide_out_right);
    }
}
