package com.kosta148.matjo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kosta148.matjo.view.RestaurantInfoFragment;
import com.kosta148.matjo.view.RestaurantReviewFragment;

/**
 * Created by Daehee on 2017-05-14.
 */

public class PagerInGroupInfoAdapter extends FragmentStatePagerAdapter {
    // 탭 타이틀
    private String[] tabTitles = new String[]{"음식점 정보", "모임 리뷰", };

    public PagerInGroupInfoAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                RestaurantInfoFragment rif = new RestaurantInfoFragment();
                return rif;
            case 1:
                RestaurantReviewFragment rlf = new RestaurantReviewFragment();
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
