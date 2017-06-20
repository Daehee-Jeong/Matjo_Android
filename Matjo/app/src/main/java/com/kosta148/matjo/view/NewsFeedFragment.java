package com.kosta148.matjo.view;

import android.content.Intent;
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
import com.kosta148.matjo.bean.GroupBean;
import com.kosta148.matjo.bean.NewsFeedBean;
import com.kosta148.matjo.bean.ReviewBean;
import com.kosta148.matjo.data.DaumLocalBean;

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
    RequestQueue requestQueue;

    int pageNo = 1;  // 초기값
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_news_feed, container, false);
        mainActivity = (MainActivity) getActivity();

        pageNo = 1;
        // 초기 리스트
        callNewsFeedList(pageNo+"");

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
            NewsFeedBean nBean = newsFeedList.get(position);
            mainActivity.showToast(nBean.getTypeMsg()+"눌렸습니다 ["+nBean.getGroupNo()+"]");
            //TODO : type에 따라 다른 컨트롤러 호출
            if (nBean.getType().equals("1")) { // 모임
                // 모임
                callGroupDetail(nBean.getGroupNo());

            } else if (nBean.getType().equals("2")) { // 리뷰
                // 리뷰
                callRestaDetail(nBean.getGroupNo());

            }

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

    /**
     * 뉴스피드 목록 조회
     * @param pageNoParam
     */
    void callNewsFeedList(String pageNoParam) {
        final String pageNo = pageNoParam;
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/newsfeed/selectNewsFeedProc.do"
                , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("MyLog", "NewsFeed response : " + response);
                final String res = response;
                // JSON 1차 파싱
                JsonObject root = new JsonParser().parse(res).getAsJsonObject();
                // 뉴스피드 목록 Json
                JsonArray newsFeedListJSArray = root.get("newsFeedList").getAsJsonArray();
                Gson gson = new Gson();

                // 뉴스피드 목록
                final ArrayList<NewsFeedBean> newsFeedListTmp = gson.fromJson(newsFeedListJSArray.toString(), new TypeToken<ArrayList<NewsFeedBean>>() {}.getType());

                Log.d("MyLog", "send NewsFeed Start");
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
                Log.e("MyLog", "send NewsFeed Finish");
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

    /**
     * 모임 상세 페이지로 이동
     * @param groupNoParam
     */
    void callGroupDetail(String groupNoParam) {
        final String groupNo = groupNoParam;
         requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/group/selectGroupDetailProc.do"
                , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("MyLog", "groupDetail response : " + response);
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

                Log.d("MyLog", "send groupDetail Start");
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
                Log.e("MyLog", "send groupDetail Finish");
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

    /**
     * 뉴스피드 목록 조회
     * @param restaIdParam
     */
    void callRestaDetail(String restaIdParam) {
        final String restaId = restaIdParam;
         requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/resta/selectRestaProcDB.do"
                , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("MyLog", "restaDetail response : " + response);
                final String res = response;
                // JSON 1차 파싱
                JsonObject root = new JsonParser().parse(res).getAsJsonObject();
                // 뉴스피드 목록 Json
                JsonObject dlBeanJSObject = root.get("restaBean").getAsJsonObject();
                Gson gson = new Gson();

                // 뉴스피드 목록
                final DaumLocalBean dlBean = gson.fromJson(dlBeanJSObject.toString(), DaumLocalBean.class);

                Log.d("MyLog", "send restaDetail Start");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 성공 시 전송
                        Intent intent = new Intent(getActivity().getApplicationContext(), RestaDetailActivity.class);
                        intent.putExtra("dlBean", dlBean);
                        mainActivity.startActivity(intent);
                    }
                });
                Log.e("MyLog", "send restaDetail Finish");
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
                params.put("restaId",restaId);
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

