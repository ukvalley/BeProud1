package com.ukvalley.umeshkhivasara.beproud.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.ukvalley.umeshkhivasara.beproud.adapter.PaginationAdapter;
import com.ukvalley.umeshkhivasara.beproud.R;
import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;
import com.ukvalley.umeshkhivasara.beproud.model.newsmodel.NewsModel;
import com.ukvalley.umeshkhivasara.beproud.model.newsmodel.NewsPaginationData;
import com.ukvalley.umeshkhivasara.beproud.supports.PaginationScrollListener;
import com.ukvalley.umeshkhivasara.beproud.supports.SignupClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>

 * interface.
 */
public class NewsFragment extends Fragment {

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 100;
    private int currentPage = PAGE_START;

    ProgressBar progressBar;

    PaginationAdapter adapter;




    private List<NewsPaginationData> movieResults;
    LinearLayoutManager linearLayoutManager;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    List<NewsPaginationData> results;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NewsFragment newInstance(int columnCount) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);


        progressBar =  view.findViewById(R.id.main_progress);
        progressBar.setVisibility(View.VISIBLE);

        // Set the adapter

            Context context = view.getContext();
            final RecyclerView recyclerView = view.findViewById(R.id.list);
                linearLayoutManager=new LinearLayoutManager(context);
                recyclerView.setLayoutManager(linearLayoutManager);










        adapter=new PaginationAdapter(context);
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

         return view;
    }















    private void loadFirstPage() {
       // Log.d(TAG, "loadFirstPage: ");
        final RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<NewsModel> call = apiService.getAllnews(currentPage);
        call.enqueue(new Callback<NewsModel>()
        {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {

                progressBar.setVisibility(View.INVISIBLE);
              //  Toast.makeText(getContext(), response.body().getStatus(), Toast.LENGTH_SHORT).show();




                //   NewsPaginationData newsPaginationData=response.body().getPagination().getData1();


                results = response.body().getPagination().getData();

                adapter.addAll(results);

                TOTAL_PAGES=response.body().getPagination().getLastPage();

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;

            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {

                progressBar.setVisibility(View.INVISIBLE);
              //  Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });

    }

    private void loadNextPage()
    {

        final RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<NewsModel> call = apiService.getAllnews(currentPage);
        call.enqueue(new Callback<NewsModel>() {
                         @Override
                         public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                             adapter.removeLoadingFooter();
                             isLoading = false;

                          //   NewsPaginationData newsPaginationData=response.body().getPagination().getData1();

                             List<NewsPaginationData> results = response.body().getPagination().getData();
                             adapter.addAll(results);

                             if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                             else isLastPage = true;
                             TOTAL_PAGES=response.body().getPagination().getLastPage();
                         }

                         @Override
                         public void onFailure(Call<NewsModel> call, Throwable t) {
                             Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                         }


        });
    }



}
