package br.borbi.ots.model;

import android.content.Context;
import android.database.Cursor;

import java.util.Date;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.entity.Search;
import br.borbi.ots.utility.Utility;

/**
 * Created by Gabriela on 30/10/2015.
 */
public class SearchModel {

    private static final String[] SEARCH_COLUMNS = {
            OTSContract.Search.TABLE_NAME + "." + OTSContract.Search._ID,
            OTSContract.Search.TABLE_NAME + "." + OTSContract.Search.COLUMN_NAME_DATE_BEGIN,
            OTSContract.Search.TABLE_NAME + "." + OTSContract.Search.COLUMN_NAME_DATE_END,
            OTSContract.Search.TABLE_NAME + "." + OTSContract.Search.COLUMN_NAME_RADIUS,
            OTSContract.Search.TABLE_NAME + "." + OTSContract.Search.COLUMN_NAME_MIN_SUNNY_DAYS,
            OTSContract.Search.TABLE_NAME + "." + OTSContract.Search.COLUMN_NAME_INCLUDES_CLOUDY_DAYS,
            OTSContract.Search.TABLE_NAME + "." + OTSContract.Search.COLUMN_NAME_MIN_TEMPERATURE,
            OTSContract.Search.TABLE_NAME + "." + OTSContract.Search.COLUMN_NAME_DATETIME_LAST_SEARCH,
            OTSContract.Search.TABLE_NAME + "." + OTSContract.Search.COLUMN_NAME_ORIGIN_LONG,
            OTSContract.Search.TABLE_NAME + "." + OTSContract.Search.COLUMN_NAME_ORIGIN_LAT
    };

    public static Search findSearch(Context contextParam){
        Cursor c = contextParam.getContentResolver().query(
                OTSContract.Search.CONTENT_URI,
                SEARCH_COLUMNS,
                null,
                null,
                null);

        return fillSearch(c);
    }

    private static Search fillSearch(Cursor cursor){
        Search search = new Search();
        if (cursor.moveToFirst()) {
            search.setId(cursor.getLong(cursor.getColumnIndex(OTSContract.Search._ID)));
            search.setBeginDate(new Date(cursor.getLong(cursor.getColumnIndex(OTSContract.Search.COLUMN_NAME_DATE_BEGIN))));
            search.setEndDate(new Date(cursor.getLong(cursor.getColumnIndex(OTSContract.Search.COLUMN_NAME_DATE_END))));
            search.setRadius(cursor.getInt(cursor.getColumnIndex(OTSContract.Search.COLUMN_NAME_RADIUS)));
            search.setMinSunnyDays(cursor.getInt(cursor.getColumnIndex(OTSContract.Search.COLUMN_NAME_MIN_SUNNY_DAYS)));
            search.setIncludesCloudyDays(Utility.getBooleanValue(cursor.getInt(cursor.getColumnIndex(OTSContract.Search.COLUMN_NAME_INCLUDES_CLOUDY_DAYS))));
            search.setMinTemperature(Double.valueOf(cursor.getInt(cursor.getColumnIndex(OTSContract.Search.COLUMN_NAME_MIN_TEMPERATURE))));
            search.setDateTimeLastSearch(new Date(cursor.getLong(cursor.getColumnIndex(OTSContract.Search.COLUMN_NAME_DATETIME_LAST_SEARCH))));
            search.setOriginLatitude(cursor.getDouble(cursor.getColumnIndex(OTSContract.Search.COLUMN_NAME_ORIGIN_LAT)));
            search.setOriginLongitude(cursor.getDouble(cursor.getColumnIndex(OTSContract.Search.COLUMN_NAME_ORIGIN_LONG)));
        }else{
            return null;
        }
        return search;
    }
}
