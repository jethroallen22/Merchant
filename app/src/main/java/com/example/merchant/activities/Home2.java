package com.example.merchant.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.merchant.R;
import com.example.merchant.adapters.OrderAdapter;
import com.example.merchant.interfaces.RecyclerViewInterface;
import com.example.merchant.models.OrderItemModel;
import com.example.merchant.models.OrderModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Home2 extends AppCompatActivity {
    //Cart List Recycler View
    RecyclerView rv_orders;
    List<OrderModel> order_list;
    OrderAdapter orderAdapter;
    List<OrderItemModel> order_item_list;
    RecyclerViewInterface recyclerViewInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        getSupportActionBar().hide();

        order_item_list = new ArrayList<>();
        //order_item_list.add(new OrderItemModel("Burger Mcdo", 2, 80F));

        rv_orders = findViewById(R.id.rv_orders);
        order_list = new ArrayList<>();
        //order_list.add(new OrderModel("Juan Dela Cruz", "Tondo, Manila", String.valueOf(LocalDateTime.now()), "3.5", order_item_list, order_item_list.size(),123F));
        orderAdapter = new OrderAdapter(this,order_list, recyclerViewInterface);
        rv_orders.setAdapter(orderAdapter);
        rv_orders.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        rv_orders.setHasFixedSize(true);
        rv_orders.setNestedScrollingEnabled(false);


    }




}