package com.example.firstapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;

import androidx.core.widget.NestedScrollView;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class PdfGenerator {

    static File createPdfFile(Context context) {
        File directory = context.getExternalFilesDir(null);

        if (directory != null && !directory.exists()) {
            if (!directory.mkdirs()) {
                throw new RuntimeException("Failed to create directory");
            }
        }

        String fileName = "invoice_" + new Date().getTime()+".pdf";
        return new File(directory, fileName);
    }

}

