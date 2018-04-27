package com.curbmap.android.models.db;

import android.arch.lifecycle.LiveData;

import javax.inject.Inject;

/**
 * The SettingsRepository contains and holds a SettingsDao object
 * so whenever we need to use the Dao, we just go through the repository instead
 */
public class SettingsRepository {
    private static final String TAG = "SettingsRepository";
    private final SettingsDao settingsDao;

    @Inject
    public SettingsRepository(SettingsDao settingsDao) {
        this.settingsDao = settingsDao;
    }

    public LiveData<Settings> getSettings() {
        return settingsDao.getSettings();
    }

    public void insertSettings(Settings settings) {
        settingsDao.insertSettings(settings);
    }

    /**
     * Deletes all settings objects in the database
     */
    public void deleteSettings() {
        settingsDao.deleteSettings();
    }



}
