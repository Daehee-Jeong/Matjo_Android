package com.kosta148.matjo.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosta148.matjo.R;
import com.kosta148.matjo.adapter.PagerInToolBarAdapter;
import com.kosta148.matjo.adapter.PagerInRestaDetailAdapter;
import com.kosta148.matjo.data.DaumLocalBean;
import com.kosta148.matjo.data.MemberBean;

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

    // SharedPreferences 선언
    private SharedPreferences sharedPreferences;

    // SharedPreferences 키 상수
    private static final String SHAREDPREFERENCES_MEMBER_NO = "memberNo";

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

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("LoginSetting.dat", MODE_PRIVATE);

        // 받아온 인텐트 꺼냄
        Intent intent = getIntent();
        dlBean = (DaumLocalBean) intent.getSerializableExtra("dlBean");

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
        pagerInRestaDetailAdapter = new PagerInRestaDetailAdapter(getSupportFragmentManager());
        viewPagerMain.setAdapter(pagerInRestaDetailAdapter);
        // 3. 탭 인디케이터 생성 및 페이저에 연결
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabStrip);
        tabStrip.setViewPager(viewPagerMain);

    } // end of onCreate

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // 모임장 조회 서버연동
    public void checkLeaderVolley() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/android/checkLeaderProc.do", new Response.Listener<String>() {
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
                    btnInsertReview.setVisibility(View.VISIBLE);
                    btnInsertReview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
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
} // end of class
