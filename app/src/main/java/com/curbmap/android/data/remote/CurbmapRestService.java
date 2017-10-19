package com.curbmap.android.data.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;





public interface CurbmapRestService {

    //https://curbmap.com:50003/areaOLC?code=85632QQ2%2B
    @GET(":50003/areaOLC?code={code}")
    Call<List<String>> doAreaOLC(
            @Header("username") String username,
            @Path(value= "code") String code
    );

    //https://curbmap.com:50003/areaPolygon?lat1=34.12374081015902&lng1=-118.25714551520997&lat2=34.09176089567784&lng2=-118.21852170539552
    @GET(":50003/areaPolygon")
    Call<List<String>> doAreaPolygon(
            @Header("username") String username,
            @Query("lat1") String lat1,
            @Query("lng1") String lng1,
            @Query("lat2") String lat2,
            @Query("lng2") String lng2
    );

}
