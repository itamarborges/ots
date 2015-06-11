package br.borbi.ots;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.pojo.Coordinates;


public class FailureActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String CLASS_NAME = FailureActivity.class.getName();

    private static GoogleApiClient mGoogleApiClient;
    private static Location mLastLocation;

    private double lastLongitude;
    private double lastLatitude;

    private boolean bTriedToConnect = false;
    private boolean bTryingToFindTheLocation = false;
    private boolean bLocalizationDetermined = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failure);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_failure, menu);
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

    public void tryToFindMeClick(View v) {

        bTriedToConnect = false;
        bTryingToFindTheLocation = false;
        bLocalizationDetermined = false;

        do {
            buildGoogleApiClient();
        } while (bTriedToConnect);


        if (!bLocalizationDetermined) {
            Toast.makeText(getApplication(), "Não foi possível determinar a sua localização. Tente novamente !", Toast.LENGTH_SHORT).show();
        } else {
            Time dayTime = new Time();
            dayTime.setToNow();

            int julianToday = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            Coordinates coordinates = new Coordinates(lastLatitude,lastLongitude, SplashScreenActivity.MAX_DISTANCE_VALID);

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
                intent.setClass(getApplication(), FiltersActivity.class);
                startActivity(intent);
            } else {
                Log.v("Debug", "Não possui dados");
                Intent intent = new Intent();
                intent.setClass(getApplication(), FiltersActivity.class);
                startActivity(intent);
            }
        }

    }

    protected synchronized void buildGoogleApiClient() {
        if (!bTryingToFindTheLocation) {
            bTryingToFindTheLocation = true;
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            bTriedToConnect = true;
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
                bLocalizationDetermined = true;
            } else {
                lastLatitude = OTSContract.INDETERMINATED_VALUE;
                lastLongitude = OTSContract.INDETERMINATED_VALUE;
            }
            mGoogleApiClient.disconnect();
        } catch (Exception e) {
            Log.v(CLASS_NAME, getString(R.string.error_localization));
            bLocalizationDetermined = false;
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.e(CLASS_NAME, "conexao suspensa, erro = " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        bTriedToConnect = true;
        bLocalizationDetermined = false;
        mGoogleApiClient.disconnect();
        Log.e(CLASS_NAME, "falhou na conexao, erro = " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    protected void onStop() {
        super.onStop();
    }
}
