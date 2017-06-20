package com.kosta148.matjo.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.kosta148.matjo.R;

/**
 * Created by kosta on 2017-04-24.
 */

public class SplashActivity extends Activity {

    // SharedPreferences 선언
    private SharedPreferences sharedPreferences;
    // SharedPreferences 키 상수
    private static final String SHAREDPREFERENCES_LOGIN_AUTO = "AutoLogin";

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("LoginSetting.dat", MODE_PRIVATE);
    } // end of onCreate

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        boolean autoFlag = true;
        if (sharedPreferences != null) {
            if (sharedPreferences.getBoolean(SHAREDPREFERENCES_LOGIN_AUTO, false)) {
                // 자동 로그인 설정시
                autoFlag = false;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 1000);

            }
        }
        if (autoFlag) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginFormActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }
        super.onResume();
    } // end of onResume
} // end of class
