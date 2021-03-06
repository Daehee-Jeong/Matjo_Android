package com.kosta148.matjo.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.kosta148.matjo.R;
import com.kosta148.matjo.adapter.ExpandableListAdapter;
import com.kosta148.matjo.bean.GroupBean;
import com.kosta148.matjo.bean.PereviewBean;
import com.kosta148.matjo.bean.ReviewBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Daehee on 2017-05-14.
 */

public class GroupReviewFragment extends Fragment {
    private RecyclerView recyclerview;
    GroupDetailActivity groupDetailActivity;

    ArrayList<ReviewBean> reviewList;
    // SharedPreferences 선언
    private SharedPreferences sharedPreferences;

    // SharedPreferences 키 상수
    private static final String SHAREDPREFERENCES_MEMBER_NO = "memberNo";

    private String selectedReviewNo;
    private float selectedRating;

    EditText etContent;
    RatingBar ratingBar;

    ExpandableListAdapter adapter;

    List<ExpandableListAdapter.Item> data;
    GroupBean groupBean;

    Handler handler = new Handler();
    ExpandableListAdapter.ItemClick myItemClick = new ExpandableListAdapter.ItemClick() {
        public void onClick(View view, int position) {
            selectedReviewNo = reviewList.get(position).getReviewNo();

            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            LayoutInflater lif = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dView = lif.inflate(R.layout.dialog_insertpereview, null);

            etContent = (EditText) dView.findViewById(R.id.etContent);
            ratingBar = (RatingBar) dView.findViewById(R.id.ratingBar);

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    selectedRating = rating;
                }
            });
            dialog.setTitle("개인 리뷰작성");
            dialog.setView(dView);
            dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    insertPereviewVolley();
                }
            });
            dialog.setNegativeButton("취소", null);
            dialog.show();

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_restaurant_review, container, false);
        checkMemberVolley();
        reviewList = getArguments().getParcelableArrayList("reviewList");
        groupBean = (GroupBean) getArguments().get("gBean");

        sharedPreferences = getContext().getSharedPreferences("LoginSetting.dat", MODE_PRIVATE);
        Gson gson = new Gson();
        for (int i = 0; i < reviewList.size(); i++) {
            ArrayList<PereviewBean> pereviewBeanList = gson.fromJson(reviewList.get(i).getPereviewJSArray(), new TypeToken<ArrayList<PereviewBean>>() {}.getType());
            reviewList.get(i).setPereviewList(pereviewBeanList);
        }

        groupDetailActivity = (GroupDetailActivity) getActivity();

        recyclerview = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        dataSet();

        return v;
    } // end of onCreateView

    public void dataSet(){
        data = new ArrayList<>();

        for (int i = 0; i < reviewList.size(); i++) {
            // 업소 이미지 받아옴 : reviewRestaImg
            double ratingAvg = 0.0;
            if (!Double.isNaN(Double.parseDouble(reviewList.get(i).getAvgRating()))) ratingAvg = Double.parseDouble(reviewList.get(i).getAvgRating());
            ExpandableListAdapter.Item placeTmp = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, reviewList.get(i).getReviewRestaName(), reviewList.get(i).getReviewRestaImg(), ratingAvg);
            placeTmp.invisibleChildren = new ArrayList<>();
            List<PereviewBean> perTmpList = reviewList.get(i).getPereviewList();
            int perSize = perTmpList.size();
            for (int j = 0; j < perSize; j++) {
                PereviewBean perTmp = perTmpList.get(j);
                double rating = 0.0;
                if (!Double.isNaN(Double.parseDouble(perTmp.getPereviewRating()))) rating = Double.parseDouble(perTmp.getPereviewRating());
                placeTmp.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, perTmp.getPereviewMemName(), perTmp.getPereviewContent(), perTmp.getPereviewMemImg(), perTmp.getPereviewImgUrl(), rating));
            }
            data.add(placeTmp);
        }
    }

    // 소속회원 체크 서버연동
    public void checkMemberVolley() {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/android/checkMemberProc.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonParser parser = new JsonParser();
                JsonObject rootObj = (parser.parse(response)).getAsJsonObject();

                Gson gson = new Gson();

                String resultData = rootObj.get("result").getAsString();
                if ("success".equals(resultData)) {
                    adapter = new ExpandableListAdapter(data, groupDetailActivity.getApplicationContext(), true);
                    recyclerview.setAdapter(adapter);
                    adapter.setItemClick(myItemClick);
                } else {
                    adapter = new ExpandableListAdapter(data, groupDetailActivity.getApplicationContext(), false);
                    recyclerview.setAdapter(adapter);
                    adapter.setItemClick(myItemClick);

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
                        Log.d("MyLog", "error : " + err);
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

    public void sendPush(final GroupBean groupBean) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/push/sendPushToGroup.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonParser parser = new JsonParser();
                JsonObject rootObj = (parser.parse(response)).getAsJsonObject();

                Gson gson = new Gson();

                String resultData = rootObj.get("result").getAsString();
                if ("success".equals(resultData)) {
                    Log.d("MyLog", "푸시전송 성공");
                } else {
                    Log.d("MyLog", "푸시전송 실패");
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
                        Log.d("MyLog", "error : " + err);
                    }
                });
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("groupNo", groupBean.getGroupNo());
                params.put("pushType", "3");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    // 개인리뷰 등록 서버연동
    public void insertPereviewVolley() {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/review/insertPereviewProc.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonParser parser = new JsonParser();
                JsonObject rootObj = (parser.parse(response)).getAsJsonObject();

                Gson gson = new Gson();
                String resultData = rootObj.get("result").getAsString();

                if ("ok".equals(resultData)) {
                    callGroupDetail();
                    sendPush(groupBean);
                    Log.d("MyLog", "개인리뷰를 작성하였습니다.");

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
                        Log.e("MyLog", "error : " + err);
                    }
                });
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("pereviewMemId", sharedPreferences.getString(SHAREDPREFERENCES_MEMBER_NO, ""));
                params.put("pereviewReviewNo", selectedReviewNo);
                params.put("pereviewContent", etContent.getText().toString());
                params.put("pereviewRating", selectedRating+"");

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    // list 갱신
    void callGroupDetail() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/group/selectGroupDetailProc.do"
                , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("MyLog", "response : " + response);
                final String res = response;
                // JSON 1차 파싱
                JsonObject root = new JsonParser().parse(res).getAsJsonObject();
                // 모임 정보
                JsonObject groupJSObject = root.get("gBean").getAsJsonObject();
                // 리뷰 목록
                JsonArray reviewListJSArray = root.get("reviewList").getAsJsonArray();
                Gson gson = new Gson();

                // 모임 정보
                final GroupBean gBean = gson.fromJson(groupJSObject, GroupBean.class);
                // 모임 리뷰 목록
                final ArrayList<ReviewBean> reviewBeanList = gson.fromJson(reviewListJSArray.toString(), new TypeToken<ArrayList<ReviewBean>>() {
                }.getType());
                // 개인 리뷰 목록 - 모임 리뷰에 추가
                for (int i = 0; i < reviewListJSArray.size(); i++) {
                    Log.d("MyLog", "PERREVIEW 추가중~ " + i);
                    JsonObject reviewJSObject = reviewListJSArray.get(i).getAsJsonObject();
                    JsonArray pereviewJSArray = reviewJSObject.get("pereviewList").getAsJsonArray();
                    reviewBeanList.get(i).setPereviewJSArray(pereviewJSArray.toString());
                }

                groupBean = gBean;
                reviewList = reviewBeanList;
                dataSet();

                checkMemberVolley();
                Log.d("MyLog", "수행완료");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MyLog", "error : " + error);
                final VolleyError err = error;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("MyLog", "error : " + err);
                    }
                });
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("groupNo", groupBean.getGroupNo());
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

} // end of class
