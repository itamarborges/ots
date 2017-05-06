package br.borbi.ots;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.LinkedList;

import br.borbi.ots.adapter.CitiesListAdapter;
import br.borbi.ots.model.CityModel;
import br.borbi.ots.model.CountryModel;
import br.borbi.ots.pojo.City;
import br.borbi.ots.pojo.Country;

public class CitiesListActivity extends AppCompatActivity {

    private static final String LOG_TAG = CitiesListActivity.class.getSimpleName();
    private ListView mListView;
    private View mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_list);

        Spinner countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
        mListView = (ListView) findViewById(R.id.listView);
        mEmptyView = findViewById(R.id.listview_list_cities_empty);

        final LinkedList<Country> listCountries = CountryModel.listCountries(getApplication());

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listCountries);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Create the list view and bind the adapter
        countrySpinner.setAdapter(arrayAdapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mEmptyView.setVisibility(View.INVISIBLE);

                City cityQuery = new City();
                cityQuery.setCountryId(listCountries.get(position).getId());

                LinkedList<City> listCities = CityModel.listCitiesWithTags(cityQuery, getApplication());

                fillAdapter(listCities);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fillAdapter(LinkedList<City> listCities) {
        CitiesListAdapter mMCitiesListAdapter = new CitiesListAdapter(listCities, getApplicationContext());
        mListView.setEmptyView(mEmptyView);
        mListView.setAdapter(mMCitiesListAdapter);
    }
}
