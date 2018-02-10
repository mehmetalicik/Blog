package com.mkytr.locationlisteningapp.Activities;

import android.arch.persistence.room.Room;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mkytr.locationlisteningapp.Database.DatabaseLocation;
import com.mkytr.locationlisteningapp.Database.DbLocation;
import com.mkytr.locationlisteningapp.R;
import com.mkytr.locationlisteningapp.Services.ShowLocationsTask;

public class ResultsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        showMarkersOnMap();
    }

    private void showMarkersOnMap(){
        ShowLocationsTask task = new ShowLocationsTask(getApplicationContext(), mMap);
        task.execute();
    }
}
