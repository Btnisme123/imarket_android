package com.example.framgia.imarketandroid.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.framgia.imarketandroid.R;
import com.example.framgia.imarketandroid.data.model.ItemProduct;
import com.example.framgia.imarketandroid.ui.activity.DetailsProductActivity;
import com.example.framgia.imarketandroid.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoavt on 20/07/2016.
 */
public class ListProductsAdapter extends RecyclerView.Adapter<ListProductsAdapter.ViewHolder> {
    public static final String NO_PROMOTION = Constants.NO_PERCENT;
    private List<ItemProduct> mItems = new ArrayList<>();
    private Context mContext;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListProductsAdapter(Context context, List<ItemProduct> myItems) {
        mContext = context;
        mItems = new ArrayList<>(myItems);
    }

    public void setItems(List<ItemProduct> items) {
        mItems = items;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_list_products_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ListProductsAdapter.ViewHolder holder, int position) {
        ItemProduct itemProduct = mItems.get(position);
        String imageLink = itemProduct.getImageLists().get(1).getPhotoLink();
        if (imageLink == null) {
            holder.mIvPresentIcon.setImageResource(R.drawable.ic_iphone5s);
        } else {
            Glide.with(mContext).load(imageLink).into(holder.mIvPresentIcon);
        }
        TextView nameProduct = holder.mTvNameProduct;
        nameProduct.setText(itemProduct.getNameProduct());
        FrameLayout promotionView = holder.mPromotionView;
        TextView percentSale = holder.mPercentSale;
        percentSale.setText(itemProduct.getPromotionPercent());
        if (itemProduct.getPromotionPercent().equals(NO_PROMOTION)) {
            promotionView.setVisibility(View.GONE);
        } else {
            promotionView.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailsProductActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    public ItemProduct removeItem(int position) {
        final ItemProduct itemProduct = mItems.remove(position);
        notifyItemRemoved(position);
        return itemProduct;
    }

    public void addItem(int position, ItemProduct model) {
        mItems.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final ItemProduct itemProduct = mItems.remove(fromPosition);
        mItems.add(toPosition, itemProduct);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void addAll(List<ItemProduct> list) {
        mItems.addAll(list);
        notifyDataSetChanged();
    }

    public void clearList() {
        mItems.clear();
        notifyDataSetChanged();
    }

    private void applyAndAnimateRemovals(List<ItemProduct> itemProducts) {
        int size = mItems.size();
        for (int i = size - 1; i >= 0; i--) {
            ItemProduct category = mItems.get(i);
            if (!itemProducts.contains(category)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAddition(List<ItemProduct> itemProducts) {
        int count = itemProducts.size();
        for (int i = 0; i < count; i++) {
            ItemProduct categoryProduct = itemProducts.get(i);
            if (!mItems.contains(categoryProduct)) {
                addItem(i, categoryProduct);
            }
        }
    }

    private void applyAndAnimateMoveItems(List<ItemProduct> itemProducts) {
        int size = itemProducts.size();
        for (int toPosition = size - 1; toPosition >= 0; toPosition--) {
            ItemProduct category = itemProducts.get(toPosition);
            int fromPosition = mItems.indexOf(category);
            if (fromPosition != 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public void animateTo(List<ItemProduct> list) {
        applyAndAnimateAddition(list);
        applyAndAnimateMoveItems(list);
        applyAndAnimateRemovals(list);
    }

    @Override
    public int getItemCount() {
        Log.e("", "number : " + mItems.size());
        return mItems == null ? 1 : mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIvPresentIcon;
        public TextView mTvNameProduct;
        public FrameLayout mPromotionView;
        public TextView mPercentSale;

        public ViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View parentView) {
            mIvPresentIcon = (ImageView) parentView.findViewById(R.id.iv_present_icon);
            mTvNameProduct = (TextView) parentView.findViewById(R.id.tv_name_product);
            mPromotionView = (FrameLayout) parentView.findViewById(R.id.fl_promotion_view);
            mPercentSale = (TextView) parentView.findViewById(R.id.tv_percent_sale);
        }
    }
}
