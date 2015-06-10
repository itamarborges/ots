package br.borbi.ots.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Gabriela on 29/05/2015.
 */
public class Utility {

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

}
