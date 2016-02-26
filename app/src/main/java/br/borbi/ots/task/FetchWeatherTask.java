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
import br.borbi.ots.enums.WeatherForecastSourcePriority;
import br.borbi.ots.enums.WeatherType;
import br.borbi.ots.integration.DeveloperForecastIntegration;
import br.borbi.ots.integration.OpenWeatherIntegration;
import br.borbi.ots.pojo.City;
import br.borbi.ots.pojo.CityResultSearch;
import br.borbi.ots.pojo.DayForecast;
import br.borbi.ots.pojo.SearchParameters;


/**
 * Created by Gabriela on 26/05/2015.
 */
public class FetchWeatherTask extends AsyncTask<SearchParameters, Void, List<CityResultSearch>> {

    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    private static final String[] WEATHER_FORECAST_RESOURCES = {"OpenWeather", "DeveloperForecast"};


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
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected List<CityResultSearch> doInBackground(SearchParameters... params) {
        if (params.length == 0) {
            Log.i(LOG_TAG,"params sao vazios");
            return null;
        }

        SearchParameters searchParameters = params[0];

        List<CityResultSearch> cities = new ArrayList<CityResultSearch>();
        List<City> citiesToSearch = searchParameters.getCities();

        CityResultSearch cityResultSearchAux = null;

        Iterator<City> it = citiesToSearch.iterator();

        boolean searchOpenWeatherFirst = true;

        WeatherForecastSourcePriority weatherForecastSource = WeatherForecastSourcePriority.getFirstWeatherForecastSource();
        int lastWeatherForecastSource = weatherForecastSource.ordinal();

        int numberOfDays = searchParameters.getNumberOfDays();

        while((it.hasNext())) {

            if (isCancelled()) break;

            City cityToSearch = (City) it.next();

            //cityResultSearchAux = searchDeveloperForecastData(cityToSearch, searchParameters.getNumberOfDays());
            if (weatherForecastSource.isOpenWeather()) {
                cityResultSearchAux = searchOpenWeatherData(cityToSearch, numberOfDays);

                if (cityResultSearchAux == null) {
                    weatherForecastSource = WeatherForecastSourcePriority.getSource(lastWeatherForecastSource++);
                }
            }

            if (weatherForecastSource.isDeveloperForecast()) {
                cityResultSearchAux = searchDeveloperForecastData(cityToSearch, searchParameters.getNumberOfDays());

                if (cityResultSearchAux == null) {
                    weatherForecastSource = WeatherForecastSourcePriority.getSource(lastWeatherForecastSource++);
                }
            }

//            if(searchOpenWeatherFirst) {
//                cityResultSearchAux = searchOpenWeatherData(cityToSearch, numberOfDays);
//
//                if (cityResultSearchAux == null) {
//                    cityResultSearchAux = searchDeveloperForecastData(cityToSearch, searchParameters.getNumberOfDays());
//                    searchOpenWeatherFirst = false;
//                }
//            }else{
//                cityResultSearchAux = searchDeveloperForecastData(cityToSearch, searchParameters.getNumberOfDays());
//                if (cityResultSearchAux == null) {
//                    cityResultSearchAux = searchOpenWeatherData(cityToSearch, searchParameters.getNumberOfDays());
//                    searchOpenWeatherFirst = true;
//                }
//            }

            if(cityResultSearchAux!=null){
                cityResultSearchAux.setWeatherForecastSourceUsed(weatherForecastSource);
                cities.add(cityResultSearchAux);
            }
        }

        return cities;
    }

    private CityResultSearch searchOpenWeatherData(City city, int numberOfDays){
        return OpenWeatherIntegration.searchWeatherData(city,numberOfDays);
    }

    private CityResultSearch searchDeveloperForecastData(City city, int numberOfDays){
        return DeveloperForecastIntegration.searchWeatherData(city,numberOfDays);
    }

    @Override
    protected void onPostExecute(List<CityResultSearch> cities) {
        super.onPostExecute(cities);
        this.taskFinishedListener.OnTaskFinished(cities);
    }
}
