package com.ukvalley.umeshkhivasara.beproud;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ukvalley.umeshkhivasara.beproud.adapter.EventPaginationAdapter;
import com.ukvalley.umeshkhivasara.beproud.adapter.Socialpostadapter;
import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;
import com.ukvalley.umeshkhivasara.beproud.model.events.EventsModel;
import com.ukvalley.umeshkhivasara.beproud.model.socialmedia.Datum;
import com.ukvalley.umeshkhivasara.beproud.model.socialmedia.Datum_;
import com.ukvalley.umeshkhivasara.beproud.model.socialmedia.SocialmediaAllpost;
import com.ukvalley.umeshkhivasara.beproud.supports.PaginationScrollListener;
import com.ukvalley.umeshkhivasara.beproud.supports.SignupClient;

import java.util.List;

import in.juspay.godel.browser.WebviewClientImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SocialMedia extends AppCompatActivity {

 /*   private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 100;
    private int currentPage = PAGE_START;*/

    ProgressBar progressBar;

 /*   Socialpostadapter adapter;

    LinearLayoutManager linearLayoutManager;

    List<Datum_> results1;
*/
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        progressBar = findViewById(R.id.main_progress);
        progressBar.setVisibility(View.INVISIBLE);


        webView = findViewById(R.id.facebookwebview);
        webView.loadUrl("http://fb.com");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
        {
            webView.goBack();
        }
        super.onBackPressed();
    }
}
