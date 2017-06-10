package com.kosta148.matjo.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kosta148.matjo.view.ImageFragment;

/**
 * Created by Daehee on 2017-05-13.
 */

public class PagerInToolBarAdapter extends FragmentStatePagerAdapter {
    // 다량의 데이터 (업소 사진들)
    int resArr[];

    /**
     * 툴바 안에 있는 뷰페이저를 위한 어댑터, 어댑터의 생성자.
     * @param resArr 다량의 데이터 (추후 이미지 URL 경로를 이용하므로 String 배열로 사용한다.
     */
    public PagerInToolBarAdapter(FragmentManager fm, int resArr[]) {
        super(fm);
        this.resArr = resArr;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putInt(ImageFragment.ARG_OBJECTS_IMAGE, resArr[position%resArr.length]);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return resArr.length;
    }
} // end of class
