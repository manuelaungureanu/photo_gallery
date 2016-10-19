package com.chefless.ela.photo_gallery.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chefless.ela.photo_gallery.Helpers.Utils;
import com.chefless.ela.photo_gallery.Model.Photo;
import com.chefless.ela.photo_gallery.R;

import org.w3c.dom.Text;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoActivity extends AppCompatActivity {

    Photo model;
    private static final String ARG_PHOTO = "photo";
    @BindView(R.id.author_view) TextView authorTextView;
    @BindView(R.id.title_view) TextView titleTextView;
    @BindView(R.id.date_taken_view) TextView date_taken_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        setToolbar();
        setModel();
        setUI();
    }

    private void setUI() {
        if(model==null)
            return;
        displayPhoto();
        authorTextView = (TextView) findViewById(R.id.author_view);
        titleTextView = (TextView) findViewById(R.id.title_view);
        date_taken_view = (TextView)findViewById(R.id.date_taken_view);
        titleTextView.setText(model.getTitle());

        if(!model.getAuthor().isEmpty()) {
            String author = String.format("%s: %s", getResources().getString(R.string.author), model.getAuthor());
            authorTextView.setText(author);
        }
        if(!model.getDate_taken().isEmpty()) {
            Date takenData = Utils.getUTCDateFromString(model.getDate_taken());
            String stringTakenDate = Utils.getDateWithDefaultFormat(takenData);
            date_taken_view.setText(String.format("%s: %s", getResources().getString(R.string.taken_at), stringTakenDate));
        }
    }

    private void displayPhoto() {
        ImageView fullscreenView = (ImageView) findViewById(R.id.fullscreen_view);
        Glide.with(this)
                .load(model.getMedia().getUrl())
                .fitCenter()
                .placeholder(R.drawable.spinner)
                .crossFade()
                .into(fullscreenView);
    }

    private void setModel() {
        model = (Photo)getIntent().getSerializableExtra(ARG_PHOTO);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_photo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
