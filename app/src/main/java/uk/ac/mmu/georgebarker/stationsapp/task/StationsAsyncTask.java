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

/**
 * I am a class that retrieves the five nearest train stations using an AsyncTask.
 */
public class StationsAsyncTask extends AsyncTask<Void, Void, String> {

    private static final int FIVE_SECONDS_IN_MILLIS = 5 * 1000;

    private String urlString;
    private Location location;
    private StationAdapter stationAdapter;
    private MapService mapService;
    private MainActivity activity;

    /**
     * I create the AsyncTask used to retrieve the five nearest stations.
     * @param urlString The formed URL that will be used to request the 5 nearest stations.
     * @param location The devices current location.
     * @param stationAdapter The adapter of the listView that is populated with the stations.
     * @param mapService The MapService that will populate the map with the stations.
     * @param activity The parent activity.
     */
    public StationsAsyncTask(String urlString, Location location, StationAdapter stationAdapter,
                             MapService mapService, MainActivity activity) {
        this.urlString = urlString;
        this.location = location;
        this.stationAdapter = stationAdapter;
        this.mapService = mapService;
        this.activity = activity;
    }

    /**
     * I am the doInBackground implementation; I go off and get the five nearest stations from the
     * web service as a string of JSON.
     * @param aVoid
     * @return
     */
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

    /**
     * I am the onPostExecute implementation; I recieve the jsonString that we got from the server,
     * validate that it is correct, turn it into some Station objects, and update the UI with them.
     * @param jsonString The string of JSON that came from the server
     */
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

    /**
     * @param jsonArray The JSON from the server as a generic JSON Array
     * @return Some nice Station objects that were converted from the generic JSONArray
     * @throws Exception thrown if something went wrong converting from JSONArray > Station.
     */
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

    /**
     * I use the devices current location to calculate the distance between a provided latitude and longitude.
     * @param latitude provided latitude of a station
     * @param longitude provided longitude of a station
     * @return
     */
    private double getDistanceToStationFromCurrentLocation(double latitude, double longitude) {
        float[] floats = new float[1];
        Location.distanceBetween(location.getLatitude(), location.getLongitude(), latitude, longitude, floats);
        return floats[0];
    }
}