package com.example.popularbonk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.popularbonk.Fire.FireClant;
import com.example.popularbonk.Fire.FireInterfes;
import com.example.popularbonk.models.ArticlesItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;





public class FragmentHome extends Fragment {

    public static final String API_KEY = "3a562ef68a954f42817d74642c90b2f9";
    public static final String COUNTRY = "id";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<ArticlesItem> articles = new ArrayList<>();
    private Adapter adapter;
    private HomeViewModel homeViewModel;
    private String TAG = FragmentHome.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = ViewModelProviders.of(FragmentHome.this)
                .get(HomeViewModel.class);
        homeViewModel.getListHome().observe(this, getHome);

        homeViewModel.setListMovies();

    }

    private Observer<List<ArticlesItem>> getHome = new Observer<List<ArticlesItem>>() {
        @Override
        public void onChanged(@Nullable List<ArticlesItem> newItems) {
            if (newItems != null) {
                adapter.setListData(newItems);
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        recyclerView = getView().findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        LoadJason();

    }

    private void initListener(){

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImageView imageView = view.findViewById(R.id.img);
                Intent intention = new Intent(getContext(), NowsDetailActivity.class);

                ArticlesItem article = articles.get(position);
                intention.putExtra("url", article.getUrl());
                intention.putExtra("title", article.getTitle());
                intention.putExtra("img",  article.getUrlToImage());
                intention.putExtra("date",  article.getPublishedAt());
                intention.putExtra("source",  article.getSource().getName());
                intention.putExtra("author",  article.getAuthor());

                startActivity(intention);

            }
        });

    }

    public void LoadJason(){
        FireInterfes fireInterfes = FireClant.getApiClient().create(FireInterfes.class);

        String country = Utils.getCountry();

        Call<com.example.popularbonk.models.Response> call;
        call = fireInterfes.getNows(country, API_KEY);

        call.enqueue(new Callback<com.example.popularbonk.models.Response>() {
            @Override
            public void onResponse(Call<com.example.popularbonk.models.Response> call, Response<com.example.popularbonk.models.Response> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {

                    if (!articles.isEmpty()){
                        articles.clear();
                    }

                    articles = response.body().getArticles();
                    adapter = new Adapter(articles, getContext());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    initListener();


                }else {
                    Toast.makeText(getContext(), "NO Result", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.example.popularbonk.models.Response> call, Throwable t) {

            }
        });




    }
}
