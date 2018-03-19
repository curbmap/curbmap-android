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

import java.util.List;

public class RestrictionAccessor {
    private static final String TAG = "RestrictionAccessor";

    public RestrictionAccessor() {
    }

    /**
     * Inserts a restriction into the restriction database
     *
     * @param restrictionAppDatabase the restriction database
     * @param restriction            the restriction to enter into the restriction database
     */
    public static void insertRestriction(AppDatabase restrictionAppDatabase, Restriction restriction) {
        RestrictionDao restrictionDao = restrictionAppDatabase.getRestrictionDao();
        restrictionDao.insertAll(restriction);
    }

    /**
     * Returns the entire list of restrictions stored in the restriction database
     *
     * @param restrictionAppDatabase the restriction database
     * @return the list of all restrictions stored in the restriction database
     */
    public static List<Restriction> getAllRestriction(AppDatabase restrictionAppDatabase) {
        RestrictionDao restrictionDao = restrictionAppDatabase.getRestrictionDao();
        return restrictionDao.getAll();
    }

    /**
     * Deletes all restrictions in the restriction database
     *
     * @param restrictionAppDatabase the restriction database
     */
    public static void deleteAllRestriction(AppDatabase restrictionAppDatabase) {
        RestrictionDao restrictionDao = restrictionAppDatabase.getRestrictionDao();
        restrictionDao.deleteAll();
    }

}
