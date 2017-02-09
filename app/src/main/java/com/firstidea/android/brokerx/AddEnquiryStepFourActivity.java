package com.firstidea.android.brokerx;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.firstidea.android.brokerx.http.model.Lead;

public class AddEnquiryStepFourActivity extends AppCompatActivity {
    Lead mLead;
    private EditText editBrokerage;
    private final int NEXT_ACTIVITY_REQ_CODE = 500;
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

        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndNext();
            }
        });
        if(mLead.getLeadID() != null && mLead.getLeadID() > 0) {
            editBrokerage.setText(mLead.getBrokerageAmt()+"");
        }
    }

    private void validateAndNext() {
        if(TextUtils.isEmpty(editBrokerage.getText().toString())) {
            editBrokerage.setError("Enter Brokerage");
            return;
        }
        float brokerage = Float.parseFloat(editBrokerage.getText().toString());
        mLead.setBrokerageAmt(brokerage);
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
