package com.ukvalley.umeshkhivasara.beproud;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.ukvalley.umeshkhivasara.beproud.adapter.SlidingImage_Adapter;
import com.ukvalley.umeshkhivasara.beproud.interfaces.RetrofitAPI;
import com.ukvalley.umeshkhivasara.beproud.model.ImageModel;
import com.ukvalley.umeshkhivasara.beproud.model.singleuser.GetSingleUserSupport;
import com.ukvalley.umeshkhivasara.beproud.model.userstatus.Userstatus;
import com.ukvalley.umeshkhivasara.beproud.supports.SessionManager;
import com.ukvalley.umeshkhivasara.beproud.supports.SignupClient;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    Uri mInvitationUrl;
    SessionManager sessionManager;
  //  public String[] back_color={"#484848","#fd6823","#22bf22","#fc3eca","#fc0f1d","#ffff02","#44beae","#d640ff","#484848","#fd6823","#22bf22","#fc3eca","#fc0f1d","#ffff02","#44beae","#d640ff","#44beae","#d640ff"};

    String userstatus;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;

    TextView text_userinactive;

    Boolean b;
    GridView gridView;
    private int[] myImageList = new int[]{R.drawable.slider1, R.drawable.slider2,
            R.drawable.slider3,R.drawable.slider4
            ,R.drawable.slider5,R.drawable.slider6};


    String [] menu_tilte={"Profile","News","Share","Downline","VBD Stores","Feedback","Company","Product","Point","Bonus","Training","Contact","Voucher","Notification","Funds","Consistency","VBD App","VBD Facebook"};
    int [] icons= {R.drawable.profile,R.drawable.news,R.drawable.share,R.drawable.downline,R.drawable.stores,R.drawable.feedback,R.drawable.company,R.drawable.product,R.drawable.point,R.drawable.bonus,R.drawable.training,R.drawable.phone,R.drawable.voucher,R.drawable.notification,R.drawable.fund,R.drawable.consistency,R.drawable.letterb,R.drawable.fb};
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar;
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sessionManager = new SessionManager(HomeActivity.this);
        getUser(sessionManager.getUserDetails());
        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();

        init();

        FirebaseApp.initializeApp(this);

        gridView = findViewById(R.id.gridview);

        CustomGridAdapter myadapter=new CustomGridAdapter(HomeActivity.this,menu_tilte);
        gridView.setAdapter(myadapter);
        text_userinactive=findViewById(R.id.text_userinactive);

        text_userinactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                    if (i == 0) {
                        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(intent);

                    }
                    if (i == 2) {
                        onInviteClicked();
                    }
                    if (i == 1) {
                        Intent intent = new Intent(HomeActivity.this, NewsActivity.class);
                        startActivity(intent);
                    }
                    if (i==3){

                        Intent intent=new Intent(HomeActivity.this,DownlineActivity.class);
                        startActivity(intent);
                    }
                    if (i==17)
                    {
                        Intent intent=new Intent(HomeActivity.this,SocialMedia.class);
                        startActivity(intent);
                    }
                    if (i==4)
                    {
                        Intent intent=new Intent(HomeActivity.this,ProductActivity.class);
                      //  startActivity(intent);
                    }
                if (i==8)
                {
                    Intent intent=new Intent(HomeActivity.this,PointActivity.class);
                    startActivity(intent);
                }

                if (i==13)
                {
                    Intent intent=new Intent(HomeActivity.this,NotificationActivity.class);
                    startActivity(intent);
                }

                if (i==10)
                {
                    Intent intent=new Intent(HomeActivity.this,TrainingActivity.class);
                    startActivity(intent);
                }
                }
            });
        }



    public class CustomGridAdapter extends BaseAdapter {

        private Context context;
        private String[] items;
        LayoutInflater inflater;

        public CustomGridAdapter(Context context, String[] items) {
            this.context = context;
            this.items = items;
            inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.grid_layout, null);
            }
            ImageView imageView_icon=convertView.findViewById(R.id.img_icon);
            TextView textView_icon=convertView.findViewById(R.id.text_iconname);
            CardView cardView_grid=convertView.findViewById(R.id.cardview);

//            cardView_grid.setBackgroundColor(Color.parseColor("#3F51B5"));
            imageView_icon.setImageResource(icons[position]);
            textView_icon.setText(menu_tilte[position]);

            return convertView;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    private void onInviteClicked() {

        SessionManager sessionManager=new SessionManager(HomeActivity.this);

        final String email = sessionManager.getUserDetails();
        final String link = "https://ukvalley.com/?invitedby=" + email;

        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDynamicLinkDomain("ukvalley.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.example.android")
                                .setMinimumVersion(125)
                                .build())
                .setIosParameters(
                        new DynamicLink.IosParameters.Builder("com.example.ios")
                                .setAppStoreId("123456789")
                                .setMinimumVersion("1.0.1")
                                .build())
                .buildShortDynamicLink()
                .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                    @Override
                    public void onSuccess(ShortDynamicLink shortDynamicLink) {

                        mInvitationUrl = shortDynamicLink.getShortLink();

                       /* String subject = String.format("%s has invited you to install Be Proud Application", email);
                        String invitationLink = mInvitationUrl.toString();
                        String msg = "Let's join together! Use my referrer link: "
                                + invitationLink;
                        String msgHtml = String.format("<p>Let's play MyExampleGame together! Use my "
                                + "<a href=\"%s\">referrer link</a>!</p>", invitationLink);*/

                     /*   Intent intent = new Intent(Intent.ACTION_SENDTO);
                      //  intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                        intent.putExtra(Intent.EXTRA_TEXT, msg);
                       // intent.putExtra(Intent.EXTRA_HTML_TEXT, msgHtml);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }*/



                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Download Be Proud Application\n"+mInvitationUrl+"\nInvitition ID is "+email);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    }
                });


       // String referrerName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==R.id.action_logout)
        {
           sessionManager.logoutUser();
        }

        return super.onOptionsItemSelected(item);
    }



    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < 6; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }

    private void init() {

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(HomeActivity.this,imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

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
                            HomeActivity.super.onBackPressed();
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


    private void getUser(String email){



        RetrofitAPI apiService = SignupClient.getClient().create(RetrofitAPI.class);
        Call<Userstatus> call = apiService.getuserstatus(email);
        call.enqueue(new Callback<Userstatus>(){

            @Override
            public void onResponse(Call<Userstatus> call, Response<Userstatus> response) {


                String  activeStatus;


                activeStatus = response.body().getData().getActiveStatus();

                if (activeStatus.equals("1"))
                {
                    gridView.setVisibility(View.VISIBLE);
                    text_userinactive.setVisibility(View.GONE);

                }
                if (activeStatus.equals("0"))
                {
                    gridView.setVisibility(View.GONE);

                    Intent intent=new Intent(HomeActivity.this,ProfileActivity.class);
                    startActivity(intent);

                    text_userinactive.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void onFailure(Call<Userstatus> call, Throwable t) {

              

            }
        });




    }


}
