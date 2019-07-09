package com.example.namitagram;

import android.app.Application;

import com.example.namitagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //let parse know about the Post Model
        ParseObject.registerSubclass(Post.class);

        //config the parse
        //connect to the parse server
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("namitagram")
                .clientKey("ricegetuppoker")
                .server("http://ndavey-fbu-instagram.herokuapp.com/parse")
                .build();
        Parse.initialize(configuration);
    }
}