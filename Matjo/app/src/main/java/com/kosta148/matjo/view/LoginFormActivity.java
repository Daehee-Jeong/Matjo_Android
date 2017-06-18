package com.kosta148.matjo.view;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosta148.matjo.R;
import com.kosta148.matjo.data.MemberBean;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFormActivity extends AppCompatActivity {

    // xml 위젯 선언
    private EditText etId;
    private EditText etPassword;
    private CheckBox checkBoxAutoLogin;
    private Button btnKakaoLogin;
    private Button btnLogin;
    private TextView tvNotMember;
    private TextView tvPasswordFinder;

    // 다이얼로그 상수
    private static final int DIALOG_LOGIN_DIFFERENT = 1;

    // SharedPreferences 선언
    private SharedPreferences sharedPreferences;

    // SharedPreferences 키 상수
    private static final String SHAREDPREFERENCES_LOGIN_ID = "LoginId";
    private static final String SHAREDPREFERENCES_LOGIN_PW = "LoginPassword";
    private static final String SHAREDPREFERENCES_LOGIN_AUTO = "AutoLogin";

    // Handler 객체
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginform);

        // xml 위젯 초기화
        etId = (EditText) findViewById(R.id.etId);
        etPassword = (EditText) findViewById(R.id.etPassword);
        checkBoxAutoLogin = (CheckBox) findViewById(R.id.checkBoxAutoLogin);
        btnKakaoLogin = (Button) findViewById(R.id.btnKakaoLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvNotMember = (TextView) findViewById(R.id.tvNotMember);
        tvPasswordFinder = (TextView) findViewById(R.id.tvPasswordFinder);

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("LoginSetting.dat", MODE_PRIVATE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginVolley(etId.getText().toString(), etPassword.getText().toString());
            }
        });

        // 회원가입 화면으로 이동
        tvNotMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemberJoinActivity.class);
                startActivity(intent);
            }
        });
        // 비밀번호 찾기 화면으로 이동
        tvPasswordFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PasswordFinderActivity.class);
                startActivity(intent);
            }
        });

    } // end of onCreate

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreferences != null) {
            checkBoxAutoLogin.setChecked(sharedPreferences.getBoolean(SHAREDPREFERENCES_LOGIN_AUTO, false));
            if (sharedPreferences.getBoolean(SHAREDPREFERENCES_LOGIN_AUTO, false)) {
                // 자동 로그인 설정시
                etId.setText(sharedPreferences.getString(SHAREDPREFERENCES_LOGIN_ID, ""));
                etPassword.setText(sharedPreferences.getString(SHAREDPREFERENCES_LOGIN_PW, ""));
                loginVolley(etId.getText().toString(), etPassword.getText().toString());
            }
        }
    } // end of onResume

    // 다이얼로그 onCreate
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_LOGIN_DIFFERENT:
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(this);
                dialog1.setTitle("로그인 실패");
                dialog1.setMessage("아이디 또는 비밀번호가 일치하지 않습니다.");
                dialog1.setPositiveButton("확인", null);

                return dialog1.create();
            default:
                break;
        }
        return null;
    } // end of onCreateDialog

    // 다이얼로그 onPrepareDialog
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case DIALOG_LOGIN_DIFFERENT:
                break;
        }
    } // end of onPrepareDialog

    // 로그인 서버연동
    public void loginVolley(String memberId, String memberPw) {

        final String mId = memberId;
        final String mPw = memberPw;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/member/selectMemberProc.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonParser parser = new JsonParser();
                JsonObject rootObj = (parser.parse(response)).getAsJsonObject();

                Gson gson = new Gson();
                JsonObject mBeanJsonObj = rootObj.getAsJsonObject("mBean");
                String mBeanStr = gson.toJson(mBeanJsonObj);
                MemberBean mBean = gson.fromJson(mBeanStr, MemberBean.class);

                String resultData = rootObj.get("result").getAsString();
                Log.d("MyLog", "resultData" + resultData);
                if ("success".equals(resultData)) {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SHAREDPREFERENCES_LOGIN_AUTO, checkBoxAutoLogin.isChecked());
                    editor.putString(SHAREDPREFERENCES_LOGIN_ID, etId.getText().toString());
                    editor.putString(SHAREDPREFERENCES_LOGIN_PW, etPassword.getText().toString());
                    editor.commit(); // 변경사항을 저장하기

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showDialog(DIALOG_LOGIN_DIFFERENT);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MyLog", "error : " + error);
                final VolleyError err = error;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "error : " + err, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("memberId", mId);
                params.put("memberPw", mPw);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
} // end of class
