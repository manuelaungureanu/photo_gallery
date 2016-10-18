package com.chefless.ela.photo_gallery.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.chefless.ela.photo_gallery.Constants;
import com.chefless.ela.photo_gallery.Interfaces.IRequestListener;
import com.chefless.ela.photo_gallery.Model.Photo;
import com.chefless.ela.photo_gallery.Network.RestfullRequestService;
import com.chefless.ela.photo_gallery.Adapters.PhotoAdapter;
import com.chefless.ela.photo_gallery.R;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.photoRecyclerView) RecyclerView photoRecyclerView;
    @BindView(R.id.codeProgressBar) ProgressBar codeProgressBar;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private PhotoAdapter photoAdapter;
    Photo[] model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();
        fetchDataAndUpdateUI();
    }

    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_home_activity);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        codeProgressBar = (ProgressBar) findViewById(R.id.codeProgressBar);
        initChatListUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void fetchDataAndUpdateUI(){
        codeProgressBar.setVisibility(View.VISIBLE);
        IRequestListener<Photo[]> postTask = getPostTask();
        RestfullRequestService service = new RestfullRequestService(this);
        service.makeRequest(Request.Method.GET, Constants.FLICKR_ENTRYPOINT, new JSONObject(), new HashMap<String, String>(), postTask);
    }

    @NonNull
    private IRequestListener<Photo[]> getPostTask() {
        final WeakReference<HomeActivity> activityWeakReference = new WeakReference<>(this);
        return new IRequestListener<Photo[]>(){
            @Override
            public void onSuccess(Photo[] result) {
                codeProgressBar.setVisibility(View.GONE);
                initModel(result);
                displayGallery();
            }
            @Override
            public void onError(Exception ex) {
                codeProgressBar.setVisibility(View.GONE);
                Toast.makeText(activityWeakReference.get(), R.string.feed_error, Toast.LENGTH_SHORT).show();
            }
        };
    }

    protected void initChatListUI() {
        // Letting the system know that the list objects are of fixed size
        photoRecyclerView = (RecyclerView) findViewById(R.id.photoRecyclerView);
        photoRecyclerView.setHasFixedSize(true);

        // Creating a layout Manager
        recyclerViewLayoutManager = new LinearLayoutManager(this);

        // Setting the layout Manager
        photoRecyclerView.setLayoutManager(recyclerViewLayoutManager);
    }

    protected void initModel(Photo[] data){
        model = data;
    }

    protected void displayGallery() {
        if(model!=null && model.length>0){
            photoAdapter = new PhotoAdapter(this, model);
            photoRecyclerView.setAdapter(photoAdapter);
        }
        else
            Toast.makeText(this, R.string.feed_no_data, Toast.LENGTH_SHORT).show();
    }
}
