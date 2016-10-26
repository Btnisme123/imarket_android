package com.example.framgia.imarketandroid.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.annotations.PrimaryKey;

/**
 * Created by yue on 22/07/2016.
 */
public class Category implements Serializable {

    @PrimaryKey
    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("image")
    private String mImageLink;
    @SerializedName("store_id")
    private int mStoreId;

    public Category() {
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Category(String id, String name, int storeId) {
        mId = id;
        mName = name;
        mStoreId = storeId;
    }

    public int getStoreId() {
        return mStoreId;
    }

    public void setStoreId(int storeId) {
        mStoreId = storeId;
    }

    public String getImageLink() {
        return mImageLink;
    }

    public void setImageLink(String imageLink) {
        this.mImageLink = imageLink;
    }
}
