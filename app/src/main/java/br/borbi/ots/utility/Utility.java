package br.borbi.ots.utility;

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
}
