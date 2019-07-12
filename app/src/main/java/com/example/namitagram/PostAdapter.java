package com.example.namitagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.namitagram.models.Post;
import com.example.namitagram.models.TimeFormatter;
import com.parse.ParseFile;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    //pass in the posts array into the constructor
    // mPosts is just a reference to posts
    private List<Post> posts;
    Context context;

    public PostAdapter(List<Post> posts, Context context){
        this.posts = posts;
        this.context = context;
    }


    //for each row, inflate the layout and pass into ViewHolder class
    //only invoked when a new row needs to be created, otherwise, the adapter will call onbindVH as the user scrolls down
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Context context = viewGroup.getContext(); --> we need this later in onBindViewHolder though,
        //so instead make it a public variable for the whole class
        context = viewGroup.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.item_post, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }



    //for each row we are showing, we need to bind the value of the tweet object to that element
    //as the user scrolls down, repopulate viewholder based on position
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        //get the data according to position (i)
        final Post post = posts.get(i);

        //populate the views according to this data
        viewHolder.tvUsername.setText(post.getUser().getUsername());
        viewHolder.tvUsername2.setText(post.getUser().getUsername());
        viewHolder.tvCaption.setText(post.getDescription());
        viewHolder.tvCreatedAt.setText(TimeFormatter.getTimeDifference(post.getCreatedAt().toString()));

        viewHolder.tvNumLikes.setText(Integer.toString(post.getNumLikes()));
        if (post.isLiked()){
            viewHolder.ivLike.setImageResource(R.drawable.ufi_heart_active);
            viewHolder.ivLike.setColorFilter(Color.argb(255,255,0,0));
        }else {
            viewHolder.ivLike.setImageResource(R.drawable.ufi_heart);
            viewHolder.ivLike.setColorFilter(Color.argb(255,0,0,0));
        }


        //load the post image
        Glide.with(context)
                .load(post.getImage().getUrl())
                .apply(new RequestOptions()
                        .centerCrop())
                .into(viewHolder.ivPhoto);

        ParseFile profFile = post.getUser().getParseFile("profPic");
        if (profFile != null) {
            viewHolder.ivProfPic.setVisibility(View.VISIBLE);
            //load the profile image
            Glide.with(context)
                    .load(profFile.getUrl())
                    .apply(new RequestOptions()
                            .centerCrop())
                    .into(viewHolder.ivProfPic);
        } else {
            viewHolder.ivProfPic.setVisibility(View.INVISIBLE);
        }


        //enable liking & unliking
        viewHolder.ivLike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!post.isLiked()){
                    //like
                    post.like();
                    viewHolder.ivLike.setImageResource(R.drawable.ufi_heart_active);
                    viewHolder.ivLike.setColorFilter(Color.argb(255,255,0,0));

                }else{
                    //unlike
                    post.unlike();
                    viewHolder.ivLike.setImageResource(R.drawable.ufi_heart);
                    viewHolder.ivLike.setColorFilter(Color.argb(255,0,0,0));
                }
                post.saveInBackground();
                viewHolder.tvNumLikes.setText(Integer.toString(post.getNumLikes()));
            }
        });

        //enable commenting
        viewHolder.ivComment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

    }


    //how many to display
    @Override
    public int getItemCount() {
        return posts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvUsername) TextView tvUsername;
        @BindView(R.id.tvUsername2) TextView tvUsername2;
        @BindView(R.id.tvCaption) TextView tvCaption;
        @BindView(R.id.ivPhoto) ImageView ivPhoto;
        @BindView(R.id.ivProfPic) ImageView ivProfPic;
        @BindView(R.id.tvCreatedAt) TextView tvCreatedAt;
        @BindView(R.id.ivLike) ImageView ivLike;
        @BindView(R.id.tvNumLikes) TextView tvNumLikes;
        @BindView(R.id.ivComment) ImageView ivComment;
        @BindView(R.id.tvNumComments) TextView tvNumComments;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        @OnClick(R.id.clItem)
        public void onClickItem(View view) {
            int position = getAdapterPosition(); // gets item position
            Post post = posts.get(position);
            Intent i = new Intent(context, PostDetailActivity.class);
            i.putExtra("POST", post);
            context.startActivity(i);
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }
}
