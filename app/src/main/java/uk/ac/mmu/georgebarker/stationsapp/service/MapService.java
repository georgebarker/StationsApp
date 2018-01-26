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


public class MapService implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Location currentLocation;
    private List<Marker> markers = new ArrayList<>();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @SuppressLint("MissingPermission")
    public void updateMap(List<Station> stations, Location currentLocation) {
        this.currentLocation = currentLocation;
        googleMap.clear();
        markers.clear();
        plotMarkers(stations);
        moveMapToMarkers();
        googleMap.setMyLocationEnabled(true);

    }

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
