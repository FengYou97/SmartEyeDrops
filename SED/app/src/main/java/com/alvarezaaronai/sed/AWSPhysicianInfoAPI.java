package com.alvarezaaronai.sed;

import com.alvarezaaronai.sed.Models.Physician;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AWSPhysicianInfoAPI {

    @GET("physician")
    Call<Physician> getPhysician();
}
