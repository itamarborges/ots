package br.borbi.ots;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.utility.CoordinatesUtillity;

interface ClickFragment {

    public void OnClickFragment(int v, Date date);
}


public class FiltersActivity extends Activity implements ClickFragment, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    public void OnClickFragment(int v, Date date){

        if (v == R.id.calendarDateBegin) {
            dateBeginView.setText(dateFormat.format(date));
            dateBegin = date;
        } else {
            dateEndView.setText(dateFormat.format(date));
            dateEnd = date;
        }
    }

    private static final String CLASS_NAME = FiltersActivity.class.getName();
    private static final String BUTTON_CLICKED = "BUTTON_CLICKED";

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

    private static GoogleApiClient mGoogleApiClient;
    private static Location mLastLocation;

    private static double lastLongitude;
    private static double lastLatitude;


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

        // Inicializa API Google Services
        buildGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                distance = convertMilesToKilometers(distance);
                editor.putBoolean(OTSContract.USE_KILOMETERS,false);
            }else{
                editor.putBoolean(OTSContract.USE_KILOMETERS,true);
            }
            Log.i(CLASS_NAME, "distancia = " + distance);
        }

        /*
        Dias com sol
         */

        String daysString = daysEditText.getText().toString();
        if(daysString != null && !daysString.isEmpty()) {
            int days = Integer.valueOf(daysEditText.getText().toString());

            Log.i(CLASS_NAME, "dias = " + days);
        }

        /*
        Considerar dias nublados?
         */

        boolean daysWithoutRain = daysWithoutRainCheckbox.isChecked();
        Log.i(CLASS_NAME, "dias nublados: " + daysWithoutRain);

        /*
        Temperatura
         */
        String temperatureString = temperatureEditText.getText().toString();
        if(temperatureString != null && !temperatureString.isEmpty()) {
            int temperature = Integer.valueOf(temperatureEditText.getText().toString());
            if (temperatureType.getCheckedRadioButtonId() == R.id.radioButtonFarenheit) {
                temperature= convertFarenheitToCelsius(temperature);
                editor.putBoolean(OTSContract.USE_CELSIUS,false);
            }else{
                editor.putBoolean(OTSContract.USE_CELSIUS,true);
            }
            Log.i(CLASS_NAME, "temperatura = " + temperature);
        }

        searchCities(Double.valueOf(distance));

    }

    /*
    Busca cidades
     */
    private void searchCities(double distance){
        double minLatitude = CoordinatesUtillity.getMinLatitude(lastLatitude, distance);
        double maxLatitude = CoordinatesUtillity.getMaxLatitude(lastLatitude, distance);
        double minLongitude = CoordinatesUtillity.getMinLongitude(lastLatitude, lastLongitude, distance);
        double maxLongitude = CoordinatesUtillity.getMaxLongitude(lastLatitude, lastLongitude, distance);


        if(minLatitude > maxLatitude){
            double aux = maxLatitude;
            maxLatitude = minLatitude;
            minLatitude = aux;
        }
        if(minLongitude > maxLongitude){
            double aux = maxLongitude;
            maxLongitude = minLongitude;
            minLongitude = aux;
        }

        Log.i(CLASS_NAME,"minlat = " + minLatitude);
        Log.i(CLASS_NAME,"maxlat = " + maxLatitude);
        Log.i(CLASS_NAME,"minlong = " + minLongitude);
        Log.i(CLASS_NAME,"maxlong  = " + maxLongitude);


        StringBuffer whereClause = new StringBuffer(OTSContract.City.COLUMN_NAME_LATITUDE).append(" >= ").append(minLatitude).append(" AND ")
                .append(OTSContract.City.COLUMN_NAME_LATITUDE).append(" <= ").append(maxLatitude).append(" AND ")
                .append(OTSContract.City.COLUMN_NAME_LONGITUDE).append(" >= ").append(minLongitude).append(" AND ").append(OTSContract.City.COLUMN_NAME_LONGITUDE)
                .append(minLongitude).append(" <= ").append(maxLongitude);


        Log.i(CLASS_NAME, whereClause.toString());


        Cursor c = getContentResolver().query(
                OTSContract.City.CONTENT_URI,
                new String[]{OTSContract.City.COLUMN_NAME_NAME_ENGLISH},
                whereClause.toString(),
                null,
                null);


        String strCity;


        Log.i(CLASS_NAME," vai iterar nos retornos" );
        if (c.moveToFirst()) {
            do {
                int numIndexName = c.getColumnIndex(OTSContract.City.COLUMN_NAME_NAME_ENGLISH);
                //int numIndexLatRad = c.getColumnIndex(OTSContract.City.COLUMN_NAME_LATITUDE_RAD);
                //int numIndexLongRad = c.getColumnIndex(OTSContract.City.COLUMN_NAME_LONGITUDE_RAD);
                strCity = c.getString(numIndexName);
                //Toast.makeText(this,strCity,Toast.LENGTH_SHORT).show();
                Log.i(CLASS_NAME, strCity);

                updateWeather(strCity);

            }
            while (c.moveToNext());

        }
    }

    private void updateWeather(String cityName) {
        FetchWeatherTask weatherTask = new FetchWeatherTask(this);
        weatherTask.execute(cityName);
    }


    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Log.i(CLASS_NAME, "mLastLocation  nao e null" );
            lastLatitude = mLastLocation.getLatitude();
            lastLongitude = mLastLocation.getLongitude();
        }

        Log.i(CLASS_NAME, "latitude = " + lastLatitude);
        Log.i(CLASS_NAME, "longitude = " + lastLongitude);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("OTS", "conexao suspensa, erro = " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(CLASS_NAME, "falhou na conexao, erro = " + connectionResult.getErrorCode());

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
            //String date = formatDate(year, monthOfYear, dayOfMonth);
            Date date = getDate(year, monthOfYear, dayOfMonth);;

            Bundle b = getArguments();
            ((ClickFragment) getActivity()).OnClickFragment(b.getInt(BUTTON_CLICKED), date);
        }
    }

    private static Date getDate(int year, int monthOfYear, int dayOfMonth){
        return (new GregorianCalendar(year,monthOfYear,dayOfMonth)).getTime();
    }

    /*
    Formata a data marcada no calendario de acordo com o formato marcado como padrao no aparelho.
     */
    private static String formatDate(int year, int monthOfYear, int dayOfMonth) {
        Calendar data = new GregorianCalendar(year,monthOfYear,dayOfMonth);
        return dateFormat.format(data.getTime());
    }

    /*
    Converte a medida de milhas para km.
     */
    private int convertMilesToKilometers(int distanceInMiles){
        return Double.valueOf(distanceInMiles * 1.609344).intValue();
    }

    /*
    Converte a temperatura de Farenheit para Celsius.
     */
    private int convertFarenheitToCelsius(int temperatureInFarenheit){
        return Double.valueOf((temperatureInFarenheit-32)/1.8).intValue();
    }
}
