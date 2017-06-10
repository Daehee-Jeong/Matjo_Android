package com.kosta148.matjo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.kosta148.matjo.R;

/**
 * Created by kosta on 2017-04-24.
 */

public class SplashActivity extends Activity {
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =  new Intent(SplashActivity.this, LoginFormActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
