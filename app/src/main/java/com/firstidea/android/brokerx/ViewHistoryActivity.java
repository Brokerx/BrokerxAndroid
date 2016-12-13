package com.firstidea.android.brokerx;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firstidea.android.brokerx.adapter.ViewHistoryRecyclerViewAdapter;
import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.widget.AppProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ViewHistoryActivity extends AppCompatActivity {

    private Integer mLeadID;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.btn_view_all) Button btnViewAll;
    private ViewHistoryRecyclerViewAdapter mAdapter;
    private ArrayList<Lead> mLeads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLeadID = getIntent().getIntExtra(Lead.KEY_LEAD_ID, 0);
        getLeadHistory();
    }

    private void getLeadHistory() {
        final Dialog dialog = AppProgressDialog.show(this);
        ObjectFactory.getInstance().getLeadServiceInstance().getLeadHistory(mLeadID, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if(messageDTO.isSuccess()) {
                    mLeads = Lead.createListFromJson(messageDTO.getData());
                    if(mLeads.size() > 2) {
                        ArrayList<Lead> leads = new ArrayList<Lead>(2);
                        leads.add(mLeads.get(0));
                        leads.add(mLeads.get(1));
                        initRecyclerView(leads, false);
                    } else {
                        initRecyclerView(mLeads, true);
                    }

                }
                dialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.dismiss();
                Toast.makeText(ViewHistoryActivity.this, "Connection Error, please try again...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecyclerView(ArrayList<Lead> leads, boolean isShowAll) {
        mAdapter = new ViewHistoryRecyclerViewAdapter(ViewHistoryActivity.this, leads, new ViewHistoryRecyclerViewAdapter.OnCardListener() {
            @Override
            public void OnCardClick(Lead lead) {
                Intent intent = new Intent(ViewHistoryActivity.this, EnquiryDetailsActivity.class);
                intent.putExtra(Lead.KEY_LEAD, lead);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        if(isShowAll) {
            btnViewAll.setVisibility(View.GONE);
        } else {
            btnViewAll.setVisibility(View.VISIBLE);
            btnViewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initRecyclerView(mLeads, true);
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, android.R.anim.slide_out_right);
    }

}
