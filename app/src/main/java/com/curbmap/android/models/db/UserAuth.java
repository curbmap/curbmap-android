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

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

@Entity
public class UserAuth {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("username")
    String username;

    @SerializedName("password")
    String password;

    @SerializedName("timeOfLastHandshake")
    long timeOfLastHandshake;

    /**
     * Creates a new UserAuth object
     * We have to explicitly store the password in the database so that
     * we can renew the session token...
     * otherwise, currently there is no
     * other way to reestablish authentication
     * with the server...
     * These two values should be captured when the user performs login
     * @param username the user's usernam
     * @param password the user's password
     */
    public UserAuth(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public String getUsername() {
        return this.username;
    }
    /**
     * Gets the password of the currently logged in user
     * @return the user's password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Stores the time of last handshake as right now.
     * by passing in a new instance of Calendar
     * this must be passed in at the time of handshake
     */
    public void setTimeOfLastHandshakeAsRightNow() {
        Calendar rightNow = Calendar.getInstance();
        this.timeOfLastHandshake = rightNow.getTimeInMillis();
    }

    /**
     * Determines whether or not we should perform a new handshake right now.
     * It is based on how often the user session expires
     * or at least... based on how often we want to renew
     *   the user session token on the device
     * @return true if we should perform new handshake, false otherwise
     */
    public boolean shouldWeHandShakeRightNow() {
        //86400 * 1000 is one day, because current handshake interval is one day
        //because currently we assume that session token expires 24 hours
        //  since last handshake
        long HANDSHAKE_INTERVAL = 86400 * 1000;
        Calendar rightNow = Calendar.getInstance();
        long currentTime = rightNow.getTimeInMillis();
        long current_interval  = currentTime - this.timeOfLastHandshake;
        return current_interval > HANDSHAKE_INTERVAL;
    }

}
