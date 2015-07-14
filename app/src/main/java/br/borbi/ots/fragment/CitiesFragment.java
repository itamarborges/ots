package br.borbi.ots.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import br.borbi.ots.R;
import br.borbi.ots.adapter.CitiesAdapter;
import br.borbi.ots.adapter.ResultCityDayForecastAdapter;
import br.borbi.ots.data.OTSContract;
import br.borbi.ots.data.OTSProvider;
import br.borbi.ots.enums.WeatherType;
import br.borbi.ots.pojo.City;
import br.borbi.ots.pojo.CityResultSearch;
import br.borbi.ots.pojo.DayForecast;
import br.borbi.ots.utility.CoordinatesUtillity;
import br.borbi.ots.utility.Utility;

/**
 * Created by Itamar on 16/06/2015.
 */
public class CitiesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = CitiesFragment.class.getSimpleName();
    private CitiesAdapter mCitiesAdapter;

    private ListView mListView;

    private Double lastLatitude;
    private Double lastLongitude;

    private View mRootView;
    private View mEmptyView;

    private static final int CITIES_LOADER = 0;

    private static final String[] CITIES_COLUMNS = {
            OTSContract.RelCountryLanguage.TABLE_NAME + "." + OTSContract.RelCountryLanguage.COLUMN_NAME_NAME,
            OTSContract.RelCityLanguage.TABLE_NAME + "." + OTSContract.RelCityLanguage.COLUMN_NAME_NAME,
            OTSContract.RelSearchCity.TABLE_NAME + "." + OTSContract.RelSearchCity.COLUMN_NAME_SEARCH_ID,
            OTSContract.RelSearchCity.TABLE_NAME + "." + OTSContract.RelSearchCity._ID,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City._ID,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_LATITUDE,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_LONGITUDE
    };

    // these indices must match the projection
    public static final int INDEX_COUNTRY_NAME = 0;
    public static final int INDEX_CITY_NAME = 1;
    public static final int INDEX_SEARCH_ID = 2;
    public static final int INDEX_REL_SEARCH_CITY_ID = 3;
    public static final int INDEX_CITY_ID = 4;
    public static final int INDEX_CITY_LATITUDE = 5;
    public static final int INDEX_CITY_LONGITUDE = 6;

    public static final String[] TAG_COLUMNS = {
            OTSContract.Tag.TABLE_NAME + "." + OTSContract.Tag._ID,
            OTSContract.Tag.TABLE_NAME + "." + OTSContract.Tag.COLUMN_NAME_RESOURCE_NAME
    };

    // these indices must match the projection
    public static final int INDEX_TAG_ID = 0;
    public static final int INDEX_RESOURCE_NAME = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public CitiesFragment() {}


    public double getLastLatitude() {
        if(lastLatitude == null){
            setCoordinates();
        }
        return lastLatitude.doubleValue();
    }

    public void setLastLatitude(Double lastLatitude) {
        this.lastLatitude = lastLatitude;
    }

    public double getLastLongitude() {
        if(lastLongitude == null){
            setCoordinates();
        }
        return lastLongitude.doubleValue();
    }

    public void setLastLongitude(Double lastLongitude) {
        this.lastLongitude = lastLongitude;
    }

    private void setCoordinates(){
        SharedPreferences sharedPreferences = getActivity().getApplication().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        Double lastLatitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LATITUDE, Double.doubleToLongBits(0)));
        Double lastLongitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LONGITUDE, Double.doubleToLongBits(0)));
        this.setLastLatitude(lastLatitude);
        this.setLastLongitude(lastLongitude);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setCoordinates();
        mRootView = inflater.inflate(R.layout.fragment_cities, container, false);
        mEmptyView = mRootView.findViewById(R.id.listview_cities_empty);

        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CITIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = OTSContract.RelCityLanguage.TABLE_NAME + "." + OTSContract.RelCityLanguage.COLUMN_NAME_NAME +" ASC";

        //pick the language used by the device
        String language = Locale.getDefault().getLanguage();
        language = "por";
        //String locationSetting = Utility.getPreferredLocation(getActivity());
        Uri uriSearchByCities = OTSContract.CONTENT_URI_LIST_CITIES_BY_SEARCH;

        String[] selectionArgs;
        String selection;

        selection = OTSProvider.FILTER_BY_LOCALE;
        selectionArgs = new String[]{language};

        return new CursorLoader(getActivity(),
                uriSearchByCities,
                CITIES_COLUMNS,
                selection,
                selectionArgs,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        LinkedList<CityResultSearch> cities = new LinkedList<>();

        if (data.moveToFirst()) {
            do {
                String strCountryName = data.getString(CitiesFragment.INDEX_COUNTRY_NAME);
                String strCityName = data.getString(CitiesFragment.INDEX_CITY_NAME);
                int idResultSearchCity = data.getInt(CitiesFragment.INDEX_REL_SEARCH_CITY_ID);
                int idCity = data.getInt(CitiesFragment.INDEX_CITY_ID);
                Double cityLatitude = data.getDouble(CitiesFragment.INDEX_CITY_LATITUDE);
                Double cityLongitude = data.getDouble(CitiesFragment.INDEX_CITY_LONGITUDE);

                Integer distance  = Utility.roundCeil(CoordinatesUtillity.getDistance(getLastLatitude(), getLastLongitude(), cityLatitude, cityLongitude));

                cities.add(new CityResultSearch(new City(idCity, strCityName,null,strCountryName,cityLatitude,cityLongitude),distance,idResultSearchCity));
            }
            while (data.moveToNext());
        }

        Collections.sort(cities);
        fillAdapter(cities);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void fillAdapter(LinkedList<CityResultSearch> cities ){
        mCitiesAdapter = new CitiesAdapter(cities,getActivity());
        mListView = (ListView) mRootView.findViewById(R.id.listview_cities);
        mListView.setEmptyView(mEmptyView);
        mListView.setAdapter(mCitiesAdapter);
    }
}
