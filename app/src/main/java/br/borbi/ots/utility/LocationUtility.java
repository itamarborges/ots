package br.borbi.ots.utility;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import br.borbi.ots.R;
import br.borbi.ots.data.OTSContract;

/**
 * Created by Gabriela on 15/07/2015.
 */
public final class LocationUtility {

    private LocationUtility() {
    }

    private static final String LOG_TAG = LocationUtility.class.getSimpleName();

    private static GoogleApiClient mGoogleApiClient;

    private static Activity mActivity;

    public static synchronized void buildGoogleApiClient(Activity activity) {
        mActivity = activity;

        mGoogleApiClient = new GoogleApiClient.Builder(mActivity )
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks)mActivity)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener)mActivity)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    private static LocationRequest createLocationRequest() {
        return LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(5 * 1000)        // 5 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds
    }

    private static void disconnectFromLocationServices(GoogleApiClient googleApiClient, LocationListener locationListener) {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, locationListener);
            googleApiClient.disconnect();
        }
    }

    private static void saveCoordinates(Double lastLatitude, Double lastLongitude, Activity activity) {
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

    public static void onConnected() {
        try {
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if(mGoogleApiClient.isConnected()) {
                    LocationRequest locationRequest = createLocationRequest();
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, (LocationListener) mActivity);
                }

            }else{
                Log.v(LOG_TAG,"Não tem permissão!");
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, mActivity.getString(R.string.error_localization),e);
            disconnectFromLocationServices(mGoogleApiClient,(LocationListener) mActivity);
        }
    }

    public static void onConnectionSuspended(int i) {
        Log.e(LOG_TAG, "conexao suspensa, erro = " + i);
    }

    public static void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "falhou na conexao, erro = " + connectionResult.getErrorCode());

        disconnectFromLocationServices(mGoogleApiClient, (LocationListener)mActivity);
    }

    public static void onLocationChanged(Location location) {
        saveCoordinates(location.getLatitude(), location.getLongitude(), mActivity);
        disconnectFromLocationServices(mGoogleApiClient, (LocationListener)mActivity);
    }
}
