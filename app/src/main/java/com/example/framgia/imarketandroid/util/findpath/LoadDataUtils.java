package com.example.framgia.imarketandroid.util.findpath;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.example.framgia.imarketandroid.R;
import com.example.framgia.imarketandroid.data.model.CommerceCanter;
import com.example.framgia.imarketandroid.data.model.CommerceList;
import com.example.framgia.imarketandroid.data.model.Floor;
import com.example.framgia.imarketandroid.data.model.ItemProduct;
import com.example.framgia.imarketandroid.data.model.ListFloor;
import com.example.framgia.imarketandroid.data.model.ProductList;
import com.example.framgia.imarketandroid.data.model.Stores;
import com.example.framgia.imarketandroid.ui.activity.ChooseMarketActivity;
import com.example.framgia.imarketandroid.ui.activity.FloorActivity;
import com.example.framgia.imarketandroid.ui.activity.ListProductsActivity;
import com.example.framgia.imarketandroid.util.Constants;
import com.example.framgia.imarketandroid.util.Flog;
import com.example.framgia.imarketandroid.util.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phongtran on 07/10/2016.
 */
public class LoadDataUtils {
    private static Context mContext;
    private static ProgressDialog mProgressDialog;
    public static BroadcastReceiver mReceiver = null;

    public static void init(Context context) {
        mContext = context;
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getString(R.string.progressdialog));
        mProgressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
        HttpRequest.getInstance().init();
    }

    public interface OnLoadMarketsCallback {
        public void onLoadMarketsDone(List<CommerceCanter> list);
    }

    private OnLoadMarketsCallback mLoadMarketsListenner;

    public LoadDataUtils setLoadMarketsListenner(OnLoadMarketsCallback loadMarketsListenner) {
        mLoadMarketsListenner = loadMarketsListenner;
        return this;
    }

    public static void loadFloor(final Context context, final int idCommerce) {
        init(context);
        mProgressDialog.show();
        HttpRequest.getInstance().loadListFloor(idCommerce);
        HttpRequest.getInstance().setOnLoadDataListener(new HttpRequest.OnLoadDataListener() {
            @Override
            public void onLoadDataSuccess(Object object) {
                ListFloor floors = (ListFloor) object;
                mProgressDialog.dismiss();
                if (floors != null) {
                    Flog.toast(mContext, R.string.data_success);
                    FloorActivity.mFloorList.clear();
                    for (int i = 0; i < floors.getFloorList().size(); i++) {
                        Floor floor = floors.getFloorList().get(i);
                        FloorActivity.mFloorList.add(floor.getNameFloor().toString());
                    }
                } else {
                    Flog.toast(mContext, R.string.not_data_in_object);
                }
            }

            @Override
            public void onLoadDataFailure(String message) {
                mProgressDialog.dismiss();
                if (!InternetUtil.isInternetConnected(context)) {
                    Flog.toast(context, R.string.no_internet);
                    processBroadcastFloor(idCommerce);
                }
            }
        });
    }


    public static void loadCommerce(final Context context) {
        init(context);
        mProgressDialog.show();
        HttpRequest.getInstance().loadListCommerce();
        HttpRequest.getInstance().setOnLoadDataListener(new HttpRequest.OnLoadDataListener() {
            @Override
            public void onLoadDataSuccess(Object object) {
                mProgressDialog.dismiss();
                CommerceList commerceList = (CommerceList) object;
                if (commerceList != null) {
//                    Flog.toast(mContext, R.string.data_success);
                    ChooseMarketActivity.sMarkets.clear();
                    int size = commerceList.getCenterList().size();
                    for (int i = 0; i < size; i++) {
                        CommerceCanter commerceCanter = commerceList.getCenterList().get(i);
                        ChooseMarketActivity.sMarkets.add(commerceCanter);
                    }
                    ChooseMarketActivity.initDataAutoCompleteTextView();
                } else {
                    Flog.toast(mContext, R.string.not_data_in_object);
                }
            }

            @Override
            public void onLoadDataFailure(String message) {
                mProgressDialog.dismiss();
                if (!InternetUtil.isInternetConnected(context)) {
                    Flog.toast(context, R.string.no_internet);
                    processBroadcastCommerce(context);
                }
            }
        });
    }

    public static void getStoreByStoreType(final Context context, final int id_floor, final int storeType) {
        init(context);
        mProgressDialog.show();
        HttpRequest.getInstance().getStore(id_floor, storeType);
        HttpRequest.getInstance().setOnLoadDataListener(new HttpRequest.OnLoadDataListener() {
            @Override
            public void onLoadDataSuccess(Object object) {
                Stores listStore = (Stores) object;
                mProgressDialog.dismiss();
                if (listStore != null) {
                    Flog.toast(mContext, R.string.store_success);
                } else {
                    Flog.toast(mContext, R.string.store_null);
                }
            }

            @Override
            public void onLoadDataFailure(String message) {
                mProgressDialog.dismiss();
                if (!InternetUtil.isInternetConnected(context)) {
                    Flog.toast(context, R.string.no_internet);
                    processBroadcastStore(id_floor, storeType);
                }
            }
        });
    }

    public static void getProductInCategory(final Context context, final int id_cate) {
        init(context);
        //mProgressDialog.show();
        HttpRequest.getInstance().getProduct(id_cate);
        HttpRequest.getInstance().setOnLoadDataListener(new HttpRequest.OnLoadDataListener() {
            @Override
            public void onLoadDataSuccess(Object object) {
                ProductList productList = (ProductList) object;
               // mProgressDialog.dismiss();
                if (productList != null) {
                    ListProductsActivity.sItemProducts.clear();
                    for (int i = 0; i < productList.getItemProductList().size(); i++) {
                        List<ItemProduct> products=new ArrayList<>();
                        ItemProduct product = productList.getItemProductList().get(i);
                        ListProductsActivity.sItemProducts.add(product);
                        Flog.toast(mContext,""+ ListProductsActivity.sItemProducts);
                    }
                    ListProductsActivity.sAdapter.notifyDataSetChanged();
                } else {
                    Flog.toast(mContext, R.string.product_null);
                }
            }

            @Override
            public void onLoadDataFailure(String message) {
                //mProgressDialog.dismiss();
                if (!InternetUtil.isInternetConnected(context)) {
                    Flog.toast(context, R.string.no_internet);
                    processBroadcastProduct(id_cate);
                }
            }
        });
    }

    private static void processBroadcastProduct(final int id_cate) {
        IntentFilter filter = new IntentFilter(Constants.INTERNET_FILTER);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, R.string.connect_change, Toast.LENGTH_SHORT).show();
                if (InternetUtil.isInternetConnected(mContext)) {
                    getProductInCategory(mContext, id_cate);
                }
            }
        };
        mContext.registerReceiver(mReceiver, filter);
    }

    private static void processBroadcastCommerce(final Context conText) {
        IntentFilter filter = new IntentFilter(Constants.INTERNET_FILTER);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, R.string.connect_change, Toast.LENGTH_SHORT).show();
                if (InternetUtil.isInternetConnected(conText)) {
                    loadCommerce(conText);
                }
            }
        };
        mContext.registerReceiver(mReceiver, filter);
    }

    private static void processBroadcastStore(final int id_floor, final int storeType) {
        IntentFilter filter = new IntentFilter(Constants.INTERNET_FILTER);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, R.string.connect_change, Toast.LENGTH_SHORT).show();
                if (InternetUtil.isInternetConnected(mContext)) {
                    getStoreByStoreType(mContext, id_floor, storeType);
                }
            }
        };
        mContext.registerReceiver(mReceiver, filter);
    }

    private static void processBroadcastFloor(final int commerce) {
        IntentFilter filter = new IntentFilter(Constants.INTERNET_FILTER);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, R.string.connect_change, Toast.LENGTH_SHORT).show();
                if (InternetUtil.isInternetConnected(mContext)) {
                    loadFloor(mContext, commerce);
                }
            }
        };
        mContext.registerReceiver(mReceiver, filter);
    }
}
