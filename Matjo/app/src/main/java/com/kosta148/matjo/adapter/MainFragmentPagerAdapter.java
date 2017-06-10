package com.kosta148.matjo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.kosta148.matjo.view.GroupListFragment;
import com.kosta148.matjo.view.NewsFeedFragment;
import com.kosta148.matjo.view.RestaListFragment;

/**
 * MainFragment 에서 제공하는 ViewPager 의 Adapter 클래스
 * Created by Daehee on 2017-04-26.
 */

public class MainFragmentPagerAdapter extends FragmentStatePagerAdapter{

    static Fragment fragments[] = new Fragment[3];
    /*String tabTitle[] = new String[]{"뉴스피드", "맛집 목록", "모임 목록"};*/
    String tabTitle[] = new String[]{"", "", ""}; // 아이콘만 표시하기 위해 공백 값 처리

    // Constructor
    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments[0] = new NewsFeedFragment();
        fragments[1] = new RestaListFragment();
        fragments[2] = new GroupListFragment();
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("getItem 호출", position + " 번째");
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }
} // end of class
