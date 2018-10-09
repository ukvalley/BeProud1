package com.ukvalley.umeshkhivasara.beproud;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;
import com.ukvalley.umeshkhivasara.beproud.model.ImageUpload;
import com.ukvalley.umeshkhivasara.beproud.model.singlenews.SingleNewsModel;
import com.ukvalley.umeshkhivasara.beproud.supports.SessionManager;
import com.ukvalley.umeshkhivasara.beproud.supports.SignupClient;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDisplay extends AppCompatActivity {

    private static final String BASE_URL_IMG = "http://www.sunclubs.org/test/test_api/public/news/";
    TextView newstilte,newscity,newsdate,newsdata;
    ImageView newsimage;
    SessionManager sessionManager;
    ProgressBar progressBarnewsdisply;
    String eventid;

    ArrayList<String> elephantList;
    int index = 0;

    ImageView imageViewleft_arrow,imageView_right_arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_display);
        Toolbar toolbar;
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        String id=intent.getStringExtra("Key");
        eventid=id;
        progressBarnewsdisply=findViewById(R.id.newsdisplaypbar);
        sessionManager=new SessionManager(EventDisplay.this);

        newscity=findViewById(R.id.displaynewscity);
        newsdata=findViewById(R.id.displaynewsdata);
        newsdate=findViewById(R.id.displaynewsdate);
        newstilte=findViewById(R.id.dispalynewstitle);
        newsimage=findViewById(R.id.dispalynewsimage);

        imageView_right_arrow=findViewById(R.id.right_arrow_event);
        imageViewleft_arrow=findViewById(R.id.left_arrow_event);

        Button button_upload_video=findViewById(R.id.btn_video_upload);
        button_upload_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("video/*");
                startActivityForResult(intent, 100);


            }
        });

        FetchSinglenews(id);

        imageView_right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index <elephantList.size()){
                    ChangeImage(elephantList.get(index));
                    index++;
                }
                else index =0;

            }
        });
    }


    private void FetchSinglenews(String id){

        RetrofitAPI retrofitAPI= SignupClient.getClient().create(RetrofitAPI.class);

        Call<SingleNewsModel> call = retrofitAPI.GetSingleEvent(id);
        call.enqueue(new Callback<SingleNewsModel>(){
            @Override
            public void onResponse(Call<SingleNewsModel> call, Response<SingleNewsModel> response) {


                //  Toast.makeText(NewsDisplay.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();
                newscity.setText(response.body().getData().getCity());
                newsdata.setText(response.body().getData().getDescription());
                newsdate.setText(response.body().getData().getDate());
                newstilte.setText(response.body().getData().getTitle());

                String image_urls= response.body().getData().getImage();

                elephantList = new ArrayList<>(Arrays.asList(image_urls.split(",")));


                if (elephantList.size()>1)
                {
                    imageView_right_arrow.setVisibility(View.VISIBLE);
                    // imageViewleft_arrow.setVisibility(View.VISIBLE);
                }

                Glide
                        .with(EventDisplay.this)
                        .load(BASE_URL_IMG + elephantList.get(0))
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
        EventDisplay.this.finish();
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            //the image URI
            Uri selectedImage = data.getData();

            updateImage(selectedImage);

        }
    }


    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    public void updateImage(Uri uri)
    {
        File file=new File(getRealPathFromURI(uri));
        SessionManager sessionManager=new SessionManager(this);
        String email=sessionManager.getUserDetails();
        Toast.makeText(this, String.valueOf(uri), Toast.LENGTH_SHORT).show();
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("video", file.getName(), requestFile);

        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), eventid);
        //  RequestBody email = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<ImageUpload> call = apiService.video_upload(descBody,body);
        call.enqueue(new Callback<ImageUpload>() {
            @Override
            public void onResponse(Call<ImageUpload> call, Response<ImageUpload> response) {
                try {
                    Toast.makeText(EventDisplay.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }catch (Exception e)
                {
                    Toast.makeText(EventDisplay.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<ImageUpload> call, Throwable t) {
                //  editText_username.setText(t.getMessage());
                // progressDialog.dismiss();
                Toast.makeText(EventDisplay.this, t.getMessage()+""+t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void ChangeImage(String image_name)
    {
        Glide
                .with(EventDisplay.this)
                .load(BASE_URL_IMG + image_name)
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
                .fitCenter()
                .crossFade()
                .into(newsimage);
    }


}





