package br.borbi.ots.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by Itamar on 07/02/2015.
 */
public final class OTSContract {


    public static final String CONTENT_AUTHORITY = "br.borbi.ots.app";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_TAG = "tag";
    public static final String PATH_REL_CITY_TAG = "rel_city_tag";
    public static final String PATH_CITY = "city";
    public static final String PATH_LANGUAGE = "language";
    public static final String PATH_REL_COUNTRY_LANGUAGE = "rel_country_language";
    public static final String PATH_REL_CITY_LANGUAGE = "rel_city_language";
    public static final String PATH_COUNTRY = "country";
    public static final String PATH_SEARCH = "search";
    public static final String PATH_REL_SEARCH_CITY = "rel_search_city";
    public static final String PATH_RESULT_SEARCH = "result_search";

    public static final int LANGUAGE_ID_FRA = 100002;
    public static final int LANGUAGE_ID_ENG = 100001;
    public static final int LANGUAGE_ID_POR = 100000;

    public static final String LANGUAGE_CODE_FRA = "fra";
    public static final String LANGUAGE_CODE_ENG = "eng";
    public static final String LANGUAGE_CODE_POR = "por";

    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    // To prevent someone from accidentally instantiating the data class,
    // give it an empty constructor.
    public OTSContract() {
    }

    public static final String CREATE_TABLE = " CREATE TABLE ";
    public static final String PRIMARY_KEY = " INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static final String TYPE_TEXT = " TEXT ";
    public static final String TYPE_INTEGER = " INTEGER ";
    public static final String TYPE_REAL = " REAL ";
    public static final String NOT_NULL = " NOT NULL ";
    public static final String NULL = " NULL ";
    public static final String UNIQUE_ON_CONFLICT_REPLACE = " UNIQUE ON CONFLICT REPLACE ";

    public static abstract class Tag implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TAG).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TAG;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TAG;

        public static final String TABLE_NAME = "tag";
        public static final String COLUMN_NAME_RESOURCE_NAME = "resource_name";

        public static Uri buildTagUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static abstract class RelCityTag implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REL_CITY_TAG).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REL_CITY_TAG;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REL_CITY_TAG;

        public static final String TABLE_NAME = "rel_city_tag";
        public static final String COLUMN_NAME_CITY_ID = "city_id";
        public static final String COLUMN_NAME_TAG_ID = "tag_id";

        public static Uri buildRelCityTagUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static abstract class City implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CITY).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CITY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CITY;


        public static final String TABLE_NAME = "city";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_LATITUDE_RAD = "latitude_rad";
        public static final String COLUMN_NAME_LONGITUDE_RAD = "longitude_rad";
        public static final String COLUMN_NAME_NAME_ENGLISH = "name_english";
        public static final String COLUMN_NAME_COUNTRY_ID = "country_id";

        public static Uri buildCityUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static abstract class Language implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LANGUAGE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LANGUAGE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LANGUAGE;

        public static final String TABLE_NAME = "language";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_LANGUAGE_CODE = "language_code";

        public static Uri buildLanguageUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static abstract class RelCountryLanguage implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REL_COUNTRY_LANGUAGE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REL_COUNTRY_LANGUAGE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REL_COUNTRY_LANGUAGE;

        public static final String TABLE_NAME = "rel_country_language";
        public static final String COLUMN_NAME_LANGUAGE_ID = "language_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_COUNTRY_ID = "country_id";

        public static Uri buildRelCountryLanguageUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static abstract class RelCityLanguage implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REL_CITY_LANGUAGE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REL_CITY_LANGUAGE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REL_CITY_LANGUAGE;

        public static final String TABLE_NAME = "rel_city_language";
        public static final String COLUMN_NAME_LANGUAGE_ID = "language_id";
        public static final String COLUMN_NAME_CITY_ID = "city_id";
        public static final String COLUMN_NAME_NAME = "name";

        public static Uri buildRelCityLanguageUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static abstract class Country implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COUNTRY).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COUNTRY;

        public static final String TABLE_NAME = "country";
        public static final String COLUMN_NAME_NAME_ENGLISH = "name_english";
        public static final String COLUMN_NAME_COUNTRY_CODE = "country_code";

        public static Uri buildCountryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static abstract class Search implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SEARCH).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SEARCH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SEARCH;

        public static final String TABLE_NAME = "search";
        public static final String COLUMN_NAME_DATE_BEGIN = "date_begin";
        public static final String COLUMN_NAME_DATE_END = "date_end";
        public static final String COLUMN_NAME_RADIUS = "radius";
        public static final String COLUMN_NAME_MIN_SUNNY_DAYS = "min_sunny_days";
        public static final String COLUMN_NAME_MIN_TEMPERATURE = "min_temperature";
        public static final String COLUMN_NAME_DATETIME_LAST_SEARCH = "datetime_last_search";

        public static Uri buildSearchUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static abstract class RelSearchCity implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REL_SEARCH_CITY).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REL_SEARCH_CITY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REL_SEARCH_CITY;

        public static final String TABLE_NAME = "rel_search_city";
        public static final String COLUMN_NAME_SEARCH_ID = "search_id";
        public static final String COLUMN_NAME_CITY_ID = "city_id";

        public static Uri buildRelSearchCityUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static abstract class ResultSearch implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RESULT_SEARCH).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RESULT_SEARCH;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RESULT_SEARCH;

        public static final String TABLE_NAME = "result_search";
        public static final String COLUMN_NAME_REL_SEARCH_CITY_ID = "rel_search_city_id";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_RESULTS = "results";

        public static Uri buildResultSearchUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
