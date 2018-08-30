package br.borbi.ots.data;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import br.borbi.ots.R;
import br.borbi.ots.data.OTSContract.City;
import br.borbi.ots.data.OTSContract.Country;
import br.borbi.ots.data.OTSContract.RelCityTag;
import br.borbi.ots.data.OTSContract.RelSearchCity;
import br.borbi.ots.data.OTSContract.ResultSearch;
import br.borbi.ots.data.OTSContract.Search;
import br.borbi.ots.data.OTSContract.Tag;

/**
 * Created by Itamar on 24/03/2015.
 */
public class OTSDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 8;

    private static final String DATABASE_NAME = "ots.db";
    private static final String LOG_TAG = "OTSDbHelper";

    private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";

    private static final long COUNTRY_ID_BRAZIL = 100000;
    private static final long COUNTRY_ID_USA = 100001;
    private static final long COUNTRY_ID_FRANCE = 100002;
    private static final long COUNTRY_ID_CANADA = 100003;

    private static final HashMap<String,Integer> mExistingTags = new HashMap<>();
    private static Integer mNextTagId = 100000;

    private final Context mContext;

    private long idInicialCity = 100000;
    private long idInicialRelTagCity = 100000;

    public OTSDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
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

        final String SQL_CREATE_SEARCH_TABLE = OTSContract.CREATE_TABLE + Search.TABLE_NAME + " (" +
                Search._ID + OTSContract.PRIMARY_KEY + " ," +
                Search.COLUMN_NAME_DATE_BEGIN + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_DATE_END + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_RADIUS + OTSContract.TYPE_REAL + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_MIN_TEMPERATURE + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_ORIGIN_LAT + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_ORIGIN_LONG + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_MIN_SUNNY_DAYS + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_INCLUDES_CLOUDY_DAYS + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                Search.COLUMN_NAME_DATETIME_LAST_SEARCH + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + " );";

        final String SQL_CREATE_COUNTRY_TABLE = OTSContract.CREATE_TABLE + Country.TABLE_NAME + " (" +
                Country._ID + OTSContract.PRIMARY_KEY + " ," +
                Country.COLUMN_NAME_COUNTRY_CODE + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL + ", " +
                Country.COLUMN_NAME_TRANSLATION_FILE_KEY + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL + "); ";

        final String SQL_CREATE_CITY_TABLE = OTSContract.CREATE_TABLE + City.TABLE_NAME + " (" +
                City._ID + OTSContract.PRIMARY_KEY + " ," +
                City.COLUMN_NAME_LATITUDE + OTSContract.TYPE_REAL + OTSContract.NOT_NULL + ", " +
                City.COLUMN_NAME_LONGITUDE + OTSContract.TYPE_REAL + OTSContract.NOT_NULL + ", " +
                City.COLUMN_NAME_NAME_ENGLISH + OTSContract.TYPE_TEXT + OTSContract.NOT_NULL+ ", " +
                City.COLUMN_NAME_COUNTRY_ID + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                City.COLUMN_NAME_TRANSLATION_FILE_KEY + OTSContract.TYPE_TEXT + ", " +
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

        final String SQL_CREATE_REL_SEARCH_CITY_TABLE = OTSContract.CREATE_TABLE + RelSearchCity.TABLE_NAME + " (" +
                RelSearchCity._ID + OTSContract.PRIMARY_KEY + " ," +
                RelSearchCity.COLUMN_NAME_CITY_ID + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                RelSearchCity.COLUMN_NAME_SEARCH_ID + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                RelSearchCity.COLUMN_NAME_DISTANCE + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                RelSearchCity.COLUMN_NAME_WEATHER_FORECAST_SOURCE + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
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
                ResultSearch.COLUMN_NAME_MORNING_TEMPERATURE + OTSContract.TYPE_REAL + ", " +
                ResultSearch.COLUMN_NAME_EVENING_TEMPERATURE + OTSContract.TYPE_REAL + ", " +
                ResultSearch.COLUMN_NAME_NIGHT_TEMPERATURE + OTSContract.TYPE_REAL + ", " +
                ResultSearch.COLUMN_NAME_WEATHER_TYPE + OTSContract.TYPE_INTEGER + OTSContract.NOT_NULL + ", " +
                ResultSearch.COLUMN_NAME_HUMIDITY + OTSContract.TYPE_REAL + ", " +
                ResultSearch.COLUMN_NAME_PRECIPITATION + OTSContract.TYPE_REAL + ", " +
                " FOREIGN KEY (" + ResultSearch.COLUMN_NAME_REL_SEARCH_CITY_ID+ ") REFERENCES " +
                RelSearchCity.TABLE_NAME + " (" + RelSearchCity._ID + ") ON DELETE CASCADE);";
/*
        Log.v(LOG_TAG, SQL_CREATE_TAG_TABLE);
        Log.v(LOG_TAG, SQL_CREATE_SEARCH_TABLE);
        Log.v(LOG_TAG, SQL_CREATE_COUNTRY_TABLE);
        Log.v(LOG_TAG, SQL_CREATE_CITY_TABLE);
        Log.v(LOG_TAG, SQL_CREATE_REL_CITY_TAG_TABLE);
        Log.v(LOG_TAG, SQL_CREATE_REL_SEARCH_CITY_TABLE);
        Log.v(LOG_TAG, SQL_CREATE_RESULT_SEARCH_TABLE);
  */
        db.execSQL(SQL_CREATE_TAG_TABLE);
        db.execSQL(SQL_CREATE_SEARCH_TABLE);
        db.execSQL(SQL_CREATE_COUNTRY_TABLE);
        db.execSQL(SQL_CREATE_CITY_TABLE);
        db.execSQL(SQL_CREATE_REL_CITY_TAG_TABLE);
        db.execSQL(SQL_CREATE_REL_SEARCH_CITY_TABLE);
        db.execSQL(SQL_CREATE_RESULT_SEARCH_TABLE);

        //Inicializations Scripts - COUNTRY

        db.execSQL("INSERT INTO " + Country.TABLE_NAME + "(" + Country._ID + ", " + Country.COLUMN_NAME_COUNTRY_CODE + ", " + Country.COLUMN_NAME_TRANSLATION_FILE_KEY + ") VALUES (" + COUNTRY_ID_BRAZIL + ", 'BR', 'brazil');");
        db.execSQL("INSERT INTO " + Country.TABLE_NAME + "(" + Country._ID + ", " + Country.COLUMN_NAME_COUNTRY_CODE + ", " + Country.COLUMN_NAME_TRANSLATION_FILE_KEY + ") VALUES (" + COUNTRY_ID_USA + ", 'US', 'united_states');");
        db.execSQL("INSERT INTO " + Country.TABLE_NAME + "(" + Country._ID + ", " + Country.COLUMN_NAME_COUNTRY_CODE + ", " + Country.COLUMN_NAME_TRANSLATION_FILE_KEY + ") VALUES (" + COUNTRY_ID_FRANCE + ", 'FR', 'france');");
        db.execSQL("INSERT INTO " + Country.TABLE_NAME + "(" + Country._ID + ", " + Country.COLUMN_NAME_COUNTRY_CODE + ", " + Country.COLUMN_NAME_TRANSLATION_FILE_KEY + ") VALUES (" + COUNTRY_ID_CANADA + ", 'CA', 'canada');");

        //Log.v(LOG_TAG, "primeiros inserts executaram");

        Resources res =  mContext.getResources();

        InputStream inputStream = res.openRawResource(R.raw.citiesbrazil);
        readCitiesFile(db,inputStream);

        inputStream = res.openRawResource(R.raw.citiesusa);
        readCitiesFile(db,inputStream);

        inputStream = res.openRawResource(R.raw.citiesfrance);
        readCitiesFile(db,inputStream);

        inputStream = res.openRawResource(R.raw.citiescanada);
        readCitiesFile(db,inputStream);

        //Log.v(LOG_TAG, "leu bd");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Log.v(LOG_TAG, "vai entrar no onUpgrade");

        db.execSQL(DROP_TABLE_IF_EXISTS + ResultSearch.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + RelSearchCity.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + Search.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + RelCityTag.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + Tag.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + City.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + Country.TABLE_NAME);

        //Log.v(LOG_TAG, "vai entrar no oncreate");

        onCreate(db);

        //Log.v(LOG_TAG, "encerrou o create");

    }

    private void readCitiesFile(SQLiteDatabase db, InputStream inputStream){

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            while (in.ready()) {
                String str = in.readLine();
                if (!isWhiteLine(str)) {
                    String[] dados = str.split(";");
                    String cityName = dados[0];
                    String countryCode = dados[1];
                    Double latitude = Double.valueOf(dados[2]);
                    Double longitude = Double.valueOf(dados[3]);
                    String[] tags = dados[4].split(",");
                    String translationFileKey = null;
                    if(dados.length>=6){
                        translationFileKey = dados[5];
                    }

                    // Inserir cidade  OK
                    if(translationFileKey== null || translationFileKey.isEmpty()){
                        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_NAME_ENGLISH + ") VALUES (" + idInicialCity + "," + getCountryId(countryCode) + "," + latitude + ", " + longitude + ",'" + cityName + "');");
                    }else {
                        db.execSQL("INSERT INTO " + City.TABLE_NAME + "(" + City._ID + ", " + City.COLUMN_NAME_COUNTRY_ID + ", " + City.COLUMN_NAME_LATITUDE + ", " + City.COLUMN_NAME_LONGITUDE + ", " + City.COLUMN_NAME_NAME_ENGLISH + ", " + City.COLUMN_NAME_TRANSLATION_FILE_KEY + ") VALUES (" + idInicialCity + "," + getCountryId(countryCode) + "," + latitude + ", " + longitude + ",'" + cityName + "','" + translationFileKey + "');");
                    }
                    // Inserir rel_tag para cada uma  OK
                    for (String tag1 : tags) {
                        String tag = tag1.trim();
                        if (!tag.isEmpty()) {
                            db.execSQL("INSERT INTO " + RelCityTag.TABLE_NAME + "(" + RelCityTag._ID + ", " + RelCityTag.COLUMN_NAME_CITY_ID + ", " + RelCityTag.COLUMN_NAME_TAG_ID + ") VALUES (" + idInicialRelTagCity + ", " + idInicialCity + "," + getTagId(tag, db) + ");");
                            idInicialRelTagCity++;
                        }
                    }

                    idInicialCity++;
                }
            }

            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long getCountryId(String countryCode){
        if(countryCode.equalsIgnoreCase("us")){
            return COUNTRY_ID_USA;
        }
        if(countryCode.equalsIgnoreCase("ca")){
            return COUNTRY_ID_CANADA;
        }
        if(countryCode.equalsIgnoreCase("br")){
            return COUNTRY_ID_BRAZIL;
        }
        if(countryCode.equalsIgnoreCase("fr")){
            return COUNTRY_ID_FRANCE;
        }
        return 0;
    }

    private Integer getTagId(String tagName,SQLiteDatabase db){
        if(mExistingTags.containsKey(tagName)){
            return mExistingTags.get(tagName);
        }
        db.execSQL("INSERT INTO " + Tag.TABLE_NAME + "(" + Tag._ID + ", " + Tag.COLUMN_NAME_RESOURCE_NAME + ") VALUES (" + mNextTagId + ", '" + tagName + "');");
        Integer tagId = mNextTagId;
        mExistingTags.put(tagName,tagId);
        mNextTagId++;
        return tagId;
    }

    private boolean isWhiteLine(String whiteLine) {
        return whiteLine == null || whiteLine.trim().length() == 0;
    }

}
