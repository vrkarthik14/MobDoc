package com.example.codex_pc.mob_dco;

/**
 * Created by CODEX_PC on 06-03-2018.
 */

public class ExersiceProfile {
    String query;
    String gender;
    float weight_kg;
    float height_cm;
    int age;

    public ExersiceProfile(String query,String gender) {
        this.query = query;
        this.gender = gender;
        weight_kg = 72.5f;
        height_cm = 167.34f;
        age = 30;

    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getWeight_kg() {
        return weight_kg;
    }

    public void setWeight_kg(float weight_kg) {
        this.weight_kg = weight_kg;
    }

    public float getHeight_cm() {
        return height_cm;
    }

    public void setHeight_cm(float height_cm) {
        this.height_cm = height_cm;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
