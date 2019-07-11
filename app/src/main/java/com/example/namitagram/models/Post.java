package com.example.namitagram.models;

import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


@ParseClassName("Post")
public class Post extends ParseObject implements Parcelable {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";
    private static final String KEY_CREATED_AT = "createdAt";
    private static final String KEY_LIKED_BY = "likedBy";

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

    public JSONArray getLikedBy(){
        JSONArray likedBy = getJSONArray(KEY_LIKED_BY);
        if(likedBy == null){
            return new JSONArray();
        }
        return likedBy;
    }

    public boolean isLiked() {
        JSONArray likedBy = getLikedBy();
        for (int i =0; i < likedBy.length(); i++){
            try {
                String objectId = ParseUser.getCurrentUser().getObjectId();
                //have to use .equals when comparing two strings
                if(likedBy.getJSONObject(i).getString("objectID").equals(objectId)){
                    return true;
                }
                likedBy.get(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void like() {
        ParseUser user = ParseUser.getCurrentUser();
        //get array of users that have liked this post, and add current user to that
        add(KEY_LIKED_BY, user);

    }

    public void unlike() {
        ParseUser user = ParseUser.getCurrentUser();
        //need an array to pass into removeAll
        ArrayList<ParseUser> users = new ArrayList<>();
        users.add(user);

        removeAll(KEY_LIKED_BY, users);
    }

    public int getNumLikes() {
        return getLikedBy().length();
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
