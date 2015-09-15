package br.borbi.ots;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.utility.Utility;

public class CitiesListActivity extends ActionBarActivity {

    Spinner mCountrySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_list);

        AdView mAdView = null;
        Utility.initializeAd(mAdView, this);

        mCountrySpinner = (Spinner) findViewById(R.id.countrySpinner);

        String[] fromColumns = {OTSContract.Country.COLUMN_NAME_COUNTRY_CODE};

        Cursor c = getContentResolver().query(
                OTSContract.Country.CONTENT_URI,
                new String[]{OTSContract.Country.COLUMN_NAME_COUNTRY_CODE, OTSContract.Country._ID},
                null,
                null,
                OTSContract.Country.COLUMN_NAME_COUNTRY_CODE);


// View IDs to map the columns (fetched above) into
        int[] toViews = {
                android.R.id.text1
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, // context
                android.R.layout.simple_spinner_item, // layout file
                c, // DB cursor
                fromColumns, // data to bind to the UI
                toViews, // views that'll represent the data from `fromColumns`
                0
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Create the list view and bind the adapter
        mCountrySpinner.setAdapter(adapter);

        mCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Cursor c = getContentResolver().query(
                        OTSContract.City.CONTENT_URI,
                        new String[]{OTSContract.Country.COLUMN_NAME_COUNTRY_CODE, OTSContract.Country._ID},
                        null,
                        null,
                        OTSContract.Country.COLUMN_NAME_COUNTRY_CODE);

            }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cities_list, menu);
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
