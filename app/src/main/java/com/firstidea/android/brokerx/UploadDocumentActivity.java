package com.firstidea.android.brokerx;

import android.*;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firstidea.android.brokerx.constants.MsgConstants;
import com.firstidea.android.brokerx.http.SingletonRestClient;
import com.firstidea.android.brokerx.http.model.Lead;
import com.firstidea.android.brokerx.http.model.LeadDocument;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.http.service.LeadService;
import com.firstidea.android.brokerx.http.service.UserService;
import com.firstidea.android.brokerx.utils.AppUtils;
import com.firstidea.android.brokerx.utils.SharedPreferencesUtil;
import com.firstidea.android.brokerx.widget.AppProgressDialog;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class UploadDocumentActivity extends AppCompatActivity {

    private final int GALLERY_PHOTO = 100;
    private final int CAMERA_PHOTO = 101;
    private final int SELECT_FILE = 102;
    private Uri mFileUri;
    private String mImagePath;
    private byte[] mImage;
    private TypedFile file;
    private File document;
    private Context mContext;
    private Integer mLeadID;
    private TextView selectedFileName;


    private final int PERMISSION_REQUEST_CODE = 1010;
    private static String[] APP_PERMISSIONS = {android.Manifest.permission.READ_SMS, android.Manifest.permission.RECEIVE_SMS,
            android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA, android.Manifest.permission.READ_CONTACTS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_document);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upload Document");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mLeadID = getIntent().getIntExtra(Lead.KEY_LEAD_ID, 0);

        mContext = this;
        initScreen();
        requestPermissions();
    }

    private void initScreen() {
        findViewById(R.id.btn_select_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooserDialog();
            }
        });
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSave();
            }
        });
        selectedFileName = (TextView) findViewById(R.id.selected_file_name);
    }

    private void showFileChooserDialog() {
        final CharSequence[] methods = {"Gallery", "Camera", "File"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select Option");
        builder.setItems(methods, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0://Gallery
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, GALLERY_PHOTO);
                        break;
                    case 1://Camera
                        startCameraActivity();
                        break;
                    case 2://File
                        showFileChooser();
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void startCameraActivity() {
        File dir = new File(AppUtils.getDir(mContext));
        if (!dir.exists()) dir.mkdirs();
        String filename = String.valueOf(System.currentTimeMillis());

        File file = new File(AppUtils.getImagePath(filename, mContext));
        mFileUri = Uri.fromFile(file);
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
        startActivityForResult(intent, CAMERA_PHOTO);
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    SELECT_FILE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GALLERY_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();

                    File imageFile = new File(AppUtils.getRealPathFromURI(selectedImage, getApplicationContext()));
                    mImagePath = AppUtils.getRealPathFromURI(selectedImage, mContext);

                    //handle Out of memory error
                    Bitmap bmp = AppUtils.decodeFile(imageFile, 100, 100);

                    mImage = AppUtils.toByteArray(bmp);
                    file = new TypedFile("multipart/form-data", imageFile);
                    document = imageFile;
                }
                break;
            case SELECT_FILE:
                if (resultCode == RESULT_OK) {
                    Uri selectedFile = data.getData();

                    File docFile = new File(AppUtils.getRealPathFromURI(selectedFile, getApplicationContext()));
//                    mImagePath = AppUtils.getRealPathFromURI(selectedFile, mContext);

                    //handle Out of memory error
//                    Bitmap bmp = AppUtils.decodeFile(docFile, 100, 100);

//                    mImage = AppUtils.toByteArray(bmp);
                    file = new TypedFile("multipart/form-data", docFile);
                    document = docFile;
                }
                break;
            case CAMERA_PHOTO:
                if (resultCode == RESULT_OK) {
                    //This is just for display image in ImageView
                    File imageFile = new File(AppUtils.getRealPathFromURI(mFileUri, mContext));
                    mImagePath = AppUtils.getRealPathFromURI(mFileUri, mContext);

                    //Rotate if necessary
                    int rotate = AppUtils.getCameraPhotoOrientation(mContext, mFileUri, AppUtils.getRealPathFromURI(mFileUri, mContext));
                    Matrix matrix = new Matrix();
                    matrix.postRotate(rotate);

                    //handle Out of memory error
                    Bitmap bmp = AppUtils.decodeFile(imageFile, 100, 100);

                    //Rotate BMP
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                    //mImageView.setBackgroundDrawable(new ColorDrawable(0));
                    mImage = AppUtils.toByteArray(rotatedBitmap);
                    file = new TypedFile("multipart/form-data", imageFile);
                    document = imageFile;
                }
                break;
        }
        selectedFileName.setText(document.getName());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void validateAndSave() {
        RadioButton radioTax = (RadioButton) findViewById(R.id.radio_tax_invoice);
        RadioButton radioexcise = (RadioButton) findViewById(R.id.radio_excise_invoice);
        RadioButton radioGodown = (RadioButton) findViewById(R.id.godown_document);
        RadioButton radioTransport = (RadioButton) findViewById(R.id.transport_document);
        RadioButton radioOther = (RadioButton) findViewById(R.id.other);
        LeadDocument leadDocument = new LeadDocument();

        if (radioTax.isChecked()) {
            leadDocument.setType("Tax Invoice");
        } else if (radioexcise.isChecked()) {
            leadDocument.setType("Excise Invoice");
        } else if (radioGodown.isChecked()) {
            leadDocument.setType("Godown Document");
        } else if (radioTransport.isChecked()) {
            leadDocument.setType("Transport Document");
        } else if (radioOther.isChecked()) {
            leadDocument.setType("Other Document");
        } else {
            Toast.makeText(mContext, "Please Select Document Type", Toast.LENGTH_SHORT).show();
            return;
        }

        if (file == null) {
            Toast.makeText(mContext, "Please select file to upload", Toast.LENGTH_SHORT).show();
            return;
        }

        leadDocument.setLeadID(mLeadID);
        User me = User.getSavedUser(mContext);
        leadDocument.setUploadedByUserID(me.getUserID());
        leadDocument.setDeleted(false);
        uploadDocument(leadDocument);
    }


    private void uploadDocument(LeadDocument leadDocument) {
        final Dialog dialog = AppProgressDialog.show(mContext);


        LeadService leadService = SingletonRestClient.createService(LeadService.class, mContext);
        Gson gson = new Gson();
        String leadDocumentJSON = gson.toJson(leadDocument);
        leadService.uploadDocument(file, leadDocumentJSON, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                for (Header header : response.getHeaders()) {
                    if (null != header.getName() && header.getName().equals("Set-Cookie")) {
//		                SingletonRestClient.COOKIES = header.getValue();
                        String cookies = header.getValue();
                        SharedPreferencesUtil.putSharedPreferencesString(mContext, "SESSION_COOKIES", cookies);
                    }
                }
                if (messageDTO.getMessageID().equals(MsgConstants.SUCCESS_ID)) {
                    LeadDocument leadDocument1 = LeadDocument.getLeadDocument(messageDTO.getData());
                    copyFile(leadDocument1.getDocumentURL());
                    Intent result = new Intent();
                    result.putExtra(LeadDocument.KEY_LEAD_DOCUMENT, leadDocument1);
                    setResult(RESULT_OK, result);
                    finish();


                } else {
                    Toast.makeText(mContext, "Some Error Occured, Please try again..", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.dismiss();
                Toast.makeText(mContext, "Please Check your Connectivity and try again..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void copyFile(String fileName) {
        try {
            String destinationPath = AppUtils.getDocumentFilePath(fileName, this);
            File destination = new File(destinationPath);
            FileChannel inChannel = new FileInputStream(document).getChannel();
            FileChannel outChannel = new FileOutputStream(destination).getChannel();
            try {
                inChannel.transferTo(0, inChannel.size(), outChannel);

            } finally {
                if (inChannel != null)
                    inChannel.close();
                if (outChannel != null)
                    outChannel.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* try
        {
            InputStream inStream = new FileInputStream(document);
            OutputStream outStream = new FileOutputStream(destination);

            byte[] buffer = new byte[1024];

            int length;
            while ((length = inStream.read(buffer)) > 0){
                outStream.write(buffer, 0, length);
            }
            FileChannel
            inStream.close();
            outStream.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }*/
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, APP_PERMISSIONS, PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }
    }

}
