package com.rahul.emergency;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminLogin extends AppCompatActivity {

    @BindView(R.id.usernameET)
    TextInputEditText nameET;
    @BindView(R.id.passwordET)
    TextInputEditText pesswordET;
    @BindView(R.id.loginBT)
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        ButterKnife.bind(AdminLogin.this);

        setTitle("Admin");
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=nameET.getText().toString().trim();
                String password=pesswordET.getText().toString().trim();

                if(name.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")){
                    editor.putString("key_name", "admin"); // Storing string
                    editor.commit();

                    startActivity(new Intent(AdminLogin.this,MainActivity.class));
                }else{
                    Toast.makeText(AdminLogin.this, "Username or Password is wrong.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
