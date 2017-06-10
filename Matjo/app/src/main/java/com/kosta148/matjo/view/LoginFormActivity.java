package com.kosta148.matjo.view;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.kosta148.matjo.R;

public class LoginFormActivity extends AppCompatActivity {

    // xml 위젯 선언
    private EditText etId;
    private EditText etPassword;
    private CheckBox checkBoxAutoLogin;
    private Button btnKakaoLogin;
    private Button btnLogin;
    private TextView tvNotMember;
    private TextView tvPasswordFinder;

    // 로그인 아이디
    private static final String LOGIN_ID = "root";
    private static final String LOGIN_PASSWORD = "root";

    // 다이얼로그 상수
    private static final int DIALOG_ID_DIFFERENT = 1;
    private static final int DIALOG_PW_DIFFERENT = 2;

    // SharedPreferences 선언
    private SharedPreferences sharedPreferences;

    // SHaredPreferences 키 상수
    private static final String SHAREDPREFERENCES_LOGIN_ID = "LoginId";
    private static final String SHAREDPREFERENCES_LOGIN_PW = "LoginPassword";
    private static final String SHAREDPREFERENCES_LOGIN_AUTO = "AutoLogin";

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
                Login();
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

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHAREDPREFERENCES_LOGIN_ID, "root");
        editor.putString(SHAREDPREFERENCES_LOGIN_PW, "root");
        editor.commit(); // 변경사항을 저장하기

    } // end of onCreate

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreferences != null) {
            etId.setText(sharedPreferences.getString(SHAREDPREFERENCES_LOGIN_ID, ""));
            etPassword.setText(sharedPreferences.getString(SHAREDPREFERENCES_LOGIN_PW, ""));
            checkBoxAutoLogin.setChecked(sharedPreferences.getBoolean(SHAREDPREFERENCES_LOGIN_AUTO, false));
            if (sharedPreferences.getBoolean(SHAREDPREFERENCES_LOGIN_AUTO, false)) {
                // 자동 로그인 설정시
                Login();
            }
        }
    } // end of onResume

    // 다이얼로그 onCreate
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_ID_DIFFERENT:
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(this);
                dialog1.setTitle("로그인 실패");
                dialog1.setMessage("아이디가 일치하지 않습니다.");
                dialog1.setPositiveButton("확인", null);

                return dialog1.create();
            case DIALOG_PW_DIFFERENT:
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(this);
                dialog2.setTitle("로그인 실패");
                dialog2.setMessage("비밀번호가 일치하지 않습니다.");
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
            case DIALOG_ID_DIFFERENT:
                break;
            case DIALOG_PW_DIFFERENT:
                break;
        }
    } // end of onPrepareDialog

    public void Login() {
        // 로그인 버튼 클릭시

        if (!(LOGIN_ID.equals(etId.getText().toString()))) {
            // 아이디가 일치하지 않을 때
            showDialog(DIALOG_ID_DIFFERENT);
            return;
        }
        if (!(LOGIN_PASSWORD.equals(etPassword.getText().toString()))) {
            // 비밀번호가 일치하지 않을 때
            showDialog(DIALOG_PW_DIFFERENT);
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHAREDPREFERENCES_LOGIN_AUTO, checkBoxAutoLogin.isChecked());
        editor.putString(SHAREDPREFERENCES_LOGIN_ID, etId.getText().toString());
        editor.putString(SHAREDPREFERENCES_LOGIN_PW, etPassword.getText().toString());
        editor.commit(); // 변경사항을 저장하기

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    } // end of Login;
} // end of class
