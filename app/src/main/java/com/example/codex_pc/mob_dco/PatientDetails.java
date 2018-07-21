package com.example.codex_pc.mob_dco;

/**
 * Created by CODEX_PC on 07-02-2018.
 */

public class PatientDetails {

    public String blood_group;
    public String Email_id;
    public String Username;
    public int age;
    public float weight;
    public float height;
    public String gender;
    public String imageUrl;

    public PatientDetails() {
    }

    public PatientDetails(String blood_group,String Email_id, String name, int age, float weight, float height, String gender,String imageUrl) {
        this.Username = name;
        this.imageUrl = imageUrl;
        this.blood_group = blood_group;
        this.Email_id = Email_id;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.gender = gender;
    }

}
