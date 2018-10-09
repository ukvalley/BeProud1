package com.ukvalley.umeshkhivasara.beproud;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.onesignal.OneSignal;
import com.ukvalley.umeshkhivasara.beproud.supports.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private static boolean activityStarted;

    private ImageView advertise;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (   activityStarted
                && getIntent() != null
                && (getIntent().getFlags() & Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) != 0) {
            finish();
            return;
        }

        activityStarted = true;

        sessionManager = new SessionManager(this);
      //  ActionBar actionBar = getSupportActionBar();
      //  actionBar.hide();

        advertise = (ImageView) findViewById(R.id.advertisement);
        CountDownTimer countDownTimer = new CountDownTimer(3000, 1500) {
            int count = 1;

            @Override
            public void onTick(long l) {

                if (count == 1) {
                 //   advertise.setImageResource(R.drawable.telephone);

                    count++;
                }

                else

                {
              //      advertise.setImageResource(R.drawable.telephone);
                    count--;
                }
            }

            @Override
            public void onFinish () {
                boolean b = sessionManager.checkLogin();
                if (b != true) {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
                finish();

            }
        }

                ;
        countDownTimer.start();

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }
}

