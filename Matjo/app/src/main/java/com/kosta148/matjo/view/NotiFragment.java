package com.kosta148.matjo.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import com.kosta148.matjo.R;
import com.kosta148.matjo.adapter.NotiListAdapter;
import com.kosta148.matjo.bean.NewsFeedBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Daehee on 2017-04-26.
 */

public class NotiFragment extends Fragment {
    Handler handler = new Handler();
    MainActivity mainActivity;

    ListView listView;
    NotiListAdapter notiListAdapter;
    List<NewsFeedBean> newsFeedList = new ArrayList<NewsFeedBean>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_noti, container, false);

        mainActivity = (MainActivity) getActivity();

        listView = (ListView)v.findViewById(R.id.listViewNoti);
        notiListAdapter = new NotiListAdapter(mainActivity.getApplicationContext(),
                                                R.layout.item_noti_list,
                                                newsFeedList);
        listView.setAdapter(notiListAdapter);

        callMyNoti();
        return v;
    } // end of onCreateView

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void callMyNoti() {
        SharedPreferences sharedPreferences  = getActivity().getSharedPreferences("LoginSetting.dat", Activity.MODE_PRIVATE);
        final String memberNo = sharedPreferences.getString("memberNo", "");

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/newsfeed/selectMyNotiProc.do"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MyLog", "response : " + response);
//                mainActivity.showToast(response);
                // 응답을 처리한다.

                JsonParser parser = new JsonParser();
                JsonObject rootObj = (parser.parse(response)).getAsJsonObject();
                String result = rootObj.get("result").getAsString();
                String resultMsg = rootObj.get("resultMsg").getAsString();

                Gson gson = new Gson();

                // TODO 파싱
                if ("success".equals(result)) {
                    JsonArray newsFeedListJSArray = rootObj.get("newsFeedList").getAsJsonArray();

                    if (newsFeedListJSArray != null && newsFeedListJSArray.size() > 0) {
                        for (int i = 0; i < newsFeedListJSArray.size(); i++) {
                            NewsFeedBean newsFeedBean = gson.fromJson(newsFeedListJSArray.get(i), NewsFeedBean.class);
                            newsFeedList.add(newsFeedBean);
                        }
                        notiListAdapter.notifyDataSetChanged();
                    }
                } else {
                    // 실패
                }

                if ( (result == null) || ("fail".equals(result)) ) {
                    mainActivity.showToast("null 또는 fail: " + resultMsg);
                } // 결과 메시지 토스트
            } // end of onResponse()
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MyLog", "error : " + error);
                final VolleyError err = error;
//                showToast("결과: " + err.getMessage());
                Log.e("TEST", "에러메시지 : " + err.getMessage());

            } // end of onErrorResponse()
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("memberNo", memberNo);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
} // end of class
