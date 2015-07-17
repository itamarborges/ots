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
import android.widget.ImageView;
import android.widget.TextView;

import br.borbi.ots.R;
import br.borbi.ots.data.OTSContract;
import br.borbi.ots.pojo.DayForecast;
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
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_MORNING_TEMPERATURE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_EVENING_TEMPERATURE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_NIGHT_TEMPERATURE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_PRECIPITATION,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch._ID
    };

    private TextView mDateDetail;
    private TextView mMinMaxTemperatureDetail;
    private TextView mAverageMorning;
    private TextView mAverageAfternoon;
    private TextView mAverageNight;
    private ImageView mWeatherImageView;


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
        mMinMaxTemperatureDetail = (TextView) rootView.findViewById(R.id.detail_min_max_temperature_textView);
        mAverageMorning = (TextView) rootView.findViewById(R.id.morning_temperature_textView);
        mAverageAfternoon = (TextView) rootView.findViewById(R.id.afternoon_temperature_textView);
        mAverageNight = (TextView) rootView.findViewById(R.id.night_temperature_textView);
        mWeatherImageView = (ImageView) rootView.findViewById(R.id.imageWeatherDetail);


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

            int indexMaxTemperature = data.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_MAXIMUM_TEMPERATURE);
            Integer maxTemperature = Utility.roundCeil(data.getDouble(indexMaxTemperature));

            int indexMinTemperature = data.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_MINIMUM_TEMPERATURE);
            Integer minTemperature = Utility.roundCeil(data.getDouble(indexMinTemperature));

            int indexMorningTemperature = data.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_MORNING_TEMPERATURE);
            Integer morningTemperature = Utility.roundCeil(data.getDouble(indexMorningTemperature));

            int indexEveningTemperature = data.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_EVENING_TEMPERATURE);
            Integer eveningTemperature = Utility.roundCeil(data.getDouble(indexEveningTemperature));

            int indexNightTemperature = data.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_NIGHT_TEMPERATURE);
            Integer nightTemperature = Utility.roundCeil(data.getDouble(indexNightTemperature));

            int indexIdWeatherType = data.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_WEATHER_TYPE);
            final int idWeatherType = data.getInt(indexIdWeatherType);

            
            if(minTemperature.intValue() == maxTemperature.intValue()){
                maxTemperature++;
            }

            if(Utility.usesFahrenheit(getActivity())){
                minTemperature = Utility.convertCelsiusToFarenheit(minTemperature);
                maxTemperature = Utility.convertCelsiusToFarenheit(maxTemperature);
                morningTemperature = Utility.convertCelsiusToFarenheit(morningTemperature);
                eveningTemperature = Utility.convertCelsiusToFarenheit(eveningTemperature);
                nightTemperature = Utility.convertCelsiusToFarenheit(nightTemperature);
            }

            mDateDetail.setText(Utility.getFormattedDate(date));
            mMinMaxTemperatureDetail.setText(getString(R.string.display_min_max_temperature, Integer.toString(minTemperature), Integer.toString(maxTemperature)));
            mAverageMorning.setText(getString(R.string.display_temperature, Integer.toString(morningTemperature)));
            mAverageAfternoon.setText(getString(R.string.display_temperature, Integer.toString(eveningTemperature)));
            mAverageNight.setText(getString(R.string.display_temperature, Integer.toString(nightTemperature)));

            mWeatherImageView.setImageResource(Utility.getSmallArtResourceForWeatherCondition(idWeatherType));

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
