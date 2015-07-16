package br.borbi.ots;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.utility.ForwardUtility;
import br.borbi.ots.utility.Utility;
import br.borbi.ots.utility.ValidationUtility;

interface ClickFragment {

    public void OnClickFragment(int v, Date date);
}


public class FiltersActivity extends ActionBarActivity implements ClickFragment{

    public static final String CLASS_NAME = FiltersActivity.class.getName();
    public static final int CITY_LOADER = 1;
    public static final String BUTTON_CLICKED = "BUTTON_CLICKED";
    public static final String DATE_BEGIN = "DATE_BEGIN";
    public static final String DATE_END = "DATE_END";
    public static final String DISTANCE = "DISTANCE";
    public static final String NUMBER_SUNNY_DAYS = "NUMBER_SUNNY_DAYS";
    public static final String USE_CLOUDY_DAYS = "USE_CLOUDY_DAYS";
    public static final String MIN_TEMPERATURE = "MIN_TEMPERATURE";
    public static final String DONT_USE_TEMPERATURE = "DONT_USE_TEMPERATURE";
    public static final int MAX_NUMBER_OF_DAYS = 15;

    private static DateFormat dateFormat;
    private static TextView dateBeginView;
    private static TextView dateEndView;
    private static RadioGroup distanceType;
    private static EditText distanceEditText;
    private static EditText daysEditText;
    private static CheckBox daysWithoutRainCheckbox;
    private static RadioGroup temperatureType;
    private static RadioButton radioButtonFarenheit;
    private static RadioButton radioButtonCelsius;
    private static EditText temperatureEditText;
    private static CheckBox temperatureCheckbox;
    private static Date dateBegin;
    private static Date dateEnd;

    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        mContext = this;

        AdView mAdView = null;
        Utility.initializeAd(mAdView, this);

        //Datas
        dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        dateBeginView = (TextView) findViewById(R.id.textViewDateBeginPeriod);
        dateBegin  = Utility.setDateToInitialHours(new Date());
        dateBeginView.setText(dateFormat.format(dateBegin));

        dateEndView = (TextView) findViewById(R.id.textViewDateEndPeriod);
        dateEnd = Utility.setDateToFinalHours(Utility.getDateDaysFromToday(MAX_NUMBER_OF_DAYS));
        dateEndView.setText(dateFormat.format(dateEnd));

        //Distancia
        distanceEditText = (EditText) findViewById(R.id.editTextMaxDistance);

        distanceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && distanceEditText.getText()!=null){
                    if (distanceType.getCheckedRadioButtonId() == R.id.radioButtonMiles){
                        ValidationUtility.validateInteger(distanceEditText, 60, null, mContext.getString(R.string.minimum_distance));
                    }else{
                        ValidationUtility.validateInteger(distanceEditText, 100, null, mContext.getString(R.string.minimum_distance));
                    }
                }
            }
        });


        distanceType = (RadioGroup) findViewById(R.id.radioGroupDistance);

        if(Utility.usesMiles(this)){
            RadioButton radioButtonMiles = (RadioButton) findViewById(R.id.radioButtonMiles);
            radioButtonMiles.setChecked(true);
        }else {
            RadioButton radioButtonKm = (RadioButton)findViewById(R.id.radioButtonKm);
            radioButtonKm.setChecked(true);
        }

        //Nro dias com sol
        daysEditText = (EditText) findViewById(R.id.editTextQtySunnyDays);
        daysEditText.setText(String.valueOf(Utility.getNumberOfDaysToShow(dateBegin, dateEnd)));

        daysEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && daysEditText.getText()!=null){
                    Integer maxNumberOfDays = Utility.getNumberOfDaysToShow(dateBegin, dateEnd);

                    ValidationUtility.validateInteger(daysEditText, 1, maxNumberOfDays, mContext.getString(R.string.minimum_days, maxNumberOfDays));
                }
            }
        });

        //Dias nublados
        daysWithoutRainCheckbox = (CheckBox) findViewById(R.id.checkBoxDaysWithoutRain);

        //Temperatura
        temperatureType = (RadioGroup) findViewById(R.id.radioGroupTemperature);
        temperatureEditText = (EditText) findViewById(R.id.editTextMinTemperature);
        temperatureCheckbox = (CheckBox) findViewById(R.id.checkBoxTemperature);

        radioButtonFarenheit = (RadioButton) findViewById(R.id.radioButtonFarenheit);
        radioButtonCelsius = (RadioButton)findViewById(R.id.radioButtonCelsius);

        if (Utility.usesFahrenheit(this)){
            radioButtonFarenheit.setChecked(true);
        }else {
            radioButtonCelsius.setChecked(true);
        }

        temperatureCheckbox.setChecked(true);
        checkTemperature();

        temperatureCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTemperature();
            }
        });

    }

    public void showCalendar(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundleArgument = new Bundle();
        bundleArgument.putInt(BUTTON_CLICKED, view.getId());
        newFragment.setArguments(bundleArgument);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void onSaveButtonClicked(View view) {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        Double lastLatitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LATITUDE, Double.doubleToLongBits(0)));
        Double lastLongitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LONGITUDE, Double.doubleToLongBits(0)));

        if((lastLatitude == null && lastLongitude == null) || (lastLatitude.doubleValue() == 0d && lastLongitude.doubleValue() == 0d)){
            ForwardUtility.goToFailure(mContext);

        }else {

            boolean allFieldsValid = validateFields();
            if (allFieldsValid) {
                callSearch();
            } else {
                Toast.makeText(this, R.string.invalid_fields, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void callSearch(){
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

        /*
        Temperatura
         */
        String temperatureString = temperatureEditText.getText().toString();
        Integer temperature = null;
        if(temperatureString != null && !temperatureString.isEmpty()) {
            temperature = Integer.valueOf(temperatureEditText.getText().toString());
            if (temperatureType.getCheckedRadioButtonId() == R.id.radioButtonFarenheit) {
                temperature= Utility.convertFarenheitToCelsius(temperature);
                editor.putBoolean(OTSContract.USE_CELSIUS,false);
            }else{
                editor.putBoolean(OTSContract.USE_CELSIUS,true);
            }
        }

        boolean dontUseTemperature = temperatureCheckbox.isChecked();
        if(temperature == null){
            dontUseTemperature = true;
        }

        editor.apply();

        Intent intent = new Intent(this,SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(DATE_BEGIN, dateBegin);
        intent.putExtra(DATE_END, dateEnd);
        intent.putExtra(DISTANCE, distance);
        intent.putExtra(NUMBER_SUNNY_DAYS, numberSunnyDays);
        intent.putExtra(USE_CLOUDY_DAYS, usesCloudyDays);
        intent.putExtra(MIN_TEMPERATURE, temperature);
        intent.putExtra(DONT_USE_TEMPERATURE, dontUseTemperature);

        startActivity(intent);
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker

            Calendar today = new GregorianCalendar();
            today.setTime(Utility.setDateToInitialHours(new Date()));

            Calendar maxDate = new GregorianCalendar();
            maxDate.setTime(Utility.setDateToFinalHours(new Date()));
            maxDate.add(Calendar.DAY_OF_MONTH, 16);

            DatePickerDialog datePickerDialog = null;

            Bundle b = getArguments();
            int buttonClicked = b.getInt(BUTTON_CLICKED);
            if (buttonClicked == R.id.calendarDateBegin) {
                datePickerDialog = new DatePickerDialog(getActivity(), this, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
            }else{
                datePickerDialog = new DatePickerDialog(getActivity(), this, maxDate.get(Calendar.YEAR), maxDate.get(Calendar.MONTH), maxDate.get(Calendar.DAY_OF_MONTH));

            }

            datePickerDialog.getDatePicker().setMinDate(today.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());


            // Create a new instance of DatePickerDialog and return it
            //return new DatePickerDialog(getActivity(), this, year, month, day);
            return datePickerDialog;
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
            dateBegin = Utility.setDateToInitialHours(date);
        } else {
            dateEndView.setText(dateFormat.format(date));
            dateEnd = Utility.setDateToFinalHours(date);
        }
        boolean validDates = validateDates();
        if(validDates){
            int numberOfDays = Utility.getNumberOfDaysToShow(dateBegin, dateEnd);
            daysEditText.setText(String.valueOf(numberOfDays));
        }
    }

    private boolean validateDates(){
        if(dateBegin != null && dateBegin.after(dateEnd)){
            Toast.makeText(this,R.string.begin_date_before_end_date,Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validateFields(){

        boolean validDistance = true;
        // Valida distancia.
        if (distanceType.getCheckedRadioButtonId() == R.id.radioButtonMiles){
            validDistance = ValidationUtility.validateInteger(distanceEditText, 60, null, mContext.getString(R.string.minimum_distance));
        }else{
            validDistance = ValidationUtility.validateInteger(distanceEditText, 100, null, mContext.getString(R.string.minimum_distance));
        }

        // Valida numero de dias
        boolean validDays = ValidationUtility.validateInteger(daysEditText, 1, Utility.getNumberOfDaysToShow(dateBegin, dateEnd), mContext.getString(R.string.minimum_days));

        return (validDistance || validDays);
    }

    private void checkTemperature(){
        boolean isChecked = temperatureCheckbox.isChecked();
        RadioGroup temperatureRadioGroup = (RadioGroup) findViewById(R.id.radioGroupTemperature);
        temperatureEditText.setEnabled(!isChecked);
        temperatureRadioGroup.setEnabled(!isChecked);
        radioButtonCelsius.setEnabled(!isChecked);
        radioButtonFarenheit.setEnabled(!isChecked);
    }
}
