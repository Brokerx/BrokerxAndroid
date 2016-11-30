package com.firstidea.android.brokerx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EnquiryDetailsActivity extends AppCompatActivity {
    private String[] option = {"Benzine etyle alcohol with vinyl copper sulphate"};
    private String[] charges = {"Charges"};
    private LinearLayout btn_Reject, btn_Accept;

    private TextView TxviewHistoryA,TxviewHistoryR;

    private LinearLayout Accept, Reject, Pending, Deal_Done,Revert,Rej_Acc_Layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        setContentView(R.layout.activity_enquiry_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        TxviewHistoryA = (TextView) findViewById(R.id.ViewHistory_Accp);
        TxviewHistoryA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ViewAccHistory = new Intent(EnquiryDetailsActivity.this,ViewHistoryActivity.class);
                startActivity(ViewAccHistory);
            }
        });

        TxviewHistoryR = (TextView) findViewById(R.id.viewhistRej);
        TxviewHistoryR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ViewRejHistory = new Intent(EnquiryDetailsActivity.this,ViewHistoryActivity.class);
                startActivity(ViewRejHistory);

            }
        });

        Accept = (LinearLayout) findViewById(R.id.Accept);
        Accept.setVisibility(View.GONE);

        Reject = (LinearLayout) findViewById(R.id.Reject);
        Reject.setVisibility(View.GONE);

        Pending = (LinearLayout) findViewById(R.id.pending);
        Pending.setVisibility(View.GONE);

        Deal_Done = (LinearLayout) findViewById(R.id.Deal_done);
        Deal_Done.setVisibility(View.GONE);

        Revert = (LinearLayout) findViewById(R.id.revert_layout);
        Revert.setVisibility(View.GONE);

        Rej_Acc_Layout = (LinearLayout) findViewById(R.id.acc_rej_layout);
        Rej_Acc_Layout.setVisibility(View.VISIBLE);

        btn_Accept = (LinearLayout) findViewById(R.id.btn_accept);

        btn_Reject = (LinearLayout) findViewById(R.id.btn_reject);

        btn_Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Accept = (LinearLayout) findViewById(R.id.Accept);
                Accept.setVisibility(View.VISIBLE);
                Toast.makeText(EnquiryDetailsActivity.this, "Accept", Toast.LENGTH_SHORT).show();
                Revert = (LinearLayout) findViewById(R.id.revert_layout);
                Revert.setVisibility(View.VISIBLE);
                Rej_Acc_Layout.setVisibility(View.GONE);


            }
        });

        btn_Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EnquiryDetailsActivity.this, "Rejected", Toast.LENGTH_SHORT).show();
                Reject = (LinearLayout) findViewById(R.id.Reject);
                Reject.setVisibility(View.VISIBLE);
                Accept.setVisibility(View.GONE);


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
