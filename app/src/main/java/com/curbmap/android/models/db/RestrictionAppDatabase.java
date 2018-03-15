package com.curbmap.android.models.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {Restriction.class}, version = 6, exportSchema = false)
@TypeConverters({PolylineConverter.class})
public abstract class RestrictionAppDatabase extends RoomDatabase {
    public abstract RestrictionDao getRestrictionDao();
}