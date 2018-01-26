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

    public UpdateButtonClickedListener(MainActivity activity) {
        this.activity = activity;
        locationService = null;
        networkService = null;
    }

    public UpdateButtonClickedListener(LocationService locationService, NetworkService networkService, StationAdapter stationAdapter, MapService mapService) {
        this.locationService = locationService;
        this.networkService = networkService;
        this.stationAdapter = stationAdapter;
        this.mapService = mapService;
    }

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
