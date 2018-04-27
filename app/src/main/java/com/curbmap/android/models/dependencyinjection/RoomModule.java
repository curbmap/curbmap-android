package com.curbmap.android.models.dependencyinjection;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;

import com.curbmap.android.models.db.AppDatabase;
import com.curbmap.android.models.db.SettingsDao;
import com.curbmap.android.models.db.SettingsRepository;
import com.curbmap.android.models.viewmodel.CustomViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {
    private final AppDatabase database;

    public RoomModule(Application application) {
        this.database = Room.databaseBuilder(
                application,
                AppDatabase.class,
                "settings"
        ).build();
    }

    @Provides
    @Singleton
    SettingsRepository provideSettingsRepository(SettingsDao settingsDao) {
        return new SettingsRepository(settingsDao);
    }

    @Provides
    @Singleton
    SettingsDao provideSettingsDao(AppDatabase database) {
        return database.getSettingsDao();
    }

    @Provides
    @Singleton
    AppDatabase provideSettingsDatabase(Application application) {
        return database;
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(SettingsRepository repository) {
        return new CustomViewModelFactory(repository);
    }
}
