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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RestrictionAccessor {
    private static final String TAG = "RestrictionAccessor";

    public RestrictionAccessor() {
    }

    /**
     * Inserts an IMAGE restriction into the restriction database
     * Please note that the timestamp recorded is based on time of entry into database
     * because we assume the restriction is entered into database right after creation
     *
     * @param restrictionAppDatabase the restriction database
     * @param restrictionImage       the restriction image object to enter into
     *                               the restriction database
     */
    public static void insertImageRestriction(
            AppDatabase restrictionAppDatabase,
            RestrictionImage restrictionImage) {
        RestrictionDao restrictionDao = restrictionAppDatabase.getRestrictionDao();
        Date currentTime = Calendar.getInstance().getTime();
        long time = currentTime.getTime();
        String object = RestrictionImageConverter.toString(restrictionImage);
        RestrictionContainer restrictionContainer = new RestrictionContainer(
                object, time, true);
        restrictionDao.insertAll(restrictionContainer);
    }


    /**
     * Inserts a TEXT restriction into the restriction database
     * Please note that the timestamp recorded is based on time of entry into database
     * because we assume the restriction is entered into database right after creation
     *
     * @param restrictionAppDatabase the restriction database
     * @param restrictionText            the restrictionText to enter into the restriction database
     */
    public static void insertRestriction(
            AppDatabase restrictionAppDatabase,
            RestrictionText restrictionText) {
        RestrictionDao restrictionDao = restrictionAppDatabase.getRestrictionDao();
        Date currentTime = Calendar.getInstance().getTime();
        long time = currentTime.getTime();
        String restrictionString = RestrictionTextConverter.toString(restrictionText);
        RestrictionContainer restrictionContainer = new RestrictionContainer(
                restrictionString, time, false);
        restrictionDao.insertAll(restrictionContainer);
    }

    /**
     * Returns the entire list of restriction containers stored in the restriction database
     *
     * @param restrictionAppDatabase the restriction database
     * @return the list of all restrictions stored in the restriction database
     */
    public static List<RestrictionContainer> getAllRestriction(AppDatabase restrictionAppDatabase) {
        RestrictionDao restrictionDao = restrictionAppDatabase.getRestrictionDao();
        return restrictionDao.getAll();
    }

    /**
     * Returns all the uploading restrictions
     * eg the restrictions which have not been uploaded to the server
     *
     * @param restrictionAppDatabase the restriction database
     * @return the restrictions which have not been uploaded to the server
     */
    public static List<RestrictionContainer> getAllUploadingRestriction(AppDatabase restrictionAppDatabase) {
        RestrictionDao restrictionDao = restrictionAppDatabase.getRestrictionDao();
        List<RestrictionContainer> restrictionContainerList = restrictionDao.getAll();
        List<RestrictionContainer> uploadingRestrictions = new ArrayList<>();
        for (RestrictionContainer r : restrictionContainerList) {
            if (!r.isUploaded()) {
                uploadingRestrictions.add(r);
            }
        }

        return uploadingRestrictions;
    }

    /**
     * Returns all the uploaded restrictions
     * eg the restrictions which have been uploaded to the server
     *
     * @param restrictionAppDatabase the restriction database
     * @return the restrictions which have been uploaded to the server
     */
    public static List<RestrictionContainer> getAllUploadedRestriction(AppDatabase restrictionAppDatabase) {
        RestrictionDao restrictionDao = restrictionAppDatabase.getRestrictionDao();
        List<RestrictionContainer> restrictionContainerList = restrictionDao.getAll();

        List<RestrictionContainer> uploadedRestrictions = new ArrayList<>();
        for (RestrictionContainer r : restrictionContainerList) {
            if (r.isUploaded()) {
                uploadedRestrictions.add(r);
            }
        }

        return uploadedRestrictions;
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
