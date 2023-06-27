package com.example.merchant.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.merchant.R;
import com.example.merchant.interfaces.RecyclerViewInterface;
import com.example.merchant.models.StoreModel;

import java.util.List;

public class VoucherStatusAdapter extends RecyclerView.Adapter<VoucherStatusAdapter.ViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    List<StoreModel> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener){
        listener = clickListener;
    }

    public VoucherStatusAdapter(Context context, List<StoreModel> list, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public VoucherStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VoucherStatusAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher, parent, false),recyclerViewInterface,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherStatusAdapter.ViewHolder holder, int position) {
        holder.tv_voucher_name.setText(list.get(position).getVoucherName());
//        Log.d("INSIDE ADAPTER", list.get(position).getProductName());
        holder.tv_voucher_min.setText("Minimum purchase of P "+list.get(position).getVoucherMin());
        holder.tv_voucher_price.setText("P "+list.get(position).getVoucherAmount());
        holder.tv_voucher_status.setText(list.get(position).getVoucher_status());
        holder.tv_voucher_end.setText("Expires on "+list.get(position).getEndDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_voucher_name, tv_voucher_min, tv_voucher_status, tv_voucher_end, tv_voucher_price;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface, OnItemClickListener listener) {
            super(itemView);
            tv_voucher_name = itemView.findViewById(R.id.tv_voucher_name);
            tv_voucher_min = itemView.findViewById(R.id.tv_voucher_min);
            tv_voucher_status = itemView.findViewById(R.id.tv_voucher_status);
            tv_voucher_price = itemView.findViewById(R.id.tv_voucher_price);
            tv_voucher_end = itemView.findViewById(R.id.tv_voucher_end);

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
