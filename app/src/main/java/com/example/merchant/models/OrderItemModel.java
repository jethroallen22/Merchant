package com.example.merchant.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class OrderItemModel implements Parcelable {
    String product_name;
    int quantity;
    float total_price;

    public OrderItemModel(String product_name, int quantity, float total_price) {
        this.product_name = product_name;
        this.quantity = quantity;
        this.total_price = total_price;
    }

    protected OrderItemModel(Parcel in) {
        product_name = in.readString();
        quantity = in.readInt();
        total_price = in.readFloat();
    }

    public static final Creator<OrderItemModel> CREATOR = new Creator<OrderItemModel>() {
        @Override
        public OrderItemModel createFromParcel(Parcel in) {
            return new OrderItemModel(in);
        }

        @Override
        public OrderItemModel[] newArray(int size) {
            return new OrderItemModel[size];
        }
    };

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(product_name);
        dest.writeInt(quantity);
        dest.writeFloat(total_price);
    }
}
