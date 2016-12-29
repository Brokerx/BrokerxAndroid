package com.firstidea.android.brokerx.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.firstidea.android.brokerx.http.SingletonRestClient;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

/**
 * Created by Govind on 29-Dec-16.
 */

public class DownLoadUtils {

    public interface DownloadCallback{
        void onDownloadSuccess();
        void onDownloadFailed();
    }

    public static void startDownLoad(final Context context, final String fileNameString, final DownloadCallback downloadCallback) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        String dialogTitle = "Initializing App...";//isUpdate ? "Updating App..." : "Initializing App...";
        progressDialog.setMessage(dialogTitle);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressNumberFormat(null);
//        progressDialog.setProgressPercentFormat(null);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String fileName = fileNameString.replace(" ","%20");
                String path = SingletonRestClient.BASE_DOCUMENT_FILES_URL+fileName;//Uri.encode(SingletonRestClient.BASE_DOCUMENT_FILES_URL+fileName);
                Uri downloadUri = Uri.parse(path);
                final String filePath = AppUtils.getDocumentFilePath(fileName, context);// + fileName;//"assets.zip";
                Uri destinationUri = Uri.parse(filePath);
                DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                        //.addCustomHeader("Auth-Token", "YourTokenApiKey")
                        .setRetryPolicy(new DefaultRetryPolicy(60000, 3, 1.0F))
                        .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH)
                        .setStatusListener(new DownloadStatusListenerV1() {
                            @Override
                            public void onDownloadComplete(DownloadRequest downloadRequest) {
                                progressDialog.dismiss();
                                downloadCallback.onDownloadSuccess();
                            }

                            @Override
                            public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                                Log.d("Download", "Download FIALED ERR= " + errorCode + " MSG= " + errorMessage);
                                Toast.makeText(context, "Download Failed (" + errorCode + ")", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                downloadCallback.onDownloadFailed();
                            }

                            @Override
                            public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                                progressDialog.setProgress(progress);
                            }

                        });
                ThinDownloadManager downloadManager;
                downloadManager = new ThinDownloadManager();
                int downloadId = downloadManager.add(downloadRequest);
            }
        }).start();

    }

}
