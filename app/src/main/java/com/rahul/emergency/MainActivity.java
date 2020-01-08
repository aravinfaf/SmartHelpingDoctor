package com.rahul.emergency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.adddoctorCV)
    CardView adddoctorcv;
    @BindView(R.id.viewdoctorCV)
    CardView viewdoctorcv;
    @BindView(R.id.doctormapCV)
    CardView doctormapCV;
    @BindView(R.id.hospitalsCV)
    CardView hospitalsCV;
    @BindView(R.id.bloodbankCV)
    CardView bloodbankcv;
    @BindView(R.id.ambulanceCV)
    CardView ambulanceCV;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);

         pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        adddoctorcv.setOnClickListener(this);
        viewdoctorcv.setOnClickListener(this);
        doctormapCV.setOnClickListener(this);
        hospitalsCV.setOnClickListener(this);
        bloodbankcv.setOnClickListener(this);
        ambulanceCV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.adddoctorCV){
            if(pref.getString("key_name","").equalsIgnoreCase("admin")) {
                startActivity(new Intent(MainActivity.this, AddDoctorActivity.class));
            }else{
                Toast.makeText(MainActivity.this, "Admin only have access.", Toast.LENGTH_SHORT).show();
            }
        }else  if(v.getId()==R.id.viewdoctorCV){
            //startActivity(new Intent(MainActivity.this,DoctorsListActivity.class));
            startActivity(new Intent(MainActivity.this,ViewDoctors.class));
        }else  if(v.getId()==R.id.hospitalsCV){
            String uri = String.format("https://www.google.com/maps/search/hospital/@11.068794,76.9987971,15z/data=!3m1!4b1");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }else  if(v.getId()==R.id.bloodbankCV){
            String uri = String.format("https://www.google.com/maps/search/bllodbank/@11.068794,76.9987971,15z/data=!3m1!4b1");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }else if(v.getId()==R.id.doctormapCV){
            startActivity(new Intent(MainActivity.this,MapsActivity.class));
        }else if(v.getId()==R.id.ambulanceCV){
            String uri = String.format("https://www.google.com/maps/search/ambulance/@11.068794,76.9987971,15z/data=!3m1!4b1");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }
    }
}
