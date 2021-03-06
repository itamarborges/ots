package br.borbi.ots;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.entity.Search;
import br.borbi.ots.model.CityResultSearchModel;
import br.borbi.ots.model.SearchModel;
import br.borbi.ots.pojo.City;
import br.borbi.ots.pojo.CityResultSearch;
import br.borbi.ots.pojo.Coordinates;
import br.borbi.ots.pojo.DayForecast;
import br.borbi.ots.pojo.SearchParameters;
import br.borbi.ots.task.FetchWeatherTask;
import br.borbi.ots.utility.CoordinatesUtility;
import br.borbi.ots.utility.DateUtility;
import br.borbi.ots.utility.ForwardUtility;
import br.borbi.ots.utility.LocationUtility;
import br.borbi.ots.utility.Utility;

interface TaskFinished {

    void OnTaskFinished(List<CityResultSearch> cities);
}

@SuppressWarnings("UnnecessaryBoxing")
public class SearchActivity extends AppCompatActivity {

    private static final String LOG_TAG = SearchActivity.class.getName();

    private static final String MINIMUM_DISTANCE = "MINIMUM_DISTANCE";

    public static final String SEARCH = "SEARCH";

    private Context mContext;
    // Cidades nao validadas, retornadas da pesquisa pela previsao do tempo.
    private List<CityResultSearch> mCitiesFromSearch = null;

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

    private FetchWeatherTask weatherTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mContext = this;

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

        if(lastLatitude == null || lastLongitude == null){
            AlertDialog dialog = LocationUtility.buildLocationDialog(mContext);
            if(dialog!=null) {
                dialog.show();
            }
        }

        mWarningTask = new WarningTask();
        mWarningTask.execute();

        List<City> cities = searchCities(Double.valueOf(mMaxDistance), lastLatitude, lastLongitude);
        if(canUseLastSearchData()) {
            cities = removeCitiesAlreadySearched(cities);
        }

        if(cities == null || cities.isEmpty()){
         validateCities(mCitiesFromSearch);
        }else {
            int numberOfDays = Utility.getNumberOfDaysToSearch(dateBegin, dateEnd);
            searchWeatherData(cities, numberOfDays);
        }
    }

    private boolean canUseLastSearchData(){
        Search search = SearchModel.findSearch(this);

        if(search == null){
            //nao tem pesquisa salva, nao procura
            return false;
        }

        if(!DateUtility.isToday(search.getDateTimeLastSearch())){
            return false;
        }
        // Se a ultima pesquisa foi feita hoje
        if (DateUtility.isDateBeforeAnother(dateBegin,search.getBeginDate())){
            // Se data inicial da ultima pesquisa é depois da data da nova pesquisa, nao usa ultima pesquisa
            return false;
        }

        if (DateUtility.isDateBeforeAnother(search.getEndDate(),dateEnd)){
            // Se data final da ultima pesquisa é antes da data da nova pesquisa, nao usa ultima pesquisa
            return false;
        }

        if(search.getMinSunnyDays() < numberSunnyDays){
            return false;
        }

        if(!search.getIncludesCloudyDays() && search.getIncludesCloudyDays() != usesCloudyDays){
            return false;
        }

        if(search.getMinTemperature() != 0){
            return search.getMinTemperature() <= minTemperature;
        }

        return true;
    }

    private List<City> removeCitiesAlreadySearched(List<City> cities){
        List<City> citiesAux = new ArrayList<>();
        citiesAux.addAll(cities);

        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.HOUR_OF_DAY,-OTSContract.HOURS_TO_SEARCH_EXPIRATION);

        LinkedList<CityResultSearch> citiesAlreadySearched = CityResultSearchModel.listCities(mContext, new Coordinates(lastLatitude, lastLongitude, Double.valueOf(mMaxDistance)), cal.getTime(), dateBegin, dateEnd);
        mCitiesFromSearch = new ArrayList<>();
        if(citiesAlreadySearched!= null || !citiesAlreadySearched.isEmpty()) {
            mCitiesFromSearch = new ArrayList<>();
            boolean found = false;
            for (City city : citiesAux) {
                found = false;
                Iterator<CityResultSearch> it = citiesAlreadySearched.iterator();
                while (it.hasNext() && !found) {
                    CityResultSearch cityResultSearch = it.next();
                    if (cityResultSearch.getCity().equals(city)) {
                        // remover da lista a pesquisar;
                        cities.remove(city);
                        // acrescentar na lista de resultados
                        mCitiesFromSearch.add(cityResultSearch);
                        found = true;
                    }
                }
            }
        }
        return cities;
    }

    /*
    Busca cidades
     */
    private List<City> searchCities(double distance, double lastLatitude, double lastLongitude) {
        Coordinates coordinates = new Coordinates(lastLatitude, lastLongitude, distance);

        String whereClause = OTSContract.City.COLUMN_NAME_LATITUDE +
                " >= ?" +
                " AND " +
                OTSContract.City.COLUMN_NAME_LATITUDE + " <= ?" +
                " AND " +
                OTSContract.City.COLUMN_NAME_LONGITUDE + " >= ?" +
                " AND " +
                OTSContract.City.COLUMN_NAME_LONGITUDE + " <= ?";

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
                whereClause,
                selectionArgs,
                null);

        List<City> cities = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                int numIndexName = c.getColumnIndex(OTSContract.City.COLUMN_NAME_NAME_ENGLISH);
                int numIndexCityId = c.getColumnIndex(OTSContract.City._ID);
                int numIndexCountryCode = c.getColumnIndex(OTSContract.Country.COLUMN_NAME_COUNTRY_CODE);
                int numIndexCityLatitude = c.getColumnIndex(OTSContract.City.COLUMN_NAME_LATITUDE);
                int numIndexCityLongitude = c.getColumnIndex(OTSContract.City.COLUMN_NAME_LONGITUDE);

                City city = new City(c.getInt(numIndexCityId), c.getString(numIndexName), c.getString(numIndexCountryCode),null,c.getDouble(numIndexCityLatitude),c.getDouble(numIndexCityLongitude));

                Integer distanceBetweenCities = Utility.roundCeil(CoordinatesUtility.getDistance(lastLatitude, lastLongitude, city.getLatitude(), city.getLongitude()));

                if(Utility.isDistanceSmallerThanMinimumDistance(distanceBetweenCities, mMaxDistance)){
                    cities.add(city);
                }
            }
            while (c.moveToNext());
        }

        if(c!= null){
            c.close();
        }

        return cities;
    }

    private void searchWeatherData(List<City> cities, int numberOfDays) {
        SearchParameters searchParameters = new SearchParameters(numberOfDays, cities);

        getContentResolver().delete(OTSContract.Search.CONTENT_URI,null,null);

        weatherTask = new FetchWeatherTask(new TaskFinishedListener());
        weatherTask.execute(searchParameters);

    }

    private void validateCities(List<CityResultSearch> cities) {
        Long mIdSearch = null;
        ArrayList<CityResultSearch> mMCities = new ArrayList<>();

        for (CityResultSearch cityResultSearch: cities) {

            int contSunnyDays = 0;
            boolean validCity = true;

            List<DayForecast> dayForecasts = new LinkedList<>();

            Integer distance = Utility.roundCeil(CoordinatesUtility.getDistance(lastLatitude, lastLongitude, cityResultSearch.getCity().getLatitude(), cityResultSearch.getCity().getLongitude()));

            if(Utility.isDistanceSmallerThanMinimumDistance(distance, mMaxDistance) && cityResultSearch.getDayForecasts()!=null){

                for (DayForecast dayForecast : cityResultSearch.getDayForecasts()) {

                    if((dayForecast.getDate().after(dateBegin) && dayForecast.getDate().before(dateEnd)) || Utility.isSameDay(dayForecast.getDate(), dateBegin) || Utility.isSameDay(dayForecast.getDate(), dateEnd)){

                        if (!dontUseTemperature && dayForecast.getMinTemperature() < minTemperature) {
                            validCity = false;
                        }

                        if (dayForecast.getWeatherType().isSunnyDay(usesCloudyDays)) {
                            contSunnyDays++;
                        }

                        dayForecasts.add(dayForecast);
                    }//
                }

                if (contSunnyDays < numberSunnyDays) {
                    validCity = false;
                }
            }else{
                validCity = false;
            }
//
            if (validCity) {
                cityResultSearch.setDayForecasts(dayForecasts);
                cityResultSearch.setDistance(distance);
                mMCities.add(cityResultSearch);
            }
        }

        Search search = new Search();
        search.setBeginDate(dateBegin);
        search.setEndDate(dateEnd);
        search.setRadius(mMaxDistance);
        search.setMinSunnyDays(numberSunnyDays);
        search.setIncludesCloudyDays(usesCloudyDays);
        search.setMinTemperature((double) minTemperature);
        search.setDateTimeLastSearch(new Date());
        search.setOriginLatitude(lastLatitude);
        search.setOriginLongitude(lastLongitude);
        search.setCites(mMCities);

        if (mMCities.size() > 0) {
            mIdSearch = saveSearch(search);
        }

        if(mWarningTask!=null) {
            mWarningTask.cancel(true);
        }
        if(mToast != null){
            mToast.cancel();
        }

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(MINIMUM_DISTANCE, mMaxDistance);
        intent.putExtra(ForwardUtility.SEARCH_ID, mIdSearch);
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
        public void OnTaskFinished(List<CityResultSearch> cities) {
            if(mCitiesFromSearch == null || mCitiesFromSearch.isEmpty()){
                mCitiesFromSearch = cities;
            }else{
                mCitiesFromSearch.addAll(cities);
            }
            validateCities(mCitiesFromSearch);
        }
    }

    private class WarningTask extends AsyncTask<String, Integer, Void> {
        protected Void doInBackground(String... params) {

            long delay = 3000;
            long difference = 8000;

            if(!isCancelled()) {
                Timer timer = new Timer();
                timer.schedule(new MyTimerTask(mContext.getString(R.string.searching_sunny_cities_message0)), delay);

                delay += difference;
            }

            if(!isCancelled()) {
                Timer timer = new Timer();
                timer.schedule(new MyTimerTask(mContext.getString(R.string.searching_sunny_cities_message6)), delay);

                delay += difference;
            }

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

                delay += difference;
            }


            if(!isCancelled()) {
                Timer timer = new Timer();
                timer.schedule(new MyTimerTask(mContext.getString(R.string.searching_sunny_cities_message4)), delay);

                delay += difference;
            }


            if(!isCancelled()) {
                Timer timer = new Timer();
                timer.schedule(new MyTimerTask(mContext.getString(R.string.searching_sunny_cities_message5)), delay);

                delay += difference;
            }
            return null;
        }

        private class MyTimerTask extends TimerTask{

            private final String message;
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

    @Override
    protected void onPause() {
        super.onPause();

        if (weatherTask != null) {
            weatherTask.cancel(true);
        }
        if (mWarningTask != null) {
            mWarningTask.cancel(true);
        }
    }
}
