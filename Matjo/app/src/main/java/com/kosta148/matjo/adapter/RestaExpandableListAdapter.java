package com.kosta148.matjo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kosta148.matjo.R;
import com.kosta148.matjo.util.RoundedDrawable;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Daehee on 2017-06-21.
 */

public class RestaExpandableListAdapter extends RecyclerView.Adapter{
    Context context;
    public static final int HEADER = 0;
    public static final int CHILD = 1;

    private List<Item> data;
    Bitmap bm;
    RoundedDrawable rd;

    public RestaExpandableListAdapter(List<Item> data, Context context) {
        this.data = data;
        this.context = context;
        bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.profile);
        rd = new RoundedDrawable(bm);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = null;
        Context context = parent.getContext();
        float dp = context.getResources().getDisplayMetrics().density;
        int subItemPaddingLeft = (int) (18 * dp);
        int subItemPaddingTopAndBottom = (int) (5 * dp);
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (type) {
            case HEADER:
                view = inflater.inflate(R.layout.list_header_resta_review, parent, false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                return header;
            case CHILD:
//                TextView itemTextView = new TextView(context);
//                itemTextView.setPadding(subItemPaddingLeft, subItemPaddingTopAndBottom, 0, subItemPaddingTopAndBottom);
//                itemTextView.setTextColor(0x88000000);
//                itemTextView.setLayoutParams(
//                        new ViewGroup.LayoutParams(
//                                ViewGroup.LayoutParams.MATCH_PARENT,
//                                ViewGroup.LayoutParams.WRAP_CONTENT));
//                return new RecyclerView.ViewHolder(itemTextView) {
//                };
                view = inflater.inflate(R.layout.list_child_resta_review, parent, false);
                ListChildViewHolder child = new ListChildViewHolder(view);
                return child;
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Item item = data.get(position);
        switch (item.type) {
            case HEADER:
                final ListHeaderViewHolder headerViewHolder = (ListHeaderViewHolder) holder;
                headerViewHolder.refferalItem = item;
                headerViewHolder.header_title.setText(item.title);
//                headerViewHolder.header_profile.setImageDrawable(rd);

                Glide.with(context).load("http://ldh66210.cafe24.com/upload/"+item.imgProfile)
                        .bitmapTransform(new CropCircleTransformation(new CustomBitmapPool()))
                        .thumbnail(0.1f)
                        .error(R.drawable.ic_group)
                        .into(headerViewHolder.header_profile);

                headerViewHolder.header_rating.setText(item.rating+"");
                headerViewHolder.ratingBarAvg.setRating((float) item.rating);

                if (item.invisibleChildren == null) {
                    headerViewHolder.btn_expand_toggle.setImageResource(R.drawable.circle_minus);
                } else {
                    headerViewHolder.btn_expand_toggle.setImageResource(R.drawable.circle_plus);
                }
                headerViewHolder.btn_expand_toggle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<Item>();
                            int count = 0;
                            int pos = data.indexOf(headerViewHolder.refferalItem);
                            while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            headerViewHolder.btn_expand_toggle.setImageResource(R.drawable.circle_plus);
                        } else {
                            int pos = data.indexOf(headerViewHolder.refferalItem);
                            int index = pos + 1;
                            for (Item i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            headerViewHolder.btn_expand_toggle.setImageResource(R.drawable.circle_minus);
                            item.invisibleChildren = null;
                        }
                    }
                });
                break;
            case CHILD:
                final ListChildViewHolder childViewHolder = (ListChildViewHolder) holder;
                childViewHolder.refferalItem = item;

                childViewHolder.child_member_id.setText(item.title);
                childViewHolder.child_content.setText(item.content);
                childViewHolder.child_rating.setText(item.rating+"");
                childViewHolder.ratingBar.setRating((float) item.rating);

//                childViewHolder.child_img.setImageResource(item.imgReview);
                Glide.with(context).load("http://ldh66210.cafe24.com/upload/"+item.imgProfile)
                        .thumbnail(0.1f)
                        .error(R.drawable.default_profile)
                        .into(childViewHolder.child_profile);


                childViewHolder.child_img.setVisibility(View.VISIBLE);
                if (item.imgProfile != null && !"".equals(item.imgProfile)) {
                    Glide.with(context).load("http://ldh66210.cafe24.com/upload/"+item.imgReview)
                            .thumbnail(0.1f)
                            .error(R.drawable.ic_no_image_large)
                            .into(childViewHolder.child_img);
                } else {
                    childViewHolder.child_img.setVisibility(View.GONE);
                }

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // 헤더 뷰홀더
    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView header_title;
        public ImageView header_profile;
        public ImageView btn_expand_toggle;
        public TextView header_rating;
        public RatingBar ratingBarAvg;
        public Item refferalItem;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            header_title = (TextView) itemView.findViewById(R.id.header_title);
            header_profile = (ImageView) itemView.findViewById(R.id.header_profile);
            btn_expand_toggle = (ImageView) itemView.findViewById(R.id.btn_expand_toggle);
            header_rating = (TextView) itemView.findViewById(R.id.header_rating);
            ratingBarAvg = (RatingBar) itemView.findViewById(R.id.ratingBarAvg);
        }
    } // end of class

    // 자식 뷰홀더
    private static class ListChildViewHolder extends RecyclerView.ViewHolder {
        public TextView child_content;
        public ImageView child_img;
        public Item refferalItem;
        public ImageView child_profile;
        public TextView child_rating;
        public TextView child_member_id;
        public RatingBar ratingBar;

        public ListChildViewHolder(View itemView) {
            super(itemView);
            child_content = (TextView) itemView.findViewById(R.id.child_content);
            child_img = (ImageView) itemView.findViewById(R.id.child_img);
            child_profile = (ImageView) itemView.findViewById(R.id.child_member_profile);
            child_rating = (TextView) itemView.findViewById(R.id.child_rating_tv);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            child_member_id = (TextView) itemView.findViewById(R.id.child_member_id);
        }
    } // end of class

    public static class Item {
        public int type;
        public String title;
        public String imgProfile;
        public String imgReview;
        public String content;
        public double rating;
        public List<Item> invisibleChildren;

        public Item() {
        }

        // 헤더 아이템일 경우 사용하는 생성자
        public Item(int type, String title, String imgProfile, double rating) {
            this.type = type;
            this.title = title;
            this.imgProfile = imgProfile;
            this.rating = rating;
        }

        // 자식 아이템일 경우 사용하는 생성자

        /**
         * 자식 아이템일 경우 사용하는 생성자
         * @param type 타입
         * @param title 회원ID (이름과 혼동 주의)
         * @param content 내용
         * @param imgProfile 프로필이미지
         * @param imgReview 리뷰이미지
         * @param rating 별점
         */
        public Item(int type, String title, String content, String imgProfile, String imgReview, double rating) {
            this.type = type;
            this.content = content;
            this.imgReview = imgReview;
            this.title = title;
            this.imgProfile = imgProfile;
            this.rating = rating;
        }
    } // end of class Item
}