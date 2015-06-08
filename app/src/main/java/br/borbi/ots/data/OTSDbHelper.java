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
    private static final int DATABASE_VERSION = 27;

    private static final String DATABASE_NAME = "ots.db";
    private static final String LOG_TAG = "OTSDbHelper";

    private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";


    public OTSDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TAG_TABLE = OTSContract.CREATE_TABLE + Tag.TABLE_NAME + " (" +
                Tag._ID + OTSContract.PRIMARY_KEY + " ," +
                Tag.COLUMN_NAME_RESOURCE_NAME + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL + OTSContract.UNIQUE_ON_CONFLICT_REPLACE + ");";

        final String SQL_CREATE_LANGUAGE_TABLE = OTSContract.CREATE_TABLE + Language.TABLE_NAME + " (" +
                Language._ID + OTSContract.PRIMARY_KEY + " ," +
                Language.COLUMN_NAME_NAME + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL + OTSContract.UNIQUE_ON_CONFLICT_REPLACE + " ," +
                Language.COLUMN_NAME_LANGUAGE_CODE + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL + OTSContract.UNIQUE_ON_CONFLICT_REPLACE + ");";

        final String SQL_CREATE_SEARCH_TABLE = OTSContract.CREATE_TABLE + Search.TABLE_NAME + " (" +
                Search._ID + OTSContract.PRIMARY_KEY + " ," +
                Search.COLUMN_NAME_DATE_BEGIN + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_DATE_END + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_RADIUS + OTSContract.TYPE_REAL + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_MIN_TEMPERATURE + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_ORIGIN_LAT + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_ORIGIN_LONG + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_MIN_SUNNY_DAYS + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_DATETIME_LAST_SEARCH + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + " );";

        final String SQL_CREATE_COUNTRY_TABLE = OTSContract.CREATE_TABLE + Country.TABLE_NAME + " (" +
                Country._ID + OTSContract.PRIMARY_KEY + " ," +
                Country.COLUMN_NAME_COUNTRY_CODE + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL + ", " +
                Country.COLUMN_NAME_NAME_ENGLISH + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL + OTSContract.UNIQUE_ON_CONFLICT_REPLACE + ");, ";

        final String SQL_CREATE_REL_COUNTRY_LANGUAGE_TABLE = OTSContract.CREATE_TABLE + RelCountryLanguage.TABLE_NAME + " (" +
                RelCountryLanguage._ID + OTSContract.PRIMARY_KEY + " ," +
                RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                RelCountryLanguage.COLUMN_NAME_NAME + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL + OTSContract.UNIQUE_ON_CONFLICT_REPLACE + ", " +
                RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                " FOREIGN KEY (" + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ") REFERENCES " +
                Country.TABLE_NAME + " (" + Country._ID + "), " +
                " FOREIGN KEY (" + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ") REFERENCES " +
                Language.TABLE_NAME + " (" + Language._ID + "));" ;

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
                ResultSearch.COLUMN_NAME_MINIMUM_TEMPERATURE + OTSContract.TYPE_REAL + OTSContract.NOT_NULL + ", " +
                ResultSearch.COLUMN_NAME_MAXIMUM_TEMPERATURE + OTSContract.TYPE_REAL + OTSContract.NOT_NULL + ", " +
                ResultSearch.COLUMN_NAME_WEATHER_TYPE + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
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
        db.execSQL(SQL_CREATE_COUNTRY_TABLE);
        db.execSQL(SQL_CREATE_REL_COUNTRY_LANGUAGE_TABLE);
        db.execSQL(SQL_CREATE_CITY_TABLE);
        db.execSQL(SQL_CREATE_REL_CITY_TAG_TABLE);
        db.execSQL(SQL_CREATE_REL_CITY_LANGUAGE_TABLE);
        db.execSQL(SQL_CREATE_REL_SEARCH_CITY_TABLE);
        db.execSQL(SQL_CREATE_RESULT_SEARCH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.v(LOG_TAG, "vai entrar no onUpgrade");

        db.execSQL(DROP_TABLE_IF_EXISTS + Tag.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + Language.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + Search.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + Country.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + RelCountryLanguage.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + City.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + RelCityTag.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + RelCityLanguage.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + RelSearchCity.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + ResultSearch.TABLE_NAME);

        Log.v(LOG_TAG, "vai entrar no oncreate");

        onCreate(db);

        Log.v(LOG_TAG, "encerrou o create");

        //Inicializations Scripts - TAG
        db.execSQL("INSERT INTO " + Tag.TABLE_NAME + "(" + Tag._ID + ", " + Tag.COLUMN_NAME_RESOURCE_NAME + ") VALUES (100000, 'beach');");
        db.execSQL("INSERT INTO " + Tag.TABLE_NAME + "(" + Tag._ID + ", " + Tag.COLUMN_NAME_RESOURCE_NAME + ") VALUES (100001, 'snow');");
        db.execSQL("INSERT INTO " + Tag.TABLE_NAME + "(" + Tag._ID + ", " + Tag.COLUMN_NAME_RESOURCE_NAME + ") VALUES (100002, 'history');");

        Log.v(LOG_TAG, "primeiros inserts executaram");
        //Inicializations Scripts - LANGUAGE
        db.execSQL("INSERT INTO " + Language.TABLE_NAME + "(" + Language._ID + ", " + Language.COLUMN_NAME_NAME + ", " + Language.COLUMN_NAME_LANGUAGE_CODE + ") VALUES (" + OTSContract.LANGUAGE_ID_POR + ", 'Portuguese', '" + OTSContract.LANGUAGE_CODE_POR + "');");
        db.execSQL("INSERT INTO " + Language.TABLE_NAME + "(" + Language._ID + ", " + Language.COLUMN_NAME_NAME + ", " + Language.COLUMN_NAME_LANGUAGE_CODE + ") VALUES (" + OTSContract.LANGUAGE_ID_ENG + ", 'English', '" + OTSContract.LANGUAGE_CODE_ENG + "');");
        db.execSQL("INSERT INTO " + Language.TABLE_NAME + "(" + Language._ID + ", " + Language.COLUMN_NAME_NAME + ", " + Language.COLUMN_NAME_LANGUAGE_CODE + ") VALUES (" + OTSContract.LANGUAGE_ID_FRA + ", 'French', '" + OTSContract.LANGUAGE_CODE_FRA + "');");

        //Inicializations Scripts - SEARCH
        //NULL

        //Inicializations Scripts - COUNTRY

        db.execSQL("INSERT INTO " + Country.TABLE_NAME + "(" + Country._ID + ", " + Country.COLUMN_NAME_COUNTRY_CODE + ", " + Country.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100000, 'BR', 'Brazil');");
        db.execSQL("INSERT INTO " + Country.TABLE_NAME + "(" + Country._ID + ", " + Country.COLUMN_NAME_COUNTRY_CODE + ", " + Country.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100001, 'US', 'United States of America');");
        db.execSQL("INSERT INTO " + Country.TABLE_NAME + "(" + Country._ID + ", " + Country.COLUMN_NAME_COUNTRY_CODE + ", " + Country.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100002, 'FR', 'France');");
        db.execSQL("INSERT INTO " + Country.TABLE_NAME + "(" + Country._ID + ", " + Country.COLUMN_NAME_COUNTRY_CODE + ", " + Country.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100003, 'CA', 'Canada');");

        //Inicializations Scripts - REL_COUNTRY_LANGUAGE

        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (100000, 100000, " + OTSContract.LANGUAGE_ID_POR + ", 'Brasil');");
        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (100001, 100000, " + OTSContract.LANGUAGE_ID_ENG + ", 'Brazil');");
        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (100002, 100000, " + OTSContract.LANGUAGE_ID_FRA + ", 'BrÃ©sil');");

        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (200000, 100001, " + OTSContract.LANGUAGE_ID_POR + ", 'Estados Unidos');");
        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (200001, 100001, " + OTSContract.LANGUAGE_ID_ENG + ", 'United States');");
        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (200002, 100001, " + OTSContract.LANGUAGE_ID_FRA + ", 'Ã‰tats Unis');");

        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (300000, 100002, " + OTSContract.LANGUAGE_ID_POR + ", 'FranÃ§a');");
        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (300001, 100002, " + OTSContract.LANGUAGE_ID_ENG + ", 'France');");
        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (300002, 100002, " + OTSContract.LANGUAGE_ID_FRA + ", 'France');");

        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (400000, 100003, " + OTSContract.LANGUAGE_ID_POR + ", 'CanadÃ¡');");
        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (400001, 100003, " + OTSContract.LANGUAGE_ID_ENG + ", 'Canada');");
        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (400002, 100003, " + OTSContract.LANGUAGE_ID_FRA + ", 'Canada');");

        //Inicializations Scripts - CITY

        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100000,100000,-1.45,-0.0253072742,-48.48333333,-0.846193799,'Belem');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100001,100000,-19.93333333,-0.3479022976,-43.95,-0.7670722063,'Belo Horizonte');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100002,100000,-15.86666667,-0.2769255746,-47.91666667,-0.8363035999,'Brasilia');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100003,100000,-25.41666667,-0.4436045182,-49.28333333,-0.860156433,'Curitiba');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100004,100000,-3.766666667,-0.0657407352,-38.55,-0.6728244266,'Fortaleza');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100005,100000,-30.03333333,-0.524180552,-51.21666667,-0.8938994652,'Porto Alegre');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100006,100000,-8.066666667,-0.140789893,-34.88333333,-0.6088290207,'Recife');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100007,100000,-22.91666667,-0.3999712869,-43.2,-0.7539822369,'Rio De Janeiro');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100008,100000,-13,-0.2268928028,-38.5,-0.671951762,'Salvador');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100009,100000,-23.55,-0.4110250388,-46.63333333,-0.8139052078,'Sao Paulo');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100010,100001,40.65,0.7094763409,-73.78333333,-1.2877621,'New York');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100011,100002,48.85,0.8525933396,2.33,0.04066617157,'Paris');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100012,100003,45.508839,0.7942790793,-73.587807,-1.284349521,'Montreal');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100013,100000,-28.66666667,-0.5003277189,-48.28333333,-0.8427031405,'Torres');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100014,100000,-28.26666667,-0.4933464019,-50,-0.872664626,'Capao da Canoa');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100015,100000,-28.31666667,-0.4942190665,-52.2,-0.9110618695,'Santa Maria');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100016,100000,-30.23333333,-0.5276712105,-51.66666667,-0.9017534469,'Pelotas');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100017,100000,-31.96666667,-0.5579235842,-51.91666667,-0.90611677,'Rio Grande');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100018,100000,-27.71666667,-0.483747091,-53.75,-0.9381144729,'Santo Angelo');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100019,100000,-28.63333333,-0.4997459425,-49.13333333,-0.8575384391,'Gramado');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100020,100000,-27.15,-0.4738568919,-50.73333333,-0.8854637072,'Antonio Prado');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100021,100000,-27.5,-0.4799655443,-49.06666667,-0.8563748863,'Vacaria');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100022,100000,-26.41666667,-0.4610578107,-47.46666667,-0.8284496183,'Florianopolis');");


        //Inicializations Scripts - REL_CITY_TAG
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100000,100000,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100001,100000,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100002,100001,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100003,100002,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100004,100003,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100005,100004,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100006,100004,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100007,100005,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100008,100006,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100009,100006,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100010,100007,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100011,100007,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100012,100008,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100013,100008,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100014,100009,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100015,100010,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100016,100010,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100017,100010,100001);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100018,100011,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100019,100011,100001);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100020,100012,100001);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100021,100013,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100022,100014,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100023,100015,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100024,100016,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100025,100017,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100026,100018,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100027,100019,100001);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100028,100020,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100029,100021,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100030,100022,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100031,100022,100000);");

        //Inicializations Scripts - REL_CITY_LANGUAGE
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100000,100000," + OTSContract.LANGUAGE_ID_POR + ",'Belem');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100001,100001," + OTSContract.LANGUAGE_ID_POR + ",'Belo Horizonte');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100002,100002," + OTSContract.LANGUAGE_ID_POR + ",'Brasilia');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100003,100003," + OTSContract.LANGUAGE_ID_POR + ",'Curitiba');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100004,100004," + OTSContract.LANGUAGE_ID_POR + ",'Fortaleza');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100005,100005," + OTSContract.LANGUAGE_ID_POR + ",'Porto Alegre');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100006,100006," + OTSContract.LANGUAGE_ID_POR + ",'Recife');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100007,100007," + OTSContract.LANGUAGE_ID_POR + ",'Rio De Janeiro');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100008,100008," + OTSContract.LANGUAGE_ID_POR + ",'Salvador');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100009,100009," + OTSContract.LANGUAGE_ID_POR + ",'Sao Paulo');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100010,100010," + OTSContract.LANGUAGE_ID_POR + ",'New York');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100011,100011," + OTSContract.LANGUAGE_ID_POR + ",'Paris');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100012,100012," + OTSContract.LANGUAGE_ID_POR + ",'Montreal');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100013,100013," + OTSContract.LANGUAGE_ID_POR + ",'Torres');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100014,100014," + OTSContract.LANGUAGE_ID_POR + ",'CapÃ£o da Canoa');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100015,100015," + OTSContract.LANGUAGE_ID_POR + ",'Santa Maria');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100016,100016," + OTSContract.LANGUAGE_ID_POR + ",'Pelotas');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100017,100017," + OTSContract.LANGUAGE_ID_POR + ",'Rio Grande');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100018,100018," + OTSContract.LANGUAGE_ID_POR + ",'Santo Ã‚ngelo');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100019,100019," + OTSContract.LANGUAGE_ID_POR + ",'Gramado');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100020,100020," + OTSContract.LANGUAGE_ID_POR + ",'AntÃ´nio Prado');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100021,100021," + OTSContract.LANGUAGE_ID_POR + ",'Vacaria');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100022,100022," + OTSContract.LANGUAGE_ID_POR + ",'FlorianÃ³polis');");


        Log.v(LOG_TAG, "inseriu tudo");
        //Inicializations Scripts - REL_SEARCH_CITY
        //NULL

        //Inicializations Scripts - RESULT_SEARCH
        //NULL



    }

}
