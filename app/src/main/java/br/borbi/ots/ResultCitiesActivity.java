package br.borbi.ots;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.fragment.ResultCityFragment;


public class ResultCitiesActivity extends ActionBarActivity {

    public static final String ID_REL_SEARCH_CITY = "ID_REL_SEARCH_CITY";
    public static final String CITY_NAME = "CITY_NAME";

    private int idRelSearchCityId;
    private String strNameCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_result_cities);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        idRelSearchCityId = sp.getInt(OTSContract.KEY_REL_SEARCH_CITY, -1);
        strNameCity = sp.getString(OTSContract.KEY_CITY_NAME, "");

        ResultCityFragment resultCityFragment= (ResultCityFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_result_city);

        resultCityFragment.setIdRelSearchCity(idRelSearchCityId);
        resultCityFragment.setStrCityName(strNameCity);
    }

}
