package com.rhapsody.cedar.sample;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SimplePlayQueue {

	private static final String PREFS_CURRENT_TRACK_INDEX = "current_track";
	private static final String PREFS_TRACKS = "tracks";
	
	SharedPreferences prefs;
	Gson gson = new Gson();
	Type listOfStrings;
	int trackCount;
	
	public SimplePlayQueue(Context context){
		listOfStrings = new TypeToken<List<String>>(){}.getType();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		prefs.edit().putInt(PREFS_CURRENT_TRACK_INDEX, 0);
	}
	
	public void setTracks(ArrayList<String> tracks){
		String tracksJson = gson.toJson(tracks).toString();
		prefs.edit().putString(PREFS_TRACKS, tracksJson).commit();
		trackCount = tracks.size();
	}
	
	public String getCurrentTrack(){
		String tracksJson = prefs.getString(PREFS_TRACKS, null);
		int currentTrackIndex = prefs.getInt(PREFS_CURRENT_TRACK_INDEX, -1);
		if(tracksJson == null || currentTrackIndex < 0){
			return null;
		}
		ArrayList<String> tracks = gson.fromJson(tracksJson,listOfStrings);
		if(currentTrackIndex < tracks.size()){
			return tracks.get(currentTrackIndex);
		} else{
			return null;
		}
	} 
	
	public void setTrackIndex(int trackIndex){
		storeCurrentTrack(trackIndex);
	}
	
	public void nextTrack(){
		if(trackCount == 0){
			return;
		}
		int currentTrackIndex = loadCurrentTrack();
		currentTrackIndex = (currentTrackIndex + 1) % trackCount;
		storeCurrentTrack(currentTrackIndex);
	}
	
	public void previousTrack(){
		int currentTrackIndex = loadCurrentTrack();
		currentTrackIndex --;
		if (currentTrackIndex < 0) {
			currentTrackIndex = trackCount - 1;
		}
		storeCurrentTrack(currentTrackIndex);
	}
	
	private void storeCurrentTrack(int currentTrackIndex) {
		prefs.edit().putInt(PREFS_CURRENT_TRACK_INDEX, currentTrackIndex).commit();
	}

	private int loadCurrentTrack() {
		return prefs.getInt(PREFS_CURRENT_TRACK_INDEX, 0);
	}
}
