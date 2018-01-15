package uk.ac.mmu.georgebarker.stationsapp.service;

import android.location.Location;

import uk.ac.mmu.georgebarker.stationsapp.adapter.StationAdapter;
import uk.ac.mmu.georgebarker.stationsapp.task.StationsAsyncTask;

public class NetworkService {

    public void updateStations(StationAdapter stationAdapter, Location location) {
        String url = formRequestUrl(location);
        new StationsAsyncTask(url, location, stationAdapter).execute();
    }

    private String formRequestUrl(Location location) {
        return  "http://10.0.2.2:8080/stations?lat="
                + location.getLatitude()
                + "&lng=" + location.getLongitude();
    }


}
