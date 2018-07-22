package com.ukvalley.umeshkhivasara.beproud.interfaces;

import com.ukvalley.umeshkhivasara.beproud.model.SignupResponsemodel;
import com.ukvalley.umeshkhivasara.beproud.model.Signupmodel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitAPI {
    @FormUrlEncoded
    @POST("register")
    Call<SignupResponsemodel> insertuser(@Field("user_name") String username, @Field("mobile") String mobile,
                                         @Field("city") String city,@Field("email") String email, @Field("password") String password, @Field("education") String education, @Field("profession") String profession, @Field("brandname") String brandname, @Field("dream") String dream, @Field("dob") String dob);

    @GET("user_list")
    Call<Signupmodel> getUsers();

    @FormUrlEncoded
    @POST("login")
    Call<SignupResponsemodel> validatelogin(@Field("email") String email, @Field("password") String password);
}
