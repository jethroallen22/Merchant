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

import com.example.merchant.R;
import com.example.merchant.models.PayoutModel;

import java.util.List;

public class PayoutAdapter extends RecyclerView.Adapter<PayoutAdapter.ViewHolder>{
    Context context;
    List<PayoutModel> list;

    public PayoutAdapter(Context context, List<PayoutModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PayoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PayoutAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.payout_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PayoutAdapter.ViewHolder holder, int position) {

//        holder.iv_transac_icon.setImageResource(R.drawable.ic_baseline_money_24);
        holder.tv_transac_date.setText(list.get(position).getTransac_date());
        //Conditional Statements for icons and amount sign
//        if (list.get(position).getTransac_type().equals("cash in")){
            holder.tv_transac_type.setText("Payout");
            holder.tv_transac_amount.setText("- ₱" + String.valueOf(list.get(position).getTransac_amount()));
//        }else if(list.get(position).getTransac_type().equals("refund")){
//            holder.tv_transac_type.setText("Refund");
//            holder.tv_transac_amount.setText("- ₱" + String.valueOf(list.get(position).getTransac_amount()));
//        }else if(list.get(position).getTransac_type().equals("order")){
//            holder.tv_transac_type.setText("Order");
//            holder.tv_transac_amount.setText("- ₱" + String.valueOf(list.get(position).getTransac_amount()));
//        }

        Log.d("Index", String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView iv_transac_icon;
        TextView tv_transac_type;
        TextView tv_transac_date;
        TextView tv_transac_amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            iv_transac_icon = itemView.findViewById(R.id.iv_transac_icon);
            tv_transac_type = itemView.findViewById(R.id.tv_transac_type);
            tv_transac_date = itemView.findViewById(R.id.tv_transac_date);
            tv_transac_amount = itemView.findViewById(R.id.tv_transac_amount);

        }
    }
}
