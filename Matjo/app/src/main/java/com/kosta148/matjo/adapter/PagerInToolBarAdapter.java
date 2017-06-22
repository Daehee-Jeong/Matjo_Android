package com.kosta148.matjo.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kosta148.matjo.view.ImageFragment;

import java.util.List;

/**
 * Created by Daehee on 2017-05-13.
 */

public class PagerInToolBarAdapter extends FragmentStatePagerAdapter {
    // 다량의 데이터 (업소 사진들)
    int resArr[];
    List<String> imgList;
    String type = "";   // group 모임 / resta 맛집
    /**
     * 툴바 안에 있는 뷰페이저를 위한 어댑터, 어댑터의 생성자.
     */
//    public PagerInToolBarAdapter(FragmentManager fm, int resArr[]) {
//        super(fm);
//        this.resArr = resArr;
//    }

    public PagerInToolBarAdapter(FragmentManager fm, List<String> imgList, String type) {
        super(fm);
        this.imgList = imgList;
        this.type = type;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new ImageFragment();
        Bundle args = new Bundle();
//        args.putInt(ImageFragment.ARG_OBJECTS_IMAGE, resArr[position%resArr.length]);
        args.putString("type", type);
        args.putString(ImageFragment.ARG_OBJECTS_IMAGE_LIST, imgList.get(position%imgList.size()));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return imgList.size();
    }
} // end of class
