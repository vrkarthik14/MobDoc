package com.example.codex_pc.mob_dco;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CODEX_PC on 09-03-2018.
 */

public class photo {
    @SerializedName("thumb")
    private String thumb;

    public photo() {
    }

    public photo(String thumb) {
        this.thumb = thumb;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
