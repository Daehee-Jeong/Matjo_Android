package com.kosta148.matjo.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kosta148.matjo.R;
import com.kosta148.matjo.adapter.ExpandableListAdapter;
import com.kosta148.matjo.bean.PereviewBean;
import com.kosta148.matjo.bean.ReviewBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daehee on 2017-05-14.
 */

public class GroupReviewFragment extends Fragment {
    private RecyclerView recyclerview;
    GroupDetailActivity groupDetailActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_restaurant_review, container, false);

        ArrayList<ReviewBean> reviewList = getArguments().getParcelableArrayList("reviewList");
        Gson gson = new Gson();
        for (int i = 0; i < reviewList.size(); i++) {
            ArrayList<PereviewBean> pereviewBeanList = gson.fromJson(reviewList.get(i).getPereviewJSArray(), new TypeToken<ArrayList<PereviewBean>>() {}.getType());
            reviewList.get(i).setPereviewList(pereviewBeanList);
        }

        groupDetailActivity = (GroupDetailActivity) getActivity();

        recyclerview = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data = new ArrayList<>();

        for (int i = 0; i < reviewList.size(); i++) {
            // TODO 업소 이미지 받아올 수 있나요?
            ExpandableListAdapter.Item placeTmp = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, reviewList.get(i).getReviewRestaName(), R.drawable.profile, Double.valueOf(reviewList.get(i).getAvgRating()));
            placeTmp.invisibleChildren = new ArrayList<>();
            List<PereviewBean> perTmpList = reviewList.get(i).getPereviewList();
            int perSize = perTmpList.size();
            for (int j = 0; j < perSize; j++) {
                PereviewBean perTmp = perTmpList.get(j);
                placeTmp.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, perTmp.getPereviewMemName()+":"+perTmp.getPereviewContent(), R.drawable.img06));
            }
            data.add(placeTmp);
        }

        recyclerview.setAdapter(new ExpandableListAdapter(data, groupDetailActivity.getApplicationContext()));

        return v;
    } // end of onCreateView
} // end of class
