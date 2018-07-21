package com.example.codex_pc.mob_dco;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CODEX_PC on 05-03-2018.
 */

public class NutrientResult1 {
    @SerializedName("nf_calories")
    float nf_calories;
    @SerializedName("nf_total_fat")
    float nf_total_fat;
    @SerializedName("nf_cholesterol")
    float nf_cholesterol;
    @SerializedName("nf_total_carbohydrate")
    float nf_total_carbohydrate;
    @SerializedName("nf_sugars")
    float nf_sugars;
    @SerializedName("nf_protein")
    float nf_protein;

    public NutrientResult1() {
    }

    public NutrientResult1(float nf_calories, float nf_total_fat, float nf_cholesterol, float nf_total_carbohydrate, float nf_sugars, float nf_protein) {
        this.nf_calories = nf_calories;
        this.nf_total_fat = nf_total_fat;
        this.nf_cholesterol = nf_cholesterol;
        this.nf_total_carbohydrate = nf_total_carbohydrate;
        this.nf_sugars = nf_sugars;
        this.nf_protein = nf_protein;
    }

    public float getNf_calories() {
        return nf_calories;
    }

    public void setNf_calories(float nf_calories) {
        this.nf_calories = nf_calories;
    }

    public float getNf_total_fat() {
        return nf_total_fat;
    }

    public void setNf_total_fat(float nf_total_fat) {
        this.nf_total_fat = nf_total_fat;
    }

    public float getNf_cholesterol() {
        return nf_cholesterol;
    }

    public void setNf_cholesterol(float nf_cholesterol) {
        this.nf_cholesterol = nf_cholesterol;
    }

    public float getNf_total_carbohydrate() {
        return nf_total_carbohydrate;
    }

    public void setNf_total_carbohydrate(float nf_total_carbohydrate) {
        this.nf_total_carbohydrate = nf_total_carbohydrate;
    }

    public float getNf_sugars() {
        return nf_sugars;
    }

    public void setNf_sugars(float nf_sugars) {
        this.nf_sugars = nf_sugars;
    }

    public float getNf_protein() {
        return nf_protein;
    }

    public void setNf_protein(float nf_protein) {
        this.nf_protein = nf_protein;
    }
}
