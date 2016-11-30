package com.firstidea.android.brokerx;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firstidea.android.brokerx.adapter.MyCircleRecyclerViewAdapter;
import com.firstidea.android.brokerx.constants.MsgConstants;
import com.firstidea.android.brokerx.enums.ConnectionStatus;
import com.firstidea.android.brokerx.http.ObjectFactory;
import com.firstidea.android.brokerx.http.model.MessageDTO;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.widget.AppProgressDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.Manifest.permission.READ_CONTACTS;

public class MycircleActivity extends AppCompatActivity implements MyCircleRecyclerViewAdapter.ChangeStatusListener {
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 101;
    private static final int PICK_CONTACT_REQUEST = 102;
    private static final int ADD_USER_REQUEST_CODE = 103;


    private User mUser;
    private List<User> mUsers;
    RecyclerView recyclerView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycircle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("My Circle");
        mContext = this;
        mUser = User.getSavedUser(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        getMyCircle();
    }

    private void getMyCircle() {
        final Dialog dialog = AppProgressDialog.show(mContext);
        ObjectFactory.getInstance().getUserServiceInstance().getUserConnections(mUser.getUserID(), new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if (messageDTO.getMessageID().equals(MsgConstants.SUCCESS_ID)) {
                    mUsers = User.createListFromJson(messageDTO.getData());
                    initialiseAdapter();
                } else {
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.dismiss();
            }
        });
    }

    private void initialiseAdapter() {
        List<User> pendingUsers = new ArrayList<>();
        List<User> acceptedUsers = new ArrayList<>();
        for (User user : mUsers) {
            if (user.getStatus().equals(ConnectionStatus.PENDING.getStatus())) {
                pendingUsers.add(user);
            } else if (user.getStatus().equals(ConnectionStatus.ACCEPTED.getStatus())) {
                acceptedUsers.add(user);
            }
        }
        List<List<User>> adapterUsers = new ArrayList<>();
        if (getIntent().hasExtra(Constants.KEY_IS_FOR_SELECTION) && getIntent().getBooleanExtra(Constants.KEY_IS_FOR_SELECTION, false)) {
            adapterUsers.add(acceptedUsers);
            recyclerView.setAdapter(new MyCircleRecyclerViewAdapter(adapterUsers, true, MycircleActivity.this, MycircleActivity.this));
        } else {
            adapterUsers.add(pendingUsers);
            adapterUsers.add(acceptedUsers);
            recyclerView.setAdapter(new MyCircleRecyclerViewAdapter(adapterUsers, false, MycircleActivity.this, MycircleActivity.this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_circle_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_to_circle) {
            if (requestContactsPermission()) {
                openContactPicker();
            }
            return true;
        }
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean requestContactsPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);
            builder.setTitle("Permission Required");
            String msg = getString(R.string.contact_permission_dialog_contents);
            builder.setMessage(Html.fromHtml(msg));
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                }
            });
            builder.show();

        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openContactPicker();
            }
        }
    }

    private void openContactPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT_REQUEST):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1", null, null);
                    if (c.moveToFirst()) {
                        String id =
                                c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone =
                                c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            String phn_no = phones.getString(phones.getColumnIndex("data1"));
                            String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.DISPLAY_NAME));
//                            Toast.makeText(this, "contact info : " + phn_no + "\n" + name, Toast.LENGTH_LONG).show();
                            getPersonDetails(name, phn_no);
                        }
                    } else {
                        Toast.makeText(mContext, "Please select the contact having number", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            case ADD_USER_REQUEST_CODE:
                if(resultCode == RESULT_OK) {
                    getMyCircle();
                }
                break;
        }
    }

    private void getPersonDetails(final String name, String number) {
        number=number.replace(" ","");
        if(number.length()>10) {
            number = number.substring(number.length()-10,number.length());
        }
        boolean isAlreadyConnected= false;
        for(User user: mUsers) {
            if(user.getMobile().equals(number)) {
                isAlreadyConnected = true;
                break;
            }
        }
        if(isAlreadyConnected) {
            Toast.makeText(mContext, "You already connected to this user", Toast.LENGTH_SHORT).show();
            return;
        }
        final Dialog dialog = AppProgressDialog.show(mContext);
        final String finalNumber =number;
        ObjectFactory.getInstance().getUserServiceInstance().getUserByMobile(number, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                if (messageDTO.getMessageID().equals(MsgConstants.SUCCESS_ID)) {
                    User user = messageDTO.getUser();
                    if(mUser.getUserID() == user.getUserID()) {
                        Toast.makeText(mContext, "You can not send request to yourself", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        return;
                    }
                    Intent intent = new Intent(mContext, BrokerDetailsActivity.class);
                    intent.putExtra(User.KEY, user);
                    intent.putExtra("IsForConnect", true);
                    startActivityForResult(intent, ADD_USER_REQUEST_CODE);
                } else {
                    showuserDoesNotExistDialog(name, finalNumber);
                }
                dialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, "Some error Ocured", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void showuserDoesNotExistDialog(final String name, String number) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setTitle("User does not exist");
        String msg = "<b>"+name+"</b> Is not registred on Broker-X. Do you want to invite him ?";
        builder.setMessage(Html.fromHtml(msg));
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String appLink = "https://play.google.com/store/apps/details?id="+getPackageName();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hi "+name+",\nCheck out Broker-X Application to handle your daily leads"
                        +" "+appLink);
                startActivity(Intent.createChooser(sharingIntent,"Share using"));
            }
        });
        builder.setNegativeButton("NO", null);
        builder.show();
    }

    @Override
    public void onChangeStatus(User user, String status) {
        final Dialog dialog = AppProgressDialog.show(mContext);
        ObjectFactory.getInstance().getUserServiceInstance().changeConnectionStatus(user.getUserConnectionID(), status, new Callback<MessageDTO>() {
            @Override
            public void success(MessageDTO messageDTO, Response response) {
                dialog.dismiss();
                if(messageDTO.isSuccess()) {
                    getMyCircle();
                } else {
                    Toast.makeText(mContext, "Server Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                dialog.dismiss();
                Toast.makeText(mContext, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
