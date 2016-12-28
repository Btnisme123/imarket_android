package com.example.framgia.imarketandroid.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.framgia.imarketandroid.R;
import com.example.framgia.imarketandroid.data.listener.OnRecyclerItemInteractListener;
import com.example.framgia.imarketandroid.data.model.Category;
import com.example.framgia.imarketandroid.data.remote.RealmRemote;
import com.example.framgia.imarketandroid.ui.activity.ListProductsActivity;
import com.example.framgia.imarketandroid.ui.adapter.CategoryStallAdapter;
import com.example.framgia.imarketandroid.ui.widget.GridItemDecoration;
import com.example.framgia.imarketandroid.util.Constants;
import com.example.framgia.imarketandroid.util.Flog;
import com.example.framgia.imarketandroid.util.HttpRequest;

import java.util.ArrayList;
import java.util.List;

public class CategoryStallFragment extends Fragment implements
    HttpRequest.OnLoadDataListener, OnRecyclerItemInteractListener {
    public static CategoryStallAdapter sCategoryStallAdapter;
    public static List<Category> sCategoryProducts;
    private RecyclerView mRecyclerView;
    private View mView;
    private int mStoreId = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return mView = inflater.inflate(R.layout.fragment_category_stall, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState==null){
            super.onViewCreated(view, savedInstanceState);
            findView();
            init();
        }
    }

    public void findView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_category);
    }

    public void init() {
        sCategoryProducts = new ArrayList<>();
        sCategoryStallAdapter = new CategoryStallAdapter(sCategoryProducts, getActivity());
        addItem(sCategoryProducts, RealmRemote.getListCategory());
        sCategoryStallAdapter.addAll(RealmRemote.getListCategory());
        mRecyclerView.setAdapter(sCategoryStallAdapter);
        sCategoryStallAdapter.setOnRecyclerItemInteractListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.addItemDecoration(new GridItemDecoration(getContext()));
        getData();
    }

    public void getData() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            HttpRequest.getInstance(getActivity().getBaseContext()).init();
            HttpRequest.getInstance(getActivity().getBaseContext())
                .initProgressDialog(getActivity());
            HttpRequest.getInstance(getActivity().getBaseContext()).loadCategories(mStoreId);
            HttpRequest.getInstance(getActivity().getBaseContext()).setOnLoadDataListener(this);
        }
    }

    private void addItem(List<Category> mainList, List<Category> subList) {
        mainList.clear();
        mainList.addAll(subList);
    }

    @Override
    public void onLoadDataSuccess(Object object) {
        if (object != null) {
            sCategoryProducts = (List<Category>) object;
            RealmRemote.saveCategories(sCategoryProducts);
            sCategoryStallAdapter.addAll(sCategoryProducts);
        }
    }

    @Override
    public void onLoadDataFailure(String message) {
        List<Category> categories = RealmRemote.getListCategory();
        if (categories != null && categories.size() != 0) {
            sCategoryStallAdapter.addAll(categories);
        }
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), ListProductsActivity.class);
        if (sCategoryProducts.size() != 0) {
            Category category = sCategoryProducts.get(position);
            int id = Integer.parseInt(category.getId());
            if (category != null) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.KeyIntent.CATEGORY_INTENT, id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }
}
