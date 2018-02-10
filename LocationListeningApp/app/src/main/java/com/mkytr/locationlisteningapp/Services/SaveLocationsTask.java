package com.mkytr.locationlisteningapp.Services;

import android.content.Context;
import android.os.AsyncTask;

import com.mkytr.locationlisteningapp.Database.DbLocation;
import com.mkytr.locationlisteningapp.Database.Utility;

import java.util.List;

/**
 * Created by mkyka on 9.02.2018.
 */

public class SaveLocationsTask extends AsyncTask<Void, Void, Void> {
    private Context context;
    private List<DbLocation> locations;

    public SaveLocationsTask(Context context, List<DbLocation> locations){
        this.context = context;
        this.locations = locations;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Utility.saveLocations(context, locations);
        return null;
    }
}
