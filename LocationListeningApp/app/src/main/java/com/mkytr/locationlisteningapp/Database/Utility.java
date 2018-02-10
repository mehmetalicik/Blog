package com.mkytr.locationlisteningapp.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.mkytr.locationlisteningapp.Services.ExportDatabaseTask;

import java.util.List;

/**
 * Created by mkyka on 9.02.2018.
 */

public class Utility {
    public static final String dbName = "LocationsDb";

    private static DatabaseLocation getDatabase(Context context){
        return Room.databaseBuilder(context, DatabaseLocation.class, dbName).build();
    }

    public static void saveLocations(Context context, List<DbLocation> locations){
        DatabaseLocation db = getDatabase(context);
        if(db!=null)
            db.locationDao().insertLocations(locations);

        db.close();
    }

    public static List<DbLocation> getLocations(Context context){
        DatabaseLocation db = getDatabase(context);
        List<DbLocation> result = db.locationDao().getAllLocations();
        db.close();
        return result;
    }

    public static void exportDatabase(Context context){
        ExportDatabaseTask exportDb = new ExportDatabaseTask(context);
        exportDb.execute();
    }
}
