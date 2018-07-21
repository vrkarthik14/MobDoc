package com.example.codex_pc.mob_dco;

/**
 * Created by CODEX_PC on 05-03-2018.
 */

public class Nutriquery_reto1 {

    String query,aggregate,timezone,consumed_at,lat,lng,locale;
    int num_servings,meal_type;
    boolean line_delimited,include_subrecipe,use_branded_foods,use_raw_foods;


    public Nutriquery_reto1(String query, int num_servings) {
        this.query = query;
        this.num_servings = num_servings;
        aggregate =  "string";
        line_delimited  = false;
         use_raw_foods  = false;
         include_subrecipe  = false;
         timezone  =  "US/Eastern";
         consumed_at  = null;
         lat  = null;
         lng  = null;
         meal_type  = 0;
         use_branded_foods  = false;
         locale  =  "en_US";
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getAggregate() {
        return aggregate;
    }

    public void setAggregate(String aggregate) {
        this.aggregate = aggregate;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getConsumed_at() {
        return consumed_at;
    }

    public void setConsumed_at(String consumed_at) {
        this.consumed_at = consumed_at;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public int getNum_servings() {
        return num_servings;
    }

    public void setNum_servings(int num_servings) {
        this.num_servings = num_servings;
    }

    public int getMeal_type() {
        return meal_type;
    }

    public void setMeal_type(int meal_type) {
        this.meal_type = meal_type;
    }

    public boolean isLine_delimited() {
        return line_delimited;
    }

    public void setLine_delimited(boolean line_delimited) {
        this.line_delimited = line_delimited;
    }

    public boolean isInclude_subrecipe() {
        return include_subrecipe;
    }

    public void setInclude_subrecipe(boolean include_subrecipe) {
        this.include_subrecipe = include_subrecipe;
    }

    public boolean isUse_branded_foods() {
        return use_branded_foods;
    }

    public void setUse_branded_foods(boolean use_branded_foods) {
        this.use_branded_foods = use_branded_foods;
    }

    public boolean isUse_raw_foods() {
        return use_raw_foods;
    }

    public void setUse_raw_foods(boolean use_raw_foods) {
        this.use_raw_foods = use_raw_foods;
    }
}
