package br.borbi.ots;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.entity.Search;
import br.borbi.ots.model.SearchModel;
import br.borbi.ots.utility.CoordinatesUtillity;
import br.borbi.ots.utility.ForwardUtility;
import br.borbi.ots.utility.LocationUtility;
import br.borbi.ots.utility.Utility;


public class ResultActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();

        boolean foundCoordinates = intent.getBooleanExtra(ForwardUtility.COORDINATES_FOUND, true);
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        Double lastLatitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LATITUDE, Double.doubleToLongBits(0)));
        Double lastLongitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LONGITUDE, Double.doubleToLongBits(0)));

        if(!foundCoordinates && (lastLatitude==null || lastLongitude == null || lastLatitude == 0d || lastLongitude == 0d)){
            LocationUtility.buildGoogleApiClient(this);
        }
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

        switch (id) {
            case R.id.action_where_am_I:
                Intent mapIntent = ForwardUtility.goToMap(this);
                if(mapIntent != null){
                    startActivity(mapIntent);
                }
                break;
            case R.id.action_cities_list:
                Intent intent = new Intent(this, CitiesListActivity.class);
                startActivity(intent);
                break;
            case R.id.action_about:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationUtility.onConnected();
    }

    @Override
    public void onConnectionSuspended(int i) {
        LocationUtility.onConnectionSuspended(i);
        Toast.makeText(this, getString(R.string.location_not_found), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LocationUtility.onConnectionFailed(connectionResult);
        Toast.makeText(this,R.string.location_not_found,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        LocationUtility.onLocationChanged(location);
        Double lastLatitude = location.getLatitude();
        Double lastLongitude = location.getLongitude();

        Search search = SearchModel.findSearch(this);
        if (search != null){
            Double distance = CoordinatesUtillity.getDistance(lastLatitude,lastLongitude, search.getOriginLatitude(), search.getOriginLongitude());
            if (!Utility.isDistanceSmallerThanMinimumDistance(distance.intValue(), 50)){
                Toast.makeText(this, getString(R.string.location_not_found), Toast.LENGTH_LONG).show();
            }
        }
    }
}
