package com.project_five.serverconnection.model;

import android.graphics.Bitmap;

public class Country {

    private String name;
    private String code;
    private int rank;
    private Bitmap bitmap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String coed) {
        this.code = coed;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return name + "\n" +
                code + "\n" +
                rank;
    }
}
