package br.borbi.ots;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
    public static final int ONE_MINUTE_IN_MILISECOND = 1000 * 60;
    public static final int MAX_DISTANCE_VALID = 100;

    private static GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private boolean mSearchedLocation = false;
    private boolean mFoundLocation = false;

    private static Double lastLongitude;
    private static Double lastLatitude;

    public static final int MIN_TIME_SPLASH = 3000;
    public static final int MAX_TIME_SPLASH = 13000;
    public static final int TIME_SPLASH = 3000;
    public static final String COORDINATES_FOUND = "COORDINATES_FOUND";

    private Long initialTime;
    private Long currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initialTime = currentTime = System.currentTimeMillis();

        findLocation();
        Log.v(CLASS_NAME, "começou em :" + currentTime.toString());


        new Timer().schedule(new TimerTask() {

            public void run() {
                //finish();

                //Verify if there is any data in search and/or if this is valid data.
                //Meaning that:
                //- the table search is not empty and
                //- the date_end is before than today
                //- the current location is not too far from the place where the seach was originally made
                Time dayTime = new Time();
                dayTime.setToNow();

                int julianToday = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

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
                    Intent intent = new Intent();

                    intent.setClass(SplashScreenActivity.this, ResultActivity.class);
                    if (lastLatitude == null || lastLongitude == null) {
                        intent.putExtra(COORDINATES_FOUND,false);
                    }

                    startActivity(intent);
                } else {
                    Log.v(CLASS_NAME, "Nao possui dados");
                    Intent intent = new Intent();
                    intent.setClass(SplashScreenActivity.this, FiltersActivity.class);
                    startActivity(intent);
                }

            }
        }, TIME_SPLASH);


        Log.v(CLASS_NAME, "terminou em: " + currentTime.toString());

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v(CLASS_NAME, "entrou no onConnected, tempo = " + System.currentTimeMillis());

        try {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


            if (mLastLocation == null) {

                Log.v(CLASS_NAME, "mLastLocation e null");


                lastLatitude = Double.valueOf(OTSContract.INDETERMINATED_VALUE);
                lastLongitude = Double.valueOf(OTSContract.INDETERMINATED_VALUE);

                //createLocationRequest();
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            } else {
                Log.i(CLASS_NAME, "mLastLocation  nao e null");
                lastLatitude = mLastLocation.getLatitude();
                lastLongitude = mLastLocation.getLongitude();

                Log.i(CLASS_NAME, "latitude = " + lastLatitude);
                Log.i(CLASS_NAME, "longitude = " + lastLongitude);

                saveCoordinates();

                mSearchedLocation = true;
                mFoundLocation = true;

                disconnectFromLocationServices();
            }

        } catch (Exception e) {
            Log.v(CLASS_NAME, getString(R.string.error_localization));
            mGoogleApiClient.disconnect();
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
        mGoogleApiClient.disconnect();
        mSearchedLocation = true;
    }

    @Override
    protected void onStart() {
        //mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
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
        /*
          int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
            //The device has access to googlePlayService
            if (resultCode == ConnectionResult.SUCCESS) {

                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
                mGoogleApiClient.connect();
                mFoundLocation = true;
                mSearchedLocation = true;
            } else {

            };
            */
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

        createLocationRequest();

    }

    public void continueWithoutMyLocation(View v) {
        Log.v("Debug", "Não possui localização");
        Intent intent = new Intent();
        intent.setClass(SplashScreenActivity.this, FiltersActivity.class);
        startActivity(intent);
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

        mSearchedLocation = true;
        mFoundLocation = true;
        disconnectFromLocationServices();
    }

    private void createLocationRequest() {
        // Create the LocationRequest object

        Log.v(CLASS_NAME, "entrou no createLocationRequest, tempo = " + System.currentTimeMillis());

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_LOW_POWER)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    private void disconnectFromLocationServices() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }
}
