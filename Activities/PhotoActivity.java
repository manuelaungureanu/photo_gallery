package com.chefless.ela.photo_gallery.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chefless.ela.photo_gallery.Model.Photo;
import com.chefless.ela.photo_gallery.R;

public class PhotoActivity extends AppCompatActivity {

    Photo model;
    private static final String ARG_PHOTO = "photo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_photo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        model = (Photo)getIntent().getSerializableExtra(ARG_PHOTO);

        ImageView fullscreenView = (ImageView) findViewById(R.id.fullscreen_view);
        Glide.with(this)
                .load(model.getMedia().getUrl())
                .fitCenter()
                .placeholder(R.drawable.spinner)
                .crossFade()
                .into(fullscreenView);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
