package com.curbmap.android.models.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.curbmap.android.models.db.SettingsRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CustomViewModelFactory implements ViewModelProvider.Factory {
    private final SettingsRepository repository;

    @Inject
    public CustomViewModelFactory(SettingsRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SettingsRepository.class)) {
            return (T) new SettingsViewModel(repository);
        }
        else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }
}
