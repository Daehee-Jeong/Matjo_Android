package com.kosta148.matjo.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosta148.matjo.R;
import com.kosta148.matjo.data.MemberBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DaeHyoung on 2017-05-14.
 */

public class PasswordFinderActivity extends AppCompatActivity {
    // xml 위젯 선언
    private EditText etId;
    private Button btnIdSubmit;
    private Spinner spinnerQuestion;
    private EditText etAnswer;
    private Button btnQuestionSubmit;
    private EditText etNewPassword;

    // 다이얼로그 상수
    private static final int DIALOG_ID_DIFFERENT = 1;
    private static final int DIALOG_QUESTION_DIFFERENT = 2;
    private static final int DIALOG_QUESTION_SUCCESS = 3;

    // Handler 객체
    Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordfinder);

        getSupportActionBar().setTitle("비밀번호 찾기");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // xml 위젯 초기화
        etId = (EditText)findViewById(R.id.etId);
        btnIdSubmit = (Button)findViewById(R.id.btnIdSubmit);
        spinnerQuestion = (Spinner)findViewById(R.id.spinnerQuestion);
        etAnswer = (EditText)findViewById(R.id.etAnswer);
        btnQuestionSubmit = (Button)findViewById(R.id.btnQuestionSubmit);

        btnIdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findIdVolley();
            }
        });

        btnQuestionSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findQuestionVolley();
            }
        });

        // spinnerQuestion setting
        spinnerQuestion.setSelection(0);
        spinnerQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 선택되었을 때 호출되는 콜백 메서드
                spinnerQuestion.setSelection(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무것도 선택되지 않았을 때 호출되는 콜백 메서드
            }
        });

    } // end of onCreate

    // 다이얼로그 onCreate
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_ID_DIFFERENT:
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(this);
                dialog1.setTitle("아이디 확인");
                dialog1.setMessage("아이디가 일치하지 않습니다.");
                dialog1.setPositiveButton("확인", null);

                return dialog1.create();
            case DIALOG_QUESTION_DIFFERENT:
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(this);
                dialog2.setTitle("질문 답 확인");
                dialog2.setMessage("질문 및 답이 일치하지 않습니다.");
                dialog2.setPositiveButton("확인", null);

                return dialog2.create();
            case DIALOG_QUESTION_SUCCESS:
                AlertDialog.Builder dialog3 = new AlertDialog.Builder(this);
                LayoutInflater lif = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = lif.inflate(R.layout.dialog_newpassword, null);

                final EditText etPassword = (EditText) view.findViewById(R.id.etPassword);
                final EditText etPasswordCheck = (EditText) view.findViewById(R.id.etPasswordCheck);
                etNewPassword = (EditText) view.findViewById(R.id.etPassword);
                dialog3.setTitle("비밀번호 설정");
                dialog3.setView(view);
                dialog3.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newPasswordVolley();
                    }
                });
                return dialog3.create();
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
            case DIALOG_ID_DIFFERENT:
                break;
            case DIALOG_QUESTION_DIFFERENT:
                break;
            case DIALOG_QUESTION_SUCCESS:
                break;
        }
    } // end of onPrepareDialog

    // 아이디 확인 서버연동
    public void findIdVolley() {
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
                if ("success".equals(resultData)) {
                    spinnerQuestion.setVisibility(View.VISIBLE);
                    etAnswer.setVisibility(View.VISIBLE);
                    btnQuestionSubmit.setVisibility(View.VISIBLE);
                } else {
                    showDialog(DIALOG_ID_DIFFERENT);
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
                params.put("memberId", etId.getText().toString());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    // 질문 답 확인 서버연동
    public void findQuestionVolley() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/member/findMemberProc.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonParser parser = new JsonParser();
                JsonObject rootObj = (parser.parse(response)).getAsJsonObject();

                Gson gson = new Gson();
                JsonObject mBeanJsonObj = rootObj.getAsJsonObject("mBean");
                String mBeanStr = gson.toJson(mBeanJsonObj);
                MemberBean mBean = gson.fromJson(mBeanStr, MemberBean.class);

                String resultData = rootObj.get("result").getAsString();
                if ("success".equals(resultData)) {
                    showDialog(DIALOG_QUESTION_SUCCESS);
                } else {
                    showDialog(DIALOG_QUESTION_DIFFERENT);
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
                params.put("memberId", etId.getText().toString());
                params.put("memberQuestion", spinnerQuestion.getSelectedItemPosition()+"");
                params.put("memberAnswer", etAnswer.getText().toString());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    // 새 비밀번호 설정 서버연동
    public void newPasswordVolley() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/member/updateMemberProc.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonParser parser = new JsonParser();
                JsonObject rootObj = (parser.parse(response)).getAsJsonObject();

                Gson gson = new Gson();

                String resultData = rootObj.get("result").getAsString();
                if ("success".equals(resultData)) {
                    Toast.makeText(getApplicationContext(), "새 비밀번호 설정에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "새 비밀번호 설정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
                params.put("memberId", etId.getText().toString());
                params.put("memberPw", etNewPassword.getText().toString());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
} // end of class
