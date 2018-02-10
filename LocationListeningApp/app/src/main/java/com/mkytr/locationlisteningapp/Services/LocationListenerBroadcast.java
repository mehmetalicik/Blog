package com.mkytr.locationlisteningapp.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import com.google.android.gms.location.LocationResult;
import com.mkytr.locationlisteningapp.Database.DbLocation;
import com.mkytr.locationlisteningapp.Database.Utility;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mkyka on 9.02.2018.
 */

public class LocationListenerBroadcast extends BroadcastReceiver {
    private static final String TAG = "LocationListenerBroadcast";
    public static final String ACTION_PROCESS_UPDATES = "com.mkytr.locationlisteningapp.action" + ".PROCESS_UPDATES";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null){
            Random randomColor = new Random();
            final String action = intent.getAction();
            if(ACTION_PROCESS_UPDATES.equals(action)){
                LocationResult result = LocationResult.extractResult(intent);
                if(result != null){
                    List<Location> locationsResult = result.getLocations();
                    List<DbLocation> locationDbResult = new ArrayList<>();
                    for(Location location : locationsResult){
                        DbLocation toAdd = new DbLocation();
                        toAdd.accuracy = location.getAccuracy();
                        toAdd.latitude = location.getLatitude();
                        toAdd.longitude = location.getLongitude();
                        toAdd.speed = location.getSpeed();
                        toAdd.timestamp = location.getTime();
                        toAdd.colorid = randomColor.nextInt(5);
                        locationDbResult.add(toAdd);
                    }
                    // Utility.saveLocations(context, locationDbResult);
                    SaveLocationsTask saveLocations = new SaveLocationsTask(context, locationDbResult);
                    saveLocations.execute();
                }
            }
        }
    }
}
