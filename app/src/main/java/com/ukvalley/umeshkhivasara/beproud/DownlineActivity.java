package com.ukvalley.umeshkhivasara.beproud;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;
import com.ukvalley.umeshkhivasara.beproud.model.child.child;
import com.ukvalley.umeshkhivasara.beproud.model.singlenews.SingleNewsModel;
import com.ukvalley.umeshkhivasara.beproud.supports.SessionManager;
import com.ukvalley.umeshkhivasara.beproud.supports.SignupClient;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownlineActivity extends AppCompatActivity {
    WebView webView_downline;
    SessionManager sessionManager;
    TextView downlinetext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downline);
        android.support.v7.widget.Toolbar toolbar;
        toolbar =  findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        downlinetext = findViewById(R.id.downline);
        downlinetext.setVisibility(View.INVISIBLE);

        webView_downline = findViewById(R.id.webview_downline);
        webView_downline.setWebViewClient(new WebViewClient());
        sessionManager = new SessionManager(DownlineActivity.this);
        webView_downline.getSettings().setLoadsImagesAutomatically(true);
        webView_downline.getSettings().setJavaScriptEnabled(true);
        webView_downline.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        String url="http://sunclubs.org/test/test_api/level_tree?id="+sessionManager.getUserDetails();
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        webView_downline.loadUrl(url);




        FetchChild(sessionManager.getUserDetails());
    }

    private void FetchChild(String id){

        RetrofitAPI retrofitAPI= SignupClient.getClient().create(RetrofitAPI.class);

        Call<child> call = retrofitAPI.getchilds(id);
        call.enqueue(new Callback<child>(){
            @Override
            public void onResponse(Call<child> call, Response<child> response) {


                downlinetext.setText("Your Downline Count is:"+response.body().getData().getCount()+"\n you will get "+response.body().getData().getUnit()+" unit of cost");
                //  Toast.makeText(NewsDisplay.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<child> call, Throwable t) {

                Toast.makeText(DownlineActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
        if (webView_downline.canGoBack())
        {
            webView_downline.goBack();
        }
        DownlineActivity.this.finish();
    }




}
