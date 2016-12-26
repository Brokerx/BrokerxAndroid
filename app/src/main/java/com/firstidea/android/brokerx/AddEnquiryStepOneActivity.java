package com.firstidea.android.brokerx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.User;

import java.util.ArrayList;
import java.util.List;

public class AddEnquiryStepOneActivity extends AppCompatActivity {

    private final int BROKER_SELECTION_REQUEST = 100;
    private EditText editBroker, editMake, editqty;
    private Spinner spinnerItem, spinnerQtyUnit, spinnerPacking;
    Lead mLead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        setContentView(R.layout.activity_add_enquiry_step_one);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndNext();
            }
        });

        editBroker = (EditText) findViewById(R.id.broker_name);
        editMake = (EditText) findViewById(R.id.make);
        editqty = (EditText) findViewById(R.id.qty);
        spinnerItem = (Spinner) findViewById(R.id.spinner_items);
        spinnerQtyUnit = (Spinner) findViewById(R.id.spinner_qty_unit);
        spinnerPacking = (Spinner) findViewById(R.id.spinner_packing);
        editBroker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEnquiryStepOneActivity.this, MycircleActivity.class);
                intent.putExtra(Constants.KEY_IS_FOR_SELECTION, true);
                startActivityForResult(intent, BROKER_SELECTION_REQUEST);
            }
        });

        spinnerItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mLead.setItemID(position);
                mLead.setItemName(spinnerItem.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerQtyUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mLead.setQtyUnit(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPacking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mLead.setPacking(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (getIntent().hasExtra(Lead.KEY_LEAD)) {
            mLead = getIntent().getExtras().getParcelable(Lead.KEY_LEAD);
            editBroker.setEnabled(false);
            String brokerName = "<b>Broker:</b> "+mLead.getBroker().getFullName();
            editBroker.setText(Html.fromHtml(brokerName));
            editMake.setText(mLead.getMake());
            editqty.setText(mLead.getQty()+"");
            spinnerQtyUnit.setSelection(mLead.getQtyUnit());
            spinnerPacking.setSelection(mLead.getPacking());
        } else {
            String type = getIntent().getExtras().getString("type");
            mLead = new Lead();
            mLead.setType(type);
            com.firstidea.android.brokerx.http.model.User user = com.firstidea.android.brokerx.http.model.User.getSavedUser(this);
            mLead.setCreatedUserID(user.getUserID());
        }
    }

    private void validateAndNext() {
        //TODO Tushar: validate all fields
        mLead.setMake(editMake.getText().toString());
        mLead.setQty(Float.parseFloat(editqty.getText().toString()));
        Intent intent = new Intent(AddEnquiryStepOneActivity.this, AddEnquiryStepTwoActivity.class);
        intent.putExtra(Lead.KEY_LEAD, mLead);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BROKER_SELECTION_REQUEST && resultCode == RESULT_OK) {
            User user = data.getExtras().getParcelable(Constants.KEY_SELECTED_USER);
            if (TextUtils.isEmpty(user.getBrokerDealsInItems())) {
                Toast.makeText(this, "Sorry, This broker doesn't deal in any Item", Toast.LENGTH_SHORT).show();
                return;
            }
            editBroker.setText(user.getFullName());
            mLead.setBrokerID(1);
            List<String> items = new ArrayList<>();
            for (String id : user.getBrokerDealsInItems().split(",")) {
                items.add(id);
            }
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(AddEnquiryStepOneActivity.this, android.R.layout.simple_list_item_1, items);
            spinnerItem.setAdapter(spinnerAdapter);
            findViewById(R.id.layout_item_spinner).setVisibility(View.VISIBLE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, android.R.anim.slide_out_right);
    }
}
