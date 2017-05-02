package br.borbi.ots.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import br.borbi.ots.FiltersActivity;
import br.borbi.ots.R;
import br.borbi.ots.ResultActivity;
import br.borbi.ots.data.OTSContract;

/**
 * Created by Gabriela on 16/07/2015.
 */
public class ForwardUtility {

    public static final String SEARCH_ID = "SEARCH_ID";
    public static final String COORDINATES_FOUND = "COORDINATES_FOUND";
    public static final String APP_JUST_OPENED = "APP_JUST_OPENED";

    public static void goToResults(boolean foundCoordinates, Long searchId, Context context){
        Intent intent = new Intent();
        intent.setClass(context, ResultActivity.class);
        intent.putExtra(COORDINATES_FOUND, foundCoordinates);
        intent.putExtra(SEARCH_ID, searchId);
        intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        context.startActivity(intent);
    }

    public static void goToFilters(Context context, boolean appJustOpened){
        Intent intent = new Intent();
        intent.setClass(context, FiltersActivity.class);
        if(appJustOpened){
            intent.putExtra(APP_JUST_OPENED,true);
        }
        context.startActivity(intent);
    }

    /**
     * Builds the intent to see the current position in a map. Only builds the intent if there are coordinates available and if there is any app that can do that.
     * @param context the context from the place that is calling this method
     * @return the intent to show a map.
     */
    public static Intent goToMap(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        Double lastLatitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LATITUDE, Double.doubleToLongBits(0)));
        Double lastLongitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LONGITUDE, Double.doubleToLongBits(0)));

        if((lastLatitude == null && lastLongitude == null) || (lastLatitude == 0d && lastLongitude == 0d)){
            AlertDialog dialog = LocationUtility.buildLocationDialog(context);
            if(dialog!=null) {
                dialog.show();
            }
            return null;
        }

        Uri geoLocation = Uri.parse("geo:" + lastLatitude + "," + lastLongitude);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(context.getPackageManager()) == null) {
            Toast.makeText(context, R.string.no_receiving_apps, Toast.LENGTH_LONG).show();
            Log.d(FiltersActivity.CLASS_NAME, "Couldn't call " + geoLocation.toString() + ", no receiving apps installed!");
            return null;
        }

        return intent;
    }
}
