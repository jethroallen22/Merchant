package com.example.merchant.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.merchant.R;
import com.example.merchant.interfaces.RecyclerViewInterface;
import com.example.merchant.models.ProductModel;

import java.util.List;

public class SpecialStatusAdapter extends RecyclerView.Adapter<SpecialStatusAdapter.ViewHolder> {

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

    public SpecialStatusAdapter(Context context, List<ProductModel> list, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public SpecialStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SpecialStatusAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.special_status_product, parent, false),recyclerViewInterface,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialStatusAdapter.ViewHolder holder, int position) {
        holder.iv_product_image.setImageBitmap(list.get(position).getBitmapImage());
        holder.tv_product_name.setText(list.get(position).getProductName());
        Log.d("INSIDE ADAPTER", list.get(position).getProductName());
        holder.tv_product_desc.setText(list.get(position).getProductDescription());
        holder.tv_special_status.setText(list.get(position).getSpecialStatus());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_product_image;
        TextView tv_product_name, tv_product_desc, tv_special_status;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface, OnItemClickListener listener) {
            super(itemView);

            iv_product_image = itemView.findViewById(R.id.iv_product_image);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_product_desc = itemView.findViewById(R.id.tv_product_desc);
            tv_special_status = itemView.findViewById(R.id.tv_special_status);

//            checkBoxProd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (recyclerViewInterface != null){
//                        int pos = getAdapterPosition();
//
//                        if (pos != RecyclerView.NO_POSITION){
//                            recyclerViewInterface.onItemClick(pos);
//                        }
//                    }
//                }
//            });
//            itemView.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    if (recyclerViewInterface != null){
//                        int pos = getAdapterPosition();
//
//                        if (pos != RecyclerView.NO_POSITION){
//                            recyclerViewInterface.onItemClick(pos);
//                        }
//                    }
//                }
//            });
        }
    }


}
