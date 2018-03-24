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

public class SettingsAccessor {
    private static final String TAG = "SettingsAccessor";

    public SettingsAccessor() {
    }

    public static Settings getSettings(AppDatabase settingsAppDatabase) {
        SettingsDao settingsDao = settingsAppDatabase.getSettingsDao();
        return settingsDao.getSettings();
    }

    public static void insertSettings(AppDatabase settingsAppDatabase, Settings settings) {
        SettingsDao settingsDao = settingsAppDatabase.getSettingsDao();
        settingsDao.insert(settings);
    }

    public static void deleteSettings(AppDatabase settingsAppDatabase) {
        SettingsDao settingsDao = settingsAppDatabase.getSettingsDao();
        settingsDao.deleteSettings();
    }

    public static void setUploadOverWifi(AppDatabase settingsAppDatabase,
                                         boolean uploadOverWifi) {
        SettingsDao settingsDao = settingsAppDatabase.getSettingsDao();
        Settings settings = settingsDao.getSettings();
        settings.setUploadOverWifi(uploadOverWifi);
        settingsDao.deleteSettings();
        settingsDao.insert(settings);
    }
}