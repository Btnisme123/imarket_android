package com.example.framgia.imarketandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.framgia.imarketandroid.R;
import com.example.framgia.imarketandroid.data.Category;
import com.example.framgia.imarketandroid.ui.adapter.CategoryStallAdapter;
import com.example.framgia.imarketandroid.ui.widget.GridItemDecoration;
import com.example.framgia.imarketandroid.util.Constants;
import com.example.framgia.imarketandroid.util.HttpRequest;
import com.example.framgia.imarketandroid.util.OnRecyclerItemInteractListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryStallActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, HttpRequest.OnLoadDataListener, OnRecyclerItemInteractListener {

    private final int SCROLL_POSITION = 0;
    private RecyclerView mRecyclerView;
    private CategoryStallAdapter mCategoryStallAdapter;
    private List<Category> mCategoryProducts;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_stall);
        findView();
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    public void findView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_category);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
    }

    public void init() {
        mCategoryProducts = new ArrayList<>();
        mCategoryStallAdapter = new CategoryStallAdapter(mCategoryProducts);
        mRecyclerView.setAdapter(mCategoryStallAdapter);
        mCategoryStallAdapter.setOnRecyclerItemInteractListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, Constants.NUMBER_OF_COLUMN));
        mRecyclerView.addItemDecoration(new GridItemDecoration(this));
        HttpRequest.getInstance().init();
        HttpRequest.getInstance().setOnLoadDataListener(this);
        HttpRequest.getInstance().loadCategories();
    }

    private List<Category> filter(List<Category> categoryProducts, String query) {
        query = query.toLowerCase();
        List<Category> filterList = new ArrayList<>();
        int size = categoryProducts.size();
        for (int i = 0; i < size; i++) {
            Category categoryProduct = categoryProducts.get(i);
            String categoryName = categoryProduct.getName().toLowerCase();
            if (categoryName.contains(query)) {
                filterList.add(categoryProduct);
            }
        }
        return filterList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        List<Category> list = filter(mCategoryProducts, query);
        mCategoryStallAdapter.animateTo(list);
        mRecyclerView.scrollToPosition(SCROLL_POSITION);
        return true;
    }

    @Override
    public void onLoadDataSuccess(Object object) {
        if (object != null) {
            mCategoryProducts = (List<Category>) object;
            mCategoryStallAdapter.swapList(mCategoryProducts);
        }
    }

    @Override
    public void onLoadDataFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
        startActivity(new Intent(this, ListProductsActivity.class));
    }
}
