package com.ukvalley.umeshkhivasara.beproud;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    public String[] back_color={"#484848","#fd6823","#22bf22","#fc3eca","#fc0f1d","#ffff02","#44beae","#d640ff","#484848","#fd6823","#22bf22","#fc3eca","#fc0f1d","#ffff02","#44beae","#d640ff","#44beae","#d640ff"};

    String [] menu_tilte={"Profile","Branches","VBD Stores","Feedback","Company","Product","News","Downline","Point","Bonus","Training","Contact","Voucher","Notification","Funds","Consistency","VBD App","VBD Facebook"};
    int [] icons= {R.drawable.profile,R.drawable.branches,R.drawable.stores,R.drawable.feedback,R.drawable.company,R.drawable.product,R.drawable.news,R.drawable.downline,R.drawable.point,R.drawable.bonus,R.drawable.training,R.drawable.phone,R.drawable.voucher,R.drawable.notification,R.drawable.fund,R.drawable.consistency,R.drawable.letterb,R.drawable.fb};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        GridView gridView=findViewById(R.id.gridview);
       CustomGridAdapter myadapter=new CustomGridAdapter(HomeActivity.this,menu_tilte);
        gridView.setAdapter(myadapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



            }
        });
    }

    public class Myadapter extends BaseAdapter{
        @Override
        public int getCount() {
            return menu_tilte.length;
        }

        @Override
        public Object getItem(int i) {
            return menu_tilte[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater=HomeActivity.this.getLayoutInflater();
            View view1=inflater.inflate(R.layout.grid_layout, null,true);

            return view;
        }
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
}
