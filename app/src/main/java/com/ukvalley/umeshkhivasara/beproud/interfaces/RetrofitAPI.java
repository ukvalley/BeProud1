package com.ukvalley.umeshkhivasara.beproud.interfaces;

import com.ukvalley.umeshkhivasara.beproud.model.ImageUpload;
import com.ukvalley.umeshkhivasara.beproud.model.UpdateModel;
import com.ukvalley.umeshkhivasara.beproud.model.SignupResponsemodel;
import com.ukvalley.umeshkhivasara.beproud.model.Signupmodel;
import com.ukvalley.umeshkhivasara.beproud.model.bankdetail.UserBankInformation;
import com.ukvalley.umeshkhivasara.beproud.model.child.child;
import com.ukvalley.umeshkhivasara.beproud.model.contactdetail.UserContactInformation;
import com.ukvalley.umeshkhivasara.beproud.model.events.EventsModel;
import com.ukvalley.umeshkhivasara.beproud.model.level_income.LevelIncome;
import com.ukvalley.umeshkhivasara.beproud.model.newsmodel.NewsModel;
import com.ukvalley.umeshkhivasara.beproud.model.notification.Notification;
import com.ukvalley.umeshkhivasara.beproud.model.point_package.Beproud;
import com.ukvalley.umeshkhivasara.beproud.model.singlenews.SingleNewsModel;
import com.ukvalley.umeshkhivasara.beproud.model.singleuser.GetSingleUserSupport;
import com.ukvalley.umeshkhivasara.beproud.model.socialmedia.SocialmediaAllpost;
import com.ukvalley.umeshkhivasara.beproud.model.userpersonaldetail.UserPersonalInformation;
import com.ukvalley.umeshkhivasara.beproud.model.userstatus.Userstatus;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {
    @FormUrlEncoded
    @POST("register")
    Call<SignupResponsemodel> insertuser(@Field("user_name") String username, @Field("mobile") String mobile,
                                         @Field("city") String city,@Field("email") String email, @Field("password") String password,@Field("adharno") String str_adharno, @Field("education") String education, @Field("profession") String profession, @Field("brandname") String brandname, @Field("dream") String dream, @Field("dob") String dob, @Field("anniversary_date") String ann_date, @Field("parent_id") String str_parent_id);

    @GET("user_list")
    Call<Signupmodel> getUsers();

    @FormUrlEncoded
    @POST("login")
    Call<SignupResponsemodel> validatelogin(@Field("email") String email, @Field("password") String password);


    @GET("edit")
    Call<GetSingleUserSupport> GetUser (@Query("email")String email);


    @GET("news")
    Call<NewsModel> getAllnews(@Query("page")int pagenumber);

    @GET("get_notification")
    Call<Notification> getAllnotification(@Query("page")int pagenumber);

    @GET("singlenews")
    Call<SingleNewsModel> GetSingleNews (@Query("id")String id);

    @GET("event")
    Call<EventsModel> getAllnewsevent(@Query("page")int pagenumber);

    @GET("singleevent")
    Call<SingleNewsModel> GetSingleEvent (@Query("id")String id);



    @GET("getpersonaldetail")
    Call<UserPersonalInformation> getpersonaldetail (@Query("email") String email);

    @GET("getcontactdetail")
    Call<UserContactInformation> getcontactdetail (@Query("email") String email);

    @GET("getbankdetail")
    Call<UserBankInformation> getbankdetail (@Query("email") String email);

    @GET("getuserstatus")
    Call<Userstatus> getuserstatus (@Query("email") String email);

    @GET("child_count")
    Call<child> getchilds (@Query("email") String email);

    @GET("get_post_all")
    Call<SocialmediaAllpost> getsocialpost (@Query("page")String pagenumber);


    @GET("level_income")
    Call<LevelIncome> getpoints (@Query("id")String email);


    @GET("edit_contact/{email}")
    Call<UpdateModel> updateContact (@Path("email") String email, @Query("email") String emailid,@Query("mobile")String mobile,@Query("city") String city,@Query("address") String address,@Query("country") String  country, @Query("pincode") String pincode);

    @GET("edit_personal/{email}")
    Call<UpdateModel> updatePersonal (@Path("email") String email, @Query("dob")String dob,@Query("co_distributor_tilte") String co_distributor_tilte,@Query("co_distributor_name") String co_distributor_name,@Query("co_distributor_dob") String  co_distributor_dob, @Query("upline") String upline,@Query("designation")String designation,@Query("user_name")String firstname,@Query("anniversary_date")String ann_date);


    @GET("edit_bank/{email}")
    Call<UpdateModel> updateBank (@Path("email") String email, @Query("pannumber") String pannumber,@Query("ifsccode")String ifsccode,@Query("bankname") String bankname,@Query("branchname") String branchname,@Query("accountnum") String  accountnum, @Query("adharno") String adharno);


    @Multipart
    @POST("pan_image_upload")
    Call<ImageUpload> uploadImage(@Part("email")RequestBody email, @Part MultipartBody.Part file);


    @Multipart
    @POST("profile_image_upload")
    Call<ImageUpload> uploadprofileImage(@Part("email")RequestBody email, @Part MultipartBody.Part file);

    @Multipart
    @POST("adhar_image_upload")
    Call<ImageUpload> adhar_image_upload(@Part("email")RequestBody email, @Part MultipartBody.Part file);

    @Multipart
    @POST("video_upload")
    Call<ImageUpload> video_upload(@Part("eventid")RequestBody email, @Part MultipartBody.Part file);

}
