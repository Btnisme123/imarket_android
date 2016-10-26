package com.example.framgia.imarketandroid.data;

import com.example.framgia.imarketandroid.R;
import com.example.framgia.imarketandroid.data.model.AlbumShop;
import com.example.framgia.imarketandroid.data.model.CartItem;
import com.example.framgia.imarketandroid.data.model.CategorySaleOff;
import com.example.framgia.imarketandroid.data.model.DrawerItem;
import com.example.framgia.imarketandroid.data.model.ImageEvent1;
import com.example.framgia.imarketandroid.data.model.ImageEvent2;
import com.example.framgia.imarketandroid.data.model.ItemBooking;
import com.example.framgia.imarketandroid.data.model.ItemProduct;
import com.example.framgia.imarketandroid.data.model.Market;
import com.example.framgia.imarketandroid.data.model.Shop;
import com.example.framgia.imarketandroid.data.model.StoreType;
import com.example.framgia.imarketandroid.util.Constants;
import com.example.framgia.imarketandroid.util.Flog;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoavt on 25/08/2016.
 */
public class FakeContainer {
    public static final String[] SUGGESTIONS = new String[]{
            "Belgium", "France", "Italy", "Germany", "Spain", "Viet Name"
    };
    public static final long GIA_SP = 18600000;
    public static final long CHI_PHI_PHAT_SINH = 50000;
    public static final int STORE_TYPE_0 = 0;
    public static final int STORE_TYPE_1 = 1;
    public static final int STORE_TYPE_2 = 2;
    public static final int STORE_TYPE_3 = 3;
    public static final int STORE_TYPE_4 = 4;
    public static final int STORE_TYPE_5 = 5;
    public static final int STORE_TYPE_6 = 6;
    public static final int STORE_TYPE_7 = 7;
    public static final String URL_TEST = "9gag.com";
    public static LatLng sLatLng = new LatLng(21.007380, 105.793139);
    public static float sGroundFirstParameter = 116f;
    public static float sGroundSecondParameter = 150f;
    public static float sGroundThirdParameter = 53f;
    public static final String ID_PRODUCT = "ID Product : ";
    public static final int ZOOM_RANGE = 15;
    public static final int CAMERA_PARAMETER = 117;

    public static ArrayList<CartItem> initCartProductList() {
        ArrayList<CartItem> list = new ArrayList<>();
        list.add(new CartItem(R.drawable.ic_iphone6s, "Iphone 6S", 16000000, 1, false));
        list.add(new CartItem(R.drawable.ic_iphone5s, "Iphone 5S", 14000000, 1, false));
        list.add(new CartItem(R.drawable.ic_htc_one, "HTC One", 12000000, 1, false));
        list.add(new CartItem(R.drawable.ic_sky_a850, "Sky A850", 4000000, 1, false));
        list.add(new CartItem(R.drawable.ic_lg_optimus, "LG Optimus", 9000000, 1, false));
        list.add(new CartItem(R.drawable.ic_window_phone, "Window Phone", 6000000, 1, false));
        list.add(new CartItem(R.drawable.ic_blackberry, "Blackberry", 12000000, 1, false));
        list.add(new CartItem(R.drawable.ic_nokia_n8, "Nokia", 5000000, 1, false));
        return list;
    }

    public static List<DrawerItem> initDrawerItems() {
        List<DrawerItem> drawerItems = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            if (i != 15) {
                drawerItems.add(new DrawerItem("Test " + (i + 1), "test"));
            } else {
                drawerItems.add(new DrawerItem("Test 16", "test", true));
            }
        }
        return drawerItems;
    }

    public static List<Market> initMarkets() {
        List<Market> markets = new ArrayList<>();
        markets.add(new Market("M0001", "Big C", "Trần Duy Hưng, Trung Hoà, Cầu Giấy, Hà Nội"));
        markets.add(new Market("M0001", "Big C", "Trần Duy Hưng, Trung Hoà, Cầu Giấy, Hà Nội"));
        markets.add(new Market("M0001", "Big C", "Trần Duy Hưng, Trung Hoà, Cầu Giấy, Hà Nội"));
        markets.add(new Market("M0001", "Big C", "Trần Duy Hưng, Trung Hoà, Cầu Giấy, Hà Nội"));
        return markets;
    }

    public static ArrayList<ItemBooking> initDiagramOption() {
        ArrayList<ItemBooking> list = new ArrayList<>();
        list.add(new ItemBooking(R.drawable.pin, Constants.LOCATION));
        list.add(new ItemBooking(R.drawable.shop, Constants.STORE));
        list.add(new ItemBooking(R.drawable.floor, Constants.FLOOR));
        return list;
    }

    public static List<String> initFloor() {
        List<String> listFloor = new ArrayList<>();
        listFloor.add("Tầng");
        listFloor.add("1");
        listFloor.add("2");
        listFloor.add("3");
        listFloor.add("4");
        listFloor.add("5");
        listFloor.add("6");
        listFloor.add("7");
        listFloor.add("8");
        listFloor.add("9");
        listFloor.add("10");
        listFloor.add("11");
        listFloor.add("12");
        listFloor.add("13");
        listFloor.add("14");
        listFloor.add("15");
        return listFloor;
    }

    public static List<Shop> initListShop() {
        List<Shop> mShopList = new ArrayList<>();
        Shop shop0 = new Shop(0, "Chọn cửa hàng");
        Shop shop1 = new Shop(1, "cửa hàng ăn uống");
        Shop shop2 = new Shop(2, "cửa hàng quần áo");
        Shop shop3 = new Shop(3, "cửa hàng sách");
        Shop shop4 = new Shop(4, "cửa hàng mỹ phẩm");
        Shop shop5 = new Shop(5, "Rạp chiếu phim");
        Shop shop6 = new Shop(6, "Trung tâm giải trí");
        Shop shop7 = new Shop(7, "Cửa hàng điện tử");
        mShopList.add(shop0);
        mShopList.add(shop1);
        mShopList.add(shop2);
        mShopList.add(shop3);
        mShopList.add(shop4);
        mShopList.add(shop5);
        mShopList.add(shop6);
        mShopList.add(shop7);
        return mShopList;
    }

    public static List<StoreType> initStore() {
        List<StoreType> storeList = new ArrayList<>();
        storeList.add(new StoreType(0, "Chọn cửa hàng", R.drawable.store, 1));
        storeList.add(new StoreType(1, "cửa hàng ăn uống", R.drawable.food_avatar, 1));
        storeList.add(new StoreType(2, "cửa hàng quần áo", R.drawable.fashion, 1));
        storeList.add(new StoreType(3, "cửa hàng sách", R.drawable.book_shop, 1));
        storeList.add(new StoreType(4, "cửa hàng mỹ phẩm", R.drawable.cosmetic, 1));
        storeList.add(new StoreType(5, "Rạp chiếu phim", R.drawable.stage, 1));
        storeList.add(new StoreType(6, "Trung tâm giải trí", R.drawable.game_icon, 1));
        storeList.add(new StoreType(7, "Cửa hàng điện tử", R.drawable.smartphone, 1));
        return storeList;
    }

    public static String getNameProduct() {
        return "Điện thoại HTC One M8 Eye";
    }

    public static String getPriceProduct() {
        return "5.990.000đ";
    }

    public static String getInfoProduct() {
        return "    Màn hình:Super LCD 3, 5\", Full HD\n" +
                "    Hệ điều hành:Android 5.0 (Lollipop)\n" +
                "    Camera sau:13 MP\n" +
                "    Camera trước:5 MP\n" +
                "    CPU:Qualcomm Snapdragon 801 4 nhân 32-bit, 2.3 GHz\n" +
                "    RAM:2 GB\n" +
                "    Bộ nhớ trong:16 GB\n" +
                "    Hỗ trợ thẻ nhớ:MicroSD, 128 GB\n" +
                "    Thẻ SIM:1 Sim, Nano SIM\n" +
                "    Kết nối:WiFi, 3G, 4G LTE Cat 4\n" +
                "    Dung lượng pin:2600 mAh\n" +
                "    Thiết kế:Nguyên khối\n" +
                "    Chức năng đặc biệt:HTC BoomSound";
    }

    public static final int NUM_FAKE_PREVIEWS = 7;

    public static ArrayList<Integer> initIdResList() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < NUM_FAKE_PREVIEWS; i++) {
            list.add(R.drawable.ic_htc_preview_01 + i);
        }
        return list;
    }

    public static int getPresentIconProduct(String nameProduct) {
        int idRes = -1;
        switch (nameProduct) {
            case "Iphone 6S":
                idRes = R.drawable.ic_iphone6s;
                break;
            case "Iphone 5S":
                idRes = R.drawable.ic_iphone5s;
                break;
            case "HTC One":
                idRes = R.drawable.ic_htc_one;
                break;
            case "Sky A850":
                idRes = R.drawable.ic_sky_a850;
                break;
            case "LG Optimus":
                idRes = R.drawable.ic_lg_optimus;
                break;
            case "Window Phone":
                idRes = R.drawable.ic_window_phone;
                break;
            case "Blackberry":
                idRes = R.drawable.ic_blackberry;
                break;
            case "Nokia":
                idRes = R.drawable.ic_nokia_n8;
                break;
            default:
                Flog.i("Nothing is match");
                break;
        }
        return idRes;
    }
    public static ArrayList<ItemProduct> getListProducts() {
        // Fake data
        ArrayList<ItemProduct> list = new ArrayList<>();
        list.add(new ItemProduct("Iphone 6S", "0%", R.drawable.ic_iphone6s));
        list.add(new ItemProduct("Iphone 5S", "10%", R.drawable.ic_iphone5s));
        list.add(new ItemProduct("HTC One", "20%", R.drawable.ic_htc_one));
        list.add(new ItemProduct("Sky A850", "0%", R.drawable.ic_sky_a850));
        list.add(new ItemProduct("LG Optimus", "5%", R.drawable.ic_lg_optimus));
        list.add(new ItemProduct("Window Phone", "0%", R.drawable.ic_window_phone));
        list.add(new ItemProduct("Blackberry", "0%", R.drawable.ic_blackberry));
        list.add(new ItemProduct("Nokia", "15%", R.drawable.ic_nokia_n8));
        return list;
    }
    public static List<AlbumShop> fakeDataAlbum() {
        List<AlbumShop> list = new ArrayList<>();
        list.add(new AlbumShop(R.drawable.ic_iphone7, "Iphone 7"));
        list.add(new AlbumShop(R.drawable.ic_iphone6s, "Iphone 6s"));
        list.add(new AlbumShop(R.drawable.ic_iphone5s, "Iphone 5s"));
        list.add(new AlbumShop(R.drawable.ic_window_phone, "Window phone"));
        list.add(new AlbumShop(R.drawable.ic_blackberry, "Blackbery"));
        list.add(new AlbumShop(R.drawable.ic_htc_one, "HTC One"));
        return list;
    }

    public static String arr[] = {
            "Chi nhanh 1",
            "Chi nhanh 2",
            "Chi nhanh 3",
            "Chi nhanh 4",
            "Chi nhanh 5",
            "Chi nhanh 6"};

    public static List<Object> getSampleArrayList() {
        List<Object> items = new ArrayList<>();
        List<ItemProduct> itemProductList = new ArrayList<>();
        itemProductList.add(new ItemProduct("Điện Thoại Iphone 5s giá ngon rẻ bất ngờ (Hàng Hot " +
                "trên thị trường",
                "25%", R
                .drawable
                .ic_iphone5s,
                "1000000 VND",
                "1100000  VND"));
        itemProductList.add(new ItemProduct("Điện Thoại Iphone 5s giá ngon rẻ bất ngờ (Hàng Hot " +
                "trên thị trường",
                "25%", R
                .drawable
                .ic_iphone5s,
                "1000000 VND",
                "1100000  VND"));
        itemProductList.add(new ItemProduct("Điện Thoại Iphone 5s giá ngon rẻ bất ngờ (Hàng Hot " +
                "trên thị trường",
                "25%", R
                .drawable
                .ic_iphone5s,
                "1000000 VND",
                "1100000  VND"));
        itemProductList.add(new ItemProduct("Điện Thoại Iphone 5s giá ngon rẻ bất ngờ (Hàng Hot " +
                "trên thị trường",
                "25%", R
                .drawable
                .ic_iphone5s,
                "1000000 VND",
                "1100000  VND"));
        itemProductList.add(new ItemProduct("Điện Thoại Iphone 5s giá ngon rẻ bất ngờ (Hàng Hot " +
                "trên thị trường",
                "25%", R
                .drawable
                .ic_iphone5s,
                "1000000 VND",
                "1100000  VND"));
        itemProductList.add(new ItemProduct("Điện Thoại Iphone 5s giá ngon rẻ bất ngờ (Hàng Hot " +
                "trên thị trường",
                "25%", R
                .drawable
                .ic_iphone5s,
                "1000000 VND",
                "1100000  VND"));
        items.add(new ImageEvent1(R.drawable.banner1));
        items.add(new ImageEvent2(R.drawable.banner2, R.drawable.banner3));
        items.add(new ImageEvent2(R.drawable.banner2, R.drawable.banner3));
        items.add(new ImageEvent1(R.drawable.banner1));
        items.add(new CategorySaleOff("Hàng điện tử", itemProductList));
        items.add(new CategorySaleOff("Hàng gia dụng", itemProductList));
        items.add(new ImageEvent1(R.drawable.banner1));
        items.add(new CategorySaleOff("Hàng thực phẩm", itemProductList));
        return items;
    }

    public static List<CartItem> GetInfoOrder(int mId) {
        List<CartItem> list = new ArrayList<>();
        list.add(new CartItem(mId, "Iphone", 2, 2, true));
        list.add(new CartItem(mId, "Iphone", 2, 2, true));
        list.add(new CartItem(mId, "Iphone", 2, 2, true));
        list.add(new CartItem(mId, "Iphone", 2, 2, true));
        return list;
    }

    public static List<CartItem> getListCartItem() {
        List<CartItem> list = new ArrayList<>();
        list.add(new CartItem(1, "Iphone", 1, 1, true, "20/1/2011", "14:03"));
        list.add(new CartItem(2, "Iphone", 2, 1, true, "20/1/2011", "15:03"));
        list.add(new CartItem(3, "Iphone", 1, 1, true, "20/1/2011", "16:03"));
        list.add(new CartItem(4, "Samsung Galaxy", 4, 1, true, "21/1/2011", "14:04"));
        list.add(new CartItem(5, "Samsung Galaxy", 1, 1, true, "21/1/2011", "11:03"));
        list.add(new CartItem(6, "Bphone", 3, 1, true, "22/1/2011", "14:11"));
        list.add(new CartItem(7, "Bphone", 1, 1, true, "22/1/2011", "12:11"));
        return list;
    }

    public static List<String> getListHeader() {
        List<String> list = new ArrayList<>();
        list.add("20/1/2011");
        list.add("21/1/2011");
        list.add("22/1/2011");
        return list;
    }

    public static String getGeneralRate() {
        return "3.9";
    }

    public static String getAmountOfRates() {
        return "1,307";
    }
}