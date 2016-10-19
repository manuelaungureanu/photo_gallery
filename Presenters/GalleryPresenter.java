package com.chefless.ela.photo_gallery.Presenters;

import com.chefless.ela.photo_gallery.Model.Photo;
import com.chefless.ela.photo_gallery.Network.RestfullRequestService;
import com.chefless.ela.photo_gallery.Views.GalleryView;

/**
 * Created by ela on 19/10/2016.
 */

//TODO: move the business logic from HomeActivity

public class GalleryPresenter {
    private GalleryView view;
    private RestfullRequestService service;

    public GalleryPresenter(GalleryView view, RestfullRequestService service){
        this.view = view;
        this.service = service;
    }

    public Photo[] onSearch(String key){
        //to be implemented
        return null;
    }
}
