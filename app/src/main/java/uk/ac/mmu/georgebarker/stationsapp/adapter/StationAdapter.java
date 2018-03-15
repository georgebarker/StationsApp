package uk.ac.mmu.georgebarker.stationsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import uk.ac.mmu.georgebarker.stationsapp.R;
import uk.ac.mmu.georgebarker.stationsapp.model.Station;

/**
 * I am an adapter class that allows a custom view to be used for the Station model
 * when displaying it in a listView.
 */
public class StationAdapter extends ArrayAdapter<Station> {
    private Context context;
    private List<Station> stations;

    /**
     * I create a StationAdapter using the custom station_item layout.
     * @param stations first set of stations
     * @param context application context
     */
    public StationAdapter(List<Station> stations, Context context) {
        super(context, R.layout.station_item, stations);
        this.stations = stations;
        this.context = context;

    }

    /**
     * I am the implementation of getView, that populates individual rows in the list view.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
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
        String distance = getDistanceFromLocationString(station.getDistanceFromCurrentLocation());
        distanceFromCurrentLocation.setText(distance);


        return convertView;
    }

    /**
     * I am used to update the stations that are visible in the list
     * @param stations list of stations to update with
     */
    public void updateStations(List<Station> stations) {
        this.stations.clear();
        this.stations.addAll(stations);
        notifyDataSetChanged();
    }

    /**
     * I format the distance away from a location into a readable string, picking metres or kilometres as appropriate.
     * @param distance
     * @return
     */
    private String getDistanceFromLocationString(double distance) {
        int ONE_KILOMETER = 1000;
        String KILOMETER_FORMAT = "%.2f km away";
        String METER_FORMAT = "%.2f m away";
        if (distance >= ONE_KILOMETER) {
            return String.format(Locale.UK, KILOMETER_FORMAT,(distance / ONE_KILOMETER));
        } else {
            return String.format(Locale.UK, METER_FORMAT, (distance));
        }
    }
}