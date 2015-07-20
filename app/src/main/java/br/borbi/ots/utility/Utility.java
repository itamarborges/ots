package br.borbi.ots.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.text.format.Time;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import br.borbi.ots.R;
import br.borbi.ots.SplashScreenActivity;
import br.borbi.ots.data.OTSContract;
import br.borbi.ots.pojo.Coordinates;

/**
 * Created by Gabriela on 29/05/2015.
 */
public class Utility {

    private static final String CLASS_NAME = Utility.class.getSimpleName();

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STRING, INT, FLOAT})
    public @interface DataType {}

    public static final int STRING = 0;
    public static final int INT = 1;
    public static final int
            FLOAT = 2;

    public static int getMediumArtResourceForWeatherCondition(int idWeatherType) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (idWeatherType >= 200 && idWeatherType <= 232) {
            return R.drawable.ots_rain_medium;
        } else if (idWeatherType >= 300 && idWeatherType <= 321) {
            return R.drawable.ots_rain_medium;
        } else if (idWeatherType >= 500 && idWeatherType <= 504) {
            return R.drawable.ots_rain_medium;
        } else if (idWeatherType == 511) {
            return R.drawable.ots_snow_medium;
        } else if (idWeatherType >= 520 && idWeatherType <= 531) {
            return R.drawable.ots_rain_medium;
        } else if (idWeatherType >= 600 && idWeatherType <= 622) {
            return R.drawable.ots_snow_medium;
        } else if (idWeatherType >= 701 && idWeatherType <= 761) {
            return R.drawable.ots_clouds_medium;
        } else if (idWeatherType == 761 || idWeatherType == 781) {
            return R.drawable.ots_rain_medium;
        } else if (idWeatherType == 800) {
            return R.drawable.ots_sunny_medium;
        } else if (idWeatherType == 801) {
            return R.drawable.ots_sunny_medium;
        } else if (idWeatherType >= 802 && idWeatherType <= 804) {
            return R.drawable.ots_clouds_medium;
        }
        return -1;
    }

    public static int getSmallArtResourceForWeatherCondition(int idWeatherType) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (idWeatherType >= 200 && idWeatherType <= 232) {
            return R.drawable.ots_rain_small;
        } else if (idWeatherType >= 300 && idWeatherType <= 321) {
            return R.drawable.ots_rain_small;
        } else if (idWeatherType >= 500 && idWeatherType <= 504) {
            return R.drawable.ots_rain_small;
        } else if (idWeatherType == 511) {
            return R.drawable.ots_rain_small;
        } else if (idWeatherType >= 520 && idWeatherType <= 531) {
            return R.drawable.ots_rain_small;
        } else if (idWeatherType >= 600 && idWeatherType <= 622) {
            return R.drawable.ots_snow_small;
        } else if (idWeatherType >= 701 && idWeatherType <= 761) {
            return R.drawable.ots_rain_small;
        } else if (idWeatherType == 761 || idWeatherType == 781) {
            return R.drawable.ots_rain_small;
        } else if (idWeatherType == 800) {
            return R.drawable.ots_sunny_small;
        } else if (idWeatherType == 801) {
            return R.drawable.ots_sunny_small;
        } else if (idWeatherType >= 802 && idWeatherType <= 804) {
            return R.drawable.ots_clouds_small;
        }
        return -1;
    }

    public static String getFormattedDate(Long dateInMillis ) {
        Date date = new Date(dateInMillis);
        SimpleDateFormat monthDayFormat = new SimpleDateFormat("dd/MM/yyyy");
        return monthDayFormat.format(date);
    }

    public static String getFormattedDateMonth(Long dateInMillis ) {
        Date date = new Date(dateInMillis);
        SimpleDateFormat monthDayFormat = new SimpleDateFormat("dd/MM");
        return monthDayFormat.format(date);
    }

    public static String getFormattedDateMonth(Date date) {
        SimpleDateFormat monthDayFormat = new SimpleDateFormat("dd/MM");
        return monthDayFormat.format(date);
    }

    /*
    Converte a medida de milhas para km.
     */
    public static int convertMilesToKilometers(int distanceInMiles){
        return Double.valueOf(distanceInMiles * 1.609344).intValue();
    }

    public static int convertKilometersToMiles(int distanceInKilometers){
        return Double.valueOf(distanceInKilometers * 0.621371192).intValue();
    }

    /*
    Converte a temperatura de Farenheit para Celsius.
     */
    public static int convertFarenheitToCelsius(int temperatureInFarenheit){
        return Double.valueOf((temperatureInFarenheit-32)/1.8).intValue();
    }

    public static int convertCelsiusToFarenheit(int temperatureInCelsius){
        return Double.valueOf((temperatureInCelsius *1.8)+32).intValue();
    }

    public static int getDifferenceInDays(Date dateBegin, Date dateEnd){
        Long difference = dateEnd.getTime() - dateBegin.getTime();
        difference = (difference/1000)/60/60/24;
        return difference.intValue();
    }

    public static Date getDate(int year, int monthOfYear, int dayOfMonth){
        return (new GregorianCalendar(year,monthOfYear,dayOfMonth,0,0,0)).getTime();
    }

    /*
    Formata a data marcada no calendario de acordo com o formato marcado como padrao no aparelho.
     */
    public static String formatDate(int year, int monthOfYear, int dayOfMonth, DateFormat dateFormat) {
        Calendar data = new GregorianCalendar(year,monthOfYear,dayOfMonth);
        return dateFormat.format(data.getTime());
    }

    public static Date setDateToFinalHours(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static Date setDateToInitialHours(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /*
    Returns a date <code>numberOfDays</code> from today.
     */
    public static Date getDateDaysFromToday(int numberOfDays){
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, numberOfDays);
        return calendar.getTime();
    }

    /*
    Retorna o numero de dias que deve ser utilizado para buscar a previsao do tempo.
     */
    public static int getNumberOfDaysToSearch(Date dateBegin, Date dateEnd){
        Calendar today = new GregorianCalendar();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        int numberOfDays = 16;
        if(dateBegin.after(today.getTime())){
            numberOfDays = Utility.getDifferenceInDays(today.getTime(), dateEnd) + 1;
        }else{
            numberOfDays = Utility.getDifferenceInDays(dateBegin, dateEnd) + 1;
        }

        return numberOfDays;
    }

    /*
        Retorna o numero de dias de pesquisa que deve ser mostrado ao usuario. Apenas para exibicao
     */
    public static int getNumberOfDaysToShow(Date dateBegin, Date dateEnd){
        return Utility.getDifferenceInDays(dateBegin, dateEnd) + 1;
    }

    public static int getDefaultNumberOfDaysToShow(Date dateBegin, Date dateEnd){
        int difference = Utility.getDifferenceInDays(dateBegin, dateEnd) + 1;
        difference = difference/2;
        if(difference == 0){
            difference = 1;
        }
        return difference;
    }

    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    static public void setSharedPreferences(Context c, String key, String value, @DataType int dataType){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor spe = sp.edit();

        switch (dataType) {
            case INT:
              spe.putInt(key, Integer.parseInt(value));
            break;
            default:
                throw new UnsupportedOperationException("Datatype invalid!");
        }
        spe.apply();
    }

    static public void getSharedPreferences(Context c, String key){
        /*
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor spe = sp.edit();

        switch (dataType) {
            case INT:
                spe.putInt(key, Integer.parseInt(value));
                break;
            default:
                throw new UnsupportedOperationException("Datatype invalid!");
        }
        spe.apply();
        */
    }

    static public int roundCeil(Double num) {
        return (int) Math.ceil(num);
    }

    public static void initializeAd(AdView adView,Activity activity){
        adView = (AdView)activity.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);
    }

    public static boolean isSameDay(Date date1, Date date2){
        date1 = setDateToInitialHours(date1);
        date2 = setDateToInitialHours(date2);
        if(date1.getTime() == date2.getTime()){
            return true;
        }
        return false;
    }

    public static boolean usesMiles(Context c){
        SharedPreferences sharedPref = c.getApplicationContext().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        boolean usesKilometers = sharedPref.getBoolean(OTSContract.USE_KILOMETERS, true);

        String country = Locale.getDefault().getCountry();
        if ("US".equalsIgnoreCase(country) || !usesKilometers) {
            return true;
        }

        return false;
    }
    
    public static boolean usesFahrenheit(Context c){
        SharedPreferences sharedPref = c.getApplicationContext().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        boolean usesCelsius = sharedPref.getBoolean(OTSContract.USE_CELSIUS, true);

        String country = Locale.getDefault().getCountry();
        if ("US".equalsIgnoreCase(country) || !usesCelsius) {
            return true;
        }

        return false;
    }

    public static Integer findSearchByDate(Context context){
        Time dayTime = new Time();
        dayTime.setToNow();

        int julianToday = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

        String[] selectionArgs = new String[1];
        selectionArgs[0] = Long.toString(dayTime.setJulianDay(julianToday));

        Cursor c = context.getContentResolver().query(
                OTSContract.Search.CONTENT_URI,
                new String[]{OTSContract.Search._ID},
                QueryUtility.buildQuerySelectSearchByDate(),
                selectionArgs,
                null);

        if (c.moveToFirst()) {
            return c.getInt(c.getColumnIndex(OTSContract.Search._ID));
        }
        return  null;
    }

    public static Integer findSearchByDateAndCoordinates(double lastLatitude, double lastLongitude, Context context){
        Time dayTime = new Time();
        dayTime.setToNow();

        int julianToday = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

        Coordinates coordinates = new Coordinates(lastLatitude, lastLongitude, SplashScreenActivity.MAX_DISTANCE_VALID);

        String[] selectionArgs = new String[5];
        selectionArgs[0] = String.valueOf(coordinates.getMinLatitude());
        selectionArgs[1] = String.valueOf(coordinates.getMaxLatitude());
        selectionArgs[2] = String.valueOf(coordinates.getMinLongitude());
        selectionArgs[3] = String.valueOf(coordinates.getMaxLongitude());
        selectionArgs[4] = Long.toString(dayTime.setJulianDay(julianToday));

        Cursor c = context.getContentResolver().query(
                OTSContract.Search.CONTENT_URI,
                new String[]{OTSContract.Search._ID},
                QueryUtility.buildQuerySelectSearchByCoordinatesAndDate(),
                selectionArgs,
                null);

        if (c.moveToFirst()) {
            return c.getInt(c.getColumnIndex(OTSContract.Search._ID));
        }
        return null;
    }

    public static void positioningCursorInTheEnd(EditText et) {
        et.setSelection(et.getText().length());
    }
}
