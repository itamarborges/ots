package br.borbi.ots;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Timer;
import java.util.TimerTask;

import br.borbi.ots.utility.ForwardUtility;
import br.borbi.ots.utility.LocationUtility;
import br.borbi.ots.utility.Utility;


public class SplashScreenActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String LOG_TAG = SplashScreenActivity.class.getName();
    public static final int MAX_DISTANCE_VALID = 100;

    private static final int TIME_SPLASH = 3000;

    private ImageView mImgSplash;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private static Double lastLongitude;
    private static Double lastLatitude;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mContext = this;

        LocationUtility.cleanSavedCoordinates(this);

        mImgSplash = (ImageView) findViewById(R.id.imageView);

        int img = ((int) (Math.random() * 10)) ;

        if (img % 2 == 0) {
            mImgSplash.setBackgroundResource(R.drawable.logo_novo_par); ;
        } else {
            mImgSplash.setBackgroundResource(R.drawable.logo_novo_impar);
        }

        if (Utility.isNetworkAvailable(this)) {
            findLocation();
        }

        //TODO EXCLUIR AS PROXIMAS 3 LINHAS AO DAR BUILD EM PRODUCAO!
        lastLatitude = -30.03306;

        lastLongitude = -51.23;

        LocationUtility.saveCoordinates(lastLatitude,lastLongitude, this);

        new Timer().schedule(new TimerTask() {

            public void run() {

                //Verify if there is any data in search and/or if this is valid data.
                //Meaning that:
                //- the table search is not empty and
                //- the date_end is before than today
                //- the current location is not too far from the place where the seach was originally made

                    if (lastLongitude == null || lastLatitude == null) {

                        Integer searchId = Utility.findSearchByDate(mContext);

                        if (searchId == null) {
                            ForwardUtility.goToFilters(mContext);
                        } else {
                            ForwardUtility.goToResults(false, searchId, mContext);
                        }

                    } else {
                        Integer searchId = Utility.findSearchByDateAndCoordinates(lastLatitude, lastLongitude, mContext);

                        if (searchId == null) {
                            ForwardUtility.goToFilters(mContext);
                        } else {
                            ForwardUtility.goToResults(true, searchId, mContext);
                        }
                    }
            }
        }, TIME_SPLASH);
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            mLocationRequest = LocationUtility.createLocationRequest();
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } catch (Exception e) {
            Log.e(LOG_TAG, getString(R.string.error_localization));
            LocationUtility.disconnectFromLocationServices(mGoogleApiClient,this);
            finish();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(LOG_TAG, "conexao suspensa, erro = " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "falhou na conexao, erro = " + connectionResult.getErrorCode());

        Toast.makeText(getApplication(), getString(R.string.error_localization), Toast.LENGTH_LONG).show();
        LocationUtility.disconnectFromLocationServices(mGoogleApiClient, this);
    }

    /**
     * Method to verify google play services on the device
     */
    private void findLocation() {

        if (Utility.isNetworkAvailable(this)) {
            buildGoogleApiClient();
        } else {
            // If there's no internet connection, forwards to failure screen.
            ForwardUtility.goToFailure(mContext,true);
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

    @Override
    public void onLocationChanged(Location location) {
        lastLatitude = location.getLatitude();
        lastLongitude = location.getLongitude();

        LocationUtility.saveCoordinates(lastLatitude, lastLongitude, this);
        LocationUtility.disconnectFromLocationServices(mGoogleApiClient, this);
    }
}
