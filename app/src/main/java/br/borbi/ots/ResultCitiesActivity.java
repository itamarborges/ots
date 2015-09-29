package br.borbi.ots;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdView;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.fragment.ResultCityFragment;
import br.borbi.ots.utility.Utility;


public class ResultCitiesActivity extends ActionBarActivity {

    public static final String ID_REL_SEARCH_CITY = "ID_REL_SEARCH_CITY";
    public static final String CITY_NAME = "CITY_NAME";

    int idRelSearchCityId;
    String strNameCity;

    AdView mAdView;

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

        mAdView = Utility.initializeAd(mAdView, this);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        idRelSearchCityId = sp.getInt(OTSContract.KEY_REL_SEARCH_CITY, -1);
        strNameCity = sp.getString(OTSContract.KEY_CITY_NAME, "");

        ResultCityFragment resultCityFragment= (ResultCityFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_result_city);

        resultCityFragment.setIdRelSearchCity(idRelSearchCityId);
        resultCityFragment.setStrCityName(strNameCity);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdView.pause();
    }

    protected void onResume(){
        super.onResume();
        mAdView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdView.destroy();
    }
}
