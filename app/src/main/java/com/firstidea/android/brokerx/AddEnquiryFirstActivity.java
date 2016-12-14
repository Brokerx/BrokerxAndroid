package com.firstidea.android.brokerx;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.firstidea.android.brokerx.http.model.Lead;

public class AddEnquiryFirstActivity extends AppCompatActivity {

    private Lead mLead= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_enquiry_first);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Post Enquiry");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        final String type = getIntent().getExtras().getString("type");
        if(getIntent().hasExtra(Lead.KEY_LEAD)) {
            mLead = getIntent().getExtras().getParcelable(Lead.KEY_LEAD);
        }
        findViewById(R.id.layout_step_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEnquiryFirstActivity.this, AddEnquiryStepOneActivity.class);
                intent.putExtra("type", type);
                if(mLead!= null) {
                    intent.putExtra(Lead.KEY_LEAD,mLead);
                }
                startActivity(intent);
            }
        });
        if(mLead!= null) {
            findViewById(R.id.layout_step_two).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AddEnquiryFirstActivity.this, AddEnquiryStepTwoActivity.class);
                    if(mLead!= null) {
                        intent.putExtra(Lead.KEY_LEAD,mLead);
                    }
                    startActivity(intent);
                }
            });
            findViewById(R.id.layout_step_three).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AddEnquiryFirstActivity.this, AddEnquiryStepThreeActivity.class);
                    if(mLead!= null) {
                        intent.putExtra(Lead.KEY_LEAD,mLead);
                    }
                    startActivity(intent);
                }
            });
            findViewById(R.id.layout_step_four).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AddEnquiryFirstActivity.this, AddEnquiryStepFourActivity.class);
                    if(mLead!= null) {
                        intent.putExtra(Lead.KEY_LEAD,mLead);
                    }
                    startActivity(intent);
                }
            });
            findViewById(R.id.layout_step_five).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AddEnquiryFirstActivity.this, AddEnquiryStepFiveActivity.class);
                    if(mLead!= null) {
                        intent.putExtra(Lead.KEY_LEAD,mLead);
                    }
                    startActivity(intent);
                }
            });

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
