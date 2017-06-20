package com.kosta148.matjo.service;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosta148.matjo.view.MainActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daehee on 2017-05-24.
 * 등록 토큰 생성, 순환, 업데이트를 처리하기 위해 FirebaseInstanceIdService를 확장하는 서비스를 추가합니다.
 * 특정 기기로 전송하거나 기기 그룹을 만드는 경우에 필요합니다.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    SharedPreferences sharedPreferences;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh(); // 내용 없음. 삭제 무관
        // 단말기의 고유 ID = 토큰 을 발급 받는다.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken(); // 토큰을 발급받는다.
        Log.e("TEST", "onTokenRefresh: " + refreshedToken);

        sharedPreferences = getSharedPreferences("LoginSetting.dat", MODE_PRIVATE);

        if (refreshedToken != null && refreshedToken.length() > 0) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("memberToken", refreshedToken);
            editor.commit();
            Log.e("TEST", "시작해야지");
            sendToken();
        }
//        sendRegistrationToServer(refreshedToken);
//        내가 추후 정의를 통해서 우리서버의 회원테이블에 토큰값이 저장될 수 있도록 해주어야 한다.
    }

    public void sendToken() {
        Log.e("TEST", "토큰 보내기 시작");
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ldh66210.cafe24.com/push/updatePushToken.do"
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.137.1/push/updatePushToken.do"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MyLog", "response : " + response);
//                showToast(response);
                // 응답을 처리한다.

                JsonParser parser = new JsonParser();
                JsonObject rootObj = (parser.parse(response)).getAsJsonObject();
                String result = rootObj.get("result").getAsString();
                String resultMsg = rootObj.get("resultMsg").getAsString();

                Gson gson = new Gson();

                // TODO 파싱

                if ( (result != null) && ("fail".equals(result)) ) {
//                    showToast("null 또는 fail: " + resultMsg);
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

                String LoginId = sharedPreferences.getString("LoginId", "");
                String memberToken = sharedPreferences.getString("memberToken", "");

                Map<String, String> params = new HashMap<>();
                params.put("memberId", LoginId);
                params.put("memberToken", memberToken);
                return params;
            }
        };
        Log.e("TEST", "토큰 전송 전");
        requestQueue.add(stringRequest);
        Log.e("TEST", "토큰 전송 후");
        Log.e("TEST", "토큰 보내기 종료");
    } // end of sendToken
} // end of class
