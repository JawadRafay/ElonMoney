package com.example.firstapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MyViewHolder> {

    private List<Products> items;
    private Context context;

    public DetailsAdapter(List<Products> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Products product = items.get(holder.getAdapterPosition());
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");

        holder.pName.setText(product.getpName());
        holder.pValue.setText(decimalFormat.format(product.gettAmount()));
        holder.p_count.setText(product.getpCount()+"");

    }


    @Override
    public int getItemCount() {

        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView pName, pValue, p_count;

        public MyViewHolder(View itemView) {
            super(itemView);

            pName = itemView.findViewById(R.id.productName);
            pValue = itemView.findViewById(R.id.amountSpent);
            p_count = itemView.findViewById(R.id.count);

        }
    }

}