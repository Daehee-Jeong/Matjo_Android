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
import com.kosta148.matjo.data.DaumLocalBean;
import com.kosta148.matjo.data.MemberBean;

import java.util.List;

/**
 * Created by Daehee on 2017-06-17.
 */

public class MemberListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<MemberBean> memberList;

    public MemberListAdapter(Context context, List<MemberBean> memberList) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.memberList = memberList;
    } // Constructor

    @Override
    public int getCount() {
        return memberList.size();
    }

    @Override
    public Object getItem(int position) {
        return memberList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_member, null);

            viewHolder = new ViewHolder();
            viewHolder.ivMemberimg = (ImageView)convertView.findViewById(R.id.ivMember);
            viewHolder.tvMemberName = (TextView) convertView.findViewById(R.id.tvMemberName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MemberBean mBean = (MemberBean) getItem(position);
        Glide.with(context).load(mBean.getMemberImg()).thumbnail(0.1f).error(R.drawable.ic_no_image_large).into(viewHolder.ivMemberimg);
        viewHolder.tvMemberName.setText(mBean.getMemberName());

        return convertView;
    }

    static class ViewHolder {
        public ImageView ivMemberimg;
        public TextView tvMemberName;
    } // end of inner class ViewHolder
} // end of class
