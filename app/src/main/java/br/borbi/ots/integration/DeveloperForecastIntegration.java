package br.borbi.ots.integration;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import br.borbi.ots.R;
import br.borbi.ots.enums.WeatherType;
import br.borbi.ots.pojo.City;
import br.borbi.ots.pojo.CityResultSearch;
import br.borbi.ots.pojo.DayForecast;
import br.borbi.ots.pojo.SearchParameters;

/**
 * Created by Gabriela on 12/01/2016.
 */
public class DeveloperForecastIntegration {

    private static final String LOG_TAG = DeveloperForecastIntegration.class.getSimpleName();

    private static final String URL = "https://api.forecast.io/forecast/";

    private static final String PARAM_EXCLUDE_FLAGS = "exclude";
    private static final String EXCLUDE_FLAGS = "currently,minutely,hourly,alerts,flags";
    private static final String PARAM_UNITS = "units";
    private static final String PARAM_UNITS_VALUE = "si";

    public static CityResultSearch searchWeatherData(City city, int numberOfDays, Context context) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String forecastJsonStr = null;
        CityResultSearch cityResultSearch = null;

        try {
            String latLong = (new StringBuilder().append(city.getLatitude()).append(",").append(city.getLongitude())).toString();

            String appKey = null;
            boolean isTest= Boolean.valueOf(context.getString(R.string.app_in_test));
            if(isTest) {
                appKey = context.getString(R.string.developerforecast_key);
            }else{
                appKey = context.getString(R.string.developerforecast_key_producao);
            }

            Uri builtUri = Uri.parse(URL).buildUpon()
                    .appendPath(appKey)
                    .appendPath(latLong)
                    .appendQueryParameter(PARAM_EXCLUDE_FLAGS, EXCLUDE_FLAGS)
                    .appendQueryParameter(PARAM_UNITS, PARAM_UNITS_VALUE)
                    .build();

            //Log.v(LOG_TAG, "url = " + builtUri.toString());

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
                    forecastJsonStr = buffer.toString();
                    //Log.v(LOG_TAG, "cidade: " + city.getNameEnglish() + ", retorno: " + forecastJsonStr);

                    cityResultSearch = getWeatherDataFromJson(forecastJsonStr, city);
                }
            } else {
                Log.i(LOG_TAG, "nao retornou nada");
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
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

        return cityResultSearch;
    }

    private static CityResultSearch getWeatherDataFromJson(String forecastJsonStr, City citySearched) {
        LinkedList<DayForecast> daysForecast = new LinkedList<DayForecast>();
        CityResultSearch cityResultSearch = null;

        try {
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            if (!forecastJson.has("latitude")) {
                //TODO tratar cidade nao encontrada
                //Log.v(LOG_TAG,"cidade " + citySearched.getNameEnglish() + " nao encontrada");
            } else {
                JSONObject daily = forecastJson.getJSONObject("daily");
                JSONArray dailyArray = daily.getJSONArray("data");
                for (int i = 0; i < dailyArray.length(); i++) {
                    // Get the JSON object representing the day
                    JSONObject dayForecast = dailyArray.getJSONObject(i);
                    long dateTime = dayForecast.getLong("time") * 1000;
                    Double high = dayForecast.getDouble("temperatureMax");
                    Double low = dayForecast.getDouble("temperatureMin");
                    Double precipitation = 0.0;
                    if(dayForecast.has("precipIntensity")) {
                        precipitation = dayForecast.getDouble("precipIntensity");
                        precipitation = precipitation * 24;
                    }
                    Double humidity = 0.0;
                    if(dayForecast.has("humidity")) {
                        humidity = dayForecast.getDouble("humidity");
                        humidity = humidity * 100;
                    }

                    DayForecast forecastForTheDay = new DayForecast(new Date(dateTime),low,high,null,null,null, WeatherType.getWeatherTypeByDeveloperForecast(dayForecast.getString("icon")),precipitation,humidity);
                    daysForecast.add(forecastForTheDay);
                }
                cityResultSearch = new CityResultSearch(citySearched,daysForecast);

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return cityResultSearch;
    }
}
