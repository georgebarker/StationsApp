package uk.ac.mmu.georgebarker.stationsapp.service;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import uk.ac.mmu.georgebarker.stationsapp.R;
import uk.ac.mmu.georgebarker.stationsapp.activity.MainActivity;
import uk.ac.mmu.georgebarker.stationsapp.adapter.StationAdapter;
import uk.ac.mmu.georgebarker.stationsapp.task.StationsAsyncTask;

public class NetworkService {

    private MainActivity activity;
    public NetworkService(MainActivity activity) {
        this.activity = activity;
    }

    public void updateStations(StationAdapter stationAdapter, Location location, MapService mapService) {
        String url = formRequestUrl(location);
        new StationsAsyncTask(url, location, stationAdapter, mapService, activity).execute();
    }

    private String formRequestUrl(Location location) {
        String apiLocation = activity.getString(R.string.api_location);
        return  apiLocation
                + "/stations?lat="
                + location.getLatitude()
                + "&lng=" + location.getLongitude();
    }


}
