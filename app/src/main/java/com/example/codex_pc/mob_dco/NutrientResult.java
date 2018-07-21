package com.example.codex_pc.mob_dco;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by CODEX_PC on 04-03-2018.
 */

public class NutrientResult {

    @SerializedName("calories")
    String calories;

    @SerializedName("totalWeight")
    String totalWeight;

    @SerializedName("dietLabels")
    List<String> dietLabels;

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }

    public List<String> getDietLabels() {
        return dietLabels;
    }

    public void setDietLabels(List<String> dietLabels) {
        this.dietLabels = dietLabels;
    }
}
