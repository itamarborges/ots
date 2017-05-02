package br.borbi.ots;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.fragment.ResultCityFragment;


public class ResultCitiesActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_result_cities);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int idRelSearchCityId = sp.getInt(OTSContract.KEY_REL_SEARCH_CITY, -1);
        String strNameCity = sp.getString(OTSContract.KEY_CITY_NAME, "");

        ResultCityFragment resultCityFragment= (ResultCityFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_result_city);

        resultCityFragment.setIdRelSearchCity(idRelSearchCityId);
        resultCityFragment.setStrCityName(strNameCity);
    }

}
