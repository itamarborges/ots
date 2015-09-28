package br.borbi.ots;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import java.util.LinkedList;

import br.borbi.ots.adapter.CitiesListAdapter;
import br.borbi.ots.model.CityModel;
import br.borbi.ots.model.CountryModel;
import br.borbi.ots.pojo.City;
import br.borbi.ots.pojo.Country;
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

        LinkedList<Country> listCountries = CountryModel.listCountries(getApplication());

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listCountries);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Create the list view and bind the adapter
        mCountrySpinner.setAdapter(arrayAdapter);

        mCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mEmptyView.setVisibility(View.INVISIBLE);

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
