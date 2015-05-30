package br.borbi.ots;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.pojo.City;
import br.borbi.ots.utility.CoordinatesUtillity;
import br.borbi.ots.utility.Utility;

interface TaskFinished {

    public void OnTaskFinished(List<City> cities);
}

public class SearchActivity extends ActionBarActivity{

    private static final String CLASS_NAME = SearchActivity.class.getName();


    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mContext = this;

        Intent intent = getIntent();
        int distance = 0;
        Date dateBegin = null;
        Date dateEnd = null;
        int numberSunnyDays = 0;
        int minTemperature = 0;
        boolean usesCloudyDays = false;
        double lastLatitude = 0d;
        double lastLongitude =0d;
        if (intent != null) {
            distance = intent.getIntExtra(FiltersActivity.DISTANCE,0);
            dateBegin = (Date) intent.getSerializableExtra(FiltersActivity.DATE_BEGIN);
            dateEnd = (Date) intent.getSerializableExtra(FiltersActivity.DATE_END);
            numberSunnyDays= intent.getIntExtra(FiltersActivity.NUMBER_SUNNY_DAYS,0);
            minTemperature= intent.getIntExtra(FiltersActivity.MIN_TEMPERATURE,0);
            usesCloudyDays = intent.getBooleanExtra(FiltersActivity.USE_CLOUDY_DAYS, false);
            lastLatitude = intent.getDoubleExtra(FiltersActivity.LAST_LATITUDE,0);
            lastLongitude = intent.getDoubleExtra(FiltersActivity.LAST_LONGITUDE,0);
        }

        List<String> cities = searchCities(Double.valueOf(distance), lastLatitude, lastLongitude);
        int numberOfDays = getNumberOfDays(dateBegin,dateEnd);
        searchWeatherData(cities, numberOfDays);
    }

    private int getNumberOfDays(Date dateBegin, Date dateEnd){
        Calendar today = new GregorianCalendar();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        int numberOfDays = 16;
        if(dateBegin.compareTo(today.getTime()) == 0){
            numberOfDays = Utility.getDifferenceInDays(dateBegin, dateEnd);
        }else{
            numberOfDays = Utility.getDifferenceInDays(today.getTime(), dateEnd);
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


        StringBuffer whereClause = new StringBuffer(OTSContract.City.COLUMN_NAME_LATITUDE).append(" >= ").append(minLatitude).append(" AND ")
                .append(OTSContract.City.COLUMN_NAME_LATITUDE).append(" <= ").append(maxLatitude).append(" AND ")
                .append(OTSContract.City.COLUMN_NAME_LONGITUDE).append(" >= ").append(minLongitude).append(" AND ").append(OTSContract.City.COLUMN_NAME_LONGITUDE)
                .append(minLongitude).append(" <= ").append(maxLongitude);


        Log.i(CLASS_NAME, whereClause.toString());


        Cursor c = getContentResolver().query(
                OTSContract.City.CONTENT_URI,
                new String[]{OTSContract.City.COLUMN_NAME_NAME_ENGLISH},
                whereClause.toString(),
                null,
                null);


        List<String> cities = new ArrayList<String>();

        Log.i(CLASS_NAME, " vai iterar nos retornos");
        if (c.moveToFirst()) {
            do {
                int numIndexName = c.getColumnIndex(OTSContract.City.COLUMN_NAME_NAME_ENGLISH);
                //int numIndexLatRad = c.getColumnIndex(OTSContract.City.COLUMN_NAME_LATITUDE_RAD);
                //int numIndexLongRad = c.getColumnIndex(OTSContract.City.COLUMN_NAME_LONGITUDE_RAD);
                //strCity = c.getString(numIndexName);
                //Toast.makeText(this,strCity,Toast.LENGTH_SHORT).show();
                //Log.i(CLASS_NAME, strCity);
                cities.add(c.getString(numIndexName));

                Log.i(CLASS_NAME, c.getString(numIndexName));
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



    public class TaskFinishedListener implements TaskFinished{
        @Override
        public void OnTaskFinished(List<City> cities) {

            Iterator<City> itCity = cities.iterator();
            while (itCity.hasNext()){
                City city = (City) itCity.next();
                Log.i(CLASS_NAME, city.toString());
            }

            /*
            Intent intent = new Intent(mContext, FiltersActivity.class);
            intent.putExtra("bla",new ArrayList<City>(cities));
            startActivity(intent);
            */

        }

    }
}
