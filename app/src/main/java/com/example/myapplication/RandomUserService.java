package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RandomUserService {
    @GET("api/")
    Call<RandomUserResponse> getRandomUser();
}