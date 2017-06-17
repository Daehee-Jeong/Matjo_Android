package com.kosta148.matjo.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
    TextView textView;
    ListView listView;
    List<DaumLocalBean> restaList = new ArrayList<DaumLocalBean>();
    RestaListAdapter restaListAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_resta_list, container, false);
        mainActivity = (MainActivity) getActivity();

        textView = (TextView) v.findViewById(R.id.textView);

        listView = (ListView) v.findViewById(R.id.listView);
        restaListAdapter = new RestaListAdapter(getActivity().getApplicationContext(),
                                                    R.layout.item_resta_list,
                                                    restaList);
        listView.setAdapter(restaListAdapter);

        v.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity.getApplicationContext(), RestaDetailActivity.class);
                mainActivity.startActivity(intent);
            }
        });

        return v;
    } // end of onCreateView


    public void searchResta(String searchText) {
        textView.setText(searchText);
        new SearchRestaTask(searchText, 1).execute();
    }

    class SearchRestaTask extends AsyncTask<String, Void, String> {
        String searchText;
        int pageNo;

        SearchRestaTask(String searchText, int pageNo) {
            this.searchText = searchText;
            this.pageNo = pageNo;
        }

        /**
         * doInBackground 실행되기 이전에 동작한다.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * 본 작업을 쓰레드로 처리해준다. * @param params * @return
         */
        @Override
        protected String doInBackground(String... params) {

            String result = "false";

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

                    Gson gson = new Gson();
                    for (int i = 0; i < list.size(); i++) {
                        DaumLocalBean dlBean = gson.fromJson(list.get(i), DaumLocalBean.class);
                        restaList.add(dlBean);
                    } // end of for
                    restaListAdapter.notifyDataSetChanged();

                    mainActivity.showToast(restaList.size()+"");

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
                    params.put("pageNo", pageNo+"");
                    return params;
                }
            };
            requestQueue.add(stringRequest);

            return null;
        } // end of doInBackground

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
        }
    } // end of NetworkTask
} // end of class
