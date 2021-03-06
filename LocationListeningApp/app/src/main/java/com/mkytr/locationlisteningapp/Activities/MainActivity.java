package com.mkytr.locationlisteningapp.Activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.mkytr.locationlisteningapp.Database.Utility;
import com.mkytr.locationlisteningapp.R;
import com.mkytr.locationlisteningapp.Services.LocationListenerBroadcast;

public class MainActivity extends AppCompatActivity {
    private static int REQUEST_LOCATION = 0;
    private static int REQUEST_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bShowResults = findViewById(R.id.bShowResults);
        Button bExportDb = findViewById(R.id.bExportDb);
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }else {
            startBackgroundService();
            bShowResults.setEnabled(true);
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_FILE);
        }else{
            bExportDb.setEnabled(true);
        }


        bShowResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultsActivity = new Intent(getApplicationContext(), ResultsActivity.class);
                startActivity(resultsActivity);
            }
        });
        bExportDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.exportDatabase(getApplicationContext());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TextView tvStatus = findViewById(R.id.tvStatus);
        if(requestCode == REQUEST_LOCATION) {
            Button bShowResults = findViewById(R.id.bShowResults);
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bShowResults.setEnabled(true);
                startBackgroundService();
            }
            else {
                tvStatus.setText(R.string.status_PermissionNeeded);
                bShowResults.setEnabled(false);
            }
        }
        if(requestCode == REQUEST_FILE){
            Button bExportDb = findViewById(R.id.bExportDb);
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                bExportDb.setEnabled(true);
            }else {
                bExportDb.setEnabled(false);
            }
        }
    }

    protected void startBackgroundService(){
        TextView tvStatus = findViewById(R.id.tvStatus);
        Button bShowResults = findViewById(R.id.bShowResults);

        // Broadcast
        Intent locationServiceIntent = new Intent(this, LocationListenerBroadcast.class);
        locationServiceIntent.setAction(LocationListenerBroadcast.ACTION_PROCESS_UPDATES);
        PendingIntent locationService = PendingIntent.getBroadcast(this, 0, locationServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        LocationRequest request = LocationRequest.create().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setFastestInterval(300000).setInterval(1800000); // 5 minutes to 30 minutes

        FusedLocationProviderClient locationClient = new FusedLocationProviderClient(this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationClient.requestLocationUpdates(request, locationService);
            tvStatus.setText(R.string.status_ServiceStarted);
            bShowResults.setEnabled(true);
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }
}
