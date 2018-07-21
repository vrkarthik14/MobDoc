package com.example.codex_pc.mob_dco;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CODEX_PC on 09-02-2018.
 */

public class DiagnosedSpecialisation implements Parcelable {

    @SerializedName("ID")
    private Integer SpecialistID;

    @SerializedName("Name")
    private String Name;

    public Integer getSpecialistID() {
        return SpecialistID;
    }

    public void setSpecialistID(Integer specialistID) {
        SpecialistID = specialistID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    protected DiagnosedSpecialisation(Parcel in) {
        SpecialistID = in.readByte() == 0x00 ? null : in.readInt();
        Name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (SpecialistID == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(SpecialistID);
        }
        dest.writeString(Name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DiagnosedSpecialisation> CREATOR = new Parcelable.Creator<DiagnosedSpecialisation>() {
        @Override
        public DiagnosedSpecialisation createFromParcel(Parcel in) {
            return new DiagnosedSpecialisation(in);
        }

        @Override
        public DiagnosedSpecialisation[] newArray(int size) {
            return new DiagnosedSpecialisation[size];
        }
    };

}
