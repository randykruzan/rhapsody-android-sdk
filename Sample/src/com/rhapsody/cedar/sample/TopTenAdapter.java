package com.rhapsody.cedar.sample;

import java.util.ArrayList;
import java.util.List;

import com.rhapsody.cedar.player.data.Track;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TopTenAdapter extends BaseAdapter{

	private ArrayList<Track> tracks = new ArrayList<Track>();
	private LayoutInflater inflater;
	
	public TopTenAdapter(Context context, List<Track> tracks){
		this.tracks.addAll(tracks);
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return tracks.size();
	}

	@Override
	public Track getItem(int pos) {
		return tracks.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return 0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		view = inflater.inflate(R.layout.list_item_top_ten, null, false);
		TextView trackTv = (TextView) view.findViewById(R.id.track);
		TextView artistTv = (TextView) view.findViewById(R.id.artist);
		Track track = getItem(pos);
		trackTv.setText(track.name);
		artistTv.setText(track.artist.name);
		return view;
	}

}
