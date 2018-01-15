package uk.ac.mmu.georgebarker.stationsapp.listener;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.view.View;

import uk.ac.mmu.georgebarker.stationsapp.adapter.StationAdapter;
import uk.ac.mmu.georgebarker.stationsapp.service.LocationService;
import uk.ac.mmu.georgebarker.stationsapp.service.NetworkService;

public class UpdateButtonClickedListener implements View.OnClickListener {

    private LocationService locationService;
    private NetworkService networkService;
    private Context context;
    private StationAdapter stationAdapter;

    public UpdateButtonClickedListener(Context context) {
        this.context = context;
        locationService = null;
        networkService = null;
    }

    public UpdateButtonClickedListener(LocationService locationService, NetworkService networkService, StationAdapter stationAdapter) {
        this.locationService = locationService;
        this.networkService = networkService;
        this.stationAdapter = stationAdapter;
    }

    @Override
    public void onClick(View v) {
        //if services are null, request permissions.
        if (locationService != null && networkService != null) {
            Location location = locationService.getLocation();
            networkService.updateStations(stationAdapter, location);
        } else {
            LocationService.requestLocationPermissions((Activity) context);
        }

    }
}
