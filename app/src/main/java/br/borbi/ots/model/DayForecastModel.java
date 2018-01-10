package br.borbi.ots.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Date;
import java.util.LinkedList;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.enums.WeatherType;
import br.borbi.ots.pojo.DayForecast;

/**
 * Created by Gabriela on 23/10/2015.
 */
class DayForecastModel {

    private static final String[] RESULT_CITY_COLUMNS = {
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_DATE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_MINIMUM_TEMPERATURE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_MAXIMUM_TEMPERATURE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_WEATHER_TYPE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch._ID,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_REL_SEARCH_CITY_ID,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_HUMIDITY,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_PRECIPITATION,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_MORNING_TEMPERATURE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_EVENING_TEMPERATURE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_NIGHT_TEMPERATURE
    };

    public static LinkedList<DayForecast> list(Context contextParam, int idRelSearchCity){
        String sortOrder = OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_DATE +" ASC";

        Uri uriResultCity = OTSContract.ResultSearch.CONTENT_URI;

        String selection  = OTSContract.ResultSearch.COLUMN_NAME_REL_SEARCH_CITY_ID + " = ? ";
        String[]selectionArgs = new String[]{String.valueOf(idRelSearchCity)};

        Cursor cursor = contextParam.getContentResolver().query(
                uriResultCity,
                RESULT_CITY_COLUMNS,
                selection,
                selectionArgs,
                sortOrder);

        return fillDayForecastList(cursor);
    }

    public static LinkedList<DayForecast> listByCityAndDate(Context contextParam, int idRelSearchCity, Date minDate, Date maxDate){
        String sortOrder = OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_DATE +" ASC";

        Uri uriResultCity = OTSContract.ResultSearch.CONTENT_URI;

        String selection = OTSContract.ResultSearch.COLUMN_NAME_REL_SEARCH_CITY_ID +
                " = ? AND " +
                OTSContract.ResultSearch.COLUMN_NAME_DATE +
                " BETWEEN ? AND ?";

        int i=0;
        String[]selectionArgs = new String[3];
        selectionArgs[i++] = String.valueOf(idRelSearchCity);
        selectionArgs[i++] = String.valueOf(minDate.getTime());
        selectionArgs[i] = String.valueOf(maxDate.getTime());

        Cursor cursor = contextParam.getContentResolver().query(
                uriResultCity,
                RESULT_CITY_COLUMNS,
                selection,
                selectionArgs,
                sortOrder);

        return fillDayForecastList(cursor);
    }

    private static LinkedList<DayForecast> fillDayForecastList(Cursor cursor){
        LinkedList<DayForecast> databaseForecasts = new LinkedList<>();
        int position = 0;
        if (cursor.moveToFirst()) {
            do {
                int numIndexDate = cursor.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_DATE);
                int numIndexMinimumTemperature = cursor.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_MINIMUM_TEMPERATURE);
                int numIndexMaximumTemperature = cursor.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_MAXIMUM_TEMPERATURE);
                int numIndexWeatherType = cursor.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_WEATHER_TYPE);
                int numIndexResultSearchID = cursor.getColumnIndex(OTSContract.ResultSearch._ID);
                int numIndexMorningTemperature = cursor.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_MORNING_TEMPERATURE);
                int numIndexEveningTemperature = cursor.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_EVENING_TEMPERATURE);
                int numIndexNightTemperature = cursor.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_NIGHT_TEMPERATURE);
                int numIndexPrecipitation = cursor.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_PRECIPITATION);
                int numIndexHumidity = cursor.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_HUMIDITY);

                //(Date date, Double minTemperature, Double maxTemperature, Double morningTemperature, Double eveningTemperature,
                // Double nightTemperature, WeatherType weatherType, Double precipitation, Double humidity) {
                DayForecast dayForecast = new DayForecast(new Date(cursor.getLong(numIndexDate)), cursor.getDouble(numIndexMinimumTemperature), cursor.getDouble(numIndexMaximumTemperature),
                        cursor.getDouble(numIndexMorningTemperature),cursor.getDouble(numIndexEveningTemperature),cursor.getDouble(numIndexNightTemperature),
                        WeatherType.getWeatherType(cursor.getInt(numIndexWeatherType)),cursor.getDouble(numIndexPrecipitation),cursor.getDouble(numIndexHumidity));
                dayForecast.setId(cursor.getInt(numIndexResultSearchID));
                dayForecast.setPosition(position);
                databaseForecasts.add(dayForecast);

                ++position;
            }
            while (cursor.moveToNext());
        }

        if(cursor != null){
            cursor.close();
        }

        return databaseForecasts;
    }
}
