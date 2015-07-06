package br.borbi.ots;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Timer;
import java.util.TimerTask;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.pojo.Coordinates;
import br.borbi.ots.utility.QueryUtility;
import br.borbi.ots.utility.Utility;


public class SplashScreenActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String CLASS_NAME = SplashScreenActivity.class.getName();
    public static final int MAX_DISTANCE_VALID = 100;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private static Double lastLongitude;
    private static Double lastLatitude;

    public static final int TIME_SPLASH = 10000;
    public static final String COORDINATES_FOUND = "COORDINATES_FOUND";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        findLocation();

        new Timer().schedule(new TimerTask() {

            public void run() {

                //Verify if there is any data in search and/or if this is valid data.
                //Meaning that:
                //- the table search is not empty and
                //- the date_end is before than today
                //- the current location is not too far from the place where the seach was originally made
                Time dayTime = new Time();
                dayTime.setToNow();

                int julianToday = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

                if (lastLongitude == null || lastLatitude == null) {

                    String[] selectionArgs = new String[1];
                    selectionArgs[0] = Long.toString(dayTime.setJulianDay(julianToday));

                    Cursor c = getContentResolver().query(
                            OTSContract.Search.CONTENT_URI,
                            new String[]{OTSContract.Search._ID},
                            QueryUtility.buildQuerySelectSearchByDate(),
                            selectionArgs,
                            null);

                    if (c.moveToFirst()) {
                        Log.v(CLASS_NAME, "Possui dados");
                        goToResults(false);
                    } else {
                        Log.v(CLASS_NAME, "Nao possui dados");
                        goToFilters();
                    }

                } else {
                    Coordinates coordinates = new Coordinates(lastLatitude, lastLongitude, MAX_DISTANCE_VALID);

                    Log.v(CLASS_NAME, "latitude = " + lastLatitude + ", long = " + lastLongitude);

                    String[] selectionArgs = new String[5];
                    selectionArgs[0] = String.valueOf(coordinates.getMinLatitude());
                    selectionArgs[1] = String.valueOf(coordinates.getMaxLatitude());
                    selectionArgs[2] = String.valueOf(coordinates.getMinLongitude());
                    selectionArgs[3] = String.valueOf(coordinates.getMaxLongitude());
                    selectionArgs[4] = Long.toString(dayTime.setJulianDay(julianToday));

                    Cursor c = getContentResolver().query(
                            OTSContract.Search.CONTENT_URI,
                            new String[]{OTSContract.Search._ID},
                            QueryUtility.buildQuerySelectSearchByCoordinatesAndDate(),
                            selectionArgs,
                            null);

                    if (c.moveToFirst()) {
                        Log.v(CLASS_NAME, "Possui dados");
                        goToResults(true);
                    } else {
                        Log.v(CLASS_NAME, "Nao possui dados");
                        goToFilters();
                    }
                }
            }
        }, TIME_SPLASH);

    }

    private void goToResults(boolean foundCoordinates){
        Intent intent = new Intent();
        intent.setClass(SplashScreenActivity.this, ResultActivity.class);
        intent.putExtra(COORDINATES_FOUND, foundCoordinates);
        TaskStackBuilder.create(this).addNextIntentWithParentStack(intent).startActivities();
    }

    private void goToFilters(){
        Intent intent = new Intent();
        intent.setClass(SplashScreenActivity.this, FiltersActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v(CLASS_NAME, "entrou no onConnected, tempo = " + System.currentTimeMillis());

        try {
            createLocationRequest();
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } catch (Exception e) {
            Log.v(CLASS_NAME, getString(R.string.error_localization));
            disconnectFromLocationServices();
            finish();
        }
    }

    private void saveCoordinates() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong(OTSContract.SHARED_LATITUDE, Double.doubleToRawLongBits(lastLatitude));
        editor.putLong(OTSContract.SHARED_LONGITUDE, Double.doubleToRawLongBits(lastLongitude));

        editor.apply();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(CLASS_NAME, "conexao suspensa, erro = " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(CLASS_NAME, "falhou na conexao, erro = " + connectionResult.getErrorCode());

        Toast.makeText(getApplication(), getString(R.string.error_localization), Toast.LENGTH_LONG).show();
        disconnectFromLocationServices();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        disconnectFromLocationServices();
        super.onStop();
    }

    /**
     * Method to verify google play services on the device
     */
    private void findLocation() {
        Log.v(CLASS_NAME, "entrou em findLocation");

        if (Utility.isNetworkAvailable(this)) {
            Log.v(CLASS_NAME, "tem internet");
            buildGoogleApiClient();

        } else {
            Log.v(CLASS_NAME, "nao tem internet");
            setContentView(R.layout.activity_failure);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    public void continueWithoutMyLocation(View v) {
        Log.v("Debug", "Não possui localização");
        Intent intent = new Intent();
        intent.setClass(SplashScreenActivity.this, FiltersActivity.class);
        startActivity(intent);
    }

    private void createLocationRequest() {
        // Create the LocationRequest object
        Log.v(CLASS_NAME, "entrou no createLocationRequest, tempo = " + System.currentTimeMillis());

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(5 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    private void disconnectFromLocationServices() {
        Log.v(CLASS_NAME,"entrou no disconnectFromLocationServices");
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(CLASS_NAME, "entrou no onLocationChanged, tempo = " + System.currentTimeMillis());

        location.getLatitude();
        lastLatitude = location.getLatitude();
        lastLongitude = location.getLongitude();

        Log.i(CLASS_NAME, "latitude, onLocationChanged = " + lastLatitude);
        Log.i(CLASS_NAME, "longitude, onLocationChanged = " + lastLongitude);

        saveCoordinates();

        disconnectFromLocationServices();
    }
}
