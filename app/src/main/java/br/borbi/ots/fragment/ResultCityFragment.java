package br.borbi.ots.fragment;

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
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import br.borbi.ots.R;
import br.borbi.ots.adapter.ResultCityDayForecastAdapter;
import br.borbi.ots.data.OTSContract;
import br.borbi.ots.enums.WeatherType;
import br.borbi.ots.pojo.DayForecast;
import br.borbi.ots.utility.DateUtility;

/**
 * Created by Itamar on 16/06/2015.
 */
public class ResultCityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = ResultCityFragment.class.getSimpleName();
    private static final int RESULT_CITY_LOADER = 0;

    private static final String[] RESULT_CITY_COLUMNS = {
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_DATE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_MINIMUM_TEMPERATURE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_MAXIMUM_TEMPERATURE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_WEATHER_TYPE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch._ID
    };

    private int idRelSearchCity;

    private int mQtyCities;

    private String strCityName;

    private ResultCityDayForecastAdapter mResultCityDayForecastAdapter;

    private LinkedList<DayForecast> forecasts;

    private String[] mWeekDaysNames;

    private View mRootView;
    private View mEmptyView;

    public String getStrCityName() {
        return strCityName;
    }

    public void setStrCityName(String strCityName) {
        this.strCityName = strCityName;
        if (mResultCityDayForecastAdapter != null) {
            mResultCityDayForecastAdapter.setStrCityName(strCityName);
        }
    }

    public int getIdRelSearchCity() {
        return idRelSearchCity;
    }

    public void setIdRelSearchCity(int idRelSearchCity) {
        this.idRelSearchCity = idRelSearchCity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ResultCityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_result_city, container, false);
        mEmptyView = mRootView.findViewById(R.id.listview_result_city_empty);

        buildWeekDaysNamesArray();
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, mWeekDaysNames);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item_day_of_week, mWeekDaysNames);
        GridView gridview = (GridView) mRootView.findViewById(R.id.nameDaysGridView);
        gridview.setAdapter(adapter);

        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(RESULT_CITY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_DATE +" ASC";

        Uri uriResultCity = OTSContract.ResultSearch.CONTENT_URI;

        String[] selectionArgs;
        String selection;

        selection = OTSContract.ResultSearch.COLUMN_NAME_REL_SEARCH_CITY_ID + " = ? ";
        selectionArgs = new String[]{String.valueOf(idRelSearchCity)};

        return new CursorLoader(getActivity(),
                uriResultCity,
                RESULT_CITY_COLUMNS,
                selection,
                selectionArgs,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        int position = 0;

        mQtyCities = data.getCount();

        LinkedList<DayForecast> databaseForecasts = new LinkedList<>();
        if (data.moveToFirst()) {
            do {
                int numIndexDate = data.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_DATE);
                int numIndexMinimumTemperature = data.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_MINIMUM_TEMPERATURE);
                int numIndexMaximumTemperature = data.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_MAXIMUM_TEMPERATURE);
                int numIndexWeatherType = data.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_WEATHER_TYPE);
                int numIndexResultSearchID = data.getColumnIndex(OTSContract.ResultSearch._ID);

                DayForecast dayForecast = new DayForecast(new Date(data.getLong(numIndexDate)), data.getDouble(numIndexMinimumTemperature), data.getDouble(numIndexMaximumTemperature), WeatherType.getWeatherType(data.getInt(numIndexWeatherType)), data.getInt(numIndexResultSearchID), position);
                databaseForecasts.add(dayForecast);

                ++position;
            }
            while (data.moveToNext());
        }

        forecasts = new LinkedList<>();
        DayForecast dayForecast = databaseForecasts.get(0);
        LinkedList<Date> dates = DateUtility.listDatesFromFirstDayOfWeek(dayForecast.getDate());
        if(dates==null){
            forecasts.addAll(databaseForecasts);
        }else{
            for(Date date:dates){
                forecasts.add(null);
            }
            forecasts.addAll(databaseForecasts);
        }

        fillAdapter();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void fillAdapter(){
        mResultCityDayForecastAdapter = new ResultCityDayForecastAdapter(forecasts,getActivity());
        mResultCityDayForecastAdapter.setStrCityName(strCityName);
        mResultCityDayForecastAdapter.setQtyItens(mQtyCities);
        GridView gridview = (GridView) mRootView.findViewById(R.id.gridview);
        gridview.setAdapter(mResultCityDayForecastAdapter);
        gridview.setEmptyView(mEmptyView);
    }

    private void buildWeekDaysNamesArray(){
        Calendar calendar = DateUtility.getDateFirstDayOfWeek();
        mWeekDaysNames = new String[7];
        for (int i=0;i<7;i++){
            mWeekDaysNames[i] = DateUtility.getFormattedDayOfWeek(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }
    }
}
