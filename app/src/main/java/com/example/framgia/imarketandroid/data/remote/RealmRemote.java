package com.example.framgia.imarketandroid.data.remote;

import android.content.Context;
import android.util.Log;

import com.example.framgia.imarketandroid.data.model.Category;
import com.example.framgia.imarketandroid.data.model.CustomMarker;
import com.example.framgia.imarketandroid.data.model.Edge;
import com.example.framgia.imarketandroid.data.model.Migration;
import com.example.framgia.imarketandroid.data.model.Point;
import com.example.framgia.imarketandroid.data.model.Store;
import com.example.framgia.imarketandroid.util.Constants;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by framgia on 24/08/2016.
 */
public class RealmRemote {
    private static Realm mRealm = Realm.getDefaultInstance();
    private static final int SCHEMA_VERSION = 3;

    public static void openDatabaseRealm(Context context, InputStream inputStream, String
            outFileName) {
        copyBundledRealmFile(context, inputStream, outFileName);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context)
                .name(outFileName)
                .schemaVersion(SCHEMA_VERSION)
                .build();
        try {
            Realm.migrateRealm(realmConfiguration, new Migration());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mRealm.getInstance(realmConfiguration);
    }

    public static RealmResults<Point> getAllPoint() {
        return mRealm.where(Point.class).findAll();
    }

    public static <T extends RealmObject> RealmResults getAll(Class<T> tClass) {
        return mRealm.where(tClass).findAll();
    }

    public static <T extends RealmObject> RealmResults getListPointDisplay(Class<T> tClass) {
        return mRealm.where(tClass).equalTo(Constants.FIELD_TYPE, 1).findAll();
    }

    public static RealmList<Point> getListPointDisplay(int type) {
        RealmResults<Point> results = null;
        if (type == 0)
            results = mRealm.where(Point.class).findAll();
        else
            results = mRealm.where(Point.class).equalTo(Constants.FIELD_TYPE, type).findAll();
        RealmList<Point> mLastResults = new RealmList<>();
        for (Point point : results) {
            mLastResults.add(point);
        }
        return mLastResults;
    }

    public static RealmResults<Edge> getListEdgeDisplay() {
        RealmResults<Edge> results = null;
        results = mRealm.where(Edge.class).findAll();
        return results;
    }

    public static LatLng getLocationFromName(int name) {
        Point point = mRealm.where(Point.class).equalTo(Constants.FIELD_NAME, name).findFirst();
        LatLng latLng = new LatLng(point.getLat(), point.getLng());
        return latLng;
    }

    public static List<Edge> getListEdge() {
        RealmResults<Edge> results = mRealm.where(Edge.class).findAll();
        return results;
    }

    public static Point getObjectPointFromId(int id) {
        Point point = mRealm.where(Point.class).equalTo(Constants.FIELD_ID, id).findFirst();
        return point;
    }

    public static Point getObjectPointFromName(int name) {
        Point point = mRealm.where(Point.class).equalTo(Constants.FIELD_NAME, name).findFirst();
        return point;
    }

    public static void deletePoint(final int name) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Point> results =
                        mRealm.where(Point.class).equalTo(Constants.FIELD_NAME, name).findAll();
                results.deleteAllFromRealm();
            }
        });
    }
    public static void deleteEdge(final int name_start, final int name_end) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Edge> results =
                    mRealm.where(Edge.class).equalTo(Constants.NAME_START, name_start).equalTo
                        (Constants.NAME_END, name_end).findAll();
                results.deleteAllFromRealm();
            }
        });
    }
    public static void savePoint(Point point) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(point);
        mRealm.commitTransaction();
    }

    public static void saveObject(Object object) {
        mRealm.beginTransaction();
        mRealm.copyToRealm((RealmObject) object);
        mRealm.commitTransaction();
    }

    public static void saveEdge(Edge edge) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(edge);
        mRealm.commitTransaction();
    }

    public static void saveStore(Store store) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(store);
        mRealm.commitTransaction();
    }

    public static CustomMarker createCustomMarkerFromPoint(Point point) {
        CustomMarker result = new CustomMarker(0, point.getLat(), point.getLng(),
                Constants.DEMO_NUMBER,point.getType(), point.getId()+"");
        return result;
    }

    private static String copyBundledRealmFile(Context context, InputStream inputStream, String
            outFileName) {
        try {
            File file = new File(context.getFilesDir(), outFileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
