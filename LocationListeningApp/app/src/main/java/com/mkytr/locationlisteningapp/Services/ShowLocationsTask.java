package com.mkytr.locationlisteningapp.Services;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mkytr.locationlisteningapp.Database.DbLocation;
import com.mkytr.locationlisteningapp.Database.Utility;

import java.util.Date;
import java.util.List;

/**
 * Created by mkyka on 8.02.2018.
 */

public class ShowLocationsTask extends AsyncTask<Void, Void, Void> {
    private GoogleMap mMap;
    private Context context;
    private List<DbLocation> locations;

    private static int[] circleColors = { Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA,
                                            Color.RED };

    private static float[] markerColors = { BitmapDescriptorFactory.HUE_BLUE, BitmapDescriptorFactory.HUE_CYAN,
                                            BitmapDescriptorFactory.HUE_GREEN, BitmapDescriptorFactory.HUE_MAGENTA,
                                            BitmapDescriptorFactory.HUE_RED };

    public ShowLocationsTask(Context getContext, GoogleMap getMap){
        this.mMap = getMap;
        this.context = getContext;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(mMap == null)
            return null;

        locations = Utility.getLocations(context);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        for(DbLocation location : locations){
            LatLng latLng = new LatLng(location.latitude, location.longitude);
            Date locationDate = new Date(location.timestamp);
            String snippetText = "Speed: " + String.valueOf(location.speed)
                    + " Time: " + locationDate.toString();

            // Pick random color each location in history to easily distinguish
            MarkerOptions mOptions = new MarkerOptions().position(latLng).snippet(snippetText)
                    .title(String.valueOf(location.id)).icon(BitmapDescriptorFactory.defaultMarker(
                            markerColors[location.colorid]
                    ));

            // Add alpha value to add transparency in circle color
            int orgColor = circleColors[location.colorid];
            int alphaCircleColor = Color.argb(100, Color.red(orgColor), Color.green(orgColor),
                    Color.blue(orgColor));
            CircleOptions cOptions = new CircleOptions().center(latLng).radius(location.accuracy)
                    .strokeColor(orgColor).fillColor(alphaCircleColor).strokeWidth(4);

            mMap.addMarker(mOptions);
            mMap.addCircle(cOptions);
        }

        // Move camera to last location in history
        DbLocation lastItem = locations.get(locations.size()-1);
        LatLng lastPositionLocation = new LatLng(lastItem.latitude, lastItem.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lastPositionLocation));
    }
}
