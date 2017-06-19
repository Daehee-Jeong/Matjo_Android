package com.kosta148.matjo.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kosta148.matjo.R;
import com.kosta148.matjo.bean.GroupBean;

/**
 * Created by Daehee on 2017-05-14.
 */

public class GroupInfoFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group_info, container, false);

        GroupBean groupBean = (GroupBean) getArguments().get("gBean");

        TextView tvGroupLeader = (TextView) v.findViewById(R.id.groupLeader);
        TextView tvGroupInfo = (TextView) v.findViewById(R.id.groupInfo);
        TextView tvGroupSize = (TextView) v.findViewById(R.id.groupSize);

        tvGroupLeader.setText("모임장 : "+groupBean.getGroupLeader());
        tvGroupInfo.setText("모임 소개 : "+groupBean.getGroupInfo());
        tvGroupSize.setText("모임원 수 : "+groupBean.getGroupSize()+"명");

        return v;
    } // end of onCreateView

} // end of class
