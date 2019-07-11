package com.example.namitagram.models;

import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


@ParseClassName("Post")
public class Post extends ParseObject implements Parcelable {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";
    private static final String KEY_CREATED_AT = "createdAt";

    //getters and setters
    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    //parse object class already has a getter for createdAt

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }


    //Querying in Post
    public static class Query extends ParseQuery<Post>{
        public Query() {
            super(Post.class);
        }

        //only want the top 20 greater than the current oldest date
        public Query getTop(java.util.Date oldestDate){
            setLimit(20);
            orderByDescending("createdAt");
            whereLessThan(KEY_CREATED_AT,oldestDate);
            return this;
        }

        //want the user data included and not just the object ID of the user
        public Query withUser(){
            include("user");
            return this;
        }

        //if we only want the posts of the current user
        public Query onlyOneUser(){
            whereEqualTo(KEY_USER, ParseUser.getCurrentUser());
            return this;
        }
    }

}
