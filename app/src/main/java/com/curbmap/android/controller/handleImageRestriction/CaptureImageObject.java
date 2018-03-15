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

package com.curbmap.android.controller.handleImageRestriction;

import android.content.Intent;

/**
 * Stores the information needed to capture an image.
 */
public class CaptureImageObject {
    public Intent takePictureIntent;
    public float azimuth;
    public String imagePath;

    /**
     * I like cats too :)
     * @param takePictureIntent The intent that launches the camera to take a picture
     * @param azimuth The direction the device faces as degrees clockwise from True North
     * @param imagePath The path that the image should be stored on the device
     */
    public CaptureImageObject(Intent takePictureIntent, float azimuth, String imagePath) {
        this.takePictureIntent = takePictureIntent;
        this.azimuth = azimuth;
        this.imagePath = imagePath;
    }
}
