package com.rahul.emergency;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rahul.emergency.db.DBManager;
import com.rahul.emergency.room.DatabaseClient;
import com.rahul.emergency.room.Doctors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddDoctorActivity extends AppCompatActivity {

    @BindView(R.id.nameET)
    TextInputEditText nameET;
    @BindView(R.id.mobileET)
    TextInputEditText mobileET;
    @BindView(R.id.sepcialistET)
    TextInputEditText specialistET;
    @BindView(R.id.latitudeET)
    TextInputEditText latitudeET;
    @BindView(R.id.longitudeET)
    TextInputEditText longitudeET;
    @BindView(R.id.addressET)
    TextInputEditText addressET;
    @BindView(R.id.addBT)
    Button addBT;

    private DBManager dbManager;

    String name;
    String mobile;
    String specialist;
    String latitude;
    String lonitude;
    String address;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);
        ButterKnife.bind(AddDoctorActivity.this);

        setTitle("Add Doctor");

        databaseReference = FirebaseDatabase.getInstance().getReference("doctors");

        dbManager = new DBManager(this);
        dbManager.open();

        addBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nameET.getText().toString().trim().length() == 0) {
                    nameET.setError("Enter name");
                } else if (mobileET.getText().toString().trim().length() == 0 || mobileET.getText().toString().length() != 10) {
                    mobileET.setError("Enter valid mobile number");
                } else if (specialistET.getText().toString().trim().length() == 0) {
                    specialistET.setError("Enter specialization");
                } else if (latitudeET.getText().toString().trim().length() == 0) {
                    latitudeET.setError("Enter latitude");
                } else if (longitudeET.getText().toString().trim().length() == 0) {
                    latitudeET.setError("Enter logitude");
                } else if (addressET.getText().toString().trim().length() == 0) {
                    addressET.setError("Enter address");
                } else {

                    name = nameET.getText().toString().trim();
                    mobile = mobileET.getText().toString().trim();
                    specialist = specialistET.getText().toString().trim();
                    latitude = latitudeET.getText().toString().trim();
                    lonitude = longitudeET.getText().toString().trim();
                    address = addressET.getText().toString();

                    String id = databaseReference.push().getKey();
                    DoctorModel model = new DoctorModel();
                    model.setId(id);
                    model.setName(name);
                    model.setMobile(mobile);
                    model.setSpecialist(specialist);
                    model.setLatitude(latitude);
                    model.setLongitude(lonitude);
                    model.setAddress(address);

                    databaseReference.child(id).setValue(model);
                    Toast.makeText(AddDoctorActivity.this, "Saved.", Toast.LENGTH_SHORT).show();
                    //InsertDoctors insertDoctors = new InsertDoctors();
                    //insertDoctors.execute();
                    //dbManager.insert(name, mobile, latitude, lonitude, specialist, address);
                    setEmptytext();

                    // Toast.makeText(AddDoctorActivity.this, "Doctor details added successfully!!!", Toast.LENGTH_SHORT).show();

                    //setEmptytext();

                    // startActivity(new Intent(AddDoctorActivity.this,DoctorsListActivity.class));
                }

            }
        });
    }

    public class InsertDoctors extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            Doctors d = new Doctors();
            d.setName(name);
            d.setMobile(mobile);
            d.setLatitude(latitude);
            d.setLongitude(lonitude);
            d.setSpecialist(specialist);
            d.setAddress(address);

            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .doctorsDao().insert(d);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //finish();
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            setEmptytext();
        }
    }

    private void setEmptytext() {
        nameET.setText("");
        mobileET.setText("");
        specialistET.setText("");
        latitudeET.setText("");
        longitudeET.setText("");
        addressET.setText("");
    }
}
