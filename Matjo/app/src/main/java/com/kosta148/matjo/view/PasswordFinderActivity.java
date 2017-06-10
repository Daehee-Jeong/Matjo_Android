package com.kosta148.matjo.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kosta148.matjo.R;

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

    // 다이얼로그 상수
    private static final int DIALOG_ID_DIFFERENT = 1;
    private static final int DIALOG_QUESTION_DIFFERENT = 2;
    private static final int DIALOG_QUESTION_SUCCESS = 3;

    // 로그인 아이디
    private static final String LOGIN_ID = "root";

    // 질문 및 답
    private static final String PASS_QUESTION = "가장 잘 생긴 사람은?";
    private static final String PASS_ANSWER = "이대형";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordfinder);

        // xml 위젯 초기화
        etId = (EditText)findViewById(R.id.etId);
        btnIdSubmit = (Button)findViewById(R.id.btnIdSubmit);
        spinnerQuestion = (Spinner)findViewById(R.id.spinnerQuestion);
        etAnswer = (EditText)findViewById(R.id.etAnswer);
        btnQuestionSubmit = (Button)findViewById(R.id.btnQuestionSubmit);

        btnIdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(LOGIN_ID.equals(etId.getText().toString()))) {
                    // 아이디가 일치하지 않을 때
                    showDialog(DIALOG_ID_DIFFERENT);
                    return;
                }
                spinnerQuestion.setVisibility(View.VISIBLE);
                etAnswer.setVisibility(View.VISIBLE);
                btnQuestionSubmit.setVisibility(View.VISIBLE);
            }
        });

        btnQuestionSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(PASS_QUESTION.equals(spinnerQuestion.getSelectedItem()))) {
                    // 아이디가 일치하지 않을 때
                    showDialog(DIALOG_QUESTION_DIFFERENT);
                    return;
                }
                if (!(PASS_ANSWER.equals(etAnswer.getText().toString()))) {
                    // 아이디가 일치하지 않을 때
                    showDialog(DIALOG_QUESTION_DIFFERENT);
                    return;
                }

                showDialog(DIALOG_QUESTION_SUCCESS);

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

                dialog3.setTitle("비밀번호 설정");
                dialog3.setView(view);
                dialog3.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PasswordFinderActivity.this, "새 비밀번호 : " + etPassword.getText().toString(), Toast.LENGTH_SHORT).show();
                        finish();
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

} // end of class
