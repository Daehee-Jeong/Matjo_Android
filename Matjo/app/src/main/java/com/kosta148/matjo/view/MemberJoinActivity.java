package com.kosta148.matjo.view;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.kosta148.matjo.R;

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
    private EditText etAddress1;
    private EditText etAddress2;
    private Spinner spinnerQuestion;
    private EditText etAnswer;
    private Button btnSubmit;

    // 다이얼로그 상수
    private static final int DIALOG_ID_NULL = 1;
    private static final int DIALOG_PW_NULL = 2;

    // SharedPreferences 선언
    private SharedPreferences sharedPreferences;

    // SHaredPreferences 키 상수
    private static final String SHAREDPREFERENCES_LOGIN_ID = "LoginId";
    private static final String SHAREDPREFERENCES_LOGIN_PW = "LoginPassword";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberjoin);

        // xml 위젯 초기화
        etId = (EditText)findViewById(R.id.etId);
        etEmail = (EditText)findViewById(R.id.etEmail);
        spinnerEmail = (Spinner)findViewById(R.id.spinnerEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        etNickname = (EditText)findViewById(R.id.etNickname);
        spinnerHp = (Spinner)findViewById(R.id.spinnerHp);
        etHp = (EditText)findViewById(R.id.etHp);
        etAddress1 = (EditText)findViewById(R.id.etAddress1);
        etAddress2 = (EditText)findViewById(R.id.etAddress2);
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

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SHAREDPREFERENCES_LOGIN_ID, etId.getText().toString());
                editor.putString(SHAREDPREFERENCES_LOGIN_PW, etPassword.getText().toString());
                editor.commit(); // 변경사항을 저장하기

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
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

} // end of class
