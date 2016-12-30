package com.firstidea.android.brokerx;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firstidea.android.brokerx.constants.AppConstants;
import com.firstidea.android.brokerx.constants.MsgConstants;
import com.firstidea.android.brokerx.http.SingletonRestClient;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.http.service.UserService;
import com.firstidea.android.brokerx.utils.AppUtils;
import com.firstidea.android.brokerx.utils.SharedPreferencesUtil;
import com.firstidea.android.brokerx.widget.AppProgressDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class SignupActivity extends AppCompatActivity {

    private CheckBox mCheckIsBroker;
    private TextView mBrokerTip;
    private Context mContext;
    private EditText editName, editEmail, editCity, editAddress, editMobile, edtPassword, etdConfPassword, editAbout, editDealsIn;
    private boolean mIsEdit = false;
    private ImageView userImage;

    private final int GALLERY_PHOTO = 100;
    private final int CAMERA_PHOTO = 101;
    private final int SELECT_CITY = 102;
    private Uri mFileUri;
    private String mImagePath, mImageType;
    private byte[] mImage;
    private TypedFile file;
    private boolean isFroEdit = false;
    private User mUser;

    private final int PERMISSION_REQUEST_CODE = 1010;

    private static String[] APP_PERMISSIONS = {Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mContext = this;

        initScreen();
        if (getIntent().hasExtra(AppConstants.KEY_IS_PROFILE_EDIT)) {
            isFroEdit = true;
            mUser = User.getSavedUser(this);
            updateViews();
            setTitle("My Profile");
            ((Button) findViewById(R.id.btn_signup)).setText("Update Profile");
        }
        for (String permission : APP_PERMISSIONS) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this);
                builder.setTitle("Welcome to Brokerx");
                builder.setMessage("We need permissions to read OTP from SMS, to select/capture your profile image\n" +
                        "Please Allow these permissions in next step");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions();
                    }
                });
                builder.show();
                break;
            }
        }
    }

    private void updateViews() {
        mCheckIsBroker.setVisibility(View.GONE);
        edtPassword.setVisibility(View.GONE);
        etdConfPassword.setVisibility(View.GONE);
        if (!mUser.getBroker()) {
            editDealsIn.setVisibility(View.GONE);
            mBrokerTip.setVisibility(View.GONE);
        } else {
            mCheckIsBroker.setVisibility(View.VISIBLE);
            mCheckIsBroker.setChecked(true);
            editDealsIn.setVisibility(View.VISIBLE);
            mBrokerTip.setVisibility(View.VISIBLE);
        }
        editName.setText(mUser.getFullName());
        editEmail.setText(mUser.getEmail());
        editMobile.setText(mUser.getMobile());
        editAddress.setText(mUser.getAddress());
        editCity.setText(mUser.getCity());
        editDealsIn.setText(mUser.getBrokerDealsInItems());
        editAbout.setText(mUser.getAbout());
        if (!TextUtils.isEmpty(mUser.getImageURL())) {
            String imgUrl = SingletonRestClient.BASE_PROFILE_IMAGE_URL + mUser.getImageURL();
            Picasso.with(this).load(imgUrl)
                    .error(R.drawable.user_profile)
                    .placeholder(R.drawable.user_profile)
                    .into(userImage);
        }
    }

    private void initScreen() {
        mCheckIsBroker = (CheckBox) findViewById(R.id.check_is_broker);
        mBrokerTip = (TextView) findViewById(R.id.broker_tip);
        editName = (EditText) findViewById(R.id.name);
        editEmail = (EditText) findViewById(R.id.email);
        editMobile = (EditText) findViewById(R.id.mobile);
        editAddress = (EditText) findViewById(R.id.address);
        edtPassword = (EditText) findViewById(R.id.password);
        etdConfPassword = (EditText) findViewById(R.id.conf_password);
        editCity = (EditText) findViewById(R.id.city);
        editAbout = (EditText) findViewById(R.id.about);
        editDealsIn = (EditText) findViewById(R.id.deals_in);
        mCheckIsBroker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mBrokerTip.setVisibility(View.VISIBLE);
                    editDealsIn.setVisibility(View.VISIBLE);
                } else {
                    mBrokerTip.setVisibility(View.GONE);
                    editDealsIn.setVisibility(View.GONE);
                }
            }
        });
        userImage = ((ImageView) findViewById(R.id.user_profile));
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDialog();
            }
        });
        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSave();
            }
        });
    }

    private void validateAndSave() {
        if (editName.getText().toString().trim().length() <= 0) {
            editName.setError("Please Enter Your Name");
            return;
        }
        //TODO Tushar : validate all fields like above

        registerUser();
    }


    private void showSelectDialog() {
        final CharSequence[] methods = {"Gallery", "Camera"};
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GALLERY_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();

                    File imageFile = new File(AppUtils.getRealPathFromURI(selectedImage, getApplicationContext()));
                    mImagePath = AppUtils.getRealPathFromURI(selectedImage, mContext);

                    //Get Image Type
                    mImageType = AppUtils.GetMimeType(mContext, selectedImage);
                    if (Preferences.DEBUG) Log.d("PosTAddActivity", "ImageTYpe: " + mImageType);

                    //Rotate if necessary
                   /* int rotate = AppUtils.getCameraPhotoOrientation(mContext, selectedImage, AppUtils.getRealPathFromURI(selectedImage, mContext));
                    Matrix matrix = new Matrix();
                    matrix.postRotate(rotate);*/

                    //handle Out of memory error
                    Bitmap bmp = AppUtils.decodeFile(imageFile, 100, 100);

                    //Rotate BMP
//                    Bitmap rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                    //mImageView.setBackgroundDrawable(new ColorDrawable(0));
                    mImage = AppUtils.toByteArray(bmp);
                    userImage.setImageBitmap(bmp);
                    file = new TypedFile("multipart/form-data", imageFile);
                }
                break;
            case CAMERA_PHOTO:
                if (resultCode == RESULT_OK) {
                    //This is just for display image in ImageView
                    File imageFile = new File(AppUtils.getRealPathFromURI(mFileUri, mContext));
                    mImagePath = AppUtils.getRealPathFromURI(mFileUri, mContext);
                    //getImage type
                    mImageType = AppUtils.GetMimeType(mContext, mFileUri);
                    if (Preferences.DEBUG) Log.d("PostAddActivity", "ImageTYpe: " + mImageType);

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
                    userImage.setImageBitmap(rotatedBitmap);
                    file = new TypedFile("multipart/form-data", imageFile);
                }
                break;
        }
    }


    private void registerUser() {
        final Dialog dialog = AppProgressDialog.show(mContext);
        User user = new User();
        if (isFroEdit) {
            user.setUserID(mUser.getUserID());
        }
        user.setFullName(editName.getText().toString());
        user.setMobile(editMobile.getText().toString());
        user.setEmail(editEmail.getText().toString());
        user.setAddress(editAddress.getText().toString());
        user.setCity(editCity.getText().toString());
        user.setBroker(mCheckIsBroker.isChecked());
        if (!isFroEdit) {
            user.setPassword(edtPassword.getText().toString());
        }
        user.setAbout(editAbout.getText().toString());
        user.setBrokerDealsInItems(editDealsIn.getText().toString());
//        String deviceID = AppUtils.getDeviceId(mContext);
//        user.setDeviceID(deviceID);
//        String gcmKey = SharedPreferencesUtil.getSharedPreferencesString(mContext, AppConstants.KEY_GCM_REG_TOKEN, "");
//        user.setGcmKey(gcmKey);

        UserService userService = SingletonRestClient.createService(UserService.class, mContext);
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        userService.register(file, userJson, new Callback<MessageDTO>() {
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
                    User registeredUser = messageDTO.getUser();
                    registeredUser.saveUser(mContext);
                    if (!mIsEdit) {
                        /*Intent intent = new Intent(SignupActivity.this, MainCategoryActivity.class);
                        startActivity(intent);*/
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        initScreen();
                        Toast.makeText(SignupActivity.this, "Profile Updated...", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SignupActivity.this, "Some Error Occured, Please try again..", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.dismiss();
                Toast.makeText(SignupActivity.this, "Please Check your Connectivity and try again..", Toast.LENGTH_SHORT).show();
            }
        });
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
