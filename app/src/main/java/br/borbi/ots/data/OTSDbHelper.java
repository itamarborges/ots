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

    private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";


    public OTSDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
        super.onConfigure(db);
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
                //City.COLUMN_NAME_NAME_ENGLISH + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL + OTSContract.UNIQUE_ON_CONFLICT_REPLACE + ", " +
                City.COLUMN_NAME_NAME_ENGLISH + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL+ ", " +
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
                //RelCityLanguage.COLUMN_NAME_NAME + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL + OTSContract.UNIQUE_ON_CONFLICT_REPLACE + ", " +
                RelCityLanguage.COLUMN_NAME_NAME + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL + ", " +
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
                Search.TABLE_NAME + " (" + Search._ID + ") ON DELETE CASCADE);" ;

        final String SQL_CREATE_RESULT_SEARCH_TABLE = OTSContract.CREATE_TABLE + ResultSearch.TABLE_NAME + " (" +
                ResultSearch._ID + OTSContract.PRIMARY_KEY + " ," +
                ResultSearch.COLUMN_NAME_REL_SEARCH_CITY_ID + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                ResultSearch.COLUMN_NAME_DATE + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                ResultSearch.COLUMN_NAME_MINIMUM_TEMPERATURE + OTSContract.TYPE_REAL + OTSContract.NOT_NULL + ", " +
                ResultSearch.COLUMN_NAME_MAXIMUM_TEMPERATURE + OTSContract.TYPE_REAL + OTSContract.NOT_NULL + ", " +
                ResultSearch.COLUMN_NAME_MORNING_TEMPERATURE + OTSContract.TYPE_REAL + OTSContract.NOT_NULL + ", " +
                ResultSearch.COLUMN_NAME_EVENING_TEMPERATURE + OTSContract.TYPE_REAL + OTSContract.NOT_NULL + ", " +
                ResultSearch.COLUMN_NAME_NIGHT_TEMPERATURE + OTSContract.TYPE_REAL + OTSContract.NOT_NULL + ", " +
                ResultSearch.COLUMN_NAME_WEATHER_TYPE + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                ResultSearch.COLUMN_NAME_HUMIDITY + OTSContract.TYPE_REAL + OTSContract.NOT_NULL + ", " +
                ResultSearch.COLUMN_NAME_PRECIPITATION + OTSContract.TYPE_REAL + ", " +
                " FOREIGN KEY (" + ResultSearch.COLUMN_NAME_REL_SEARCH_CITY_ID+ ") REFERENCES " +
                RelSearchCity.TABLE_NAME + " (" + RelSearchCity._ID + ") ON DELETE CASCADE);";

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

        //Inicializations Scripts - TAG
        db.execSQL("INSERT INTO " + Tag.TABLE_NAME + "(" + Tag._ID + ", " + Tag.COLUMN_NAME_RESOURCE_NAME + ") VALUES (100000, 'beach');");
        db.execSQL("INSERT INTO " + Tag.TABLE_NAME + "(" + Tag._ID + ", " + Tag.COLUMN_NAME_RESOURCE_NAME + ") VALUES (100001, 'snow');");
        db.execSQL("INSERT INTO " + Tag.TABLE_NAME + "(" + Tag._ID + ", " + Tag.COLUMN_NAME_RESOURCE_NAME + ") VALUES (100002, 'historic');");

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
        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (100002, 100000, " + OTSContract.LANGUAGE_ID_FRA + ", 'Brésil');");

        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (200000, 100001, " + OTSContract.LANGUAGE_ID_POR + ", 'Estados Unidos');");
        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (200001, 100001, " + OTSContract.LANGUAGE_ID_ENG + ", 'United States');");
        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (200002, 100001, " + OTSContract.LANGUAGE_ID_FRA + ", 'États Unis');");

        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (300000, 100002, " + OTSContract.LANGUAGE_ID_POR + ", 'França');");
        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (300001, 100002, " + OTSContract.LANGUAGE_ID_ENG + ", 'France');");
        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (300002, 100002, " + OTSContract.LANGUAGE_ID_FRA + ", 'France');");

        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (400000, 100003, " + OTSContract.LANGUAGE_ID_POR + ", 'Canadá');");
        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (400001, 100003, " + OTSContract.LANGUAGE_ID_ENG + ", 'Canada');");
        db.execSQL("INSERT INTO " + RelCountryLanguage.TABLE_NAME + "(" + RelCountryLanguage._ID + ", " + RelCountryLanguage.COLUMN_NAME_COUNTRY_ID + ", " + RelCountryLanguage.COLUMN_NAME_LANGUAGE_ID + ", " + RelCountryLanguage.COLUMN_NAME_NAME + ") VALUES (400002, 100003, " + OTSContract.LANGUAGE_ID_FRA + ", 'Canada');");

        //Inicializations Scripts - CITY
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100023,100000,-23.006670,-0.4610578107,-44.318062,-0.8284496183,'Angra dos Reis');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100020,100000,-28.857266,-0.4738568919,-51.283276,-0.8854637072,'Antonio Prado');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100024,100000,-10.911110,-0.4610578107,-37.071671,-0.8284496183,'Aracaju');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100025,100000,-22.746941,-0.4610578107,-41.881672,-0.8284496183,'Armacao de Buzios');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100026,100000,-22.966110,-0.4610578107,-42.027779,-0.8284496183,'Arraial do Cabo');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100027,100000,-26.990561,-0.4610578107,-48.634720,-0.8284496183,'Balneario Camboriu');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100000,100000,-1.455830,-0.0253072742,-48.504440,-0.846193799,'Belem');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100001,100000,-19.920830,-0.3479022976,-43.937778,-0.7670722063,'Belo Horizonte');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100002,100000,-15.779720,-0.2769255746,-47.929722,-0.8363035999,'Brasilia');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100028,100000,-22.739441,-0.4610578107,-45.591389,-0.8284496183,'Campos do Jordao');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100029,100000,-29.365561,-0.4610578107,-50.815559,-0.8284496183,'Canela');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100014,100000,-29.745560,-0.4933464019,-50.009720,-0.872664626,'Capao da Canoa');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100003,100000,-25.427780,-0.4436045182,-49.273060,-0.860156433,'Curitiba');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100022,100000,-27.596670,-0.4610578107,-48.549171,-0.8284496183,'Florianopolis');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100004,100000,-3.717220,-0.0657407352,-38.543060,-0.6728244266,'Fortaleza');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100030,100000,-25.547779,-0.4610578107,-54.588058,-0.8284496183,'Foz do Iguacu');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100031,100000,-20.666670,-0.4610578107,-40.497501,-0.8284496183,'Guarapari');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100019,100000,-29.374568,-0.4997459425,-50.876435,-0.8575384391,'Gramado');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100032,100000,-23.778061,-0.4610578107,-45.358059,-0.8284496183,'Ilhabela');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100033,100000,-14.788890,-0.4610578107,-39.049438,-0.8284496183,'Ilheus');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100034,100000,-7.115000,-0.4610578107,-34.863060,-0.8284496183,'Joao Pessoa');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100035,100000,-5.795000,-0.4610578107,-35.209438,-0.8284496183,'Natal');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100016,100000,-31.771940,-0.5276712105,-52.342499,-0.9017534469,'Pelotas');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100005,100000,-30.033060,-0.524180552,-51.230000,-0.8938994652,'Porto Alegre');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100036,100000,-16.449720,-0.4610578107,-39.064720,-0.8284496183,'Porto Seguro');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100006,100000,-8.053890,-0.140789893,-34.881111,-0.6088290207,'Recife');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100007,100000,-22.902781,-0.3999712869,-43.207500,-0.7539822369,'Rio De Janeiro');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100017,100000,-32.035000,-0.5579235842,-52.098610,-0.90611677,'Rio Grande');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100008,100000,-12.971110,-0.2268928028,-38.510830,-0.671951762,'Salvador');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100015,100000,-29.684170,-0.4942190665,-53.806938,-0.9110618695,'Santa Maria');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100018,100000,-28.299170,-0.483747091,-54.263062,-0.9381144729,'Santo Angelo');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100009,100000,-23.547501,-0.4110250388,-46.636108,-0.8139052078,'Sao Paulo');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100013,100000,-29.335279,-0.5003277189,-49.726940,-0.8427031405,'Torres');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100021,100000,-28.512220,-0.4799655443,-50.933891,-0.8563748863,'Vacaria');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100037,100000,-20.319441,-0.4610578107,-40.337780,-0.8284496183,'Vitoria');");

        //db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100010,100001,40.65,0.7094763409,-73.78333333,-1.2877621,'New York');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100011,100002,48.85,0.8525933396,2.33,0.04066617157,'Paris');");
        //db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100012,100003,45.508839,0.7942790793,-73.587807,-1.284349521,'Montreal');");

        //Inicializations Scripts - REL_CITY_TAG
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100000,100000,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100001,100000,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100002,100001,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100003,100002,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100004,100003,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100005,100004,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100007,100005,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100006,100004,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100008,100006,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100009,100006,100000);");
      //  db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100010,100007,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100011,100007,100000);");
        //db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100012,100008,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100013,100008,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100014,100009,100002);");
        //db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100015,100010,100002);");
        //db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100016,100010,100000);");
        //db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100017,100010,100001);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100018,100011,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100019,100011,100001);");
        //db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100020,100012,100001);");
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
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100032,100023,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100033,100024,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100034,100025,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100035,100026,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100036,100027,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100037,100028,100001);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100038,100029,100001);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100039,100031,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100040,100032,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100041,100033,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100042,100034,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100043,100034,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100044,100035,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100045,100036,100000);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100046,100036,100002);");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100047,100037,100000);");

        //Inicializations Scripts - REL_CITY_LANGUAGE
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100000,100000," + OTSContract.LANGUAGE_ID_POR + ",'Belém');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100001,100001," + OTSContract.LANGUAGE_ID_POR + ",'Belo Horizonte');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100002,100002," + OTSContract.LANGUAGE_ID_POR + ",'Brasília');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100003,100003," + OTSContract.LANGUAGE_ID_POR + ",'Curitiba');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100004,100004," + OTSContract.LANGUAGE_ID_POR + ",'Fortaleza');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100005,100005," + OTSContract.LANGUAGE_ID_POR + ",'Porto Alegre');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100006,100006," + OTSContract.LANGUAGE_ID_POR + ",'Recife');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100007,100007," + OTSContract.LANGUAGE_ID_POR + ",'Rio De Janeiro');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100008,100008," + OTSContract.LANGUAGE_ID_POR + ",'Salvador');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100009,100009," + OTSContract.LANGUAGE_ID_POR + ",'São Paulo');");
        //db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100010,100010," + OTSContract.LANGUAGE_ID_POR + ",'New York');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100011,100011," + OTSContract.LANGUAGE_ID_POR + ",'Paris');");
        //db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100012,100012," + OTSContract.LANGUAGE_ID_POR + ",'Montreal');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100013,100013," + OTSContract.LANGUAGE_ID_POR + ",'Torres');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100014,100014," + OTSContract.LANGUAGE_ID_POR + ",'Capão da Canoa');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100015,100015," + OTSContract.LANGUAGE_ID_POR + ",'Santa Maria');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100016,100016," + OTSContract.LANGUAGE_ID_POR + ",'Pelotas');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100017,100017," + OTSContract.LANGUAGE_ID_POR + ",'Rio Grande');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100018,100018," + OTSContract.LANGUAGE_ID_POR + ",'Santo Ângelo');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100019,100019," + OTSContract.LANGUAGE_ID_POR + ",'Gramado');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100020,100020," + OTSContract.LANGUAGE_ID_POR + ",'Antônio Prado');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100021,100021," + OTSContract.LANGUAGE_ID_POR + ",'Vacaria');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100022,100022," + OTSContract.LANGUAGE_ID_POR + ",'Florianópolis');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100023,100023," + OTSContract.LANGUAGE_ID_POR + ",'Angra dos Reis');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100024,100024," + OTSContract.LANGUAGE_ID_POR + ",'Aracaju');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100025,100025," + OTSContract.LANGUAGE_ID_POR + ",'Armação de Búzios');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100026,100026," + OTSContract.LANGUAGE_ID_POR + ",'Arraial do Cabo');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100027,100027," + OTSContract.LANGUAGE_ID_POR + ",'Balneário Camboriú');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100028,100028," + OTSContract.LANGUAGE_ID_POR + ",'Campos do Jordão');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100029,100029," + OTSContract.LANGUAGE_ID_POR + ",'Canela');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100030,100030," + OTSContract.LANGUAGE_ID_POR + ",'Foz do Iguaçu');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100031,100031," + OTSContract.LANGUAGE_ID_POR + ",'Guarapari');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100032,100032," + OTSContract.LANGUAGE_ID_POR + ",'Ilhabela');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100033,100033," + OTSContract.LANGUAGE_ID_POR + ",'Ilhéus');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100034,100034," + OTSContract.LANGUAGE_ID_POR + ",'João Pessoa');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100035,100035," + OTSContract.LANGUAGE_ID_POR + ",'Natal');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100036,100036," + OTSContract.LANGUAGE_ID_POR + ",'Porto Seguro');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100037,100037," + OTSContract.LANGUAGE_ID_POR + ",'Vitória');");

        Log.v(LOG_TAG, "inseriu tudo");
        //Inicializations Scripts - REL_SEARCH_CITY
        //NULL

        //Inicializations Scripts - RESULT_SEARCH
        //NULL

        // INSERE CIDADES CANADA e usa

        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100038,100001,38.895111,0.6788477498786641,-77.036369,-1.3445382828312915,'Washington, D. C.');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100048,100038,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100038,100038,100001,'Washington, D. C.');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100039,100038,100000,'Washington, D. C.');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100039,100001,39.290379,0.6857464779064372,-76.61219,-1.3371349626634745,'Baltimore');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100049,100039,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100040,100039,100001,'Baltimore');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100041,100039,100000,'Baltimore');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100040,100001,39.364281,0.6870363111302459,-74.422928,-1.2989251325746785,'Atlantic City');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100050,100040,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100042,100040,100001,'Atlantic City');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100043,100040,100000,'Atlantic City');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100041,100001,39.952339,0.6972998594229388,-75.163788,-1.3118555788710036,'Philadelphia');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100051,100041,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100044,100041,100001,'Philadelphia');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100045,100041,100000,'Philadelphia');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100042,100001,41.76371,0.7289142473480811,-72.685089,-1.2685941201551127,'Hartford');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100052,100042,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100046,100042,100001,'Hartford');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100047,100042,100000,'Hartford');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100043,100001,42.101479,0.7348094285092497,-72.589813,-1.2669312402569828,'Springfield');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100053,100043,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100048,100043,100001,'Springfield');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100049,100043,100000,'Springfield');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100044,100001,42.262589,0.7376213284671377,-71.802292,-1.2531864058783841,'Worcester');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100054,100044,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100050,100044,100001,'Worcester');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100051,100044,100000,'Worcester');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100045,100001,43.661469,0.762036390307436,-70.255333,-1.2261868779350253,'Portland');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100055,100045,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100052,100045,100001,'Portland');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100053,100045,100000,'Portland');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100046,100001,42.331429,0.7388228131242107,-83.045753,-1.4494218196479585,'Detroit');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100056,100046,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100054,100046,100001,'Detroit');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100055,100046,100000,'Detroit');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100047,100001,42.788422,0.7467988456327771,-71.20089,-1.2426899608503053,'Salem');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100057,100047,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100056,100047,100001,'Salem');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100057,100047,100000,'Salem');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100048,100001,42.65258,0.744427955470283,-73.756233,-1.2872891097180947,'Albany');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100058,100048,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100058,100048,100001,'Albany');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100059,100048,100000,'Albany');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100049,100001,42.886452,0.7485097918985071,-78.878372,-1.3766873000129047,'Buffalo');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100059,100049,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100060,100049,100001,'Buffalo');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100061,100049,100000,'Buffalo');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100050,100001,42.440632,0.740728765027266,-76.496613,-1.3351177634738969,'Ithaca');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100060,100050,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100062,100050,100001,'Ithaca');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100063,100050,100000,'Ithaca');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100051,100001,41.92704,0.7317648936153633,-73.99736,-1.2914975697835511,'Kingston');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100061,100051,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100064,100051,100001,'Kingston');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100065,100051,100000,'Kingston');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100052,100001,40.714272,0.7105980989525368,-74.005966,-1.291647772818978,'New York City');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100062,100052,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100066,100052,100001,'New York City');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100067,100052,100000,'New York City');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100053,100001,43.08313,0.7519424705647445,-73.784569,-1.28778366621494,'Saratoga Springs');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100063,100053,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100068,100053,100001,'Saratoga Springs');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100069,100053,100000,'Saratoga Springs');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100054,100001,43.048119,0.7513314133403288,-76.147423,-1.329023248258858,'Syracuse');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100064,100054,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100070,100054,100001,'Syracuse');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100071,100054,100000,'Syracuse');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100055,100001,40.273701,0.7029086844137329,-76.884422,-1.3418863073927638,'Harrisburg');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100065,100055,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100072,100055,100001,'Harrisburg');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100073,100055,100000,'Harrisburg');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100056,100001,40.03788,0.6987928315183872,-76.305511,-1.3317824043667508,'Lancaster');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100066,100056,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100074,100056,100001,'Lancaster');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100075,100056,100000,'Lancaster');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100057,100001,41.40897,0.7227228663595563,-75.662407,-1.3205581221340053,'Scranton');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100067,100057,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100076,100057,100001,'Scranton');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100077,100057,100000,'Scranton');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100058,100001,41.82399,0.7299663318211832,-71.412827,-1.2463889593071045,'Providence');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100068,100058,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100078,100058,100001,'Providence');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100079,100058,100000,'Providence');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100059,100001,39.661209,0.6922186823716078,-76.884979,-1.3418960288766975,'Manchester');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100069,100059,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100080,100059,100001,'Manchester');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100081,100059,100000,'Manchester');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100060,100001,41.73177,0.728356789184994,-70.820038,-1.2360428394874998,'Rochester');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100070,100060,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100082,100060,100001,'Rochester');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100083,100060,100000,'Rochester');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100061,100001,42.729481,0.7457701311183591,-82.801308,-1.445155449557921,'New Haven');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100071,100061,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100084,100061,100001,'New Haven');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100085,100061,100000,'New Haven');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100062,100001,40.285919,0.7031219287417415,-76.650253,-1.337799287336661,'Hershey');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100072,100062,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100086,100062,100001,'Hershey');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100087,100062,100000,'Hershey');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100063,100003,45.508839,0.7942790793100037,-73.587807,-1.2843495214721308,'Montreal');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100073,100063,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100088,100063,100001,'Montreal');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100089,100063,100000,'Montreal');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100064,100003,45.411171,0.792574451136166,-75.69812,-1.32118143156977,'Ottawa');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100074,100064,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100090,100064,100001,'Ottawa');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100091,100064,100000,'Ottawa');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100065,100003,45.400082,0.7923809115754121,-71.899078,-1.2548756402482195,'Sherbrooke');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100075,100065,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100092,100065,100001,'Sherbrooke');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100093,100065,100000,'Sherbrooke');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100066,100003,45.018089,0.7857138760058416,-74.728149,-1.3042522439709079,'Cornwall');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100076,100066,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100094,100066,100001,'Cornwall');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100095,100066,100000,'Cornwall');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100067,100003,44.178761,0.7710648389016626,-77.370529,-1.3503704750597558,'Belleville');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100077,100067,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100096,100067,100001,'Belleville');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100097,100067,100000,'Belleville');");
        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LATITUDE_RAD + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_LONGITUDE_RAD + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (100068,100001,42.358429,0.7392940520222492,-71.059769,-1.2402269347565984,'Boston');");
        db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (100078,100068,100001);");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100098,100068,100001,'Boston');");
        db.execSQL("INSERT INTO " + RelCityLanguage.TABLE_NAME + "(" + RelCityLanguage._ID + ", " + RelCityLanguage.COLUMN_NAME_CITY_ID + ", "+ RelCityLanguage.COLUMN_NAME_LANGUAGE_ID + "," + RelCityLanguage.COLUMN_NAME_NAME + ") VALUES (100099,100068,100000,'Boston');");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.v(LOG_TAG, "vai entrar no onUpgrade");

        db.execSQL(DROP_TABLE_IF_EXISTS + ResultSearch.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + RelSearchCity.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + Search.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + RelCityTag.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + Tag.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + RelCountryLanguage.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + RelCityLanguage.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + Language.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + City.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + Country.TABLE_NAME);

        Log.v(LOG_TAG, "vai entrar no oncreate");

        onCreate(db);

        Log.v(LOG_TAG, "encerrou o create");

    }

}
