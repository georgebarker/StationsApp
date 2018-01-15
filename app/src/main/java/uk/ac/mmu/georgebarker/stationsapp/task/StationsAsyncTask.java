package uk.ac.mmu.georgebarker.stationsapp.task;

import android.location.Location;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import uk.ac.mmu.georgebarker.stationsapp.adapter.StationAdapter;
import uk.ac.mmu.georgebarker.stationsapp.model.Station;

public class StationsAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final int HTTP_OK = 200;

    private String urlString;
    private Location location;
    private StationAdapter stationAdapter;
    private String jsonString;

    public StationsAsyncTask(String urlString, Location location, StationAdapter stationAdapter) {
        this.urlString = urlString;
        this.location = location;
        this.stationAdapter = stationAdapter;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();

            if (responseCode == HTTP_OK) {
                jsonString = new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        JSONArray jsonStations = null;
        try {
            jsonStations = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            //jsonString is fucked, set error messages, return.
        }
        List<Station> stations = mapJsonToStations(jsonStations);
        stationAdapter.updateStations(stations);
    }

    private List<Station> mapJsonToStations(JSONArray jsonArray) {
        List<Station> stations = new ArrayList<>();
        for (int index = 0; index < jsonArray.length(); index++) {
            Station station = new Station();
            JSONObject jsonStation;
            try {
                jsonStation = jsonArray.getJSONObject(index);
                station.setStationName(jsonStation.getString("StationName"));

                double latitude = jsonStation.getDouble("Latitude");
                double longitude = jsonStation.getDouble("Longitude");

                double distance = getDistanceToStationFromCurrentLocation(latitude, longitude);
                station.setDistanceFromCurrentLocation(distance);

                stations.add(station);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return stations;
    }

    private double getDistanceToStationFromCurrentLocation(double latitude, double longitude) {
        float[] floats = new float[1];
        Location.distanceBetween(location.getLatitude(), location.getLongitude(), latitude, longitude, floats);
        return floats[0];
    }
}