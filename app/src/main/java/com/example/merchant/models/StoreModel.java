package com.example.merchant.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class StoreModel implements Parcelable {
    int idStore;
    String storeName, storeDescription, storeLocation, storeCategory, storeImage;
    Float storeRating, storePopularity;
    String storeStartTime, storeEndTime;

    public StoreModel(int idStore, String storeName, String storeDescription, String storeLocation, String storeCategory, String storeImage, Float storeRating, Float storePopularity, String storeStartTime, String storeEndTime) {
        this.idStore = idStore;
        this.storeName = storeName;
        this.storeDescription = storeDescription;
        this.storeLocation = storeLocation;
        this.storeCategory = storeCategory;
        this.storeImage = storeImage;
        this.storeRating = storeRating;
        this.storePopularity = storePopularity;
        this.storeStartTime = storeStartTime;
        this.storeEndTime = storeEndTime;
    }

    public StoreModel(String storeName, String storeDescription, String storeLocation, String storeCategory, String storeImage, Float storeRating, Float storePopularity, String storeStartTime, String storeEndTime) {
        this.storeName = storeName;
        this.storeDescription = storeDescription;
        this.storeLocation = storeLocation;
        this.storeCategory = storeCategory;
        this.storeImage = storeImage;
        this.storeRating = storeRating;
        this.storePopularity = storePopularity;
        this.storeStartTime = storeStartTime;
        this.storeEndTime = storeEndTime;
    }

    public StoreModel(){}

    protected StoreModel(Parcel in) {
        idStore = in.readInt();
        storeName = in.readString();
        storeDescription = in.readString();
        storeLocation = in.readString();
        storeCategory = in.readString();
        storeImage = in.readString();

        if (in.readByte() == 0) {
            storeRating = null;
        } else {
            storeRating = in.readFloat();
        } if (in.readByte() == 0) {
            storePopularity = null;
        } else {
            storePopularity = in.readFloat();
        }
        storeStartTime = in.readString();
        storeStartTime = in.readString();
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

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
    }

    public String getStoreStartTime() {
        return storeStartTime;
    }

    public void setStoreStartTime(String storeStartTime) {
        this.storeStartTime = storeStartTime;
    }

    public String getStoreEndTime() {
        return storeEndTime;
    }

    public void setStoreEndTime(String storeEndTime) {
        this.storeEndTime = storeEndTime;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreDescription() {
        return storeDescription;
    }

    public void setStoreDescription(String storeDescription) {
        this.storeDescription = storeDescription;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }

    public String getStoreCategory() {
        return storeCategory;
    }

    public void setStoreCategory(String storeCategory) {
        this.storeCategory = storeCategory;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public Float getStoreRating() {
        return storeRating;
    }

    public void setStoreRating(Float storeRating) {
        this.storeRating = storeRating;
    }

    public Float getStorePopularity() {
        return storePopularity;
    }

    public void setStorePopularity(Float storePopularity) {
        this.storePopularity = storePopularity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(idStore);
        parcel.writeString(storeName);
        parcel.writeString(storeDescription);
        parcel.writeString(storeLocation);
        parcel.writeString(storeCategory);
        parcel.writeString(storeImage);

        if (storeRating == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(storeRating);
        } if (storePopularity == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(storePopularity);
        }
        parcel.writeString(storeStartTime);
        parcel.writeString(storeEndTime);
    }
}
