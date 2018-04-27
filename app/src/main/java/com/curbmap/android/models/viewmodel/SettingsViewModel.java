package com.curbmap.android.models.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.curbmap.android.models.db.Settings;
import com.curbmap.android.models.db.SettingsRepository;

/**
 * Handles the ViewModel to handle accessing
 *  and manipulating the Settings database
 */
public class SettingsViewModel extends ViewModel {

    private SettingsRepository repository;

    SettingsViewModel(SettingsRepository repository) {
        this.repository = repository;
    }

    /**
     * @return the current settings object
     */
    public LiveData<Settings> getSettings() {
        return repository.getSettings();
    }


    /**
     * Updates the value of uploadOverWifi in the settings object in database
     * @param uploadOverWifi
     * todo: figure out how to update the value of livedata
     */
    /*
    public void setUploadOverWifi(boolean uploadOverWifi) {
        LiveData<Settings> settings = repository.getSettings();

        Settings newSettings = new Settings();
        settings.postValue(newSettings);
    }
    */


}
