package com.kosta148.matjo.view;

import android.content.Intent;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosta148.matjo.R;
import com.kosta148.matjo.adapter.RestaListAdapter;
import com.kosta148.matjo.data.DaumLocalBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Daehee on 2017-04-26.
 */

public class RestaListFragment extends Fragment {
    MainActivity mainActivity;
    ListView listView;
    List<DaumLocalBean> restaList = new ArrayList<DaumLocalBean>();
    RestaListAdapter restaListAdapter;
    int pageNo = 1;
    String searchText = "";
    TextView tvLocation;
    boolean isUseLoc = false;

    String location = "";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_resta_list, container, false);
        mainActivity = (MainActivity) getActivity();

        LayoutInflater li = mainActivity.getLayoutInflater();
        RelativeLayout headerView = (RelativeLayout)li.inflate(R.layout.header_resta_list, null); // 레이아웃에 맞게!
        tvLocation = (TextView) headerView.findViewById(R.id.tvLocation);
        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isUseLoc) {
                }
            }
        });

        listView = (ListView) v.findViewById(R.id.listView);
        listView.addHeaderView(headerView, null, false);
        listView.setOnScrollListener(mOnScrollListener);
        listView.setOnItemClickListener(mOnItemClickListener);
        restaListAdapter = new RestaListAdapter(getActivity().getApplicationContext(),
                R.layout.item_resta_list,
                restaList);
        listView.setAdapter(restaListAdapter);

        return v;
    } // end of onCreateView

    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (view.getId() == R.id.headerLayout) { // 헤더뷰 클릭시 무효화
                return;
            }
            mainActivity.showToast(restaList.get(position-1).getRestaTitle());
            Intent intent = new Intent(getActivity().getApplicationContext(), RestaDetailActivity.class);
            DaumLocalBean dlBean = restaList.get(position-1); // headerView 의 추가로 1을 빼주어야 한다.
            intent.putExtra("dlBean", dlBean);
            startActivity(intent);
        }
    }; // end of ItemClickListener


    boolean lastItemVisibleFlag = false;        //화면에 리스트의 마지막 아이템이 보여지는지 체크
    AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
        boolean lastitemVisibleFlag = false; // 마지막이냐 아니냐의 플래그
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem) + 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
            lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
        }
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //OnScrollListener.SCROLL_STATE_IDLE은 스크롤이 이동하다가 멈추었을때 발생되는 스크롤 상태
            //즉 스크롤이 바닦에 닿아 멈춘 상태에 처리를 하겠다는 뜻
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) { // '리스트의 마지막에' + '멈췄다'
                searchResta(searchText, ++pageNo, location); // 데이터 추가 요청
            }
        }
    }; // end of OnScrollListener

    public void searchResta(final String searchText, final int pageNo, final String location) {
        this.searchText = searchText;
        this.pageNo = pageNo;
        this.location = location;

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/resta/selectRestaListProc.do"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MyLog", "response : " + response);
//                    mainActivity.showToast(response);
                // 응답을 처리한다.

                JsonParser parser = new JsonParser();
                JsonObject rootObj = (parser.parse(response)).getAsJsonObject();
                JsonArray list = rootObj.getAsJsonArray("list");
                String result = rootObj.get("result").getAsString();
                String resultMsg = rootObj.get("resultMsg").getAsString();

                Gson gson = new Gson();
                if (list != null && (list.size() > 0) ) {
                    for (int i = 0; i < list.size(); i++) {
                        DaumLocalBean dlBean = gson.fromJson(list.get(i), DaumLocalBean.class);
                        restaList.add(dlBean);
                    } // end of for
                    restaListAdapter.notifyDataSetChanged();
                }
                if ( (result != null) && ("fail".equals(result)) ) {
                    if (pageNo >= 3) {
                        mainActivity.showToast("데이터를 모두 불러왔습니다.");
                    } else {
                        mainActivity.showToast(resultMsg);
                    }
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
                if (!"0.0f,0.0f".equals(location) && !"0.0,0.0".equals(location) && !"".equals(location)) {
                    params.put("location", location);
                }
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
} // end of class
