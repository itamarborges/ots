package br.borbi.ots.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.borbi.ots.R;
import br.borbi.ots.data.OTSContract;
import br.borbi.ots.enums.WeatherForecastSourcePriority;
import br.borbi.ots.utility.Utility;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Itamar on 16/06/2015.
 */
public class DetailCityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = DetailCityFragment.class.getSimpleName();
    private int idResultSearch;
    private static final int DETAIL_CITY_LOADER = 0;

    @BindView(R.id.detail_date_textView) TextView mDateDetail;
    @BindView(R.id.detail_min_max_temperature_textView) TextView mMinMaxTemperatureDetail;
    @BindView(R.id.morning_temperature_textView) TextView mAverageMorning;
    @BindView(R.id.afternoon_temperature_textView) TextView mAverageAfternoon;
    @BindView(R.id.night_temperature_textView) TextView mAverageNight;
    @BindView(R.id.precipitation_textView) TextView mPrecipitation;
    @BindView(R.id.imageWeatherDetail) ImageView mWeatherImageView;
    @BindView(R.id.url_source_textView) TextView mUrlSourceTextView;
    @BindView(R.id.temperatura_media_textView) TextView mTemperaturaMediaTextView;
    @BindView(R.id.temperatures_linearLayout) LinearLayout mTemperaturesLinearLayout;
    @BindView(R.id.btnNext) Button mNextButton;
    @BindView(R.id.btnPrevious) Button mPreviousButton;

    private float x1, x2;

    public void setQtyItens(int mQtyItens) {
        this.mQtyItens = mQtyItens;
    }

    public void setRelativePosition(int mRelativePosition) {
        this.mRelativePosition = mRelativePosition;
    }

    private int mQtyItens;
    private int mRelativePosition;

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
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch._ID,
            OTSContract.RelSearchCity.TABLE_NAME + "." + OTSContract.RelSearchCity.COLUMN_NAME_WEATHER_FORECAST_SOURCE
    };

    public DetailCityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail_city, container, false);

        ButterKnife.bind(this, rootView);

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent touchevent) {
                switch (touchevent.getAction()) {
                    // when user first touches the screen we get x and y coordinate
                    case MotionEvent.ACTION_DOWN: {
                        x1 = touchevent.getX();
                        return true;
                    }
                    case MotionEvent.ACTION_UP: {
                        x2 = touchevent.getX();
                        // /if left to right sweep event on screen
                        if (x1 < x2) {
                            mPreviousButton.performClick();
                        }

                        // if right to left sweep event on screen
                        if (x1 > x2) {
                            mNextButton.performClick();
                        }
                        break;
                    }
                }
                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_CITY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

        if (mRelativePosition == 0) {
            mPreviousButton.setVisibility(View.INVISIBLE);
        } else {
            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DetailCityFragment newDetailCityFragment = new DetailCityFragment();

                    newDetailCityFragment.setIdResultSearch(idResultSearch - 1);
                    newDetailCityFragment.setQtyItens(mQtyItens);
                    newDetailCityFragment.setRelativePosition(mRelativePosition - 1);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                    fragmentTransaction.replace(R.id.fragment_layout, newDetailCityFragment);
                    fragmentTransaction.commit();

                }
            });
        }

        if (mRelativePosition == (mQtyItens - 1)){
            mNextButton.setVisibility(View.INVISIBLE);
        } else {
            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DetailCityFragment newDetailCityFragment = new DetailCityFragment();

                    newDetailCityFragment.setIdResultSearch(idResultSearch + 1);
                    newDetailCityFragment.setQtyItens(mQtyItens);
                    newDetailCityFragment.setRelativePosition(mRelativePosition + 1);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left );

                    fragmentTransaction.replace(R.id.fragment_layout, newDetailCityFragment);

                    fragmentTransaction.commit();
                }
            });

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] selectionArgs;
        String selection;

        selection = OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch._ID + " = ? ";
        selectionArgs = new String[]{String.valueOf(idResultSearch)};

        return new CursorLoader(getActivity(),
                //uriResultSearch,
                OTSContract.CONTENT_URI_LIST_RESULT_SEARCH_WITH_REL_SEARCH_CITY,
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
            Integer morningTemperature = null;
            if(!data.isNull(indexMorningTemperature)){
                morningTemperature = Utility.roundCeil(data.getDouble(indexMorningTemperature));
            }

            int indexEveningTemperature = data.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_EVENING_TEMPERATURE);
            Integer eveningTemperature = null;
            if(!data.isNull(indexEveningTemperature)) {
                eveningTemperature = Utility.roundCeil(data.getDouble(indexEveningTemperature));
            }

            int indexNightTemperature = data.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_NIGHT_TEMPERATURE);
            Integer nightTemperature = null;
            if(!data.isNull(indexNightTemperature)) {
                nightTemperature = Utility.roundCeil(data.getDouble(indexNightTemperature));
            }

            int indexPrecipitation = data.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_PRECIPITATION);
            Integer precipitation = Utility.roundCeil(data.getDouble(indexPrecipitation));

            int indexIdWeatherType = data.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_WEATHER_TYPE);
            final int idWeatherType = data.getInt(indexIdWeatherType);

            int indexWeatherForecastSource = data.getColumnIndex(OTSContract.RelSearchCity.COLUMN_NAME_WEATHER_FORECAST_SOURCE);
            final int idWeatherForecastSource = data.getInt(indexWeatherForecastSource);
            
            if(minTemperature.intValue() == maxTemperature.intValue()){
                maxTemperature++;
            }
            boolean usesFahrenheit = Utility.usesFahrenheit(getActivity());
            if(usesFahrenheit){
                minTemperature = Utility.convertCelsiusToFarenheit(minTemperature);
                maxTemperature = Utility.convertCelsiusToFarenheit(maxTemperature);
                mMinMaxTemperatureDetail.setText(getString(R.string.display_min_max_temperature_fahrenheit, Integer.toString(minTemperature), Integer.toString(maxTemperature)));
            }else{
                mMinMaxTemperatureDetail.setText(getString(R.string.display_min_max_temperature_celsius, Integer.toString(minTemperature), Integer.toString(maxTemperature)));
            }

            if(morningTemperature == null || eveningTemperature == null || nightTemperature==null){
                mTemperaturesLinearLayout.setVisibility(View.INVISIBLE);
                mTemperaturaMediaTextView.setVisibility(View.INVISIBLE);

            }else{
                if(Utility.usesFahrenheit(getActivity())){
                    morningTemperature = Utility.convertCelsiusToFarenheit(morningTemperature);
                    eveningTemperature = Utility.convertCelsiusToFarenheit(eveningTemperature);
                    nightTemperature = Utility.convertCelsiusToFarenheit(nightTemperature);

                    if(morningTemperature!=null) {
                        mAverageMorning.setText(getString(R.string.display_temperature_fahrenheit, Integer.toString(morningTemperature)));
                    }
                    if(eveningTemperature!=null) {
                        mAverageAfternoon.setText(getString(R.string.display_temperature_fahrenheit, Integer.toString(eveningTemperature)));
                    }
                    if(nightTemperature!=null) {
                        mAverageNight.setText(getString(R.string.display_temperature_fahrenheit, Integer.toString(nightTemperature)));
                    }
                }else{

                    if(morningTemperature!=null) {
                        mAverageMorning.setText(getString(R.string.display_temperature_celsius, Integer.toString(morningTemperature)));
                    }
                    if(eveningTemperature!=null) {
                        mAverageAfternoon.setText(getString(R.string.display_temperature_celsius, Integer.toString(eveningTemperature)));
                    }
                    if(nightTemperature!=null) {
                        mAverageNight.setText(getString(R.string.display_temperature_celsius, Integer.toString(nightTemperature)));
                    }
                }
            }

            mPrecipitation.setText(getString(R.string.detail_precipitation, Integer.toString(precipitation)));
            mDateDetail.setText(Utility.getFormattedDate(date, getActivity()));
            mWeatherImageView.setImageResource(Utility.getMediumArtResourceForWeatherCondition(idWeatherType));

            WeatherForecastSourcePriority weatherForecastSourcePriority = WeatherForecastSourcePriority.getSource(idWeatherForecastSource);
            Pattern pattern;
            String url;
            if(weatherForecastSourcePriority.isDeveloperForecast()){
                mUrlSourceTextView.setText(getString(R.string.developerForecastCredit));
                pattern = Pattern.compile(getString(R.string.developerForecastCredit));
                url = getString(R.string.weather_forecast_source_developer_forecast);
            }else{
                mUrlSourceTextView.setText(getString(R.string.openWeatherCredit));
                pattern = Pattern.compile(getString(R.string.openWeatherCredit));
                url = getString(R.string.weather_forecast_source_open_weather);
            }

            Linkify.TransformFilter t = new Linkify.TransformFilter() {
                @Override
                public String transformUrl(Matcher match, String url) {
                    return ""; // retorna uma String vazia para não enviar um parâmetro de query
                }
            };

            Linkify.addLinks(mUrlSourceTextView, pattern, url,null,t);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {  }

    public int getidResultSearch() {
        return idResultSearch;
    }

    public void setIdResultSearch(int idResultSearch) {
        this.idResultSearch = idResultSearch;
    }

}
