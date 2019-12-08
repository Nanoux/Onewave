package com.nanowheel.nanoux.nanowheel.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class HTTPUtil {

    private static final String BASE_URL = "https://api.github.com/";

    private static RequestQueue queue;

    private static void setup(Context con){
        if (queue == null) {
            queue = Volley.newRequestQueue(con);
        }
    }

    public static void checkVersion(Context con, Response.Listener<String> responseHandler,Response.ErrorListener errorHandler){
        getString("repos/nanoux/onewave/releases/latest",null,con,responseHandler,errorHandler);
    }

    private static void getString(String url, final HashMap<String, String> data, Context con, Response.Listener<String> responseHandler,Response.ErrorListener errorHandler) {
        setup(con);
        String uri = getAbsoluteUrl(url);
        if (data != null && data.size() > 0) {
            uri += "?";
            String[] keys = new String[data.size()];
            keys = data.keySet().toArray(keys);
            for (int i = 0; i < keys.length; i++) {
                String val = data.get(keys[i]);
                uri += keys[i] + "=" + val;
                if (i < keys.length - 1) {
                    uri += "&";
                }
            }
        }
        StringRequest getRequest = new StringRequest(Request.Method.GET, uri, responseHandler,errorHandler){
            @Override
            protected Map<String, String> getParams()
            {
                return data;
            }
        };
        queue.add(getRequest);
    }

    static void postString(String url, final HashMap<String, String> data, Context con, Response.Listener<String> responseHandler, Response.ErrorListener errorHandler) {
        setup(con);
        StringRequest postRequest = new StringRequest(Request.Method.POST, getAbsoluteUrl(url), responseHandler,errorHandler){
            @Override
            protected Map<String, String> getParams()
            {
                return data;
            }
        };
        queue.add(postRequest);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
