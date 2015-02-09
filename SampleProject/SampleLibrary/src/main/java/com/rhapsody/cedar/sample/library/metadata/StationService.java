package com.rhapsody.cedar.sample.library.metadata;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

import static com.rhapsody.cedar.sample.library.metadata.Constants.*;

public interface StationService {

    @GET("/v1/stations/top")
    public void getTopStations(@Query(APIKEY) String apikey, @Query(LIMIT) int limit, @Query(OFFSET) int offset, Callback<List<Station>> callback);

}
