package com.ukvalley.umeshkhivasara.beproud;

import android.content.Intent;
import android.app.Activity;
import instamojo.library.InstapayListener;
import instamojo.library.InstamojoPay;
import instamojo.library.Config;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.IntentFilter;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    SessionManager sessionManager;

    TextView editText_birthdate,textView_alreadyregister;
    EditText editText_username,editText_mobilenumber,editText_city,editText_email,editText_education,editText_profession,editText_brandname,editText_dream,editText_pass;
    private DatePickerDialog dpd;
    Button button_signup;
    Button button_makepayment;
    String str_email;

    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        final Activity activity = this;
        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
       pay.put("send_sms", true);
      pay.put("send_email", true);
 } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
    }
    
    InstapayListener listener;

    
    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG)
                        .show();



                sessionManager.createLoginSession(str_email);
                Intent intent=new Intent(SignupActivity.this,HomeActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(int code, String reason) {
                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                        .show();
            }
        };
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Call the function callInstamojo to start payment here

        editText_birthdate = findViewById(R.id.edt_birthdate);
        editText_username=findViewById(R.id.edt_name);
        editText_mobilenumber=findViewById(R.id.edt_mobile);
        editText_city=findViewById(R.id.edt_city);
        editText_email=findViewById(R.id.edt_email);
        editText_education=findViewById(R.id.edt_education);
        editText_profession=findViewById(R.id.edt_profession);
        editText_brandname=findViewById(R.id.edt_brandname);
        editText_dream=findViewById(R.id.edt_dream);
        editText_pass=findViewById(R.id.edt_pass);
        sessionManager=new SessionManager(SignupActivity.this);

        textView_alreadyregister=findViewById(R.id.edtalreadyregister);

        textView_alreadyregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignupActivity.this,SigninActivity.class);
                startActivity(intent);
            }
        });

        button_makepayment = findViewById(R.id.edt_btn_makepayment);
        button_makepayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callInstamojoPay("umsh98904@gmail.com","9890437811","100","Registreation","Umesh");
            }
        });

        button_signup = findViewById(R.id.edt_btn_signup);


        editText_birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                /*
                It is recommended to always create a new instance whenever you need to show a Dialog.
                The sample app is reusing them because it is useful when looking for regressions
                during testing
                 */
                if (dpd == null) {
                    dpd = DatePickerDialog.newInstance(
                            SignupActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                } else {
                    dpd.initialize(
                            SignupActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                }

                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });


        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String str_name=  editText_username.getText().toString();
                String str_mobile=  editText_mobilenumber.getText().toString();
                String str_city=  editText_city.getText().toString();

                str_email = editText_email.getText().toString();
                String str_education=  editText_education.getText().toString();
                String str_profession=  editText_profession.getText().toString();
                String str_brandname=  editText_brandname.getText().toString();
                String str_dream=  editText_dream.getText().toString();
                String str_birthdate= editText_birthdate.getText().toString();
                String str_pass=editText_pass.getText().toString();





                insertData(str_name,str_mobile,str_city,str_email,str_pass,str_education,str_profession,str_brandname,str_dream,str_birthdate);
            }
        });
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date =+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        editText_birthdate.setText(date);
        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
    }


    private void insertData(String username, String mobile, String city, final String email, String password, String education, String profession, String brandname, String dream, String dob){
        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<SignupResponsemodel> call = apiService.insertuser(username,mobile,city,email,password,education,profession,brandname,dream,dob);
        call.enqueue(new Callback<SignupResponsemodel>() {
            @Override
            public void onResponse(Call<SignupResponsemodel> call, Response<SignupResponsemodel> response) {

                SignupResponsemodel insertFoodResponseModel = response.body();

                //check the status code
                if(insertFoodResponseModel.getStatus().equals("success")){
                    Toast.makeText(SignupActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    button_makepayment.setVisibility(View.VISIBLE);



                   // progressDialog.dismiss();
                }else{
                    Toast.makeText(SignupActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                  //  progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SignupResponsemodel> call, Throwable t) {
                Toast.makeText(SignupActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                editText_username.setText(t.getMessage());
               // progressDialog.dismiss();
            }
        });
    }

  /*  private void getGetSingleUser(){
        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<Signupmodel1> call = apiService.getUsers();
        call.enqueue(new Callback<Signupmodel1>() {
            @Override
            public void onResponse(Call<Signupmodel1> call, Response<Signupmodel1> response) {

                Signupmodel1 insertFoodResponseModel = response.body();

                //check the status code
                if(insertFoodResponseModel.getStatus()=="success"){
                    Toast.makeText(SignupActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    // progressDialog.dismiss();
                }else{
                    Toast.makeText(SignupActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    //  progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Signupmodel1> call, Throwable t) {
                Toast.makeText(SignupActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                editText_mobilenumber.setText(t.getMessage());
                // progressDialog.dismiss();
            }
        });
    }
*/




    }

