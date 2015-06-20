package br.borbi.ots.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import br.borbi.ots.R;

/**
 * Created by Gabriela on 29/05/2015.
 */
public class Utility {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STRING, INT, FLOAT})
    public @interface DataType {}

    public static final int STRING = 0;
    public static final int INT = 1;
    public static final int
            FLOAT = 2;

    public static int getArtResourceForWeatherCondition(int idWeatherType) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (idWeatherType >= 200 && idWeatherType <= 232) {
            return R.drawable.art_storm;
        } else if (idWeatherType >= 300 && idWeatherType <= 321) {
            return R.drawable.art_light_rain;
        } else if (idWeatherType >= 500 && idWeatherType <= 504) {
            return R.drawable.art_rain;
        } else if (idWeatherType == 511) {
            return R.drawable.art_snow;
        } else if (idWeatherType >= 520 && idWeatherType <= 531) {
            return R.drawable.art_rain;
        } else if (idWeatherType >= 600 && idWeatherType <= 622) {
            return R.drawable.art_snow;
        } else if (idWeatherType >= 701 && idWeatherType <= 761) {
            return R.drawable.art_fog;
        } else if (idWeatherType == 761 || idWeatherType == 781) {
            return R.drawable.art_storm;
        } else if (idWeatherType == 800) {
            return R.drawable.art_clear;
        } else if (idWeatherType == 801) {
            return R.drawable.art_light_clouds;
        } else if (idWeatherType >= 802 && idWeatherType <= 804) {
            return R.drawable.art_clouds;
        }
        return -1;
    }

    public static String getFormattedDate(int dateInMillis ) {
        SimpleDateFormat monthDayFormat = new SimpleDateFormat("dd/mm/yyyy");
        return monthDayFormat.format(dateInMillis);
    }

    /*
    Converte a medida de milhas para km.
     */
    public static int convertMilesToKilometers(int distanceInMiles){
        return Double.valueOf(distanceInMiles * 1.609344).intValue();
    }

    /*
    Converte a temperatura de Farenheit para Celsius.
     */
    public static int convertFarenheitToCelsius(int temperatureInFarenheit){
        return Double.valueOf((temperatureInFarenheit-32)/1.8).intValue();
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


}
