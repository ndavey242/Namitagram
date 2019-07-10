package com.example.namitagram;

import android.app.Application;

import com.example.namitagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //let parse know about the Post Model
        ParseObject.registerSubclass(Post.class);

        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        //config the parse
        //get it running & connect
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("namitagram")
                .clientKey("ricegetuppoker")
                .server("http://ndavey-fbu-instagram.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);
    }
}