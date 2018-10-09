package com.ukvalley.umeshkhivasara.beproud;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ukvalley.umeshkhivasara.beproud.adapter.NotificationPaginationAdapter;

import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;


import com.ukvalley.umeshkhivasara.beproud.model.notification.Datum_;
import com.ukvalley.umeshkhivasara.beproud.model.notification.Notification;
import com.ukvalley.umeshkhivasara.beproud.model.notification.Pagination;
import com.ukvalley.umeshkhivasara.beproud.supports.PaginationScrollListener;
import com.ukvalley.umeshkhivasara.beproud.supports.SignupClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {



    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 100;
    private int currentPage = PAGE_START;

    ProgressBar progressBar;

    NotificationPaginationAdapter adapter;




    private List<Datum_> results1;
    LinearLayoutManager linearLayoutManager;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar;
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar =  findViewById(R.id.main_progress_notification);
        progressBar.setVisibility(View.VISIBLE);


        final RecyclerView recyclerView = findViewById(R.id.list_notification);
        linearLayoutManager=new LinearLayoutManager(NotificationActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);










        adapter=new NotificationPaginationAdapter(NotificationActivity.this);
        recyclerView.setAdapter(adapter);


        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        loadFirstPage();




    }


    private void loadFirstPage() {
        // Log.d(TAG, "loadFirstPage: ");
        final RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<Notification> call = apiService.getAllnotification(currentPage);
        call.enqueue(new Callback<Notification>()
        {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {

                progressBar.setVisibility(View.INVISIBLE);
                //  Toast.makeText(getContext(), response.body().getStatus(), Toast.LENGTH_SHORT).show();




                //   NewsPaginationData newsPaginationData=response.body().getPagination().getData1();


                results1 = response.body().getPagination().getData();

                adapter.addAll(results1);

                TOTAL_PAGES=response.body().getPagination().getLastPage();

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;

            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {

                progressBar.setVisibility(View.INVISIBLE);
                //  Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });

    }

    private void loadNextPage()
    {

        final RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<Notification> call = apiService.getAllnotification(currentPage);
        call.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                //   NewsPaginationData newsPaginationData=response.body().getPagination().getData1();

                List<Datum_> results = response.body().getPagination().getData();
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
                TOTAL_PAGES=response.body().getPagination().getLastPage();
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
              //  Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });
    }
}
