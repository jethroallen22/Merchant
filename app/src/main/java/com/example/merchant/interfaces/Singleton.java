package com.example.merchant.interfaces;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Singleton {

    private RequestQueue requestQueue;
    private static Singleton sInstance;

    private Singleton(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized Singleton getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Singleton(context);
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
