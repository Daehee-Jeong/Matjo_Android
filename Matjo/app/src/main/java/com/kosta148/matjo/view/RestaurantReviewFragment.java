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
import com.google.gson.reflect.TypeToken;
import com.kosta148.matjo.R;
import com.kosta148.matjo.adapter.ExpandableListAdapter;
import com.kosta148.matjo.bean.PereviewBean;
import com.kosta148.matjo.bean.ReviewBean;
import com.kosta148.matjo.data.DaumLocalBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Daehee on 2017-05-14.
 */

public class RestaurantReviewFragment extends Fragment {
    private RecyclerView recyclerview;
    RestaDetailActivity restaDetailActivity;

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
    DaumLocalBean dlBean;

    Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_restaurant_review, container, false);

        try {
            reviewList = getArguments().getParcelableArrayList("reviewList");
        } catch(NullPointerException e) {
            reviewList = new ArrayList<ReviewBean>();
        }

        sharedPreferences = getContext().getSharedPreferences("LoginSetting.dat", MODE_PRIVATE);
        Gson gson = new Gson();
        for (int i = 0; i < reviewList.size(); i++) {
            ArrayList<PereviewBean> pereviewBeanList = gson.fromJson(reviewList.get(i).getPereviewJSArray(), new TypeToken<ArrayList<PereviewBean>>() {}.getType());
            reviewList.get(i).setPereviewList(pereviewBeanList);
        }

        restaDetailActivity = (RestaDetailActivity) getActivity();

        recyclerview = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));



        return v;
    } // end of onCreateView

} // end of class
