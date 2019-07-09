package com.example.namitagram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.namitagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //actionBar.setBackgroundDrawable();

        ParseQuery.getQuery(Post.class).findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e == null){
                    for (int i = 0; i < objects.size(); i++){
                        Log.d("home activity","post[" + i + "] = " + objects.get(i).getDescription());
                    }
                }else{
                    e.printStackTrace();
                }
            }
        });
    }
}
