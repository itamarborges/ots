package br.borbi.ots.task;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.borbi.ots.SearchActivity;
import br.borbi.ots.enums.WeatherForecastSourcePriority;
import br.borbi.ots.integration.DeveloperForecastIntegration;
import br.borbi.ots.integration.OpenWeatherIntegration;
import br.borbi.ots.pojo.City;
import br.borbi.ots.pojo.CityResultSearch;
import br.borbi.ots.pojo.SearchParameters;


/**
 * Created by Gabriela on 26/05/2015.
 */
public class FetchWeatherTask extends AsyncTask<SearchParameters, Void, List<CityResultSearch>> {

    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    private final SearchActivity.TaskFinishedListener taskFinishedListener;

    public FetchWeatherTask(SearchActivity.TaskFinishedListener taskFinishedListener) {
        this.taskFinishedListener = taskFinishedListener;
    }

    @Override
    protected List<CityResultSearch> doInBackground(SearchParameters... params) {
        if (params.length == 0) {
            Log.i(LOG_TAG,"params sao vazios");
            return null;
        }

        SearchParameters searchParameters = params[0];

        List<CityResultSearch> cities = new ArrayList<>();
        List<City> citiesToSearch = searchParameters.getCities();

        CityResultSearch cityResultSearchAux = null;

        Iterator<City> it = citiesToSearch.iterator();

        WeatherForecastSourcePriority weatherForecastSource = WeatherForecastSourcePriority.getFirstWeatherForecastSource();
        int lastWeatherForecastSource = weatherForecastSource.ordinal();

        int numberOfDays = searchParameters.getNumberOfDays();

        while((it.hasNext())) {

            if (isCancelled()) break;

            City cityToSearch = it.next();

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
        return DeveloperForecastIntegration.searchWeatherData(city);
    }

    @Override
    protected void onPostExecute(List<CityResultSearch> cities) {
        super.onPostExecute(cities);
        this.taskFinishedListener.OnTaskFinished(cities);
    }
}
