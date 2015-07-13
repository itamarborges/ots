package br.borbi.ots;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdView;

import br.borbi.ots.fragment.DetailCityFragment;
import br.borbi.ots.utility.Utility;


public class DetailCityActivity extends ActionBarActivity {

    public static final String ID_RESULT_SEARCH = "ID_RESULT_SEARCH";
    public static final String CITY_NAME = "CITY_NAME";

    private int idResultSearch;
    private String strCityName;

    public String getStrCityName() {
        return strCityName;
    }

    public void setStrCityName(String strCityName) {
        this.strCityName = strCityName;
    }

    public int getidResultSearch() {
        return idResultSearch;
    }

    public void setidResultSearch(int idResultSearch) {
        this.idResultSearch = idResultSearch;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_city);

        AdView mAdView = null;
        Utility.initializeAd(mAdView, this);

        Intent intent = getIntent();
        idResultSearch = intent.getIntExtra(ID_RESULT_SEARCH, -1);
        strCityName = intent.getStringExtra(CITY_NAME);

        DetailCityFragment detailCityFragment = (DetailCityFragment)
              getSupportFragmentManager().findFragmentById(R.id.fragment_detail_city);

        detailCityFragment.setidResultSearch(idResultSearch);


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
