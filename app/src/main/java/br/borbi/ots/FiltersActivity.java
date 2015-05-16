package br.borbi.ots;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.math.BigInteger;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

interface ClickFragment {

    public void OnClickFragment(int v, Date date);
}


public class FiltersActivity extends Activity implements ClickFragment{

    public void OnClickFragment(int v, Date date){

        if (v == R.id.calendarDateBegin) {
            dateBeginView.setText(dateFormat.format(date));
            dateBegin = date;
        } else {
            dateEndView.setText(dateFormat.format(date));
            dateEnd = date;
        }
    }

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);


        String country = Locale.getDefault().getCountry();

        /*
        Prepara datas
         */
        dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        dateBeginView = (TextView) findViewById(R.id.textViewDateBeginPeriod);
        dateEndView = (TextView) findViewById(R.id.textViewDateEndPeriod);

        distanceEditText = (EditText) findViewById(R.id.editTextMaxDistance);
        distanceType = (RadioGroup) findViewById(R.id.radioGroupDistance);

        //TODO: 1- se ja estiver no shared pref, marcar como default o q estiver marcado. 2- se nao estiver, verificar locale. se for en-US, marcar como default milhas. se nao, km

        RadioButton radioButtonKm = (RadioButton)findViewById(R.id.radioButtonKm);
        if ("US".equalsIgnoreCase(country)){
            //TODO acrescentar busca do shared prefs
            RadioButton radioButtonMiles = (RadioButton) findViewById(R.id.radioButtonMiles);
            radioButtonMiles.setChecked(true);
        }else {
            radioButtonKm.setChecked(true);
        }

        daysEditText = (EditText) findViewById(R.id.editTextQtySunnyDays);

        daysWithoutRainCheckbox = (CheckBox) findViewById(R.id.checkBoxDaysWithoutRain);

        temperatureType = (RadioGroup) findViewById(R.id.radioGroupTemperature);
        temperatureEditText = (EditText) findViewById(R.id.editTextMinTemperature);
        //TODO: 1- se ja estiver no shared pref, marcar como default o q estiver marcado. 2- se nao estiver, verificar locale. se for en-US, marcar como default fr. se nao, C
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

        /*
        Prepara raio de distancia. Distancia sera sempre em km.
         */
        String distanceString = distanceEditText.getText().toString();
        if(distanceString != null && !distanceString.isEmpty()) {
            int distance = Integer.valueOf(distanceEditText.getText().toString());
            if (distanceType.getCheckedRadioButtonId() == R.id.radioButtonMiles) {
                //TODO: se ja estiver salvo no shared preferences como milhas, nao salvar. se nao estiver, salvar.
                distance = convertMilesToKilometers(distance);
            }else{
                //TODO: se ja estiver salvo no shared preferences como km, nao salvar. se nao estiver, salvar.
            }
            Log.i("DISTANCIA", "distancia = " + distance);
        }

        /*
        Dias com sol
         */

        String daysString = daysEditText.getText().toString();
        if(daysString != null && !daysString.isEmpty()) {
            int days = Integer.valueOf(daysEditText.getText().toString());

            Log.i("DIAS", "dias = " + days);
        }

        /*
        Considerar dias nublados?
         */

        boolean daysWithoutRain = daysWithoutRainCheckbox.isChecked();
        Log.i("DIAS-NUBLADOS", "dias nublados: " + daysWithoutRain);

        /*
        Temperatura
         */
        String temperatureString = temperatureEditText.getText().toString();
        if(temperatureString != null && !temperatureString.isEmpty()) {
            int temperature = Integer.valueOf(temperatureEditText.getText().toString());
            if (temperatureType.getCheckedRadioButtonId() == R.id.radioButtonFarenheit) {
                //TODO: se ja estiver salvo no shared preferences como far, nao salvar. se nao estiver, salvar.
                temperature= convertFarenheitToCelsius(temperature);
            }else{
                //TODO: se ja estiver salvo no shared preferences como celsius, nao salvar. se nao estiver, salvar.
            }
            Log.i("TEMPERATURA", "temperatura = " + temperature);
        }

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
        return Double.valueOf(distanceInMiles * 0.621371192237).intValue();
    }

    /*
    Converte a temperatura de Farenheit para Celsius.
     */
    private int convertFarenheitToCelsius(int temperatureInFarenheit){
        return Double.valueOf((temperatureInFarenheit-32)/1.8).intValue();
    }
}
