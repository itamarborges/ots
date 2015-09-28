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
import br.borbi.ots.data.OTSProvider;
import br.borbi.ots.enums.WeatherType;
import br.borbi.ots.model.CityResultSearchModel;
import br.borbi.ots.pojo.City;
import br.borbi.ots.pojo.CityResultSearch;

/**
 * Created by Itamar on 16/06/2015.
 */
public class CitiesFragment extends Fragment {

    public static final String LOG_TAG = CitiesFragment.class.getSimpleName();
    private CitiesAdapter mCitiesAdapter;

    private ListView mListView;

    private Double lastLatitude;
    private Double lastLongitude;

    private Integer mininumDistance;

    private View mRootView;
    private View mEmptyView;

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


        LinkedList<CityResultSearch> cities = CityResultSearchModel.list(getActivity());
        if(cities.size() > 0){
            CityResultSearch cityResultSearch = cities.get(0);
            if(cityResultSearch.getDistance() <=50){
                cityResultSearch.setIsFirstCity(true);
            }
        }

        fillAdapter(cities);


        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //getLoaderManager().initLoader(CITIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

    }

    private void fillAdapter(LinkedList<CityResultSearch> cities ){
        mCitiesAdapter = new CitiesAdapter(cities,getActivity());
        mListView = (ListView) mRootView.findViewById(R.id.listview_cities);
        mListView.setEmptyView(mEmptyView);
        mListView.setAdapter(mCitiesAdapter);
    }
}
