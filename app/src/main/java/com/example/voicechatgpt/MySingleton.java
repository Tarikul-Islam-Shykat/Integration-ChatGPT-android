package com.example.voicechatgpt;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static  MySingleton instance;
    private RequestQueue requestQueue;
    private  static Context context;

    public MySingleton(Context context){
        this.context = context;
        requestQueue = getRequestQueue();
    }

    // create a singleton class based on the context
    public  static synchronized  MySingleton getInstance(Context context){
        if(instance == null){
            instance = new MySingleton(context);
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T>request){
        getRequestQueue().add(request);
    }

}
