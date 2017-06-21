package com.kosta148.matjo.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.kosta148.matjo.adapter.MemberListAdapter;
import com.kosta148.matjo.bean.GroupBean;
import com.kosta148.matjo.data.MemberBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daehee on 2017-05-14.
 */

public class GroupInfoFragment extends Fragment {

    Button btnShowMember;
    ListView listView;

    GroupBean groupBean;
    ArrayList<MemberBean> memberList = new ArrayList<>();

    Handler handler;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group_info, container, false);
        handler = new Handler();

        groupBean = (GroupBean) getArguments().get("gBean");

        TextView tvGroupLeader = (TextView) v.findViewById(R.id.groupLeader);
        TextView tvGroupInfo = (TextView) v.findViewById(R.id.groupInfo);
        TextView tvGroupSize = (TextView) v.findViewById(R.id.groupSize);

        btnShowMember = (Button) v.findViewById(R.id.btnShowMember);

        tvGroupLeader.setText("모임장 : "+groupBean.getGroupLeader());
        tvGroupInfo.setText("모임 소개 : "+groupBean.getGroupInfo());
        tvGroupSize.setText("모임원 수 : "+groupBean.getGroupSize()+"명");

        btnShowMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMemberVolley();
            }
        });

        return v;
    } // end of onCreateView

    // 구성원 보기 서버연동
    public void showMemberVolley() {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/group/selectGroupMemberProc.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                memberList = new ArrayList<>();

                JsonParser parser = new JsonParser();
                JsonObject rootObj = (parser.parse(response)).getAsJsonObject();

                Gson gson = new Gson();
                JsonArray list = rootObj.getAsJsonArray("memberList");
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        MemberBean mBean = gson.fromJson(list.get(i), MemberBean.class);
                        memberList.add(mBean);
                    }
                }

                String resultData = rootObj.get("result").getAsString();
                if ("success".equals(resultData)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    LayoutInflater lif = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dView = lif.inflate(R.layout.dialog_showmember, null);

                    MemberListAdapter adapter = new MemberListAdapter(getContext(), memberList);
                    listView = (ListView) dView.findViewById(R.id.listView);
                    listView.setAdapter(adapter);

                    dialog.setTitle("모임 구성원보기");
                    dialog.setView(dView);
                    dialog.setPositiveButton("확인", null);
                    dialog.show();
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
                        Toast.makeText(getContext(), "error : " + err, Toast.LENGTH_SHORT).show();
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
