package com.example.codex_pc.mob_dco;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CODEX_PC on 09-02-2018.
 */

public class HelthItem {

    @SerializedName("ID")
    private int ID;

    @SerializedName("Name")
    private String Name;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
