package com.example.popularbonk.Fire;

import com.example.popularbonk.models.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FireInterfes {
    @GET("top-headlines")
    Call<Response> getNows(
            @Query("country") String country ,
            @Query("apiKey") String apiKey

    );

    @GET("search/movie")
    Call<Response> getNews(@Query("api_key") String apiKey,
                                        @Query("query") String nowsName);
}

