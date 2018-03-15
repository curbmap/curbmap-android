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

/**
 * The User object which stores information about a user.
 * It is important not to modify this because, it is based on what the server uses.
 * If we modify this file, we will not be able to use the retrofit/gson function
 * which automatically converts the server's REST response into appropriate POJO
 */
@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("username")
    String username;

    @SerializedName("badge")
    String badge;

    @SerializedName("badgeLastUpdated")
    String badgeLastUpdated;

    @SerializedName("score")
    Integer score;

    @SerializedName("scoreUpdatedAt")
    String scoreUpdatedAt;

    /**
     * The session token to authenticate the user is logged in
     * Warning: the session token expires.
     * We think it expires after 24 hours but are not sure!
     * Therefore, if 404 error is received from the server
     * when sending requests to port 500003, eg imageUpload
     * then this may be caused by an expired session token.
     * The solution is to renew the session token
     * by sending a new login request to the server
     * every so often, eg every 24 hours minimum.
     * We send a new request every time the app is launched
     * because we assume nobody will use the app for more than 24 hours nonstop...
     */
    @SerializedName("session")
    String session;

    /**
     * Creates a string showing the user's information
     * This is meant for debugging purposes.
     * @return User's information as a String.
     */
    public String makeString() {
        String result = "";
        result += "username : " + this.getUsername();
        result += ", badge : " + this.getBadge();
        result += ", badgeLastUpdated : " + this.getBadgeLastUpdated();
        result += ", score : " + this.getScore();
        result += ", scoreUpdatedAt : " + this.getScoreUpdatedAt();
        result += ", session : " + this.getSession();
        return result;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getBadgeLastUpdated() {
        return badgeLastUpdated;
    }

    public void setBadgeLastUpdated(String badgeLastUpdated) {
        this.badgeLastUpdated = badgeLastUpdated;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getScoreUpdatedAt() {
        return scoreUpdatedAt;
    }

    public void setScoreUpdatedAt(String scoreUpdatedAt) {
        this.scoreUpdatedAt = scoreUpdatedAt;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
