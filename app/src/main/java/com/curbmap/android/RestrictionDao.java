package com.curbmap.android;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RestrictionDao {
    @Query("SELECT * FROM restriction")
    List<Restriction> getAll();

    @Insert
    void insertAll(Restriction... restrictions);

    @Delete
    void delete(Restriction restriction);
}