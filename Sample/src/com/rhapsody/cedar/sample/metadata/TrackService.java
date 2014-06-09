package com.rhapsody.cedar.sample.metadata;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

import com.rhapsody.cedar.player.data.Track;

public interface TrackService {

	public static final String QUERY_LIMIT = "limit";
	public static final String QUERY_OFFSET = "offset";
	public static final String PATH_TRACK_ID = "trackId";
	public static final String QUERY_APIKEY = "apikey";
	
	@GET("/v1/tracks/top")
	public void getTopTracks(@Query(QUERY_APIKEY) String apikey, @Query(QUERY_LIMIT) int limit, @Query(QUERY_OFFSET) int offset, Callback<List<Track>> callback);

	@GET("/v1/tracks/{trackId}")
	public void getTrackDetail(@Query(QUERY_APIKEY) String apikey, @Path(PATH_TRACK_ID) String trackId, Callback<Track> callback);
}
