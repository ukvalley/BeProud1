package com.ukvalley.umeshkhivasara.beproud.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.ukvalley.umeshkhivasara.beproud.R;
import com.ukvalley.umeshkhivasara.beproud.model.socialmedia.Datum_;


import java.util.ArrayList;
import java.util.List;

public class Socialpostadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final String BASE_URL_IMG = "http://www.sunclubs.org/test/test_api/public/bank/";

    private List<Datum_> movieResults1;
    private Context context;

    private boolean isLoadingAdded = false;

    public Socialpostadapter(Context context) {
        this.context = context;
        movieResults1 = new ArrayList<>();
    }

    public List<Datum_> getMovies() {
        return movieResults1;
    }

    public void setMovies(List<Datum_> movieResults1) {
        this.movieResults1 = movieResults1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.fb_posts, parent, false);
                viewHolder = new Socialpostadapter.LoadingVH(v2);
                break;
        }

        return viewHolder;
    }



    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.fb_posts, parent, false);


        viewHolder = new Socialpostadapter.Fbpostdata(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final Datum_ result = movieResults1.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final Fbpostdata movieVH = (Fbpostdata) holder;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       /* Intent intent=new Intent(context,EventDisplay.class);
                        String id=String.valueOf(result.getId());
                        intent.putExtra("Key",id);
                        context.startActivity(intent);*/
                    }
                });

                movieVH.nameofcreater.setText(result.getEmail());
                movieVH.nameofcreater.setClickable(true);
                movieVH.nameofcreater.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Toast.makeText(context, String.valueOf(result.getId()), Toast.LENGTH_SHORT).show();

                       /* Intent  intent=new Intent(context,NewsDisplay.class);
                        String id=String.valueOf(result.getId());
                        intent.putExtra("Key",id);
                        context.startActivity(intent);*/
                    }
                });

                movieVH.mProgress.setVisibility(View.GONE);
                movieVH.postdata.setText(result.getPostData());


                /**
                 * Using Glide to handle image loading.
                 * Learn more about Glide here:
                 * <a href="http://blog.grafixartist.com/image-gallery-app-android-studio-1-4-glide/" />
                 */
                Glide
                        .with(context)
                        .load(BASE_URL_IMG + result.getPostImage())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                // TODO: 08/11/16 handle failure
                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                // image ready, hide progress now
                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;   // return false if you want Glide to handle everything else.
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(movieVH.image_post);


        }

    }

    @Override
    public int getItemCount() {
        return movieResults1 == null ? 0 : movieResults1.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movieResults1.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(Datum_ r) {
        movieResults1.add(r);
        notifyItemInserted(movieResults1.size() - 1);
    }

    public void addAll(List<Datum_> moveResults) {
        for (Datum_ result : moveResults) {
            add(result);
        }
    }

    public void remove(Datum_ r) {
        int position = movieResults1.indexOf(r);
        if (position > -1) {
            movieResults1.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Datum_());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults1.size() - 1;
        Datum_ result = getItem(position);

        if (result != null) {
            movieResults1.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Datum_ getItem(int position) {
        return movieResults1.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class Fbpostdata extends RecyclerView.ViewHolder {
        private TextView nameofcreater;
        private TextView postdata;
        private ImageView image_post;
        private ProgressBar mProgress;

        public Fbpostdata(View itemView) {
            super(itemView);

            nameofcreater = (TextView) itemView.findViewById(R.id.post_creater);
            postdata = (TextView) itemView.findViewById(R.id.post_description);

            image_post = (ImageView) itemView.findViewById(R.id.post_image);
            mProgress = (ProgressBar) itemView.findViewById(R.id.movie_progress);


        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}