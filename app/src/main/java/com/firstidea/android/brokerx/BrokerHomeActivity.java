package com.firstidea.android.brokerx;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firstidea.android.brokerx.adapter.PendingEnqBrokerAdapter;
import com.firstidea.android.brokerx.constants.AppConstants;
import com.firstidea.android.brokerx.fragment.broker.BrokerHomeFragment;
import com.firstidea.android.brokerx.http.SingletonRestClient;
import com.firstidea.android.brokerx.http.model.User;
import com.firstidea.android.brokerx.model.PendingEntries;
import com.firstidea.android.brokerx.utils.AppUtils;
import com.firstidea.android.brokerx.utils.SharedPreferencesUtil;
import com.squareup.picasso.Picasso;

public class BrokerHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private android.support.v4.app.FragmentManager fragmentManager;
    private android.support.v4.app.Fragment currentFragment = null;
    private Button mEditProfle;
    NavigationView navigationView;
    private User me;

    public static final int ACTION_ACTIVITY = 1007;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broker_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        android.support.v4.app.Fragment fragment = new BrokerHomeFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.content, fragment).commit();

        me = User.getSavedUser(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button viewProfileButton = (Button) navigationView.getHeaderView(0).findViewById(R.id.editProfileButton);
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrokerHomeActivity.this, SignupActivity.class);
                intent.putExtra(AppConstants.KEY_IS_PROFILE_EDIT, true);
                startActivity(intent);
            }
        });
        initDrawerHeader();
        AppUtils.loadFCMid(this);
    }

    private void initDrawerHeader() {
        ImageView userImage = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_image);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrokerHomeActivity.this, SignupActivity.class);
                intent.putExtra(AppConstants.KEY_IS_PROFILE_EDIT, true);
                startActivity(intent);
            }
        });
        if(!TextUtils.isEmpty(me.getImageURL())) {
            String imgUrl = SingletonRestClient.BASE_PROFILE_IMAGE_URL + me.getImageURL();
            Picasso.with(this).load(imgUrl)
                    .error(R.drawable.user_img)
                    .placeholder(R.drawable.user_img)
                    .into(userImage);
        }
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.user_name)).setText(me.getFullName());
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.user_phone)).setText(me.getMobile());
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.broker_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*if (id == R.id.action_chat) {
            return true;
        }*/
        if (id == R.id.action_notification) {
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            currentFragment = new BrokerHomeFragment();
        } else if (id == R.id.nav_my_circle) {
            Intent intent=new Intent(BrokerHomeActivity.this, MycircleActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_pending_enquiries) {
            Intent intent=new Intent(BrokerHomeActivity.this, PendingEntriesActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_history) {
            Intent intent=new Intent(BrokerHomeActivity.this, MyHistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_analysis) {
            Intent intent=new Intent(BrokerHomeActivity.this, AnalysisBrokerActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            SharedPreferencesUtil.clearAll(this);
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        }

        changeFragment();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void changeFragment() {
        if (currentFragment != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content, currentFragment).commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTION_ACTIVITY && resultCode == Activity.RESULT_OK) {
            currentFragment = new BrokerHomeFragment();
            changeFragment();
        }
    }
}
