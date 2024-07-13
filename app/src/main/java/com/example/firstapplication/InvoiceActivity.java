package com.example.firstapplication;


import static com.itextpdf.kernel.pdf.PdfName.BaseFont;
import static com.itextpdf.kernel.pdf.PdfName.Color;
import static com.itextpdf.kernel.pdf.PdfName.Resources;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.WebColors;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

@RequiresApi(api = Build.VERSION_CODES.O)
public class InvoiceActivity extends AppCompatActivity {

    ArrayList<Products> selectedProducts;
    Products products = new Products();
    Dialog customDialog;
    RecyclerView recyclerView;
    String totalItemValue, name;
    private static final int REQUEST_PERMISSION_CODE = 123;
    TextView uName, totalSpent, date, time;
    ExtendedFloatingActionButton export, share;
    InvoiceAdapter invoiceAdapter;
    Date currentDate, currentTime;
    SimpleDateFormat dateFormat,  timeFormat;
    File pdfFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        selectedProducts = (ArrayList<Products>) getIntent().getSerializableExtra("selectedProducts");
        totalItemValue = getIntent().getStringExtra("used");
        name = getIntent().getStringExtra("name");

        totalSpent = findViewById(R.id.total);
        uName = findViewById(R.id.name);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        export = findViewById(R.id.export);
        share = findViewById(R.id.share);

        totalSpent.setText(totalItemValue);
        uName.setText(name);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        invoiceAdapter = new InvoiceAdapter(selectedProducts, this);
        recyclerView.setAdapter(invoiceAdapter);

        currentDate = new Date();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault());
        date.setText(dateFormat.format(currentDate));

        currentTime = new Date();
        timeFormat = new SimpleDateFormat("hh:mm:ss a", java.util.Locale.getDefault());
        time.setText(timeFormat.format(currentTime));

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showCustomDialog();

                new Timer().schedule(new TimerTask() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                checkExternalStoragePermission();

                                customDialog.dismiss();

                            }
                        });


                    }
                }, 1000);

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();

                new Timer().schedule(new TimerTask() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                sharePdf();

                                customDialog.dismiss();

                            }
                        });


                    }
                }, 1000);


            }
        });
    }

    private void checkExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE);
        } else {
            generatePdf();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                generatePdf();

            } else {
                Toast.makeText(this, "Permission denied. Cannot generate PDF.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void generatePdf() {

        pdfFile = PdfGenerator.createPdfFile(InvoiceActivity.this);

        PdfWriter pdfWriter = null;
        try {
            pdfWriter = new PdfWriter(pdfFile);
        } catch (FileNotFoundException e) {
        e.printStackTrace();        }
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);

        Document document = new Document(pdfDocument);


        try {
            document.add(new Paragraph("Elon Money \n").setTextAlignment(TextAlignment.CENTER).setFontSize(36).setFont(PdfFontFactory.createFont(FontConstants.TIMES_ROMAN)));
        } catch (IOException e) {
        e.printStackTrace();
        }
        document.add(new Paragraph("Date: "+dateFormat.format(currentDate)+" ").setTextAlignment(TextAlignment.RIGHT).setFontSize(16).setFontColor(WebColors.getRGBColor("#F86E6E")));
        document.add(new Paragraph("Time: "+timeFormat.format(currentTime)+" ").setTextAlignment(TextAlignment.RIGHT).setFontSize(16).setFontColor(WebColors.getRGBColor("#F86E6E")));
        document.add(new Paragraph("Invoice No: 123456 \n\n").setTextAlignment(TextAlignment.RIGHT).setFontSize(16).setFontColor(WebColors.getRGBColor("#F86E6E")));
        document.add(new Paragraph("To: "+name+"\n\n").setTextAlignment(TextAlignment.LEFT).setFontSize(16));

        float columnWidth[] = {285, 175, 65};
        Table table = new Table(columnWidth);

        table.addCell(new Cell().add(new Paragraph("Name").setTextAlignment(TextAlignment.CENTER)));
        table.addCell(new Cell().add(new Paragraph("Price (USD)").setTextAlignment(TextAlignment.CENTER)));
        table.addCell(new Cell().add(new Paragraph("Qty.").setTextAlignment(TextAlignment.CENTER)));

        for (int i = 0; i < selectedProducts.size(); i++) {

            DecimalFormat formatter = new DecimalFormat("#,###,###,###");
            String formattedValue = formatter.format(selectedProducts.get(i).getpPrice());
            table.addCell(new Cell().add(new Paragraph(selectedProducts.get(i).getpName()).setTextAlignment(TextAlignment.CENTER)).setPadding(10).setVerticalAlignment(VerticalAlignment.MIDDLE).setFontColor(WebColors.getRGBColor("#0062AF")));
            table.addCell(new Cell().add(new Paragraph(formattedValue).setTextAlignment(TextAlignment.CENTER)).setPadding(10).setVerticalAlignment(VerticalAlignment.MIDDLE));
            table.addCell(new Cell().add(new Paragraph(selectedProducts.get(i).getpCount()+"").setTextAlignment(TextAlignment.CENTER)).setPadding(10).setVerticalAlignment(VerticalAlignment.MIDDLE).setFontColor(WebColors.getRGBColor("#0062AF")));

        }

        document.add(table);

        Paragraph p1 = new Paragraph();
        p1.add("\nTotal: ").setFontSize(16).setFontColor(WebColors.getRGBColor("#0062AF"));
        p1.add(totalItemValue).setFontSize(16).setBold();
        p1.add(" USD").setFontSize(16).setBold();

        try {
            document.add(p1.setTextAlignment(TextAlignment.RIGHT));
        } catch (Exception e) {
        e.printStackTrace();}


        document.add(new Paragraph("\n\n* This invoice is generated by Elon Money with the purpose of just fun. Share with your family and friends as soon as possible.").setTextAlignment(TextAlignment.CENTER).setFontSize(14).setFontColor(WebColors.getRGBColor("#838383")));
        document.add(new Paragraph("\n\nThank you for your purchase from Elon Money").setTextAlignment(TextAlignment.CENTER).setFontSize(18).setFontColor(WebColors.getRGBColor("#F86E6E")));


        document.close();

        openPdf(pdfFile);

    }


    private void openPdf(File pdfFile) {

        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("application/pdf");

        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe) {
            Uri pdfUri = FileProvider.getUriForFile(
                    this,
                    "com.example.firstapplication.provider",
                    pdfFile);

            intent.setDataAndType(pdfUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        }
    }

    public void sharePdf() {

        generatePdf();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        Uri pdfUri = FileProvider.getUriForFile(
                this,
                "com.example.firstapplication.provider",
                pdfFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(shareIntent, "Share PDF using"));
    }

    private void showCustomDialog() {
        customDialog = new Dialog(this);

        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        @SuppressLint("InflateParams") View customView = LayoutInflater.from(this).inflate(R.layout.success_dialog_layout, null);
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
