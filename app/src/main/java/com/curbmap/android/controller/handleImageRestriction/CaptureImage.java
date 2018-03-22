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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.curbmap.android.controller.CheckPermissions;
import com.curbmap.android.models.lib.Compass;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class CaptureImage {
    private static final String TAG = "CaptureImage";

    private static final int REQUEST_IMAGE_CAPTURE = 111;

    /**
     * Given activity, context and compass objects,
     * this function processes and finds the azimuth of image and
     * appropriate imagePath to store the image
     * and the intent for launching the camera
     * and returns the CaptureImageObject which stores this new information
     * @param activity The current activity
     * @param context The current application context
     * @param compass A compass object to find the azimuth from
     * @return CaptureImageObject containing the
     * take picture intent, azimuth of device and appropriate path to store captured image
     */
    public static CaptureImageObject captureImage(
            Activity activity,
            Context context,
            Compass compass
    ) {

        Log.d(TAG, "beginning of captureImage");
        String imagePath;
        float azimuth;

        if (CheckPermissions.checkCameraPermission(activity, context)) {
            if (CheckPermissions.checkWritePermission(activity, context)) {

                String storageState = Environment.getExternalStorageState();
                if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                    Date currentTime = Calendar.getInstance().getTime();
                    long time = currentTime.getTime();

                    //we create a filename based on current time
                    //this is a way to store unique filenames
                    //and also allows us to upload images in chronological order
                    String filename = String.valueOf(time);
                    imagePath = Environment.getExternalStorageDirectory() +
                            "/Android/data/" +
                            context.getPackageName() +
                            "/files/" +
                            filename +
                            ".jpg";



                    //---------------------------------------------------------------------
                    //this section checks whether there is duplicate file
                    //and makes sure to delete the previous if there is a duplicate
                    //and *then* creates the file at the path for the camera
                    // later to store the image at
                    //it is unnecessary now because we are using timestamp as the filename
                    //but it remains here just in case...
                    File _photoFile = new File(imagePath);
                    Log.d("the path is ", imagePath);
                    try {
                        if (!_photoFile.exists()) {
                            _photoFile.getParentFile().mkdirs();
                            _photoFile.createNewFile();
                            Log.d(TAG, "created new file");
                        } else {
                            _photoFile.delete();
                            _photoFile.getParentFile().mkdirs();
                            _photoFile.createNewFile();
                            Log.d(TAG, "replaced old file");
                        }

                    } catch (IOException e) {
                        Log.e(TAG, "Could not create file.", e);
                    }
                    Log.i(TAG, imagePath);
                    //---------------------------------------------------------------------

                    Uri _fileUri = Uri.fromFile(_photoFile);


                    Log.d("compass-azimuth", String.valueOf(compass.getAzimuth()));
                    azimuth = compass.getAzimuth();

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, _fileUri);

                    return new CaptureImageObject(
                            takePictureIntent,
                            azimuth,
                            imagePath
                    );

                } else {
                    new AlertDialog.Builder(context)
                            .setMessage("External Storage (SD Card) is required.\n\n" +
                                    "Current state: " +
                                    storageState)
                            .setCancelable(true).create().show();
                }
            }
        }

        return null;
    }
}
