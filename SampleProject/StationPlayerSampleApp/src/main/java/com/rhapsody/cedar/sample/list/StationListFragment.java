package com.rhapsody.cedar.sample.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.rhapsody.cedar.sample.player.StationPlayerActivity;
import com.rhapsody.cedar.sample.StationPlayerSampleApplication;
import com.rhapsody.cedar.sample.library.metadata.Metadata;
import com.rhapsody.cedar.sample.library.metadata.Station;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class StationListFragment extends ListFragment implements Callback<List<Station>>  {

    StationAdapter adapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StationPlayerSampleApplication app = (StationPlayerSampleApplication) getActivity().getApplication();
        new Metadata(app.getAppInfo().getApiKey()).getTopStations(10, 0, this);
    }

    @Override
    public void success(List<Station> stations, Response response) {
        adapter = new StationAdapter(getActivity(), stations);
        setListAdapter(adapter);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        Toast.makeText(getActivity(), "Sorry", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Station station = adapter.getItem(position);
        Intent i = new Intent(getActivity(), StationPlayerActivity.class);
        i.putExtra(StationPlayerActivity.EXTRA_STATION, station);
        startActivity(i);
    }

}
