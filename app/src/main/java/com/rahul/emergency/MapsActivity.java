package com.rahul.emergency;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseReference databaseReference;
    ArrayList<DoctorModel> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setTitle("Doctors");
        data = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("doctors");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(11.0797, 76.9997);
        mMap.addMarker(new MarkerOptions().position(sydney).title("I'm here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //mMap.setMinZoomPreference(12.3f);

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DoctorModel model = postSnapshot.getValue(DoctorModel.class);
                    data.add(model);

                }

                for (int i = 0; i < data.size(); i++) {

                    Log.e("DDDD", "" + data.get(i).getLatitude() + "/" + data.get(i).getLongitude());

                    final LatLng name = new LatLng(Double.parseDouble(data.get(i).getLatitude()), Double.parseDouble(data.get(i).getLongitude()));
                    mMap.addMarker(new MarkerOptions()
                            .position(name)
                            .title(data.get(i).getName())
                            .snippet(data.get(i).getMobile())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                }

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        String s = marker.getSnippet();

                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MapsActivity.this);
                        bottomSheetDialog.setContentView(R.layout.bottomsheetc_contact);
                        bottomSheetDialog.show();

                        ImageView call = bottomSheetDialog.findViewById(R.id.callIV);
                        ImageView message = bottomSheetDialog.findViewById(R.id.messageIV);
                        ImageView map = bottomSheetDialog.findViewById(R.id.mapIV);
                        ImageView whatsapp = bottomSheetDialog.findViewById(R.id.whatsappIV);

                        call.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + s));
                                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    Activity#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for Activity#requestPermissions for more details.
                                    return;
                                }
                                startActivity(intent);


                            }
                        });

                        message.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                composeSmsMessage("I'm in need of help!!!", s);

                            }
                        });

                        whatsapp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = "https://api.whatsapp.com/send?phone=+91 " + s;
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);

                            }
                        });

                        return false;
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void composeSmsMessage(String message, String phoneNumber) {
        try {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.putExtra("sms_body", message);
            intent.setData(Uri.parse("smsto:"+phoneNumber));
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException anfe) {
            Log.d("Error", "Error");
        }
    }

}