package com.chefless.ela.photo_gallery.Interfaces;

/**
 * Created by ela on 16/10/2016.
 */

public interface IRequestListener<K> {

    void onSuccess(K result);

    void onError(Exception ex);
}
