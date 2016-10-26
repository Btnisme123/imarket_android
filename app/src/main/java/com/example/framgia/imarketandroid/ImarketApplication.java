package com.example.framgia.imarketandroid;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;

import com.example.framgia.imarketandroid.data.model.Migration;
import com.example.framgia.imarketandroid.util.Constants;
import com.example.framgia.imarketandroid.util.NotificationUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by toannguyen201194 on 24/08/2016.
 */
public class ImarketApplication extends Application {
    public int notificationCount;

    @Override
    public void onCreate() {
        super.onCreate();
        printHashKey();
        notificationCount = 0;
        copyBundledRealmFile(this.getResources().openRawResource(R.raw.default_data), "default1" +
            ".realm");
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
            .name("default1.realm").migration(new Migration())
            .schemaVersion(0)
            .build();
        Realm.setDefaultConfiguration(realmConfiguration);
//
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
//            .name(Realm.DEFAULT_REALM_NAME)
//            .schemaVersion(0)
//            .deleteRealmIfMigrationNeeded()
//            .build();
//        Realm.setDefaultConfiguration(realmConfiguration);
        //  NotificationUtil.setAlarm(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public void incrementCount() {
        notificationCount++;
    }

    public int getNotificationCount() {
        return notificationCount;
    }

    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                getPackageName(),
                PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance(Constants.MESSAGEDIGEST);
                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    private String copyBundledRealmFile(InputStream inputStream, String outFileName) {
        try {
            File file = new File(this.getFilesDir(), outFileName);
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
