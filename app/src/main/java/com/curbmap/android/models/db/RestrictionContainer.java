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
import android.util.Log;

import com.google.gson.annotations.SerializedName;

/**
 * A generic restriction container that can contain either a
 * text or image restriction, or even an image restriction
 * which has its associated text restriction.
 * <p>
 * timestamp is used for sorting restrictions chronologically
 * in the Your Contributions fragment
 * <p>
 * We have to store the restriction as a generic object
 * because if we try to store it as either a Restriction or RestrictionImage object
 * Room will have trouble choosing which constructor to use
 * so now it will be up to us to determine what type of restriction is stored...
 */

@Entity(tableName = "restriction")
public class RestrictionContainer {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("isImage")
    private boolean isImage;

    @SerializedName("time")
    private long time;

    @SerializedName("isUploaded")
    private boolean isUploaded = false;

    @SerializedName("object")
    private String object;


    /**
     * Constructor for text restriction
     *
     * @param object  String of either RestrictionText or restrictionImage object
     * @param time    The current timestamp which can be generated as follows:
     *                Date currentTime = Calendar.getInstance().getTime();
     *                long time = currentTime.getTime();
     * @param isImage true if restriction is image, false if restriction is text (not image)
     */
    RestrictionContainer(String object, long time, boolean isImage) {
        this.object = object;
        this.time = time;
        this.isImage = isImage;
    }

    public String getImagePath() {
        if (this.isImageRestriction()) {
            RestrictionImage restrictionImage = RestrictionImageConverter.fromString(this.object);
            return restrictionImage.getImagePath();
        } else {
            return "";
        }
    }

    public void setRestrictionAsUploaded() {
        if (this.isUploaded) {
            /*
                If this error is logged ,it means that the image was already set as uploaded
                but setRestrictionAsUploaded was called.
                This means that something in the logic of the async
                of the uploading of image was wrong, possibly resulting in
                uploading the same image multiple times to the server!
                That would be a serious bug, because
                we must only upload each image once to the server.
             */
            Log.e("RestrictionContainer", "redundant call to setRestrictionAsUploaded");
        }

        this.isUploaded = true;

    }


    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isUploaded() {
        return this.isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }


    /**
     * @return true if Restriction Container contains the text information of its restriction
     * false otherwise
     * Warning: it will return true even if it contains image of restriction.
     */
    public boolean isTextRestriction() {
        return !this.isImage;
    }

    /**
     * @return true if Restriction Container contains the image of restriction
     * false otherwise.
     * Warning: it will return true even if it contains text of restriction.
     */
    public boolean isImageRestriction() {
        return this.isImage;
    }


}
