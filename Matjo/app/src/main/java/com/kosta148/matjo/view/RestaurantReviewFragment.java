package com.kosta148.matjo.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kosta148.matjo.R;
import com.kosta148.matjo.adapter.ExpandableListAdapter;
import com.kosta148.matjo.adapter.RestaExpandableListAdapter;
import com.kosta148.matjo.bean.PereviewBean;
import com.kosta148.matjo.bean.ReviewBean;
import com.kosta148.matjo.data.DaumLocalBean;

import java.util.ArrayList;
import java.util.List;

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

    RestaExpandableListAdapter adapter;

    List<RestaExpandableListAdapter.Item> data;
    DaumLocalBean dlBean;

    Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_restaurant_review, container, false);

        reviewList = getArguments().getParcelableArrayList("reviewList");
        if (reviewList == null) reviewList = new ArrayList<ReviewBean>();

        Gson gson = new Gson();
        for (int i = 0; i < reviewList.size(); i++) {
            ArrayList<PereviewBean> pereviewBeanList = gson.fromJson(reviewList.get(i).getPereviewJSArray(), new TypeToken<ArrayList<PereviewBean>>() {}.getType());
            reviewList.get(i).setPereviewList(pereviewBeanList);
        }

        restaDetailActivity = (RestaDetailActivity) getActivity();

        recyclerview = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        List<RestaExpandableListAdapter.Item> data = new ArrayList<>();

        for (int i = 0; i < reviewList.size(); i++) {
            // TODO 업소 이미지 받아올 수 있나요?
            RestaExpandableListAdapter.Item placeTmp = new RestaExpandableListAdapter.Item(RestaExpandableListAdapter.HEADER, reviewList.get(i).getReviewGroupName(), "http://blogfiles1.naver.net/20140628_246/baseon_1403916116658pykLg_PNG/%B9%AB%B7%E1%BE%C6%C0%CC%C4%DC_%B8%C6%B5%B5%B3%AF%B5%E5.png", Double.valueOf(reviewList.get(i).getAvgRating()));
            placeTmp.invisibleChildren = new ArrayList<>();
            List<PereviewBean> perTmpList = reviewList.get(i).getPereviewList();
            int perSize = perTmpList.size();
            for (int j = 0; j < perSize; j++) {
                PereviewBean perTmp = perTmpList.get(j);
                double rating = 0.0;
                if (!Double.isNaN(Double.parseDouble(perTmp.getPereviewRating()))) rating = Double.parseDouble(perTmp.getPereviewRating());
                placeTmp.invisibleChildren.add(new RestaExpandableListAdapter.Item(RestaExpandableListAdapter.CHILD, perTmp.getPereviewMemName(), perTmp.getPereviewContent(), perTmp.getPereviewMemImg(), perTmp.getPereviewImgUrl(), rating));
            }
            data.add(placeTmp);
        }

        recyclerview.setAdapter(new RestaExpandableListAdapter(data, restaDetailActivity.getApplicationContext()));

        return v;
    } // end of onCreateView
} // end of class
