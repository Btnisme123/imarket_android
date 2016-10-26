package com.example.framgia.imarketandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.framgia.imarketandroid.R;
import com.example.framgia.imarketandroid.data.model.Category;
import com.example.framgia.imarketandroid.data.remote.RealmRemote;
import com.example.framgia.imarketandroid.ui.adapter.ViewPagerAdapter;
import com.example.framgia.imarketandroid.ui.fragments.CategoryStallFragment;
import com.example.framgia.imarketandroid.ui.fragments.NoConnectFragment;
import com.example.framgia.imarketandroid.ui.fragments.SaleOffEventFragment;
import com.example.framgia.imarketandroid.ui.fragments.ShopDetailInterfaceFragment;
import com.example.framgia.imarketandroid.ui.fragments.SuggestStoreFragment;
import com.example.framgia.imarketandroid.util.Constants;
import com.example.framgia.imarketandroid.util.DialogShareUtil;
import com.example.framgia.imarketandroid.util.Flog;
import com.example.framgia.imarketandroid.util.findpath.InternetUtil;
import com.facebook.CallbackManager;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

/**
 * Created by toannguyen201194 on 06/09/2016.
 */
public class HomeStoreActivity extends AppCompatActivity implements SearchView
        .OnQueryTextListener, View.OnClickListener, NoConnectFragment.OnClickToLoadConnect {
    private final String NAMESTORE = "Apple Store";
    private TabLayout mTabLayout;
    private ViewPager mViewPagerStore;
    private FloatingActionButton mFabShare, mFabMessage, mFabFollow;
    private Toolbar mToolbar;
    private CallbackManager mCallback = CallbackManager.Factory.create();
    private SuggestStoreFragment mSuggestStoreFragment;
    private ShopDetailInterfaceFragment mShopDetailInterfaceFragment;
    private ViewPagerAdapter mPagerAdapter;
    private boolean mIsCached;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_store);
        initView();
        // TODO: 29/08/2016  remove badge
        ShortcutBadger.removeCount(this);
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tab_home_store);
        mViewPagerStore = (ViewPager) findViewById(R.id.view_home_store);
        mFabFollow = (FloatingActionButton) findViewById(R.id.fab_follow);
        mFabMessage = (FloatingActionButton) findViewById(R.id.fab_message);
        mFabShare = (FloatingActionButton) findViewById(R.id.fab_share);
        mFabFollow.setOnClickListener(this);
        mFabMessage.setOnClickListener(this);
        mFabShare.setOnClickListener(this);
        setupViewPager(mViewPagerStore);
        mTabLayout.setupWithViewPager(mViewPagerStore);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(NAMESTORE);
    }

    public void setupViewPager(ViewPager viewPager) {
        mPagerAdapter = new ViewPagerAdapter( getSupportFragmentManager());
        if (InternetUtil.isInternetConnected(HomeStoreActivity.this)) {
            mPagerAdapter.addFragment(new CategoryStallFragment(),
                    getString(R.string.title_fragment_Category));
            mPagerAdapter.addFragment(new SaleOffEventFragment(),
                    getString(R.string.title_fragment_saleoffevent));
            mSuggestStoreFragment = new SuggestStoreFragment();
            mPagerAdapter.addFragment(mSuggestStoreFragment, getString(R.string.title_fragment_rate));
            mShopDetailInterfaceFragment = new ShopDetailInterfaceFragment(mCallback);
            mPagerAdapter.addFragment(mShopDetailInterfaceFragment,
                    getString(R.string.title_fragment_informationstore));
            viewPager.setAdapter(mPagerAdapter);
        } else {
            Flog.toast(this, R.string.no_internet);
                mPagerAdapter.addFragment(new NoConnectFragment(HomeStoreActivity.this),
                        getString(R.string.title_fragment_Category));
            mPagerAdapter.addFragment(new NoConnectFragment(HomeStoreActivity.this),
                    getString(R.string.title_fragment_saleoffevent));
            mPagerAdapter.addFragment(new NoConnectFragment(HomeStoreActivity.this),
                    getString(R.string.title_fragment_rate));
            mPagerAdapter.addFragment(new NoConnectFragment(HomeStoreActivity.this),
                    getString(R.string.title_fragment_informationstore));
            viewPager.setAdapter(mPagerAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                View searchView = findViewById(R.id.item_search);
                View cartView = findViewById(R.id.item_cart);
                ShowcaseConfig config = new ShowcaseConfig();
                config.setDelay(Constants.TIME_DELAY_GUIDE);
                MaterialShowcaseSequence sequence = new MaterialShowcaseSequence
                        (HomeStoreActivity.this,
                                Constants.SHOWCASE_ID_HOME);
                sequence.setConfig(config);
                sequence.addSequenceItem(searchView,
                        getString(R.string.sequence_search),
                        Constants.GOT_IT);
                sequence.addSequenceItem(cartView,
                        getString(R.string.sequence_cart),
                        Constants.GOT_IT);
                sequence.start();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_cart:
                startActivity(new Intent(HomeStoreActivity.this, CartActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        List<Category> list = filter(CategoryStallFragment.sCategoryProducts, query);
        CategoryStallFragment.sCategoryStallAdapter.animateTo(list);
        return true;
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
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        mCallback.onActivityResult(requestCode, responseCode, data);
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab_follow:
                Toast.makeText(this, R.string.toast_follow, Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_message:
                startActivity(new Intent(this, BookTableActivity.class));
                break;
            case R.id.fab_share:
                DialogShareUtil.dialogShare(this, R.drawable.ic_iphone5s, mCallback);
                break;
        }
    }

    @Override
    public void onClickLoadConnect() {
        finish();
        startActivity(getIntent());
    }
}