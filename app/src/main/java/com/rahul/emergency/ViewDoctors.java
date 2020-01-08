package com.rahul.emergency;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rahul.emergency.room.DatabaseClient;
import com.rahul.emergency.room.Doctors;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewDoctors extends AppCompatActivity {

    @BindView(R.id.doctorsRV)
    RecyclerView doctorsRV;

    DoctorAdapter adapter;

    DatabaseReference databaseReference;
    ArrayList<DoctorModel> data;

    String name;
    String mobile;
    String specialist;
    String latitude;
    String lonitude;
    String address;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctors);
        ButterKnife.bind(ViewDoctors.this);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        setTitle("All Doctors");
        data = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("doctors");

//        GetDoctors getDoctors=new GetDoctors();
//        getDoctors.execute();


    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                data.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    DoctorModel artist = postSnapshot.getValue(DoctorModel.class);
                    //adding artist to the list
                    data.add(artist);
                }

                if (data.size() == 0) {
                    Toast.makeText(ViewDoctors.this, "No Records Found.", Toast.LENGTH_SHORT).show();
                    doctorsRV.setVisibility(View.GONE);
                } else {
                    doctorsRV.setVisibility(View.VISIBLE);
                    adapter = new DoctorAdapter(ViewDoctors.this, data);
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    doctorsRV.setLayoutManager(layoutManager);
                    doctorsRV.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

        Context context;
        List<DoctorModel> data;

        public DoctorAdapter(Context context, List<DoctorModel> doctors) {
            this.context = context;
            this.data = doctors;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.activity_view_record, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            if (holder instanceof ViewHolder) {
                holder.nameTV.setText("Name : " + data.get(position).getName());
                holder.mobileTV.setText("Mobile no : " + data.get(position).getMobile());
                holder.addressTV.setText("Address : " + data.get(position).getAddress());
                holder.specialistTV.setText("Specialization : " + data.get(position).getSpecialist());

                holder.infoTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                        bottomSheetDialog.setContentView(R.layout.bottomsheetc_contact);
                        bottomSheetDialog.show();

                        ImageView call = bottomSheetDialog.findViewById(R.id.callIV);
                        ImageView message = bottomSheetDialog.findViewById(R.id.messageIV);
                        ImageView map = bottomSheetDialog.findViewById(R.id.mapIV);
                        ImageView whatsapp = bottomSheetDialog.findViewById(R.id.whatsappIV);


                        call.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + data.get(position).getMobile()));
                                if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    Activity#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for Activity#requestPermissions for more details.
                                    return;
                                }
                                context.startActivity(intent);

                            }
                        });
                        message.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                composeSmsMessage("I'm in need of help!!!", data.get(position).getMobile());

                            }
                        });
                        whatsapp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = "https://api.whatsapp.com/send?phone=+91 " + data.get(position).getMobile();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                context.startActivity(i);
                            }
                        });
                    }
                });

                if(pref.getString("key_name","").equalsIgnoreCase("admin")){
                    holder.editIV.setVisibility(View.VISIBLE);
                }else{
                    holder.editIV.setVisibility(View.GONE);
                }
                holder.editIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Dialog d = new Dialog(ViewDoctors.this);
                        //d.setCancelable(false);
                        d.setContentView(R.layout.dialog_add_doctor);
                        d.show();

                        TextInputEditText nameET = d.findViewById(R.id.nameET);
                        TextInputEditText mobileET = d.findViewById(R.id.mobileET);
                        TextInputEditText specialistET = d.findViewById(R.id.sepcialistET);
                        TextInputEditText latitudeET = d.findViewById(R.id.latitudeET);
                        TextInputEditText longitudeET = d.findViewById(R.id.longitudeET);
                        TextInputEditText addressET = d.findViewById(R.id.addressET);
                        Button delete = d.findViewById(R.id.deleteBT);
                        Button update = d.findViewById(R.id.updateBT);

                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                //Toast.makeText(ViewDoctors.this, ""+data.get(position).getId(), Toast.LENGTH_SHORT).show();
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("doctors").
                                            child(data.get(position).getId());
                                    databaseReference.removeValue();
                                    Toast.makeText(ViewDoctors.this, "Deleted.", Toast.LENGTH_SHORT).show();
                                    d.dismiss();

                                    notifyDataSetChanged();

                            }
                        });
                        update.setOnClickListener(new View.OnClickListener() {
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

                                    DoctorModel model = new DoctorModel();
                                    model.setId(data.get(position).getId());
                                    model.setName(name);
                                    model.setMobile(mobile);
                                    model.setSpecialist(specialist);
                                    model.setLatitude(latitude);
                                    model.setLongitude(lonitude);
                                    model.setAddress(address);

                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("doctors").
                                            child(data.get(position).getId());
                                    databaseReference.setValue(model);
                                    Toast.makeText(ViewDoctors.this, "Updated.", Toast.LENGTH_SHORT).show();
                                    d.dismiss();
                                }
                            }
                        });

                    }
                });

            }

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.nameTV)
            TextView nameTV;
            @BindView(R.id.mobileTV)
            TextView mobileTV;
            @BindView(R.id.specialistTV)
            TextView specialistTV;
            @BindView(R.id.addressTV)
            TextView addressTV;
            @BindView(R.id.infoTV)
            TextView infoTV;
            @BindView(R.id.editIV)
            ImageView editIV;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public void composeSmsMessage(String message, String phoneNumber) {
            try {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.putExtra("sms_body", message);
                intent.setData(Uri.parse("smsto:" + phoneNumber));
                context.startActivity(intent);
            } catch (android.content.ActivityNotFoundException anfe) {
                Log.d("Error", "Error");
            }
        }

    }

}
