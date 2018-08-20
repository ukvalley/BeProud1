package com.ukvalley.umeshkhivasara.beproud;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.Activity;
import instamojo.library.InstapayListener;
import instamojo.library.InstamojoPay;
import instamojo.library.Config;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.IntentFilter;
import android.net.Uri;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
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

    TextView textView_alreadyregister;
    EditText editText_birthdate,editText_ann_date;
    EditText editText_adharno, editText_username,editText_mobilenumber,editText_city,editText_email,editText_education,editText_profession,editText_brandname,editText_dream,editText_pass,edt_parent_id;
    private DatePickerDialog dpd;
    private DatePickerDialog dpd1;
    Button button_signup;
    Button button_makepayment;
    String str_email;
    
    ProgressBar progressBar_signup;

    public static final int FLAG_BIRTH = 0;
    public static final int FLAG_ANNIVERSARY = 1;

    TextView textView_terms;
    CheckBox checkBox_terms;

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
                finish();
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


        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        //
                        // If the user isn't signed in and the pending Dynamic Link is
                        // an invitation, sign in the user anonymously, and record the
                        // referrer's UID.
                        //

                      //  FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (deepLink != null) {
                            String referrerUid = deepLink.getQueryParameter("invitedby");
                            Toast.makeText(SignupActivity.this, referrerUid, Toast.LENGTH_SHORT).show();
                           // createAnonymousAccountWithReferrerInfo(referrerUid);
                        }
                    }
                });




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
        editText_adharno=findViewById(R.id.edt_adharno);

        checkBox_terms=findViewById(R.id.terms_condition_check);
        textView_terms=findViewById(R.id.txt_terms);

        edt_parent_id=findViewById(R.id.edt_parentid);

        progressBar_signup=findViewById(R.id.signup_pbar);
        
        editText_ann_date=findViewById(R.id.edt_ann_date);

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
                callInstamojoPay("umsh98904@gmail.com","9890437811","10","Registreation","Umesh");
            }
        });

        button_signup = findViewById(R.id.edt_btn_signup);

        editText_ann_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                setFlag(FLAG_ANNIVERSARY);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        editText_birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                /*
                It is recommended to always create a new instance whenever you need to show a Dialog.
                The sample app is reusing them because it is useful when looking for regressions
                during testing
                 */
                if (dpd1 == null) {
                    dpd1 = DatePickerDialog.newInstance(
                            SignupActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                } else {
                    dpd1.initialize(
                            SignupActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                }

                setFlag(FLAG_BIRTH);
                dpd1.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        textView_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(SignupActivity.this)
                        .setTitle("Terms And Condition")
                        .setMessage("We reserve the right to alter these terms and condition at any time.\n" +
                                "All the decision taken by Be Proud management will be accepted by you.\n" +
                                "Please Take care of your belongings BE PROUD WOMEN will not be responsive for any loses\n" +
                                "Do not distribute any printed material for marketing of any kind in BE PROUD WOMEN event without management Notice\n" +
                                "Do not conduct an unauthorized event, hold demonstrations of unauthorized public gatherings and make speeches in behalf of BE PROUD WOMEN\n" +
                                "BE PROUD WOMEN will charge Rs.200/- per person for each Kitty\n" +
                                "Payments must be made within 2 days.\n" +
                                "Yearly Registration Fees Rs. 1100/-\n" +
                                "From Feb. 2017 to Dec. 2017\n" +
                                "We can remove any member if they create infringes copyright\n" +
                                "Final discussion taken by founder will be accepted to all.")
                        .setNegativeButton("OK", null)
                        .create().show();
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
                String str_parent_id=edt_parent_id.getText().toString();
                String str_ann_date=editText_ann_date.getText().toString();
                String str_adharno=editText_adharno.getText().toString();




                if (checkBox_terms.isChecked()) {
                    insertData(str_name, str_mobile, str_city, str_email, str_pass, str_adharno, str_education, str_profession, str_brandname, str_dream, str_birthdate, str_ann_date, str_parent_id);
                    progressBar_signup.setVisibility(View.VISIBLE);
                }
                else 
                {
                    Toast.makeText(SignupActivity.this, "Please accept Terms and Condition", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private int flag = 0;


    public void setFlag(int i) {

        flag = i;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        if (flag==FLAG_BIRTH) {
            String date = +dayOfMonth + "/" + (++monthOfYear) + "/" + year;
            editText_birthdate.setText(date);
            Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
        }
        if (flag==FLAG_ANNIVERSARY)
        {
            String date = +dayOfMonth + "/" + (++monthOfYear) + "/" + year;
            editText_ann_date.setText(date);
            Toast.makeText(this, date, Toast.LENGTH_SHORT).show();

        }
    }



    private void insertData(String username, String mobile, String city, final String email, String password, String str_adharno, String education, final String profession, String brandname, String dream, String dob, String ann_date, String str_parent_id){
        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<SignupResponsemodel> call = apiService.insertuser(username,mobile,city,email,password, str_adharno, education,profession,brandname,dream,dob,ann_date,str_parent_id);
        call.enqueue(new Callback<SignupResponsemodel>() {
            @Override
            public void onResponse(Call<SignupResponsemodel> call, Response<SignupResponsemodel> response) {

                SignupResponsemodel insertFoodResponseModel = response.body();

                if(insertFoodResponseModel.getStatus().equals("success")){

                    Toast.makeText(SignupActivity.this, "Registration Success Please Make Payment", Toast.LENGTH_SHORT).show();
                    progressBar_signup.setVisibility(View.INVISIBLE);
                    button_makepayment.setVisibility(View.VISIBLE);

                }else{
                    progressBar_signup.setVisibility(View.INVISIBLE);
                    Toast.makeText(SignupActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<SignupResponsemodel> call, Throwable t) {
                progressBar_signup.setVisibility(View.INVISIBLE);
                Toast.makeText(SignupActivity.this, "Something went wrong "+t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
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
                        SignupActivity.super.onBackPressed();
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




}

