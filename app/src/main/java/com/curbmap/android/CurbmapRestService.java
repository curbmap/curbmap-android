package com.curbmap.android;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface CurbmapRestService {

    //https://curbmap.com:50003/areaPolygon?lat1=34.12374081015902&lng1=-118.25714551520997&lat2=34.09176089567784&lng2=-118.21852170539552
    @GET("areaPolygon")
    Call<String> doAreaPolygon(
            @Header("username") String username,
            @Query("lat1") double lat1,
            @Query("lng1") double lng1,
            @Query("lat2") double lat2,
            @Query("lng2") double lng2
    );

}