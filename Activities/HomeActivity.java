package com.chefless.ela.photo_gallery.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.chefless.ela.photo_gallery.Constants;
import com.chefless.ela.photo_gallery.Helpers.Utils;
import com.chefless.ela.photo_gallery.Interfaces.IRequestListener;
import com.chefless.ela.photo_gallery.Model.Photo;
import com.chefless.ela.photo_gallery.Network.RestfullRequestService;
import com.chefless.ela.photo_gallery.Adapters.PhotoAdapter;
import com.chefless.ela.photo_gallery.R;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.photoRecyclerView) RecyclerView photoRecyclerView;
    @BindView(R.id.codeProgressBar) ProgressBar codeProgressBar;
    @BindView(R.id.searchEditText) EditText searchEditText;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private PhotoAdapter photoAdapter;
    Photo[] model;
    Boolean dirToSortDesc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();
        fetchDataAndUpdateUI();
    }

    private void initUI() {
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_home_activity);
        setSupportActionBar(toolbar);
        codeProgressBar = (ProgressBar) findViewById(R.id.codeProgressBar);
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        initChatListUI();

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (searchEditText.getText().toString().length() != 0) {
                            InputMethodManager imm =
                                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);

                            fetchDataAndUpdateUI();
                        }

                        return true;
                    }

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                fetchDataAndUpdateUI();
                return true;
            case R.id.action_order:
                sortByDateTaken();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sortByDateTaken() {
        dirToSortDesc = !dirToSortDesc;
        ArrayList<Photo> arrayList = new ArrayList<Photo>(Arrays.asList(model));
        Collections.sort(arrayList, new Comparator<Photo>(){
            @Override
            public int compare(Photo photo1, Photo photo2) {
                if(dirToSortDesc)
                    return Utils.getUTCDateFromString(photo2.getDate_taken()).compareTo(Utils.getUTCDateFromString(photo1.getDate_taken()));
                else
                    return Utils.getUTCDateFromString(photo1.getDate_taken()).compareTo(Utils.getUTCDateFromString(photo2.getDate_taken()));
            }
        });
        model = arrayList.toArray(new Photo[arrayList.size()]);
        displayGallery();
    }

    protected void fetchDataAndUpdateUI(){

        String tags = searchEditText.getText().toString();
        codeProgressBar.setVisibility(View.VISIBLE);
        IRequestListener<Photo[]> postTask = getPostTask();
        RestfullRequestService service = new RestfullRequestService(this);
        String url = Constants.FLICKR_ENTRYPOINT;
        //add the tags param to url
        if(!tags.isEmpty())
            url = String.format("%s&%s=%s", url, Constants.FLICKR_TAGS_HEADER_NAME, tags);
        service.makeRequest(Request.Method.GET, url , new JSONObject(), new HashMap<String, String>(), postTask);
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
