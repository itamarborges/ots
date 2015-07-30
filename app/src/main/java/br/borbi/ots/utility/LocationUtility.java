package br.borbi.ots.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import br.borbi.ots.data.OTSContract;

/**
 * Created by Gabriela on 15/07/2015.
 */
public class LocationUtility {

    private static final String LOG_TAG = LocationUtility.class.getSimpleName();

    public static LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(5 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

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
}
