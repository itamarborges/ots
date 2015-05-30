package br.borbi.ots;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.utility.CoordinatesUtillity;
import br.borbi.ots.utility.Utility;

interface ClickFragment {

    public void OnClickFragment(int v, Date date);
}


public class FiltersActivity extends Activity
        implements ClickFragment, android.app.LoaderManager.LoaderCallbacks<Cursor>{

    public static final String CLASS_NAME = FiltersActivity.class.getName();
    public static final int CITY_LOADER = 1;
    public static final String BUTTON_CLICKED = "BUTTON_CLICKED";
    public static final String DATE_BEGIN = "DATE_BEGIN";
    public static final String DATE_END = "DATE_END";
    public static final String DISTANCE = "DISTANCE";
    public static final String NUMBER_SUNNY_DAYS = "NUMBER_SUNNY_DAYS";
    public static final String USE_CLOUDY_DAYS = "USE_CLOUDY_DAYS";
    public static final String MIN_TEMPERATURE = "MIN_TEMPERATURE";

    private static DateFormat dateFormat;
    private static TextView dateBeginView;
    private static TextView dateEndView;
    private static RadioGroup distanceType;
    private static EditText distanceEditText;
    private static EditText daysEditText;
    private static CheckBox daysWithoutRainCheckbox;
    private static RadioGroup temperatureType;
    private static EditText temperatureEditText;
    private static Date dateBegin;
    private static Date dateEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);


        String country = Locale.getDefault().getCountry();

        //Datas
        dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        dateBeginView = (TextView) findViewById(R.id.textViewDateBeginPeriod);
        dateEndView = (TextView) findViewById(R.id.textViewDateEndPeriod);


        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        //Distancia
        distanceEditText = (EditText) findViewById(R.id.editTextMaxDistance);
        distanceType = (RadioGroup) findViewById(R.id.radioGroupDistance);

        boolean usesKilometers = sharedPref.getBoolean(OTSContract.USE_KILOMETERS, true);

        if ("US".equalsIgnoreCase(country) || !usesKilometers){
            RadioButton radioButtonMiles = (RadioButton) findViewById(R.id.radioButtonMiles);
            radioButtonMiles.setChecked(true);
        }else {
            RadioButton radioButtonKm = (RadioButton)findViewById(R.id.radioButtonKm);
            radioButtonKm.setChecked(true);
        }

        //Nro dias com sol
        daysEditText = (EditText) findViewById(R.id.editTextQtySunnyDays);

        //Dias nublados
        daysWithoutRainCheckbox = (CheckBox) findViewById(R.id.checkBoxDaysWithoutRain);


        //Temperatura
        temperatureType = (RadioGroup) findViewById(R.id.radioGroupTemperature);
        temperatureEditText = (EditText) findViewById(R.id.editTextMinTemperature);

        boolean usesCelsius = sharedPref.getBoolean(OTSContract.USE_CELSIUS, true);
        if ("US".equalsIgnoreCase(country) || !usesCelsius){
            RadioButton radioButtonFarenheit = (RadioButton) findViewById(R.id.radioButtonFarenheit);
            radioButtonFarenheit.setChecked(true);
        }else {
            RadioButton radioButtonCelsius = (RadioButton)findViewById(R.id.radioButtonCelsius);
            radioButtonCelsius.setChecked(true);
        }


        getLoaderManager().initLoader(CITY_LOADER,null,this);

    }



    public void showCalendar(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundleArgument = new Bundle();
        bundleArgument.putInt(BUTTON_CLICKED, view.getId());
        newFragment.setArguments(bundleArgument);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void onSaveButtonClicked(View view){

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        /*
        Prepara raio de distancia. Distancia sera sempre em km.
         */
        String distanceString = distanceEditText.getText().toString();
        int distance = 0;
        if(distanceString != null && !distanceString.isEmpty()) {
           distance = Integer.valueOf(distanceEditText.getText().toString());
            if (distanceType.getCheckedRadioButtonId() == R.id.radioButtonMiles) {
                distance = Utility.convertMilesToKilometers(distance);
                editor.putBoolean(OTSContract.USE_KILOMETERS,false);
            }else{
                editor.putBoolean(OTSContract.USE_KILOMETERS,true);
            }
        }

        /*
        Dias com sol
         */
        String daysString = daysEditText.getText().toString();
        int numberSunnyDays = 0;
        if(daysString != null && !daysString.isEmpty()) {
            numberSunnyDays = Integer.valueOf(daysEditText.getText().toString());
        }

        /*
        Considerar dias nublados?
         */

        boolean usesCloudyDays = daysWithoutRainCheckbox.isChecked();
        Log.i(CLASS_NAME, "dias nublados: " + usesCloudyDays);

        /*
        Temperatura
         */
        String temperatureString = temperatureEditText.getText().toString();
        int temperature = 0;
        if(temperatureString != null && !temperatureString.isEmpty()) {
            temperature = Integer.valueOf(temperatureEditText.getText().toString());
            if (temperatureType.getCheckedRadioButtonId() == R.id.radioButtonFarenheit) {
                temperature= Utility.convertFarenheitToCelsius(temperature);
                editor.putBoolean(OTSContract.USE_CELSIUS,false);
            }else{
                editor.putBoolean(OTSContract.USE_CELSIUS,true);
            }
            Log.i(CLASS_NAME, "temperatura = " + temperature);
        }

        Intent intent = new Intent(this,SearchActivity.class);
        intent.putExtra(DATE_BEGIN, dateBegin);
        intent.putExtra(DATE_END, dateEnd);
        intent.putExtra(DISTANCE, distance);
        intent.putExtra(NUMBER_SUNNY_DAYS, numberSunnyDays);
        intent.putExtra(USE_CLOUDY_DAYS, usesCloudyDays);
        intent.putExtra(MIN_TEMPERATURE, temperature);

        startActivity(intent);
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }


    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Date date = Utility.getDate(year, monthOfYear, dayOfMonth);

            Bundle b = getArguments();
            ((ClickFragment) getActivity()).OnClickFragment(b.getInt(BUTTON_CLICKED), date);
        }
    }

    public void OnClickFragment(int v, Date date){
        if (v == R.id.calendarDateBegin) {
            dateBeginView.setText(dateFormat.format(date));
            dateBegin = date;
        } else {
            dateEndView.setText(dateFormat.format(date));
            dateEnd = date;
        }
    }
}
