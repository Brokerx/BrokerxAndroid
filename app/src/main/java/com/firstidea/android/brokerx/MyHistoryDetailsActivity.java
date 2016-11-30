package com.firstidea.android.brokerx;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firstidea.android.brokerx.model.MyHistroyItem;

public class MyHistoryDetailsActivity extends AppCompatActivity {

    private MyHistroyItem mbrObject;
    private TextView Chemical_Name,InvoiceNo, Total_Brokerage, Date, basic_charge, seller_brokerage, buyer_brokerage;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        /**
         *
         * <b>Assign  the data on perticilar text box</b>
         * <br>get a data with help of object and set to display in text view</br>
         */

        if (getIntent().hasExtra("mbrObject")) {
            mbrObject = (MyHistroyItem) getIntent().getSerializableExtra("mbrObject");
        } else {
            mbrObject = new MyHistroyItem();
        }


        Chemical_Name = (TextView) findViewById(R.id.chemicalName2);
        InvoiceNo = (TextView) findViewById(R.id.InvoiceNo2);
        Total_Brokerage = (TextView) findViewById(R.id.total_brokerage);
        Date = (TextView) findViewById(R.id.date2);
        basic_charge = (TextView) findViewById(R.id.basic_charage);
        seller_brokerage = (TextView) findViewById(R.id.seller_broker);
        buyer_brokerage = (TextView) findViewById(R.id.buyer_broker);



        Chemical_Name.setText(mbrObject.getChemical_Name());
        InvoiceNo.setText(mbrObject.getInvoiceNo());
        Total_Brokerage.setText(mbrObject.getBrokerage());
        Date.setText(mbrObject.getDate());
        basic_charge.setText(mbrObject.getBasic_charge());
        seller_brokerage.setText(mbrObject.getSeller_brokerage());
        buyer_brokerage.setText(mbrObject.getBuyer_brokerage());



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
