package com.example.merchant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.ViewHolder> {
    private List<String> dealsList;
    private LayoutInflater inflater;

    public DealsAdapter(Context context, List<String> dealsList) {
        this.inflater = LayoutInflater.from(context);
        this.dealsList = dealsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String deal = dealsList.get(position);
        holder.textView.setText(deal);
    }

    @Override
    public int getItemCount() {
        return dealsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}

