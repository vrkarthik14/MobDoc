package com.example.codex_pc.mob_dco;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by CODEX_PC on 04-03-2018.
 */

public class FoodData {
    @SerializedName("text")
    String text;
    @SerializedName("parsed")
    List<parsed> parsed;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<FoodData.parsed> getParsed() {
        return parsed;
    }

    public void setParsed(List<FoodData.parsed> parsed) {
        this.parsed = parsed;
    }

    class parsed {

        @SerializedName("food")
        food food;
        @SerializedName("quantity")
        String quantity;
        @SerializedName("measure")
        measure measure;

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public FoodData.parsed.food getFood() {
            return food;
        }

        public void setFood(FoodData.parsed.food food) {
            this.food = food;
        }

        public FoodData.parsed.measure getMeasure() {
            return measure;
        }

        public void setMeasure(FoodData.parsed.measure measure) {
            this.measure = measure;
        }

        class food{
            @SerializedName("uri")
            String uri;
            @SerializedName("label")
            String label;

            public String getUri() {
                return uri;
            }

            public void setUri(String uri) {
                this.uri = uri;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }
        }
        class measure{
            @SerializedName("uri")
            String uri;
            @SerializedName("label")
            String label;

            public String getUri() {
                return uri;
            }

            public void setUri(String uri) {
                this.uri = uri;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }
        }


    }

}