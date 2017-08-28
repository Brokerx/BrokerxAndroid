package com.firstidea.android.brokerx;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.widget.AppProgressDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import im.dacer.androidcharts.PieHelper;
import im.dacer.androidcharts.PieView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PieChartActivity extends AppCompatActivity  {

    private Context mContext;
    private TextView buyerAmountView, sellerAmountView, totalAmountView;

    private TextView mStartDateView,mEndDateView;
    private int startDay, endDay;
    private int startMonth, endMonth;
    private int startYear, endYear;
    private Date mStartDate,mEndDate;
    SimpleDateFormat SDF = new SimpleDateFormat("dd MMM yyyy");
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Brokerage Desparity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mContext= this;

        buyerAmountView = (TextView) findViewById(R.id.buyer_amount);
        sellerAmountView = (TextView) findViewById(R.id.seller_amount);
        totalAmountView = (TextView) findViewById(R.id.total_amount);

        mStartDateView = (TextView) findViewById(R.id.starDate);
        mEndDateView = (TextView) findViewById(R.id.endDate);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark,
                R.color.colorPrimaryLight,
                R.color.teal,
                R.color.teal);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getDesparityResult();
            }
        });
        findViewById(R.id.button_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDesparityResult();
            }
        });
        cal = Calendar.getInstance();
        startDay = endDay = cal.get(Calendar.DAY_OF_MONTH);
        startMonth = endMonth = cal.get(Calendar.MONTH);
        startYear = endYear = cal.get(Calendar.YEAR);

        getDesparityResult();
    }

    private void getDesparityResult() {
        final Dialog dialog = AppProgressDialog.show(this);
        String startDateString = null, enadDateString = null;
        if (mStartDate != null && mEndDate != null) {
            SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
            startDateString = SDF.format(mStartDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mEndDate);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date newEndDate = calendar.getTime();
            enadDateString = SDF.format(newEndDate);
        }
        User me = User.getSavedUser(this);
        ObjectFactory.getInstance().getAnalysisServiceInstance().getBrokerDesparity(me.getUserID(), startDateString, enadDateString, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if (messageDTO.isSuccess()) {
                    String amounts = messageDTO.getData().toString();
                    setView(amounts);
                }else {
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, "Pleaaswe check internet connection", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setView(String amount) {
        String amounts[] = amount.split("\\|");
        float buyerAmount = Float.parseFloat(amounts[0]);
        float sellerAmount = Float.parseFloat(amounts[1]);
        float totalAmount = buyerAmount+sellerAmount;
        float buyerPercentage = (buyerAmount*100)/totalAmount;
        float SellerPercentage = (sellerAmount*100)/totalAmount;

        final PieView pieView = (PieView)findViewById(R.id.pie_view);
        buyerAmountView.setText("From Buyer: "+buyerAmount+" Rs.");
        sellerAmountView.setText("From Seller: "+sellerAmount+" Rs.");
        totalAmountView.setText("Total Brokerage: "+totalAmount+" Rs.");
        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();
        pieHelperArrayList.add(new PieHelper(buyerPercentage, ContextCompat.getColor(this, R.color.teal)));
        pieHelperArrayList.add(new PieHelper(SellerPercentage, ContextCompat.getColor(this, R.color.main_color)));

        pieView.setDate(pieHelperArrayList);
        pieView.setClickable(false);
        pieView.setOnPieClickListener(null);
//        pieView.selectedPie(2);

    }


    public void setStartDate(View view) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startYear = year;
                startMonth = monthOfYear;
                startDay = dayOfMonth;
                mStartDate= calendar.getTime();
                mStartDateView.setText(SDF.format(mStartDate));
            }
        }, startYear, startMonth, startDay).show();
    }


    public void setEndDate(View view) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
        }, endYear, endMonth, endDay).show();
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
