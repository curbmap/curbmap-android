/*
 * Copyright (c) 2018 curbmap.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.curbmap.android.models.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Dao for CRUD of Settings object
 * The database is meant to hold a maximum of one Settings object
 * In other words, at any given moment there will only be
 *  zero or one Settings objects stored in the database
 */
@Dao
public interface SettingsDao {
    /**
     * Get the current settings object
     * @return
     */
    @Query("SELECT * FROM settings LIMIT 1")
    LiveData<Settings> getSettings();

    /**
     * Insert a new Settings object
     * @param settings
     */
    @Insert(onConflict = REPLACE)
    void insertSettings(Settings settings);

    /**
     * Delete the current Settings object
     * @param settings
     */
    @Delete
    void delete(Settings settings);

    /**
     * Deletes all settings
     * This should not be necessary but use just in case
     *  the database stores multiple settings object
     */
    @Query("DELETE FROM settings")
    void deleteSettings();

}