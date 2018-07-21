package com.example.codex_pc.mob_dco;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CODEX_PC on 09-02-2018.
 */

public class HealthDiagnosis implements Parcelable{
    @SerializedName("Issue")
    private DiagnosedIssue Issue;

    @SerializedName("Specialisation")
    private List<DiagnosedSpecialisation> Specialisation;

    public DiagnosedIssue getIssue() {
        return Issue;
    }

    public void setIssue(DiagnosedIssue issue) {
        Issue = issue;
    }

    public List<DiagnosedSpecialisation> getSpecialisation() {
        return Specialisation;
    }

    public void setSpecialisation(List<DiagnosedSpecialisation> specialisation) {
        Specialisation = specialisation;
    }

    protected HealthDiagnosis(Parcel in) {
        Issue = (DiagnosedIssue) in.readValue(DiagnosedIssue.class.getClassLoader());
        if (in.readByte() == 0x01) {
            Specialisation = new ArrayList<DiagnosedSpecialisation>();
            in.readList(Specialisation, DiagnosedSpecialisation.class.getClassLoader());
        } else {
            Specialisation = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(Issue);
        if (Specialisation == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(Specialisation);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HealthDiagnosis> CREATOR = new Parcelable.Creator<HealthDiagnosis>() {
        @Override
        public HealthDiagnosis createFromParcel(Parcel in) {
            return new HealthDiagnosis(in);
        }

        @Override
        public HealthDiagnosis[] newArray(int size) {
            return new HealthDiagnosis[size];
        }
    };
}
