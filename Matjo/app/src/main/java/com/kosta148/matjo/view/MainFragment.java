package com.kosta148.matjo.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.astuetz.PagerSlidingTabStrip;
import com.kosta148.matjo.R;
import com.kosta148.matjo.adapter.MainFragmentPagerAdapter;

/**
 * Created by Daehee on 2017-04-26.
 */

public class MainFragment extends Fragment {
    MainFragmentPagerAdapter mainFragmentPagerAdapter;
    ViewPager viewPager;
    MainActivity mainActivity;
    PagerSlidingTabStrip tabStrip;
    TabLayout tabLayout;
    int currentPos = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_main, container, false);
        mainActivity = (MainActivity) getActivity();

        mainFragmentPagerAdapter = new MainFragmentPagerAdapter(mainActivity.getSupportFragmentManager());
        viewPager = (ViewPager) v.findViewById(R.id.viewPager01);
        viewPager.setAdapter(mainFragmentPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                currentPos = position;
                tabLayout.getTabAt(0).setIcon(R.mipmap.ic_news_feed_white_36dp_default);
                tabLayout.getTabAt(1).setIcon(R.mipmap.ic_restaurant_white_36dp_default);
                tabLayout.getTabAt(2).setIcon(R.mipmap.ic_people_36dp_default);

                switch(position) {
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.mipmap.ic_news_feed_white_36dp_pressed);
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.mipmap.ic_restaurant_white_36dp_pressed);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.mipmap.ic_people_36dp_pressed);
                        break;
                } // end of switch
            } // end of pageSelected()
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
//        tabStrip = (PagerSlidingTabStrip) v.findViewById(R.id.tabStrip);
//        tabStrip.setViewPager(viewPager);

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.mipmap.ic_news_feed_white_36dp_pressed);
        tabLayout.getTabAt(1).setIcon(R.mipmap.ic_restaurant_white_36dp_default);
        tabLayout.getTabAt(2).setIcon(R.mipmap.ic_people_36dp_default);

        return v;
    } // end of onCreateView

    public int getPx(int dimensionDp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dimensionDp * density + 0.5f);
    }
} // end of class




