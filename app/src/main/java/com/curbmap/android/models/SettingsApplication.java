package com.curbmap.android.models;

import android.app.Application;

import com.curbmap.android.models.dependencyinjection.ApplicationComponent;
import com.curbmap.android.models.dependencyinjection.ApplicationModule;
import com.curbmap.android.models.dependencyinjection.DaggerApplicationComponent;
import com.curbmap.android.models.dependencyinjection.RoomModule;

public class SettingsApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .roomModule(new RoomModule(this))
                .build();

    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
