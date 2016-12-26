package com.firstidea.android.brokerx;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.model.LeadDocument;
import com.firstidea.android.brokerx.http.model.LeadStatusHistory;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.widget.AppProgressDialog;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LeadStatusHistoryDetailsActivity extends AppCompatActivity {

    LeadStatusHistory mLeadStatusHistory;
    private boolean isDocumentSectionOpened = false;
    private List<LeadDocument> mLeadDocuments;
    LinearLayout documentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_status_history_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Status Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        documentLayout = (LinearLayout) findViewById(R.id.layout_documents);
        mLeadStatusHistory = getIntent().getExtras().getParcelable(LeadStatusHistory.KEY_STATUS_HISTORY);
        initScreen();
    }

    private void initScreen() {
        findViewById(R.id.arrow_three).setVisibility(View.GONE);
        switch (mLeadStatusHistory.getCurrentStatus()) {
            case 6:
                ((ImageView) findViewById(R.id.status6_circle)).setImageResource(R.drawable.accept_circle);
                ((TextView)findViewById(R.id.status6_lbl)).setTypeface(null, Typeface.BOLD);
                ((TextView)findViewById(R.id.status6_dttm)).setText(mLeadStatusHistory.getDealClearedDateTime());
            case 5:
                ((ImageView) findViewById(R.id.status5_circle)).setImageResource(R.drawable.accept_circle);
                ((TextView)findViewById(R.id.status5_lbl)).setTypeface(null, Typeface.BOLD);
                ((TextView)findViewById(R.id.status5_dttm)).setText(mLeadStatusHistory.getDocumentsreceivedDateTime());
            case 4:
                ((ImageView) findViewById(R.id.status4_circle)).setImageResource(R.drawable.accept_circle);
                ((TextView)findViewById(R.id.status4_lbl)).setTypeface(null, Typeface.BOLD);
                ((TextView)findViewById(R.id.status4_dttm)).setText(mLeadStatusHistory.getPaymentReceivedDateTime());
            case 3:
                ((ImageView) findViewById(R.id.status3_circle)).setImageResource(R.drawable.accept_circle);
                ((TextView)findViewById(R.id.status3_lbl)).setTypeface(null, Typeface.BOLD);
                findViewById(R.id.arrow_three).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_step_three).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDocumentsAndToggle();
                    }
                });
            case 2:
                ((ImageView) findViewById(R.id.status2_circle)).setImageResource(R.drawable.accept_circle);
                ((TextView)findViewById(R.id.status2_lbl)).setTypeface(null, Typeface.BOLD);
                String text = ((TextView)findViewById(R.id.status2_lbl)).getText().toString();
                text += "\n(Invoice No: "+mLeadStatusHistory.getInvoiceNumber()+")";
                ((TextView)findViewById(R.id.status2_lbl)).setText(text);
                ((TextView)findViewById(R.id.status2_dttm)).setText(mLeadStatusHistory.getGoodsLiftedDateTime());
            case 1:
                ((ImageView) findViewById(R.id.status1_circle)).setImageResource(R.drawable.accept_circle);
                ((TextView)findViewById(R.id.status1_dttm)).setText(mLeadStatusHistory.getDealDoneDateTime());
                break;
        }
    }

    private void getDocumentsAndToggle() {
        if(mLeadDocuments == null) {
            getLeadDocuments();
            return;
        }
        if(isDocumentSectionOpened) {
            documentLayout.setVisibility(View.GONE);
            findViewById(R.id.arrow_three).setBackgroundResource(R.drawable.ic_arrow_down_gray);
        } else {
            documentLayout.setVisibility(View.VISIBLE);
            findViewById(R.id.arrow_three).setBackgroundResource(R.drawable.ic_arrow_up_gray);
        }

        isDocumentSectionOpened = !isDocumentSectionOpened;
    }

    private void getLeadDocuments() {
        final Dialog dialog = AppProgressDialog.show(this);
        ObjectFactory.getInstance().getLeadServiceInstance().getLeadDocuments(mLeadStatusHistory.getLeadID(), new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if (messageDTO.isSuccess()) {
                    mLeadDocuments = LeadDocument.createListFromJson(messageDTO.getData());
                    populateDocumentList();
                    getDocumentsAndToggle();
                    dialog.dismiss();
                } else {
                    Toast.makeText(LeadStatusHistoryDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(LeadStatusHistoryDetailsActivity.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
            }
        });
    }

    private void populateDocumentList() {

        for(LeadDocument leadDocument: mLeadDocuments) {
            View row = View.inflate(this, R.layout.row_document, null);
            String fileName = getValidFileName(leadDocument.getDocumentURL());
            ((TextView)row.findViewById(R.id.file_name)).setText(fileName);
            ((TextView)row.findViewById(R.id.dttm)).setText(leadDocument.getUploadedDttm());
            if(leadDocument.getDocumentURL().toLowerCase().endsWith(".pdf")) {
                ((TextView)row.findViewById(R.id.icon_text)).setText("PDF");
                ((TextView)row.findViewById(R.id.icon_text)).setBackgroundResource(R.drawable.icon_file_pdf);
            } else if(leadDocument.getDocumentURL().toLowerCase().endsWith(".doc")){
                ((TextView)row.findViewById(R.id.icon_text)).setText("");
                ((TextView)row.findViewById(R.id.icon_text)).setBackgroundResource(R.drawable.icon_file_doc);
            } else {
                ((TextView)row.findViewById(R.id.icon_text)).setText(fileName.substring(fileName.length()-4, fileName.length()).replace(".","").toUpperCase());
                ((TextView)row.findViewById(R.id.icon_text)).setBackgroundResource(R.drawable.icon_file_unknown);
            }
            documentLayout.addView(row);
        }
    }

    private String getValidFileName(String documentURL) {
        String firstPart = documentURL.substring(0, documentURL.lastIndexOf("_"));
        String secondpart = documentURL.substring(documentURL.lastIndexOf("."), documentURL.length());

        return firstPart+secondpart;
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
