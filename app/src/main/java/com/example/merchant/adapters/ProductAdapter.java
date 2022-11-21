package com.example.merchant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
import com.example.merchant.R;
import com.example.merchant.models.ProductModel;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;
    List<ProductModel> list;

    public ProductAdapter(Context context, List<ProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
//        Glide.with(context)
//                .load(list.get(position).getProduct_image())
//                .into(holder.iv_product_image);
        holder.tv_product_name.setText(list.get(position).getProduct_name());
        holder.tv_product_calories.setText(list.get(position).getProduct_calories() + "cal");
        holder.tv_product_price.setText("P" + list.get(position).getProduct_price().toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_product_image;
        TextView tv_product_name, tv_product_price, tv_product_calories;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_product_image = itemView.findViewById(R.id.iv_product_image);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_product_calories = itemView.findViewById(R.id.tv_product_cal);
            tv_product_price = itemView.findViewById(R.id.tv_product_price);
        }
    }
}
