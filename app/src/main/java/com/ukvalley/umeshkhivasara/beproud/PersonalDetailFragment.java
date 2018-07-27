package com.ukvalley.umeshkhivasara.beproud;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;
import com.ukvalley.umeshkhivasara.beproud.model.GetSingleUserSupport;
import com.ukvalley.umeshkhivasara.beproud.supports.SignupClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PersonalDetailFragment extends Fragment{

    TextView textView_profile_dob,profile_lastname,profile_distributorid,profile_distributor_title,profile_codistributor_name,profile_co_distributor_dob,profile_upline,profile_designation;

    public PersonalDetailFragment() {
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


        textView_profile_dob = container.findViewById(R.id.profile_dob);
        profile_codistributor_name=container.findViewById(R.id.profile_co_distributor_name);
        profile_designation=container.findViewById(R.id.profile_designation);
        profile_distributorid=container.findViewById(R.id.profile_distributor_id);
        profile_upline=container.findViewById(R.id.profile_upline);
        profile_distributor_title=container.findViewById(R.id.profile_distributor_co_title);
        profile_co_distributor_dob=container.findViewById(R.id.profile_co_distributor_dob);
        profile_lastname=container.findViewById(R.id.profile_lastname);


        return inflater.inflate(R.layout.fragment_personal_detail, container, false);


    }


    private void getUser(String email) {

        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<GetSingleUserSupport> call = apiService.GetUser(email);
        call.enqueue(new Callback<GetSingleUserSupport>() {

            @Override
            public void onResponse(Call<GetSingleUserSupport> call, Response<GetSingleUserSupport> response) {

                //Toast.makeText(ProfileActivity.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();
                /*profile_city.setText(response.body().getGetSingleUser().getCity().toString());
                profile_title.setText(response.body().getGetSingleUser().getUserName().toString());
                profile_education.setText(response.body().getGetSingleUser().getEducation().toString());
   */





            }

            @Override
            public void onFailure(Call<GetSingleUserSupport> call, Throwable t) {

                //Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}