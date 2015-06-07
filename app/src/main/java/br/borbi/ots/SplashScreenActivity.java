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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Timer;
import java.util.TimerTask;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.pojo.Coordinates;


public class SplashScreenActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String CLASS_NAME = SplashScreenActivity.class.getName();
    public static final int MAX_DISTANCE_VALID = 100;

    private static GoogleApiClient mGoogleApiClient;
    private static Location mLastLocation;

    private static double lastLongitude;
    private static double lastLatitude;

    public static final int TIME_SPLASH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Inicializa API Google Services
        buildGoogleApiClient();

        new Timer().schedule(new TimerTask() {

            public void run() {
                finish();

                //Verify if there is any data in search and/or if this is valid data.
                //Meaning that:
                //- the table search is not empty and
                //- the date_end is before than today
                //- the current location is not too far from the place where the seach was originally made
                Time dayTime = new Time();
                dayTime.setToNow();

                int julianToday = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

                Coordinates coordinates = new Coordinates(lastLatitude,lastLongitude, MAX_DISTANCE_VALID);

                StringBuffer whereClause = new StringBuffer(
                        OTSContract.Search.COLUMN_NAME_ORIGIN_LAT).append(" >= ?")
                        .append(" AND ")
                        .append(OTSContract.Search.COLUMN_NAME_ORIGIN_LAT).append(" <= ?")
                        .append(" AND ")
                        .append(OTSContract.Search.COLUMN_NAME_ORIGIN_LONG).append(" >= ?")
                        .append(" AND ")
                        .append(OTSContract.Search.COLUMN_NAME_ORIGIN_LONG).append(" <= ?")
                        .append(" AND ")
                        .append(OTSContract.Search.COLUMN_NAME_DATE_END).append(" >= ?");


                String[] selectionArgs = new String[5];
                selectionArgs[0] = String.valueOf(coordinates.getMinLatitude());
                selectionArgs[1] = String.valueOf(coordinates.getMaxLatitude());
                selectionArgs[2] = String.valueOf(coordinates.getMinLongitude());
                selectionArgs[3] = String.valueOf(coordinates.getMaxLongitude());
                selectionArgs[4] = Long.toString(dayTime.setJulianDay(julianToday));

                Cursor c = getContentResolver().query(
                        OTSContract.Search.CONTENT_URI,
                        new String[]{OTSContract.Search._ID},
                        whereClause.toString(),
                        selectionArgs,
                        null);

                if (c.moveToFirst()) {
                    Log.v("Debug", "Possui dados");
                    Intent intent = new Intent();
                    intent.setClass(SplashScreenActivity.this, FiltersActivity.class);
                    startActivity(intent);
                } else {
                    Log.v("Debug", "Não possui dados");
                    Intent intent = new Intent();
                    intent.setClass(SplashScreenActivity.this, FiltersActivity.class);
                    startActivity(intent);
                }

            }
        }, TIME_SPLASH);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                Log.i(CLASS_NAME, "mLastLocation  nao e null");
                lastLatitude = mLastLocation.getLatitude();
                lastLongitude = mLastLocation.getLongitude();

                Log.i(CLASS_NAME, "latitude = " + lastLatitude);
                Log.i(CLASS_NAME, "longitude = " + lastLongitude);

                SharedPreferences sharedPreferences = getApplication().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putLong(OTSContract.SHARED_LATITUDE, Double.doubleToRawLongBits(lastLatitude));
                editor.putLong(OTSContract.SHARED_LONGITUDE, Double.doubleToRawLongBits(lastLongitude));

                editor.commit();
            } else {
                lastLatitude = OTSContract.INDETERMINATED_VALUE;
                lastLongitude = OTSContract.INDETERMINATED_VALUE;
            }



        } catch (Exception e) {
            Log.v(CLASS_NAME, getString(R.string.error_localization));
            finish();
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(CLASS_NAME, "conexao suspensa, erro = " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(CLASS_NAME, "falhou na conexao, erro = " + connectionResult.getErrorCode());

        Toast.makeText(getApplication(), getString(R.string.error_localization), Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}
