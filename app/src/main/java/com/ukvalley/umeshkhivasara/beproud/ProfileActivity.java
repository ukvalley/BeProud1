package com.ukvalley.umeshkhivasara.beproud;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;
import com.ukvalley.umeshkhivasara.beproud.model.GetSingleUserSupport;
import com.ukvalley.umeshkhivasara.beproud.model.SignupResponsemodel;
import com.ukvalley.umeshkhivasara.beproud.supports.SessionManager;
import com.ukvalley.umeshkhivasara.beproud.supports.SignupClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ProfileActivity extends AppCompatActivity {

    SessionManager sessionManager;

    private TabLayout tabLayout;
    private ViewPager viewPager;
   android.support.v7.widget.Toolbar toolbar;

   TextView profile_title,profile_education,profile_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile2_activity);

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
       // actionBar.hide();

        profile_city=findViewById(R.id.profile2_city);
        profile_education=findViewById(R.id.profile2_education);
        profile_title=findViewById(R.id.profile2_title);

        sessionManager=new SessionManager(ProfileActivity.this);

       /*
        textView_brandname=findViewById(R.id.profile_brandname);
        textView_city=findViewById(R.id.profile_city);
        textView_dob=findViewById(R.id.profile_dob);
        textView_dream=findViewById(R.id.profile_dream);
        textView_eduation=findViewById(R.id.profile_education);
        textView_email=findViewById(R.id.profile_email);
        textView_mobile=findViewById(R.id.profile_mobile);
        textView_profession=findViewById(R.id.profile_profession);
        textView_name=findViewById(R.id.profile_username);
        */


        getUser(sessionManager.getUserDetails());



    }

    private void getUser(String email){

        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<GetSingleUserSupport> call = apiService.GetUser(email);
        call.enqueue(new Callback<GetSingleUserSupport>(){

            @Override
            public void onResponse(Call<GetSingleUserSupport> call, Response<GetSingleUserSupport> response) {

                Toast.makeText(ProfileActivity.this, response.body().getStatus(), Toast.LENGTH_SHORT).show();
                profile_city.setText(response.body().getGetSingleUser().getCity().toString());
                profile_title.setText(response.body().getGetSingleUser().getUserName().toString());
                profile_education.setText(response.body().getGetSingleUser().getEducation().toString());


            }

            @Override
            public void onFailure(Call<GetSingleUserSupport> call, Throwable t) {

                Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PersonalDetailFragment(), "Personal Details");
        adapter.addFragment(new ContactDetailFragment(), "Contact Details");
        adapter.addFragment(new BankDetailFragment(), "Bank Details");
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
}
