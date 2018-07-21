package com.example.codex_pc.mob_dco;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CODEX_PC on 09-02-2018.
 */

public class AccessToken {
    // Token string
    /// </summary>
    @SerializedName("Token")
    private String Token;

    /// <summary>
    /// Valid period of token in seconds
    /// </summary>
    @SerializedName("ValidThrough")
    private int ValidThrough;

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public int getValidThrough() {
        return ValidThrough;
    }

    public void setValidThrough(int validThrough) {
        ValidThrough = validThrough;
    }

}
