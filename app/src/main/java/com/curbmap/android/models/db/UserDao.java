package com.curbmap.android.models.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user LIMIT 1")
    User getUser();

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM user")
    void deleteUser();

}