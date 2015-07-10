package br.borbi.ots;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import br.borbi.ots.utility.Utility;


public class ResultCitiesActivity extends ActionBarActivity {

    public static final String ID_REL_SEARCH_CITY = "ID_REL_SEARCH_CITY";
    public static final String CITY_NAME = "CITY_NAME";

    int idRelSearchCityId;
    String strNameCity;

    private TextView mCityName;

    public String getStrNameCity() {
        return strNameCity;
    }

    public void setStrNameCity(String strNameCity) {
        this.strNameCity = strNameCity;
    }

    public int getIdRelSearchCityId() {
        return idRelSearchCityId;
    }

    public void setIdRelSearchCityId(int idRelSearchCityId) {
        this.idRelSearchCityId = idRelSearchCityId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_result_cities);
        mCityName = (TextView) findViewById(R.id.cityNameTextView);

        AdView mAdView = null;
        Utility.initializeAd(mAdView, this);

        Intent intent = getIntent();
        idRelSearchCityId = intent.getIntExtra(ID_REL_SEARCH_CITY, -1);
        strNameCity = intent.getStringExtra(CITY_NAME);

        ResultCityFragment resultCityFragment= (ResultCityFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_result_city);

        resultCityFragment.setIdRelSearchCity(idRelSearchCityId);
        resultCityFragment.setStrCityName(strNameCity);

        mCityName.setText(strNameCity);
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
