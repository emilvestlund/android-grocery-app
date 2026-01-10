package com.example.labb_4;

import org.json.JSONObject;

public interface VolleyCallback <T> {
    public void onSuccess(JSONObject object);
    public void onFailure(Exception e);
                                                /*  Interface VolleyCallback --
                                                 *  This interface is used to handle the response from the API.
                                                 */
}
