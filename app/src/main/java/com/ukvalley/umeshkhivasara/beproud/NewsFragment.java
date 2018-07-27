package com.ukvalley.umeshkhivasara.beproud;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;
import com.ukvalley.umeshkhivasara.beproud.model.newsmodel.NewsDirectData;
import com.ukvalley.umeshkhivasara.beproud.model.newsmodel.NewsModel;
import com.ukvalley.umeshkhivasara.beproud.model.newsmodel.NewsPaginationData;
import com.ukvalley.umeshkhivasara.beproud.supports.EndlessScrollListener;
import com.ukvalley.umeshkhivasara.beproud.supports.PaginationScrollListener;
import com.ukvalley.umeshkhivasara.beproud.supports.RecyclerItemClickListener;
import com.ukvalley.umeshkhivasara.beproud.supports.SignupClient;

import java.util.ArrayList;
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


        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);

        progressBar.setVisibility(View.GONE);

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

  /*      recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                        // do whatever
                Intent intent=new Intent(getContext(),NewsDisplay.class);
                movieResults = new ArrayList<>();

                NewsPaginationData result1 = movieResults.get(position);

                intent.putExtra("keyid",result1.getId());
                Toast.makeText(getContext(), String.valueOf(position).toString(), Toast.LENGTH_SHORT).show();

                startActivity(intent);


            }

            @Override
            public void onItemLongClick(View view, int position) {

            }


                })
        );
*/
        return view;
    }


    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }*/

   /* @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }*/





    private void loadFirstPage() {
       // Log.d(TAG, "loadFirstPage: ");
        final RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<NewsModel> call = apiService.getAllnews(currentPage);
        call.enqueue(new Callback<NewsModel>()
        {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {


                Toast.makeText(getContext(), response.body().getStatus(), Toast.LENGTH_SHORT).show();




                //   NewsPaginationData newsPaginationData=response.body().getPagination().getData1();


                results = response.body().getPagination().getData();

                adapter.addAll(results);

                TOTAL_PAGES=response.body().getPagination().getLastPage();

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;

            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
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
