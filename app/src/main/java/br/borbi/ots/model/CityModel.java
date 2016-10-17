package br.borbi.ots.model;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

import java.util.Collections;
import java.util.LinkedList;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.data.OTSProvider;
import br.borbi.ots.pojo.City;

/**
 * Created by Itamar on 18/09/2015.
 */
public class CityModel {

    private static final String LOG_TAG = CityModel.class.getSimpleName();

    public City getCity(City cityParam, Activity activityParam) throws Exception {

        City city = null;

        if (cityParam.getId() == 0) {
            throw new Exception("Id city is empty.");
        }

        String[] selectionArgs = new String[]{Long.toString(cityParam.getId())};
        Cursor c =  activityParam.getContentResolver().query(
                OTSContract.City.CONTENT_URI,
                OTSProvider.CITIES_COLUMNS,
                OTSProvider.FILTER_BY_CITY,
                selectionArgs,
                null);

        if (c.moveToNext()) {

            int id = c.getInt(c.getColumnIndex(OTSContract.City._ID));
            Double latitude = c.getDouble(c.getColumnIndex(OTSContract.City.COLUMN_NAME_LATITUDE));
            Double longitude = c.getDouble(c.getColumnIndex(OTSContract.City.COLUMN_NAME_LONGITUDE));
            String nameEnglish = c.getString(c.getColumnIndex(OTSContract.City.COLUMN_NAME_NAME_ENGLISH));
            int countryId = c.getInt(c.getColumnIndex(OTSContract.City.COLUMN_NAME_COUNTRY_ID));
            String translationFileKey = c.getString(c.getColumnIndex(OTSContract.City.COLUMN_NAME_TRANSLATION_FILE_KEY));

            city = new City(id, latitude, longitude, nameEnglish, countryId, translationFileKey);

            city.setName(activityParam.getString(activityParam.getResources().getIdentifier(translationFileKey, "string", activityParam.getPackageName())));

        }
        if(c!= null){
            c.close();
        }

        return city;
    }

    public static LinkedList<City> listCitiesWithTags(City cityParam, Context contextParam) {

        LinkedList<City> listCities = new LinkedList<City>();

        City city = null;

        String strFilter = null;
        String[] selectionArgs = null;

        if (cityParam.getCountryId() != 0) {
            strFilter = OTSProvider.FILTER_CITY_BY_COUNTRY;
            selectionArgs = new String[]{Long.toString(cityParam.getCountryId())};
        }

        Cursor c =  contextParam.getContentResolver().query(
                OTSContract.City.CONTENT_URI,
                OTSProvider.CITIES_COLUMNS,
                strFilter,
                selectionArgs,
                null);

        while (c.moveToNext()) {

            int id = c.getInt(c.getColumnIndex(OTSContract.City._ID));
            Double latitude = c.getDouble(c.getColumnIndex(OTSContract.City.COLUMN_NAME_LATITUDE));
            Double longitude = c.getDouble(c.getColumnIndex(OTSContract.City.COLUMN_NAME_LONGITUDE));
            String nameEnglish = c.getString(c.getColumnIndex(OTSContract.City.COLUMN_NAME_NAME_ENGLISH));
            int countryId = c.getInt(c.getColumnIndex(OTSContract.City.COLUMN_NAME_COUNTRY_ID));
            String translationFileKey = c.getString(c.getColumnIndex(OTSContract.City.COLUMN_NAME_TRANSLATION_FILE_KEY));

            city = new City(id, latitude, longitude, nameEnglish, countryId, translationFileKey);

            city.setName(contextParam.getString(contextParam.getResources().getIdentifier(translationFileKey, "string", contextParam.getPackageName())));

            String selectionTags = OTSProvider.FILTER_BY_CITY;
            String[] selectionArgsTags = new String[]{String.valueOf(id)};

            //Recupera as tags
            Cursor cTags = contextParam.getContentResolver().query(
                    OTSContract.CONTENT_URI_LIST_TAGS_FROM_A_CITY,
                    OTSProvider.TAG_COLUMNS,
                    selectionTags,
                    selectionArgsTags,
                    null);

            LinkedList<String> tagNames = new LinkedList<String>();
            String tag = "";
            String resourceName = "";
            while (cTags.moveToNext()) {
                resourceName = cTags.getString(cTags.getColumnIndex(OTSContract.Tag.COLUMN_NAME_RESOURCE_NAME));

                tag = contextParam.getString(contextParam.getResources().getIdentifier(resourceName, "string", contextParam.getPackageName()));

                if (!tag.isEmpty()) {
                    tagNames.add(tag);
                }
            }
            if(cTags!= null){
                cTags.close();
            }


            city.setTags(tagNames);

            listCities.add(city);
        }

        if(c!= null){
            c.close();
        }

        Collections.sort(listCities);

        return listCities;
    }

    public City consult(City cityParam) {

        City city = null;

        return city;
    }

    public LinkedList<City> list(City cityParam) {

        LinkedList<City> listCity = null;

        return listCity;
    }

/*
    public LinkedList<City> getCitiesWithTags(City cityParam) {



        return null;
    }






    LinkedList<City> cities = new LinkedList<>();

    if (c.moveToFirst()) {
        do {
            City city = new City();
            city.setId(c.getInt(INDEX_CITY_ID));
            city.setTranslationFileKey(c.getString(INDEX_CITY_TRANSLATION_FILE_KEY));

            cities.add(city);
        }
        while (c.moveToNext());
    }

    Collections.sort(cities);
    if(cities.size() > 0){
        CityResultSearch cityResultSearch = cities.get(0);
        if(cityResultSearch.getDistance() <=50){
            cityResultSearch.setIsFirstCity(true);
        }
    }

    fillAdapter(cities);
*/
}
