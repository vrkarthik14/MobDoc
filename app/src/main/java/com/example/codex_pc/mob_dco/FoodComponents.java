package com.example.codex_pc.mob_dco;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CODEX_PC on 07-03-2018.
 */

public class FoodComponents {

    @SerializedName("ITEM")
    private String ITEM;
    @SerializedName("TAG_NAME")
    private String TAG_NAME;
    @SerializedName("MEASURE")
    private String MEASURE;
    @SerializedName("QUANTITY")
    private String QUANTITY;
    @SerializedName("TAG_IMAGE")
    private String TAG_IMAGE;

    public FoodComponents() {
    }

    public FoodComponents(String ITEM, String TAG_NAME, String MEASURE, String QUANTITY, String TAG_IMAGE) {
        this.ITEM = ITEM;
        this.TAG_NAME = TAG_NAME;
        this.MEASURE = MEASURE;
        this.QUANTITY = QUANTITY;
        this.TAG_IMAGE = TAG_IMAGE;
    }

    public String getTAG_IMAGE() {
        return TAG_IMAGE;
    }

    public void setTAG_IMAGE(String TAG_IMAGE) {
        this.TAG_IMAGE = TAG_IMAGE;
    }

    public String getMEASURE() {
        return MEASURE;
    }

    public void setMEASURE(String MEASURE) {
        this.MEASURE = MEASURE;
    }

    public String getITEM() {
        return ITEM;
    }

    public void setITEM(String ITEM) {
        this.ITEM = ITEM;
    }

    public String getTAG_NAME() {
        return TAG_NAME;
    }

    public void setTAG_NAME(String TAG_NAME) {
        this.TAG_NAME = TAG_NAME;
    }

    public String getQUANTITY() {
        return QUANTITY;
    }

    public void setQUANTITY(String QUANTITY) {
        this.QUANTITY = QUANTITY;
    }
}
