package br.borbi.ots.task;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import br.borbi.ots.SearchActivity;
import br.borbi.ots.enums.WeatherType;
import br.borbi.ots.pojo.City;
import br.borbi.ots.pojo.DayForecast;
import br.borbi.ots.pojo.SearchParameters;


/**
 * Created by Gabriela on 26/05/2015.
 */
public class FetchWeatherTask extends AsyncTask<SearchParameters, Void, List<City>> {

    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    private final Context mContext;

    private SearchActivity.TaskFinishedListener taskFinishedListener;

    public FetchWeatherTask(Context context) {
        mContext = context;
    }

    public FetchWeatherTask(Context context,SearchActivity.TaskFinishedListener taskFinishedListener) {
        mContext = context;
        this.taskFinishedListener = taskFinishedListener;
    }

    @Override
    protected List<City> doInBackground(SearchParameters... params) {

        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            Log.i(LOG_TAG,"params sao vazios");
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        String format = "json";
        String units = "metric";

        SearchParameters searchParameters = params[0];

        List<City> cities = new ArrayList<City>();
        List<City> citiesToSearch = searchParameters.getCities();

        try {
            final String FORECAST_BASE_URL ="http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String QUERY_PARAM = "q";
            final String FORMAT_PARAM = "mode";
            final String UNITS_PARAM = "units";
            final String DAYS_PARAM = "cnt";
            final String APPID_KEY = "5ed032e071b3b0d20ae075ff8f8c7215";
            final String APPID_PARAM = "APPID";

            Iterator<City> it = citiesToSearch.iterator();
            while(it.hasNext()){
                City cityToSearch = (City) it.next();

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, cityToSearch.getName()+"," + cityToSearch.getCountryCode())
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(APPID_PARAM, APPID_KEY)
                        .appendQueryParameter(DAYS_PARAM, String.valueOf(searchParameters.getNumberOfDays()))
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v("123", url.toString());

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
                        Log.v(LOG_TAG,forecastJsonStr);
                        cities.add(getWeatherDataFromJson(forecastJsonStr, cityToSearch));
                    }
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
        return cities;
    }

    private City getWeatherDataFromJson(String forecastJsonStr, City citySearched)
            throws JSONException {

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

        LinkedList<DayForecast> daysForecast = new LinkedList<DayForecast>();
        City city = null;

        try {
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            JSONObject cityJson = forecastJson.getJSONObject(OWM_CITY);

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
                Double precipitation = null;
                if(dayForecast.has(OWM_PRECIPITATION)) {
                    precipitation = dayForecast.getDouble(OWM_PRECIPITATION);
                }

                DayForecast forecastForTheDay = new DayForecast(new Date(dateTime),low,high,morningTemperature,eveningTemperature,nightTemperature, WeatherType.getWeatherType(weatherId),precipitation,humidity);
                daysForecast.add(forecastForTheDay);
            }

            city = new City(citySearched.getId(), citySearched.getName(), citySearched.getCountryCode(),daysForecast);
            Log.v(LOG_TAG,city.toString());

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return city;
    }


    @Override
    protected void onPostExecute(List<City> cities) {
        super.onPostExecute(cities);
        this.taskFinishedListener.OnTaskFinished(cities);
    }
}
