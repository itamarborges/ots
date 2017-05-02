package br.borbi.ots.integration;

import android.content.Context;
import android.net.Uri;
import android.text.format.Time;
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
import java.util.LinkedList;

import br.borbi.ots.credentials.Credentials;
import br.borbi.ots.enums.WeatherType;
import br.borbi.ots.pojo.City;
import br.borbi.ots.pojo.CityResultSearch;
import br.borbi.ots.pojo.DayForecast;

/**
 * Created by Gabriela on 12/01/2016.
 */
public class OpenWeatherIntegration {

    private static final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
    private static final String APPID_PARAM = "APPID";
    private static final String QUERY_PARAM = "q";
    private static final String FORMAT_PARAM = "mode";
    private static final String UNITS_PARAM = "units";
    private static final String DAYS_PARAM = "cnt";
    private static final String FORMAT = "json";
    private static final String UNITS = "metric";
    private static final String LOG_TAG = OpenWeatherIntegration.class.getSimpleName();

    public static CityResultSearch searchWeatherData(City city, int numberOfDays) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        CityResultSearch cityResultSearch = null;

        try {
            String appKey = Credentials.getOpenWeather();
            String cityName = city.getNameEnglish();
            int indexComma = cityName.indexOf(",");
            if(indexComma > 0) {
                cityName = cityName.substring(0, indexComma);
            }
            cityName =  cityName + "," + city.getCountryCode();

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    //.appendQueryParameter(QUERY_PARAM, city.getNameEnglish() + "," + city.getCountryCode())
                    .appendQueryParameter(QUERY_PARAM, cityName)
                    .appendQueryParameter(FORMAT_PARAM, FORMAT)
                    .appendQueryParameter(UNITS_PARAM, UNITS)
                    .appendQueryParameter(APPID_PARAM, appKey)
                    .appendQueryParameter(DAYS_PARAM, String.valueOf(numberOfDays))
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() != 0) {
                    forecastJsonStr = buffer.toString();
                    cityResultSearch = getWeatherDataFromJson(forecastJsonStr, city);
                }
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
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

        // These are the names of the JSON objects that need to be extracted.

        // Location information
        final String OWM_CITY = "city";

        // Weather information.  Each day's forecast info is an element of the "list" array.
        final String OWM_LIST = "list";

        // All temperatures are children of the "temp" object.
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_TEMPERATURE_MORNING = "morn";
        final String OWM_TEMPERATURE_EVENING = "eve";
        final String OWM_TEMPERATURE_NIGHT = "night";

        final String OWM_WEATHER = "weather";
        final String OWM_DESCRIPTION = "main";
        final String OWM_WEATHER_ID = "id";

        final String OWM_PRECIPITATION = "rain";
        final String OWM_HUMIDITY = "humidity";

        LinkedList<DayForecast> daysForecast = new LinkedList<>();
        CityResultSearch cityResultSearch = null;

        try {
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            if (!forecastJson.has(OWM_LIST)) {
                //TODO tratar cidade nao encontrada
                //Log.v(LOG_TAG,"cidade " + citySearched.getNameEnglish() + " nao encontrada");
            } else {
                JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

                // OWM returns daily forecasts based upon the local time of the city that is being
                // asked for, which means that we need to know the GMT offset to translate this data
                // properly.

                // Since this data is also sent in-order and the first day is always the
                // current day, we're going to take advantage of that to get a nice
                // normalized UTC date for all of our weather.

                Time dayTime = new Time();
                dayTime.setToNow();

                // we start at the day returned by local time. Otherwise this is a mess.
                int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

                // now we work exclusively in UTC
                dayTime = new Time();

                for (int i = 0; i < weatherArray.length(); i++) {

                    // Get the JSON object representing the day
                    JSONObject dayForecast = weatherArray.getJSONObject(i);

                    // Cheating to convert this to UTC time, which is what we want anyhow
                    long dateTime = dayTime.setJulianDay(julianStartDay + i);

                    // Description is in a child array called "weather", which is 1 element long.
                    // That element also contains a weather code.
                    JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                    String description = weatherObject.getString(OWM_DESCRIPTION);
                    int weatherId = weatherObject.getInt(OWM_WEATHER_ID);

                    // Temperatures are in a child object called "temp".  Try not to name variables
                    // "temp" when working with temperature.  It confuses everybody.
                    JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                    Double high = temperatureObject.getDouble(OWM_MAX);
                    Double low = temperatureObject.getDouble(OWM_MIN);
                    Double morningTemperature = temperatureObject.getDouble(OWM_TEMPERATURE_MORNING);
                    Double eveningTemperature = temperatureObject.getDouble(OWM_TEMPERATURE_EVENING);
                    Double nightTemperature = temperatureObject.getDouble(OWM_TEMPERATURE_NIGHT);

                    Double humidity = dayForecast.getDouble(OWM_HUMIDITY);
                    Double precipitation = 0.0;
                    if (dayForecast.has(OWM_PRECIPITATION)) {
                        precipitation = dayForecast.getDouble(OWM_PRECIPITATION);
                    }

                    DayForecast forecastForTheDay = new DayForecast(new Date(dateTime), low, high, morningTemperature, eveningTemperature, nightTemperature, WeatherType.getWeatherType(weatherId), precipitation, humidity);
                    daysForecast.add(forecastForTheDay);
                }
                cityResultSearch = new CityResultSearch(citySearched, daysForecast);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return cityResultSearch;
    }
}
