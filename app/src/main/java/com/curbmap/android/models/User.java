package com.curbmap.android.models;

import com.google.gson.annotations.SerializedName;

public class User {
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
