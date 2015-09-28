package br.borbi.ots.model;

import android.content.Context;
import android.net.Uri;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.pojo.City;
import br.borbi.ots.pojo.CityResultSearch;
import android.database.Cursor;

/**
 * Created by Gabriela on 27/09/2015.
 */
public class CityResultSearchModel {

    private static final String[] RESULT_CITIES_COLUMNS = {
            OTSContract.Country.TABLE_NAME + "." + OTSContract.Country.COLUMN_NAME_NAME_ENGLISH,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_NAME_ENGLISH,
            OTSContract.RelSearchCity.TABLE_NAME + "." + OTSContract.RelSearchCity.COLUMN_NAME_SEARCH_ID,
            OTSContract.RelSearchCity.TABLE_NAME + "." + OTSContract.RelSearchCity._ID,
            OTSContract.RelSearchCity.TABLE_NAME + "." + OTSContract.RelSearchCity.COLUMN_NAME_DISTANCE,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City._ID,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_LATITUDE,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_LONGITUDE,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_TRANSLATION_FILE_KEY
    };

    // these indices must match the projection
    public static final int INDEX_COUNTRY_NAME = 0;
    public static final int INDEX_CITY_NAME = 1;
    public static final int INDEX_SEARCH_ID = 2;
    public static final int INDEX_REL_SEARCH_CITY_ID = 3;
    public static final int INDEX_REL_SEARCH_CITY_DISTANCE = 4;
    public static final int INDEX_CITY_ID = 5;
    public static final int INDEX_CITY_LATITUDE = 6;
    public static final int INDEX_CITY_LONGITUDE = 7;
    public static final int INDEX_CITY_TRANSLATION_FILE_KEY = 8;

    public static LinkedList<CityResultSearch> list(Context contextParam){

        String sortOrder = OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_NAME_ENGLISH +" ASC";

        Uri uriSearchByCities = OTSContract.CONTENT_URI_LIST_CITIES_BY_SEARCH;

        Cursor cursor = contextParam.getContentResolver().query(
                uriSearchByCities,
                RESULT_CITIES_COLUMNS,
                null,
                null,
                sortOrder);


        LinkedList<CityResultSearch> cities = new LinkedList<>();

        if (cursor.moveToFirst()) {
            do {
                String strCountryName = cursor.getString(INDEX_COUNTRY_NAME);
                String strCityName = cursor.getString(INDEX_CITY_NAME);
                int idResultSearchCity = cursor.getInt(INDEX_REL_SEARCH_CITY_ID);
                int idCity = cursor.getInt(INDEX_CITY_ID);
                Double cityLatitude = cursor.getDouble(INDEX_CITY_LATITUDE);
                Double cityLongitude = cursor.getDouble(INDEX_CITY_LONGITUDE);
                Integer distance = cursor.getInt(INDEX_REL_SEARCH_CITY_DISTANCE);
                String translationFileKey = cursor.getString(INDEX_CITY_TRANSLATION_FILE_KEY);

                City city = new City(idCity, strCityName, null, strCountryName, cityLatitude, cityLongitude);
                city.setName(contextParam.getString(contextParam.getResources().getIdentifier(translationFileKey, "string", contextParam.getPackageName())));
                CityResultSearch cityResultSearch = new CityResultSearch(city, distance, idResultSearchCity);
                cities.add(cityResultSearch);
            }
            while (cursor.moveToNext());
        }

        Collections.sort(cities);

        return cities;
    }
}
