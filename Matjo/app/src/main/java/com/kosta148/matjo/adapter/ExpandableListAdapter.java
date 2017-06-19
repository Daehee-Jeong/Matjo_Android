package com.kosta148.matjo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosta148.matjo.R;
import com.kosta148.matjo.util.RoundedDrawable;

import java.util.ArrayList;
import java.util.List;

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

    public ExpandableListAdapter(List<Item> data, Context context) {
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
                view = inflater.inflate(R.layout.list_header, parent, false);
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
                view = inflater.inflate(R.layout.list_child, parent, false);
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
                headerViewHolder.header_profile.setImageDrawable(rd);

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
                childViewHolder.child_content.setText(item.content);
                childViewHolder.child_img.setImageResource(item.imgReview);
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
        public ImageView btn_expand_toggle;
        public ImageView header_profile;
        public Item refferalItem;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            header_title = (TextView) itemView.findViewById(R.id.header_title);
            header_profile = (ImageView) itemView.findViewById(R.id.header_profile);
            btn_expand_toggle = (ImageView) itemView.findViewById(R.id.btn_expand_toggle);
        }
    } // end of class

    // 자식 뷰홀더
    private static class ListChildViewHolder extends RecyclerView.ViewHolder {
        public TextView child_content;
        public ImageView child_img;
        public Item refferalItem;

        public ListChildViewHolder(View itemView) {
            super(itemView);
            child_content = (TextView) itemView.findViewById(R.id.child_content);
            child_img = (ImageView) itemView.findViewById(R.id.child_img);
        }
    } // end of class

    public static class Item {
        public int type;
        public String title;
        public int imgProfile;
        public int imgReview;
        public String content;
        public double rating;
        public List<Item> invisibleChildren;

        public Item() {
        }

        // 헤더 아이템일 경우 사용하는 생성자
        public Item(int type, String title, int imgResId, double rating) {
            this.type = type;
            this.title = title;
            this.imgProfile = imgResId;
            this.rating = rating;
        }

        // 자식 아이템일 경우 사용하는 생성자
        public Item(int type, String content, int imgReview) {
            this.type = type;
            this.content = content;
            this.imgReview = imgReview;
        }
    } // end of class Item
} // end of class Adapter
