package com.curbmap.android.models.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

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

    @SerializedName("session")
    String session;





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
