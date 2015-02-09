package com.rhapsody.cedar.sample.library.metadata;

import com.rhapsody.cedar.player.data.Track;
import com.rhapsody.cedar.sample.library.Constants;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;


public class Metadata {

    TrackService trackService;
    StationService stationService;
    String apiKey;

    public Metadata(String apiKey) {
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Constants.ENDPOINT_HTTP).build();
        trackService = adapter.create(TrackService.class);
        stationService = adapter.create(StationService.class);
        this.apiKey = apiKey;
    }

    public void getTopTracks(int limit, int offset, Callback<List<Track>> callback) {
        trackService.getTopTracks(apiKey, limit, offset, callback);
    }

    public void getTopStations(int limit, int offset, Callback<List<Station>> callback) {
        stationService.getTopStations(apiKey, limit, offset, callback);
    }

    public TrackService getTrackService() {
        return trackService;
    }

}
