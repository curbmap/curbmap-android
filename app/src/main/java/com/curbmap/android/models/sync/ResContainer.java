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

package com.curbmap.android.models.sync;

/**
 * Defines the container for a restriction
 * A ResContainer can contain either an image or form-based restriction
 * this is meant to be a unified container to hold restrictions
 * for the proper processing of uploading images to the server
 * and for aligning the restrictions on the gridview
 * in the user's 'Your Contributions' view
 */
public class ResContainer {


    /**
     * Gets the file path to the thumbnail that is associated with the restriction
     * For an image-based restriction, returns a thumbnail of the actual image.
     * For a form-based restriction, returns a generic thumbnail based on type of restriction.
     *
     * @return file path to thumbnail of restriction
     */
    public String getThumbnail() {
        return "";
    }

    /**
     * Gets the timestamp of a restriction for performing chronological sorting
     *
     * @return the time the restriction was created by the user in milliseconds since epoch
     */
    public long getTimestamp() {
        return 0;
    }

    /**
     * Generically performs the uploading of the restriction to the server,
     * regardless of whether or not the restriction is image or form based.
     * This should probably be an asynctask or something like that
     */
    public void doUpload() {

    }
}
