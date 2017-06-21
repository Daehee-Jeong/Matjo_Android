package com.kosta148.matjo.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosta148.matjo.R;
import com.kosta148.matjo.adapter.PagerInGroupDetailAdapter;
import com.kosta148.matjo.adapter.PagerInToolBarAdapter;
import com.kosta148.matjo.bean.GroupBean;
import com.kosta148.matjo.bean.ReviewBean;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GroupDetailActivity extends AppCompatActivity {
    // 본 액티비티의 호출부에서 인텐트를 통해 업소명, 사진경로 배열등을 보내주는 식으로 작업하면 된다.
    // 다량의 데이터 (모임 사진들)
    int resArr[] = {
            R.drawable.img01, R.drawable.img02, R.drawable.img03, R.drawable.img04, R.drawable.img05, R.drawable.img06,
    };
    // 변경시킬 타이틀 명 (모임 이름)
    String name = "다먹어버리조";
    TextView tvScrollingIndex;
    EditText etContent;
    FloatingActionButton fab;

    Toolbar toolbar;
    ViewPager viewPagerInToolbar;
    PagerInToolBarAdapter pagerInToolBarAdapter;
    ViewPager viewPagerMain;
    PagerInGroupDetailAdapter pagerInGroupDetailAdapter;
    TabLayout tabLayout;
    Handler handler = new Handler();
    ActionBar actionBar;

    Bitmap bitmap;
    List<String> imgList = new ArrayList<String>();

    // SharedPreferences 선언
    private SharedPreferences sharedPreferences;

    // SharedPreferences 키 상수
    private static final String SHAREDPREFERENCES_MEMBER_NO = "memberNo";

    GroupBean groupBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        sharedPreferences = getSharedPreferences("LoginSetting.dat", MODE_PRIVATE);

        // 값 받기
        Intent intent = getIntent();
        groupBean = (GroupBean)intent.getSerializableExtra("gBean");
        // TODO reviewBean도 받아오자
        ArrayList<ReviewBean> reviewList = intent.getParcelableArrayListExtra("reviewList");

        imgList.add(groupBean.getGroupImg());

        Log.d("MyLog", "값 받음 ; "+groupBean.getGroupName());
        if (reviewList != null && reviewList.size() > 0) {
            Log.d("MyLog", "review pereview도 잘 들어왔나여 : " + reviewList.get(0).getPereviewJSArray());
        } else {
            Log.d("MyLog", "review가 비어있습니당");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(groupBean.getGroupName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actionBar = getSupportActionBar();

        // 기본 UI 컴포넌트 생성
        tvScrollingIndex = (TextView) findViewById(R.id.tvScrollingIndex);
        tvScrollingIndex.setText("1 / " + (resArr.length));

        checkMemberVolley();



        /** 툴바 내 뷰페이저 구현 **/
        // 1. 다량의 데이터 : 멤버변수 resArr
        // 2. 뷰페이저 객체 생성
        viewPagerInToolbar = (ViewPager) findViewById(R.id.viewPagerInToolbar);
        // 3. 페이저어댑터 생성 및 뷰페이저에 연결
        pagerInToolBarAdapter = new PagerInToolBarAdapter(getSupportFragmentManager(), imgList);
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
        pagerInGroupDetailAdapter = new PagerInGroupDetailAdapter(getSupportFragmentManager(), groupBean, reviewList);
        viewPagerMain.setAdapter(pagerInGroupDetailAdapter);
        // 3. 탭 인디케이터 생성 및 페이저에 연결
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabStrip);
        tabStrip.setViewPager(viewPagerMain);

        // Web API 호출부분
        // TODO groupNo 예시로 7 설정해놓음 - 유동적으로 받아올 수 있게 변경하기
//        new CallGroupDetailTask("7").execute();

    } // end of onCreate

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class CallImageByUrlTask extends AsyncTask<String, Void, String> {

        private  String strUrl;
        public CallImageByUrlTask(String strUrl){
            this.strUrl = strUrl;
        }
        @Override
        protected String doInBackground(String... params) {//'...'은 배열의 역할을 한다. 무한정으로 값을 추가해 줄 수 있다
            try {
                URL url = new URL(strUrl);

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);

            } catch (IOException ex) {

            }

            return null;
        }

        // json 결과값 받아와서 처리해주는 부분
        @Override
        protected void onPostExecute(String s) {

        }

    }

    // 모임가입 신청 서버연동
    public void insertGroupVolley() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/android/insertGroupMemberProc.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonParser parser = new JsonParser();
                JsonObject rootObj = (parser.parse(response)).getAsJsonObject();

                Gson gson = new Gson();
                String resultData = rootObj.get("result").getAsString();
                String resultMsg = rootObj.get("resultMsg").getAsString();

                if ("success".equals(resultData)) {
                    Toast.makeText(getApplicationContext(), resultMsg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), resultMsg, Toast.LENGTH_SHORT).show();
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
                params.put("memberNo", sharedPreferences.getString(SHAREDPREFERENCES_MEMBER_NO, ""));
                params.put("groupNo", groupBean.getGroupNo());
                params.put("applyContent", etContent.getText().toString());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    // 소속회원 체크 서버연동
    public void checkMemberVolley() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/android/checkMemberProc.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonParser parser = new JsonParser();
                JsonObject rootObj = (parser.parse(response)).getAsJsonObject();

                Gson gson = new Gson();

                String resultData = rootObj.get("result").getAsString();
                if ("success".equals(resultData)) {

                } else {
                    fab = (FloatingActionButton) findViewById(R.id.fabInsertGroup);
                    fab.setVisibility(View.VISIBLE);

                    // 플로팅 액션 버튼 (가입기능)
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // 가입 기능
                            AlertDialog.Builder builder = new AlertDialog.Builder(GroupDetailActivity.this);
                            LayoutInflater lif = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View dView = lif.inflate(R.layout.dialog_insertgroup, null);

                            etContent = (EditText) dView.findViewById(R.id.etContent);

                            builder.setTitle("모임가입 신청");
                            builder.setView(dView);
                            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    insertGroupVolley();
                                }
                            });
                            builder.setNegativeButton("취소", null);

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });

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
                params.put("memberNo", sharedPreferences.getString(SHAREDPREFERENCES_MEMBER_NO, ""));
                params.put("groupNo", groupBean.getGroupNo());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    
} // end of class
