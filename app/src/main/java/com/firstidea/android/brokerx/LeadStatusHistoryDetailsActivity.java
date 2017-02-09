package com.firstidea.android.brokerx;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.LeadDocument;
import com.firstidea.android.brokerx.http.model.LeadStatusHistory;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.utils.AppUtils;
import com.firstidea.android.brokerx.utils.DownLoadUtils;
import com.firstidea.android.brokerx.widget.AppProgressDialog;

import java.io.File;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LeadStatusHistoryDetailsActivity extends AppCompatActivity {

    LeadStatusHistory mLeadStatusHistory;
    private boolean isDocumentSectionOpened = false;
    private List<LeadDocument> mLeadDocuments;
    LinearLayout documentLayout;
    View documentDivider;
    private final int Attach_DOCUMENT_REQ_CODE = 1001;
    private boolean isSeller = false;
    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_status_history_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Status Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mContext = this;

        documentLayout = (LinearLayout) findViewById(R.id.layout_documents);
        documentDivider = findViewById(R.id.document_divider);
        mLeadStatusHistory = getIntent().getExtras().getParcelable(LeadStatusHistory.KEY_STATUS_HISTORY);
        isSeller = getIntent().getExtras().getBoolean("IsSeller", false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark,
                R.color.colorPrimaryLight,
                R.color.teal,
                R.color.teal);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getLeadStatusHistory();
            }
        });

        initScreen();
    }

    private void getLeadStatusHistory() {
        final Dialog dialog = AppProgressDialog.show(mContext);
        ObjectFactory.getInstance().getLeadServiceInstance().getLeadStatusHistory(mLeadStatusHistory.getLeadID(), new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if (messageDTO.isSuccess()) {
                    mLeadStatusHistory = LeadStatusHistory.createFromJSON(messageDTO.getData());
                    initScreen();
                    dialog.dismiss();
                    getLeadDocuments();
                } else {
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
            }
        });
    }

    private void initScreen() {
        findViewById(R.id.arrow_three).setVisibility(View.GONE);
        switch (mLeadStatusHistory.getCurrentStatus()) {
            case 6:
                ((ImageView) findViewById(R.id.status6_circle)).setImageResource(R.drawable.accept_circle);
                ((TextView) findViewById(R.id.status6_lbl)).setTypeface(null, Typeface.BOLD);
                ((TextView) findViewById(R.id.status6_dttm)).setText(mLeadStatusHistory.getDealClearedDateTime());
            case 5:
                ((ImageView) findViewById(R.id.status5_circle)).setImageResource(R.drawable.accept_circle);
                ((TextView) findViewById(R.id.status5_lbl)).setTypeface(null, Typeface.BOLD);
                ((TextView) findViewById(R.id.status5_dttm)).setText(mLeadStatusHistory.getDocumentsreceivedDateTime());
            case 4:
                ((ImageView) findViewById(R.id.status4_circle)).setImageResource(R.drawable.accept_circle);
                ((TextView) findViewById(R.id.status4_lbl)).setTypeface(null, Typeface.BOLD);
                ((TextView) findViewById(R.id.status4_dttm)).setText(mLeadStatusHistory.getPaymentReceivedDateTime());
            case 3:
                ((ImageView) findViewById(R.id.status3_circle)).setImageResource(R.drawable.accept_circle);
                ((TextView) findViewById(R.id.status3_lbl)).setTypeface(null, Typeface.BOLD);
                findViewById(R.id.arrow_three).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_step_three).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDocumentsAndToggle();
                    }
                });
            case 2:
                ((ImageView) findViewById(R.id.status2_circle)).setImageResource(R.drawable.accept_circle);
                ((TextView) findViewById(R.id.status2_lbl)).setTypeface(null, Typeface.BOLD);
                String text = "Goods Lifted";
                text += "\n(Invoice No: " + mLeadStatusHistory.getInvoiceNumber() + ")";
                ((TextView) findViewById(R.id.status2_lbl)).setText(text);
                ((TextView) findViewById(R.id.status2_dttm)).setText(mLeadStatusHistory.getGoodsLiftedDateTime());
            case 1:
                ((ImageView) findViewById(R.id.status1_circle)).setImageResource(R.drawable.accept_circle);
                ((TextView) findViewById(R.id.status1_dttm)).setText(mLeadStatusHistory.getDealDoneDateTime());
                break;
        }
    }

    private void getDocumentsAndToggle() {
        if (mLeadDocuments == null) {
            getLeadDocuments();
            return;
        }
        if (isDocumentSectionOpened) {
            documentLayout.setVisibility(View.GONE);
            documentDivider.setVisibility(View.GONE);
            findViewById(R.id.arrow_three).setBackgroundResource(R.drawable.ic_arrow_down_gray);
        } else {
            documentLayout.setVisibility(View.VISIBLE);
            documentDivider.setVisibility(View.VISIBLE);
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
                    mSwipeRefreshLayout.setRefreshing(false);
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
        documentLayout.removeAllViews();
        for (final LeadDocument leadDocument : mLeadDocuments) {
            final View row = View.inflate(this, R.layout.row_document, null);
            final String fileName = getValidFileName(leadDocument.getDocumentURL());
            ((TextView) row.findViewById(R.id.file_name)).setText(fileName);
            ((TextView) row.findViewById(R.id.dttm)).setText(leadDocument.getUploadedDttm());
            boolean isImageFile = false;
            if (leadDocument.getDocumentURL().toLowerCase().endsWith(".pdf")) {
                ((TextView) row.findViewById(R.id.icon_text)).setText("PDF");
                ((TextView) row.findViewById(R.id.icon_text)).setBackgroundResource(R.drawable.icon_file_pdf);
            } else if (leadDocument.getDocumentURL().toLowerCase().endsWith(".doc")) {
                ((TextView) row.findViewById(R.id.icon_text)).setText("");
                ((TextView) row.findViewById(R.id.icon_text)).setBackgroundResource(R.drawable.icon_file_doc);
            } else {
                ((TextView) row.findViewById(R.id.icon_text)).setText(fileName.substring(fileName.length() - 4, fileName.length()).replace(".", "").toUpperCase());
                ((TextView) row.findViewById(R.id.icon_text)).setBackgroundResource(R.drawable.icon_file_unknown);
                isImageFile = fileName.toLowerCase().contains("jpg") || fileName.toLowerCase().contains("png")
                        || fileName.toLowerCase().contains("jpeg") || fileName.toLowerCase().contains("bmp");
            }
            final boolean isDownLoaded = AppUtils.isFileAvailable(leadDocument.getDocumentURL(), this);
            if (isDownLoaded) {
                ((ImageView) row.findViewById(R.id.download_status)).setVisibility(View.GONE);
                ((ImageView) row.findViewById(R.id.download_status_completed)).setVisibility(View.VISIBLE);
            }
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isDownLoaded) {
                        String path = AppUtils.getDocumentFilePath(leadDocument.getDocumentURL(), LeadStatusHistoryDetailsActivity.this);
                        File file = new File(path);
                        /*Uri internal = Uri.fromFile(file);
                        Intent intent;
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(internal);*/
                        Uri uri = Uri.fromFile(file);
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                        String mime = "*/*";
                        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                        if (mimeTypeMap.hasExtension(mimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {
                            mime = mimeTypeMap.getMimeTypeFromExtension(mimeTypeMap.getFileExtensionFromUrl(uri.toString()));
                            intent.setDataAndType(uri, mime);
                        }
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LeadStatusHistoryDetailsActivity.this);
                            builder.setTitle("No Application Found");
                            builder.setMessage("Download one from Android Play Store?");
                            builder.setPositiveButton("Yes, Please",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                                            String extension = fileName.substring(fileName.length() - 4, fileName.length()).replace(".", "").toUpperCase();
                                            marketIntent.setData(Uri.parse("market://search?q=" + extension + "&c=apps"));
                                            startActivity(marketIntent);
                                        }
                                    });
                            builder.setNegativeButton("No, Thanks", null);
                            builder.create().show();
                        }
                    } else {
                        //Download image using thin download manager
                        downloadFile(leadDocument.getDocumentURL(), row);
                    }
                }
            });
            documentLayout.addView(row);
        }
        if (isSeller) {
            View row = View.inflate(this, R.layout.row_attach_more_documents, null);
            row.findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LeadStatusHistoryDetailsActivity.this, UploadDocumentActivity.class);
                    intent.putExtra(Lead.KEY_LEAD_ID, mLeadStatusHistory.getLeadID());
                    startActivityForResult(intent, Attach_DOCUMENT_REQ_CODE);
                }
            });
            documentLayout.addView(row);
        }
    }

    private void downloadFile(String documentURL, final View row) {
        DownLoadUtils.startDownLoad(this, documentURL, new DownLoadUtils.DownloadCallback() {
            @Override
            public void onDownloadSuccess() {
                populateDocumentList();
            }

            @Override
            public void onDownloadFailed() {

            }
        });

    }


    private String getValidFileName(String documentURL) {
        String firstPart = documentURL.substring(0, documentURL.lastIndexOf("_"));
        String secondpart = documentURL.substring(documentURL.lastIndexOf("."), documentURL.length());

        return firstPart + secondpart;
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

        if (resultCode == RESULT_OK && requestCode == Attach_DOCUMENT_REQ_CODE) {
            LeadDocument leadDocument = data.getExtras().getParcelable(LeadDocument.KEY_LEAD_DOCUMENT);
            mLeadDocuments.add(leadDocument);
            populateDocumentList();
        }
    }
}
