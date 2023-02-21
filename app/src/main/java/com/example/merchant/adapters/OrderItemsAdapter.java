package com.example.merchant.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.merchant.R;
import com.example.merchant.models.OrderItemModel;

import java.util.List;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ViewHolder> {

    Context context;
    List<OrderItemModel> list;

    public OrderItemsAdapter(Context context, List<OrderItemModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("OI Adapter", "INSIDE");
        holder.tv_order_item_name.setText("- " + list.get(position).getProductName());
        holder.tv_order_item_qty.setText("Qty:" + list.get(position).getItemQuantity() + "x");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class
    ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_order_item_name;
        TextView tv_order_item_qty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_order_item_name = itemView.findViewById(R.id.tv_order_item_name);
            tv_order_item_qty = itemView.findViewById(R.id.tv_order_item_qty);
        }
    }
}
