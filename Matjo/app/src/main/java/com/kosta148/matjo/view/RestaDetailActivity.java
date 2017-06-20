package com.kosta148.matjo.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosta148.matjo.R;
import com.kosta148.matjo.adapter.PagerInToolBarAdapter;
import com.kosta148.matjo.adapter.PagerInRestaDetailAdapter;
import com.kosta148.matjo.bean.GroupBean;
import com.kosta148.matjo.data.DaumLocalBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestaDetailActivity extends AppCompatActivity {
    // 본 액티비티의 호출부에서 인텐트를 통해 업소명, 사진경로 배열등을 보내주는 식으로 작업하면 된다.
    // 다량의 데이터 (업소 사진들)
    int resArr[] = {
            R.drawable.img01, R.drawable.img02, R.drawable.img03, R.drawable.img04, R.drawable.img05, R.drawable.img06,
    };
    // 변경시킬 타이틀 명 (업소 이름)
    String name = "가산 철파니야";
    TextView tvScrollingIndex;

    Toolbar toolbar;
    ViewPager viewPagerInToolbar;
    PagerInToolBarAdapter pagerInToolBarAdapter;
    ViewPager viewPagerMain;
    PagerInRestaDetailAdapter pagerInRestaDetailAdapter;
    TabLayout tabLayout;
    DaumLocalBean dlBean;

    // 리뷰등록 버튼
    Button btnInsertReview;

    // 모임장의 모임리스트
    ArrayList<GroupBean> groupList;

    // 다이얼로그 상수
    private static final int DIALOG_SHOW_REVIEW = 1;
    private static final int DIALOG_INSERT_REVIEW = 2;

    // SharedPreferences 선언
    private SharedPreferences sharedPreferences;

    // selectedGroupNo
    private String seletedGroupNo;

    // SharedPreferences 키 상수
    private static final String SHAREDPREFERENCES_MEMBER_NO = "memberNo";
    // 인텐트에서 받아오는 값들
    String restaId = "";
    String restaTitle = "";
    String restaCate = "";
    String restaAddr = "";
    String restaImgUrl = "";
    String restaLat = "";
    String restaLng = "";
    String restaPhone = "";
    String restaUrl = "";

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resta_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 리뷰 등록버튼
        btnInsertReview = (Button)findViewById(R.id.btnInsertReview);

        btnInsertReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_SHOW_REVIEW);
            }
        });
        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("LoginSetting.dat", MODE_PRIVATE);

        // 받아온 인텐트 꺼냄
        Intent intent = getIntent();
        dlBean = (DaumLocalBean) intent.getSerializableExtra("dlBean");
        restaId = dlBean.getRestaId();
        restaTitle = dlBean.getRestaTitle();
        restaCate = dlBean.getRestaCate();
        restaAddr = dlBean.getRestaAddr();
        restaImgUrl = dlBean.getRestaImgUrl();
        restaLat = dlBean.getRestaLat();
        restaLng = dlBean.getRestaLng();
        restaPhone = dlBean.getRestaPhone();
        restaUrl = dlBean.getRestaUrl();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(restaTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 기본 UI 컴포넌트 생성
        tvScrollingIndex = (TextView) findViewById(R.id.tvScrollingIndex);
        tvScrollingIndex.setText("1 / " + (resArr.length));

        // 플로팅 액션 버튼 (보류)
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                /* 전화하기 기능 구현 */
            }
        });

        /** 툴바 내 뷰페이저 구현 **/
        // 1. 다량의 데이터 : 멤버변수 resArr
        // 2. 뷰페이저 객체 생성
        viewPagerInToolbar = (ViewPager) findViewById(R.id.viewPagerInToolbar);
        // 3. 페이저어댑터 생성 및 뷰페이저에 연결
        pagerInToolBarAdapter = new PagerInToolBarAdapter(getSupportFragmentManager(), resArr);
        viewPagerInToolbar.setAdapter(pagerInToolBarAdapter);
        // 4. 리스너 등록
        viewPagerInToolbar.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                tvScrollingIndex.setText((position + 1) + " / " + (resArr.length));
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        /** 메인 화면 내 뷰페이저 구현 **/
        // 1. 뷰페이저 생성
        viewPagerMain = (ViewPager) findViewById(R.id.viewPagerMain);
        // 2. 어댑터 생성 및 페이저에 등록
        pagerInRestaDetailAdapter = new PagerInRestaDetailAdapter(getSupportFragmentManager(), dlBean);
        viewPagerMain.setAdapter(pagerInRestaDetailAdapter);
        // 3. 탭 인디케이터 생성 및 페이저에 연결
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabStrip);
        tabStrip.setViewPager(viewPagerMain);

        // 모임장에 따라 리뷰버튼 체크
        checkLeaderVolley();
    } // end of onCreate

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    // 다이얼로그 onCreate
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_SHOW_REVIEW:
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(this);
                LayoutInflater lif = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = lif.inflate(R.layout.dialog_insertreview, null);

                final Spinner spinnerGroup = (Spinner) view.findViewById(R.id.spinnerGroup);

                ArrayList<String> groupArrayList = new ArrayList<String>();
                for (int i = 0; i < groupList.size(); i++) {
                    groupArrayList.add(groupList.get(i).getGroupName());
                }

                ArrayAdapter<String> adapterGroup = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, groupArrayList);
                //스피너 속성
                spinnerGroup.setAdapter(adapterGroup);

                spinnerGroup.setSelection(0);
                spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        spinnerGroup.setSelection(position);
                        seletedGroupNo = groupList.get(position).getGroupNo();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                dialog1.setTitle("리뷰 등록");
                dialog1.setView(view);
                dialog1.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        insertRestaVolley();
                        insertReviewVolley();
                        showDialog(DIALOG_INSERT_REVIEW);
                    }
                });
                dialog1.setNegativeButton("아니요", null);
                return dialog1.create();
            case DIALOG_INSERT_REVIEW:
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(this);
                dialog2.setTitle("리뷰 등록 완료");
                dialog2.setMessage("리뷰가 정상적으로 등록되었습니다.\n모임상세 화면에서 확인해주세요");
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
            case DIALOG_SHOW_REVIEW:
                break;
        }
    } // end of onPrepareDialog

    // 모임장 조회 서버연동
    public void checkLeaderVolley() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/android/checkLeaderProc.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                groupList = new ArrayList<>();

                JsonParser parser = new JsonParser();
                JsonObject rootObj = (parser.parse(response)).getAsJsonObject();

                Gson gson = new Gson();
                JsonArray list = rootObj.getAsJsonArray("list");
                for (int i = 0; i < list.size(); i++) {
                    GroupBean gBean = gson.fromJson(list.get(i), GroupBean.class);
                    groupList.add(gBean);
                }

                String resultData = rootObj.get("result").getAsString();
                if ("success".equals(resultData)) {
                    btnInsertReview.setVisibility(View.VISIBLE);
                } else {
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
                params.put("groupLeader", sharedPreferences.getString(SHAREDPREFERENCES_MEMBER_NO, ""));

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    // 리뷰 등록 서버연동
    public void insertReviewVolley() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/review/insertReviewProc.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                groupList = new ArrayList<>();

                JsonParser parser = new JsonParser();
                JsonObject rootObj = (parser.parse(response)).getAsJsonObject();

                String resultData = rootObj.get("result").getAsString();
                if ("success".equals(resultData)) {
                    showDialog(DIALOG_INSERT_REVIEW);
                } else {

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
                params.put("reviewGroupNo", seletedGroupNo);
                params.put("reviewRestaNo", restaId);
                params.put("reviewRestaName", restaTitle);
                params.put("reviewRestaCate", restaCate);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    // 맛집 등록 서버연동
    public void insertRestaVolley() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/resta/insertRestaProc.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                groupList = new ArrayList<>();

                JsonParser parser = new JsonParser();
                JsonObject rootObj = (parser.parse(response)).getAsJsonObject();

                Gson gson = new Gson();
                String resultData = rootObj.get("result").getAsString();
                if ("success".equals(resultData)) {

                } else {

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
                params.put("restaId", restaId);
                params.put("restaTitle", restaTitle);
                params.put("restaCate", restaCate);
                params.put("restaAddr", restaAddr);
                params.put("restaImgUrl", restaImgUrl);
                params.put("restaLat", restaLat);
                params.put("restaLng", restaLng);
                params.put("restaPhone", restaPhone);
                params.put("restaUrl", restaUrl);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
} // end of class
