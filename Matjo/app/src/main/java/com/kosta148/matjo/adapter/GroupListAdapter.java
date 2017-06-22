package com.kosta148.matjo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.kosta148.matjo.R;
import com.kosta148.matjo.bean.GroupBean;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Daehee on 2017-06-17.
 */

public class GroupListAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<GroupBean> groupList;
    LayoutInflater inflater;

    public GroupListAdapter(Context context, int layout, List<GroupBean> groupList) {
        this.context = context;
        this.layout = layout;
        this.groupList = groupList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    } // Constructor

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Object getItem(int position) {
        return groupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_group_list, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.ivGroupImg = (ImageView)convertView.findViewById(R.id.ivGroupImg);
            viewHolder.tvGroupName = (TextView)convertView.findViewById(R.id.tvGroupName);
            viewHolder.tvGroupInfo = (TextView)convertView.findViewById(R.id.tvGroupInfo);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        GroupBean gBean = (GroupBean) getItem(position);
        if (position != 0) {
            Glide.with(context).load(gBean.getGroupImg())
                    .bitmapTransform(new CropCircleTransformation(new CustomBitmapPool()))
                    .thumbnail(0.1f)
                    .error(R.mipmap.ic_launcher)
                    .into(viewHolder.ivGroupImg);
        } else {
            // 0번이면 모임 추가 이미지를 보여준다
            viewHolder.ivGroupImg.setImageResource(R.drawable.circle_plus);
        }

        viewHolder.tvGroupName.setText(gBean.getGroupName());
        viewHolder.tvGroupInfo.setText(gBean.getGroupInfo());
        return convertView;
    }

    static class ViewHolder {
        public ImageView ivGroupImg;
        public TextView tvGroupName;
        public TextView tvGroupInfo;
    } // end of inner class ViewHolder
} // end of class

/**
 * Circle 이미지 사용을 위해 Glide 에서 요구하고있는 BitmapPool 객체이다. 설정법을 모르면
 * Default 로 사용을 해도 무방하다.
 * 동그란이미지가 사용되는 GroupList 에만 적용되므로 별도 파일로 분리하지 않았다.
 */
class CustomBitmapPool implements BitmapPool {
    @Override
    public int getMaxSize() {
        return 0;
    }
    @Override
    public void setSizeMultiplier(float sizeMultiplier) {
    }
    @Override
    public boolean put(Bitmap bitmap) {
        return false;
    }
    @Override
    public Bitmap get(int width, int height, Bitmap.Config config) {
        return null;
    }
    @Override
    public Bitmap getDirty(int width, int height, Bitmap.Config config) {
        return null;
    }
    @Override
    public void clearMemory() {
    }
    @Override
    public void trimMemory(int level) {
    }
} // end of class