package com.firstidea.android.brokerx;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;

import com.firstidea.android.brokerx.http.model.User;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

    Context mContext = this;
    boolean _active = true;
    int _splashTime = 3000; // time to display the splash screen in ms
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        user = User.getSavedUser(this);
        if(user != null) {
            // thread for displaying the SplashScreen
            Thread splashTread = new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    try {
                        int waited = 0;
                        while (_active && (waited < _splashTime)) {
                            sleep(100);
                            if (_active) {
                                waited += 100;
                            }
                        }
                    } catch (InterruptedException e) {
                        // do nothing
                    } finally {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                startApp();
                            }
                        });

                    }
                }
            };
            splashTread.start();
        } else {
            findViewById(R.id.fullscreen_content_controls).setVisibility(View.VISIBLE);
            findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivityForResult(intent, 101);
                }
            });

            findViewById(R.id.signup_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SignupActivity.class);
                    startActivityForResult(intent, 102);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(RESULT_OK == resultCode) {
            user = User.getSavedUser(this);
            startApp();
        }

    }

    private void startApp() {

        Intent intent;
        if(user.getBroker()) {
            intent = new Intent(this, BrokerHomeActivity.class);
        } else {
            intent = new Intent(this, BuyerSellerHomeActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
