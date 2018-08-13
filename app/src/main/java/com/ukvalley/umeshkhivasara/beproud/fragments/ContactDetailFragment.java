package com.ukvalley.umeshkhivasara.beproud.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ukvalley.umeshkhivasara.beproud.R;
import com.ukvalley.umeshkhivasara.beproud.SignupActivity;
import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;
import com.ukvalley.umeshkhivasara.beproud.model.SignupResponsemodel;
import com.ukvalley.umeshkhivasara.beproud.model.UpdateModel;
import com.ukvalley.umeshkhivasara.beproud.model.contactdetail.UserContactInformation;
import com.ukvalley.umeshkhivasara.beproud.supports.SessionManager;
import com.ukvalley.umeshkhivasara.beproud.supports.SignupClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContactDetailFragment extends Fragment{

    EditText cont_email,cont_phone,cont_address,cont_city,cont_country,cont_pincode;

    ProgressBar progressBar_contact;

    public ContactDetailFragment() {
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

        View view = inflater.inflate(R.layout.fragment_contact_detail, container, false);

        cont_email=view.findViewById(R.id.profile_emailid);
        cont_phone=view.findViewById(R.id.profile_phone);
        cont_address=view.findViewById(R.id.profile_address);
        cont_city=view.findViewById(R.id.profile_citydetails);
        cont_country=view.findViewById(R.id.profile_country);
        cont_pincode=view.findViewById(R.id.profile_pincode);
        progressBar_contact=view.findViewById(R.id.contactpbar);

        disableEditText(cont_email);
        SessionManager sessionManager=new SessionManager(getContext());
        Button btn_update_contact_details=view.findViewById(R.id.btn_update_contact_details);
        btn_update_contact_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              String email=    cont_email.getText().toString();
              String phone=cont_phone.getText().toString();
              String address=cont_address.getText().toString();
              String  city=cont_city.getText().toString();
              String country=cont_country.getText().toString();
              String pincode=cont_pincode.getText().toString();

              updatecontactData(email,phone,address,city,country,pincode);
                progressBar_contact.setVisibility(View.VISIBLE);
            }
        });


        getUserContactdetail(sessionManager.getUserDetails());

        return view;
    }


    private void getUserContactdetail(String email) {

        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<UserContactInformation> call = apiService.getcontactdetail(email);
        call.enqueue(new Callback<UserContactInformation>() {

            @Override
            public void onResponse(Call<UserContactInformation> call, Response<UserContactInformation> response) {

              //  Toast.makeText(getContext(), response.body().getData().getEmail(), Toast.LENGTH_SHORT).show();
                cont_email.setText(response.body().getData().getEmail());
                cont_phone.setText(response.body().getData().getMobile());
                cont_address.setText(response.body().getData().getAddress());
                cont_city.setText(response.body().getData().getCity());
                cont_country.setText(response.body().getData().getCountry());
                cont_pincode.setText(response.body().getData().getPincode());

                progressBar_contact.setVisibility(View.INVISIBLE);




            }

            @Override
            public void onFailure(Call<UserContactInformation> call, Throwable t) {

                //Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar_contact.setVisibility(View.INVISIBLE);


            }
        });
    }


    private void updatecontactData(String email, String phone, String address, String city, String country, String pincode){
        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<UpdateModel> call = apiService.updateContact(email,email,phone,city,address,country,pincode);
        call.enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {

                UpdateModel updatemodel = response.body();

                //check the status code
                if(updatemodel.getStatus().equals("success")){
                 //   Toast.makeText(SignupActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                //    button_makepayment.setVisibility(View.VISIBLE);
                    progressBar_contact.setVisibility(View.INVISIBLE);

                    Toast.makeText(getContext(), "Contact Details Updated Successfully", Toast.LENGTH_SHORT).show();


                    // progressDialog.dismiss();
                }else{
                   // Toast.makeText(SignupActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    //  progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
             //   Toast.makeText(SignupActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
             //   editText_username.setText(t.getMessage());
                // progressDialog.dismiss();
                progressBar_contact.setVisibility(View.INVISIBLE);

            }
        });
    }
    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
}