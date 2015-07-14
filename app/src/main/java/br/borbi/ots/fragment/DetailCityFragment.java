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
import android.widget.TextView;

import br.borbi.ots.R;
import br.borbi.ots.data.OTSContract;
import br.borbi.ots.utility.Utility;

/**
 * Created by Itamar on 16/06/2015.
 */
public class DetailCityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = DetailCityFragment.class.getSimpleName();
    private int idResultSearch;
    private static final int DETAIL_CITY_LOADER = 0;

    private static final String[] RESULT_SEARCH_COLUMNS = {
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_DATE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_MINIMUM_TEMPERATURE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_MAXIMUM_TEMPERATURE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_WEATHER_TYPE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_HUMIDITY,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch._ID
    };

    private TextView mDateDetail;
    private TextView mHumidityDetail;
    private TextView mMinTemperatureDetail;
    private TextView mMaxTemperatureDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public DetailCityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail_city, container, false);

        mDateDetail = (TextView) rootView.findViewById(R.id.detail_date_textView);
        mHumidityDetail = (TextView) rootView.findViewById(R.id.detail_humidity_textView);
        mMinTemperatureDetail = (TextView) rootView.findViewById(R.id.detail_min_temperature_textView);
        mMaxTemperatureDetail = (TextView) rootView.findViewById(R.id.detail_max_temperature_textView);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_CITY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri uriResultSearch = OTSContract.ResultSearch.CONTENT_URI;

        String[] selectionArgs;
        String selection;

        selection = OTSContract.ResultSearch._ID + " = ? ";
        selectionArgs = new String[]{String.valueOf(idResultSearch)};

        return new CursorLoader(getActivity(),
                uriResultSearch,
                RESULT_SEARCH_COLUMNS,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            int indexDate = data.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_DATE);
            Long date = data.getLong(indexDate);

            int indexHumidity = data.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_HUMIDITY);
            Double humidity = data.getDouble(indexHumidity);

            int indexMaxTemperature = data.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_MAXIMUM_TEMPERATURE);
            Integer maxTemperature = Utility.roundCeil(data.getDouble(indexMaxTemperature));

            int indexMinTemperature = data.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_MINIMUM_TEMPERATURE);
            Integer minTemperature = Utility.roundCeil(data.getDouble(indexMinTemperature));

            if(minTemperature.intValue() == maxTemperature.intValue()){
                maxTemperature++;
            }

            mDateDetail.setText(Utility.getFormattedDate(date));

            mHumidityDetail.setText(getString(R.string.display_per_cent, Double.toString(humidity)));
            mMinTemperatureDetail.setText(getString(R.string.display_temperature, Integer.toString(minTemperature)));
            mMaxTemperatureDetail.setText(getString(R.string.display_temperature, Integer.toString(maxTemperature)));

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {  }

    public int getidResultSearch() {
        return idResultSearch;
    }

    public void setidResultSearch(int idResultSearch) {
        this.idResultSearch = idResultSearch;
    }
}
