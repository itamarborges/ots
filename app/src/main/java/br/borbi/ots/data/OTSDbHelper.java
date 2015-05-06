package br.borbi.ots.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import br.borbi.ots.data.OTSContract.City;
import br.borbi.ots.data.OTSContract.Country;
import br.borbi.ots.data.OTSContract.Language;
import br.borbi.ots.data.OTSContract.RelCityLanguage;
import br.borbi.ots.data.OTSContract.RelCityTag;
import br.borbi.ots.data.OTSContract.RelCountryLanguage;
import br.borbi.ots.data.OTSContract.RelSearchCity;
import br.borbi.ots.data.OTSContract.ResultSearch;
import br.borbi.ots.data.OTSContract.Search;
import br.borbi.ots.data.OTSContract.Tag;

/**
 * Created by Itamar on 24/03/2015.
 */
public class OTSDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "ots.db";
    private static final String LOG_TAG = "OTSDbHelper";


    public OTSDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TAG_TABLE = OTSContract.CREATE_TABLE + Tag.TABLE_NAME + " (" +
                Tag._ID + OTSContract.PRIMARY_KEY + " ," +
                Tag.COLUMN_NAME_RESOURCE_NAME + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL + OTSContract.UNIQUE_ON_CONFLICT_REPLACE + ");";

        final String SQL_CREATE_LANGUAGE_TABLE = OTSContract.CREATE_TABLE + Language.TABLE_NAME + " (" +
                Language._ID + OTSContract.PRIMARY_KEY + " ," +
                Language.COLUMN_NAME_NAME + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL + OTSContract.UNIQUE_ON_CONFLICT_REPLACE + ");";

        final String SQL_CREATE_SEARCH_TABLE = OTSContract.CREATE_TABLE + Search.TABLE_NAME + " (" +
                Search._ID + OTSContract.PRIMARY_KEY + " ," +
                Search.COLUMN_NAME_DATE_BEGIN + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_DATE_END + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_RADIUS + OTSContract.TYPE_REAL + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_MIN_SUNNY_DAYS + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_MIN_TEMPERATURE + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_DATETIME_LAST_SEARCH + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + " );";

        final String SQL_CREATE_REL_COUNTRY_LANGUAGE_TABLE = OTSContract.CREATE_TABLE + RelCountryLanguage.TABLE_NAME + " (" +
                RelCountryLanguage._ID + OTSContract.PRIMARY_KEY + " ," +
                RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                RelCountryLanguage.COLUMN_NAME_NAME + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL + OTSContract.UNIQUE_ON_CONFLICT_REPLACE + ", " +
                " FOREIGN KEY (" + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ") REFERENCES " +
                Language.TABLE_NAME + " (" + Language._ID + "));" ;

        final String SQL_CREATE_COUNTRY_TABLE = OTSContract.CREATE_TABLE + Country.TABLE_NAME + " (" +
                Country._ID + OTSContract.PRIMARY_KEY + " ," +
                Country.COLUMN_NAME_COUNTRY_CODE + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL + ", " +
                Country.COLUMN_NAME_NAME_ENGLISH + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL + OTSContract.UNIQUE_ON_CONFLICT_REPLACE + ", " +
                Country.COLUMN_NAME_REL_COUNTRY_LANGUAGE_ID + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                " FOREIGN KEY (" + Country.COLUMN_NAME_REL_COUNTRY_LANGUAGE_ID + ") REFERENCES " +
                RelCountryLanguage.TABLE_NAME+ " (" + RelCountryLanguage._ID + "));" ;

        final String SQL_CREATE_CITY_TABLE = OTSContract.CREATE_TABLE + City.TABLE_NAME + " (" +
                City._ID + OTSContract.PRIMARY_KEY + " ," +
                City.COLUMN_NAME_LATITUDE + OTSContract.TYPE_REAL + OTSContract.NOT_NULL + ", " +
                City.COLUMN_NAME_LONGITUDE + OTSContract.TYPE_REAL + OTSContract.NOT_NULL + ", " +
                City.COLUMN_NAME_LATITUDE_RAD + OTSContract.TYPE_REAL + OTSContract.NOT_NULL + ", " +
                City.COLUMN_NAME_LONGITUDE_RAD + OTSContract.TYPE_REAL + OTSContract.NOT_NULL + ", " +
                City.COLUMN_NAME_NAME_ENGLISH + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL + OTSContract.UNIQUE_ON_CONFLICT_REPLACE + ", " +
                City.COLUMN_NAME_COUNTRY_ID + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                " FOREIGN KEY (" + City.COLUMN_NAME_COUNTRY_ID + ") REFERENCES " +
                Country.TABLE_NAME + " (" + Country._ID + "));" ;

        final String SQL_CREATE_REL_CITY_TAG_TABLE = OTSContract.CREATE_TABLE + RelCityTag.TABLE_NAME + " (" +
                RelCityTag._ID + OTSContract.PRIMARY_KEY + " ," +
                RelCityTag.COLUMN_NAME_CITY_ID + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                RelCityTag.COLUMN_NAME_TAG_ID + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                " FOREIGN KEY (" + RelCityTag.COLUMN_NAME_CITY_ID + ") REFERENCES " +
                City.TABLE_NAME + " (" + City._ID + "), " +
                " FOREIGN KEY (" + RelCityTag.COLUMN_NAME_TAG_ID + ") REFERENCES " +
                Tag.TABLE_NAME + " (" + Tag._ID + "));" ;

        final String SQL_CREATE_REL_CITY_LANGUAGE_TABLE = OTSContract.CREATE_TABLE + RelCityLanguage.TABLE_NAME + " (" +
                RelCityLanguage._ID + OTSContract.PRIMARY_KEY + " ," +
                RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                RelCityLanguage.COLUMN_NAME_CITY_ID + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                RelCityLanguage.COLUMN_NAME_NAME + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL + OTSContract.UNIQUE_ON_CONFLICT_REPLACE + ", " +
                " FOREIGN KEY (" + RelCityLanguage.COLUMN_NAME_CITY_ID + ") REFERENCES " +
                City.TABLE_NAME + " (" + City._ID + "), " +
                " FOREIGN KEY (" + RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + ") REFERENCES " +
                Language.TABLE_NAME + " (" + Language._ID + "));" ;

        final String SQL_CREATE_REL_SEARCH_CITY_TABLE = OTSContract.CREATE_TABLE + RelSearchCity.TABLE_NAME + " (" +
                RelSearchCity._ID + OTSContract.PRIMARY_KEY + " ," +
                RelSearchCity.COLUMN_NAME_CITY_ID + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                RelSearchCity.COLUMN_NAME_SEARCH_ID + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                " FOREIGN KEY (" + RelSearchCity.COLUMN_NAME_CITY_ID + ") REFERENCES " +
                City.TABLE_NAME + " (" + City._ID + "), " +
                " FOREIGN KEY (" + RelSearchCity.COLUMN_NAME_SEARCH_ID + ") REFERENCES " +
                Search.TABLE_NAME + " (" + Search._ID + "));" ;

        final String SQL_CREATE_RESULT_SEARCH_TABLE = OTSContract.CREATE_TABLE + ResultSearch.TABLE_NAME + " (" +
                ResultSearch._ID + OTSContract.PRIMARY_KEY + " ," +
                ResultSearch.COLUMN_NAME_REL_SEARCH_CITY_ID + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                ResultSearch.COLUMN_NAME_DATE + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                ResultSearch.COLUMN_NAME_RESULTS + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL + ", " +
                " FOREIGN KEY (" + ResultSearch.COLUMN_NAME_REL_SEARCH_CITY_ID+ ") REFERENCES " +
                RelSearchCity.TABLE_NAME + " (" + RelSearchCity._ID + "));";

        Log.v(LOG_TAG, SQL_CREATE_TAG_TABLE);
        Log.v(LOG_TAG, SQL_CREATE_LANGUAGE_TABLE);
        Log.v(LOG_TAG, SQL_CREATE_SEARCH_TABLE);
        Log.v(LOG_TAG, SQL_CREATE_REL_COUNTRY_LANGUAGE_TABLE);
        Log.v(LOG_TAG, SQL_CREATE_COUNTRY_TABLE);
        Log.v(LOG_TAG, SQL_CREATE_CITY_TABLE);
        Log.v(LOG_TAG, SQL_CREATE_REL_CITY_TAG_TABLE);
        Log.v(LOG_TAG, SQL_CREATE_REL_CITY_LANGUAGE_TABLE);
        Log.v(LOG_TAG, SQL_CREATE_REL_SEARCH_CITY_TABLE);
        Log.v(LOG_TAG, SQL_CREATE_RESULT_SEARCH_TABLE);
        
        db.execSQL(SQL_CREATE_TAG_TABLE);
        db.execSQL(SQL_CREATE_LANGUAGE_TABLE);
        db.execSQL(SQL_CREATE_SEARCH_TABLE);
        db.execSQL(SQL_CREATE_REL_COUNTRY_LANGUAGE_TABLE);
        db.execSQL(SQL_CREATE_COUNTRY_TABLE);
        db.execSQL(SQL_CREATE_CITY_TABLE);
        db.execSQL(SQL_CREATE_REL_CITY_TAG_TABLE);
        db.execSQL(SQL_CREATE_REL_CITY_LANGUAGE_TABLE);
        db.execSQL(SQL_CREATE_REL_SEARCH_CITY_TABLE);
        db.execSQL(SQL_CREATE_RESULT_SEARCH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
