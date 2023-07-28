package com.example.merchant.models;

import static android.util.Base64.DEFAULT;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class OrderModel implements Parcelable {
    int idOrder;
    int idUser;
    float orderItemTotalPrice;
    String orderStatus;
    int store_idstore;
    String users_name;
    List<OrderItemModel> orderItem_list;
    String timedate;
    String feedback, proof;

    public OrderModel(int idOrder, float orderItemTotalPrice, String orderStatus, int store_idstore, String users_name, List<OrderItemModel> orderItem_list) {
        this.idOrder = idOrder;
        this.orderItemTotalPrice = orderItemTotalPrice;
        this.orderStatus = orderStatus;
        this.store_idstore = store_idstore;
        this.users_name = users_name;
        this.orderItem_list = orderItem_list;
    }

    public OrderModel(int idOrder, float orderItemTotalPrice, String orderStatus, int store_idstore, String users_name, String timedate, List<OrderItemModel> orderItem_list) {
        this.idOrder = idOrder;
        this.orderItemTotalPrice = orderItemTotalPrice;
        this.orderStatus = orderStatus;
        this.store_idstore = store_idstore;
        this.users_name = users_name;
        this.timedate = timedate;
        this.orderItem_list = orderItem_list;
    }

    public OrderModel(int idOrder, float orderItemTotalPrice, String orderStatus, int store_idstore, String users_name, String timedate, String feedback, String proof, List<OrderItemModel> orderItem_list) {
        this.idOrder = idOrder;
        this.orderItemTotalPrice = orderItemTotalPrice;
        this.orderStatus = orderStatus;
        this.store_idstore = store_idstore;
        this.users_name = users_name;
        this.timedate = timedate;
        this.feedback = feedback;
        this.proof = proof;
        this.orderItem_list = orderItem_list;
    }

    public OrderModel() {
    }

    protected OrderModel(Parcel in) {
        idOrder = in.readInt();
        orderItemTotalPrice = in.readFloat();
        orderStatus = in.readString();
        store_idstore = in.readInt();
        users_name = in.readString();
        timedate = in.readString();
        feedback = in.readString();
        proof = in.readString();
        orderItem_list = in.createTypedArrayList(OrderItemModel.CREATOR);
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

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public float getOrderItemTotalPrice() {
        return orderItemTotalPrice;
    }

    public void setOrderItemTotalPrice(float orderItemTotalPrice) {
        this.orderItemTotalPrice = orderItemTotalPrice;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getStore_idstore() {
        return store_idstore;
    }

    public void setStore_idstore(int store_idstore) {
        this.store_idstore = store_idstore;
    }

    public String getUsers_name() {
        return users_name;
    }

    public void setUsers_name(String users_name) {
        this.users_name = users_name;
    }

    public String getTimedate() {
        return timedate;
    }

    public void setTimedate(String timedate) {
        this.timedate = timedate;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public List<OrderItemModel> getOrderItem_list() {
        return orderItem_list;
    }

    public void setOrderItem_list(List<OrderItemModel> orderItem_list) {
        this.orderItem_list = orderItem_list;
    }

    public Bitmap getBitmapImage(){
        byte[] byteArray = Base64.decode(proof, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0 , byteArray.length);
        return bitmap;
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(idOrder);
        dest.writeFloat(orderItemTotalPrice);
        dest.writeString(orderStatus);
        dest.writeInt(store_idstore);
        dest.writeString(users_name);
        dest.writeString(timedate);
        dest.writeString(feedback);
        dest.writeString(proof);
        dest.writeTypedList(orderItem_list);
    }
}

