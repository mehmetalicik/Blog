package com.mkytr.locationlisteningapp.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by mkyka on 8.02.2018.
 */

@Dao
public interface DaoLocation {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocations(List<DbLocation> locations);

    @Query("SELECT * FROM DbLocation ORDER BY timestamp ASC")
    List<DbLocation> getAllLocations();

    @Query("DELETE FROM DbLocation")
    void deleteLocations();
}
