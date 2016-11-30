package com.firstidea.android.brokerx;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firstidea.android.brokerx.adapter.PendingEnqBrokerAdapter;
import com.firstidea.android.brokerx.fragment.broker.BrokerHomeFragment;
import com.firstidea.android.brokerx.utils.AppUtils;
import com.firstidea.android.brokerx.utils.SharedPreferencesUtil;

public class BrokerHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private android.support.v4.app.FragmentManager fragmentManager;
    private android.support.v4.app.Fragment currentFragment = null;
    private Button mEditProfle;

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        AppUtils.loadFCMid(this);
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
        if (id == R.id.action_chat) {
            return true;
        }
        if (id == R.id.action_notification) {
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
            Intent intent=new Intent(BrokerHomeActivity.this, PendingEnqBrokerActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_history) {

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
}
