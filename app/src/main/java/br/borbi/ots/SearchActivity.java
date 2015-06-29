package br.borbi.ots;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.entity.Search;
import br.borbi.ots.pojo.City;
import br.borbi.ots.pojo.Coordinates;
import br.borbi.ots.pojo.DayForecast;
import br.borbi.ots.pojo.SearchParameters;
import br.borbi.ots.utility.Utility;

interface TaskFinished {

    public void OnTaskFinished(List<City> cities);
}

public class SearchActivity extends ActionBarActivity {

    private static final String CLASS_NAME = SearchActivity.class.getName();

    public static final String CITY_LIST = "CITY_LIST";

    public static final String SEARCH = "SEARCH";

    private Context mContext;
    private ArrayList<City> mCities;

    private int minTemperature = 0;
    private int numberSunnyDays = 0;
    private boolean usesCloudyDays = false;
    private boolean dontUseTemperature = false;
    private Date dateBegin = null;
    private Date dateEnd = null;
    private Integer distance = null;
    private Double lastLatitude = 0d;
    private Double lastLongitude = 0d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mContext = this;

        AdView mAdView = null;
        Utility.initializeAd(mAdView, this);
        Intent intent = getIntent();

        if (intent != null) {
            distance = intent.getIntExtra(FiltersActivity.DISTANCE, 0);
            dateBegin = (Date) intent.getSerializableExtra(FiltersActivity.DATE_BEGIN);
            dateEnd = (Date) intent.getSerializableExtra(FiltersActivity.DATE_END);
            numberSunnyDays = intent.getIntExtra(FiltersActivity.NUMBER_SUNNY_DAYS, 0);
            minTemperature = intent.getIntExtra(FiltersActivity.MIN_TEMPERATURE, 0);
            dontUseTemperature = intent.getBooleanExtra(FiltersActivity.DONT_USE_TEMPERATURE, false);
            usesCloudyDays = intent.getBooleanExtra(FiltersActivity.USE_CLOUDY_DAYS, false);
        }

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        lastLatitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LATITUDE, Double.doubleToLongBits(0)));
        lastLongitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LONGITUDE, Double.doubleToLongBits(0)));

        lastLatitude = -30.033333;
        lastLongitude = -51.216667;

        if(lastLatitude == null || lastLongitude == null){
            Intent failureIntent = new Intent();
            failureIntent.setClass(SearchActivity.this, FailureActivity.class);
            startActivity(failureIntent);
        }


        List<City> cities = searchCities(Double.valueOf(distance), lastLatitude, lastLongitude);
        int numberOfDays = Utility.getNumberOfDaysToSearch(dateBegin, dateEnd);
        searchWeatherData(cities, numberOfDays);
    }

    /*
    Busca cidades
     */
    private List<City> searchCities(double distance, double lastLatitude, double lastLongitude) {
        Coordinates coordinates = new Coordinates(lastLatitude, lastLongitude, distance);

        StringBuffer whereClause = new StringBuffer(
                OTSContract.City.COLUMN_NAME_LATITUDE).append(" >= ?")
                .append(" AND ")
                .append(OTSContract.City.COLUMN_NAME_LATITUDE).append(" <= ?")
                .append(" AND ")
                .append(OTSContract.City.COLUMN_NAME_LONGITUDE).append(" >= ?")
                .append(" AND ")
                .append(OTSContract.City.COLUMN_NAME_LONGITUDE).append(" <= ?");


        String[] selectionArgs = new String[4];
        selectionArgs[0] = String.valueOf(coordinates.getMinLatitude());
        selectionArgs[1] = String.valueOf(coordinates.getMaxLatitude());
        selectionArgs[2] = String.valueOf(coordinates.getMinLongitude());
        selectionArgs[3] = String.valueOf(coordinates.getMaxLongitude());

        Log.i(CLASS_NAME, whereClause.toString());

        Cursor c = getContentResolver().query(
                OTSContract.CONTENT_URI_LIST_CITIES_BY_COORDINATES,
                new String[]{OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_NAME_ENGLISH, OTSContract.City.TABLE_NAME + "." + OTSContract.City._ID, OTSContract.Country.COLUMN_NAME_COUNTRY_CODE},
                whereClause.toString(),
                selectionArgs,
                null);


        List<City> cities = new ArrayList<City>();

        Log.i(CLASS_NAME, " vai iterar nos retornos");
        Log.i(CLASS_NAME, " total de retornos = " + c.getCount());
        if (c.moveToFirst()) {
            do {
                int numIndexName = c.getColumnIndex(OTSContract.City.COLUMN_NAME_NAME_ENGLISH);
                int numIndexCityId = c.getColumnIndex(OTSContract.City._ID);
                int numIndexCountryCode = c.getColumnIndex(OTSContract.Country.COLUMN_NAME_COUNTRY_CODE);

                City city = new City(c.getLong(numIndexCityId), c.getString(numIndexName), c.getString(numIndexCountryCode));

                cities.add(city);

                Log.i(CLASS_NAME, city.toString());
            }
            while (c.moveToNext());
        }
        return cities;
    }

    private void searchWeatherData(List<City> cities, int numberOfDays) {

        Log.i(CLASS_NAME, "number of days = " + numberOfDays);

        SearchParameters searchParameters = new SearchParameters(numberOfDays, cities);

        FetchWeatherTask weatherTask = new FetchWeatherTask(this, new TaskFinishedListener());
        weatherTask.execute(searchParameters);

    }

    private void validateCities(List<City> cities) {

        mCities = new ArrayList<City>();

        for (City city : cities) {

            int contSunnyDays = 0;
            boolean validCity = true;

            List<DayForecast> dayForecasts = new LinkedList<DayForecast>();

            for (DayForecast dayForecast : city.getDayForecasts()) {
                if (dayForecast.getDate().after(dateBegin)) {

                    if (!dontUseTemperature && dayForecast.getMinTemperature() < minTemperature) {
                        validCity = false;
                    }

                    if (dayForecast.getWeatherType().isSunnyDay(usesCloudyDays)) {
                        contSunnyDays++;
                    }

                    dayForecasts.add(dayForecast);
                }
            }

            if (contSunnyDays < numberSunnyDays) {
                validCity = false;
            }

            if (validCity) {
                city.setDayForecasts(dayForecasts);
                mCities.add(city);
            }
        }

        Search search = new Search();
        search.setBeginDate(dateBegin);
        search.setEndDate(dateEnd);
        search.setRadius(distance);
        search.setMinSunnyDays(numberSunnyDays);
        search.setMinTemperature(Double.valueOf(minTemperature));
        search.setDateTimeLastSearch(new Date());
        search.setOriginLatitude(lastLatitude);
        search.setOriginLongitude(lastLongitude);
        search.setCites(mCities);

        Log.i(CLASS_NAME, search.toString());


        Log.i(CLASS_NAME, "============= CIDADES VALIDAS");
        for (City city : mCities) {
            Log.i(CLASS_NAME, city.toString());
        }

        Long searchId = null;
        if (mCities.size() > 0) {
            searchId = saveSearch(search);
        }

        Intent intent = new Intent(this, ResultActivity.class);
        //TODO verificar se segue enviando a lista de cidades aqui ou o id da search
        intent.putExtra(CITY_LIST, new ArrayList(mCities));
        startActivity(intent);
    }

    private Long saveSearch(Search search) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SEARCH, search);
        bundle = getContentResolver().call(OTSContract.CONTENT_URI_LIST_CITIES_BY_COORDINATES, OTSContract.METHOD_SAVE_SEARCH, "", bundle);
        if (bundle == null) {
            return null;
        }
        return bundle.getLong(SEARCH);
    }

    /*
    private void normalizeDate(ContentValues values) {
        // normalize the date value
        if (values.containsKey(WeatherContract.WeatherEntry.COLUMN_DATE)) {
            long dateValue = values.getAsLong(WeatherContract.WeatherEntry.COLUMN_DATE);
            values.put(WeatherContract.WeatherEntry.COLUMN_DATE, WeatherContract.normalizeDate(dateValue));
        }
    }


    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }
*/

    public class TaskFinishedListener implements TaskFinished {
        @Override
        public void OnTaskFinished(List<City> cities) {

            Iterator<City> itCity = cities.iterator();
            while (itCity.hasNext()) {
                City city = (City) itCity.next();
                Log.i(CLASS_NAME, city.toString());
            }

            validateCities(cities);

        }

    }
}
