package com.firstidea.android.brokerx;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firstidea.android.brokerx.http.model.Lead;

public class AddEnquiryStepFourActivity extends AppCompatActivity {
    Lead mLead;
    private EditText editBrokerage;
    private final int NEXT_ACTIVITY_REQ_CODE = 500;
    private TextView basicAmounttext, BrokerageAmountText;
    private float brokerageAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.slide_in_left ,android.R.anim.slide_out_right);
        setContentView(R.layout.activity_add_enquiry_step_four);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLead = getIntent().getParcelableExtra(Lead.KEY_LEAD);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        editBrokerage = (EditText) findViewById(R.id.edit_brokerage_amt);
        basicAmounttext = (TextView) findViewById(R.id.total_amount);
        BrokerageAmountText = (TextView) findViewById(R.id.brokerage_amount);

        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndNext();
            }
        });
        final float basicPriceAmt = mLead.getBasicPrice() * mLead.getQty();


        basicAmounttext.setText(Html.fromHtml("<b>Basic Price:</b> "+basicPriceAmt+" Rs."));
        editBrokerage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                brokerageAmount = getFloatValue(editBrokerage.getText().toString().trim()) * basicPriceAmt/100;
                BrokerageAmountText.setText(Html.fromHtml("<b>Total Brokerage:</b> "+brokerageAmount+" Rs."));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if((mLead.getLeadID() != null && mLead.getLeadID() > 0)
                || mLead.getBrokerageAmt() > 0) {
            float brokeragePerc = mLead.getBrokeragePerc();//mLead.getBrokerageAmt() * 100/basicPriceAmt;
            editBrokerage.setText(brokeragePerc+"");
        }
    }

    private float getFloatValue(String stringVal) {
        if(stringVal.startsWith(".")) {
            stringVal = "0"+stringVal;
        }
        if(stringVal.trim().length() <= 0) {
            stringVal = "0";
        }
        return Float.parseFloat(stringVal);
    }

    private void validateAndNext() {
        if(TextUtils.isEmpty(editBrokerage.getText().toString())) {
//            editBrokerage.setError("Enter Brokerage");
//            return;
            editBrokerage.setText("0");
        }
//        float brokerage = Float.parseFloat(editBrokerage.getText().toString());
        mLead.setBrokerageAmt(brokerageAmount);
        Intent intent = new Intent(AddEnquiryStepFourActivity.this,AddEnquiryStepFiveActivity.class);
        intent.putExtra(Lead.KEY_LEAD,mLead);
        startActivityForResult(intent, NEXT_ACTIVITY_REQ_CODE);
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
        if(requestCode == NEXT_ACTIVITY_REQ_CODE  && resultCode == RESULT_OK) {
            mLead = data.getExtras().getParcelable(Lead.KEY_LEAD);
            Intent intent = new Intent();
            intent.putExtra(Lead.KEY_LEAD,mLead);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
