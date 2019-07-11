package com.example.namitagram;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.namitagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {
    PostAdapter postAdapter;
    ArrayList<Post> posts;//our data source
    RecyclerView rvPosts;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    public ProfileFragment() {
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
                //first clear everything out
                postAdapter.clear();
                //repopulate
                populateFeed();
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
        rvPosts.setLayoutManager(linearLayoutManager);
        //set the adapter
        rvPosts.setAdapter(postAdapter);

        populateFeed();
    }

    private void populateFeed() {

        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop()
                .withUser()
                .onlyOneUser();


        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
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