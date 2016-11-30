package com.firstidea.android.brokerx;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firstidea.android.brokerx.adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.logo));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar.setScrollbarFadingEnabled(true);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        final TabLayout.Tab newProspects = tabLayout.newTab();
        final TabLayout.Tab oldProspects = tabLayout.newTab();
        final TabLayout.Tab oldProspects1 = tabLayout.newTab();
        final TabLayout.Tab oldProspects2 = tabLayout.newTab();
        final TabLayout.Tab oldProspects3 = tabLayout.newTab();
        tabLayout.addTab(newProspects, 0);
        tabLayout.addTab(oldProspects, 1);
        tabLayout.addTab(oldProspects1, 2);
        tabLayout.addTab(oldProspects2, 3);
        tabLayout.addTab(oldProspects3, 4);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                Fragment fragment = null;
//                switch (position) {
//                    case 0:
//                        viewPagerAdapter.getItem(position);
//                        break;
//                    case 1:
//
//                        break;
//
//                }
                viewPagerAdapter.getItem(position);
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    if(tab == null) break;
                    if(tab.getCustomView() == null) break;
                    ImageView tabImage = (ImageView) tab.getCustomView().findViewById(R.id.tab_image);
                    if(i==0){
                        tabImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.circle_normal));
                    } else if(i==1){
                        tabImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.b_normal));
                    } else if(i==2){
                        tabImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.s_normal));
                    } else if(i==3){
                        tabImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.p_normal));
                    } else if(i==4){
                        tabImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.activity_normal));
                    }
                }

                TabLayout.Tab tab = tabLayout.getTabAt(position);
                if(tab == null) return;
                if(tab.getCustomView() == null) return;

                ImageView tabImage = (ImageView) tab.getCustomView().findViewById(R.id.tab_image);
                if(position==0){
                    tabImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.circle_selected));
                } else if(position==1){
                    tabImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.b_selected));
                } else if(position==2){
                    tabImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.s_selected));
                } else if(position==3){
                    tabImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.p_selected));
                } else if(position==4){
                    tabImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.activity_selected));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        retriveProspectData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        tabLayout.setupWithViewPager(viewPager);


        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(this).inflate(R.layout.tab_layout, tabLayout, false);

            ImageView tabImage = (ImageView) relativeLayout.findViewById(R.id.tab_image);
            if(i==0){
                tabImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.circle_normal));
            } else if(i==1){
                tabImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.b_normal));
            } else if(i==2){
                tabImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.s_normal));
            } else if(i==3){
                tabImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.p_normal));
            } else if(i==4){
                tabImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.activity_normal));
            }
            tab.setCustomView(relativeLayout);
            tab.select();
        }
        viewPager.setCurrentItem(0);
    }

    public void openDetails(View view) {
        Intent intent = new Intent(this, LeadDetailsActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add_buyer_enq) {
            Intent intent = new Intent(mContext, AddEnquiryActivity.class);
            intent.putExtra(Constants.KEY_ENQ_TYPE, Constants.KEY_USER_TYPE_BUYER);
            startActivity(intent);
            return true;
        }
        if(item.getItemId() == R.id.action_add_seller_enq) {
            Intent intent = new Intent(mContext, AddEnquiryActivity.class);
            intent.putExtra(Constants.KEY_ENQ_TYPE, Constants.KEY_USER_TYPE_SELLER);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
