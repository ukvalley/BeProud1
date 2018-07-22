package com.ukvalley.umeshkhivasara.beproud.supports;

import android.content.Context;
import android.widget.Toast;

import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;
import com.ukvalley.umeshkhivasara.beproud.model.Signupmodel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class getUsers {


    private void getData(final Context context){
        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<com.ukvalley.umeshkhivasara.beproud.model.Signupmodel> call = apiService.getUsers();
        call.enqueue(new Callback<com.ukvalley.umeshkhivasara.beproud.model.Signupmodel>() {
            @Override
            public void onResponse(Call<com.ukvalley.umeshkhivasara.beproud.model.Signupmodel> call, Response<com.ukvalley.umeshkhivasara.beproud.model.Signupmodel> response) {

                com.ukvalley.umeshkhivasara.beproud.model.Signupmodel insertFoodResponseModel = response.body();

                //check the status code
                if(insertFoodResponseModel.getStatus()=="success"){
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    // progressDialog.dismiss();
                }else{
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    //  progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Signupmodel> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
               // editText_mobilenumber.setText(t.getMessage());
                // progressDialog.dismiss();
            }
        });
    }

}
