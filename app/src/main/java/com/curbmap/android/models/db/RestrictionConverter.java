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

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;

/**
 * Converts Restriction to and from String
 */
public class RestrictionConverter {
    @TypeConverter
    public static Restriction fromString(String restrictionString) {
        Gson gson = new Gson();
        return gson.fromJson(restrictionString, Restriction.class);
    }

    @TypeConverter
    public static String toString(Restriction restriction) {
        Gson gson = new Gson();
        return gson.toJson(restriction);
    }
}
