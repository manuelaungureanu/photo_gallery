package com.chefless.ela.photo_gallery.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chefless.ela.photo_gallery.Activities.PhotoActivity;
import com.chefless.ela.photo_gallery.Model.Photo;
import com.chefless.ela.photo_gallery.R;

/**
 * Created by ela on 17/10/2016.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.SimpleViewHolder> {

    private Context context;
    private Photo[] source;
    String lastname, firstname;
    private static final String ARG_PHOTO = "photo";

    public PhotoAdapter(Context context, Photo[] photos) {
        this.context = context;
        this.source = photos;
    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.photo_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        Resources res = context.getResources();

        holder.photoTitle.setText(source[position].getTitle());

        if(source[position].getMedia() != null && source[position].getMedia().getUrl()!=null) {

            Glide.with(context)
                    .load(source[position].getMedia().getUrl())
                    .override(150, 150)
                    .centerCrop()
                    .placeholder(R.drawable.loading)
                    .error(context.getResources().getDrawable(android.R.drawable.ic_delete))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .into(holder.photoImageView);
        }
        else{
            holder.photoImageView.setImageDrawable(null);
        }
        holder.photoLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPhoto(position);
            }
        });
    }

    private void goToPhoto(int position) {

        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra(ARG_PHOTO, source[position]);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return source.length;
    }


    @Override
    public int getItemViewType(int position) {
        return position;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        ViewGroup photoLinear;
        ImageView photoImageView;
        TextView photoTitle;

        public SimpleViewHolder(View convertView) {
            super(convertView);

            photoLinear = (ViewGroup) convertView.findViewById(R.id.photoLinear);
            photoImageView = (ImageView) convertView.findViewById(R.id.photo_view);
            photoTitle = (TextView) convertView.findViewById(R.id.title_view);
        }
    }
}

