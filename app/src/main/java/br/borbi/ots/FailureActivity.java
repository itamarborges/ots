package br.borbi.ots;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.pojo.Coordinates;
import br.borbi.ots.utility.ForwardUtility;
import br.borbi.ots.utility.LocationUtility;
import br.borbi.ots.utility.QueryUtility;
import br.borbi.ots.utility.Utility;


public class FailureActivity extends ActionBarActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String LOG_TAG = FailureActivity.class.getName();

    private static final int WAIT_TIME = 5000;

    private Double lastLongitude;
    private Double lastLatitude;

    private boolean bTriedToConnect = false;
    private boolean bTryingToFindTheLocation = false;
    private boolean bLocalizationDetermined = false;

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failure);

        mContext = this;

        getApplication().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this);
    }

    public void tryToFindMeClick(View v) {

        bTriedToConnect = false;
        bTryingToFindTheLocation = false;
        bLocalizationDetermined = false;

        if(lastLatitude == null || lastLongitude == null){
            //iniciar thread para aguardar uns 5 s
            Timer timer = new Timer();
            timer.schedule(new MyTimerTask(), WAIT_TIME);

        }else{
            //vai para filtros ou resultado
            //mostrar pesquisa com localizacao
            Log.v(LOG_TAG, "achou coord ");
            forwardActivity();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.v(LOG_TAG, "onSharedPreferenceChanged, key = " + key);
        if(OTSContract.SHARED_LATITUDE.equals(key)){
            Log.v(LOG_TAG, "lat = " + sharedPreferences.getLong(OTSContract.SHARED_LATITUDE, Double.doubleToLongBits(0)));
            this.lastLatitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LATITUDE, Double.doubleToLongBits(0)));
        }else if(OTSContract.SHARED_LONGITUDE.equals(key)){
            Log.v(LOG_TAG, "long = " + sharedPreferences.getLong(OTSContract.SHARED_LONGITUDE, Double.doubleToLongBits(0)));
            this.lastLongitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LONGITUDE, Double.doubleToLongBits(0)));
        }
    }

    private class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(lastLatitude == null || lastLongitude == null) {
                        Log.v(LOG_TAG, "coord vazias");

                        Toast.makeText(getApplicationContext(),R.string.location_not_found_yet, Toast.LENGTH_LONG).show();

                    }else{
                        Log.v(LOG_TAG, "achou coord ");
                        forwardActivity();
                    }
                }
            });
        }
    }

    private void forwardActivity(){

        Toast.makeText(getApplicationContext(),R.string.location_found, Toast.LENGTH_LONG).show();

        Time dayTime = new Time();
        dayTime.setToNow();

        int julianToday = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

        Integer searchId = Utility.findSearchByDateAndCoordinates(julianToday, dayTime, lastLatitude, lastLongitude,mContext);

        if (searchId == null) {
            Log.v(LOG_TAG, "Nao possui dados");
            ForwardUtility.goToFilters(mContext);
        } else {
            Log.v(LOG_TAG, "Possui dados");
            ForwardUtility.goToResults(false, searchId,mContext);
        }
    }
}
