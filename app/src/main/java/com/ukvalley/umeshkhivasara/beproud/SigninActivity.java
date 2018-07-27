package com.ukvalley.umeshkhivasara.beproud;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;
import com.ukvalley.umeshkhivasara.beproud.model.SignupResponsemodel;
import com.ukvalley.umeshkhivasara.beproud.supports.SessionManager;
import com.ukvalley.umeshkhivasara.beproud.supports.SignupClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity {
    SessionManager sessionManager;

    EditText  editText_emaillogin, editText_passwordlogin;
    TextView textView_notregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

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
                if(insertFoodResponseModel.getStatus().equals("success")){
                    Toast.makeText(SigninActivity.this, response.body().getMessage()+""+response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    sessionManager.createLoginSession(email);
                    Intent intent=new Intent(SigninActivity.this,HomeActivity.class);
                    startActivity(intent);
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
}
