package com.ukvalley.umeshkhivasara.beproud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;
import com.ukvalley.umeshkhivasara.beproud.model.level_income.LevelIncome;
import com.ukvalley.umeshkhivasara.beproud.model.point_package.Beproud;
import com.ukvalley.umeshkhivasara.beproud.model.userstatus.Userstatus;
import com.ukvalley.umeshkhivasara.beproud.supports.SessionManager;
import com.ukvalley.umeshkhivasara.beproud.supports.SignupClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PointActivity extends AppCompatActivity {


    TextView textView_level1,textView_level2,textView_level3,textView_level4,textView_level5,textView_level6,textView_level7,textView_level8,textView_level9,textView_level10;

    TextView textView_comm1,textView_comm2,textView_comm3,textView_comm4,textView_comm5,textView_comm6,textView_comm7,textView_comm8,textView_comm9,textView_comm10;

    TextView textView_paid1,textView_paid2,textView_paid3,textView_paid4,textView_paid5,textView_paid6,textView_paid7,textView_paid8,textView_paid9,textView_paid10;

    TextView textView_unpaid1,textView_unpaid2,textView_unpaid3,textView_unpaid4,textView_unpaid5,textView_unpaid6,textView_unpaid7,textView_unpaid8,textView_unpaid9,textView_unpaid10;


    TextView textView_total1,textView_total2,textView_total3,textView_total4,textView_total5,textView_total6,textView_total7,textView_total8,textView_total9,textView_total10;

    TextView textView_total_amt,total_child,total_paid,total_unpaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        Toolbar toolbar;
        toolbar =  findViewById(R.id.toolbar_point);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        textView_level1=findViewById(R.id.tv_lc1);
        textView_level2=findViewById(R.id.tv_lc2);
        textView_level3=findViewById(R.id.tv_lc3);
        textView_level4=findViewById(R.id.tv_lc4);
        textView_level5=findViewById(R.id.tv_lc5);
        textView_level6=findViewById(R.id.tv_lc6);
        textView_level7=findViewById(R.id.tv_lc7);
        textView_level8=findViewById(R.id.tv_lc8);
        textView_level9=findViewById(R.id.tv_lc9);
        textView_level10=findViewById(R.id.tv_lc10);


        textView_comm1=findViewById(R.id.comm_1);
        textView_comm2=findViewById(R.id.comm_2);
        textView_comm3=findViewById(R.id.comm_3);
        textView_comm4=findViewById(R.id.comm_4);
        textView_comm5=findViewById(R.id.comm_5);
        textView_comm6=findViewById(R.id.comm_6);
        textView_comm7=findViewById(R.id.comm_7);
        textView_comm8=findViewById(R.id.comm_8);
        textView_comm9=findViewById(R.id.comm_9);
        textView_comm10=findViewById(R.id.comm_10);

        textView_paid1=findViewById(R.id.paid_1);
        textView_paid2=findViewById(R.id.paid_2);
        textView_paid3=findViewById(R.id.paid_3);
        textView_paid4=findViewById(R.id.paid_4);
        textView_paid5=findViewById(R.id.paid_5);
        textView_paid6=findViewById(R.id.paid_6);
        textView_paid7=findViewById(R.id.paid_7);
        textView_paid8=findViewById(R.id.paid_8);
        textView_paid9=findViewById(R.id.paid_9);
        textView_paid10=findViewById(R.id.paid_10);

        textView_unpaid1=findViewById(R.id.unpaid_1);
        textView_unpaid2=findViewById(R.id.unpaid_2);
        textView_unpaid3=findViewById(R.id.unpaid_3);
        textView_unpaid4=findViewById(R.id.unpaid_4);
        textView_unpaid5=findViewById(R.id.unpaid_5);
        textView_unpaid6=findViewById(R.id.unpaid_6);
        textView_unpaid7=findViewById(R.id.unpaid_7);
        textView_unpaid8=findViewById(R.id.unpaid_8);
        textView_unpaid9=findViewById(R.id.unpaid_9);
        textView_unpaid10=findViewById(R.id.unpaid_10);

        textView_total1=findViewById(R.id.total_1);
        textView_total2=findViewById(R.id.total_2);
        textView_total3=findViewById(R.id.total_3);
        textView_total4=findViewById(R.id.total_4);
        textView_total5=findViewById(R.id.total_5);
        textView_total6=findViewById(R.id.total_6);
        textView_total7=findViewById(R.id.total_7);
        textView_total8=findViewById(R.id.total_8);
        textView_total9=findViewById(R.id.total_9);
        textView_total10=findViewById(R.id.total_10);

        textView_total_amt=findViewById(R.id.total_total);

        total_child=findViewById(R.id.user_count);

        total_paid=findViewById(R.id.total_paid);

        total_unpaid=findViewById(R.id.total_unpaid);


        SessionManager sessionManager=new SessionManager(PointActivity.this);

        getPoints(sessionManager.getUserDetails());


    }

    private void getPoints(String email){



        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<LevelIncome> call = apiService.getpoints(email);
        call.enqueue(new Callback<LevelIncome>(){

            @Override
            public void onResponse(Call<LevelIncome> call, Response<LevelIncome> response) {

              //  Toast.makeText(PointActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

                int paid1= response.body().getLevel1Paid() * response.body().getLevel1Rate();
                int paid2= response.body().getLevel2Paid() * response.body().getLevel2Rate();
                int paid3= response.body().getLevel3Paid() * response.body().getLevel3Rate();
                int paid4= response.body().getLevel4Paid() * response.body().getLevel4Rate();
                int paid5= response.body().getLevel5Paid() * response.body().getLevel5Rate();
                int paid6= response.body().getLevel6Paid() * response.body().getLevel6Rate();
                int paid7= response.body().getLevel7Paid() * response.body().getLevel7Rate();
                int paid8= response.body().getLevel8Paid() * response.body().getLevel8Rate();
                int paid9= response.body().getLevel9Paid() * response.body().getLevel9Rate();
                int paid10= response.body().getLevel10Paid() * response.body().getLevel10Rate();

                int unpaid1= response.body().getLevel1Unpaid() * response.body().getLevel1Rate();
                int unpaid2= response.body().getLevel2Unpaid() * response.body().getLevel2Rate();
                int unpaid3= response.body().getLevel3Unpaid() * response.body().getLevel3Rate();
                int unpaid4= response.body().getLevel4Unpaid() * response.body().getLevel4Rate();
                int unpaid5= response.body().getLevel5Unpaid() * response.body().getLevel5Rate();
                int unpaid6= response.body().getLevel6Unpaid() * response.body().getLevel6Rate();
                int unpaid7= response.body().getLevel7Unpaid() * response.body().getLevel7Rate();
                int unpaid8= response.body().getLevel8Unpaid() * response.body().getLevel8Rate();
                int unpaid9= response.body().getLevel9Unpaid() * response.body().getLevel9Rate();
                int unpaid10= response.body().getLevel10Unpaid() * response.body().getLevel10Rate();

                int total1= paid1+unpaid1;
                int total2= paid2+unpaid2;
                int total3= paid3+unpaid3;
                int total4= paid4+unpaid4;
                int total5= paid5+unpaid5;
                int total6= paid6+unpaid6;
                int total7= paid7+unpaid7;
                int total8= paid8+unpaid8;
                int total9= paid9+unpaid9;
                int total10= paid10+unpaid10;


                textView_level1.setText(response.body().getLevel1().toString());
                textView_level2.setText(response.body().getLevel2().toString());
                textView_level3.setText(response.body().getLevel3().toString());
                textView_level4.setText(response.body().getLevel4().toString());
                textView_level5.setText(response.body().getLevel5().toString());
                textView_level6.setText(response.body().getLevel6().toString());
                textView_level7.setText(response.body().getLevel7().toString());
                textView_level8.setText(response.body().getLevel8().toString());
                textView_level9.setText(response.body().getLevel9().toString());
                textView_level10.setText(response.body().getLevel10().toString());


                textView_comm1.setText(response.body().getLevel1Rate().toString());
                textView_comm2.setText(response.body().getLevel2Rate().toString());
                textView_comm3.setText(response.body().getLevel3Rate().toString());
                textView_comm4.setText(response.body().getLevel4Rate().toString());
                textView_comm5.setText(response.body().getLevel5Rate().toString());
                textView_comm6.setText(response.body().getLevel6Rate().toString());
                textView_comm7.setText(response.body().getLevel7Rate().toString());
                textView_comm8.setText(response.body().getLevel8Rate().toString());
                textView_comm9.setText(response.body().getLevel9Rate().toString());
                textView_comm10.setText(response.body().getLevel10Rate().toString());


                textView_paid1.setText(String.valueOf(paid1));
                textView_paid2.setText(String.valueOf(paid2));
                textView_paid3.setText(String.valueOf(paid3));
                textView_paid4.setText(String.valueOf(paid4));
                textView_paid5.setText(String.valueOf(paid5));
                textView_paid6.setText(String.valueOf(paid6));
                textView_paid7.setText(String.valueOf(paid7));
                textView_paid8.setText(String.valueOf(paid8));
                textView_paid9.setText(String.valueOf(paid9));
                textView_paid10.setText(String.valueOf(paid10));

                textView_unpaid1.setText(String.valueOf(unpaid1));
                textView_unpaid2.setText(String.valueOf(unpaid2));
                textView_unpaid3.setText(String.valueOf(unpaid3));
                textView_unpaid4.setText(String.valueOf(unpaid4));
                textView_unpaid5.setText(String.valueOf(unpaid5));
                textView_unpaid6.setText(String.valueOf(unpaid6));
                textView_unpaid7.setText(String.valueOf(unpaid7));
                textView_unpaid8.setText(String.valueOf(unpaid8));
                textView_unpaid9.setText(String.valueOf(unpaid9));
                textView_unpaid10.setText(String.valueOf(unpaid10));



                textView_total1.setText(String.valueOf(total1));
                textView_total2.setText(String.valueOf(total2));
                textView_total3.setText(String.valueOf(total3));
                textView_total4.setText(String.valueOf(total4));
                textView_total5.setText(String.valueOf(total5));
                textView_total6.setText(String.valueOf(total6));
                textView_total7.setText(String.valueOf(total7));
                textView_total8.setText(String.valueOf(total8));
                textView_total9.setText(String.valueOf(total9));
                textView_total10.setText(String.valueOf(total10));


                total_paid.setText(String.valueOf(paid1+paid2+paid3+paid4+paid5+paid6+paid7+paid8+paid9+paid10));

                total_unpaid.setText(String.valueOf(unpaid1+unpaid2+unpaid3+unpaid4+unpaid5+unpaid6+unpaid7+unpaid8+unpaid9+unpaid10));

                textView_total_amt.setText(String.valueOf(total1+total2+total3+total4+total5+total6+total7+total8+total8+total9+total10));

                total_child.setText(String.valueOf(
                        response.body().getLevel1() + response.body().getLevel2()+ response.body().getLevel3() + response.body().getLevel4()
                        + response.body().getLevel5() + response.body().getLevel6() + response.body().getLevel7() + response.body().getLevel8()
                        + response.body().getLevel9() + response.body().getLevel10()
                ));
            }

            @Override
            public void onFailure(Call<LevelIncome> call, Throwable t) {

               // Toast.makeText(PointActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });




    }

}
