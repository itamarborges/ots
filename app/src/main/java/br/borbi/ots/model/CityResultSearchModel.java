package br.borbi.ots.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.enums.WeatherForecastSourcePriority;
import br.borbi.ots.pojo.City;
import br.borbi.ots.pojo.CityResultSearch;
import br.borbi.ots.pojo.Coordinates;

/**
 * Created by Gabriela on 27/09/2015.
 */
public class CityResultSearchModel {

    private static final String LOG_TAG = CityResultSearchModel.class.getSimpleName();

    private static final String[] RESULT_CITIES_COLUMNS = {
            OTSContract.Country.TABLE_NAME + "." + OTSContract.Country.COLUMN_NAME_TRANSLATION_FILE_KEY,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_NAME_ENGLISH,
            OTSContract.RelSearchCity.TABLE_NAME + "." + OTSContract.RelSearchCity.COLUMN_NAME_SEARCH_ID,
            OTSContract.RelSearchCity.TABLE_NAME + "." + OTSContract.RelSearchCity._ID,
            OTSContract.RelSearchCity.TABLE_NAME + "." + OTSContract.RelSearchCity.COLUMN_NAME_DISTANCE,
            OTSContract.RelSearchCity.TABLE_NAME + "." + OTSContract.RelSearchCity.COLUMN_NAME_WEATHER_FORECAST_SOURCE,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City._ID,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_LATITUDE,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_LONGITUDE,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_TRANSLATION_FILE_KEY
    };

    // these indices must match the projection
    private static final int INDEX_COUNTRY_TRANSLATION_FILE_KEY = 0;
    private static final int INDEX_CITY_NAME = 1;
    public static final int INDEX_SEARCH_ID = 2;
    private static final int INDEX_REL_SEARCH_CITY_ID = 3;
    private static final int INDEX_REL_SEARCH_CITY_DISTANCE = 4;
    private static final int INDEX_REL_SEARCH_FORECAST_SOURCE = 5;
    private static final int INDEX_CITY_ID = 6;
    private static final int INDEX_CITY_LATITUDE = 7;
    private static final int INDEX_CITY_LONGITUDE = 8;
    private static final int INDEX_CITY_TRANSLATION_FILE_KEY = 9;

    public static LinkedList<CityResultSearch> list(Context contextParam){

        String sortOrder = OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_NAME_ENGLISH +" ASC";

        Uri uriSearchByCities = OTSContract.CONTENT_URI_LIST_CITIES_BY_SEARCH;

        Cursor cursor = contextParam.getContentResolver().query(
                uriSearchByCities,
                RESULT_CITIES_COLUMNS,
                null,
                null,
                sortOrder);

        return fillCityResultSearchList(contextParam,cursor);
    }

    /*
    Lista as cidades que podem corresponder ao resultado atual de pesquisa
     */
    public static LinkedList<CityResultSearch> listCities(Context contextParam, Coordinates coordinates, Date minDateLastSearch, Date maxDateBegin, Date minDateEnd){
        String sortOrder = OTSContract.Search.TABLE_NAME + "." + OTSContract.Search.COLUMN_NAME_DATE_BEGIN +" ASC";

        Uri uriSearchByCities = OTSContract.CONTENT_URI_LIST_CITIES_BY_SEARCH_AND_NEW_SEARCH_PARAMETERS;

        int i=0;
        String[] selectionArgs = new String[7];
        selectionArgs[i++] = String.valueOf(coordinates.getMinLatitude());
        selectionArgs[i++] = String.valueOf(coordinates.getMaxLatitude());
        selectionArgs[i++] = String.valueOf(coordinates.getMinLongitude());
        selectionArgs[i++] = String.valueOf(coordinates.getMaxLongitude());
        selectionArgs[i++] = String.valueOf(minDateLastSearch.getTime());
        selectionArgs[i++] = String.valueOf(maxDateBegin.getTime());
        selectionArgs[i] = String.valueOf(minDateEnd.getTime());

        Cursor cursor = contextParam.getContentResolver().query(
                uriSearchByCities,
                RESULT_CITIES_COLUMNS,
                null,
                selectionArgs,
                sortOrder);

        // busca os resultados de previsao do tempo
        LinkedList<CityResultSearch> cities = fillCityResultSearchList(contextParam,cursor);

        for(CityResultSearch cityResultSearch:cities){
            cityResultSearch.setDayForecasts(DayForecastModel.listByCityAndDate(contextParam,cityResultSearch.getIdResultSearch(), maxDateBegin,minDateEnd));
        }

        return cities;
    }

    private static LinkedList<CityResultSearch> fillCityResultSearchList(Context contextParam,Cursor cursor ){
        LinkedList<CityResultSearch> cities = new LinkedList<>();

        if (cursor.moveToFirst()) {
            do {
                String strCountryName = contextParam.getString(contextParam.getResources().getIdentifier(cursor.getString(INDEX_COUNTRY_TRANSLATION_FILE_KEY), "string", contextParam.getPackageName()));
                String strCityName = cursor.getString(INDEX_CITY_NAME);
                int idResultSearchCity = cursor.getInt(INDEX_REL_SEARCH_CITY_ID);
                int idCity = cursor.getInt(INDEX_CITY_ID);
                Double cityLatitude = cursor.getDouble(INDEX_CITY_LATITUDE);
                Double cityLongitude = cursor.getDouble(INDEX_CITY_LONGITUDE);
                Integer distance = cursor.getInt(INDEX_REL_SEARCH_CITY_DISTANCE);
                String translationFileKey = cursor.getString(INDEX_CITY_TRANSLATION_FILE_KEY);
                WeatherForecastSourcePriority weatherSource = WeatherForecastSourcePriority.getSource(cursor.getInt(INDEX_REL_SEARCH_FORECAST_SOURCE));

                City city = new City(idCity, strCityName, null, strCountryName, cityLatitude, cityLongitude);
                city.setName(contextParam.getString(contextParam.getResources().getIdentifier(translationFileKey, "string", contextParam.getPackageName())));
                CityResultSearch cityResultSearch = new CityResultSearch(city, distance, idResultSearchCity, weatherSource);
                cities.add(cityResultSearch);
            }
            while (cursor.moveToNext());
        }

        if(cursor != null){
            cursor.close();
        }

        Collections.sort(cities);

        return cities;
    }
}
