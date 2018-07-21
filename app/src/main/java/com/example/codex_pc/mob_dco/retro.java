package com.example.codex_pc.mob_dco;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by CODEX_PC on 09-02-2018.
 */

public interface retro {


    @POST("login")
    Call<AccessToken> getToken(@Header("Authorization") String authHeader);

    @GET("symptoms")
    Call<List<HelthItem>> loadSymptoms(@Query("token") String token, @Query("language")String language, @Query("format")String format);

    @GET("symptoms/proposed")
    Call<List<HelthItem>> proposedSymptoms(@Query("symptoms")String symtoms,@Query("gender")String gender,@Query("year_of_birth")Integer yob,
                                           @Query("token")String token,@Query("language")String language,@Query("format")String format);

    @GET("issues")
    Call<List<HelthItem>> issuesLoad(@Query("token") String token,@Query("language")String language,@Query("format")String format);

    @GET("issues/{id}/info")
    Call<IssueInfo> loadIssueINfor(@Path("id") Integer ID, @Query("token")String token, @Query("language")String language, @Query("format")String format);

    @GET("diagnosis")
    Call<List<HealthDiagnosis>> loadDiagnosi(@Query("symptoms")String symptoms,@Query("gender")String gender,@Query("year_of_birth")Integer yob,@Query("token")String token,@Query("language")String language,@Query("format")String format);

    @GET("redflag")
    Call<String> isRed(@Query("symptomId")Integer ID,@Query("token")String token,@Query("language")String language,@Query("format")String format);



}
