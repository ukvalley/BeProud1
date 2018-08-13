package com.ukvalley.umeshkhivasara.beproud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;
import com.ukvalley.umeshkhivasara.beproud.model.singlenews.SingleNewsModel;
import com.ukvalley.umeshkhivasara.beproud.supports.SessionManager;
import com.ukvalley.umeshkhivasara.beproud.supports.SignupClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDisplay extends AppCompatActivity {

    private static final String BASE_URL_IMG = "http://www.sunclubs.org/test/test_api/public/news/";
    TextView newstilte,newscity,newsdate,newsdata;
    ImageView newsimage;
    SessionManager sessionManager;
    ProgressBar progressBarnewsdisply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_display);

        Toolbar toolbar;
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        String id=intent.getStringExtra("Key");
        progressBarnewsdisply=findViewById(R.id.newsdisplaypbar);
        sessionManager=new SessionManager(NewsDisplay.this);

        newscity=findViewById(R.id.displaynewscity);
        newsdata=findViewById(R.id.displaynewsdata);
        newsdate=findViewById(R.id.displaynewsdate);
        newstilte=findViewById(R.id.dispalynewstitle);
        newsimage=findViewById(R.id.dispalynewsimage);

        FetchSinglenews(id);
    }


    private void FetchSinglenews(String id){

        RetrofitAPI retrofitAPI= SignupClient.getClient().create(RetrofitAPI.class);

        Call<SingleNewsModel> call = retrofitAPI.GetSingleNews(id);
        call.enqueue(new Callback<SingleNewsModel>(){
            @Override
            public void onResponse(Call<SingleNewsModel> call, Response<SingleNewsModel> response) {


              //  Toast.makeText(NewsDisplay.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();
              newscity.setText(response.body().getData().getCity());
                newsdata.setText(response.body().getData().getDescription());
                newsdate.setText(response.body().getData().getDate());
                newstilte.setText(response.body().getData().getTitle());


               Glide
                        .with(NewsDisplay.this)
                        .load(BASE_URL_IMG + response.body().getData().getImage())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                // TODO: 08/11/16 handle failure
                               // movieVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                // image ready, hide progress now
                               // movieVH.mProgress.setVisibility(View.GONE);
                                return false;   // return false if you want Glide to handle everything else.
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(newsimage);




//
                    progressBarnewsdisply.setVisibility(View.INVISIBLE);



        }

            @Override
            public void onFailure(Call<SingleNewsModel> call, Throwable t) {
                progressBarnewsdisply.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==R.id.action_logout)
        {
            sessionManager.logoutUser();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        NewsDisplay.this.finish();
    }
}





