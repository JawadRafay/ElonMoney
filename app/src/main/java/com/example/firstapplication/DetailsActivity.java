package com.example.firstapplication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class DetailsActivity extends AppCompatActivity  {

    ArrayList<Products> selectedProducts;
    RecyclerView recyclerView;
    DetailsAdapter detailsAdapter;
    ExtendedFloatingActionButton generate;
    TextView tUsed;
    String totalItemValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tUsed = findViewById(R.id.totalSpent);
        generate = findViewById(R.id.generate_invoice);

        selectedProducts = (ArrayList<Products>)getIntent().getSerializableExtra("selectedProducts");
        totalItemValue = getIntent().getStringExtra("used");
        tUsed.setText(totalItemValue);



        recyclerView = findViewById(R.id.detailsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        detailsAdapter = new DetailsAdapter(selectedProducts,this);
        recyclerView.setAdapter(detailsAdapter);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustomDialog();
            }
        });
    }
    private void openCustomDialog() {
        Dialog customDialog = new Dialog(this);
        customDialog.setContentView(R.layout.custom_dialogue);
        Objects.requireNonNull(customDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.setCancelable(true);

        TextInputEditText editText = customDialog.findViewById(R.id.name_input);
        MaterialButton confirm = customDialog.findViewById(R.id.confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = editText.getText().toString();

                if (name.length() < 3) {
                    editText.setError("Enter a valid name");
                } else {

                    showCustomDialog();

                    new Timer().schedule(new TimerTask() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    @SuppressLint({"NewApi", "LocalSuppress"})
                                    Intent intent = new Intent(DetailsActivity.this, InvoiceActivity.class);
                                    intent.putExtra("selectedProducts", (Serializable) selectedProducts);
                                    intent.putExtra("used", totalItemValue);
                                    intent.putExtra("name", name);
                                    startActivity(intent);
                                    customDialog.dismiss();

                                }
                            });


                        }
                    }, 1000);
                }
            }
        });

        customDialog.show();
    }

    private void showCustomDialog() {
        // Create a custom dialog
        final Dialog customDialog = new Dialog(this);

        // Set the content view
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        @SuppressLint("InflateParams") View customView = LayoutInflater.from(this).inflate(R.layout.success_dialog_layout, null);
        customDialog.setContentView(customView);
        // Adjust dialog parameters
        Window window = customDialog.getWindow();
        Objects.requireNonNull(customDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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