package br.borbi.ots;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import java.util.LinkedList;

import br.borbi.ots.adapter.CitiesListAdapter;
import br.borbi.ots.data.OTSContract;
import br.borbi.ots.model.CityModel;
import br.borbi.ots.pojo.City;
import br.borbi.ots.utility.Utility;

public class CitiesListActivity extends ActionBarActivity {

    private CitiesListAdapter mCitiesListAdapter;
    private ListView mListView;
    private View mEmptyView;

    // these indices must match the projection
    public static final int INDEX_CITY_ID = 0;
    public static final int INDEX_CITY_TRANSLATION_FILE_KEY = 1;

    Spinner mCountrySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_list);

        AdView mAdView = null;
        Utility.initializeAd(mAdView, this);

        mCountrySpinner = (Spinner) findViewById(R.id.countrySpinner);
        mListView = (ListView) findViewById(R.id.listView);
        mEmptyView = (TextView) findViewById(R.id.listview_list_cities_empty);


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

                City cityQuery = new City();
                cityQuery.setCountryId((int) id);

                LinkedList<City> listCities = CityModel.listCitiesWithTags(cityQuery, getApplication());

                fillAdapter(listCities);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fillAdapter(LinkedList<City> listCities) {
        mCitiesListAdapter = new CitiesListAdapter(listCities, getApplicationContext());
        mListView.setEmptyView(mEmptyView);
        mListView.setAdapter(mCitiesListAdapter);
    }
}
