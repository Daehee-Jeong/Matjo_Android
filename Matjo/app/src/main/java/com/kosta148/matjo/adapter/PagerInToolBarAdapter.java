package com.kosta148.matjo.adapter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kosta148.matjo.R;
import com.kosta148.matjo.data.DaumLocalBean;
import com.kosta148.matjo.view.ImageFragment;

/**
 * Created by Daehee on 2017-05-13.
 */

public class PagerInToolBarAdapter extends FragmentStatePagerAdapter {
    // 다량의 데이터 (업소 사진들)
    int resArr[];
    Bitmap bitmap;
    /**
     * 툴바 안에 있는 뷰페이저를 위한 어댑터, 어댑터의 생성자.
     */
    public PagerInToolBarAdapter(FragmentManager fm, int resArr[]) {
        super(fm);
        this.resArr = resArr;
    }

    /**
     * 모임 툴바 이미지(하나밖에 안되고, 웹 url로 받아와야함) 받아오는 용도
     * @param fm
     * @param bitmap
     */
    public PagerInToolBarAdapter(FragmentManager fm, Bitmap bitmap) {
        super(fm);
        this.bitmap = bitmap;
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
