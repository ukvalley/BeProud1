package com.ukvalley.umeshkhivasara.beproud.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ukvalley.umeshkhivasara.beproud.HomeActivity;

import com.ukvalley.umeshkhivasara.beproud.R;
import com.ukvalley.umeshkhivasara.beproud.SigninActivity;
import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;
import com.ukvalley.umeshkhivasara.beproud.model.ImageUpload;
import com.ukvalley.umeshkhivasara.beproud.model.SignupResponsemodel;
import com.ukvalley.umeshkhivasara.beproud.model.UpdateModel;
import com.ukvalley.umeshkhivasara.beproud.model.bankdetail.UserBankInformation;
import com.ukvalley.umeshkhivasara.beproud.supports.LoggingInterceptor;
import com.ukvalley.umeshkhivasara.beproud.supports.SessionManager;
import com.ukvalley.umeshkhivasara.beproud.supports.SignupClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;


public class BankDetailFragment extends Fragment{

    EditText profile_edt_pannumber,progile_edt_ifsc,progile_edt_bankname,progile_edt_branchname,progile_edt_accountno,profile_editText_adhar;
    ImageView paniamge,adharimage;


    private static final String BASE_URL_IMG = "http://www.sunclubs.org/test/test_api/public/bank/";

    String imagePath;

    ProgressBar progressBar_bank;


    public BankDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_bank_detail, container, false);


        progressBar_bank=view.findViewById(R.id.bankpbar);
        profile_edt_pannumber=view.findViewById(R.id.profile_pan);
        progile_edt_ifsc=view.findViewById(R.id.profile_ifsc);
        progile_edt_bankname=view.findViewById(R.id.profile_bankname);
        progile_edt_branchname=view.findViewById(R.id.profile_branch_name);
        progile_edt_accountno=view.findViewById(R.id.profile_Account_no);
        paniamge=view.findViewById(R.id.profile_panimage);
        adharimage=view.findViewById(R.id.profile_adhar_image);
        profile_editText_adhar=view.findViewById(R.id.profile_adhar);

        final SessionManager sessionManager=new SessionManager(getContext());
        Button button_update_bank=view.findViewById(R.id.btn_update_bank_details);

       paniamge.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               requestStoragePermission();

               Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               startActivityForResult(i, 100);

           }
       });

       adharimage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               startActivityForResult(i, 101);

           }
       });

        button_update_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pannumber,ifsc,bankname,branchname,accountno;
                pannumber=profile_edt_pannumber.getText().toString();
                ifsc=progile_edt_ifsc.getText().toString();
                bankname=  progile_edt_bankname.getText().toString();
                branchname= progile_edt_branchname.getText().toString();
                accountno= progile_edt_accountno.getText().toString();
                String adharno=profile_editText_adhar.getText().toString();

                updateBankDetails(sessionManager.getUserDetails(),pannumber,ifsc,bankname,branchname,accountno,adharno);
                progressBar_bank.setVisibility(View.VISIBLE);
            }
        });



        getUserBankdetail(sessionManager.getUserDetails());


        return view;
    }

    private void updateBankDetails(String email, final String pannumber, String ifsc, String bankname, String branchname, String accountno, String adharno) {

        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<UpdateModel> call = apiService.updateBank(email,pannumber,ifsc,bankname,branchname,accountno,adharno);
        call.enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {

                progressBar_bank.setVisibility(View.INVISIBLE);
                UpdateModel updatemodel = response.body();

                //check the status code

                    //   Toast.makeText(SignupActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    //    button_makepayment.setVisibility(View.VISIBLE);

                    Toast.makeText(getContext(), "Bank Details Updated Successfully", Toast.LENGTH_SHORT).show();


                    // progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
                //   Toast.makeText(SignupActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                //   editText_username.setText(t.getMessage());
                // progressDialog.dismiss();
                progressBar_bank.setVisibility(View.INVISIBLE);
            }
        });
    }


    private void getUserBankdetail(String email) {

        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<UserBankInformation> call = apiService.getbankdetail(email);
        call.enqueue(new Callback<UserBankInformation>() {

            @Override
            public void onResponse(Call<UserBankInformation> call, Response<UserBankInformation> response) {


                //  Toast.makeText(getContext(), response.body().getData().getEmail(), Toast.LENGTH_SHORT).show();

              //  Toast.makeText(getContext(), response.body().getStatus(), Toast.LENGTH_SHORT).show();

                profile_edt_pannumber.setText(response.body().getData().getPannumber());
                progile_edt_ifsc.setText(response.body().getData().getIfsccode());
                progile_edt_bankname.setText(response.body().getData().getBankname());
                progile_edt_branchname.setText(response.body().getData().getBranchname());
                progile_edt_accountno.setText(response.body().getData().getAccountnum());
                profile_editText_adhar.setText(response.body().getData().getAdharno());

                if (!response.body().getData().getPanimage().equals(""))
                {
                    Glide
                            .with(getContext())
                            .load(BASE_URL_IMG + response.body().getData().getPanimage())
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
                            .into(paniamge);



                }


                if (!response.body().getData().getAdharimage().equals(""))
                {
                    Glide
                            .with(getContext())
                            .load(BASE_URL_IMG + response.body().getData().getAdharimage())
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
                            .into(adharimage);



                }

                progressBar_bank.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onFailure(Call<UserBankInformation> call, Throwable t) {

                progressBar_bank.setVisibility(View.INVISIBLE);
                //Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }















   public void updateImage(Uri uri)
    {
        File file=new File(getRealPathFromURI(uri));
        SessionManager sessionManager=new SessionManager(getContext());
        String email=sessionManager.getUserDetails();
        Toast.makeText(getContext(), String.valueOf(uri), Toast.LENGTH_SHORT).show();
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("panimage", file.getName(), requestFile);

        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), email);
      //  RequestBody email = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<ImageUpload> call = apiService.uploadImage(descBody,body);
        call.enqueue(new Callback<ImageUpload>() {
            @Override
            public void onResponse(Call<ImageUpload> call, Response<ImageUpload> response) {
                progressBar_bank.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ImageUpload> call, Throwable t) {
                //  editText_username.setText(t.getMessage());
                // progressDialog.dismiss();
                progressBar_bank.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), t.getMessage()+""+t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateadarImage(Uri uri)
    {
        File file=new File(getRealPathFromURI(uri));
        SessionManager sessionManager=new SessionManager(getContext());
        String email=sessionManager.getUserDetails();
        Toast.makeText(getContext(), String.valueOf(uri), Toast.LENGTH_SHORT).show();
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("adharimage", file.getName(), requestFile);

        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), email);
        //  RequestBody email = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<ImageUpload> call = apiService.adhar_image_upload(descBody,body);
        call.enqueue(new Callback<ImageUpload>() {
            @Override
            public void onResponse(Call<ImageUpload> call, Response<ImageUpload> response) {
                progressBar_bank.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<ImageUpload> call, Throwable t) {
                //  editText_username.setText(t.getMessage());
                // progressDialog.dismiss();
                progressBar_bank.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), t.getMessage()+""+t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
    }














    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            //the image URI
            Uri selectedImage = data.getData();

            updateImage(selectedImage);
            progressBar_bank.setVisibility(View.VISIBLE);

        }

        if (requestCode == 101 && resultCode == Activity.RESULT_OK && data != null) {
            //the image URI
            Uri selectedImage = data.getData();

            updateadarImage(selectedImage);
            progressBar_bank.setVisibility(View.VISIBLE);

        }
    }


    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }









}