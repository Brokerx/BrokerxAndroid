package com.firstidea.android.brokerx;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.firstidea.android.brokerx.http.model.Lead;

public class AddEnquiryStepFourActivity extends AppCompatActivity {
    Lead mLead;
    private EditText editBrokerage;
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

    }

    private void validateAndNext() {
        //TODO Tushar: validate all fields
        float brokerage = Float.parseFloat(editBrokerage.getText().toString());
        mLead.setBrokerageAmt(brokerage);
        Intent intent = new Intent(AddEnquiryStepFourActivity.this,AddEnquiryStepFiveActivity.class);
        intent.putExtra(Lead.KEY_LEAD,mLead);
        startActivity(intent);
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
