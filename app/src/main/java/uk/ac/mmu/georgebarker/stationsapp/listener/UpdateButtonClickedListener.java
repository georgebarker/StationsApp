package uk.ac.mmu.georgebarker.stationsapp.listener;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.view.View;

import uk.ac.mmu.georgebarker.stationsapp.activity.MainActivity;
import uk.ac.mmu.georgebarker.stationsapp.adapter.StationAdapter;
import uk.ac.mmu.georgebarker.stationsapp.service.LocationService;
import uk.ac.mmu.georgebarker.stationsapp.service.NetworkService;
import uk.ac.mmu.georgebarker.stationsapp.service.MapService;

public class UpdateButtonClickedListener implements View.OnClickListener {

    private LocationService locationService;
    private NetworkService networkService;
    private MainActivity activity;
    private StationAdapter stationAdapter;
    private MapService mapService;

    /**
     * I am the constructor used when the application has no permissions
     * to set up any of the services that it requires.
     * @param activity
     */
    public UpdateButtonClickedListener(MainActivity activity) {
        this.activity = activity;
        locationService = null;
        networkService = null;
    }

    /**
     * I am the constructor used when the application has all permissions necessary.
     * @param locationService
     * @param networkService
     * @param stationAdapter
     * @param mapService
     */
    public UpdateButtonClickedListener(LocationService locationService, NetworkService networkService, StationAdapter stationAdapter, MapService mapService) {
        this.locationService = locationService;
        this.networkService = networkService;
        this.stationAdapter = stationAdapter;
        this.mapService = mapService;
    }

    /**
     * I am used when the update button is clicked - if the services it null,
     * it means we do not have permissions, so we must request them.
     * Otherwise, we will get our location and update our app with the new stations.
     * @param v
     */
    @Override
    public void onClick(View v) {
        //if services are null, request permissions.
        if (locationService != null && networkService != null) {
            Location location = locationService.getLocation();
            networkService.updateStations(stationAdapter, location, mapService);
        } else {
            LocationService.requestLocationPermissions(activity);
        }

    }
}
