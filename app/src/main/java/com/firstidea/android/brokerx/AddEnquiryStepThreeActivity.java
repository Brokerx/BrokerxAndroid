package com.firstidea.android.brokerx;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.firstidea.android.brokerx.http.model.Lead;

public class AddEnquiryStepThreeActivity extends AppCompatActivity {

    private EditText editBasicPrice, editExciseDuty, editTransportCharges, editMiscCharges, editTotalCharges;
    private Spinner spinnerBasicUnit, spinnerExciseUnit;
    private CheckBox checkAsPerAvailability;
    private Lead mLead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.slide_in_left ,android.R.anim.slide_out_right);
        setContentView(R.layout.activity_add_enquiry_step_three);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mLead = getIntent().getParcelableExtra(Lead.KEY_LEAD);
        editBasicPrice = (EditText) findViewById(R.id.basic_price);
        editExciseDuty = (EditText) findViewById(R.id.excise_duty);
        editTransportCharges = (EditText) findViewById(R.id.transport_charges);
        editMiscCharges = (EditText) findViewById(R.id.misc_charges);
        editTotalCharges = (EditText) findViewById(R.id.total_charges);
        checkAsPerAvailability = (CheckBox) findViewById(R.id.check_as_per_availability);

        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndNext();
            }
        });
        spinnerBasicUnit = (Spinner) findViewById(R.id.spinner_basic_price_unit);
        spinnerExciseUnit = (Spinner) findViewById(R.id.spinner_excise_duty_unit);

        spinnerBasicUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mLead.setBasicPriceUnit(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerExciseUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mLead.setExciseUnit(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(mLead.getLeadID() != null && mLead.getLeadID() > 0) {
            editBasicPrice.setText(mLead.getBasicPrice()+"");
            spinnerBasicUnit.setSelection(mLead.getBasicPriceUnit());
            editExciseDuty.setText(mLead.getExciseDuty()+"");
            spinnerExciseUnit.setSelection(mLead.getExciseUnit());
            if(mLead.isAsPerAvailablity()) {
                checkAsPerAvailability.setChecked(true);
            }
            editTransportCharges.setText(mLead.getTransportCharges()+"");
            editMiscCharges.setText(mLead.getMiscCharges()+"");
        }
    }

    private void validateAndNext() {
        //TODO Tushar: validate all fields also add textchange listener to all price edittext and calculate and set total charges edittext
        float basicPrice = Float.parseFloat(editBasicPrice.getText().toString());
        float exciseDuty = Float.parseFloat(editExciseDuty.getText().toString());
        float transportChrg = Float.parseFloat(editTransportCharges.getText().toString());
        float miscChrg = Float.parseFloat(editMiscCharges.getText().toString());
        mLead.setBasicPrice(basicPrice);
        mLead.setExciseDuty(exciseDuty);
        mLead.setTransportCharges(transportChrg);
        mLead.setMiscCharges(miscChrg);
        mLead.setAsPerAvailablity(checkAsPerAvailability.isChecked());

        Intent intent = new Intent(AddEnquiryStepThreeActivity.this,AddEnquiryStepFourActivity.class);
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
