package com.example.codex_pc.mob_dco;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CODEX_PC on 08-03-2018.
 */

public class ExercisePOJO {
    @SerializedName("nf_calories")
    float nf_calories;
    @SerializedName("name")
    String name;
    @SerializedName("photo")
    photo photo;
    @SerializedName("duration_min")
    float duration_min;

    public ExercisePOJO() {
    }

    public ExercisePOJO(float nf_calories, String name, com.example.codex_pc.mob_dco.photo photo, float duration_min) {
        this.nf_calories = nf_calories;
        this.name = name;
        this.photo = photo;
        this.duration_min = duration_min;
    }

    public float getNf_calories() {
        return nf_calories;
    }

    public void setNf_calories(float nf_calories) {
        this.nf_calories = nf_calories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public com.example.codex_pc.mob_dco.photo getPhoto() {
        return photo;
    }

    public void setPhoto(com.example.codex_pc.mob_dco.photo photo) {
        this.photo = photo;
    }

    public float getDuration_min() {
        return duration_min;
    }

    public void setDuration_min(float duration_min) {
        this.duration_min = duration_min;
    }
}
