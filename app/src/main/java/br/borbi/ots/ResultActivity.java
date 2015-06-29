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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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
}
