package com.rhapsody.cedar.sample.metadata;

import static com.rhapsody.cedar.sample.Constants.API_KEY;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;

import com.rhapsody.cedar.player.data.Track;
import com.rhapsody.cedar.sample.Constants;

public class TrackMetadata {

	TrackService trackService;
	
	public TrackMetadata(){
		RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.ENDPOINT_HTTP).build();
		trackService = adapter.create(TrackService.class);
	}
	
	public void getTopTracks(int limit, int offset, Callback<List<Track>> callback) {
		trackService.getTopTracks(API_KEY, limit, offset, callback);
	}

	public void getTrackDetail(String trackId, Callback<Track> callback) {
		trackService.getTrackDetail(API_KEY, trackId, callback);
	}
	
}
