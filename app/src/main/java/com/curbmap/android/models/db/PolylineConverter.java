package com.curbmap.android.models.db;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;

public class PolylineConverter {
    @TypeConverter
    public Polyline fromString(String polylineString) {
        Gson gson = new Gson();
        return gson.fromJson(polylineString, Polyline.class);
    }

    @TypeConverter
    public String toString(Polyline polyline) {
        Gson gson = new Gson();
        return gson.toJson(polyline);
    }
}
