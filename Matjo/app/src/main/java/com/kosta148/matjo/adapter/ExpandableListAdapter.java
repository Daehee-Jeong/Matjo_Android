package com.kosta148.matjo.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kosta148.matjo.R;
import com.kosta148.matjo.util.RoundedDrawable;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by anandbose on 09/06/15.
 */
public class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    public static final int HEADER = 0;
    public static final int CHILD = 1;

    private List<Item> data;
    Bitmap bm;
    RoundedDrawable rd;

    boolean isMember = false;
    boolean hasWritten = false;

    Handler handler = new Handler();

    // SharedPreferences 선언
    private SharedPreferences sharedPreferences;
    // SharedPreferences 키 상수
    private static final String SHAREDPREFERENCES_MEMBER_NAME = "memberName";
    private String loginName = "";

    private ItemClick itemClick;
    public interface ItemClick {
        public void onClick(View view,int position);
    }

    //아이템 클릭시 실행 함수 등록 함수
    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public ExpandableListAdapter(List<Item> data, Context context, boolean isMember) {
        this.data = data;
        this.context = context;
        bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.profile);
        rd = new RoundedDrawable(bm);
        this.isMember = isMember;

        sharedPreferences = context.getSharedPreferences("LoginSetting.dat", MODE_PRIVATE);
        loginName = sharedPreferences.getString(SHAREDPREFERENCES_MEMBER_NAME, "");
        Log.d("MyLog", "loginName is <"+loginName+">");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = null;
        Context context = parent.getContext();
        float dp = context.getResources().getDisplayMetrics().density;
        int subItemPaddingLeft = (int) (18 * dp);
        int subItemPaddingTopAndBottom = (int) (5 * dp);

        // SharedPreferences 초기화

        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (type) {
            case HEADER:
                view = inflater.inflate(R.layout.list_header_group_review, parent, false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);

                if(isMember){
                    header.header_pereviw.setVisibility(View.VISIBLE);
                }

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
                view = inflater.inflate(R.layout.list_child_group_review, parent, false);
                ListChildViewHolder child = new ListChildViewHolder(view);
                return child;
        }

        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Item item = data.get(position);
        final int Position = position;
        switch (item.type) {
            case HEADER:
                final ListHeaderViewHolder headerViewHolder = (ListHeaderViewHolder) holder;
                headerViewHolder.refferalItem = item;
                headerViewHolder.header_title.setText(item.title);
//                headerViewHolder.header_profile.setImageDrawable(rd);
                Glide.with(context).load(item.imgProfile)
                        .bitmapTransform(new CropCircleTransformation(new CustomBitmapPool()))
                        .thumbnail(0.1f)
                        .error(R.drawable.ic_resta)
                        .into(headerViewHolder.header_profile);
                headerViewHolder.header_rating.setText(item.rating+"");
                headerViewHolder.ratingBarAvg.setRating((float) item.rating);

                // 회원일때만 개인리뷰 작성 / 기존 개인리뷰 작성 안했으면 작성
                List<Item> childList = data.get(position).invisibleChildren;
                hasWritten = false;
                // 개인 리뷰 작성했는지 여부 확인
                if (childList != null) {
                    for (int i = 0; i < childList.size(); i++) {
                        String itemContent = childList.get(i).content;
                        String memberName = childList.get(i).title;
                        Log.d("MyLog", "memberName is <" + memberName + ">");
                        if (loginName.equals(memberName)) {
                            Log.d("MyLog", "you have already written review " + position);
                            hasWritten = true;
                            break;
                        }
                    }
                }
                if(isMember && !hasWritten){
                    headerViewHolder.header_pereviw.setVisibility(View.VISIBLE);
                } else {
                    headerViewHolder.header_pereviw.setVisibility(View.INVISIBLE);
                }

                headerViewHolder.header_pereviw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemClick != null){
                            itemClick.onClick(v, Position);
                        }
                    }
                });

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
                final ExpandableListAdapter.ListChildViewHolder childViewHolder = (ExpandableListAdapter.ListChildViewHolder) holder;
                childViewHolder.refferalItem = item;

                childViewHolder.child_member_id.setText(item.title);
                childViewHolder.child_content.setText(item.content);
                childViewHolder.child_rating.setText(item.rating+"");
                childViewHolder.ratingBar.setRating((float) item.rating);

//                childViewHolder.child_img.setImageResource(item.imgReview);
                Glide.with(context).load(item.imgProfile)
                        .thumbnail(0.1f)
                        .error(R.drawable.default_profile)
                        .into(childViewHolder.child_profile);

                childViewHolder.child_img.setVisibility(View.VISIBLE);
                if (item.imgReview != null && !"".equals(item.imgReview)) {
                    Glide.with(context).load(item.imgReview)
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
        public Button header_pereviw;
        public Item refferalItem;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            header_title = (TextView) itemView.findViewById(R.id.header_title);
            header_profile = (ImageView) itemView.findViewById(R.id.header_profile);
            btn_expand_toggle = (ImageView) itemView.findViewById(R.id.btn_expand_toggle);
            header_rating = (TextView) itemView.findViewById(R.id.header_rating);
            ratingBarAvg = (RatingBar) itemView.findViewById(R.id.ratingBarAvg);
            header_pereviw = (Button) itemView.findViewById(R.id.btnInsertPereview);
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
        public Item(int type, String title, String content, String imgProfile, String imgReview, double rating) {
            this.type = type;
            this.content = content;
            this.imgReview = imgReview;
            this.title = title;
            this.imgProfile = imgProfile;
            this.rating = rating;
        }
    } // end of class Item

} // end of class Adapter
