package com.example.merchant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.merchant.R;
import com.example.merchant.interfaces.RecyclerViewInterface;
import com.example.merchant.models.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    List<OrderModel> list;

    public OrderAdapter(Context context, List<OrderModel> list, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_item, parent, false),recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        ArrayList<String> strings = new ArrayList<>();
        String text = "";
        if(list.size() != 0) {
            int i, j = 0;
            //text = String.join(", ", list.get(position).getOrderItem_list().get(i).getProduct_name());
            for (i = 0; i < list.get(position).getOrderItem_list().size(); i++) {
                if (i == list.get(position).getOrderItem_list().size() - 1) {
                    text += list.get(position).getOrderItem_list().get(i).getProductName();
                } else {
                    text += list.get(position).getOrderItem_list().get(i).getProductName() + ", ";
                }
            }
            holder.tv_order_info.setText(text);
            holder.tv_order_item_info.setText("Qty: " + list.get(position).getOrderItem_list().size());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_order_info, tv_order_item_info;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            tv_order_info = itemView.findViewById(R.id.tv_order_info);
            tv_order_item_info = itemView.findViewById(R.id.tv_order_item_info);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
