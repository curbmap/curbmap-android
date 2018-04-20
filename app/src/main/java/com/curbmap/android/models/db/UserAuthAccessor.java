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

import java.util.Calendar;

public class UserAuthAccessor {
    private static final String TAG = "UserAuthAccessor";

    public UserAuthAccessor() {
    }

    public static UserAuth getUserAuth(AppDatabase userAuthAppDatabase) {
        UserAuthDao userAuthDao = userAuthAppDatabase.getUserAuthDao();
        return userAuthDao.getUserAuth();
    }

    public static void insertUserAuth(AppDatabase userAuthAppDatabase, UserAuth userAuth) {
        UserAuthDao userAuthDao = userAuthAppDatabase.getUserAuthDao();
        userAuthDao.insert(userAuth);
    }

    /**
     * Updates the timestamp on a user auth
     * for the purposes of renewing token when performing handshake
     * @param userAuthAppDatabase The UserAuth database
     */
    public static void updateUserAuth(AppDatabase userAuthAppDatabase) {
        UserAuthDao userAuthDao = userAuthAppDatabase.getUserAuthDao();
        UserAuth userAuth = userAuthDao.getUserAuth();
        String username = userAuth.getUsername();
        String password = userAuth.getPassword();
        long timestamp = Calendar.getInstance().getTimeInMillis();
        userAuthDao.deleteUserAuth();
        UserAuth newUserAuth = new UserAuth(
                username,
                password,
                timestamp
        );
        userAuthDao.insert(newUserAuth);
    }

    public static void deleteUserAuth(AppDatabase userAuthAppDatabase) {
        UserAuthDao userAuthDao = userAuthAppDatabase.getUserAuthDao();
        userAuthDao.deleteUserAuth();
    }
}