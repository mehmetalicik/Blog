package com.mkytr.locationlisteningapp.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by mkyka on 8.02.2018.
 */

@Entity
public class DbLocation {
    @PrimaryKey (autoGenerate = true)
    public int id;

    public int colorid;
    public double latitude;
    public double longitude;
    public long timestamp;
    public float speed;
    public float accuracy;
}
