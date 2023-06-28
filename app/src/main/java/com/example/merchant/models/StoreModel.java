package com.example.merchant.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import androidx.annotation.NonNull;

public class StoreModel implements Parcelable {
    long store_id;
    String store_image;
    String store_name;
    String store_description;
    String store_location;
    String store_category;
    Float store_rating;
    int store_open;
    int store_closing;
    //for Voucher
    String voucherName;
    int voucherAmount, voucherMin;
    String startDate, endDate, voucher_status;

    public StoreModel(long store_id, String store_image, String store_name, String store_description,
                      String store_location, String store_category, Float store_rating, int store_open,
                      int store_closing) {
        this.store_id = store_id;
        this.store_image = store_image;
        this.store_name = store_name;
        this.store_description = store_description;
        this.store_location = store_location;
        this.store_category = store_category;
        this.store_rating = store_rating;
        this.store_open = store_open;
        this.store_closing = store_closing;
    }

    public StoreModel(long store_id, String voucherName, int voucherAmount, int voucherMin,
                      String startDate, String endDate, String voucher_status){
        this.store_id = store_id;
        this.voucherName = voucherName;
        this.voucherAmount = voucherAmount;
        this.voucherMin = voucherMin;
        this.startDate = startDate;
        this.endDate = endDate;
        this.voucher_status = voucher_status;
    }

    public StoreModel(){
    }
    public StoreModel(Parcel in) {
        store_id = in.readLong();
        store_image = in.readString();
        store_name = in.readString();
        store_description = in.readString();
        store_location = in.readString();
        store_category = in.readString();
        store_rating = in.readFloat();
        store_open = in.readInt();
        store_closing = in.readInt();
        voucherName = in.readString();
        voucherAmount = in.readInt();
        voucherMin = in.readInt();
        startDate = in.readString();
        endDate = in.readString();
        voucher_status = in.readString();
    }

    public static final Creator<StoreModel> CREATOR = new Creator<StoreModel>() {
        @Override
        public StoreModel createFromParcel(Parcel in) {
            return new StoreModel(in);
        }

        @Override
        public StoreModel[] newArray(int size) {
            return new StoreModel[size];
        }
    };

    public long getStore_id() {
        return store_id;
    }

    public void setStore_id(long store_id) {
        this.store_id = store_id;
    }

    public String getStore_image() {
        return store_image;
    }

    public void setStore_image(String store_image) {
        this.store_image = store_image;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_description() {
        return store_description;
    }

    public void setStore_description(String store_description) {
        this.store_description = store_description;
    }

    public String getStore_location() {
        return store_location;
    }

    public void setStore_location(String store_location) {
        this.store_location = store_location;
    }

    public String getStore_category() {
        return store_category;
    }

    public void setStore_category(String store_category) {
        this.store_category = store_category;
    }

    public Float getStore_rating() {
        return store_rating;
    }

    public void setStore_rating(Float store_rating) {
        this.store_rating = store_rating;
    }

    public int getStore_open() {
        return store_open;
    }

    public void setStore_open(int store_open) {
        this.store_open = store_open;
    }

    public int getStore_closing() {
        return store_closing;
    }

    public void setStore_closing(int store_closing) {
        this.store_closing = store_closing;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public int getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(int voucherAmount) {
        this.voucherAmount = voucherAmount;
    }

    public int getVoucherMin() {
        return voucherMin;
    }

    public void setVoucherMin(int voucherMin) {
        this.voucherMin = voucherMin;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getVoucher_status() {
        return voucher_status;
    }

    public void setVoucher_status(String voucher_status) {
        this.voucher_status = voucher_status;
    }

    public Bitmap getBitmapImage(){
        byte[] byteArray = Base64.decode(store_image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0 , byteArray.length);
        return bitmap;
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(store_id);
        dest.writeString(store_image);
        dest.writeString(store_name);
        dest.writeString(store_description);
        dest.writeString(store_location);
        dest.writeString(store_category);
        dest.writeFloat(store_rating);
        dest.writeInt(store_open);
        dest.writeInt(store_closing);
        dest.writeString(voucherName);
        dest.writeInt(voucherAmount);
        dest.writeInt(voucherMin);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(voucher_status);
    }
}
