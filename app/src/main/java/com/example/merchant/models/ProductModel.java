package com.example.merchant.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import androidx.annotation.NonNull;

public class ProductModel implements Parcelable {

    int idProduct;
    int store_idStore;
    String productName;
    String productDescription;
    float productPrice;
    String productImage;
    String productServingSize;
    String productTag;
    String productPrepTime;
    String weather;
    String specialStatus, stock;
    int percentage;


    public ProductModel(int idProduct, int store_idStore, String productName, String productDescription, float productPrice, String productImage, String productServingSize, String productTag, String productPrepTime, String productRestoName, String productRestoImage, String weather) {
        this.idProduct = idProduct;
        this.store_idStore = store_idStore;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productServingSize = productServingSize;
        this.productTag = productTag;
        this.productPrepTime = productPrepTime;
        this.weather = weather;
    }

    public ProductModel(int idProduct, int store_idStore, String productName, String productDescription, float productPrice, String productImage, String productServingSize, String productTag, String productPrepTime, String productRestoName, String productRestoImage, String weather, String stock) {
        this.idProduct = idProduct;
        this.store_idStore = store_idStore;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productServingSize = productServingSize;
        this.productTag = productTag;
        this.productPrepTime = productPrepTime;
        this.weather = weather;
        this.stock = stock;
    }

    public ProductModel(int idProduct, String productName, String productDescription, float productPrice, String productImage, String specialStatus, int percentage) {
        this.idProduct = idProduct;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.specialStatus = specialStatus;
        this.percentage = percentage;
    }

    public ProductModel(){}

    protected ProductModel(Parcel in) {
        idProduct = in.readInt();
        store_idStore = in.readInt();
        productName = in.readString();
        productDescription = in.readString();
        productPrice = in.readFloat();
        productImage = in.readString();
        productServingSize = in.readString();
        productTag = in.readString();
        productPrepTime = in.readString();
        weather = in.readString();
        specialStatus = in.readString();
        percentage = in.readInt();
        stock = in.readString();
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getStore_idStore() {
        return store_idStore;
    }

    public void setStore_idStore(int store_idStore) {
        this.store_idStore = store_idStore;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }


    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductServingSize() {
        return productServingSize;
    }

    public void setProductServingSize(String productServingSize) {
        this.productServingSize = productServingSize;
    }

    public String getProductTag() {
        return productTag;
    }

    public void setProductTag(String productTag) {
        this.productTag = productTag;
    }

    public String getProductPrepTime() {
        return productPrepTime;
    }

    public void setProductPrepTime(String productPrepTime) {
        this.productPrepTime = productPrepTime;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getSpecialStatus() {
        return specialStatus;
    }

    public void setSpecialStatus(String specialStatus) {
        this.specialStatus = specialStatus;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public Bitmap getBitmapImage(){
        byte[] byteArray = Base64.decode(productImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0 , byteArray.length);
        return bitmap;
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(idProduct);
        parcel.writeInt(store_idStore);
        parcel.writeString(productName);
        parcel.writeString(productDescription);
        parcel.writeFloat(productPrice);
        parcel.writeString(productImage);
        parcel.writeString(productServingSize);
        parcel.writeString(productTag);
        parcel.writeString(productPrepTime);
        parcel.writeString(weather);
        parcel.writeString(specialStatus);
        parcel.writeInt(percentage);
        parcel.writeString(stock);
    }
}