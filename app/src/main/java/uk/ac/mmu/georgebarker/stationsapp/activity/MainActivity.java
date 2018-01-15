package uk.ac.mmu.georgebarker.stationsapp.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.mmu.georgebarker.stationsapp.R;
import uk.ac.mmu.georgebarker.stationsapp.adapter.StationAdapter;
import uk.ac.mmu.georgebarker.stationsapp.listener.UpdateButtonClickedListener;
import uk.ac.mmu.georgebarker.stationsapp.model.Station;
import uk.ac.mmu.georgebarker.stationsapp.service.LocationService;
import uk.ac.mmu.georgebarker.stationsapp.service.NetworkService;

public class MainActivity extends AppCompatActivity {

    ListView stationsListView;
    FloatingActionButton updateButton;
    Context context;
    NetworkService networkService;
    LocationService locationService;
    List<Station> stations = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        stationsListView = findViewById(R.id.stations_list);
        updateButton = findViewById(R.id.update_fab);

        LocationService.requestLocationPermissions(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (isPermissionGranted(grantResults)) {
            locationService = new LocationService(context);
            networkService = new NetworkService();

            StationAdapter adapter = new StationAdapter(stations, context);
            stationsListView.setAdapter(adapter);

            Location location = locationService.getLocation();
            networkService.updateStations(adapter, location);

            updateButton.setOnClickListener(new UpdateButtonClickedListener(locationService, networkService, adapter));
        } else {
            updateButton.setOnClickListener(new UpdateButtonClickedListener(context));
            // hide list view, show error messages
        }

    }

    private boolean isPermissionGranted(int[] grantResults) {
        return grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED;
    }
}
