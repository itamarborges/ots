package br.borbi.ots;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.pojo.Coordinates;
import br.borbi.ots.utility.DateUtility;
import br.borbi.ots.utility.ForwardUtility;
import br.borbi.ots.utility.LocationUtility;
import br.borbi.ots.utility.LogUtility;
import br.borbi.ots.utility.Utility;
import br.borbi.ots.utility.ValidationUtility;

interface ClickFragment {

    void OnClickFragment(int v, Date date);
}

public class FiltersActivity extends AppCompatActivity implements ClickFragment,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String LOG_TAG = FiltersActivity.class.getSimpleName();

    public static final String CLASS_NAME = FiltersActivity.class.getName();
    public static final String BUTTON_CLICKED = "BUTTON_CLICKED";
    public static final String DATE_BEGIN = "DATE_BEGIN";
    public static final String DATE_END = "DATE_END";
    public static final String DISTANCE = "DISTANCE";
    public static final String NUMBER_SUNNY_DAYS = "NUMBER_SUNNY_DAYS";
    public static final String USE_CLOUDY_DAYS = "USE_CLOUDY_DAYS";
    public static final String MIN_TEMPERATURE = "MIN_TEMPERATURE";
    public static final String DONT_USE_TEMPERATURE = "DONT_USE_TEMPERATURE";
    public static final int MAX_NUMBER_OF_DAYS = 15;
    public static final String DISTANCE_DEFAULT = "250";
    public static final int MINIMUM_DISTANCE_KILOMETERS=100;
    public static final int MAXIMUM_DISTANCE_KILOMETERS=2000;
    public static final int MINIMUM_DISTANCE_MILES=60;
    public static final int MAXIMUM_DISTANCE_MILES=1250;
    public static final int INDETERMINED_TEMPERATURE = 999;

    private static DateFormat dateFormat;
    private TextView dateBeginView;
    private TextView dateEndView;
    private EditText distanceEditText;
    private EditText daysEditText;
    private CheckBox daysWithoutRainCheckbox;
    private EditText temperatureEditText;
    private CheckBox temperatureCheckbox;
    private static Date dateBegin;
    private static Date dateEnd;

    private static Button kilometersButton;
    private static Button milesButton;

    private static Button celsiusButton;
    private static Button fahrenheitButton;

    private boolean celsiusChecked = true;
    private boolean kilometersChecked = true;

    //Variables to compare with the current query
    private Long mLastSearchDateTime;
    private Double mLastSearchLongitude;
    private Double mLastSearchLatitude;
    private Long mLastSearchInitialDate;
    private Long mLastSearchFinalDate;
    private Long mLastSearchIdSearch;
    private int mLastSearchSunnyDays;
    private int mLastSearchMinTemperature;
    private int mLastSearchDistance;
    private boolean mLastSearchConsiderCloudyDays;
    private boolean mLasrSearchTemperatureDoesNotMatter;
    private boolean mLastSearchUseCelsius;
    private boolean mLastSearchUseKilometers;
    private boolean mBolRenewInformations;

    private static Double mLastLatitude;
    private static Double mLastLongitude;
    private Context mContext;

    private Boolean mAppJustOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mBolRenewInformations = false;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        mContext = this;

        Intent intent = getIntent();
        if(intent.hasExtra(ForwardUtility.APP_JUST_OPENED)){
            mAppJustOpened = intent.getBooleanExtra(ForwardUtility.APP_JUST_OPENED,false);
        }

        findLocation();

        getParametersLastSearch();

        dateBeginView = (TextView) findViewById(R.id.textViewDateBeginPeriod);
        dateEndView = (TextView) findViewById(R.id.textViewDateEndPeriod);

        //Distancia
        distanceEditText = (EditText) findViewById(R.id.editTextMaxDistance);

        distanceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && distanceEditText.getText() != null) {
                    if (kilometersChecked) {
                        ValidationUtility.validateInteger(distanceEditText, MINIMUM_DISTANCE_KILOMETERS, MAXIMUM_DISTANCE_KILOMETERS, mContext.getString(R.string.minimum_distance_kilometers, String.valueOf(MINIMUM_DISTANCE_KILOMETERS), String.valueOf(MAXIMUM_DISTANCE_KILOMETERS)));
                    } else {
                        ValidationUtility.validateInteger(distanceEditText, MINIMUM_DISTANCE_MILES, MAXIMUM_DISTANCE_MILES, mContext.getString(R.string.minimum_distance_miles, String.valueOf(MINIMUM_DISTANCE_MILES), String.valueOf(MAXIMUM_DISTANCE_MILES)));
                    }
                }
            }
        });

        TextView helpDatePeriod = (TextView) findViewById(R.id.textViewHelpDatePeriod);

        kilometersButton = (Button)findViewById(R.id.btnKm);
        milesButton = (Button)findViewById(R.id.btnMi);

        //Nro dias com sol
        daysEditText = (EditText) findViewById(R.id.editTextQtySunnyDays);

        daysEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && daysEditText.getText() != null) {
                    Integer maxNumberOfDays = Utility.getNumberOfDaysToShow(dateBegin, dateEnd);

                    ValidationUtility.validateInteger(daysEditText, 1, maxNumberOfDays, mContext.getString(R.string.minimum_days, String.valueOf(maxNumberOfDays)));

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                } else {
                    daysEditText.setSelection(daysEditText.getText().length());
                }
            }
        });

        //Dias nublados
        daysWithoutRainCheckbox = (CheckBox) findViewById(R.id.checkBoxDaysWithoutRain);

        daysWithoutRainCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        //Temperatura
        temperatureEditText = (EditText) findViewById(R.id.editTextMinTemperature);
        temperatureCheckbox = (CheckBox) findViewById(R.id.checkBoxTemperature);
        celsiusButton= (Button)findViewById(R.id.btnCelsius);
        fahrenheitButton = (Button)findViewById(R.id.btnFahrenheit);

        temperatureCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTemperature();
            }
        });

        if (mLastSearchDateTime.equals(-1L)) {

            dateBegin  = Utility.setDateToInitialHours(new Date());
            dateEnd = Utility.setDateToFinalHours(Utility.getDateDaysFromToday(MAX_NUMBER_OF_DAYS));

            if(Utility.usesMiles(this)){
                activateButton(milesButton);
                deactivateButton(kilometersButton);
                kilometersChecked = false;
            }else {
                activateButton(kilometersButton);
                deactivateButton(milesButton);
            }

            daysEditText.setText(String.valueOf(1));

            temperatureCheckbox.setChecked(true);

            distanceEditText.setText(DISTANCE_DEFAULT);

            daysWithoutRainCheckbox.setChecked(true);

        } else {
            dateBegin  = new Date(mLastSearchInitialDate);
            if(DateUtility.isDateBeforeAnother(dateBegin,new Date())){
                dateBegin = Utility.setDateToInitialHours(new Date());
            }
            dateEnd = new Date(mLastSearchFinalDate);
            if(DateUtility.isDateBeforeAnother(dateEnd,new Date())){
                dateEnd = Utility.setDateToFinalHours(Utility.getDateDaysFromToday(MAX_NUMBER_OF_DAYS));
            }

            if(!mLastSearchUseKilometers){
                activateButton(milesButton);
                deactivateButton(kilometersButton);
                kilometersChecked = false;
            }else {
                activateButton(kilometersButton);
                deactivateButton(milesButton);
            }

            daysEditText.setText(String.valueOf(mLastSearchSunnyDays));

            daysWithoutRainCheckbox.setChecked(mLastSearchConsiderCloudyDays);

            temperatureCheckbox.setChecked(mLasrSearchTemperatureDoesNotMatter);

            if (mLastSearchMinTemperature != INDETERMINED_TEMPERATURE) {
                temperatureEditText.setText(String.valueOf(mLastSearchMinTemperature));
            }

            distanceEditText.setText(String.valueOf(mLastSearchDistance));

        }

        //Datas
        dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        dateBeginView.setText(dateFormat.format(dateBegin));
        dateEndView.setText(dateFormat.format(dateEnd));

        helpDatePeriod.setText(getString(R.string.help_date_period));

        checkTemperature();

        Utility.positioningCursorInTheEnd(distanceEditText);

    }
    public void showCalendar(View view) {

        int viewIdClicked = -1;

        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundleArgument = new Bundle();

        viewIdClicked = (view.getId() == R.id.layoutDateBegin || view.getId() == R.id.calendarDateBegin) ? R.id.calendarDateBegin: R.id.calendarDateEnd;

        bundleArgument.putInt(BUTTON_CLICKED, viewIdClicked);
        newFragment.setArguments(bundleArgument);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    /**
     * Procura pelos parâmetros da ultima pesquisa realizada e salva no shared preferences.
     */
    private void getParametersLastSearch(){
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        //Recovering the paremeters from the last valid search
        mLastSearchDateTime = sharedPreferences.getLong(OTSContract.SHARED_LAST_SEARCH_DATE_TIME, -1);
        mLastSearchLongitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LAST_SEARCH_LONGITUDE, Double.doubleToLongBits(-1)));
        mLastSearchLatitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LAST_SEARCH_LATITUDE, Double.doubleToLongBits(-1)));
        mLastSearchInitialDate = sharedPreferences.getLong(OTSContract.SHARED_LAST_SEARCH_INITIAL_DATE, -1);
        mLastSearchFinalDate = sharedPreferences.getLong(OTSContract.SHARED_LAST_SEARCH_FINAL_DATE, -1);
        mLastSearchSunnyDays = sharedPreferences.getInt(OTSContract.SHARED_LAST_SEARCH_SUNNY_DAYS, -1);
        mLastSearchMinTemperature = sharedPreferences.getInt(OTSContract.SHARED_LAST_SEARCH_MIN_TEMPERATURE, -1);
        mLastSearchConsiderCloudyDays = sharedPreferences.getBoolean(OTSContract.SHARED_LAST_SEARCH_CONSIDER_CLOUDY_DAYS, false);
        mLasrSearchTemperatureDoesNotMatter = sharedPreferences.getBoolean(OTSContract.SHARED_LAST_SEARCH_TEMPERATURE_DOES_NOT_MATTER, false);
        mLastSearchUseCelsius = sharedPreferences.getBoolean(OTSContract.SHARED_LAST_SEARCH_USE_CELSIUS, false);
        mLastSearchUseKilometers = sharedPreferences.getBoolean(OTSContract.SHARED_LAST_SEARCH_USE_KILOMETERS, false);
        mLastSearchDistance = sharedPreferences.getInt(OTSContract.SHARED_LAST_SEARCH_DISTANCE, -1);

        try {
            mLastSearchIdSearch = sharedPreferences.getLong(OTSContract.SHARED_LAST_SEARCH_ID_SEARCH, -1);
        }catch (ClassCastException e){
            mLastSearchIdSearch = (long) sharedPreferences.getInt(OTSContract.SHARED_LAST_SEARCH_ID_SEARCH, -1);
        }

        mBolRenewInformations = true;
    }

    public void onSaveButtonClicked(View view) {
        boolean useKilometers = true;
        boolean useCelsius = true;
        boolean usesCloudyDays = daysWithoutRainCheckbox.isChecked();
        boolean temperatureDoesNotMatter = temperatureCheckbox.isChecked();
        int mMinTemperaure = 0;

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        Double lastLatitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LATITUDE, Double.doubleToLongBits(0)));
        Double lastLongitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LONGITUDE, Double.doubleToLongBits(0)));

        if ((lastLatitude == null && lastLongitude == null) || (lastLatitude.doubleValue() == 0d && lastLongitude.doubleValue() == 0d)) {
            AlertDialog dialog = LocationUtility.buildLocationDialog(mContext);
            if(dialog!=null) {
                dialog.show();
            }
        } else {
            boolean allFieldsValid = validateFields();
            if (allFieldsValid) {

                SharedPreferences.Editor editor = sharedPreferences.edit();

                //Verifying if the parameters are equals than the last ones
                //If they are and didn´t have passed more than 2 hours, the search won't happen
                Time time = new Time();
                time.setToNow();

                Long timeNow = time.toMillis(false);

                useKilometers = (kilometersChecked) ? true : false;
                useCelsius = (celsiusChecked) ? true : false;

                //in milliseconds
                long diff = timeNow - mLastSearchDateTime;
                long diffHours = diff / (60 * 60 * 1000) % 24;

                if(temperatureEditText.getText() != null){
                    String value = temperatureEditText.getText().toString().trim();
                    if("".equals(value) || value.isEmpty()){
                        temperatureDoesNotMatter = true;
                    }
                }

                mMinTemperaure = (temperatureDoesNotMatter) ? INDETERMINED_TEMPERATURE : Integer.valueOf(temperatureEditText.getText().toString());

                if (!mBolRenewInformations) {
                    getParametersLastSearch();
                }

                Coordinates coordinates = new Coordinates(mLastSearchLatitude, mLastSearchLongitude, 50);

                boolean mMBolDifferentLatitude = false;
                boolean mMBolDifferentLongitude = false;

                if ((lastLatitude > coordinates.getMaxLatitude()) || (lastLatitude < coordinates.getMinLatitude())) {
                    mMBolDifferentLatitude = true;
                }

                if (lastLongitude > (coordinates.getMaxLongitude()) || (lastLongitude < coordinates.getMinLongitude())) {
                    mMBolDifferentLongitude = true;
                }

                if ((mLastSearchDateTime.equals(-1L)) ||
                    (mLastSearchIdSearch == -1) ||
                    (diffHours >= OTSContract.HOURS_TO_SEARCH_EXPIRATION) ||
                    (mMBolDifferentLatitude) ||
                    (mMBolDifferentLatitude) ||
                    (!mLastSearchInitialDate.equals(dateBegin.getTime())) ||
                    (!mLastSearchFinalDate.equals(dateEnd.getTime())) ||
                    (mLastSearchSunnyDays != Integer.valueOf(daysEditText.getText().toString())) ||
                    (mLastSearchMinTemperature != mMinTemperaure) ||
                    (mLastSearchConsiderCloudyDays != usesCloudyDays) ||
                    (mLasrSearchTemperatureDoesNotMatter != temperatureDoesNotMatter) ||
                    (mLastSearchUseCelsius != useCelsius) ||
                    (mLastSearchDistance != Integer.valueOf(distanceEditText.getText().toString())) ||
                    (mLastSearchUseKilometers != useKilometers)) {

                    //If the parameters have different values and there is no wifi connection,
                    //it will show a message asking for the user to turn on the wifi
                    if (!Utility.isNetworkAvailable(this)) {
                        AlertDialog dialog = buildInternetDialog(mContext);
                        if (dialog != null) {
                            dialog.show();
                            return;
                        }
                    }

                    editor.putLong(OTSContract.SHARED_LAST_SEARCH_ID_SEARCH, -1);
                    editor.putLong(OTSContract.SHARED_LAST_SEARCH_DATE_TIME, timeNow);
                    editor.putLong(OTSContract.SHARED_LAST_SEARCH_LONGITUDE, Double.doubleToRawLongBits(lastLongitude));
                    editor.putLong(OTSContract.SHARED_LAST_SEARCH_LATITUDE, Double.doubleToRawLongBits(lastLatitude));
                    editor.putLong(OTSContract.SHARED_LAST_SEARCH_INITIAL_DATE, dateBegin.getTime());
                    editor.putLong(OTSContract.SHARED_LAST_SEARCH_FINAL_DATE, dateEnd.getTime());
                    editor.putInt(OTSContract.SHARED_LAST_SEARCH_SUNNY_DAYS, Integer.valueOf(daysEditText.getText().toString()));
                    editor.putInt(OTSContract.SHARED_LAST_SEARCH_MIN_TEMPERATURE, mMinTemperaure);
                    editor.putInt(OTSContract.SHARED_LAST_SEARCH_DISTANCE, Integer.valueOf(distanceEditText.getText().toString()));
                    editor.putBoolean(OTSContract.SHARED_LAST_SEARCH_CONSIDER_CLOUDY_DAYS, usesCloudyDays);
                    editor.putBoolean(OTSContract.SHARED_LAST_SEARCH_TEMPERATURE_DOES_NOT_MATTER, temperatureDoesNotMatter);
                    editor.putBoolean(OTSContract.SHARED_LAST_SEARCH_USE_CELSIUS, useCelsius);
                    editor.putBoolean(OTSContract.SHARED_LAST_SEARCH_USE_KILOMETERS, useKilometers);

                    editor.apply();

                    mBolRenewInformations = false;

                    callSearch();
                } else {
                    //Call the last results
                    forwardActivity();
                }


            } else {
                Toast.makeText(this, R.string.invalid_fields, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void callSearch(){
        SharedPreferences sharedPref = getApplication().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        /*
        Prepara raio de distancia. Distancia sera sempre em km.
         */
        String distanceString = distanceEditText.getText().toString();
        int distance = 0;
        if(distanceString != null && !distanceString.isEmpty()) {
           distance = Integer.valueOf(distanceEditText.getText().toString());
            if(kilometersChecked){
                editor.putBoolean(OTSContract.USE_KILOMETERS,true);
            }else{
                distance = Utility.convertMilesToKilometers(distance);
                editor.putBoolean(OTSContract.USE_KILOMETERS,false);
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
            if(celsiusChecked){
                editor.putBoolean(OTSContract.USE_CELSIUS,true);
            }else{
                temperature= Utility.convertFarenheitToCelsius(temperature);
                editor.putBoolean(OTSContract.USE_CELSIUS,false);
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

    public void changeDistanceMeasure(View view) {
        if(view.getId() == R.id.btnKm){
            activateButton(kilometersButton);
            deactivateButton(milesButton);
            kilometersChecked = true;
        }else {
            activateButton(milesButton);
            deactivateButton(kilometersButton);
            kilometersChecked = false;
        }
    }

    public void changeTemperatureMeasure(View view) {
        if(view.getId() == R.id.btnCelsius){
            activateButton(celsiusButton);
            deactivateButton(fahrenheitButton);
            celsiusChecked = true;
        }else {
            activateButton(fahrenheitButton);
            deactivateButton(celsiusButton);
            celsiusChecked = false;
        }
    }

    private void activateButton(Button button){
        button.setBackgroundResource(R.color.ots_blue);
        button.setTextColor(getResources().getColor(R.color.ots_pure_white));
    }

    private void deactivateButton(Button button){
        button.setBackgroundResource(R.color.ots_disabled_button_color);
        button.setTextColor(getResources().getColor(R.color.ots_pure_black));
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker

            Calendar checkedBeginDate;

            Calendar today = new GregorianCalendar();
            today.setTime(Utility.setDateToInitialHours(new Date()));
            if(dateBegin == null){
                checkedBeginDate = today;
            }else{
                checkedBeginDate= new GregorianCalendar();
                checkedBeginDate.setTime(Utility.setDateToInitialHours(dateBegin));
            }

            Calendar checkedEndDate;

            Calendar maxDate = new GregorianCalendar();
            maxDate.setTime(Utility.setDateToFinalHours(new Date()));
            maxDate.add(Calendar.DAY_OF_MONTH, 15);

            if(dateEnd==null){
                checkedEndDate = maxDate;
            }else{
                checkedEndDate = new GregorianCalendar();
                checkedEndDate .setTime(Utility.setDateToFinalHours(dateEnd));
            }

            DatePickerDialog datePickerDialog = null;

            Bundle b = getArguments();
            int buttonClicked = b.getInt(BUTTON_CLICKED);
            if (buttonClicked == R.id.calendarDateBegin) {
                datePickerDialog = new DatePickerDialog(getActivity(), this, checkedBeginDate.get(Calendar.YEAR), checkedBeginDate.get(Calendar.MONTH), checkedBeginDate.get(Calendar.DAY_OF_MONTH));
            }else{
                datePickerDialog = new DatePickerDialog(getActivity(), this, checkedEndDate.get(Calendar.YEAR), checkedEndDate.get(Calendar.MONTH), checkedEndDate.get(Calendar.DAY_OF_MONTH));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
        case R.id.action_where_am_I:
            Intent mapIntent = ForwardUtility.goToMap(this);
            if(mapIntent != null){
                startActivity(mapIntent);
            }
            break;
            case R.id.action_cities_list:
                Intent intent = new Intent(this, CitiesListActivity.class);
                startActivity(intent);
            break;
            case R.id.action_about:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void OnClickFragment(int v, Date date){
        if (v == R.id.calendarDateBegin) {
            dateBeginView.setText(dateFormat.format(date));
            dateBegin = Utility.setDateToInitialHours(date);
        } else {
            dateEndView.setText(dateFormat.format(date));
            dateEnd = Utility.setDateToFinalHours(date);
        }
        /*
        boolean validDates = validateDates();
        if(validDates){
            int numberOfDays = Utility.getDefaultNumberOfDaysToShow(dateBegin, dateEnd);
            daysEditText.setText(String.valueOf(numberOfDays));
        }
        */
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
        if(kilometersChecked){
            validDistance = ValidationUtility.validateInteger(distanceEditText, MINIMUM_DISTANCE_KILOMETERS, MAXIMUM_DISTANCE_KILOMETERS, mContext.getString(R.string.minimum_distance_kilometers,String.valueOf(MINIMUM_DISTANCE_KILOMETERS), String.valueOf(MAXIMUM_DISTANCE_KILOMETERS)));
        }else{
            validDistance = ValidationUtility.validateInteger(distanceEditText, MINIMUM_DISTANCE_MILES, MAXIMUM_DISTANCE_MILES, mContext.getString(R.string.minimum_distance_miles,String.valueOf(MINIMUM_DISTANCE_MILES), String.valueOf(MAXIMUM_DISTANCE_MILES)));
        }

        // Valida numero de dias
        Integer maxNumberOfDays = Utility.getNumberOfDaysToShow(dateBegin, dateEnd);
        boolean validDays = ValidationUtility.validateInteger(daysEditText, 1, Utility.getNumberOfDaysToShow(dateBegin, dateEnd), mContext.getString(R.string.minimum_days, String.valueOf(maxNumberOfDays)));

        return (validDistance && validDays);
    }

    private void activateTemperatureButtonsStatus(){
        if (Utility.usesFahrenheit(this)){
            activateButton(fahrenheitButton);
            deactivateButton(celsiusButton);
            celsiusChecked = false;
        }else {
            activateButton(celsiusButton);
            deactivateButton(fahrenheitButton);
        }
    }

    private void disableTemperatureButtonsStatus(){
        deactivateButton(fahrenheitButton);
        deactivateButton(celsiusButton);
        fahrenheitButton.setBackgroundResource(R.color.ots_disabled_button_color);
        fahrenheitButton.setTextColor(getResources().getColor(R.color.ots_pure_white));
        celsiusButton.setBackgroundResource(R.color.ots_disabled_button_color);
        celsiusButton.setTextColor(getResources().getColor(R.color.ots_pure_white));
    }

    /*
    Verifica se o checkbox "nao importa" para a temperatura minima esta marcado. Se estiver, desabilita o campo.
     */
    private void checkTemperature() {
        boolean isChecked = temperatureCheckbox.isChecked();
        temperatureEditText.setEnabled(!isChecked);
        fahrenheitButton.setEnabled(!isChecked);
        celsiusButton.setEnabled(!isChecked);

        if (!isChecked) {
            activateTemperatureButtonsStatus();
            temperatureEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(temperatureEditText, InputMethodManager.SHOW_IMPLICIT);
        } else {
            disableTemperatureButtonsStatus();
            temperatureEditText.setText("");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(temperatureCheckbox.getWindowToken(), 0);
        }
    }

    protected void onResume(){
        super.onResume();
        defineNextStep();
    }

    /**
     * If this is the first time this activity is called in the current session, verifies if there is any data in search and/or if this is valid data. Meaning that:
     * - the date_end is before than today
     * - the table search is not empty and
     * - the current location is not too far from the place where the search was originally made
     */
    private void defineNextStep(){
        if(mAppJustOpened) {
            mAppJustOpened = false;
            if (mLastLongitude != null && mLastLatitude != null) {
                Long searchId = Utility.findSearchByDateAndCoordinates(mLastLatitude, mLastLongitude, mContext);

                if (searchId != null) {
                    ForwardUtility.goToResults(true, searchId, mContext);
                }
            }
        }
    }

    private void forwardActivity(){
        Long searchId = null;

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        Double lastLatitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LATITUDE, Double.doubleToLongBits(0)));
        Double lastLongitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LONGITUDE, Double.doubleToLongBits(0)));

        if(lastLatitude == null || lastLongitude == null){
            searchId = Utility.findSearchByDate(mContext);
        }else {
            searchId = Utility.findSearchByDateAndCoordinates(lastLatitude, lastLongitude, mContext);
        }

        if (searchId == null) {
            callSearch();
        } else {
            boolean hasFoundCoordinates = (lastLatitude != null && lastLongitude != null);
            ForwardUtility.goToResults(hasFoundCoordinates, searchId, mContext);
        }
    }

    /**
     * Method to verify google play services on the device
     */
    private void findLocation() {

        boolean isTest= Boolean.valueOf(getString(R.string.app_in_test));
        if(isTest) {
           // mLastLatitude = -30.03306;
           // mLastLongitude = -51.23;
            // LocationUtility.saveCoordinates(mLastLatitude, mLastLongitude, this);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }else{
            Log.v(LOG_TAG,"vai chamar buildGoogleApiClient em findLocation");
            LocationUtility.buildGoogleApiClient(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
            // Inicia o servico de localizacao
            LocationUtility.buildGoogleApiClient(this);
        }else{
            AlertDialog dialog = LocationUtility.buildLocationDialog(mContext);
            if(dialog!=null) {
                dialog.show();
            }
        }
    }

    public AlertDialog buildInternetDialog(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.explanation_internet).setTitle(R.string.failure_message_internet);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        return builder.create();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationUtility.onConnected();
    }

    @Override
    public void onConnectionSuspended(int i) {
        LocationUtility.onConnectionSuspended(i);
        Toast.makeText(this, getString(R.string.error_localization), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LocationUtility.onConnectionFailed(connectionResult);
    }

    @Override
    public void onLocationChanged(Location location) {
        LocationUtility.onLocationChanged(location);
        mLastLatitude = location.getLatitude();
        mLastLongitude = location.getLongitude();
    }
}
