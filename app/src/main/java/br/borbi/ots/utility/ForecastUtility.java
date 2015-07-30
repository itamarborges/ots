package br.borbi.ots.utility;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Gabriela on 29/07/2015.
 */
public class ForecastUtility {

    private static final String LOG_TAG = ForecastUtility.class.getSimpleName();

    public static String findCurrentLocationCityName(Double latitude, Double longitude) {
        String cityName = "";

        final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
        final String LATITUDE_PARAM = "lat";
        final String LONGITUDE_PARAM = "lon";
        final String APPID_KEY = "5ed032e071b3b0d20ae075ff8f8c7215";
        final String APPID_PARAM = "APPID";

        Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(LATITUDE_PARAM, latitude.toString())
                .appendQueryParameter(LONGITUDE_PARAM, longitude.toString())
                .appendQueryParameter(APPID_PARAM, APPID_KEY)
                .build();

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;


        try {
            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() != 0) {
                    String forecastJsonStr = buffer.toString();
                    JSONObject forecastJson = new JSONObject(forecastJsonStr);
                    cityName = forecastJson.getString("name");
                }
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }


        return cityName;
    }
}
