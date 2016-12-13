package com.firstidea.android.brokerx;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.SingletonRestClient;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.service.LeadService;
import com.firstidea.android.brokerx.widget.AppProgressDialog;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddEnquiryStepFiveActivity extends AppCompatActivity {
    private Button mNext;
    private EditText editAgainstForm, editCreditPeriod, editFreeStorage, editPrefSeller, editComments;
    private Lead mLead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        setContentView(R.layout.activity_add_enquiry_step_five);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mLead = getIntent().getParcelableExtra(Lead.KEY_LEAD);

        mNext = (Button) findViewById(R.id.btn_next);

        editAgainstForm = (EditText) findViewById(R.id.against_form);
        editCreditPeriod = (EditText) findViewById(R.id.credit_period);
        editFreeStorage = (EditText) findViewById(R.id.free_storage_period);
        editPrefSeller = (EditText) findViewById(R.id.prefered_seller_name);
        editComments = (EditText) findViewById(R.id.comments);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndCallService();
            }
        });

    }

    private void validateAndCallService() {
        //TODO Tushar: validate all fields
        mLead.setAgainstForm(editAgainstForm.getText().toString());
        mLead.setCreditPeriod(editCreditPeriod.getText().toString());
        mLead.setFreeStoragePeriod(editFreeStorage.getText().toString());
        mLead.setPreferredSellerName(editPrefSeller.getText().toString());
        mLead.setComments(editComments.getText().toString());

        final Dialog dialog = AppProgressDialog.show(this);
       /* LeadService leadService = SingletonRestClient.createService(LeadService.class, this);
        leadService.saveLead(mLead, new Callback<MessageDTO>() {*/
        ObjectFactory.getInstance().getLeadServiceInstance().saveLead(mLead, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                dialog.dismiss();
                Intent intent = new Intent(AddEnquiryStepFiveActivity.this, EnquiryDetailsActivity.class);
                intent.putExtra(Lead.KEY_LEAD, mLead);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(AddEnquiryStepFiveActivity.this, "Some Error Occurred, Please Try again", Toast.LENGTH_SHORT).show();
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
