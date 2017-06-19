package com.kosta148.matjo.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

import com.kosta148.matjo.adapter.GroupListAdapter;
import com.kosta148.matjo.adapter.RestaListAdapter;
import com.kosta148.matjo.bean.GroupBean;
import com.kosta148.matjo.bean.ReviewBean;
import com.kosta148.matjo.data.DaumLocalBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Daehee on 2017-04-26.
 */

public class GroupListFragment extends Fragment {
    MainActivity mainActivity;
    Handler handler = new Handler();

    GridView gridView;
    List<GroupBean> groupList = new ArrayList<GroupBean>();
    GroupListAdapter groupListAdapter;
    int pageNo = 1;
    String searchText = "";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_group_list, container, false);
        mainActivity = (MainActivity) getActivity();

        gridView = (GridView) v.findViewById(R.id.gridView01);
        gridView.setOnItemClickListener(mOnItemClickListener);
        groupListAdapter = new GroupListAdapter(mainActivity.getApplicationContext(),
                                                    R.layout.item_group_list,
                                                    groupList);
        gridView.setAdapter(groupListAdapter);

        return v;
    } // end of onCreateView

    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mainActivity.showToast(groupList.get(position).getGroupName());
            Intent intent = new Intent();
            GroupBean gBean = groupList.get(position);
            intent.putExtra("gBean", gBean);
            startActivity(intent);
            callGroupDetail("7");
        }
    }; // end of ItemClickListener

    public void searchGroup(final String searchText, final int pageNo) {
        this.searchText = searchText;
        this.pageNo = pageNo;

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/group/selectGroupListProc.do"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MyLog", "response : " + response);
//                    mainActivity.showToast(response);
                // 응답을 처리한다.

                JsonParser parser = new JsonParser();
                JsonObject rootObj = (parser.parse(response)).getAsJsonObject();
                JsonArray gList = rootObj.getAsJsonArray("gList");
                String result = rootObj.get("result").getAsString();
                String resultMsg = rootObj.get("resultMsg").getAsString();

                Gson gson = new Gson();
                if (gList != null && (gList.size() > 0) ) {
                    for (int i = 0; i < gList.size(); i++) {
                        GroupBean gBean = gson.fromJson(gList.get(i), GroupBean.class);
                        groupList.add(gBean);
                    } // end of for
                    groupListAdapter.notifyDataSetChanged();
                }
                if ( (result != null) && ("fail".equals(result)) ) {
                    mainActivity.showToast(resultMsg);
                } // 결과 메시지 토스트
            } // end of onResponse()
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MyLog", "error : " + error);
                final VolleyError err = error;
                mainActivity.showToast(err.getMessage());

            } // end of onErrorResponse()
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("searchText", searchText);
                params.put("pageNo", pageNo + "");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    } // end of searchGroup()

    void callGroupDetail(String groupNoParam) {
        final String groupNo = groupNoParam;
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
//                    ArrayList<PereviewBean> pereviewBeanList = gson.fromJson(pereviewJSArray.toString(), new TypeToken<ArrayList<PereviewBean>>(){}.getType());
//                    reviewBeanList.get(i).setPereviewList(pereviewBeanList);
//                    if (pereviewBeanList != null) {
//                        Log.d("MyLog", "PEREVIEW 값들어감 : "+pereviewBeanList.size());
//                    }
                }

                Log.d("MyLog", "send Start");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 성공 시 값 보내주면서 모임 상세정보 액티비티 열어주기
                        Intent intent = new Intent(mainActivity.getApplicationContext(), GroupDetailActivity.class);
                        intent.putExtra("gBean", gBean);
                        intent.putParcelableArrayListExtra("reviewList", reviewBeanList);
                        mainActivity.startActivity(intent);
                    }
                });
                Log.e("MyLog", "send Finish");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MyLog", "error : " + error);
                final VolleyError err = error;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), "error : " + err, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("groupNo", groupNo);
                return params;
            }
        };
        // 재 호출 설정
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }

    private class CallGroupDetailTask extends AsyncTask<String, Void, String> {//Void는 null값을 가진 객체이다. updateBackground 메소를 쓰지 않기 때문에 null값의 객체를 파라미터로 넣어준것!

        private String groupNo;

        public CallGroupDetailTask(String groupNo) {
            this.groupNo = groupNo;
        }

        @Override
        protected String doInBackground(String... params) {//'...'은 배열의 역할을 한다. 무한정으로 값을 추가해 줄 수 있다

            return null;
        }

        // json 결과값 받아와서 처리해주는 부분
        @Override
        protected void onPostExecute(String s) {
        }
    }
}
