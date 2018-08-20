package com.ukvalley.umeshkhivasara.beproud.fragments;

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

import com.ukvalley.umeshkhivasara.beproud.R;
import com.ukvalley.umeshkhivasara.beproud.adapter.EventPaginationAdapter;
import com.ukvalley.umeshkhivasara.beproud.adapter.PaginationAdapter;
import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;
import com.ukvalley.umeshkhivasara.beproud.model.events.Datum_;
import com.ukvalley.umeshkhivasara.beproud.model.events.EventsModel;
import com.ukvalley.umeshkhivasara.beproud.model.newsmodel.NewsModel;

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
public class EventFragment extends Fragment {

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 100;
    private int currentPage = PAGE_START;

    ProgressBar progressBar;

    EventPaginationAdapter adapter;




    private List<EventsModel> movieresults1;
    LinearLayoutManager linearLayoutManager;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    List<Datum_> results1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static EventFragment newInstance(int columnCount) {
        EventFragment fragment = new EventFragment();
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










        adapter=new EventPaginationAdapter(context);
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
        Call<EventsModel> call = apiService.getAllnewsevent(currentPage);
        call.enqueue(new Callback<EventsModel>()
        {
            @Override
            public void onResponse(Call<EventsModel> call, Response<EventsModel> response) {

                progressBar.setVisibility(View.INVISIBLE);
              //  Toast.makeText(getContext(), response.body().getStatus(), Toast.LENGTH_SHORT).show();




                //   EventsModel EventsModel=response.body().getPagination().getData1();


                results1 = response.body().getPagination().getData();

                adapter.addAll(results1);

                TOTAL_PAGES=response.body().getPagination().getLastPage();

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;

            }

            @Override
            public void onFailure(Call<EventsModel> call, Throwable t) {

                progressBar.setVisibility(View.INVISIBLE);
              //  Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });

    }

    private void loadNextPage()
    {

        final RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<EventsModel> call = apiService.getAllnewsevent(currentPage);
        call.enqueue(new Callback<EventsModel>() {
                         @Override
                         public void onResponse(Call<EventsModel> call, Response<EventsModel> response) {
                             adapter.removeLoadingFooter();
                             isLoading = false;

                          //   EventsModel EventsModel=response.body().getPagination().getData1();

                             List<Datum_> results1 = response.body().getPagination().getData();
                             adapter.addAll(results1);

                             if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                             else isLastPage = true;
                             TOTAL_PAGES=response.body().getPagination().getLastPage();
                         }

                         @Override
                         public void onFailure(Call<EventsModel> call, Throwable t) {
                             Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                         }


        });
    }



}
