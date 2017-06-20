package com.kosta148.matjo.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.OAuthLoginDefine;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;


import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.name;

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
    private static final String SHAREDPREFERENCES_LOGIN_NAME = "LoginName";
    private static final String SHAREDPREFERENCES_LOGIN_IMAGE = "LoginImage";
    private static final String SHAREDPREFERENCES_MEMBER_NO = "memberNo";
    private static final String SHAREDPREFERENCES_LOGIN_AUTO = "AutoLogin";

    // Handler 객체
    Handler handler = new Handler();

    //네이버로그인 버튼
    OAuthLoginButton mOAuthLoginButton;
    //네이버 로그인 정보
    OAuthLogin mOAuthLoginModule;
    //네이버로그인시 필요-버튼클릭시 이동
    Activity activity;

    //네이버로그인 정보 저장할 변수
    String email = "";
    String nickname = "";
    String profile_image = "";

    /**
     * OAuthLoginHandler를 startOAuthLoginActivity() 메서드 호출 시 파라미터로 전달하거나 OAuthLoginButton
     객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다.
     */
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginModule.getAccessToken(getApplicationContext());
                String refreshToken = mOAuthLoginModule.getRefreshToken(getApplicationContext());
                long expiresAt = mOAuthLoginModule.getExpiresAt(getApplicationContext());
                String tokenType = mOAuthLoginModule.getTokenType(getApplicationContext());
//                    mOauthAT.setText(accessToken);
//                    mOauthRT.setText(refreshToken);
//                    mOauthExpires.setText(String.valueOf(expiresAt));
//                    mOauthTokenType.setText(tokenType);
//                    mOAuthState.setText(mOAuthLoginModule.getState(getApplicationContext()).toString());
                new RequestApiTask().execute();

            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(getApplicationContext()).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(getApplicationContext());
                Toast.makeText(getApplicationContext(), "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_LONG).show();
            }
        };
    };

    private class RequestApiTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected Void doInBackground(Void... params) {
            String url = "https://openapi.naver.com/v1/nid/getUserProfile.xml";
            String at = mOAuthLoginModule.getAccessToken(getApplicationContext());
            Pasingversiondata(mOAuthLoginModule.requestApi(getApplicationContext(), at, url));
            return null;
        }

        protected void onPostExecute(Void content) {
            Log.d("myLog", "email " + email);
            Log.d("myLog", "name " + name);
            Log.d("myLog", "nickname " + nickname);

            if (email == null) {
                Toast.makeText(activity,
                        "로그인 실패하였습니다.  잠시후 다시 시도해 주세요", Toast.LENGTH_SHORT)
                        .show();
            } else {
            }
        }
        private void Pasingversiondata(String data) { // xml 파싱
            String f_array[] = new String[9];
            try {
                XmlPullParserFactory parserCreator = XmlPullParserFactory
                        .newInstance();
                XmlPullParser parser = parserCreator.newPullParser();
                InputStream input = new ByteArrayInputStream(
                        data.getBytes("UTF-8"));
                parser.setInput(input, "UTF-8");
                int parserEvent = parser.getEventType();
                String tag;
                boolean inText = false;
                boolean lastMatTag = false;
                int colIdx = 0;
                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG:
                            tag = parser.getName();
                            if (tag.compareTo("xml") == 0) {
                                inText = false;
                            } else if (tag.compareTo("data") == 0) {
                                inText = false;
                            } else if (tag.compareTo("result") == 0) {
                                inText = false;
                            } else if (tag.compareTo("resultcode") == 0) {
                                inText = false;
                            } else if (tag.compareTo("message") == 0) {
                                inText = false;
                            } else if (tag.compareTo("response") == 0) {
                                inText = false;
                            } else {
                                inText = true;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            tag = parser.getName();
                            if (inText) {
                                if (parser.getText() == null) {
                                    f_array[colIdx] = "";
                                } else {
                                    f_array[colIdx] = parser.getText().trim();
                                }
                                colIdx++;
                            }
                            inText = false;
                            break;
                        case XmlPullParser.END_TAG:
                            tag = parser.getName();
                            inText = false;
                            break;
                    }
                    parserEvent = parser.next();
                }
            } catch (Exception e) {
            }
            email =  f_array[7];
            nickname = f_array[6];
            profile_image = f_array[2];

            insertVolley();
        }
    }

    // 가입되어 있는 것 확인 후, 로그인 후, 이동
    public void insertVolley() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/member/selectMemberProc.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonParser parser = new JsonParser();
                JsonObject rootObj = (parser.parse(response)).getAsJsonObject();

                Gson gson = new Gson();

                String resultData = rootObj.get("result").getAsString();
                if ("success".equals(resultData)) {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SHAREDPREFERENCES_LOGIN_AUTO, checkBoxAutoLogin.isChecked());
                    editor.putString(SHAREDPREFERENCES_LOGIN_ID, email);
                    editor.putString(SHAREDPREFERENCES_LOGIN_NAME, nickname);
                    editor.putString(SHAREDPREFERENCES_LOGIN_IMAGE, profile_image);
                    editor.commit(); // 변경사항을 저장하기

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    insertVolleyNaverLogin();

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
                params.put("memberId", email);
                params.put("memberName", nickname);
                params.put("memberImg", profile_image);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    //네이버 로그인으로 회원가입 후,로그인 후, 이동
    public void insertVolleyNaverLogin() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/member/insertMemberProc.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonParser parser = new JsonParser();
                JsonObject rootObj = (parser.parse(response)).getAsJsonObject();

                Gson gson = new Gson();

                String resultData = rootObj.get("result").getAsString();
                if ("success".equals(resultData)) {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SHAREDPREFERENCES_LOGIN_AUTO, checkBoxAutoLogin.isChecked());
                    editor.putString(SHAREDPREFERENCES_LOGIN_ID, email);
                    editor.putString(SHAREDPREFERENCES_LOGIN_NAME, nickname);
                    editor.putString(SHAREDPREFERENCES_LOGIN_IMAGE, profile_image);
                    editor.commit(); // 변경사항을 저장하기

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(),"네이버 아이디로 로그인 실패",Toast.LENGTH_SHORT).show();
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
                params.put("memberId", email);
                params.put("memberName", nickname);
                params.put("memberImg", profile_image);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginform);
        activity = this;

        mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
                mOAuthLoginButton.setBgResourceId(R.drawable.naver_login_icon);

                //네이버 로그인 정보 저장
                mOAuthLoginModule = OAuthLogin.getInstance();
                mOAuthLoginModule.init(
                        getApplicationContext()
                        ,"R_2VTJtZ2pPxEqkyzxXH"
                        ,"RqId8DRmdG"
                        ,"Matjo"
                );
                mOAuthLoginModule.startOauthLoginActivity(activity, mOAuthLoginHandler);
            }
        });


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
                    editor.putString(SHAREDPREFERENCES_MEMBER_NO, mBean.getMemberNo());
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

