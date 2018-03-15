package uk.ac.mmu.georgebarker.stationsapp.listener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * I am a class that has been left intentionally blank to make code cleaner in other classes -
 * I am used to start listening to the devices location.
 */
public class StationsLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
