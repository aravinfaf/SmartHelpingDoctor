package com.rahul.emergency;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rahul.emergency.db.DBManager;
import com.rahul.emergency.db.DatabaseHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoctorsListActivity extends AppCompatActivity {

    @BindView(R.id.list_view)
    ListView list_view;
    @BindView(R.id.emptyTV)
    TextView emptyTV;

    private DBManager dbManager;
    private SimpleCursorAdapter adapter;
    final String[] from = new String[]{
            DatabaseHelper.NAME, DatabaseHelper.SPECIALIST, DatabaseHelper.MOBILE, DatabaseHelper.ADDRESS};

    final int[] to = new int[]{R.id.nameTV, R.id.mobileTV, R.id.specialistTV, R.id.addressTV};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list);
        ButterKnife.bind(DoctorsListActivity.this);

        setTitle("Doctors List");

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        adapter = new SimpleCursorAdapter(this, R.layout.activity_view_record, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        list_view.setAdapter(adapter);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView mobile = view.findViewById(R.id.specialistTV);
                TextView latitude = view.findViewById(R.id.latitudeTV);
                TextView longitude = view.findViewById(R.id.logitudeTV);

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DoctorsListActivity.this);
                bottomSheetDialog.setContentView(R.layout.bottomsheetc_contact);
                bottomSheetDialog.show();

                ImageView call = bottomSheetDialog.findViewById(R.id.callIV);
                ImageView message = bottomSheetDialog.findViewById(R.id.messageIV);
                ImageView map = bottomSheetDialog.findViewById(R.id.mapIV);
                ImageView whatsapp = bottomSheetDialog.findViewById(R.id.whatsappIV);

                String phone = mobile.getText().toString();
                String lat = latitude.getText().toString();
                String longi = longitude.getText().toString();

                call.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phone));
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
                    public void onClick(View v) {


                        composeSmsMessage("I'm in need of help!!!", phone);

                    }
                });
                whatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://api.whatsapp.com/send?phone=+91 " + phone;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
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
