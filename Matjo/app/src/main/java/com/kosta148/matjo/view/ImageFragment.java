package com.kosta148.matjo.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kosta148.matjo.R;

/**
 * Created by Daehee on 2017-05-13.
 */

public class ImageFragment extends Fragment {
    public static final String ARG_OBJECTS_IMAGE = "imageResId";
    AppCompatActivity scrollingActivity; // 부모 액티비티
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image, container, false);
        Bundle args = getArguments();

        scrollingActivity = (AppCompatActivity) getActivity();

        // 이미지 셋팅
        imageView = (ImageView) v.findViewById(R.id.imageView01);
        imageView.setImageResource(args.getInt(ARG_OBJECTS_IMAGE));

        return v;
    }
} // end of class
