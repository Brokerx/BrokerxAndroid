package com.firstidea.android.brokerx;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.firstidea.android.brokerx.enums.ExciseType;
import com.firstidea.android.brokerx.http.model.Lead;

public class AddEnquiryStepThreeActivity extends AppCompatActivity {

    private EditText editBasicPrice,  editTransportCharges, editMiscCharges, editTotalCharges, edit_tax_perc; //editExciseDuty
    private Spinner spinnerBasicUnit,spinnerGSTType,spinnerGSTPperc;// spinnerExciseUnit;
    private CheckBox checkAsPerAvailability;
//    private RadioButton radio_excise_inclusive, radio_excise_exclusive;
    private Lead mLead;
    private final int NEXT_ACTIVITY_REQ_CODE = 500;
    private boolean isSetGstManually = false;

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
//        editExciseDuty = (EditText) findViewById(R.id.excise_duty);
        editTransportCharges = (EditText) findViewById(R.id.transport_charges);
        editMiscCharges = (EditText) findViewById(R.id.misc_charges);
        editTotalCharges = (EditText) findViewById(R.id.total_charges);
        edit_tax_perc = (EditText) findViewById(R.id.edit_tax_perc);
        checkAsPerAvailability = (CheckBox) findViewById(R.id.check_as_per_availability);
        /*radio_excise_inclusive = (RadioButton) findViewById(R.id.radio_excise_inclusive);
        radio_excise_exclusive = (RadioButton) findViewById(R.id.radio_excise_exclusive);

        radio_excise_inclusive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                calculateTotal();
            }
        });
        radio_excise_exclusive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                calculateTotal();
            }
        });*/
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndNext();
            }
        });
        spinnerBasicUnit = (Spinner) findViewById(R.id.spinner_basic_price_unit);
//        spinnerExciseUnit = (Spinner) findViewById(R.id.spinner_excise_duty_unit);
        spinnerGSTType = (Spinner) findViewById(R.id.spinner_gst_type);
        spinnerGSTPperc = (Spinner) findViewById(R.id.spinner_gst_perc);

        spinnerBasicUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mLead.setBasicPriceUnit(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        spinnerExciseUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                mLead.setExciseUnit(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        spinnerGSTType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mLead.setGstType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerGSTPperc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 5) {
                    findViewById(R.id.layout_gst_perc).setVisibility(View.VISIBLE);
                    if(isSetGstManually) {
                        isSetGstManually = false;
                    } else {
                        edit_tax_perc.setText("0");
                    }
                } else  {

                    findViewById(R.id.layout_gst_perc).setVisibility(View.GONE);
                    switch (position) {
                        case 0:
                            edit_tax_perc.setText("0");
                            break;
                        case 1:
                            edit_tax_perc.setText("5");
                            break;
                        case 2:
                            edit_tax_perc.setText("12");
                            break;
                        case 3:
                            edit_tax_perc.setText("18");
                            break;
                        case 4:
                            edit_tax_perc.setText("28");
                            break;

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if((mLead.getLeadID() != null && mLead.getLeadID() > 0)
            || (mLead.getBasicPrice() > 0)) {
            editBasicPrice.setText(mLead.getBasicPrice()+"");
            spinnerBasicUnit.setSelection(mLead.getBasicPriceUnit());
//            editExciseDuty.setText(mLead.getExciseDuty()+"");
            /*spinnerExciseUnit.setSelection(mLead.getExciseUnit());
            if(mLead.isAsPerAvailablity()) {
                checkAsPerAvailability.setChecked(true);
            }*/
            editTransportCharges.setText(mLead.getTransportCharges()+"");
            editMiscCharges.setText(mLead.getMiscCharges()+"");
            edit_tax_perc.setText(mLead.getTax()+"");
            float taxPerc = mLead.getTax();
            if(taxPerc ==0){
                spinnerGSTPperc.setSelection(0);
            } else if(taxPerc ==5){
                spinnerGSTPperc.setSelection(1);
            }else if(taxPerc ==12){
                spinnerGSTPperc.setSelection(2);
            }else if(taxPerc ==18){
                spinnerGSTPperc.setSelection(3);
            }else if(taxPerc ==28){
                spinnerGSTPperc.setSelection(4);
            }else{
                isSetGstManually = true;
                spinnerGSTPperc.setSelection(5);
            }
            if(mLead.getGstType() != null) {
                spinnerGSTType.setSelection(mLead.getGstType());
            }
            /*if(mLead.getExcisetype()!= null && mLead.getExcisetype().equals(ExciseType.EXCLUSIVE.getType())) {
                radio_excise_exclusive.setChecked(true);
            } else {
                radio_excise_inclusive.setChecked(true);
            }*/
            calculateTotal();
        }
        TextWatcher priceWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateTotal();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        editBasicPrice.addTextChangedListener(priceWatcher);
//        editExciseDuty.addTextChangedListener(priceWatcher);
        editMiscCharges.addTextChangedListener(priceWatcher);
        edit_tax_perc.addTextChangedListener(priceWatcher);
        editTransportCharges.addTextChangedListener(priceWatcher);
    }

    private void validateAndNext() {
        //TODO Tushar: validate all fields
        if(TextUtils.isEmpty(editBasicPrice.getText().toString())) {
            editBasicPrice.setError("Please Enter Basic Price");
            return;
        }

        float basicPrice = Float.parseFloat(editBasicPrice.getText().toString());
        float exciseDuty = 0;
        float transportChrg = 0;
        float miscChrg = 0;
        float taxPerc = 0;

        /*if(editExciseDuty.getText().toString().trim().length() > 0) {
            exciseDuty = Float.parseFloat(editExciseDuty.getText().toString());
        }*/
        if(editTransportCharges.getText().toString().trim().length() > 0) {
            transportChrg = Float.parseFloat(editTransportCharges.getText().toString());
        }
        if(editMiscCharges.getText().toString().trim().length() > 0) {
            miscChrg = Float.parseFloat(editMiscCharges.getText().toString());
        }
        if(edit_tax_perc.getText().toString().trim().length() > 0) {
            taxPerc = Float.parseFloat(edit_tax_perc.getText().toString());
        }
        mLead.setBasicPrice(basicPrice);
        mLead.setBasicPriceUnit(spinnerBasicUnit.getSelectedItemPosition());
        mLead.setExciseDuty(exciseDuty);
        mLead.setGstType(spinnerGSTType.getSelectedItemPosition());
//        mLead.setExciseUnit(spinnerExciseUnit.getSelectedItemPosition());
//        mLead.setExcisetype(radio_excise_inclusive.isChecked() ? ExciseType.INCLUSIVE.getType(): ExciseType.EXCLUSIVE.getType());
        mLead.setTransportCharges(transportChrg);
        mLead.setMiscCharges(miscChrg);
        mLead.setTax(taxPerc);
        mLead.setAsPerAvailablity(checkAsPerAvailability.isChecked());

        Intent intent = new Intent(AddEnquiryStepThreeActivity.this,AddEnquiryStepFourActivity.class);
        intent.putExtra(Lead.KEY_LEAD,mLead);
        startActivityForResult(intent, NEXT_ACTIVITY_REQ_CODE);
    }

    private void calculateTotal() {
        float basicPrice = 0;
        if(editBasicPrice.getText().toString().trim().length() > 0) {
            basicPrice = getFloatValue(editBasicPrice.getText().toString().trim());
        }
        float exciseDuty = 0;
       /* if(radio_excise_exclusive.isChecked()) {
            if (editExciseDuty.getText().toString().trim().length() > 0) {
                exciseDuty = getFloatValue(editExciseDuty.getText().toString().trim());
            }
        }*/
        float taxPerc = 0;
        if(edit_tax_perc.getText().toString().trim().length() > 0) {
            taxPerc = getFloatValue(edit_tax_perc.getText().toString().trim());
        }
        float transportCharges = 0;
        if (editTransportCharges.getText().toString().trim().length() > 0) {
            transportCharges = getFloatValue(editTransportCharges.getText().toString().trim());
        }
        float miscCharges = 0;
        if (editMiscCharges.getText().toString().trim().length() > 0) {
            miscCharges = getFloatValue(editMiscCharges.getText().toString().trim());
        }

        float basicPriceAmt = basicPrice * mLead.getQty();
        float excisePriceAmt = exciseDuty * mLead.getQty();
        float taxAmount = basicPriceAmt * (taxPerc / 100);
        float totalAmt = basicPriceAmt + excisePriceAmt + transportCharges + miscCharges+ taxAmount;
        editTotalCharges.setText(totalAmt + " Rs");
    }

    private float getFloatValue(String stringVal) {
        if(stringVal.startsWith(".")) {
            stringVal = "0"+stringVal;
        }
        return Float.parseFloat(stringVal);
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
