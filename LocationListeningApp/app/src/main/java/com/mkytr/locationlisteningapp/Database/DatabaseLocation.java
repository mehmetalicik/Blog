package com.mkytr.locationlisteningapp.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by mkyka on 8.02.2018.
 */

@Database(entities = {DbLocation.class}, version = 2, exportSchema = false)
public abstract class DatabaseLocation extends RoomDatabase{
    public abstract DaoLocation locationDao();
}
