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
import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;
import com.ukvalley.umeshkhivasara.beproud.model.UpdateModel;
import com.ukvalley.umeshkhivasara.beproud.model.userpersonaldetail.UserPersonalInformation;
import com.ukvalley.umeshkhivasara.beproud.supports.SessionManager;
import com.ukvalley.umeshkhivasara.beproud.supports.SignupClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PersonalDetailFragment extends Fragment{

    EditText textView_profile_dob,profile_distributorid,profile_distributor_title,profile_codistributor_name,profile_co_distributor_dob,profile_upline,profile_designation,profile_firstname,profile_ann_date;

    ProgressBar progressBar_personal;

    public PersonalDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_personal_detail, container, false);

      //  profile_lastname=view.findViewById(R.id.profile_lastname);
        textView_profile_dob = view.findViewById(R.id.profile_dob);
        profile_codistributor_name=view.findViewById(R.id.profile_co_distributor_name);
        profile_designation=view.findViewById(R.id.profile_designation);
        profile_distributorid=view.findViewById(R.id.profile_distributor_id);
        profile_upline=view.findViewById(R.id.profile_upline);
        profile_distributor_title=view.findViewById(R.id.profile_distributor_co_title);
        profile_co_distributor_dob=view.findViewById(R.id.profile_co_distributor_dob);
        profile_firstname=view.findViewById(R.id.profile_firstname);

        profile_ann_date=view.findViewById(R.id.profile_ann_date);
        progressBar_personal=view.findViewById(R.id.personalpbar);


        final SessionManager sessionManager=new SessionManager(getContext());
        disableEditText(profile_distributorid);

        Button button_update_personal=view.findViewById(R.id.btn_update_personal_details);
        button_update_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            // String lastname=   profile_lastname.getText().toString();
             String dob=textView_profile_dob.getText().toString();
             String d_title=profile_distributor_title.getText().toString();
             String d_name=profile_codistributor_name.getText().toString();
             String d_dob=profile_co_distributor_dob.getText().toString();
             String upline=profile_upline.getText().toString();
             String designation=profile_designation.getText().toString();
             String fullname=profile_firstname.getText().toString();
             String ann_date=profile_ann_date.getText().toString();

             String email=sessionManager.getUserDetails();
             updatePersoanelData(email,dob,d_title,d_name,d_dob,upline,designation,fullname,ann_date);
             progressBar_personal.setVisibility(View.VISIBLE);
            }
        });



        getUserPersonal(sessionManager.getUserDetails());
        return view;



    }


    private void getUserPersonal(String email) {

        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<UserPersonalInformation> call = apiService.getpersonaldetail(email);
        call.enqueue(new Callback<UserPersonalInformation>() {

            @Override
            public void onResponse(Call<UserPersonalInformation> call, Response<UserPersonalInformation> response) {

              //  profile_lastname.setText(response.body().getData().getLastname());
              //  Toast.makeText(getContext(), response.body().getData().getDob(), Toast.LENGTH_SHORT).show();
               textView_profile_dob.setText(response.body().getData().getDob());
               profile_distributorid.setText(response.body().getData().getDistributoId());
               profile_distributor_title.setText(response.body().getData().getCoDistributorTilte());
               profile_codistributor_name.setText(response.body().getData().getCoDistributorName());
               profile_co_distributor_dob.setText(response.body().getData().getCoDistributorDob());
               profile_upline.setText(response.body().getData().getUpline());
               profile_designation.setText(response.body().getData().getDesignation());
               profile_firstname.setText(response.body().getData().getFirstname());
               profile_ann_date.setText(response.body().getData().getAnniversary_date());
               progressBar_personal.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onFailure(Call<UserPersonalInformation> call, Throwable t) {
                progressBar_personal.setVisibility(View.INVISIBLE);
                //Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void updatePersoanelData (String email, String dob, String co_distributor_title, String co_distributor_name, String co_distributor_dob,String uline, String designation,String firstname,String ann_date){
        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<UpdateModel> call = apiService.updatePersonal(email,dob,co_distributor_title,co_distributor_name,co_distributor_dob,uline,designation,firstname,ann_date);
        call.enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {

                UpdateModel updatemodel = response.body();

                //check the status code
                if(updatemodel.getStatus().equals("success")){
                    //   Toast.makeText(SignupActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    //    button_makepayment.setVisibility(View.VISIBLE);
                    progressBar_personal.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Personal Details Updated Successfully", Toast.LENGTH_SHORT).show();


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
                progressBar_personal.setVisibility(View.INVISIBLE);
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