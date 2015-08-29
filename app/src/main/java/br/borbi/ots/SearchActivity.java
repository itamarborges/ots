package br.borbi.ots;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.entity.Search;
import br.borbi.ots.pojo.City;
import br.borbi.ots.pojo.CityResultSearch;
import br.borbi.ots.pojo.Coordinates;
import br.borbi.ots.pojo.DayForecast;
import br.borbi.ots.pojo.SearchParameters;
import br.borbi.ots.task.FetchWeatherTask;
import br.borbi.ots.utility.CoordinatesUtillity;
import br.borbi.ots.utility.Utility;

interface TaskFinished {

    public void OnTaskFinished(List<City> cities);
}

public class SearchActivity extends ActionBarActivity {

    private static final String LOG_TAG = SearchActivity.class.getName();

    public static final String MINIMUM_DISTANCE = "MINIMUM_DISTANCE";

    public static final String SEARCH = "SEARCH";

    private Context mContext;
    private ArrayList<CityResultSearch> mCities;

    private int minTemperature = 0;
    private int numberSunnyDays = 0;
    private boolean usesCloudyDays = false;
    private boolean dontUseTemperature = false;
    private Date dateBegin = null;
    private Date dateEnd = null;
    private Integer mMaxDistance = null;
    private Double lastLatitude = 0d;
    private Double lastLongitude = 0d;
    private Toast mToast;
    private WarningTask mWarningTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mContext = this;

        AdView mAdView = null;
        Utility.initializeAd(mAdView, this);
        Intent intent = getIntent();

        if (intent != null) {
            mMaxDistance = intent.getIntExtra(FiltersActivity.DISTANCE, 0);
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

        //lastLatitude = -30.033333;
        //lastLongitude = -51.216667;

        //lastLatitude = 42.358429;
        //lastLongitude = -71.059769;

        if(lastLatitude == null || lastLongitude == null){
            Intent failureIntent = new Intent();
            failureIntent.setClass(SearchActivity.this, FailureActivity.class);
            startActivity(failureIntent);
        }

        mWarningTask = new WarningTask();
        mWarningTask.execute();

        List<City> cities = searchCities(Double.valueOf(mMaxDistance), lastLatitude, lastLongitude);
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

        String[] citiesColumns = {
                OTSContract.City.TABLE_NAME + "." + OTSContract.City._ID,
                OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_NAME_ENGLISH,
                OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_LATITUDE,
                OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_LONGITUDE,
                OTSContract.Country.TABLE_NAME + "." + OTSContract.Country.COLUMN_NAME_COUNTRY_CODE
        };


        Cursor c = getContentResolver().query(
                OTSContract.CONTENT_URI_LIST_CITIES_BY_COORDINATES,
                citiesColumns,
                //new String[]{OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_NAME_ENGLISH, OTSContract.City.TABLE_NAME + "." + OTSContract.City._ID, OTSContract.Country.COLUMN_NAME_COUNTRY_CODE},
                whereClause.toString(),
                selectionArgs,
                null);

        List<City> cities = new ArrayList<City>();

        if (c.moveToFirst()) {
            do {
                int numIndexName = c.getColumnIndex(OTSContract.City.COLUMN_NAME_NAME_ENGLISH);
                int numIndexCityId = c.getColumnIndex(OTSContract.City._ID);
                int numIndexCountryCode = c.getColumnIndex(OTSContract.Country.COLUMN_NAME_COUNTRY_CODE);
                int numIndexCityLatitude = c.getColumnIndex(OTSContract.City.COLUMN_NAME_LATITUDE);
                int numIndexCityLongitude = c.getColumnIndex(OTSContract.City.COLUMN_NAME_LONGITUDE);

                City city = new City(c.getLong(numIndexCityId), c.getString(numIndexName), c.getString(numIndexCountryCode),null,c.getDouble(numIndexCityLatitude),c.getDouble(numIndexCityLongitude));

                cities.add(city);
            }
            while (c.moveToNext());
        }
        return cities;
    }

    private void searchWeatherData(List<City> cities, int numberOfDays) {
        SearchParameters searchParameters = new SearchParameters(numberOfDays, cities);

        getContentResolver().delete(OTSContract.Search.CONTENT_URI,null,null);

        FetchWeatherTask weatherTask = new FetchWeatherTask(this, new TaskFinishedListener());
        weatherTask.execute(searchParameters);

    }

    private void validateCities(List<City> cities) {

        mCities = new ArrayList<CityResultSearch>();

        for (City city : cities) {

            int contSunnyDays = 0;
            boolean validCity = true;

            List<DayForecast> dayForecasts = new LinkedList<DayForecast>();

            Integer distance = Utility.roundCeil(CoordinatesUtillity.getDistance(lastLatitude, lastLongitude, city.getLatitude(), city.getLongitude()));

            if(Utility.isDistanceSmallerThanMinimumDistance(distance, mMaxDistance)){

                for (DayForecast dayForecast : city.getDayForecasts()) {
                    if (dayForecast.getDate().after(dateBegin) || Utility.isSameDay(dayForecast.getDate(), dateBegin)) {

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
            }else{
                validCity = false;
            }

            if (validCity) {
                city.setDayForecasts(dayForecasts);
                mCities.add(new CityResultSearch(city,distance));
            }
        }

        Search search = new Search();
        search.setBeginDate(dateBegin);
        search.setEndDate(dateEnd);
        search.setRadius(mMaxDistance);
        search.setMinSunnyDays(numberSunnyDays);
        search.setMinTemperature(Double.valueOf(minTemperature));
        search.setDateTimeLastSearch(new Date());
        search.setOriginLatitude(lastLatitude);
        search.setOriginLongitude(lastLongitude);
        search.setCites(mCities);

        if (mCities.size() > 0) {
            saveSearch(search);
        }

        if(mWarningTask!=null) {
            mWarningTask.cancel(true);
        }
        if(mToast != null){
            mToast.cancel();
        }

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(MINIMUM_DISTANCE, mMaxDistance);
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

    public class TaskFinishedListener implements TaskFinished {
        @Override
        public void OnTaskFinished(List<City> cities) {
            validateCities(cities);
        }
    }

    private class WarningTask extends AsyncTask<String, Integer, Void> {
        protected Void doInBackground(String... params) {

            long delay = 3000;
            long difference =3000;

            if(!isCancelled()) {
                Timer timer = new Timer();
                timer.schedule(new MyTimerTask(mContext.getString(R.string.searching_sunny_cities_message1)), delay);

                delay += difference;
            }

            if(!isCancelled()) {
                Timer timer = new Timer();
                timer.schedule(new MyTimerTask(mContext.getString(R.string.searching_sunny_cities_message2)), delay);

                delay += difference;
            }

            if(!isCancelled()) {
                Timer timer = new Timer();
                timer.schedule(new MyTimerTask(mContext.getString(R.string.searching_sunny_cities_message3)), delay);
            }

            return null;
        }

        private class MyTimerTask extends TimerTask{

            private String message;
            public MyTimerTask(String message) {
                this.message = message;
            }

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!mWarningTask.isCancelled()) {
                            mToast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
                            mToast.show();
                        }
                    }
                });
            }
        }
    }
}
