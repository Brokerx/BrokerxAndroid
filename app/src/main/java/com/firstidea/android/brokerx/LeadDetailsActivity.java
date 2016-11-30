package com.firstidea.android.brokerx;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.firstidea.android.brokerx.adapter.LeadHistoryPagerAdapter;


public class LeadDetailsActivity extends AppCompatActivity {
/*
    private ViewPager viewPager;
    private LeadHistoryPagerAdapter viewPagerAdapter;*/

    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Enquiry Details");
        actionBar.setHomeButtonEnabled(true);

        iniScreen();
    }

    private void iniScreen() {
        /*viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPagerAdapter = new LeadHistoryPagerAdapter(getSupportFragmentManager());
        viewPager.setCurrentItem(2);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                viewPager.getParent().requestDisallowInterceptTouchEvent(true);
            }
        });
viewPager.setAdapter(viewPagerAdapter);*/
        findViewById(R.id.btn_revert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });

        mPager = (ViewPager) findViewById(R.id.viewpager);
        mPagerAdapter = new LeadHistoryPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    public void move(View view) {
        int moveIndex = Integer.parseInt(view.getTag().toString());
        int nextIndex = mPager.getCurrentItem() + moveIndex;
        if (nextIndex >= mPagerAdapter.getCount()) {
            nextIndex = 0;
        }
        if (nextIndex < 0) {
            nextIndex = mPagerAdapter.getCount() - 1;
        }
        mPager.setCurrentItem(nextIndex);
    }

    private void showInputDialog() {
        boolean wrapInScrollView = true;
        final View view = findViewById(R.id.layout_buttons);
        new MaterialDialog.Builder(this)
                .title("Alter Details")
                .customView(R.layout.dialog_alter_details, wrapInScrollView)
                .positiveText("Alter")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        View customView = dialog.getCustomView();
                        String details = ((EditText) customView.findViewById(R.id.edt_details)).getText().toString();
                        String rate = ((EditText) customView.findViewById(R.id.edt_rate)).getText().toString();
                        Snackbar.make(view, Html.fromHtml("<b> Details: </b>" + details + "<br><b> Rate: </b>&#8377; " + rate), Snackbar.LENGTH_INDEFINITE)
                                .setAction("Send", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                }).show();
                    }
                })
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
