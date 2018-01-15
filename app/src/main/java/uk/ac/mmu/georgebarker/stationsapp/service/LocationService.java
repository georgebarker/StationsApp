package uk.ac.mmu.georgebarker.stationsapp.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import java.util.List;

import uk.ac.mmu.georgebarker.stationsapp.listener.StationsLocationListener;

import static android.content.Context.LOCATION_SERVICE;

public class LocationService {

    private static final int CHECK_FOR_UPDATE_DISTANCE = 10; // (m)
    private static final int CHECK_FOR_UPDATE_TIME = 1000; // milliseconds

    private Context context;
    private LocationManager locationManager;

    @SuppressLint("MissingPermission")
    public LocationService(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        //this could go in constructor - not sure.
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, CHECK_FOR_UPDATE_DISTANCE,
                CHECK_FOR_UPDATE_TIME, new StationsLocationListener());
    }


    @SuppressLint("MissingPermission")
    public Location getLocation() {
        if (locationManager != null) {
            if (arePermissionsGranted()) {

                List<String> providers = locationManager.getProviders(true);

                for (String provider : providers) {
                    Location location = locationManager.getLastKnownLocation(provider);
                    if (location != null) {
                        return location;
                    }
                }
            } else {
                requestLocationPermissions((Activity) context);
            }

        }
        return null;
    }

    private boolean arePermissionsGranted() {
        return context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestLocationPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                1);
    }

}


