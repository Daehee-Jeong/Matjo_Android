package com.kosta148.matjo.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kosta148.matjo.R;

/**
 * Created by Daehee on 2017-04-26.
 */

public class GroupListFragment extends Fragment {
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_group_list, container, false);
        mainActivity = (MainActivity) getActivity();

        return v;
    } // end of onCreateView
}
