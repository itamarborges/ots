package br.borbi.ots;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.utility.Utility;


public class ResultActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        AdView mAdView = null;
        Utility.initializeAd(mAdView, this);

        Intent intent = getIntent();

        boolean foundCoordinates = intent.getBooleanExtra(SplashScreenActivity.COORDINATES_FOUND, true);
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        Double lastLatitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LATITUDE, Double.doubleToLongBits(0)));
        Double lastLongitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LONGITUDE, Double.doubleToLongBits(0)));

        if(!foundCoordinates && lastLatitude==null){
            Toast.makeText(this,R.string.location_not_found,Toast.LENGTH_LONG).show();
        }
    }
}
