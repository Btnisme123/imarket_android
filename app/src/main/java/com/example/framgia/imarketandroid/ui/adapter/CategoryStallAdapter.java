package com.example.framgia.imarketandroid.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.framgia.imarketandroid.R;
import com.example.framgia.imarketandroid.data.listener.OnRecyclerItemInteractListener;
import com.example.framgia.imarketandroid.data.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VULAN on 7/20/2016.
 */
public class CategoryStallAdapter
        extends RecyclerView.Adapter<CategoryStallAdapter.CategoryHolder> {
    private List<Category> mCategoryProducts;
    private OnRecyclerItemInteractListener mListener;
    Context mContext;

    public CategoryStallAdapter(List<Category> mCategoryProducts, Context context) {
        this.mCategoryProducts = new ArrayList<>(mCategoryProducts);
        this.mContext = context;
    }

    public void setOnRecyclerItemInteractListener(OnRecyclerItemInteractListener listener) {
        mListener = listener;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, final int position) {
        Category categoryProduct = mCategoryProducts.get(position);
        holder.textView.setText(categoryProduct.getName());
        Glide.with(mContext).load(categoryProduct.getImageLink()).into(holder.imageView);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategoryProducts == null ? 0 : mCategoryProducts.size();
    }

    public Category removeItem(int position) {
        final Category category = mCategoryProducts.remove(position);
        notifyItemRemoved(position);
        return category;
    }

    public void addItem(int position, Category model) {
        mCategoryProducts.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Category category = mCategoryProducts.remove(fromPosition);
        mCategoryProducts.add(toPosition, category);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void addAll(List<Category> list) {
        mCategoryProducts.addAll(list);
        notifyDataSetChanged();
    }

    public void clearList() {
        mCategoryProducts.clear();
        notifyDataSetChanged();
    }

    private void applyAndAnimateRemovals(List<Category> categoryProducts) {
        int size = mCategoryProducts.size();
        for (int i = size - 1; i >= 0; i--) {
            Category category = mCategoryProducts.get(i);
            if (!categoryProducts.contains(category)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAddition(List<Category> categoryProducts) {
        for (int i = 0, count = categoryProducts.size(); i < count; i++) {
            Category categoryProduct = categoryProducts.get(i);
            if (!mCategoryProducts.contains(categoryProduct)) {
                addItem(i, categoryProduct);
            }
        }
    }

    private void applyAndAnimateMoveItems(List<Category> categoryProducts) {
        int size = categoryProducts.size();
        for (int toPosition = size - 1; toPosition >= 0; toPosition--) {
            Category category = categoryProducts.get(toPosition);
            int fromPosition = mCategoryProducts.indexOf(category);
            if (fromPosition != 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public void animateTo(List<Category> list) {
        applyAndAnimateAddition(list);
        applyAndAnimateMoveItems(list);
        applyAndAnimateRemovals(list);
    }

    class CategoryHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public LinearLayout linearLayout;

        public CategoryHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_category);
            textView = (TextView) itemView.findViewById(R.id.text_category);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearlayout_category);
        }
    }
}
