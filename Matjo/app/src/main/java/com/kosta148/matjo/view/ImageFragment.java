package com.kosta148.matjo.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kosta148.matjo.R;

/**
 * Created by Daehee on 2017-05-13.
 */

public class ImageFragment extends Fragment {
    public static final String ARG_OBJECTS_IMAGE = "imageResId";
    public static final String ARG_OBJECTS_IMAGE_LIST = "imageList";
    AppCompatActivity scrollingActivity; // 부모 액티비티
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image, container, false);
        Bundle args = getArguments();
        String type = args.getString("type");   // group 모임 / resta 맛집

        scrollingActivity = (AppCompatActivity) getActivity();

        // 이미지 셋팅
        imageView = (ImageView) v.findViewById(R.id.imageView01);
        String imgPath = args.getString(ARG_OBJECTS_IMAGE_LIST);
        if ("group".equals(type)) {
            imgPath = "http://ldh66210.cafe24.com/upload/" + imgPath;
        }
        Glide.with(getActivity()).load(imgPath).thumbnail(0.1f).error(R.drawable.ic_no_image_large).into(imageView);

        return v;
    }
} // end of class
