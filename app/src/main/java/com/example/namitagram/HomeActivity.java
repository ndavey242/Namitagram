package com.example.namitagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    private String myUsername;

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        myUsername = intent.getExtras().getString("myUsername");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setLogo(getResources().getDrawable(R.drawable.icon));
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        // define your fragments here
        final Fragment feed = new FeedFragment();
        final Fragment capture = new CaptureFragment();
        final Fragment profile = new ProfileFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.Feed:
                        fragment = feed;
                        break;
                    case R.id.Capture:
                        fragment = capture;
                        break;
                    case R.id.Profile:
                        fragment = profile;
                        break;
                    default:
                        fragment = feed;
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.fragment_placeholder, fragment).commit();
                return true;
            }
        });
        //set default
        bottomNavigationView.setSelectedItemId(R.id.Feed);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).getSubMenu().getItem(0).setTitle(myUsername);
        return true;
    }

    public void onLogOut(MenuItem item) {
        ParseUser.logOut();
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }


}
