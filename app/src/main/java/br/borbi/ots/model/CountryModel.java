package br.borbi.ots.model;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

import java.util.Collections;
import java.util.LinkedList;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.data.OTSProvider;
import br.borbi.ots.pojo.Country;

/**
 * Created by Itamar on 18/09/2015.
 */
public class CountryModel {

    public Country getCountry(Country CountryParam, Activity activityParam) throws Exception {

        Country country = null;

        if (CountryParam.getId() == 0) {
            throw new Exception("Id Country is empty.");
        }

        String[] selectionArgs = new String[]{Long.toString(CountryParam.getId())};
        Cursor c = activityParam.getContentResolver().query(
                OTSContract.Country.CONTENT_URI,
                OTSProvider.CITIES_COLUMNS,
                OTSProvider.FILTER_BY_COUNTRY,
                selectionArgs,
                null);

        if (c.moveToNext()) {

            int mId = c.getInt(c.getColumnIndex(OTSContract.Country._ID));
            String mCountryCode = c.getString(c.getColumnIndex(OTSContract.Country.COLUMN_NAME_COUNTRY_CODE));
            String mTranslationFileKey = c.getString(c.getColumnIndex(OTSContract.Country.COLUMN_NAME_TRANSLATION_FILE_KEY));
            String mName = activityParam.getString(activityParam.getResources().getIdentifier(mTranslationFileKey, "string", activityParam.getPackageName()));

            country = new Country(mId, mCountryCode, mName);
        }

        if(c != null){
            c.close();
        }

        return country;
    }

    public static LinkedList<Country> listCountries(Context contextParam) {

        LinkedList<Country> listCountries = new LinkedList<Country>();

        Country country;

        Cursor c =  contextParam.getContentResolver().query(
                OTSContract.Country.CONTENT_URI,
                OTSProvider.COUNTRY_COLUMNS,
                null,
                null,
                null);

        while (c.moveToNext()) {

            int mId = c.getInt(c.getColumnIndex(OTSContract.Country._ID));
            String mCountryCode = c.getString(c.getColumnIndex(OTSContract.Country.COLUMN_NAME_COUNTRY_CODE));
            String mTranslationFileKey = c.getString(c.getColumnIndex(OTSContract.Country.COLUMN_NAME_TRANSLATION_FILE_KEY));
            String mName = contextParam.getString(contextParam.getResources().getIdentifier(mTranslationFileKey, "string", contextParam.getPackageName()));

            country = new Country(mId, mCountryCode, mName);

            listCountries.add(country);
        }

        if(c != null){
            c.close();
        }

        Collections.sort(listCountries);

        return listCountries;
    }
}
