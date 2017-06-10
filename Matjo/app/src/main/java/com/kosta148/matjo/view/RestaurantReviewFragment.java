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

import com.kosta148.matjo.R;
import com.kosta148.matjo.adapter.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daehee on 2017-05-14.
 */

public class RestaurantReviewFragment extends Fragment {
    private RecyclerView recyclerview;
    ScrollingActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_restaurant_review, container, false);

        activity = (ScrollingActivity) getActivity();

        recyclerview = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data = new ArrayList<>();

        ExpandableListAdapter.Item places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "사람A", R.drawable.profile, 3.5);
        places.invisibleChildren = new ArrayList<>();
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, getResources().getString(R.string.long_text), R.drawable.img06));
        data.add(places);

        places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "사람B", R.drawable.profile, 3.5);
        places.invisibleChildren = new ArrayList<>();
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, getResources().getString(R.string.long_text), R.drawable.img06));
        data.add(places);

        places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "사람B", R.drawable.profile, 3.5);
        places.invisibleChildren = new ArrayList<>();
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, getResources().getString(R.string.long_text), R.drawable.img06));
        data.add(places);places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "사람B", R.drawable.profile, 3.5);
        places.invisibleChildren = new ArrayList<>();
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, getResources().getString(R.string.long_text), R.drawable.img06));
        data.add(places);places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "사람B", R.drawable.profile, 3.5);
        places.invisibleChildren = new ArrayList<>();
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, getResources().getString(R.string.long_text), R.drawable.img06));
        data.add(places);places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "사람B", R.drawable.profile, 3.5);
        places.invisibleChildren = new ArrayList<>();
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, getResources().getString(R.string.long_text), R.drawable.img06));
        data.add(places);places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "사람B", R.drawable.profile, 3.5);
        places.invisibleChildren = new ArrayList<>();
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, getResources().getString(R.string.long_text), R.drawable.img06));
        data.add(places);places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "사람B", R.drawable.profile, 3.5);
        places.invisibleChildren = new ArrayList<>();
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, getResources().getString(R.string.long_text), R.drawable.img06));
        data.add(places);places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "사람B", R.drawable.profile, 3.5);
        places.invisibleChildren = new ArrayList<>();
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, getResources().getString(R.string.long_text), R.drawable.img06));
        data.add(places);places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "사람B", R.drawable.profile, 3.5);
        places.invisibleChildren = new ArrayList<>();
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, getResources().getString(R.string.long_text), R.drawable.img06));
        data.add(places);places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "사람B", R.drawable.profile, 3.5);
        places.invisibleChildren = new ArrayList<>();
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, getResources().getString(R.string.long_text), R.drawable.img06));
        data.add(places);places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "사람B", R.drawable.profile, 3.5);
        places.invisibleChildren = new ArrayList<>();
        places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, getResources().getString(R.string.long_text), R.drawable.img06));
        data.add(places);

        recyclerview.setAdapter(new ExpandableListAdapter(data, activity.getApplicationContext()));

        return v;
    } // end of onCreateView
} // end of class
