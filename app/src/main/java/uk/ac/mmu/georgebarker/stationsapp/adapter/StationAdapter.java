package uk.ac.mmu.georgebarker.stationsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import uk.ac.mmu.georgebarker.stationsapp.R;
import uk.ac.mmu.georgebarker.stationsapp.model.Station;

public class StationAdapter extends ArrayAdapter<Station> {
    private Context context;
    private List<Station> stations;

    public StationAdapter(List<Station> stations, Context context) {
        super(context, R.layout.station_item, stations);
        this.stations = stations;
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.station_item, null);
        }

        Station station = getItem(position);

        TextView stationName = convertView.findViewById(R.id.station_name);
        TextView distanceFromCurrentLocation = convertView.findViewById(R.id.distance);

        stationName.setText(station.getStationName());
        distanceFromCurrentLocation.setText(String.valueOf(station.getDistanceFromCurrentLocation()));


        return convertView;
    }

    public void updateStations(List<Station> stations) {
        this.stations.clear();
        this.stations.addAll(stations);
        notifyDataSetChanged();
    }
}