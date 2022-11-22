package com.example.merchant.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class OrderModel implements Parcelable {
    long order_id;
    String name;
    String address;
    String time;
    String distance;
    String status;
    List<OrderItemModel> orderItem_list;
    int item_count;
    float total;

    public OrderModel(long order_id, String name, String address, String time, String distance, List<OrderItemModel> orderItem_list, int item_count, float total) {
        this.order_id = order_id;
        this.name = name;
        this.address = address;
        this.time = time;
        this.distance = distance;
        this.orderItem_list = orderItem_list;
        this.item_count = item_count;
        this.total = total;
    }

    public OrderModel(String name, String address, String time, String distance, List<OrderItemModel> orderItem_list, int item_count, float total) {
        this.name = name;
        this.address = address;
        this.time = time;
        this.distance = distance;
        this.orderItem_list = orderItem_list;
        this.item_count = item_count;
        this.total = total;
    }

    public OrderModel() {
    }

    protected OrderModel(Parcel in) {
        order_id = in.readLong();
        name = in.readString();
        address = in.readString();
        time = in.readString();
        distance = in.readString();
        orderItem_list = in.createTypedArrayList(OrderItemModel.CREATOR);
        item_count = in.readInt();
        total = in.readFloat();
    }

    public static final Creator<OrderModel> CREATOR = new Creator<OrderModel>() {
        @Override
        public OrderModel createFromParcel(Parcel in) {
            return new OrderModel(in);
        }

        @Override
        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
        }
    };

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<OrderItemModel> getOrderItem_list() {
        return orderItem_list;
    }

    public void setOrderItem_list(List<OrderItemModel> orderItem_list) {
        this.orderItem_list = orderItem_list;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(order_id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(time);
        dest.writeString(distance);
        dest.writeTypedList(orderItem_list);
        dest.writeInt(item_count);
        dest.writeFloat(total);
    }
}

