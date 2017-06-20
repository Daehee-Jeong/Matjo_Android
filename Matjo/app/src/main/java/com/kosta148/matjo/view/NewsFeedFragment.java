package com.kosta148.matjo.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
import com.kosta148.matjo.adapter.NewsFeedAdapter;
import com.kosta148.matjo.bean.NewsFeedBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewsFeedFragment extends Fragment {
    MainActivity mainActivity;
    ListView listView;
    List<NewsFeedBean> newsFeedList = new ArrayList<NewsFeedBean>();
    NewsFeedAdapter newsFeedAdapter;
    Handler handler = new Handler();

    SwipeRefreshLayout swipeRefreshLayout;

    int pageNo = 1;  // 초기값
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_news_feed, container, false);
        mainActivity = (MainActivity) getActivity();

        pageNo = 1;
        // 초기 리스트
        callNewsFeedList(pageNo+"");

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 작성하세요 ^0^

                handler.postDelayed(new Runnable() { // 러너블은 기냥쓴거임~
                    @Override
                    public void run() {
                       swipeRefreshLayout.setRefreshing(false); // 작업종료되면 얘를 꼭 호출하세요 ^0^
                    }
                }, 1000);
            }
       });
        /* 이 코드를 넣으면 한바퀴 돌때마다 이색깔들로 돌아가면서 바뀜~
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );*/

        listView = (ListView) v.findViewById(R.id.listView);
        listView.setOnScrollListener(mOnScrollListener);
        listView.setOnItemClickListener(mOnItemClickListener);
        newsFeedAdapter = new NewsFeedAdapter(getActivity().getApplicationContext(),
                R.layout.item_resta_list,
                newsFeedList);
        listView.setAdapter(newsFeedAdapter);

        return v;
    } // end of onCreateView

    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mainActivity.showToast(newsFeedList.get(position).getTypeMsg());
            NewsFeedBean nBean = newsFeedList.get(position);

            //TODO : type에 따라 다른 컨트롤러 호출

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
                callNewsFeedList(++pageNo+""); // 데이터 추가 요청
            }
        }
    }; // end of OnScrollListener

    void callNewsFeedList(String pageNoParam) {
        final String pageNo = pageNoParam;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/newsfeed/selectNewsFeedProc.do"
                , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("MyLog", "response : " + response);
                final String res = response;
                // JSON 1차 파싱
                JsonObject root = new JsonParser().parse(res).getAsJsonObject();
                // 뉴스피드 목록 Json
                JsonArray newsFeedListJSArray = root.get("newsFeedList").getAsJsonArray();
                Gson gson = new Gson();

                // 뉴스피드 목록
                final ArrayList<NewsFeedBean> newsFeedListTmp = gson.fromJson(newsFeedListJSArray.toString(), new TypeToken<ArrayList<NewsFeedBean>>() {}.getType());

                Log.d("MyLog", "send Start");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 성공 시 값 설정
                        for (int i = 0; i < newsFeedListTmp.size() ; i++) {
                            newsFeedList.add(newsFeedListTmp.get(i));
                        }
                        newsFeedAdapter.notifyDataSetChanged();
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
                params.put("pageNo",pageNo+"");
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

} // end of class

