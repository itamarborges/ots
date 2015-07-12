package br.borbi.ots.utility;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Gabriela on 10/07/2015.
 */
public class DateUtility {

    private static final String LOG_TAG = DateUtility.class.getSimpleName();

    public static int getFirstDayOfWeek(){
        Calendar cal = new GregorianCalendar();
        return cal.getFirstDayOfWeek();
    }

    public static boolean isFirstDayOfWeek(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.get(Calendar.DAY_OF_WEEK);
        if(calendar.get(Calendar.DAY_OF_WEEK) == getFirstDayOfWeek()){
            return true;
        }

        return false;
    }

    public static LinkedList<Date> listDatesFromFirstDayOfWeek(Date beginDate){
        LinkedList<Date> dates = new LinkedList<>();
        int firstDay = getFirstDayOfWeek();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(beginDate);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if(dayOfWeek== firstDay){
            return null;
        }

        int diff= dayOfWeek-firstDay;

        calendar.add(Calendar.DAY_OF_MONTH, -diff);
        while(diff>0){
            dates.add(calendar.getTime());
            diff--;
            calendar.add(Calendar.DAY_OF_MONTH, diff);
        }

        return dates;
    }
}
