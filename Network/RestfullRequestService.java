package com.chefless.ela.photo_gallery.Network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.chefless.ela.photo_gallery.Interfaces.IRequestListener;
import com.chefless.ela.photo_gallery.Model.Photo;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Created by ela on 16/10/2016.
 */

public class RestfullRequestService {

    private Context context;

        public RestfullRequestService(Context c) {
        this.context = c;
    }

    public void makeRequest(int method, String requestUrl, JSONObject jsonObject, final Map<String, String> requestHeaders, final IRequestListener<Photo[]> postTask) {

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(method, requestUrl, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ObjectMapper mapper = new ObjectMapper();
                        Photo[] responseObj = null;
                        try {
                            responseObj = mapper.readValue(response.get("items").toString(), Photo[].class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(postTask!=null)
                            postTask.onSuccess(responseObj);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                if(postTask!=null)
                    postTask.onError(error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;

            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return requestHeaders!=null ? requestHeaders : new HashMap<String, String>();
            }
        };

        queue.add(request);
    }
}
