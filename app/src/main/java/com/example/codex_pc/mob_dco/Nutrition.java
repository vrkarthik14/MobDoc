package com.example.codex_pc.mob_dco;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by CODEX_PC on 04-03-2018.
 */

public interface Nutrition {


    @GET("parser")
    Call<FoodData> loadFoodData(@Query("ingr")String ingr,@Query("app_id")String app_id,@Query("app_key")String app_key);

    @POST("nutrients")
    Call<NutrientResult> loadNutritionResult(@Header("Content-type") String header, @Body  RawBody body,@Query("app_id")String app_id,@Query("app_key")String app_key);

    @POST("natural/nutrients")
    Call<NutrintionResultList> loadNutrion1(@Header("Content-type")String header,@Body Nutriquery_reto1 nutriqueryReto1,@Query("x-app-id")String app_id,@Query("x-app-key")String app_key,@Query("x-remote-user-id")String user_id);

    @POST("natural/exercise")
    Call<ExersiseResult> getExersizeReslut(@Header("Content-type")String header,@Body ExersiceProfile profile,@Query("x-app-id")String app_id,@Query("x-app-key")String app_key,@Query("x-remote-user-id")String user_id);

    @POST("natural/tags")
    Call<List<FoodComponents>> getFoodComponents(@Header("Content-type")String header, @Body Nutriquery_reto1 reto1, @Query("x-app-id")String app_id, @Query("x-app-key")String app_key, @Query("x-remote-user-id")String user_id);
 }
