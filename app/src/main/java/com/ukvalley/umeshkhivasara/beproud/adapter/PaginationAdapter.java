package com.ukvalley.umeshkhivasara.beproud.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ukvalley.umeshkhivasara.beproud.NewsDisplay;
import com.ukvalley.umeshkhivasara.beproud.R;
import com.ukvalley.umeshkhivasara.beproud.SignupActivity;
import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;
import com.ukvalley.umeshkhivasara.beproud.model.SignupResponsemodel;
import com.ukvalley.umeshkhivasara.beproud.model.newsmodel.NewsPaginationData;
import com.ukvalley.umeshkhivasara.beproud.supports.SignupClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Suleiman on 19/10/16.
 */

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final String BASE_URL_IMG = "http://www.sunclubs.org/test/test_api/public/news/";

    private List<NewsPaginationData> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapter(Context context) {
        this.context = context;
        movieResults = new ArrayList<>();
    }

    public List<NewsPaginationData> getMovies() {
        return movieResults;
    }

    public void setMovies(List<NewsPaginationData> movieResults) {
        this.movieResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.fragment_news, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }

        return viewHolder;
    }



    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.fragment_news, parent, false);


        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final NewsPaginationData result = movieResults.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent  intent=new Intent(context,NewsDisplay.class);
                        String id=String.valueOf(result.getId());
                        intent.putExtra("Key",id);
                        context.startActivity(intent);
                    }
                });

            /*    movieVH.like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                    }
                });*/

               /* movieVH.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
*/
                movieVH.mMovieTitle.setText(result.getTitle());
                movieVH.mMovieTitle.setClickable(true);
                movieVH.mMovieTitle.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                      // Toast.makeText(context, String.valueOf(result.getId()), Toast.LENGTH_SHORT).show();

                       Intent  intent=new Intent(context,NewsDisplay.class);
                       String id=String.valueOf(result.getId());
                       intent.putExtra("Key",id);
                       context.startActivity(intent);
                   }
               });

                movieVH.mProgress.setVisibility(View.GONE);
               movieVH.mYear.setText(result.getDate());
                movieVH.mcity.setText(result.getCity());
                movieVH.mMovieDesc.setText(result.getDescription());

                /**
                 * Using Glide to handle image loading.
                 * Learn more about Glide here:
                 * <a href="http://blog.grafixartist.com/image-gallery-app-android-studio-1-4-glide/" />
                 */
                String image_urls= result.getImage();
                ArrayList<String> elephantList = new ArrayList<> (Arrays.asList(image_urls.split(",")));
             Glide
                        .with(context)
                        .load(BASE_URL_IMG + elephantList.get(0))
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                // TODO: 08/11/16 handle failure
                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                // image ready, hide progress now
                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;   // return false if you want Glide to handle everything else.
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(movieVH.mPosterImg);


        }

    }

    @Override
    public int getItemCount() {
        return movieResults == null ? 0 : movieResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(NewsPaginationData r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<NewsPaginationData> moveResults) {
        for (NewsPaginationData result : moveResults) {
            add(result);
        }
    }

    public void remove(NewsPaginationData r) {
        int position = movieResults.indexOf(r);
        if (position > -1) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new NewsPaginationData());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        NewsPaginationData result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public NewsPaginationData getItem(int position) {
        return movieResults.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class MovieVH extends RecyclerView.ViewHolder {
        private TextView mMovieTitle;
        private TextView mMovieDesc;
        private TextView mYear; // displays "year | language"
        private ImageView mPosterImg;
        private  TextView mcity;
       // private Button like,comment;
        private ProgressBar mProgress;

        public MovieVH(View itemView) {
            super(itemView);

            mMovieTitle = (TextView) itemView.findViewById(R.id.movie_title);
            mMovieDesc = (TextView) itemView.findViewById(R.id.movie_desc);
            mYear = (TextView) itemView.findViewById(R.id.movie_year);
            mPosterImg = (ImageView) itemView.findViewById(R.id.movie_poster);
            mProgress = (ProgressBar) itemView.findViewById(R.id.movie_progress);
            mcity=itemView.findViewById(R.id.movie_city);
           /* like=itemView.findViewById(R.id.news_like);
            comment=itemView.findViewById(R.id.news_comment);*/

        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


   /* private void Like(String username, String mobile, String city, final String email, String password, String str_adharno, String education, final String profession, String brandname, String dream, String dob, String ann_date, String str_parent_id){
        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<SignupResponsemodel> call = apiService.insertuser(username,mobile,city,email,password, str_adharno, education,profession,brandname,dream,dob,ann_date,str_parent_id);
        call.enqueue(new Callback<SignupResponsemodel>() {
            @Override
            public void onResponse(Call<SignupResponsemodel> call, Response<SignupResponsemodel> response) {

                SignupResponsemodel insertFoodResponseModel = response.body();

                if(insertFoodResponseModel.getStatus().equals("success")){

                  //  Toast.makeText(SignupActivity.this, "Registration Success Please Make Payment", Toast.LENGTH_SHORT).show();
                  //  progressBar_signup.setVisibility(View.INVISIBLE);
                  //  button_makepayment.setVisibility(View.VISIBLE);

                }else{
                  //  progressBar_signup.setVisibility(View.INVISIBLE);
                    //Toast.makeText(SignupActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<SignupResponsemodel> call, Throwable t) {
                progressBar_signup.setVisibility(View.INVISIBLE);
                Toast.makeText(SignupActivity.this, "Something went wrong "+t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/



}