package com.example.codex_pc.mob_dco;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CODEX_PC on 09-03-2018.
 */
public class PieEntry1 implements Parcelable {

    String xdata;

    float ydata;

    public PieEntry1(String xdata, float ydata) {
        this.xdata = xdata;
        this.ydata = ydata;
    }

    public String getXdata() {
        return xdata;
    }

    public void setXdata(String xdata) {
        this.xdata = xdata;
    }

    public float getYdata() {
        return ydata;
    }

    public void setYdata(float ydata) {
        this.ydata = ydata;
    }

    protected PieEntry1(Parcel in) {
        xdata = in.readString();
        ydata = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(xdata);
        dest.writeFloat(ydata);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PieEntry1> CREATOR = new Parcelable.Creator<PieEntry1>() {
        @Override
        public PieEntry1 createFromParcel(Parcel in) {
            return new PieEntry1(in);
        }

        @Override
        public PieEntry1[] newArray(int size) {
            return new PieEntry1[size];
        }
    };
}