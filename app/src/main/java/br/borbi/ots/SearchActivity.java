package br.borbi.ots;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.pojo.City;
import br.borbi.ots.pojo.DayForecast;
import br.borbi.ots.utility.CoordinatesUtillity;
import br.borbi.ots.utility.Utility;

interface TaskFinished {

    public void OnTaskFinished(List<City> cities);
}

public class SearchActivity extends ActionBarActivity{

    private static final String CLASS_NAME = SearchActivity.class.getName();

    public static final String CITY_LIST = "CITY_LIST";


    private Context mContext;

    private ArrayList<City> mCities;

    private int minTemperature=0;
    private int numberSunnyDays = 0;
    private boolean usesCloudyDays = false;
    private boolean dontUseTemperature = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mContext = this;

        Intent intent = getIntent();
        int distance = 0;
        Date dateBegin = null;
        Date dateEnd = null;


        double lastLatitude = 0d;
        double lastLongitude =0d;
        if (intent != null) {
            distance = intent.getIntExtra(FiltersActivity.DISTANCE,0);
            dateBegin = (Date) intent.getSerializableExtra(FiltersActivity.DATE_BEGIN);
            dateEnd = (Date) intent.getSerializableExtra(FiltersActivity.DATE_END);
            numberSunnyDays= intent.getIntExtra(FiltersActivity.NUMBER_SUNNY_DAYS,0);
            minTemperature= intent.getIntExtra(FiltersActivity.MIN_TEMPERATURE,0);
            dontUseTemperature = intent.getBooleanExtra(FiltersActivity.DONT_USE_TEMPERATURE, false);
            usesCloudyDays = intent.getBooleanExtra(FiltersActivity.USE_CLOUDY_DAYS, false);
            lastLatitude = intent.getDoubleExtra(FiltersActivity.LAST_LATITUDE,0);
            lastLongitude = intent.getDoubleExtra(FiltersActivity.LAST_LONGITUDE,0);
        }

        List<String> cities = searchCities(Double.valueOf(distance), lastLatitude, lastLongitude);
        int numberOfDays = getNumberOfDays(dateBegin,dateEnd);
        searchWeatherData(cities, numberOfDays);
    }

    private int getNumberOfDays(Date dateBegin, Date dateEnd){
        Date today = Utility.setDateToInitialHours(new Date());
        dateBegin = Utility.setDateToInitialHours(dateBegin);
        dateEnd = Utility.setDateToFinalHours(dateEnd);

        int numberOfDays = 16;

        if(dateBegin.compareTo(today) == 0){
            numberOfDays = Utility.getDifferenceInDays(dateBegin, dateEnd);
            numberOfDays++;
        }else{
            numberOfDays = Utility.getDifferenceInDays(today, dateEnd);
            numberOfDays++;
        }

        return numberOfDays;
    }


    /*
    Busca cidades
     */
    private List<String> searchCities(double distance, double lastLatitude, double lastLongitude){
        double minLatitude = CoordinatesUtillity.getMinLatitude(lastLatitude, distance);
        double maxLatitude = CoordinatesUtillity.getMaxLatitude(lastLatitude, distance);
        double minLongitude = CoordinatesUtillity.getMinLongitude(lastLatitude, lastLongitude, distance);
        double maxLongitude = CoordinatesUtillity.getMaxLongitude(lastLatitude, lastLongitude, distance);


        if(minLatitude > maxLatitude){
            double aux = maxLatitude;
            maxLatitude = minLatitude;
            minLatitude = aux;
        }
        if(minLongitude > maxLongitude){
            double aux = maxLongitude;
            maxLongitude = minLongitude;
            minLongitude = aux;
        }

        Log.i(CLASS_NAME, "minlat = " + minLatitude);
        Log.i(CLASS_NAME, "maxlat = " + maxLatitude);
        Log.i(CLASS_NAME, "minlong = " + minLongitude);
        Log.i(CLASS_NAME, "maxlong  = " + maxLongitude);


        StringBuffer whereClause = new StringBuffer(
                OTSContract.City.COLUMN_NAME_LATITUDE).append(" >= ?")
                .append(" AND ")
                .append(OTSContract.City.COLUMN_NAME_LATITUDE).append(" <= ?")
                .append(" AND ")
                .append(OTSContract.City.COLUMN_NAME_LONGITUDE).append(" >= ?")
                .append(" AND ")
                .append(OTSContract.City.COLUMN_NAME_LONGITUDE).append(" <= ?");


        String[] selectionArgs = new String[4];
        selectionArgs[0] = String.valueOf(minLatitude);
        selectionArgs[1] = String.valueOf(maxLatitude);
        selectionArgs[2] = String.valueOf(minLongitude);
        selectionArgs[3] = String.valueOf(maxLongitude);


        Log.i(CLASS_NAME, whereClause.toString());


        Cursor c = getContentResolver().query(
                OTSContract.CONTENT_URI_LIST_CITIES_BY_COORDINATES,
                new String[]{OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_NAME_ENGLISH, OTSContract.Country.COLUMN_NAME_COUNTRY_CODE},
                whereClause.toString(),
                selectionArgs,
                null);


        List<String> cities = new ArrayList<String>();

        Log.i(CLASS_NAME, " vai iterar nos retornos");
        Log.i(CLASS_NAME, " total de retornos = " + c.getCount());
        if (c.moveToFirst()) {
            do {
                int numIndexName = c.getColumnIndex(OTSContract.City.COLUMN_NAME_NAME_ENGLISH);
                int numIndexCountryCode = c.getColumnIndex(OTSContract.Country.COLUMN_NAME_COUNTRY_CODE);

                String city = c.getString(numIndexName)+","+ c.getString(numIndexCountryCode);

                cities.add(city);

                Log.i(CLASS_NAME, city);
            }
            while (c.moveToNext());
        }
        return cities;
    }

    private void searchWeatherData(List<String> cities, int numberOfDays){

        Log.i(CLASS_NAME, "number of days = " + numberOfDays);

        String[] citiesArray = new String[cities.size() + 1];
        citiesArray[0] = String.valueOf(numberOfDays);

        Iterator<String> it = cities.iterator();
        int i = 1;
        while(it.hasNext()){
            citiesArray[i++] = (String) it.next();
        }

        for (int j=0;j<citiesArray.length;j++){
            Log.i(CLASS_NAME, citiesArray[j]);
        }

        FetchWeatherTask weatherTask = new FetchWeatherTask(this, new TaskFinishedListener());
        weatherTask.execute(citiesArray);

    }

    private void validateCities(List<City> cities){

        mCities = new ArrayList<City>();

        Iterator<City> itCity = cities.iterator();
        while (itCity.hasNext()) {
            City city = (City) itCity.next();

            Iterator<DayForecast> itDayForecast = city.getDayForecasts().iterator();
            int contSunnyDays = 0;
            boolean validCity = true;


            while(itDayForecast.hasNext() && validCity){
                DayForecast dayForecast = (DayForecast) itDayForecast.next();

                if(!dontUseTemperature && dayForecast.getMinTemperature() < minTemperature){
                    validCity = false;
                }

                if(dayForecast.getWeatherType().isSunnyDay(usesCloudyDays)){
                    contSunnyDays++;
                }
            }

            if(contSunnyDays < numberSunnyDays){
                validCity = false;
            }

            if(validCity){
                mCities.add(city);
            }
        }

        Log.i(CLASS_NAME, "============= CIDADES VALIDAS");
        itCity = mCities.iterator();
        while (itCity.hasNext()) {
            City city = (City) itCity.next();
            Log.i(CLASS_NAME, city.toString());
        }

        Intent intent = new Intent(this,ResultActivity.class);
        intent.putExtra(CITY_LIST, new ArrayList(mCities));
        startActivity(intent);
    }



    public class TaskFinishedListener implements TaskFinished{
        @Override
        public void OnTaskFinished(List<City> cities) {

            Iterator<City> itCity = cities.iterator();
            while (itCity.hasNext()){
                City city = (City) itCity.next();
                Log.i(CLASS_NAME, city.toString());
            }

            validateCities(cities);

        }

    }
}
