package com.alvarezaaronai.sed;

import com.alvarezaaronai.sed.models.Physician;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AWSPhysicianInfoAPI {

    @GET("physician")
    Call<Physician> getPhysician();
}
