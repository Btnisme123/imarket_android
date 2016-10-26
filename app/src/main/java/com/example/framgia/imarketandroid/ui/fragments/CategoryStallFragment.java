package com.example.framgia.imarketandroid.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.framgia.imarketandroid.util.HttpRequest;

import java.util.ArrayList;
import java.util.List;

public class CategoryStallFragment extends Fragment implements
        HttpRequest.OnLoadDataListener, OnRecyclerItemInteractListener {
    public static CategoryStallAdapter sCategoryStallAdapter;
    public static List<Category> sCategoryProducts;
    private RecyclerView mRecyclerView;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return mView = inflater.inflate(R.layout.fragment_category_stall, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView();
        init();
    }

    public void findView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_category);
    }

    public void init() {
        sCategoryProducts = new ArrayList<>();
        sCategoryStallAdapter = new CategoryStallAdapter(sCategoryProducts,getActivity());
        mRecyclerView.setAdapter(sCategoryStallAdapter);
        sCategoryStallAdapter.setOnRecyclerItemInteractListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.addItemDecoration(new GridItemDecoration(getContext()));
        getData();
    }

    public void getData() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            HttpRequest.getInstance().init();
            HttpRequest.getInstance().loadCategories();
            HttpRequest.getInstance().setOnLoadDataListener(this);
        }
    }

    @Override
    public void onLoadDataSuccess(Object object) {
        if (object != null) {
            sCategoryProducts = (List<Category>) object;
            sCategoryStallAdapter.addAll(sCategoryProducts);
        }
    }

    @Override
    public void onLoadDataFailure(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getContext(), ListProductsActivity.class);
        intent.putExtra(Constants.CATEGORY_INTENT, sCategoryProducts.get(position));
        startActivity(intent);
    }

}
