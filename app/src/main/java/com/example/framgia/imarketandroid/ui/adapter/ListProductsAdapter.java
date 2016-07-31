package com.example.framgia.imarketandroid.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.framgia.imarketandroid.R;
import com.example.framgia.imarketandroid.models.ItemProduct;
import com.example.framgia.imarketandroid.util.OnRecyclerItemInteractListener;

import java.util.ArrayList;

/**
 * Created by hoavt on 20/07/2016.
 */
public class ListProductsAdapter extends RecyclerView.Adapter<ListProductsAdapter.ViewHolder> {
    public static final String NO_PROMOTION = "0%";
    private ArrayList<ItemProduct> mItems = new ArrayList<>();
    private Context mContext;
    private OnRecyclerItemInteractListener mListener;

    public void setOnRecyclerItemInteractListener(OnRecyclerItemInteractListener mOnRecyclerItemInteractListener) {
        this.mListener = mOnRecyclerItemInteractListener;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListProductsAdapter(Context context, ArrayList<ItemProduct> myItems) {
        mContext = context;
        mItems = myItems;
    }

    public void setItems(ArrayList<ItemProduct> items) {
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
    public void onBindViewHolder(ListProductsAdapter.ViewHolder holder, final int position) {
        ItemProduct itemProduct = mItems.get(position);
        ImageView ivPresentIcon = holder.imagePresentIcon;
        ivPresentIcon.setImageResource(itemProduct.getPresentIcon());
        TextView nameProduct = holder.textNameProduct;
        nameProduct.setText(itemProduct.getNameProduct());
        FrameLayout promotionView = holder.promotionView;
        TextView percentSale = holder.percentSale;
        percentSale.setText(itemProduct.getPromotionPercent());
        if (itemProduct.getPromotionPercent().equals(NO_PROMOTION)) {
            promotionView.setVisibility(View.GONE);
        } else {
            promotionView.setVisibility(View.VISIBLE);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public ImageView imagePresentIcon;
        public TextView textNameProduct;
        public FrameLayout promotionView;
        public TextView percentSale;

        public ViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View parentView) {
            imagePresentIcon = (ImageView) parentView.findViewById(R.id.iv_present_icon);
            textNameProduct = (TextView) parentView.findViewById(R.id.tv_name_product);
            promotionView = (FrameLayout) parentView.findViewById(R.id.fl_promotion_view);
            percentSale = (TextView) parentView.findViewById(R.id.tv_percent_sale);
            cardView = (CardView) parentView.findViewById(R.id.cardview_product);
        }
    }
}
