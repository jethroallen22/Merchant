package com.example.merchant.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
import com.example.merchant.R;
import com.example.merchant.interfaces.RecyclerViewInterface;
import com.example.merchant.models.ProductModel;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    List<ProductModel> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener){
        listener = clickListener;
    }

    public ProductAdapter(Context context, List<ProductModel> list, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false),recyclerViewInterface,listener);
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
        ImageView iv_product_image, iv_edit_product, iv_delete_product;
        TextView tv_product_name, tv_product_price, tv_product_calories;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface, OnItemClickListener listener) {
            super(itemView);

            iv_product_image = itemView.findViewById(R.id.iv_product_image);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_product_calories = itemView.findViewById(R.id.tv_product_cal);
            tv_product_price = itemView.findViewById(R.id.tv_product_price);
            iv_edit_product = itemView.findViewById(R.id.iv_edit_product);
            iv_delete_product = itemView.findViewById(R.id.iv_delete_product);

            iv_edit_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClickEdit(pos);
                        }
                    }
                }
            });

            iv_delete_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            listener.onItemClick(pos);
                        }
                    }
                }
            });

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
