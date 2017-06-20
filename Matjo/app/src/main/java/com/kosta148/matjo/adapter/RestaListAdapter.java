package com.kosta148.matjo.adapter;

import android.content.Context;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kosta148.matjo.R;
import com.kosta148.matjo.data.DaumLocalBean;
import com.kosta148.matjo.view.RestaListFragment;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Daehee on 2017-06-17.
 */

public class RestaListAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<DaumLocalBean> restaList;
    LayoutInflater inflater;
    int lastPosition = Integer.MIN_VALUE;

    public RestaListAdapter(Context context, int layout, List<DaumLocalBean> restaList) {
        this.context = context;
        this.layout = layout;
        this.restaList = restaList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    } // Constructor

    @Override
    public int getCount() {
        return restaList.size();
    }

    @Override
    public Object getItem(int position) {
        return restaList.get(position);
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
            viewHolder.ivRestaImg = (ImageView)convertView.findViewById(R.id.ivRestaImg);
            viewHolder.tvRestaTitle = (TextView)convertView.findViewById(R.id.tvRestaTitle);
            viewHolder.tvRestaCate = (TextView)convertView.findViewById(R.id.tvRestaCate);
            viewHolder.tvRestaAddr = (TextView)convertView.findViewById(R.id.tvRestaAddr);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DaumLocalBean dlBean = (DaumLocalBean) getItem(position);
        Glide.with(context).load(dlBean.getRestaImgUrl()).thumbnail(0.1f).error(R.drawable.ic_no_image_large).into(viewHolder.ivRestaImg);
        viewHolder.tvRestaTitle.setText(dlBean.getRestaTitle());
        viewHolder.tvRestaCate.setText(dlBean.getRestaCate());
        viewHolder.tvRestaAddr.setText(dlBean.getRestaAddr());

//        애니메이션 (notifyDataSetChanged 호출 시 깜빡임의 이슈로 주석처리)
//        Animation animation = AnimationUtils.loadAnimation(context, (position > this.lastPosition ? R.anim.add_from_bottom : R.anim.add_from_top));
//        convertView.startAnimation(animation);
//        lastPosition = position;
        return convertView;
    }

    static class ViewHolder {
        public ImageView ivRestaImg;
        public TextView tvRestaTitle;
        public TextView tvRestaCate;
        public TextView tvRestaAddr;
    } // end of inner class ViewHolder
} // end of class
