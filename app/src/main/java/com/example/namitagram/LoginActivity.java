package com.example.namitagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class
LoginActivity extends AppCompatActivity {

    @BindView(R.id.etUsername) EditText usernameInput;
    @BindView(R.id.etPassword) EditText passwordInput;
    @BindView(R.id.btnLogin) Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                login(username, password);
            }
        });
    }

    private void login(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null){
                    Log.d("LoginActivity","Login success");
                    final Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else{
                    Log.e("LoginActivity", "Login failure");
                    e.printStackTrace();
                }
            }
        });
    }
}
