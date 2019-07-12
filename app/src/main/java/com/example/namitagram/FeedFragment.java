package com.example.namitagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.namitagram.models.EndlessRecyclerViewScrollListener;
import com.example.namitagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FeedFragment extends Fragment {
    PostAdapter postAdapter;
    ArrayList<Post> posts;//our data source
    RecyclerView rvPosts;

    private EndlessRecyclerViewScrollListener scrollListener;

    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        //THIS IS WHERE WE BIND BUTTERKNIFE!!!!!!
        ButterKnife.bind(this,  view);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("REFRESHING", "we are refreshing whoooo");
                //first clear everything out
                postAdapter.clear();
                //repopulate
                populateFeed(Calendar.getInstance().getTime());
                //now make sure swipeContainer.setRefreshing is set to false
                //but let's not do that here becauuuuuse.... ASYNCHRONOUS
                //lets put it at the end of populateTimeline instead!
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);
        //init the arraylist (data source)
        posts = new ArrayList<>();
        //construct the adapter from the data source
        postAdapter = new PostAdapter(posts, getContext());

        //RecyclerView setup (layout manager, use adapter), pass in the context!
        //the layout manager needs the context to know what kinds of settings to use
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        //endless scrolling
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextData(page);
            }
        };

        // Adds the scroll listener to RecyclerView
        rvPosts.addOnScrollListener(scrollListener);

        rvPosts.setLayoutManager(linearLayoutManager);
        //set the adapter
        rvPosts.setAdapter(postAdapter);

        populateFeed(Calendar.getInstance().getTime());
//      ^^ in the action progress bar

    }

    //for endless scrolling
    public void loadNextData(int offset) {
        Date oldestDate = posts.get(posts.size() - 1).getCreatedAt(); //get the created at of the oldest post
        populateFeed(oldestDate);
    }

    private void populateFeed(final Date oldestDate) {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop(oldestDate)
                .withUser();


        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    Log.d("SIZE OF QUERY RESULT", Integer.toString(objects.size()));
                    Log.d("Oldest Date", oldestDate.toString());
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("QUERY",objects.get(i).getDescription());
                        posts.add(objects.get(i));
                        postAdapter.notifyItemInserted(objects.size() - 1);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });

        swipeContainer.setRefreshing(false);
    }

}