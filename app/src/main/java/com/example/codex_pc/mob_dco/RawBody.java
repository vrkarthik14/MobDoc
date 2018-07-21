package com.example.codex_pc.mob_dco;

import java.util.List;

/**
 * Created by CODEX_PC on 04-03-2018.
 */

public class RawBody {
    int yield;
    List<ingredients> ingredients;

    public int getYield() {
        return yield;
    }

    public void setYield(int yield) {
        this.yield = yield;
    }

    public List<RawBody.ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RawBody.ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    class ingredients{
        int quantity;
        String measureURI;
        String foodURI;


        public ingredients(int quantity, String measureURI, String foodURI) {
            this.quantity = quantity;
            this.measureURI = measureURI;
            this.foodURI = foodURI;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getMeasureURI() {
            return measureURI;
        }

        public void setMeasureURI(String measureURI) {
            this.measureURI = measureURI;
        }

        public String getFoodURI() {
            return foodURI;
        }

        public void setFoodURI(String foodURI) {
            this.foodURI = foodURI;
        }
    }
}
