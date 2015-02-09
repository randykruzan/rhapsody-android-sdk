package com.rhapsody.cedar.sample.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rhapsody.cedar.sample.R;
import com.rhapsody.cedar.sample.library.metadata.Station;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StationAdapter extends BaseAdapter{

    private List<Station> stations = new ArrayList<Station>();
    private LayoutInflater inflater;
    private Context context;

    public StationAdapter(Context context, List<Station> stations){
        this.stations.addAll(stations);
        this.context = context;
        if(context != null) {
            inflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        return stations.size();
    }

    @Override
    public Station getItem(int pos) {
        return stations.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        Station station = getItem(pos);
        view = inflater.inflate(R.layout.list_item_simple_image, null, false);

        TextView trackTv = (TextView) view.findViewById(R.id.title);
        trackTv.setText(station.name);

        TextView artistTv = (TextView) view.findViewById(R.id.subtitle);
        artistTv.setText(station.summary);

        ImageView image = (ImageView) view.findViewById(R.id.image);
        Picasso.with(context).load(station.imageUrls.large).fit().centerCrop().into(image);
        return view;
    }

}
