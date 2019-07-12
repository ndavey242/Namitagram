package com.example.namitagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.namitagram.models.Post;
import com.example.namitagram.models.TimeFormatter;
import com.parse.ParseFile;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostDetailActivity extends AppCompatActivity {
    @BindView(R.id.tvUsername) TextView tvUsername;
    @BindView(R.id.tvUsername2) TextView tvUsername2;
    @BindView(R.id.tvCaption) TextView tvCaption;
    //            @BindView(R.id.tvCreatedAt) TextView tvCreatedAt;
    @BindView(R.id.ivPhoto) ImageView ivPhoto;
    @BindView(R.id.tvCreatedAt) TextView tvCreatedAt;
    @BindView(R.id.ivProfPic) ImageView ivProfPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        Post post = intent.getParcelableExtra("POST");

        tvUsername.setText(post.getUser().getUsername());
        tvUsername2.setText(post.getUser().getUsername());
        tvCaption.setText(post.getDescription());
        tvCreatedAt.setText(TimeFormatter.getTimeDifference(post.getCreatedAt().toString()));


        Glide.with(this)
                .load(post.getImage().getUrl())
                .apply(new RequestOptions()
                        .centerCrop())
                .into(ivPhoto);

        ParseFile profFile = post.getUser().getParseFile("profPic");
        if (profFile != null) {
            ivProfPic.setVisibility(View.VISIBLE);
            //load the profile image
            Glide.with(this)
                    .load(profFile.getUrl())
                    .apply(new RequestOptions()
                            .centerCrop())
                    .into(ivProfPic);
        } else {
            ivProfPic.setVisibility(View.INVISIBLE);
        }

    }
}
