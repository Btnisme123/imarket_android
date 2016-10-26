package com.example.framgia.imarketandroid.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.framgia.imarketandroid.R;
import com.example.framgia.imarketandroid.data.FakeContainer;
import com.example.framgia.imarketandroid.data.model.Comment;
import com.example.framgia.imarketandroid.data.model.ItemBooking;
import com.example.framgia.imarketandroid.data.model.Showcase;
import com.example.framgia.imarketandroid.ui.adapter.BookProductAdapter;
import com.example.framgia.imarketandroid.ui.adapter.CommentStoreAdapter;
import com.example.framgia.imarketandroid.ui.adapter.PreviewDetailsAdapter;
import com.example.framgia.imarketandroid.ui.adapter.ScreenSlidePagerAdapter;
import com.example.framgia.imarketandroid.ui.views.CustomStarView;
import com.example.framgia.imarketandroid.util.Constants;
import com.example.framgia.imarketandroid.util.DialogShareUtil;
import com.example.framgia.imarketandroid.util.ShowcaseGuideUtil;
import com.example.framgia.imarketandroid.util.SystemUtil;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoavt on 22/07/2016.
 */
public class DetailsProductActivity extends AppCompatActivity
        implements BookProductAdapter.OnClickItemBarListenner,
        PreviewDetailsAdapter.OnClickShowPreviewDetail,
        CommentStoreAdapter.OnPreviewCommentListener,
        CustomStarView.onItemClickListener {
    private RecyclerView mRvPreviewProducts;
    private RecyclerView mRvBookingProducts;
    private RecyclerView.Adapter mPreviewAdapter;
    private RecyclerView.Adapter mBookingAdapter;
    private RecyclerView.LayoutManager mPreviewLayoutManager;
    private RecyclerView.LayoutManager mBookingLayoutManager;
    private TextView mTvNameProduct;
    private TextView mTvPriceProduct;
    private TextView mTvInfoProduct;
    private List<Comment> mListRate = new ArrayList<>();
    private List<Comment> mListComment = new ArrayList<>();
    private List<CustomStarView> mListStar;

    private RecyclerView mRecyclerRateMessage;
    private RecyclerView mRvPreviewComment;
    private EditText mEdContentMess, mEdTitleMess;
    private TextView mTextViewStar1, mTextViewStar2, mTextViewStar3, mTextViewStar4, mTextViewStar5;
    private AlertDialog mAlertDialogPostMessage;
    private ImageView mButtonPostProductMess;
    private CallbackManager mCallbackManager;
    private ShareDialog mShareDialog;
    private TextView mTvGeneralRate;
    private TextView mTvAmountOfRates;
    private Toolbar mToolbar;
    private ImageView mIvShowPreview;
    private TextView mTvReadAllReviews;
    private TextView mTvPost;
    private CommentStoreAdapter mStoreAdapter;
    private CommentStoreAdapter mSuggestStoreAdapter;

    private LinearLayout mLayoutStar;
    private View.OnClickListener mOnClickListenner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.tv_product_info:
                    Intent readmoreIntent = new Intent(DetailsProductActivity.this, ReadmoreActivity.class);
                    readmoreIntent.putExtra(Constants.EXTRA_NAME_PRODUCT, mTvNameProduct.getText().toString());
                    readmoreIntent.putExtra(Constants.EXTRA_PRICE_PRODUCT, mTvPriceProduct.getText().toString());
                    readmoreIntent.putExtra(Constants.EXTRA_INFOR_PRODUCT, FakeContainer.getInfoProduct());
                    startActivity(readmoreIntent);
                    break;
                case R.id.button_post_product:
                    initAlertDiaLogPostMessage();
                    break;
                case R.id.tv_readall_reviews:
                    mRecyclerRateMessage.setVisibility(View.VISIBLE);
                    break;
                case R.id.button_post_message_rate:
                    String title = mEdTitleMess.getText().toString();
                    String content = mEdContentMess.getText().toString();
                    Comment newMessage = new Comment(
                            R.drawable.avatar,
                            title,
                            content,
                            getString(R.string.name_user),
                            SystemUtil.getCurDate()
                    );
                    newMessage.setImageStars(getTotalStar());
                    mListComment.set(0, newMessage);
                    mSuggestStoreAdapter.notifyDataSetChanged();
                    mStoreAdapter.notifyDataSetChanged();
                    mAlertDialogPostMessage.dismiss();
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
//        AppEventsLogger.activateApp(getApplicationContext()); // Hoa comment:
        mCallbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_product_details_layout);
        mShareDialog = new ShareDialog(this);
        initViews();
        fakeMessage();
        initRecycle();
        initViewPager();
        ShowcaseGuideUtil.singleShowcase(DetailsProductActivity.this,
                Constants.SHOWCASE_ID_DETAILS_PRODUCT,
                new Showcase(mButtonPostProductMess, getString(R.string.sequence_write_vote)));
    }

    private void initViewPager() {
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), FakeContainer.initIdResList());
        mPager.setAdapter(mPagerAdapter);
        mPager.setVisibility(View.GONE);
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbar.setTitle(FakeContainer.getNameProduct());
        mToolbar.setTitleTextColor(Color.WHITE);
        mRvPreviewProducts = (RecyclerView) findViewById(R.id.rv_product_preview);
//        mRvBookingProducts = (RecyclerView) findViewById(R.id.rv_product_book_options);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRvPreviewProducts.setHasFixedSize(true);
//        mRvBookingProducts.setHasFixedSize(true);
        // use a linear layout manager
        mPreviewLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvPreviewProducts.setLayoutManager(mPreviewLayoutManager);
        mBookingLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        mRvBookingProducts.setLayoutManager(mBookingLayoutManager);
        mPreviewAdapter = new PreviewDetailsAdapter(this, FakeContainer.initIdResList())
                .setOnClickShowPreviewDetail(this);
        mBookingAdapter = new BookProductAdapter(this, initBookingProducts());
        mRvPreviewProducts.setAdapter(mPreviewAdapter);
//        mRvBookingProducts.setAdapter(mBookingAdapter);
        // init textviews
        mTvNameProduct = (TextView) findViewById(R.id.tv_product_name);
        mTvPriceProduct = (TextView) findViewById(R.id.tv_product_price);
        mTvInfoProduct = (TextView) findViewById(R.id.tv_product_info);
        mTvInfoProduct.setOnClickListener(mOnClickListenner);
        mTvGeneralRate = (TextView) findViewById(R.id.tv_general_rate);
        mTvAmountOfRates = (TextView) findViewById(R.id.tv_amount_of_rates);
        mTvReadAllReviews = (TextView) findViewById(R.id.tv_readall_reviews);
        setTexts();
        mButtonPostProductMess = (ImageView) findViewById(R.id.button_post_product);
        mButtonPostProductMess.setOnClickListener(mOnClickListenner);
        mIvShowPreview = (ImageView) findViewById(R.id.iv_show_preview);
        mTvReadAllReviews.setOnClickListener(mOnClickListenner);
    }

    private ArrayList<ItemBooking> initBookingProducts() {
        ArrayList<ItemBooking> list = new ArrayList<>();
        list.add(new ItemBooking(R.drawable.ic_call_white_24px, getString(R.string.call)));
        list.add(new ItemBooking(R.drawable.ic_schedule_white_24px, getString(R.string.schedule)));
        list.add(new ItemBooking(R.drawable.ic_shopping_cart_white_24px, getString(R.string.book)));
        list.add(new ItemBooking(R.drawable.ic_favorite_white_24px, getString(R.string.favorite)));
        list.add(new ItemBooking(R.drawable.ic_white_star_full, getString(R.string.vote_product)));
        return list;
    }

    private void setTexts() {
        mTvNameProduct.setText(FakeContainer.getNameProduct());
        mTvPriceProduct.setText(FakeContainer.getPriceProduct());
        mTvGeneralRate.setText(FakeContainer.getGeneralRate());
        mTvAmountOfRates.setText(FakeContainer.getAmountOfRates());
    }

    private void initAlertDiaLogPostMessage() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_post_message_rate, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        mLayoutStar = (LinearLayout) promptsView.findViewById(R.id.layout_stars);
        TextView textNameProduct = (TextView) promptsView.findViewById(R.id.text_question);
        textNameProduct.setText(mTvNameProduct.getText().toString());
        mEdTitleMess = (EditText) promptsView.findViewById(R.id.edit_text_message_rate_title);
        mEdContentMess = (EditText) promptsView.findViewById(R.id.edit_text_message_rate_comment);
        mTvPost = (TextView) promptsView.findViewById(R.id.button_post_message_rate);
        mTvPost.setOnClickListener(mOnClickListenner);
        mTextViewStar1 = (TextView) promptsView.findViewById(R.id.text_start_1);
        mTextViewStar2 = (TextView) promptsView.findViewById(R.id.text_start_2);
        mTextViewStar3 = (TextView) promptsView.findViewById(R.id.text_start_3);
        mTextViewStar4 = (TextView) promptsView.findViewById(R.id.text_start_4);
        mTextViewStar5 = (TextView) promptsView.findViewById(R.id.text_start_5);
        mTextViewStar1.setOnClickListener(mOnClickListenner);
        mTextViewStar2.setOnClickListener(mOnClickListenner);
        mTextViewStar3.setOnClickListener(mOnClickListenner);
        mTextViewStar4.setOnClickListener(mOnClickListenner);
        mTextViewStar5.setOnClickListener(mOnClickListenner);
        mAlertDialogPostMessage = alertDialogBuilder.create();
        mAlertDialogPostMessage.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mAlertDialogPostMessage.show();
        addStarList();
    }

    private void fakeMessage() {
        Comment newMessage = new Comment(
                R.drawable.avatar,
                getString(R.string.name),
                getString(R.string.rate_product_message),
                getString(R.string.name_user),
                SystemUtil.getCurDate()
        );
        for (int i = 0; i < 5; i++) {
            mListRate.add(newMessage);
        }
        for (int i = 0; i < 1; i++) {
            mListComment.add(newMessage);
        }
    }

    private void initRecycle() {
        mSuggestStoreAdapter = new CommentStoreAdapter(this, mListComment)
                .setListener(this);
        mRvPreviewComment = (RecyclerView) findViewById(R.id.rv_preview_comments);
        mRvPreviewComment.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRvPreviewComment.setAdapter(mSuggestStoreAdapter);
        mStoreAdapter = new CommentStoreAdapter(this, mListRate);
        mRecyclerRateMessage = (RecyclerView) findViewById(R.id.rv_message_rate_product);
        mRecyclerRateMessage.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerRateMessage.setAdapter(mStoreAdapter);
        mRecyclerRateMessage.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void shareFacebook(String content) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(FakeContainer.URL_TEST))
                    .setContentTitle(content)
                    .build();
            mShareDialog.show(linkContent);
        }
    }

    @Override
    public void onClickItemBar(String textNameItem, int position) {
        if (textNameItem.equalsIgnoreCase(getString(R.string.danh_gia))) {
            initAlertDiaLogPostMessage();
        } else {
            if (textNameItem.equalsIgnoreCase(getString(R.string.call))) {
                StringBuffer buffer = new StringBuffer()
                        .append(getString(R.string.tel))
                        .append(getString(R.string.hint_number));
                Uri call = Uri.parse(buffer.toString());
                Intent surf = new Intent(Intent.ACTION_CALL, call);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(surf);
            } else if (textNameItem.equalsIgnoreCase(getString(R.string.book))) {
                startActivity(new Intent(this, BookProductActivity.class));
            } else if (textNameItem.equalsIgnoreCase(getString(R.string.favorite))) {
                //TODO favorite
            } else if (textNameItem.equalsIgnoreCase(getString(R.string.schedule))) {
                DialogShareUtil.dialogShareProduct(this, FakeContainer.initIdResList().get(0),
                        mCallbackManager, mTvNameProduct.getText().toString());
            }
        }
    }

    @Override
    public void onClickShowPreviewDetail(int curItem) {
        mPager.setCurrentItem(curItem, false);
        mPager.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (mIvShowPreview.isShown())
            mIvShowPreview.setVisibility(View.GONE);
        else if (mRecyclerRateMessage.isShown())
            mRecyclerRateMessage.setVisibility(View.GONE);
        else if (mPager.isShown())
            mPager.setVisibility(View.GONE);
        else
            super.onBackPressed();
    }

    @Override
    public void onPreviewCommentClicked() {
        mRecyclerRateMessage.setVisibility(View.VISIBLE);
    }

    private void addStarList() {
        mListStar = new ArrayList<>();
        for (int i = 0; i < Constants.LIMIT_STAR; i++) {
            CustomStarView customStarView = new CustomStarView(DetailsProductActivity.this, i);
            mListStar.add(customStarView);
            mListStar.get(i).setOnItemClickListener(this);
            mListStar.get(i).getView().requestLayout();
            mLayoutStar.addView(customStarView.getView());
            mLayoutStar.invalidate();
        }
    }

    @Override
    public void onItemClick(int position) {
        setStars(position);
    }

    public void setStars(int position) {
        for (int i = 0; i <= position; i++) {
            mListStar.get(i).setChecked(true);
            mListStar.get(i).setSelectedStar();
        }
        for (int i = position + 1; i < Constants.LIMIT_STAR; i++) {
            mListStar.get(i).setChecked(false);
            mListStar.get(i).setSelectedStar();
        }
    }

    public int getTotalStar() {
        int count = 0;
        for (int i = 0; i < Constants.LIMIT_STAR; i++) {
            if (mListStar.get(i).isChecked()) {
                count++;
            }
        }
        return count;
    }
}
