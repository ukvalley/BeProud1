package com.ukvalley.umeshkhivasara.beproud.supports;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupClient {
   // public static final String URL     = "https://132.148.16.199/beproudadmin/";
    public static final String URL     = "http://sunclubs.org/test/test_api/";
    public static Retrofit RETROFIT = null;

    public static Retrofit getClient() {
        if (RETROFIT==null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();


            RETROFIT = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return RETROFIT;
    }







}
