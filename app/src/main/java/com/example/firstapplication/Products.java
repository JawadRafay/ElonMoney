package com.example.firstapplication;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Products implements Serializable {

    private String pName;
    private double pPrice;

    public String gettUsed() {
        return tUsed;
    }

    public void settUsed(String tUsed) {
        this.tUsed = tUsed;
    }

    private String tUsed;
    private int pImage;
    private int pCount = 0;
    private int pId;
    private double tAmount;

    public double gettAmount() {
        return tAmount;
    }

    public void settAmount(double tAmount) {
        this.tAmount = tAmount;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public int getpCount() {
        return pCount;
    }

    public void setpCount(int pCount) {
        this.pCount = pCount;
    }




    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public int getpImage() {
        return pImage;
    }

    public void setpImage(int pImage) {
        this.pImage = pImage;
    }

    public double getpPrice() {
        return pPrice;
    }

    public void setpPrice(double pPrice) {
        this.pPrice = pPrice;
    }

    public Products(String pName, int pImage, double pPrice, int pId) {
        this.pName = pName;
        this.pImage = pImage;
        this.pPrice = pPrice;
        this.pId = pId;
    }

    public Products() {
    }

    @NonNull
    public String toString(){
        return "Product Name: "+pName+"\nProduct Price: "+pPrice+"\nProduct Count: "+pCount+"\nProduct Id: "+pId;
    }
}
