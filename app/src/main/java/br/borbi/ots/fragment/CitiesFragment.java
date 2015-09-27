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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Collections;
import java.util.LinkedList;

import br.borbi.ots.R;
import br.borbi.ots.adapter.CitiesAdapter;
import br.borbi.ots.data.OTSContract;
import br.borbi.ots.pojo.City;
import br.borbi.ots.pojo.CityResultSearch;

/**
 * Created by Itamar on 16/06/2015.
 */
public class CitiesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = CitiesFragment.class.getSimpleName();
    private CitiesAdapter mCitiesAdapter;

    private ListView mListView;

    private Double lastLatitude;
    private Double lastLongitude;
    private String mCurrentCityName;

    private Integer mininumDistance;

    private View mRootView;
    private View mEmptyView;

    private static final int CITIES_LOADER = 0;

    private static final String[] CITIES_COLUMNS = {
            OTSContract.Country.TABLE_NAME + "." + OTSContract.Country.COLUMN_NAME_TRANSLATION_FILE_KEY,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_NAME_ENGLISH,
            OTSContract.RelSearchCity.TABLE_NAME + "." + OTSContract.RelSearchCity.COLUMN_NAME_SEARCH_ID,
            OTSContract.RelSearchCity.TABLE_NAME + "." + OTSContract.RelSearchCity._ID,
            OTSContract.RelSearchCity.TABLE_NAME + "." + OTSContract.RelSearchCity.COLUMN_NAME_DISTANCE,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City._ID,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_LATITUDE,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_LONGITUDE,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_TRANSLATION_FILE_KEY
    };

    // these indices must match the projection
    public static final int INDEX_COUNTRY_TRANSLATION_FILE_KEY = 0;
    public static final int INDEX_CITY_NAME = 1;
    public static final int INDEX_SEARCH_ID = 2;
    public static final int INDEX_REL_SEARCH_CITY_ID = 3;
    public static final int INDEX_REL_SEARCH_CITY_DISTANCE = 4;
    public static final int INDEX_CITY_ID = 5;
    public static final int INDEX_CITY_LATITUDE = 6;
    public static final int INDEX_CITY_LONGITUDE = 7;
    public static final int INDEX_CITY_TRANSLATION_FILE_KEY = 8;

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

    public Integer getMininumDistance() {
        return mininumDistance;
    }

    public void setMininumDistance(Integer mininumDistance) {
        this.mininumDistance = mininumDistance;
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

        String sortOrder = OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_NAME_ENGLISH +" ASC";

        Uri uriSearchByCities = OTSContract.CONTENT_URI_LIST_CITIES_BY_SEARCH;

        return new CursorLoader(getActivity(),
                uriSearchByCities,
                CITIES_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        LinkedList<CityResultSearch> cities = new LinkedList<>();

        if (data.moveToFirst()) {
            do {
                String strCountryName = getString(getResources().getIdentifier(data.getString(CitiesFragment.INDEX_COUNTRY_TRANSLATION_FILE_KEY), "string", getActivity().getPackageName()));
                String strCityName = data.getString(CitiesFragment.INDEX_CITY_NAME);
                int idResultSearchCity = data.getInt(CitiesFragment.INDEX_REL_SEARCH_CITY_ID);
                int idCity = data.getInt(CitiesFragment.INDEX_CITY_ID);
                Double cityLatitude = data.getDouble(CitiesFragment.INDEX_CITY_LATITUDE);
                Double cityLongitude = data.getDouble(CitiesFragment.INDEX_CITY_LONGITUDE);
                Integer distance = data.getInt(CitiesFragment.INDEX_REL_SEARCH_CITY_DISTANCE);
                String translationFileKey = data.getString(CitiesFragment.INDEX_CITY_TRANSLATION_FILE_KEY);

                City city = new City(idCity, strCityName, null, strCountryName, cityLatitude, cityLongitude);
                city.setName(getActivity().getString(getActivity().getResources().getIdentifier(translationFileKey, "string", getActivity().getPackageName())));
                CityResultSearch cityResultSearch = new CityResultSearch(city, distance, idResultSearchCity);
                cities.add(cityResultSearch);
            }
            while (data.moveToNext());
        }

        Collections.sort(cities);
        if(cities.size() > 0){
            CityResultSearch cityResultSearch = cities.get(0);
            if(cityResultSearch.getDistance() <=50){
                cityResultSearch.setIsFirstCity(true);
            }
        }

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
