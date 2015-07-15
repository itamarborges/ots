package br.borbi.ots;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import br.borbi.ots.adapter.CitiesAdapter;
import br.borbi.ots.data.OTSContract;
import br.borbi.ots.data.OTSProvider;
import br.borbi.ots.fragment.CitiesFragment;
import br.borbi.ots.utility.QueryUtility;
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

        Integer minimumDistance = intent.getIntExtra(SearchActivity.MINIMUM_DISTANCE,0);
        if(minimumDistance.intValue() == 0){
            Integer searchId = intent.getIntExtra(SplashScreenActivity.SEARCH_ID,0);

            String[] selectionArgs = new String[]{searchId.toString()};
            Cursor c = getContentResolver().query(
                    OTSContract.Search.CONTENT_URI,
                    new String[]{OTSContract.Search.COLUMN_NAME_RADIUS},
                    OTSProvider.FILTER_BY_SEARCH_ID,
                    selectionArgs,
                    null);
            if (c.moveToFirst()) {
                minimumDistance = c.getInt(c.getColumnIndex(OTSContract.Search.COLUMN_NAME_RADIUS));
            }
        }

        CitiesFragment citiesFragment = (CitiesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_cities);
        citiesFragment.setMininumDistance(minimumDistance);
    }
}
