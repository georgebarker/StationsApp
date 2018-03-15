package uk.ac.mmu.georgebarker.stationsapp.service;

import android.annotation.SuppressLint;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import uk.ac.mmu.georgebarker.stationsapp.model.Station;

/**
 * I am a class that provides services to update the map.
 */
public class MapService implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Location currentLocation;
    private List<Marker> markers = new ArrayList<>();

    /**
     * I am the implementation of onMapReady, I receive a GoogleMap and set this property.
     * @param googleMap the google map that is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    /**
     * I update the map with the latest stations and the devices current location.
     * @param stations The updated list of stations
     * @param currentLocation The devices current location
     */
    @SuppressLint("MissingPermission")
    public void updateMap(List<Station> stations, Location currentLocation) {
        this.currentLocation = currentLocation;
        googleMap.clear();
        markers.clear();
        plotMarkers(stations);
        moveMapToMarkers();
        googleMap.setMyLocationEnabled(true);

    }

    /**
     * I plot the markers of the train stations onto the map.
     * @param stations the list of stations to plot.
     */
    private void plotMarkers(List<Station> stations) {
        for (Station station : stations) {
            LatLng stationLatLng = new LatLng(station.getLatitude(), station.getLongitude());
            Marker marker = googleMap.addMarker(new MarkerOptions().position(stationLatLng)
                    .title(station.getStationName()));
            markers.add(marker);
        }

        //This is done so that the current location is taken into account when moving the map
        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        Marker currentLocationMarker = googleMap.addMarker(new MarkerOptions().position(currentLatLng));
        currentLocationMarker.setVisible(false);
    }

    /**
     * I move the map to nicely fit the markers that are plotted on it.
     */
    private void moveMapToMarkers() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }

        LatLngBounds bounds = builder.build();

        int padding = 100;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cameraUpdate);

    }
}
