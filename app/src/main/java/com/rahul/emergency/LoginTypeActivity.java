package com.rahul.emergency;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginTypeActivity extends AppCompatActivity {

    @BindView(R.id.admin)
    Button admin;
    @BindView(R.id.customer)
    Button customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_type);
        ButterKnife.bind(LoginTypeActivity.this);

        setTitle("Login");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginTypeActivity.this,AdminLogin.class));
            }
        });


        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("key_name", "customer"); // Storing string
                editor.commit();
                startActivity(new Intent(LoginTypeActivity.this,MainActivity.class));
            }
        });
    }
}
