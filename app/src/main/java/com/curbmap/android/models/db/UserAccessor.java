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

import android.util.Log;

public class UserAccessor {
    private static final String TAG = "UserAccessor";

    public UserAccessor() {
    }

    /**
     * Checks whether user is currently logged in or not
     *
     * @param userAppDatabase the user database
     * @return true if user is currently logged in, false otherwise
     */
    public static boolean isLoggedIn(AppDatabase userAppDatabase) {
        UserDao userDao = userAppDatabase.getUserDao();
        User user = userDao.getUser();
        return (user != null);
    }

    /**
     * Gets the string of the currently logged in user
     * otherwise returns an informative string
     *
     * @param userAppDatabase the user database
     * @return string of user or error info
     */
    public static String getString(AppDatabase userAppDatabase) {
        if (isLoggedIn(userAppDatabase)) {
            UserDao userDao = userAppDatabase.getUserDao();
            User user = userDao.getUser();
            return user.makeString();
        } else {
            //this should throw an error, but we avoid that because
            //we want the getString to be more generalized
            //just to help with debugging
            Log.e(TAG, "attempted UserAccessor.getString() when not logged in");
            return "User is not logged in";
        }
    }

    /**
     * When user signs in, insert the user into the database
     * so that we can retrieve their info later on
     *
     * @param userAppDatabase The user database
     * @param user            The user object to enter into the database
     */
    public static void insertUser(AppDatabase userAppDatabase, User user) {
        UserDao userDao = userAppDatabase.getUserDao();
        userDao.insert(user);
    }

    /**
     * Gets the currently logged in user object
     *
     * @param userAppDatabase the user database
     * @return the user object for the currently logged in user
     */
    public static User getUser(AppDatabase userAppDatabase) {
        UserDao userDao = userAppDatabase.getUserDao();
        return userDao.getUser();
    }

    /**
     * Logs out the user by deleting the user from the user database
     *
     * @param userAppDatabase the user database
     */
    public static void deleteUser(AppDatabase userAppDatabase) {
        UserDao userDao = userAppDatabase.getUserDao();
        userDao.deleteUser();
    }
}