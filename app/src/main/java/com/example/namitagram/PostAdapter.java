package com.example.namitagram;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.namitagram.models.Post;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    //pass in the posts array into the constructor
    // mPosts is just a reference to posts
    private List<Post> mPosts;

    public PostAdapter(List<Post> posts, Activity activity){
        mPosts = posts;
        this.activity = activity;
    }

    //context to be used in onCreateVH & onBind
    Context context;
    //activity to be set in constructor & to be used to pass TimelineActivity into onClickReply intent
    Activity activity;


    //for each row, inflate the layout and pass into ViewHolder class
    //only invoked when a new row needs to be created, otherwise, the adapter will call onbindVH as the user scrolls down
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Context context = viewGroup.getContext(); --> we need this later in onBindViewHolder though,
        //so instead make it a public variable for the whole class
        context = viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_post, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }



    //for each row we are showing, we need to bind the value of the tweet object to that element
    //as the user scrolls down, repopulate viewholder based on position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //get the data according to position (i)
        Post post = mPosts.get(i);

        //populate the views according to this data
        viewHolder.tvUsername.setText(post.getUser().getUsername());
//        viewHolder.tvCreatedAt.setText(TimeFormatter.getTimeDifference(tweet.createdAt));
        viewHolder.tvCaption.setText(post.getDescription());

        Glide.with(context)
                .load(post.getImage().getUrl())
                .apply(new RequestOptions()
                        .transform(new RoundedCorners(20)))
                .into(viewHolder.ivPhoto);
    }


    //how many to display
    @Override
    public int getItemCount() {
        return mPosts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvUsername) TextView tvUsername;
        @BindView(R.id.tvCaption) TextView tvCaption;
        //            @BindView(R.id.tvCreatedAt) TextView tvCreatedAt;
        @BindView(R.id.ivPhoto) ImageView ivPhoto;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

//            @OnClick(R.id.clItem)
//            public void onClickItem(View view) {
//                int position = getAdapterPosition(); // gets item position
//                Tweet tweet = mTweets.get(position);
//                Intent i = new Intent(context, TweetDetailActivity.class);
//                i.putExtra("TWEET", Parcels.wrap(tweet));
//                context.startActivity(i);
//
//            }
//
//            @OnClick (R.id.ibReply)
//            public void onClickReply(View view) {
//                int position = getAdapterPosition(); // gets item position
//                Tweet tweet = mTweets.get(position);
//                User user = tweet.user;
//                String screenName = user.screenName;
//                Intent i = new Intent(context, ComposeActivity.class);
//                i.putExtra("REPLY", true);
//                i.putExtra("SCREEN_NAME", "@" + screenName);
//                activity.startActivityForResult(i,20);
//            }
    }

    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }
}
