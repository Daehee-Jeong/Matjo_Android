package com.kosta148.matjo.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kosta148.matjo.bean.ReviewBean;
import com.kosta148.matjo.data.DaumLocalBean;
import com.kosta148.matjo.view.RestaurantInfoFragment;
import com.kosta148.matjo.view.RestaurantReviewFragment;

import java.util.ArrayList;

/**
 * Created by Daehee on 2017-05-14.
 */

public class PagerInRestaDetailAdapter extends FragmentStatePagerAdapter {
    // 탭 타이틀
    private String[] tabTitles = new String[]{"음식점 정보", "모임 리뷰", };

    private DaumLocalBean dlBean;
    private ArrayList<ReviewBean> reviewList;

    public PagerInRestaDetailAdapter(FragmentManager fm, DaumLocalBean dlBean, ArrayList<ReviewBean> reviewList) {
        super(fm);
        this.dlBean = dlBean;
        this.reviewList = reviewList;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RestaurantInfoFragment rif = new RestaurantInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("dlBean", dlBean);
                rif.setArguments(bundle);
                return rif;
            case 1:
                RestaurantReviewFragment rlf = new RestaurantReviewFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("dlBean", dlBean);
                bundle2.putParcelableArrayList("reviewList", reviewList);
                rlf.setArguments(bundle2);
                return rlf;
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
