package uk.ac.mmu.georgebarker.stationsapp.task;

import android.location.Location;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import uk.ac.mmu.georgebarker.stationsapp.activity.MainActivity;
import uk.ac.mmu.georgebarker.stationsapp.adapter.StationAdapter;
import uk.ac.mmu.georgebarker.stationsapp.model.Station;
import uk.ac.mmu.georgebarker.stationsapp.service.MapService;

public class StationsAsyncTask extends AsyncTask<Void, Void, String> {

    private static final int FIVE_SECONDS_IN_MILLIS = 5 * 1000;

    private String urlString;
    private Location location;
    private StationAdapter stationAdapter;
    private MapService mapService;
    private MainActivity activity;

    public StationsAsyncTask(String urlString, Location location, StationAdapter stationAdapter, MapService mapService, MainActivity activity) {
        this.urlString = urlString;
        this.location = location;
        this.stationAdapter = stationAdapter;
        this.mapService = mapService;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(Void... aVoid) {
        String jsonString = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(FIVE_SECONDS_IN_MILLIS);
            connection.connect();
            InputStream stream = connection.getInputStream();
            jsonString = new Scanner(stream, "UTF-8").useDelimiter("\\A").next();
        } catch (Exception e) {
            //jsonString will be null, and is handled in onPostExecute.
        }
        return jsonString;
    }

    @Override
    protected void onPostExecute(String jsonString) {
        JSONArray jsonStations;
        List<Station> stations;
        try {
            if (jsonString == null) throw new Exception();
            jsonStations = new JSONArray(jsonString);
            stations = mapJsonToStations(jsonStations);
        } catch (Exception e) {
            //Something went wrong, display error messages.
            activity.showMainLayout(false);
            activity.showErrorMessages();
            return;
        }

        activity.showMainLayout(true);
        stationAdapter.updateStations(stations);
        mapService.updateMap(stations, location);
    }

    private List<Station> mapJsonToStations(JSONArray jsonArray) throws Exception {
        List<Station> stations = new ArrayList<>();
        for (int index = 0; index < jsonArray.length(); index++) {
            Station station = new Station();
            JSONObject jsonStation;
                jsonStation = jsonArray.getJSONObject(index);
                station.setStationName(jsonStation.getString("StationName"));

                double latitude = jsonStation.getDouble("Latitude");
                double longitude = jsonStation.getDouble("Longitude");

                station.setLatitude(latitude);
                station.setLongitude(longitude);

                double distance = getDistanceToStationFromCurrentLocation(latitude, longitude);
                station.setDistanceFromCurrentLocation(distance);

                stations.add(station);
        }

        return stations;
    }

    private double getDistanceToStationFromCurrentLocation(double latitude, double longitude) {
        float[] floats = new float[1];
        Location.distanceBetween(location.getLatitude(), location.getLongitude(), latitude, longitude, floats);
        return floats[0];
    }
}