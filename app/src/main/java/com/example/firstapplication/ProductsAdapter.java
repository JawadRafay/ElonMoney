package com.example.firstapplication;

import android.annotation.SuppressLint;
import android.content.Context;
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

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    private List<Products> items;
    private Context context;

    public ProductsAdapter(RecyclerViewInterface recyclerViewInterface, List<Products> items, Context context) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.items = items;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Products product = items.get(holder.getAdapterPosition());
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");

        holder.pName.setText(product.getpName());
        holder.pImage.setImageResource(product.getpImage());
        holder.pValue.setText(decimalFormat.format(product.getpPrice()));
        holder.p_count.setText(product.getpCount()+"");

        holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double price = product.getpPrice();
                double totalAmount = recyclerViewInterface.getTotalAmount(); // Add a method to get the total amount from your activity
                if (totalAmount >= price) {
                    product.setpCount(product.getpCount() + 1);
                    holder.p_count.setText(String.valueOf(product.getpCount()));
                    recyclerViewInterface.onBuyClick(product);
                } else {
                    Toast.makeText(context, "Insufficient funds", Toast.LENGTH_SHORT).show();
                    // You can handle this situation in another way, depending on your requirements
                }
            }
        });
        holder.sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(product.getpCount() == 0){
                    return;
                }
                product.setpCount(product.getpCount()-1);
                holder.p_count.setText(String.valueOf(product.getpCount()));
                recyclerViewInterface.onSellClick(product);

            }
        });

    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView pImage;
        TextView pName, pValue, p_count;
        MaterialButton buy, sell;

        public MyViewHolder(View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            pImage = itemView.findViewById(R.id.productImage);
            pName = itemView.findViewById(R.id.productName);
            pValue = itemView.findViewById(R.id.pValue);
            buy = itemView.findViewById(R.id.btn_buy);
            sell = itemView.findViewById(R.id.btn_sell);
            p_count = itemView.findViewById(R.id.item_count);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION)
                            recyclerViewInterface.onItemClick(pos);
                    }
                }
            });

        }
    }

    interface RecyclerViewInterface {
        void onItemClick(int position);
        void onBuyClick(Products products);
        void onSellClick(Products products);
        double getTotalAmount();
    }
}