package uk.ac.mmu.georgebarker.stationsapp.service;

import android.location.Location;

import uk.ac.mmu.georgebarker.stationsapp.R;
import uk.ac.mmu.georgebarker.stationsapp.activity.MainActivity;
import uk.ac.mmu.georgebarker.stationsapp.adapter.StationAdapter;
import uk.ac.mmu.georgebarker.stationsapp.task.StationsAsyncTask;

/**
 * I am a class that provides services to retrieve the stations from the network.
 */
public class NetworkService {

    private MainActivity activity;

    /**
     * I create a NetworkService, taking an activity.
     * @param activity
     */
    public NetworkService(MainActivity activity) {
        this.activity = activity;
    }

    /**
     * I am called to kick off the AsyncTask that gets the five nearest train stations.
     * @param stationAdapter The adapter that will be updated with the new train stations.
     * @param location The devices current location.
     * @param mapService This is used in the AsyncTask to update the map with the new train stations.
     */
    public void updateStations(StationAdapter stationAdapter, Location location, MapService mapService) {
        String url = formRequestUrl(location);
        new StationsAsyncTask(url, location, stationAdapter, mapService, activity).execute();
    }

    /**
     * I form the the request URL using the devices current location.
     * @param location The devices current location.
     * @return
     */
    private String formRequestUrl(Location location) {
        String apiLocation = activity.getString(R.string.api_location);
        return  apiLocation
                + "/stations?lat="
                + location.getLatitude()
                + "&lng=" + location.getLongitude();
    }


}
