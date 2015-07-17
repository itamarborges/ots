package br.borbi.ots;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.data.OTSProvider;
import br.borbi.ots.fragment.CitiesFragment;
import br.borbi.ots.utility.ForwardUtility;
import br.borbi.ots.utility.Utility;


public class ResultActivity extends ActionBarActivity {

    private static String LOG_TAG = ResultActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        AdView mAdView = null;
        Utility.initializeAd(mAdView, this);

        Intent intent = getIntent();

        boolean foundCoordinates = intent.getBooleanExtra(ForwardUtility.COORDINATES_FOUND, true);
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        Double lastLatitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LATITUDE, Double.doubleToLongBits(0)));
        Double lastLongitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LONGITUDE, Double.doubleToLongBits(0)));

        if(!foundCoordinates && (lastLatitude==null || lastLongitude == null || lastLatitude == 0d || lastLongitude == 0d)){
            Toast.makeText(this,R.string.location_not_found,Toast.LENGTH_LONG).show();
        }

        Integer minimumDistance = intent.getIntExtra(SearchActivity.MINIMUM_DISTANCE,0);
        if(minimumDistance.intValue() == 0){
            Integer searchId = intent.getIntExtra(ForwardUtility.SEARCH_ID,0);

            String[] selectionArgs = new String[]{searchId.toString()};
            Cursor c = getContentResolver().query(
                    OTSContract.Search.CONTENT_URI,
                    new String[]{OTSContract.Search.COLUMN_NAME_RADIUS, OTSContract.Search.COLUMN_NAME_ORIGIN_LAT, OTSContract.Search.COLUMN_NAME_ORIGIN_LONG},
                    OTSProvider.FILTER_BY_SEARCH_ID,
                    selectionArgs,
                    null);
            if (c.moveToFirst()) {
                minimumDistance = c.getInt(c.getColumnIndex(OTSContract.Search.COLUMN_NAME_RADIUS));
                lastLatitude = c.getDouble(c.getColumnIndex(OTSContract.Search.COLUMN_NAME_ORIGIN_LAT));
                lastLongitude = c.getDouble(c.getColumnIndex(OTSContract.Search.COLUMN_NAME_ORIGIN_LONG));
            }
        }

        CitiesFragment citiesFragment = (CitiesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_cities);
        citiesFragment.setMininumDistance(minimumDistance);
        citiesFragment.setLastLatitude(lastLatitude);
        citiesFragment.setLastLongitude(lastLongitude);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filters, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_where_am_I) {
            SharedPreferences sharedPreferences = getApplication().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);

            Double lastLatitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LATITUDE, Double.doubleToLongBits(0)));
            Double lastLongitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LONGITUDE, Double.doubleToLongBits(0)));

            if((lastLatitude == null && lastLongitude == null) || (lastLatitude.doubleValue() == 0d && lastLongitude.doubleValue() == 0d)){
                ForwardUtility.goToFailure(getApplicationContext());
            } else {
                Uri geoLocation = Uri.parse("geo:" + lastLatitude + "," + lastLongitude);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(geoLocation);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Log.d(FiltersActivity.CLASS_NAME, "Couldn't call " + geoLocation.toString() + ", no receiving apps installed!");
                }
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
