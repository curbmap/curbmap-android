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

    @SerializedName("role")
    String role;

    @SerializedName("badge")
    String badge;

    @SerializedName("badge_updatedAt")
    String badge_updatedAt;

    @SerializedName("score")
    Integer score;

    @SerializedName("score_updatedAt")
    String score_updatedAt;

    @SerializedName("email")
    String email;

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
    @SerializedName("token")
    String token;

    /**
     * Creates a string showing the user's information
     * This is meant for debugging purposes.
     *
     * @return User's information as a String.
     */
    public String makeString() {
        String result = "This is the string generated for logged in user\n";
        result += "username: " + this.getUsername() + "\n"
                + "role: " +  this.getRole() + "\n"
                + "badge: " + this.getBadge() + "\n"
                + "badge_updatedAt: " + this.getBadge_updatedAt() + "\n"
                + "score: " + this.getScore() + "\n"
                + "score_updatedAt: " + this.getScore_updatedAt() + "\n"
                + "token: " + this.getToken() + "\n"
                + "email: " + this.getEmail();
        return result;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getBadge_updatedAt() {
        return badge_updatedAt;
    }

    public void setBadge_updatedAt(String badge_updatedAt) {
        this.badge_updatedAt = badge_updatedAt;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getScore_updatedAt() {
        return score_updatedAt;
    }

    public void setScore_updatedAt(String score_updatedAt) {
        this.score_updatedAt = score_updatedAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
