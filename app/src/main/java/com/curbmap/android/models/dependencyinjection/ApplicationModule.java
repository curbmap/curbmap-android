/*
 * *
 *  * Copyright (C) 2017 Ryan Kay Open Source Project
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.curbmap.android.models.dependencyinjection;

import android.app.Application;

import com.curbmap.android.models.SettingsApplication;

import dagger.Module;
import dagger.Provides;

/**
 *
 *
 * Created by R_KAY on 8/15/2017.
 */
@Module
public class ApplicationModule {
    private final SettingsApplication application;
    public ApplicationModule(SettingsApplication application) {
        this.application = application;
    }

    @Provides
    SettingsApplication provideSettingsApplication(){
        return application;
    }

    @Provides
    Application provideApplication(){
        return application;
    }
}
