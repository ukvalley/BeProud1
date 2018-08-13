package com.ukvalley.umeshkhivasara.beproud;


import android.Manifest;
import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ukvalley.umeshkhivasara.beproud.fragments.BankDetailFragment;
import com.ukvalley.umeshkhivasara.beproud.fragments.ContactDetailFragment;
import com.ukvalley.umeshkhivasara.beproud.fragments.PersonalDetailFragment;
import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;
import com.ukvalley.umeshkhivasara.beproud.model.ImageUpload;
import com.ukvalley.umeshkhivasara.beproud.model.singleuser.GetSingleUserSupport;
import com.ukvalley.umeshkhivasara.beproud.supports.SessionManager;
import com.ukvalley.umeshkhivasara.beproud.supports.SignupClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ProfileActivity extends AppCompatActivity {

    SessionManager sessionManager;

    private TabLayout tabLayout;
    private ViewPager viewPager;
   android.support.v7.widget.Toolbar toolbar;

   ImageView profile_pic;

   TextView profile_title,profile_education,profile_city;

    private static final String BASE_URL_IMG = "http://www.sunclubs.org/test/test_api/public/profile/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile2_activity);

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewPager =  findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
       // actionBar.hide();

        profile_city=findViewById(R.id.profile2_city);
        profile_education=findViewById(R.id.profile2_education);
        profile_title=findViewById(R.id.profile2_title);

        profile_pic=findViewById(R.id.profile_pic);

        sessionManager=new SessionManager(ProfileActivity.this);



        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                requestStoragePermission();

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });


        getUser(sessionManager.getUserDetails());



    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
    }
    private void getUser(String email){

        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<GetSingleUserSupport> call = apiService.GetUser(email);
        call.enqueue(new Callback<GetSingleUserSupport>(){

            @Override
            public void onResponse(Call<GetSingleUserSupport> call, Response<GetSingleUserSupport> response) {

             //   Toast.makeText(ProfileActivity.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();

                profile_city.setText(response.body().getData().getCity().toString());
                profile_title.setText(response.body().getData().getUserName().toString());
                profile_education.setText(response.body().getData().getEducation().toString());



                if (!response.body().getData().getProfile_image().equals(""))
                {
                    Glide
                            .with(ProfileActivity.this)
                            .load(BASE_URL_IMG + response.body().getData().getProfile_image())
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
                            .into(profile_pic);

                }


            }

            @Override
            public void onFailure(Call<GetSingleUserSupport> call, Throwable t) {

                Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PersonalDetailFragment(), "Personal");
        adapter.addFragment(new ContactDetailFragment(), "Contact");
        adapter.addFragment(new BankDetailFragment(), "Bank");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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
        MultipartBody.Part body = MultipartBody.Part.createFormData("profile_image", file.getName(), requestFile);

        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), email);
        //  RequestBody email = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<ImageUpload> call = apiService.uploadprofileImage(descBody,body);
        call.enqueue(new Callback<ImageUpload>() {
            @Override
            public void onResponse(Call<ImageUpload> call, Response<ImageUpload> response) {
                Toast.makeText(ProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ImageUpload> call, Throwable t) {
                //  editText_username.setText(t.getMessage());
                // progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, t.getMessage()+""+t.getCause(), Toast.LENGTH_SHORT).show();
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
        ProfileActivity.this.finish();
        super.onBackPressed();
    }
}
