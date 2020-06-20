package com.example.MNM;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    protected EditText email;
    protected EditText password;
    protected TextView sign_up;
    protected TextView forget;
    protected Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initiateView();

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(LoginActivity.this,RegisterUser.class));
            }
        });
       forget.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class));

           }
       });
    }

    private void initiateView() {
        email = (EditText) findViewById(R.id.email);
        sign_up=(TextView) findViewById(R.id.sign_up);
        password = (EditText) findViewById(R.id.password);
        forget=(TextView) findViewById(R.id.forget);
        login = (Button) findViewById(R.id.login);
    }


}
