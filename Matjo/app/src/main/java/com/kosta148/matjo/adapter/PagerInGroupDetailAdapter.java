package com.kosta148.matjo.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kosta148.matjo.bean.GroupBean;
import com.kosta148.matjo.bean.ReviewBean;
import com.kosta148.matjo.view.GroupInfoFragment;
import com.kosta148.matjo.view.GroupReviewFragment;

import java.util.ArrayList;

/**
 * Created by Daehee on 2017-05-14.
 */

public class PagerInGroupDetailAdapter extends FragmentStatePagerAdapter {
    // 탭 타이틀
    private String[] tabTitles = new String[]{"모임 정보", "모임 리뷰", };
    private GroupBean groupBean;
    private ArrayList<ReviewBean> reviewList;

    public PagerInGroupDetailAdapter(FragmentManager fm, GroupBean groupBean, ArrayList<ReviewBean> reviewList) {
        super(fm);
        this.groupBean = groupBean;
        this.reviewList = reviewList;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                GroupInfoFragment gif = new GroupInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("gBean", groupBean);
                gif.setArguments(bundle);
                return gif;
            case 1:
                GroupReviewFragment glf = new GroupReviewFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("gBean", groupBean);
                bundle2.putParcelableArrayList("reviewList", reviewList);
                glf.setArguments(bundle2);
                return glf;
            default:
                return null;
        }
    } // end of getItem

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
} // end of class
