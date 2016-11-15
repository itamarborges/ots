package br.borbi.ots.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import br.borbi.ots.R;
import br.borbi.ots.data.OTSContract;

/**
 * Created by Gabriela on 15/07/2015.
 */
public class LocationUtility {

    private static final String LOG_TAG = LocationUtility.class.getSimpleName();

    public static LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(5 * 1000)        // 5 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds

        return locationRequest;
    }

    public static void disconnectFromLocationServices(GoogleApiClient googleApiClient, LocationListener locationListener) {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, locationListener);
            googleApiClient.disconnect();
        }
    }

    public static void saveCoordinates(Double lastLatitude, Double lastLongitude, Activity activity) {
        SharedPreferences sharedPreferences = activity.getApplication().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong(OTSContract.SHARED_LATITUDE, Double.doubleToRawLongBits(lastLatitude));
        editor.putLong(OTSContract.SHARED_LONGITUDE, Double.doubleToRawLongBits(lastLongitude));

        editor.apply();
    }

    public static void cleanSavedCoordinates(Activity activity){
        SharedPreferences sharedPreferences = activity.getApplication().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(OTSContract.SHARED_LATITUDE);
        editor.remove(OTSContract.SHARED_LONGITUDE);
        editor.apply();
    }

    /**
     * Builds a dialog to ask the user to turn on the location on the device.
     * @param context context from the place that is calling this
     * @return returns a dialog
     */
    public static AlertDialog buildLocationDialog(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.location).setTitle(R.string.location_turn_on);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);

            }
        });
        builder.setNegativeButton(R.string.not_now, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                Toast.makeText(context, context.getText(R.string.location_explanation), Toast.LENGTH_LONG).show();
            }
        });

        return builder.create();
    }
}
