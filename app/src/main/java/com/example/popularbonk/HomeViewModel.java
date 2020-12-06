package com.example.popularbonk;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.popularbonk.Fire.FireClant;
import com.example.popularbonk.Fire.FireInterfes;
import com.example.popularbonk.models.ArticlesItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String country = BuildConfig.country;
    private MutableLiveData<List<ArticlesItem>> listMovies = new MutableLiveData<>();

    public MutableLiveData<List<ArticlesItem>> getListHome() {
        return listMovies;
    }

    void setListMovies() {
        FireInterfes fireInterfes = FireClant.getApiClient().create(FireInterfes.class);
        Call<com.example.popularbonk.models.Response> movieCall = fireInterfes.getNows(API_KEY, country);
        movieCall.enqueue(new Callback<com.example.popularbonk.models.Response>() {
            @Override
            public void onResponse(@NonNull Call<com.example.popularbonk.models.Response> call, @NonNull Response<com.example.popularbonk.models.Response> nows) {
                if (nows.body() != null) {
                    listMovies.postValue(nows.body().getArticles());
                    Log.d("onResponseMovie ", nows.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<com.example.popularbonk.models.Response> call, @NonNull Throwable t) {
                Log.d("onFailureMovie ", t.getMessage());
            }
        });
    }

    void setListSearchMovies(String name) {
        FireInterfes apiInterface = FireClant.getApiClient().create(FireInterfes.class);
        Call<com.example.popularbonk.models.Response> movieCall = apiInterface.getNews(API_KEY, name);
        movieCall.enqueue(new Callback<com.example.popularbonk.models.Response>() {
            @Override
            public void onResponse(@NonNull Call<com.example.popularbonk.models.Response> call, @NonNull Response<com.example.popularbonk.models.Response> nows) {
                if (nows.body() != null) {
                    listMovies.postValue(nows.body().getArticles());
                    Log.d("onResponseSearchMovie ", nows.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<com.example.popularbonk.models.Response> call, @NonNull Throwable t) {
                Log.d("onFailureSearchMovie ", t.getMessage());
            }
        });
    }
}
