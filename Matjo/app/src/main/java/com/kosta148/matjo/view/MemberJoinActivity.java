package com.kosta148.matjo.view;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.kosta148.matjo.bean.AddressBean;
import com.kosta148.matjo.data.MemberBean;
import com.kosta148.matjo.util.AddressCityAPI;
import com.kosta148.matjo.util.AddressLocalAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DaeHyoung on 2017-05-14.
 */

public class MemberJoinActivity extends AppCompatActivity {

    // xml 위젯 선언
    private EditText etId;
    private EditText etEmail;
    private Spinner spinnerEmail;
    private EditText etPassword;
    private EditText etNickname;
    private Spinner spinnerHp;
    private EditText etHp;
    private Spinner spinnerCity;
    private Spinner spinnerLocal;
    private Spinner spinnerQuestion;
    private EditText etAnswer;
    private Button btnSubmit;

    // 다이얼로그 상수
    private static final int DIALOG_ID_NULL = 1;
    private static final int DIALOG_PW_NULL = 2;

    // SharedPreferences 선언
    private SharedPreferences sharedPreferences;

    // SharedPreferences 키 상수
    private static final String SHAREDPREFERENCES_LOGIN_ID = "LoginId";
    private static final String SHAREDPREFERENCES_LOGIN_PW = "LoginPassword";

    // Handler 객체
    Handler handler = new Handler();

    List<AddressBean> localList;
    ArrayList<String> localArrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberjoin);

        getSupportActionBar().setTitle("회원가입");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // xml 위젯 초기화
        etId = (EditText)findViewById(R.id.etId);
        etEmail = (EditText)findViewById(R.id.etEmail);
        spinnerEmail = (Spinner)findViewById(R.id.spinnerEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etNickname = (EditText)findViewById(R.id.etNickname);
        spinnerHp = (Spinner)findViewById(R.id.spinnerHp);
        etHp = (EditText)findViewById(R.id.etHp);
        spinnerCity = (Spinner)findViewById(R.id.spinnerCity);
        spinnerLocal = (Spinner)findViewById(R.id.spinnerLocal);
        spinnerQuestion = (Spinner)findViewById(R.id.spinnerQuestion);
        etAnswer = (EditText)findViewById(R.id.etAnswer);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("LoginSetting.dat", MODE_PRIVATE);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(etId.getText().toString())) {
                    // 아이디를 입력하지 않을 때
                    showDialog(DIALOG_ID_NULL);
                    return;
                }
                if ("".equals(etPassword.getText().toString())) {
                    // 비밀번호가 입력하지 않을 때
                    showDialog(DIALOG_PW_NULL);
                    return;
                }

                insertVolley();
            }
        });

        // spinnerEmail setting
        spinnerEmail.setSelection(0);
        spinnerEmail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 선택되었을 때 호출되는 콜백 메서드
                if(position != 0){
                    etEmail.setEnabled(false);
                    etEmail.setText(spinnerEmail.getItemAtPosition(position).toString());

                } else {
                    etEmail.setEnabled(true);
                    etEmail.setText("");
                }
                spinnerEmail.setSelection(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무것도 선택되지 않았을 때 호출되는 콜백 메서드
            }
        });

        // spinnerHp setting
        spinnerHp.setSelection(0);
        spinnerHp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 선택되었을 때 호출되는 콜백 메서드
                spinnerHp.setSelection(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무것도 선택되지 않았을 때 호출되는 콜백 메서드
            }
        });

        // spinnerCity setting
        List<AddressBean> cityList = new AddressCityAPI().addrList;
        ArrayList<String> cityArrayList = new ArrayList<String>();
        for (int i = 0; i < cityList.size(); i++) {
            cityArrayList.add(cityList.get(i).getName());
        }

        ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cityArrayList);
        //스피너 속성
        spinnerCity.setAdapter(adapterCity);

        spinnerCity.setSelection(0);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerCity.setSelection(position);
//                Toast.makeText(getApplicationContext(), "도시 : " + spinnerCity.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                localList = new AddressLocalAPI().getList(spinnerCity.getSelectedItem().toString());
                localArrayList = new ArrayList<String>();
                for (int i = 0; i < localList.size(); i++) {
                    localArrayList.add(localList.get(i).getName());
                }
                ArrayAdapter<String> adapterLocal = new ArrayAdapter<String>(MemberJoinActivity.this, android.R.layout.simple_spinner_dropdown_item, localArrayList);
                spinnerLocal.setAdapter(adapterLocal);

                spinnerLocal.setSelection(0);
                spinnerLocal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        spinnerLocal.setSelection(position);
//                        Toast.makeText(getApplicationContext(), "지역 : " + spinnerLocal.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
            case DIALOG_ID_NULL:
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(this);
                dialog1.setTitle("회원가입 실패");
                dialog1.setMessage("아이디를 입력하지 않았습니다.");
                dialog1.setPositiveButton("확인", null);

                return dialog1.create();
            case DIALOG_PW_NULL:
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(this);
                dialog2.setTitle("회원가입 실패");
                dialog2.setMessage("비밀번호를 입력하지 않았습니다.");
                dialog2.setPositiveButton("확인", null);

                return dialog2.create();
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
            case DIALOG_ID_NULL:
                break;
            case DIALOG_PW_NULL:
                break;
        }
    } // end of onPrepareDialog

    // 회원가입 서버연동
    public void insertVolley() {

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
                    editor.putString(SHAREDPREFERENCES_LOGIN_ID, etId.getText().toString());
                    editor.putString(SHAREDPREFERENCES_LOGIN_PW, etPassword.getText().toString());
                    editor.commit(); // 변경사항을 저장하기

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
                params.put("memberId", etId.getText().toString()+"@"+etEmail.getText().toString());
                params.put("memberPw", etPassword.getText().toString());
                params.put("memberName", etNickname.getText().toString());
                params.put("memberHp", spinnerHp.getSelectedItem().toString()+etHp.getText().toString());
                params.put("memberCity", spinnerCity.getSelectedItem().toString());
                params.put("memberLocal", spinnerLocal.getSelectedItem().toString());
                params.put("memberQuestion", spinnerQuestion.getSelectedItemPosition()+"");
                params.put("memberAnswer", etAnswer.getText().toString());

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
