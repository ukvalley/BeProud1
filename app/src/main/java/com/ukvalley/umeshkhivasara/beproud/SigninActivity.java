package com.ukvalley.umeshkhivasara.beproud;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;
import com.ukvalley.umeshkhivasara.beproud.model.SignupResponsemodel;
import com.ukvalley.umeshkhivasara.beproud.supports.Config;
import com.ukvalley.umeshkhivasara.beproud.supports.SessionManager;
import com.ukvalley.umeshkhivasara.beproud.supports.SignupClient;


import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    SessionManager sessionManager;

    EditText  editText_emaillogin, editText_passwordlogin;
    TextView textView_notregister;

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        youTubeView =  findViewById(R.id.youtube_player);
        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);



        editText_emaillogin=findViewById(R.id.edtemail);
        editText_passwordlogin=findViewById(R.id.edtpass);
        Button buttonsignin=findViewById(R.id.edt_btn_signin);

        textView_notregister=findViewById(R.id.edtnotregister);

        textView_notregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SigninActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

        sessionManager=new SessionManager(SigninActivity.this);

        buttonsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s_email=editText_emaillogin.getText().toString();
                String s_pass=editText_passwordlogin.getText().toString();
                Performlogin(s_email,s_pass);
            }
        });

    }


    private void Performlogin(final String email, String password){
        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<SignupResponsemodel> call = apiService.validatelogin(email,password);
        call.enqueue(new Callback<SignupResponsemodel>() {
            @Override
            public void onResponse(Call<SignupResponsemodel> call, Response<SignupResponsemodel> response) {

                SignupResponsemodel insertFoodResponseModel = response.body();

                //check the status code
                Toast.makeText(SigninActivity.this, response.toString(), Toast.LENGTH_SHORT).show();response.body().toString();

                if(insertFoodResponseModel.getStatus().equals("success")){
                    Toast.makeText(SigninActivity.this, response.body().getMessage()+""+response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    sessionManager.createLoginSession(email);
                    Intent intent=new Intent(SigninActivity.this,HomeActivity.class);
                    startActivity(intent);
                    SigninActivity.this.finish();
                    // progressDialog.dismiss();

                }else{
                    Toast.makeText(SigninActivity.this, response.body().getMessage()+""+response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    //  progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SignupResponsemodel> call, Throwable t) {
                Toast.makeText(SigninActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
              //  editText_username.setText(t.getMessage());
                // progressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
        }
    }
    protected Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    @Override
    public void onBackPressed () {

        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SigninActivity.super.onBackPressed();
                        quit();
                    }
                }).create().show();
    }



    public void quit () {
        Intent start = new Intent(Intent.ACTION_MAIN);
        start.addCategory(Intent.CATEGORY_HOME);
        start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(start);
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer.cueVideo("bMrSTjzfZrk"); // Plays https://www.youtube.com/watch?v=bMrSTjzfZrk
        }
    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(SigninActivity.this, RECOVERY_REQUEST).show();
        } else {

            Toast.makeText(SigninActivity.this, youTubeInitializationResult.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
