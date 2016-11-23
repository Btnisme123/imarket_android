package com.example.framgia.imarketandroid.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.framgia.imarketandroid.R;
import com.example.framgia.imarketandroid.data.FakeContainer;
import com.example.framgia.imarketandroid.data.listener.OnRecyclerItemInteractListener;
import com.example.framgia.imarketandroid.data.model.CartItem;
import com.example.framgia.imarketandroid.data.model.CommerceCanter;
import com.example.framgia.imarketandroid.data.model.DrawerItem;
import com.example.framgia.imarketandroid.data.model.Session;
import com.example.framgia.imarketandroid.data.model.UserModel;
import com.example.framgia.imarketandroid.ui.adapter.HistoryTimeAdapter;
import com.example.framgia.imarketandroid.ui.adapter.RecyclerDrawerAdapter;
import com.example.framgia.imarketandroid.ui.adapter.RecyclerMarketAdapter;
import com.example.framgia.imarketandroid.ui.widget.LinearItemDecoration;
import com.example.framgia.imarketandroid.util.Constants;
import com.example.framgia.imarketandroid.util.DialogShareUtil;
import com.example.framgia.imarketandroid.util.Flog;
import com.example.framgia.imarketandroid.util.SharedPreferencesUtil;
import com.example.framgia.imarketandroid.util.findpath.InternetUtil;
import com.example.framgia.imarketandroid.util.findpath.LoadDataUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by hoavt on 11/10/2016.
 */
public class ChooseMarketActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        SearchView.OnQueryTextListener, OnRecyclerItemInteractListener, TextWatcher {
    static Realm myRealm;
    private RecyclerMarketAdapter mMarketAdapter;
    private List<CommerceCanter> mTraceMarkets = new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerMarket;
    private CursorAdapter mSearchSuggestionAdapter;
    private TextView mTextEmail;
    private List<DrawerItem> mDrawerItems = new ArrayList<>();
    private RecyclerView mRecyclerDrawer;
    private RecyclerDrawerAdapter mRecyclerDrawerAdapter;
    private View mStrokeLine1, mStrokeLine2, mStrokeLine3;
    private View mLinearMenu;
    private TextView mTextSignIn, mTextSignOut, mTextProfile;
    private TextView mTextUsername;
    private CircleImageView mCircleImageView;
    private List<CartItem> mCartItems = new ArrayList<>();
    private List<String> mHeaderNames = new ArrayList<>();
    private HistoryTimeAdapter mHistoryTimeAdapter;
    private RelativeLayout mReFavorite, mReBound, mReFollow;
    private AutoCompleteTextView mTextViewSearchInput;
    private LinearLayoutCompat mButtonSearch;
    private List<CommerceCanter> mListChooseCenter = new ArrayList<>();
    private boolean mFlag;
    private CheckBox mCheckBoxNearMarket;
    private Button mButtonClearInput;
    private FloatingActionButton mFABSynchronizeMarket;
    private LoadDataUtils mDataUtils;
    private List<CommerceCanter> mMarkets = new ArrayList<>();
    private List<String> mListAutoSearch = new ArrayList<>();
    private boolean mFlagCacheCommerce;
    private List<CommerceCanter> mListComAdap = new ArrayList<>();

    public void initDataAutoCompleteTextView() {
        mListAutoSearch.clear();
        if (mMarkets == null || mMarkets.size() == 0) {
            mListAutoSearch.add(Constants.COMMERCE_CENTER);
        } else {
            for (CommerceCanter center : mMarkets) {
                mListAutoSearch.add(center.getName());
                mListAutoSearch.add(center.getAddress());
            }
        }
    }

    public void cacheCommerce(Context context) {
        myRealm = Realm.getInstance(
                new RealmConfiguration.Builder(context)
                        .name(Constants.COM_CACHE)
                        .deleteRealmIfMigrationNeeded()
                        .build());
        myRealm.beginTransaction();
        if (mMarkets != null) {
            for (CommerceCanter center : mMarkets) {
                  myRealm.copyToRealmOrUpdate(center);
            }
            mListComAdap.clear();
            mListComAdap.addAll(mMarkets);
        }
        myRealm.commitTransaction();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_market);
        findViews();
        setListeners();
        supportActionBar();
        setSearchSuggestionAdapter();
        initRececlerView();
    }

    private void initRececlerView() {
        // recyclerview navigation drawer
        mRecyclerDrawer.setLayoutManager(new LinearLayoutManager(this));
        getRecycleFavorite();
        mRecyclerDrawerAdapter.setOnClick(this);
        setListeners();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mRecyclerMarket.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerMarket.addItemDecoration(new LinearItemDecoration(this));
        if (!mFlagCacheCommerce) {
            mDataUtils.loadCommerce(ChooseMarketActivity.this, mMarkets);
        } else {
            getCommerceFromCache();
        }
        mListComAdap.clear();
        mListComAdap.addAll(mMarkets);
        mMarketAdapter = new RecyclerMarketAdapter(this, mListComAdap);
        mTraceMarkets.addAll(mMarkets);
        mRecyclerMarket.setAdapter(mMarketAdapter);
        mMarketAdapter.setOnRecyclerItemInteractListener(this);
    }

    private void supportActionBar() {
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setSearchSuggestionAdapter() {
        final String[] columns = new String[]{Constants.MARKET_SUGGESTION};
        final int[] displayViews = new int[]{android.R.id.text1};
        mSearchSuggestionAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                columns,
                displayViews,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        getInfo();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View view) {
        if (InternetUtil.isInternetConnected(ChooseMarketActivity.this)) {
            switch (view.getId()) {
                case R.id.button_favorite:
                    getRecycleFavorite();
                    getVisible(mStrokeLine1, mStrokeLine2, mStrokeLine3, mLinearMenu);
                    DialogShareUtil.getSmallBang(ChooseMarketActivity.this, mReFavorite);
                    break;
                case R.id.button_bought:
                    DialogShareUtil.getSmallBang(ChooseMarketActivity.this, mReBound);
                    getVisible(mStrokeLine2, mStrokeLine1, mStrokeLine3, mLinearMenu);
                    mHeaderNames = FakeContainer.getListHeader();
                    mCartItems = FakeContainer.getListCartItem();
                    if (mHistoryTimeAdapter == null) {
                        mHistoryTimeAdapter =
                                new HistoryTimeAdapter(mHeaderNames, mCartItems, this);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                        mRecyclerDrawer.setLayoutManager(linearLayoutManager);
                        mRecyclerDrawer.setAdapter(mHistoryTimeAdapter);
                    } else {
                        mRecyclerDrawer.setAdapter(mHistoryTimeAdapter);
                    }
                    break;
                case R.id.button_follow:
                    DialogShareUtil.getSmallBang(ChooseMarketActivity.this, mReFollow);
                    getVisible(mStrokeLine3, mStrokeLine2, mStrokeLine1, mLinearMenu);
                    break;
                case R.id.button_more:
                    if (mLinearMenu.getVisibility() == View.GONE) {
                        mLinearMenu.setVisibility(View.VISIBLE);
                    } else {
                        mLinearMenu.setVisibility(View.GONE);
                    }
                    break;
                case R.id.button_sign_in:
                    mLinearMenu.setVisibility(View.GONE);
                    startActivity(new Intent(this, LoginActivity.class));
                    getInfo();
                    break;
                case R.id.button_sign_out:
                    SharedPreferencesUtil.getInstance().init(this, Constants.PREFS_NAME);
                    Session session = (Session) SharedPreferencesUtil
                            .getInstance()
                            .getValue(Constants.SESSION, Session.class);
                    if (session != null) {
                        actionSignout();
                    } else {
                        DialogShareUtil
                                .toastDialogMessage(getString(R.string.signout_fails_message),
                                        ChooseMarketActivity.this);
                    }
                    mLinearMenu.setVisibility(View.GONE);
                    break;
                case R.id.button_profile:
                    SharedPreferencesUtil.getInstance().init(this, Constants.PREFS_NAME);
                    Session session2 = (Session) SharedPreferencesUtil
                            .getInstance()
                            .getValue(Constants.SESSION, Session.class);
                    if (session2 != null)
                        startActivity(new Intent(this, UpdateProfileActivity.class));
                    else {
                        mLinearMenu.setVisibility(View.GONE);
                        startActivity(new Intent(this, LoginActivity.class));
                        getInfo();
                    }
                    break;
                case R.id.image_avatar:
                    mLinearMenu.setVisibility(View.GONE);
                    break;
                case R.id.acib_search_action:
                    clickSearch();
                    hideKeyboard();
                    break;
                case R.id.actv_search_input:
                    mTextViewSearchInput.setAdapter(new ArrayAdapter<>(this,
                            android.R.layout.simple_list_item_1, mListAutoSearch));
                    initDataAutoCompleteTextView();
                    showKeyboard();
                    break;
                case R.id.checkbox_near_market:
                    if (mCheckBoxNearMarket.isChecked()) {
                        //TODO show commerce center list by location
                    }
                    break;
                case R.id.button_clear_input:
                    mTextViewSearchInput.setText("");
                    break;
                case R.id.fab_synchronize:
                    mDataUtils.loadCommerce(ChooseMarketActivity.this, mMarkets);
                    synChronizeData();
                    break;
                default:
                    break;
            }
        } else {
            Flog.toast(this, R.string.no_internet);
        }
    }

    private void showKeyboard() {
        mTextViewSearchInput.setFocusable(true);
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mTextViewSearchInput, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        InputMethodManager inputManager =
                (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        populateSuggestionAdapter(newText);
        return false;
    }

    private void findViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_choose_commerce);
        mRecyclerMarket = (RecyclerView) findViewById(R.id.recycler_market);
        mRecyclerDrawer = (RecyclerView) findViewById(R.id.recycler_navigation_drawer);
        mStrokeLine1 = findViewById(R.id.nav_drawer_stroke_1);
        mStrokeLine2 = findViewById(R.id.nav_drawer_stroke_2);
        mStrokeLine3 = findViewById(R.id.nav_drawer_stroke_3);
        mLinearMenu = findViewById(R.id.linear_menu);
        mTextProfile = (TextView) findViewById(R.id.button_profile);
        mTextSignIn = (TextView) findViewById(R.id.button_sign_in);
        mTextSignOut = (TextView) findViewById(R.id.button_sign_out);
        mTextUsername = (TextView) findViewById(R.id.text_user_name);
        mTextEmail = (TextView) findViewById(R.id.text_email);
        mCircleImageView = (CircleImageView) findViewById(R.id.image_avatar);
        mReFavorite = (RelativeLayout) findViewById(R.id.button_favorite);
        mReBound = (RelativeLayout) findViewById(R.id.button_bought);
        mReFollow = (RelativeLayout) findViewById(R.id.button_follow);
        mTextViewSearchInput = (AutoCompleteTextView) findViewById(R.id.actv_search_input);
        mTextViewSearchInput.setHint(getString(R.string.input_name));
        mButtonSearch = (LinearLayoutCompat) findViewById(R.id.acib_search_action);
        mCheckBoxNearMarket = (CheckBox) findViewById(R.id.checkbox_near_market);
        mButtonClearInput = (Button) findViewById(R.id.button_clear_input);
        mFABSynchronizeMarket = (FloatingActionButton) findViewById(R.id.fab_synchronize);
        mDataUtils = new LoadDataUtils();
//        mDataUtils.init(this);
    }

    private void setListeners() {
        findViewById(R.id.button_favorite).setOnClickListener(this);
        findViewById(R.id.button_bought).setOnClickListener(this);
        findViewById(R.id.button_follow).setOnClickListener(this);
        findViewById(R.id.button_more).setOnClickListener(this);
        mTextProfile.setOnClickListener(this);
        mTextSignIn.setOnClickListener(this);
        mTextSignOut.setOnClickListener(this);
        mReFavorite.setOnClickListener(this);
        mReBound.setOnClickListener(this);
        mReFollow.setOnClickListener(this);
        mTextViewSearchInput.addTextChangedListener(this);
        mButtonSearch.setOnClickListener(this);
        mCheckBoxNearMarket.setOnClickListener(this);
        mButtonClearInput.setOnClickListener(this);
        mFABSynchronizeMarket.setOnClickListener(this);
    }

    private void populateSuggestionAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID,
                Constants.MARKET_SUGGESTION});
        int length = FakeContainer.SUGGESTIONS.length;
        for (int i = 0; i < length; i++) {
            if (FakeContainer.SUGGESTIONS[i].toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[]{i, FakeContainer.SUGGESTIONS[i]});
            mSearchSuggestionAdapter.changeCursor(c);
        }
    }

    private void getInfo() {
        SharedPreferencesUtil.getInstance().init(this, Constants.PREFS_NAME);
        UserModel userModel = (UserModel) SharedPreferencesUtil
                .getInstance()
                .getValue(Constants.SESSION, UserModel.class);
        if (userModel != null && userModel.getSession() != null) {
            if (userModel.getSession().getFullname() != null) {
                mTextUsername.setText(userModel.getSession().getFullname().toString());
            }
            if (userModel.getSession().getUsername() != null) {
                mTextEmail.setText(userModel.getSession().getUsername().toString());
            }
            if (userModel.getSession().getUrlImage() != null) {
                String url =
                        Constants.HEAD_URL + userModel.getSession().getUrlImage();
                Glide.with(this).load(url).into(mCircleImageView);
            } else {
                mCircleImageView.setImageResource(R.drawable.ic_framgia);
            }
        } else {
            mTextUsername.setText(R.string.login_hint_username);
            mTextEmail.setText(R.string.email);
            mCircleImageView.setImageResource(R.drawable.ic_framgia);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLinearMenu.setVisibility(View.GONE);
        getInfo();
    }

    private void actionSignout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.noti);
        builder.setMessage(R.string.confirm_signout);
        builder
                .setPositiveButton(R.string.ok_dialog_success, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferencesUtil.getInstance().clearSharedPreference();
                        dialog.dismiss();
                        Toast.makeText(ChooseMarketActivity.this,
                                getString(R.string.signout_done_message), Toast.LENGTH_SHORT).show();
                        getInfo();
                    }
                });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
        SharedPreferencesUtil.getInstance().clearSharedPreference();
    }

    public void getVisible(View view1, View view2, View view3, View view4) {
        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
        view4.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(View v, int position) {
        switch (v.getId()) {
            case R.id.image_btn_delete:
                mDrawerItems.remove(position);
                mRecyclerDrawerAdapter.notifyDataSetChanged();
                break;
            case R.id.linear_navigation_drawer:
                startActivity(new Intent(this, DetailsProductActivity.class));
                break;
            case R.id.item_recycler_market:
                mLinearMenu.setVisibility(View.GONE);
                Intent intent = new Intent(this, FloorActivity.class);
                intent.putExtra(Constants.COMMERCE_INTENT, mListComAdap.get(position));
                startActivity(intent);
                break;
        }
    }

    private void getRecycleFavorite() {
        mDrawerItems = FakeContainer.initDrawerItems();
        mRecyclerDrawerAdapter = new RecyclerDrawerAdapter(this, mDrawerItems);
        mRecyclerDrawer.setAdapter(mRecyclerDrawerAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDataUtils.mReceiver != null) {
            unregisterReceiver(mDataUtils.mReceiver);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (!s.toString().isEmpty() && !mFlag) {
                mTextViewSearchInput.setAdapter(new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, mListAutoSearch));
            initDataAutoCompleteTextView();
            mFlag = true;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private void clickSearch() {
        String textSearch = mTextViewSearchInput.getText().toString();
        if (!textSearch.isEmpty()) {
            mListChooseCenter.clear();
            for (CommerceCanter center : mMarkets) {
                if (center.getName().equalsIgnoreCase(textSearch) ||
                    center.getAddress().equalsIgnoreCase(textSearch)) {
                    mListChooseCenter.add(center);
                }
            }
            mListComAdap.clear();
            mListComAdap.addAll(mListChooseCenter);
            mMarketAdapter.notifyDataSetChanged();
            mMarketAdapter.setOnRecyclerItemInteractListener(this);
        } else {
            mListComAdap.clear();
            mListComAdap.addAll(mMarkets);
            mMarketAdapter.notifyDataSetChanged();
            mMarketAdapter.setOnRecyclerItemInteractListener(this);
        }
    }

    private void getCommerceFromCache() {
        RealmResults<CommerceCanter> resultsCache =
                myRealm.where(CommerceCanter.class).findAll();
        mMarkets.clear();
        mListComAdap.clear();
        mMarkets.addAll(resultsCache);
        mListComAdap.addAll(resultsCache);
    }

    private void synChronizeData() {
        initDataAutoCompleteTextView();
        cacheCommerce(ChooseMarketActivity.this);
        mFlagCacheCommerce = true;
        mListComAdap.clear();
        mListComAdap.addAll(mMarkets);
        mMarketAdapter.notifyDataSetChanged();
        mMarketAdapter.setOnRecyclerItemInteractListener(ChooseMarketActivity.this);
    }

}
