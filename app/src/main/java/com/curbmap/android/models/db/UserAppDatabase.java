package com.curbmap.android.models.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {User.class}, version = 2, exportSchema = false)
public abstract class UserAppDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
}