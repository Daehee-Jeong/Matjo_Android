package com.kosta148.matjo.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kosta148.matjo.R;
import com.kosta148.matjo.util.RoundedDrawable;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Daehee on 2017-05-15.
 */

public class GroupInfoActivity extends AppCompatActivity {
    String name = "모임 이름";
    Toolbar toolbar;
    Bitmap bm;
    RoundedDrawable rd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bm = BitmapFactory.decodeResource(getResources(), R.drawable.profile_group);
        rd = new RoundedDrawable(bm);

        ((ImageView) findViewById(R.id.ivProfileGroup)).setImageDrawable(rd);
    }
} // end of class
