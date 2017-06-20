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

/**
 * Created by Daehee on 2017-06-17.
 */

public class NewsFeedAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<NewsFeedBean> newsFeedList;
    LayoutInflater inflater;

    public NewsFeedAdapter(Context context, int layout, List<NewsFeedBean> restaList) {
        this.context = context;
        this.layout = layout;
        this.newsFeedList = restaList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    } // Constructor

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
            convertView = inflater.inflate(R.layout.item_resta_list, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.ivNewsImg = (ImageView)convertView.findViewById(R.id.ivRestaImg);
            viewHolder.tvNewsTitle = (TextView)convertView.findViewById(R.id.tvRestaTitle);
            viewHolder.tvNewsCate = (TextView)convertView.findViewById(R.id.tvRestaCate);
            viewHolder.tvNewsAddr = (TextView)convertView.findViewById(R.id.tvRestaAddr);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NewsFeedBean nBean = (NewsFeedBean) getItem(position);
        String type = nBean.getType();
        if (type.equals("1")) { //모임
            Glide.with(context).load("http://ldh66210.cafe24.com/upload/"+nBean.getImgPath()).thumbnail(0.1f).error(R.drawable.ic_group).into(viewHolder.ivNewsImg);
            viewHolder.tvNewsTitle.setText("["+nBean.getGroupName()+"] 모임이 등록되었습니다");
            viewHolder.tvNewsCate.setText(nBean.getRegDate());
            viewHolder.tvNewsAddr.setText("모임장 | "+nBean.getGroupLeader());
        } else { // 리뷰 등록
            Glide.with(context).load(nBean.getImgPath()).thumbnail(0.1f).error(R.drawable.ic_resta).into(viewHolder.ivNewsImg);
            viewHolder.tvNewsTitle.setText("["+nBean.getRestaName()+"]의 리뷰가 등록되었습니다");
            viewHolder.tvNewsCate.setText(nBean.getRegDate());
            viewHolder.tvNewsAddr.setText(nBean.getRestaCate());
        }
        return convertView;
    }

    static class ViewHolder {
        public ImageView ivNewsImg;
        public TextView tvNewsTitle;
        public TextView tvNewsCate;
        public TextView tvNewsAddr;
    } // end of inner class ViewHolder
} // end of class
