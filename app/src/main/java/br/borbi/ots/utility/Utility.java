
package br.borbi.ots.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.IntDef;
import android.widget.EditText;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import br.borbi.ots.R;
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

    private static final int STRING = 0;
    private static final int INT = 1;
    private static final int
            FLOAT = 2;

    public static int getMediumArtResourceForWeatherCondition(int idWeatherType) {
        // Based on weather code data found at:
        // http://openweathermap.org/weather-conditions
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
        } else if (idWeatherType >= 800 && idWeatherType <= 801) {
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
        } else if (idWeatherType >= 800 && idWeatherType <= 801) {
            return R.drawable.ots_sunny_small;
        } else if (idWeatherType >= 802 && idWeatherType <= 804) {
            return R.drawable.ots_clouds_small;
        }
        return -1;
    }

    public static String getFormattedDate(Long dateInMillis, Context context) {
        Date date = new Date(dateInMillis);
        SimpleDateFormat monthDayFormat = new SimpleDateFormat(context.getString(R.string.dateFormatDetails));
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

    private static int getDifferenceInDays(Date dateBegin, Date dateEnd){
        Long difference = dateEnd.getTime() - dateBegin.getTime();
        difference = (difference/1000)/60/60/24;
        return difference.intValue();
    }

    public static Date getDate(int year, int monthOfYear, int dayOfMonth){
        return (new GregorianCalendar(year,monthOfYear,dayOfMonth,0,0,0)).getTime();
    }

    public static Date setDateToFinalHours(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 59);


        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);


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

        int numberOfDays;
        if(dateBegin.after(today.getTime())){
            numberOfDays = Utility.getDifferenceInDays(today.getTime(), dateEnd) + 2;
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

    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    static public int roundCeil(Double num) {
        return (int) Math.ceil(num);
    }

    public static boolean isSameDay(Date date1, Date date2){
        date1 = setDateToInitialHours(date1);
        date2 = setDateToInitialHours(date2);
        return date1.getTime() == date2.getTime();
    }

    public static boolean usesMiles(Context c){
        SharedPreferences sharedPref = c.getApplicationContext().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        boolean usesKilometers = sharedPref.getBoolean(OTSContract.USE_KILOMETERS, true);

        String country = Locale.getDefault().getCountry();
        return "US".equalsIgnoreCase(country) || !usesKilometers;

    }
    
    public static boolean usesFahrenheit(Context c){
        SharedPreferences sharedPref = c.getApplicationContext().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        boolean usesCelsius = sharedPref.getBoolean(OTSContract.USE_CELSIUS, true);

        String country = Locale.getDefault().getCountry();
        return "US".equalsIgnoreCase(country) || !usesCelsius;

    }

    public static Long findSearchByDate(Context context){
        String[] selectionArgs = new String[1];
        selectionArgs[0] = Long.toString((new Date()).getTime());

        Cursor c = context.getContentResolver().query(
                OTSContract.Search.CONTENT_URI,
                new String[]{OTSContract.Search._ID},
                QueryUtility.buildQuerySelectSearchByDate(),
                selectionArgs,
                null);

        Long searchId = null;
        if (c.moveToFirst()) {
            searchId = c.getLong(c.getColumnIndex(OTSContract.Search._ID));
        }
        if(c!= null){
            c.close();
        }
        return  searchId;
    }

    public static Long findSearchByDateAndCoordinates(double lastLatitude, double lastLongitude, Context context){
        Coordinates coordinates = new Coordinates(lastLatitude, lastLongitude, OTSContract.MAX_DISTANCE_VALID);

        String[] selectionArgs = new String[5];
        selectionArgs[0] = String.valueOf(coordinates.getMinLatitude());
        selectionArgs[1] = String.valueOf(coordinates.getMaxLatitude());
        selectionArgs[2] = String.valueOf(coordinates.getMinLongitude());
        selectionArgs[3] = String.valueOf(coordinates.getMaxLongitude());
        selectionArgs[4] = Long.toString((new Date()).getTime());

        Cursor c = context.getContentResolver().query(
                OTSContract.Search.CONTENT_URI,
                new String[]{OTSContract.Search._ID},
                QueryUtility.buildQuerySelectSearchByCoordinatesAndDate(),
                selectionArgs,
                null);

        Long searchId = null;
        if (c.moveToFirst()) {
            searchId = c.getLong(c.getColumnIndex(OTSContract.Search._ID));
        }
        if(c!= null){
            c.close();
        }
        return searchId;
    }

    public static void positioningCursorInTheEnd(EditText et) {
        et.setSelection(et.getText().length());
    }

    public static boolean isDistanceSmallerThanMinimumDistance(Integer distance, Integer minimumDistance){
        return minimumDistance == null || minimumDistance == 0 || (minimumDistance.compareTo(distance) >= 0);
    }

    public static boolean getBooleanValue(int value){
        return (value != 0);
    }

    public static boolean isGoogleMapsInstalled(Context mContext) {
        try {
            mContext.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {

            return false;
        }
    }
}
