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

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {Restriction.class, User.class, UserAuth.class},
        version = 3,
        exportSchema = false)
@TypeConverters({PolylineConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    static final String TAG = "AppDatabase";

    private static AppDatabase INSTANCE;

    /**
     * Gets the restriction database
     *
     * @param context The context of the application
     * @return The restriction database
     */
    public static AppDatabase getRestrictionAppDatabase(Context context) {
        if (INSTANCE != null) destroyInstance();
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "restrictions")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    /**
     * Gets the user database
     *
     * @param context The context of the application
     * @return The user database
     */
    public static AppDatabase getUserAppDatabase(Context context) {
        if (INSTANCE != null) destroyInstance();
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "user")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    /**
     * Gets the UserAuth database
     *
     * @param context The context of the application
     * @return The UserAuth database
     */
    public static AppDatabase getUserAuthAppDatabase(Context context) {
        if (INSTANCE != null) destroyInstance();
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "userAuthentication")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    /**
     * Destroys the PersonAppDatabase instance
     * This is important to call whenever an activity is destroyed so that
     * we do not have a SqLite leak.
     * Although it is an option to instantiate it once and keep passing it around
     * until an activity is finally destroyed, we choose to instantiate and destroy it
     * within every single activity because it reduces the corruptibility of the code.
     * Because for example, what if the user somehow ends up in an activity without visiting a prior
     * activity? For example due to some app cache mechanism that loads up an acitivity straight away.
     * In that case we would have to create the database anyway.
     * However, if the database becomes too large then it might become necessary for performance
     * to only load up the database once and keep passing the object around.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract RestrictionDao getRestrictionDao();

    public abstract UserDao getUserDao();

    public abstract UserAuthDao getUserAuthDao();
}