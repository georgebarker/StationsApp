package uk.ac.mmu.georgebarker.stationsapp.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;

import uk.ac.mmu.georgebarker.stationsapp.R;
import uk.ac.mmu.georgebarker.stationsapp.adapter.StationAdapter;
import uk.ac.mmu.georgebarker.stationsapp.listener.UpdateButtonClickedListener;
import uk.ac.mmu.georgebarker.stationsapp.model.Station;
import uk.ac.mmu.georgebarker.stationsapp.service.LocationService;
import uk.ac.mmu.georgebarker.stationsapp.service.NetworkService;
import uk.ac.mmu.georgebarker.stationsapp.service.MapService;

public class MainActivity extends AppCompatActivity {

    private ListView stationsListView;
    private FloatingActionButton updateButton;
    private SupportMapFragment map;
    private Context context;
    private NetworkService networkService;
    private LocationService locationService;
    private RelativeLayout errorLayout;
    private LinearLayout mainLayout;
    private RelativeLayout loadingLayout;
    private List<Station> stations = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        mainLayout = findViewById(R.id.main_layout);
        errorLayout = findViewById(R.id.error_layout);
        loadingLayout = findViewById(R.id.loading_layout);

        stationsListView = findViewById(R.id.stations_list);
        updateButton = findViewById(R.id.update_fab);
        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

//        showMainLayout(false); need to find where this should go so the map can be set up nicely.

        LocationService.requestLocationPermissions(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (isPermissionGranted(grantResults)) {
            locationService = new LocationService(context);
            networkService = new NetworkService(this);

            StationAdapter adapter = new StationAdapter(stations, context);
            stationsListView.setAdapter(adapter);

            Location location = locationService.getLocation();

            MapService mapService = new MapService();
            map.getMapAsync(mapService);

            networkService.updateStations(adapter, location, mapService);



            updateButton.setOnClickListener(new UpdateButtonClickedListener(locationService, networkService, adapter, mapService));
        } else {
            updateButton.setOnClickListener(new UpdateButtonClickedListener(this));
            showErrorMessages();
        }

    }

    private boolean isPermissionGranted(int[] grantResults) {
        return grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED;
    }

    public void showMainLayout(boolean isVisible) {
        if (isVisible) {
            mainLayout.setVisibility(LinearLayout.VISIBLE);
            loadingLayout.setVisibility(RelativeLayout.GONE);
            errorLayout.setVisibility(RelativeLayout.GONE);
        } else {
            mainLayout.setVisibility(LinearLayout.GONE);
        }
    }

    public void showErrorMessages() {
        mainLayout.setVisibility(LinearLayout.GONE);
        loadingLayout.setVisibility(RelativeLayout.GONE);
        errorLayout.setVisibility(RelativeLayout.VISIBLE);
    }
}
