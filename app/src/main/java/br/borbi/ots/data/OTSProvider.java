package br.borbi.ots.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import br.borbi.ots.SearchActivity;
import br.borbi.ots.entity.Search;
import br.borbi.ots.enums.WeatherType;
import br.borbi.ots.pojo.City;
import br.borbi.ots.pojo.DayForecast;

/**
 * Created by Itamar on 08/04/2015.
 */
public class OTSProvider extends ContentProvider {

    static final int TAG = 100;
    static final int REL_CITY_TAG = 200;
    static final int CITY = 300;
    static final int LANGUAGE = 400;
    static final int REL_COUNTRY_LANGUAGE = 500;
    static final int REL_CITY_LANGUAGE = 600;
    static final int COUNTRY = 700;
    static final int SEARCH = 800;
    static final int REL_SEARCH_CITY = 900;
    static final int RESULT_SEARCH = 1000;
    static final int LIST_CITIES_BY_COORDINATES = 1100;
    static final int LIST_CITIES_BY_SEARCH = 1200;
    static final int LIST_TAGS_FROM_A_CITY = 1300;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String CLASS_NAME = OTSProvider.class.getName();
    private OTSDbHelper mOpenHelper;

    public static final String FILTER_BY_LOCALE=
            OTSContract.Language.TABLE_NAME+
                    "." + OTSContract.Language.COLUMN_NAME_LANGUAGE_CODE + " = ? ";

    public static final String FILTER_BY_CITY=
            OTSContract.City.TABLE_NAME+
                    "." + OTSContract.City._ID + " = ? ";

    public static final String FILTER_BY_SEARCH_ID=
            OTSContract.Search.TABLE_NAME+
                    "." + OTSContract.Search._ID + " = ? ";


    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = OTSContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, OTSContract.PATH_TAG, TAG);
        uriMatcher.addURI(authority, OTSContract.PATH_REL_CITY_TAG, REL_CITY_TAG);
        uriMatcher.addURI(authority, OTSContract.PATH_CITY, CITY);
        uriMatcher.addURI(authority, OTSContract.PATH_LANGUAGE, LANGUAGE);
        uriMatcher.addURI(authority, OTSContract.PATH_REL_COUNTRY_LANGUAGE, REL_COUNTRY_LANGUAGE);
        uriMatcher.addURI(authority, OTSContract.PATH_REL_CITY_LANGUAGE, REL_CITY_LANGUAGE);
        uriMatcher.addURI(authority, OTSContract.PATH_COUNTRY, COUNTRY);
        uriMatcher.addURI(authority, OTSContract.PATH_SEARCH, SEARCH);
        uriMatcher.addURI(authority, OTSContract.PATH_REL_SEARCH_CITY, REL_SEARCH_CITY);
        uriMatcher.addURI(authority, OTSContract.PATH_RESULT_SEARCH, RESULT_SEARCH);

        uriMatcher.addURI(authority, OTSContract.PATH_LIST_CITIES_BY_COORDINATES, LIST_CITIES_BY_COORDINATES);
        uriMatcher.addURI(authority, OTSContract.PATH_LIST_CITIES_BY_SEARCH, LIST_CITIES_BY_SEARCH);
        uriMatcher.addURI(authority, OTSContract.PATH_LIST_TAGS_FROM_A_CITY, LIST_TAGS_FROM_A_CITY);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new OTSDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;
        switch (sUriMatcher.match(uri)) {
            case TAG: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OTSContract.Tag.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case REL_CITY_TAG: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OTSContract.RelCityTag.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case CITY: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OTSContract.City.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case LANGUAGE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OTSContract.Language.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case REL_COUNTRY_LANGUAGE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OTSContract.RelCountryLanguage.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case REL_CITY_LANGUAGE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OTSContract.RelCityLanguage.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case COUNTRY: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OTSContract.Country.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case SEARCH: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OTSContract.Search.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case REL_SEARCH_CITY: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OTSContract.RelSearchCity.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case RESULT_SEARCH: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OTSContract.ResultSearch.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case LIST_CITIES_BY_COORDINATES: {
                retCursor = listCitiesByCoordinates(projection, selection, selectionArgs);
                break;
            }
            case LIST_CITIES_BY_SEARCH: {
                retCursor = listCitiesBySearch(projection, selection, selectionArgs);
                break;
            }
            case LIST_TAGS_FROM_A_CITY: {
                retCursor = listTagsFromACity(projection, selection, selectionArgs);
                break;
            }
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;

    }

    @Override
    public String getType(Uri uri) {        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case TAG:
                return OTSContract.Tag.CONTENT_TYPE;
            case REL_CITY_TAG:
                return OTSContract.RelCityTag.CONTENT_TYPE;
            case CITY:
                return OTSContract.City.CONTENT_TYPE;
            case LANGUAGE:
                return OTSContract.Language.CONTENT_TYPE;
            case REL_COUNTRY_LANGUAGE:
                return OTSContract.RelCountryLanguage.CONTENT_TYPE;
            case REL_CITY_LANGUAGE:
                return OTSContract.RelCityLanguage.CONTENT_TYPE;
            case COUNTRY:
                return OTSContract.Country.CONTENT_TYPE;
            case SEARCH:
                return OTSContract.Search.CONTENT_TYPE;
            case REL_SEARCH_CITY:
                return OTSContract.RelSearchCity.CONTENT_TYPE;
            case RESULT_SEARCH:
                return OTSContract.ResultSearch.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TAG: {
                long _id = db.insert(OTSContract.Tag.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = OTSContract.Tag.buildTagUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REL_CITY_TAG: {
                long _id = db.insert(OTSContract.RelCityTag.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = OTSContract.RelCityTag.buildRelCityTagUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CITY: {
                long _id = db.insert(OTSContract.City.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = OTSContract.City.buildCityUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case LANGUAGE: {
                long _id = db.insert(OTSContract.Language.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = OTSContract.Language.buildLanguageUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REL_COUNTRY_LANGUAGE: {
                long _id = db.insert(OTSContract.RelCountryLanguage.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = OTSContract.RelCountryLanguage.buildRelCountryLanguageUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REL_CITY_LANGUAGE: {
                long _id = db.insert(OTSContract.RelCityLanguage.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = OTSContract.RelCityLanguage.buildRelCityLanguageUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case COUNTRY: {
                long _id = db.insert(OTSContract.Country.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = OTSContract.Country.buildCountryUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case SEARCH: {
                long _id = db.insert(OTSContract.Search.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = OTSContract.Search.buildSearchUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REL_SEARCH_CITY: {
                long _id = db.insert(OTSContract.RelSearchCity.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = OTSContract.RelSearchCity.buildRelSearchCityUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case RESULT_SEARCH: {
                long _id = db.insert(OTSContract.ResultSearch.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = OTSContract.ResultSearch.buildResultSearchUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case TAG:
                rowsDeleted = db.delete(
                        OTSContract.Tag.TABLE_NAME, selection, selectionArgs);
                break;
            case REL_CITY_TAG:
                rowsDeleted = db.delete(
                        OTSContract.RelCityTag.TABLE_NAME, selection, selectionArgs);
                break;
            case CITY:
                rowsDeleted = db.delete(
                        OTSContract.City.TABLE_NAME, selection, selectionArgs);
                break;
            case LANGUAGE:
                rowsDeleted = db.delete(
                        OTSContract.Language.TABLE_NAME, selection, selectionArgs);
                break;
            case REL_COUNTRY_LANGUAGE:
                rowsDeleted = db.delete(
                        OTSContract.RelCountryLanguage.TABLE_NAME, selection, selectionArgs);
                break;
            case REL_CITY_LANGUAGE:
                rowsDeleted = db.delete(
                        OTSContract.RelCityLanguage.TABLE_NAME, selection, selectionArgs);
                break;
            case COUNTRY:
                rowsDeleted = db.delete(
                        OTSContract.Country.TABLE_NAME, selection, selectionArgs);
                break;
            case SEARCH:
                rowsDeleted = db.delete(
                        OTSContract.Search.TABLE_NAME, selection, selectionArgs);
                break;
            case REL_SEARCH_CITY:
                rowsDeleted = db.delete(
                        OTSContract.RelSearchCity.TABLE_NAME, selection, selectionArgs);
                break;
            case RESULT_SEARCH:
                rowsDeleted = db.delete(
                        OTSContract.ResultSearch.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case TAG:
                rowsUpdated = db.update(OTSContract.Tag.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case REL_CITY_TAG:
                rowsUpdated = db.update(OTSContract.RelCityTag.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case CITY:
                rowsUpdated = db.update(OTSContract.City.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case LANGUAGE:
                rowsUpdated = db.update(OTSContract.Language.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case REL_COUNTRY_LANGUAGE:
                rowsUpdated = db.update(OTSContract.RelCountryLanguage.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case REL_CITY_LANGUAGE:
                rowsUpdated = db.update(OTSContract.RelCityLanguage.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case COUNTRY:
                rowsUpdated = db.update(OTSContract.Country.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case SEARCH:
                rowsUpdated = db.update(OTSContract.Search.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case REL_SEARCH_CITY:
                rowsUpdated = db.update(OTSContract.RelSearchCity.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case RESULT_SEARCH:
                rowsUpdated = db.update(OTSContract.ResultSearch.TABLE_NAME, values, selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;

    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TAG:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(OTSContract.Tag.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    public Cursor listTagsFromACity(String[] projection, String selection, String[] selectionArgs) {
        SQLiteQueryBuilder sTagsFromACityQueryBuilder = new SQLiteQueryBuilder();

        sTagsFromACityQueryBuilder.setTables(
                OTSContract.City.TABLE_NAME + " INNER JOIN " +
                        OTSContract.RelCityTag.TABLE_NAME +
                        " ON " + OTSContract.City.TABLE_NAME +
                        "." + OTSContract.City._ID +
                        " = " + OTSContract.RelCityTag.TABLE_NAME +
                        "." + OTSContract.RelCityTag.COLUMN_NAME_CITY_ID +
                        " INNER JOIN " +
                        OTSContract.Tag.TABLE_NAME +
                        " ON " + OTSContract.Tag.TABLE_NAME +
                        "." + OTSContract.Tag._ID +
                        " = " + OTSContract.RelCityTag.TABLE_NAME +
                        "." + OTSContract.RelCityTag.COLUMN_NAME_TAG_ID);


        /*
        Log.v(CLASS_NAME, "=== projection = ");
        printArray(projection);
        Log.v(CLASS_NAME, "==== selection = " + selection);
        Log.v(CLASS_NAME, "==== selectionArgs = ");
        printArray(selectionArgs);
        Log.v(CLASS_NAME, "tables = " + sWeatherByLocationSettingQueryBuilder.getTables());
        */

        return sTagsFromACityQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

    }

    public Cursor listCitiesByCoordinates(String[] projection, String selection, String[] selectionArgs) {
        SQLiteQueryBuilder sWeatherByLocationSettingQueryBuilder = new SQLiteQueryBuilder();

        sWeatherByLocationSettingQueryBuilder.setTables(
                OTSContract.City.TABLE_NAME + " INNER JOIN " +
                        OTSContract.Country.TABLE_NAME +
                        " ON " + OTSContract.Country.TABLE_NAME +
                        "." + OTSContract.Country._ID +
                        " = " + OTSContract.City.TABLE_NAME +
                        "." + OTSContract.City.COLUMN_NAME_COUNTRY_ID);


        /*
        Log.v(CLASS_NAME, "=== projection = ");
        printArray(projection);
        Log.v(CLASS_NAME, "==== selection = " + selection);
        Log.v(CLASS_NAME, "==== selectionArgs = ");
        printArray(selectionArgs);
        Log.v(CLASS_NAME, "tables = " + sWeatherByLocationSettingQueryBuilder.getTables());
        */

        return sWeatherByLocationSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

    }

    public Cursor listCitiesBySearch(String[] projection, String selection, String[] selectionArgs) {
        SQLiteQueryBuilder sWeatherBySearchQueryBuilder = new SQLiteQueryBuilder();
        /*
        select rel_country_language.name, rel_city_language.name,
rel_search_city.search_id,
rel_search_city._id
from search INNER JOIN rel_search_city ON search._id = rel_search_city.search_id INNER JOIN city ON city._id = rel_search_city.city_id INNER JOIN rel_city_language ON rel_city_language.city_id = city._id INNER JOIN country ON country._id = city.country_id INNER JOIN rel_country_language ON country._id = rel_country_language.country_id INNER JOIN language ON (language._id = rel_country_language.language_id AND language._id = rel_city_language.language_id) where language.language_code = 'por'

         */


        sWeatherBySearchQueryBuilder.setTables(
                OTSContract.Search.TABLE_NAME + " INNER JOIN " +
                        OTSContract.RelSearchCity.TABLE_NAME +
                        " ON " + OTSContract.Search.TABLE_NAME +
                        "." + OTSContract.Search._ID +
                        " = " + OTSContract.RelSearchCity.TABLE_NAME +
                        "." + OTSContract.RelSearchCity.COLUMN_NAME_SEARCH_ID +
                        " INNER JOIN " +
                        OTSContract.City.TABLE_NAME +
                        " ON " + OTSContract.City.TABLE_NAME +
                        "." + OTSContract.City._ID +
                        " = " + OTSContract.RelSearchCity.TABLE_NAME +
                        "." + OTSContract.RelSearchCity.COLUMN_NAME_CITY_ID +
                        " INNER JOIN " +
                        OTSContract.RelCityLanguage.TABLE_NAME +
                        " ON " + OTSContract.RelCityLanguage.TABLE_NAME +
                        "." + OTSContract.RelCityLanguage.COLUMN_NAME_CITY_ID +
                        " = " + OTSContract.City.TABLE_NAME +
                        "." + OTSContract.City._ID +
                        " INNER JOIN " +
                        OTSContract.Country.TABLE_NAME +
                        " ON " + OTSContract.Country.TABLE_NAME +
                        "." + OTSContract.Country._ID +
                        " = " + OTSContract.City.TABLE_NAME +
                        "." + OTSContract.City.COLUMN_NAME_COUNTRY_ID +
                        " INNER JOIN " +
                        OTSContract.RelCountryLanguage.TABLE_NAME +
                        " ON " + OTSContract.Country.TABLE_NAME +
                        "." + OTSContract.Country._ID +
                        " = " + OTSContract.RelCountryLanguage.TABLE_NAME +
                        "." + OTSContract.RelCountryLanguage.COLUMN_NAME_COUNTRY_ID +
                        " INNER JOIN " +
                        OTSContract.Language.TABLE_NAME +
                        " ON ("
                        + OTSContract.Language.TABLE_NAME +
                        "." + OTSContract.Language._ID +
                        " = " + OTSContract.RelCountryLanguage.TABLE_NAME +
                        "." + OTSContract.RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID +
                        " AND " +
                        OTSContract.Language.TABLE_NAME +
                        "." + OTSContract.Language._ID +
                        " = " + OTSContract.RelCityLanguage.TABLE_NAME +
                        "." + OTSContract.RelCityLanguage.COLUMN_NAME_LANGUAGE_ID+")");


        Log.v(CLASS_NAME, "=== projection = ");
        printArray(projection);
        Log.v(CLASS_NAME, "==== selection = " + selection);
        Log.v(CLASS_NAME, "==== selectionArgs = ");
        printArray(selectionArgs);
        Log.v(CLASS_NAME, "tables = " + sWeatherBySearchQueryBuilder.getTables());

        return sWeatherBySearchQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }


    @Override
    public Bundle call(String method, String arg, Bundle extras) {

        Bundle bundle = null;

        Log.v(CLASS_NAME, "entrou no call, method = " + method);
        if (OTSContract.METHOD_SAVE_SEARCH.equals(method)) {
            Search search = (Search) extras.getSerializable(SearchActivity.SEARCH);
            bundle = insertSearch(search);
        }

        //return super.call(method, arg, extras);
        return bundle;
    }

    private Bundle insertSearch(Search search) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // Apaga pesquisas ja existentes

        delete(OTSContract.Search.CONTENT_URI,null,null);

        long searchId = 0;

        try {
            //Insere search;
            ContentValues searchValues = new ContentValues();
            searchValues.put(OTSContract.Search.COLUMN_NAME_DATE_BEGIN, search.getBeginDate().getTime());
            searchValues.put(OTSContract.Search.COLUMN_NAME_DATE_END, search.getEndDate().getTime());
            searchValues.put(OTSContract.Search.COLUMN_NAME_RADIUS, search.getRadius());
            searchValues.put(OTSContract.Search.COLUMN_NAME_MIN_SUNNY_DAYS, search.getMinSunnyDays());
            searchValues.put(OTSContract.Search.COLUMN_NAME_MIN_TEMPERATURE, search.getMinTemperature());
            searchValues.put(OTSContract.Search.COLUMN_NAME_MIN_SUNNY_DAYS, search.getMinSunnyDays());
            searchValues.put(OTSContract.Search.COLUMN_NAME_DATETIME_LAST_SEARCH, search.getDateTimeLastSearch().getTime());
            searchValues.put(OTSContract.Search.COLUMN_NAME_ORIGIN_LAT, search.getOriginLatitude());
            searchValues.put(OTSContract.Search.COLUMN_NAME_ORIGIN_LONG, search.getOriginLongitude());


            db.beginTransaction();
            searchId = db.insert(OTSContract.Search.TABLE_NAME, null, searchValues);

            if (searchId <= 0) {
                throw new android.database.SQLException("Failed to insert row into search");
            }

            List<City> cities = search.getCites();

            for (City city : cities) {
                ContentValues relSearchCityValues = new ContentValues();
                relSearchCityValues.put(OTSContract.RelSearchCity.COLUMN_NAME_CITY_ID, city.getId());
                relSearchCityValues.put(OTSContract.RelSearchCity.COLUMN_NAME_SEARCH_ID, searchId);

                long relSearchCityId = db.insert(OTSContract.RelSearchCity.TABLE_NAME, null, relSearchCityValues);

                for (DayForecast dayForecast : city.getDayForecasts()) {
                    ContentValues resultSearchValues = new ContentValues();
                    resultSearchValues.put(OTSContract.ResultSearch.COLUMN_NAME_DATE, dayForecast.getDate().getTime());
                    resultSearchValues.put(OTSContract.ResultSearch.COLUMN_NAME_MINIMUM_TEMPERATURE, dayForecast.getMinTemperature());
                    resultSearchValues.put(OTSContract.ResultSearch.COLUMN_NAME_MAXIMUM_TEMPERATURE, dayForecast.getMaxTemperature());
                    resultSearchValues.put(OTSContract.ResultSearch.COLUMN_NAME_MORNING_TEMPERATURE, dayForecast.getMorningTemperature());
                    resultSearchValues.put(OTSContract.ResultSearch.COLUMN_NAME_EVENING_TEMPERATURE, dayForecast.getEveningTemperature());
                    resultSearchValues.put(OTSContract.ResultSearch.COLUMN_NAME_NIGHT_TEMPERATURE, dayForecast.getNightTemperature());
                    resultSearchValues.put(OTSContract.ResultSearch.COLUMN_NAME_WEATHER_TYPE, WeatherType.getId(dayForecast.getWeatherType()));
                    resultSearchValues.put(OTSContract.ResultSearch.COLUMN_NAME_REL_SEARCH_CITY_ID, relSearchCityId);
                    resultSearchValues.put(OTSContract.ResultSearch.COLUMN_NAME_HUMIDITY, dayForecast.getHumidity());
                    resultSearchValues.put(OTSContract.ResultSearch.COLUMN_NAME_PRECIPITATION, dayForecast.getPrecipitation());
                    db.insert(OTSContract.ResultSearch.TABLE_NAME, null, resultSearchValues);
                }
            }

            db.setTransactionSuccessful();

        } catch (Exception e) {

        } finally {
            db.endTransaction();
        }

        Bundle bundle = new Bundle();
        bundle.putLong(SearchActivity.SEARCH, searchId);

        return bundle;
    }


    private void normalizeDate(ContentValues values) {
        // normalize the date value
        if (values.containsKey(OTSContract.Search.COLUMN_NAME_DATE_BEGIN) || values.containsKey(OTSContract.Search.COLUMN_NAME_DATE_BEGIN)) {
            long dateValue = values.getAsLong(OTSContract.Search.COLUMN_NAME_DATE_BEGIN);
            values.put(OTSContract.Search.COLUMN_NAME_DATE_BEGIN, normalizeDate(dateValue));
        }
    }


    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    private long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(startDate);
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }


    private void printArray(String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            Log.v(CLASS_NAME, arr[i]);
        }
    }


}
