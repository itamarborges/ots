package br.borbi.ots.utility;

import android.content.Context;
import android.util.Log;

/**
 * Created by Gabriela on 15/09/2015.
 */
public class CityUtility {

    public static String LOG_TAG = CityUtility.class.getSimpleName();

    public static String getCityDisplayName(String cityName,Context context){
        Log.v(LOG_TAG,"city name = " + cityName);
        return context.getString(context.getResources().getIdentifier(cityName, "cities", context.getPackageName()));
    }
}