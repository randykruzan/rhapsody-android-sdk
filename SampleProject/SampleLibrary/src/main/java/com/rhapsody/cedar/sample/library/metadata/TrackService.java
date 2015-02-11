package com.rhapsody.cedar.sample.library.metadata;

import com.rhapsody.cedar.player.data.Track;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;

import static com.rhapsody.cedar.sample.library.metadata.Constants.APIKEY;
import static com.rhapsody.cedar.sample.library.metadata.Constants.AUTHORIZATION;

public interface TrackService {

    @GET("/v1/tracks/top")
	public void getTopTracks(@Query(Constants.APIKEY) String apikey, @Query(Constants.LIMIT) int limit, @Query(Constants.OFFSET) int offset, Callback<List<Track>> callback);

    @GET("/v1/me/listens")
    public void getListeningHistory(@Query(APIKEY) String apikey, @Header(AUTHORIZATION) String authorization, Callback<List<Track>> callback);

}
