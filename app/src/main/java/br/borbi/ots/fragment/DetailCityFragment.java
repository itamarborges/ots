package br.borbi.ots.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import br.borbi.ots.R;
import br.borbi.ots.data.OTSContract;
import br.borbi.ots.utility.Utility;

;

/**
 * Created by Itamar on 16/06/2015.
 */
public class DetailCityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = DetailCityFragment.class.getSimpleName();
    private int idResultSearch;
    private static final int DETAIL_CITY_LOADER = 0;

    private AdView mAdView;

    private float x1, y1, x2, y2;

    public int getQtyItens() {
        return mQtyItens;
    }

    public void setQtyItens(int mQtyItens) {
        this.mQtyItens = mQtyItens;
    }

    public int getRelativePosition() {
        return mRelativePosition;
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
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch._ID
    };

    private TextView mDateDetail;
    private TextView mMinMaxTemperatureDetail;
    private TextView mAverageMorning;
    private TextView mAverageAfternoon;
    private TextView mAverageNight;
    private TextView mPrecipitation;
    private ImageView mWeatherImageView;

    private Button mNextButton;
    private Button mPreviousButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //AdView mAdView = null;
        //Utility.initializeAd(mAdView, getActivity());

    }

    public DetailCityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail_city, container, false);

        //mAdView = Utility.initializeAdView(mAdView, rootView);

        mDateDetail = (TextView) rootView.findViewById(R.id.detail_date_textView);
        mMinMaxTemperatureDetail = (TextView) rootView.findViewById(R.id.detail_min_max_temperature_textView);
        mAverageMorning = (TextView) rootView.findViewById(R.id.morning_temperature_textView);
        mAverageAfternoon = (TextView) rootView.findViewById(R.id.afternoon_temperature_textView);
        mAverageNight = (TextView) rootView.findViewById(R.id.night_temperature_textView);
        mWeatherImageView = (ImageView) rootView.findViewById(R.id.imageWeatherDetail);
        mNextButton = (Button) rootView.findViewById(R.id.btnNext);
        mPreviousButton = (Button) rootView.findViewById(R.id.btnPrevious);
        mPrecipitation = (TextView) rootView.findViewById(R.id.precipitation_textView);

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent touchevent) {
                switch (touchevent.getAction()) {
                    // when user first touches the screen we get x and y coordinate
                    case MotionEvent.ACTION_DOWN: {
                        x1 = touchevent.getX();
                        y1 = touchevent.getY();
                        return true;
                    }
                    case MotionEvent.ACTION_UP: {
                        x2 = touchevent.getX();
                        y2 = touchevent.getY();
                        // /if left to right sweep event on screen
                        if (x1 < x2) {
                            mPreviousButton.performClick();
                        }

                        // if right to left sweep event on screen
                        if (x1 > x2) {
                            mNextButton.performClick();
                        }

                        // if UP to Down sweep event on screen
                /*if (y1 < y2)
                {
                    Toast.makeText(this, "UP to Down Swap Performed", Toast.LENGTH_LONG).show();
                }

                // /if Down to UP sweep event on screen
                if (y1 > y2)
                {
                    Toast.makeText(this, "Down to UP Swap Performed", Toast.LENGTH_LONG).show();
                }
                */
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

            int indexPrecipitation = data.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_PRECIPITATION);
            Integer precipitation = Utility.roundCeil(data.getDouble(indexPrecipitation));

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

                mMinMaxTemperatureDetail.setText(getString(R.string.display_min_max_temperature_fahrenheit, Integer.toString(minTemperature), Integer.toString(maxTemperature)));
                mAverageMorning.setText(getString(R.string.display_temperature_fahrenheit, Integer.toString(morningTemperature)));
                mAverageAfternoon.setText(getString(R.string.display_temperature_fahrenheit, Integer.toString(eveningTemperature)));
                mAverageNight.setText(getString(R.string.display_temperature_fahrenheit, Integer.toString(nightTemperature)));
            }else{
                mMinMaxTemperatureDetail.setText(getString(R.string.display_min_max_temperature_celsius, Integer.toString(minTemperature), Integer.toString(maxTemperature)));
                mAverageMorning.setText(getString(R.string.display_temperature_celsius, Integer.toString(morningTemperature)));
                mAverageAfternoon.setText(getString(R.string.display_temperature_celsius, Integer.toString(eveningTemperature)));
                mAverageNight.setText(getString(R.string.display_temperature_celsius, Integer.toString(nightTemperature)));
            }

            mPrecipitation.setText(getString(R.string.detail_precipitation, Integer.toString(precipitation)));
            mDateDetail.setText(Utility.getFormattedDate(date));
            mWeatherImageView.setImageResource(Utility.getMediumArtResourceForWeatherCondition(idWeatherType));
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

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        //mAdView.pause();
    }

    public void onResume(){
        super.onResume();
        //mAdView.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mAdView.destroy();
    }
}
