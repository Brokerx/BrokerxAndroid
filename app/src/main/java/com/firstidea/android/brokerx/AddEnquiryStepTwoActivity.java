package com.firstidea.android.brokerx;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.firstidea.android.brokerx.http.model.Lead;

public class AddEnquiryStepTwoActivity extends AppCompatActivity {

    Lead mLead;
    private EditText editLocation;
    private final int NEXT_ACTIVITY_REQ_CODE = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.slide_in_left ,android.R.anim.slide_out_right);
        setContentView(R.layout.activity_add_enquiry_step_two);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mLead = getIntent().getParcelableExtra(Lead.KEY_LEAD);
        editLocation= (EditText) findViewById(R.id.edit_location);
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndNext();
            }
        });
        if(!TextUtils.isEmpty(mLead.getLocation())) {
            editLocation.setText(mLead.getLocation());
        }
    }

    private void validateAndNext() {
        //No need of validation

        mLead.setLocation(editLocation.getText().toString());
        Intent intent = new Intent(AddEnquiryStepTwoActivity.this, AddEnquiryStepThreeActivity.class);
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
