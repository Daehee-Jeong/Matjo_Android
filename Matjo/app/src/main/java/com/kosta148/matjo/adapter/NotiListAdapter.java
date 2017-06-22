package com.kosta148.matjo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kosta148.matjo.R;
import com.kosta148.matjo.bean.NewsFeedBean;

import java.util.List;
import java.util.zip.Inflater;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Daehee on 2017-06-22.
 */

public class NotiListAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<NewsFeedBean> newsFeedList;
    LayoutInflater inflater;

    public NotiListAdapter(Context context, int layout, List<NewsFeedBean> newsFeedList) {
        this.context = context;
        this.layout = layout;
        this.newsFeedList = newsFeedList;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return newsFeedList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsFeedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(layout, null, false);
            viewHolder = new ViewHolder();
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.ivNoti = (ImageView) convertView.findViewById(R.id.ivNoti);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NewsFeedBean newsFeedBean = (NewsFeedBean) getItem(position);

        String content = "모임 " +
                            newsFeedBean.getGroupName() +
                            "의 " +
                            newsFeedBean.getTypeMsg() +
                            "(" +
                            newsFeedBean.getRestaName() +
                            ")" +
                            " 등록되었습니다.";
        viewHolder.tvContent.setText(content);
        viewHolder.tvDate.setText(newsFeedBean.getRegDate());
        Glide.with(context).load(newsFeedBean.getImgPath())
                .bitmapTransform(new CropCircleTransformation(new CustomBitmapPool()))
                .thumbnail(0.1f)
                .error(R.drawable.ic_group)
                .into(viewHolder.ivNoti);

        return convertView;
    }

    static class ViewHolder {
        ImageView ivNoti;
        TextView tvContent;
        TextView tvDate;
    }
} // end of class
