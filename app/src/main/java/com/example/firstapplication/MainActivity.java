package com.example.firstapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements ProductsAdapter.RecyclerViewInterface {

    RecyclerView productRecycler;
    ProductsAdapter productsAdapter;
    List<Products> list;
    TextView tAmount,uAmount;
    Dialog customDialog;
    double price;
    double totalAmount,usedAmount,totalItemValue, count, used ;
    String value, formattedUsed;
    ExtendedFloatingActionButton next;
    View view;

    long v = 188700000000L;

    Products products = new Products();


    List<Products> selectedProducts= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productRecycler = findViewById(R.id.productRecycler);
        tAmount = findViewById(R.id.amountTotal);
        uAmount = findViewById(R.id.amountUsed);
        next = findViewById(R.id.export);
        view = findViewById(R.id.view);

        value = tAmount.getText().toString();

        if (tAmount != null){

        totalAmount = Double.parseDouble( tAmount.getText().toString().replaceAll(",",""));
        usedAmount = Double.parseDouble(uAmount.getText().toString());}
        else {
            Toast.makeText(this, "Data is not in it", Toast.LENGTH_SHORT).show();
        }

        productRecycler.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<Products>();
        list.add(new Products("PlayStation 5",R.drawable.playstation5,640.0,1));
        list.add(new Products("Dubai Trip",R.drawable.dubai,380.0,2));
        list.add(new Products("Diamond Ring",R.drawable.ring,350.0,3));
        list.add(new Products("Nintendo Switch",R.drawable.switc,299.0,4));
        list.add(new Products("Iphone 15 Pro Max Titanium - 1TB",R.drawable.iphone,1599.0,5));
        list.add(new Products("Samsung S23 Ultra - 1TB",R.drawable.samsung,1499.0,6));
        list.add(new Products("MacBook Pro 14' M2 Max (64GB RAM | 4TB)",R.drawable.img,4699.0,7));
        list.add(new Products("Mac Studio M2 Ultra (128GB RAM | 8TB)",R.drawable.img_1,699.0,8));
        list.add(new Products("Pro Gaming PC(I9 13900K, RTX 4090, 64GB, 4TB SSD)",R.drawable.img_2,6950.0,9));
        list.add(new Products("Open Fast Food Franchise",R.drawable.img_3,1500000.0,10));
        list.add(new Products("Netflix for 80 Years",R.drawable.img_4,16500.0,11));
        list.add(new Products("Tesla Model S Plaid",R.drawable.img_5,132000.0,12));
        list.add(new Products("Ferrari F8 Tributo",R.drawable.img_6,276000.0,13));
        list.add(new Products("Private Island, Central America (medium size)",R.drawable.img_7,4950000.0,14));
        list.add(new Products("Helicopter Bell 206",R.drawable.img_8,850000.0,15));
        list.add(new Products("Rolex Oyster",R.drawable.img_9,14000.0,16));
        list.add(new Products("Lamborghini Aventador SVJ",R.drawable.img_10,512000.0,16));
        list.add(new Products("One week in EVERY country of the planet",R.drawable.img_11,1250000.0,16));
        list.add(new Products("Jet Gulfstream G450",R.drawable.img_12,18000000.0,16));


        productsAdapter = new ProductsAdapter(this,list,this);
        productRecycler.setAdapter(productsAdapter);
        used = price;


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedProducts.size() > 0) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra("selectedProducts", (Serializable) selectedProducts);
                    intent.putExtra("used", formattedUsed);
                    startActivity(intent);
                } else {

                    showCustomDialog();

                    new Timer().schedule(new TimerTask() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    customDialog.dismiss();

                                }
                            });


                        }
                    }, 1000);

                }

            }
        });
    }
    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onBuyClick(Products products) {
       price = products.getpPrice();
       if (totalAmount > price){
           totalAmount = totalAmount - price;
           DecimalFormat formatter = new DecimalFormat("#,###,###,###");
           String formattedValue = formatter.format(totalAmount);
           tAmount.setText(formattedValue);
           used = used + price;
           usedAmount = (used / v) * 100;

           formattedUsed = formatter.format(used);

           if(selectedProducts.size()>0) {
               boolean found = false;
               for (int i = 0; i < selectedProducts.size(); i++) {
                   if (selectedProducts.get(i).getpId()==products.getpId()) {
                       selectedProducts.remove(i);
                       selectedProducts.add(i, products);
                       count = products.getpCount();
                       totalItemValue = count * price;
                       uAmount.setText(usedAmount+"");
                       products.settAmount(totalItemValue);


                        found = true;
                        break;
                   }
               }if(!found)
               {selectedProducts.add(products);
                   count = products.getpCount();
                   totalItemValue = count * price;
                   uAmount.setText(usedAmount+"");
                   products.settAmount(totalItemValue);
                  }
           }else
               selectedProducts.add(products);
       }
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onSellClick(Products products) {
        price = products.getpPrice();


            totalAmount = totalAmount + price;
            DecimalFormat formatter = new DecimalFormat("#,###,###,###");
            String formattedValue = formatter.format(totalAmount);
            tAmount.setText(formattedValue);
            used = used - price;
        usedAmount = (used / v) * 100;
        uAmount.setText(usedAmount+"");
            formattedUsed = formatter.format(used);
        count = products.getpCount();
        totalItemValue = count * price;
        products.settAmount(totalItemValue);
            if(products.getpCount()==0) {
                count = products.getpCount();
                totalItemValue = count * price;
                products.settAmount(totalItemValue);
                selectedProducts.remove(products);
            }



    }

    @Override
    public double getTotalAmount() {
        return totalAmount;
    }

    private void showCustomDialog() {
        customDialog = new Dialog(this);

        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        @SuppressLint("InflateParams") View customView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_layout, null);
        customDialog.setContentView(customView);
        // Adjust dialog parameters
        Window window = customDialog.getWindow();
        Objects.requireNonNull(customDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) customView.getLayoutParams();
        layoutParams.bottomMargin = dpToPx(20);
        layoutParams.leftMargin = dpToPx(10);
        layoutParams.rightMargin = dpToPx(10);
        customView.setLayoutParams(layoutParams);

        if (window != null) {
            window.setGravity(Gravity.BOTTOM);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        // Show the dialog
        customDialog.show();

    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

}