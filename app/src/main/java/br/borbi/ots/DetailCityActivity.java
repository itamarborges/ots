package br.borbi.ots;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.ads.AdView;

import br.borbi.ots.fragment.DetailCityFragment;
import br.borbi.ots.utility.Utility;


public class DetailCityActivity extends ActionBarActivity {

    public static final String ID_RESULT_SEARCH = "ID_RESULT_SEARCH";
    public static final String CITY_NAME = "CITY_NAME";
    public static final String QTY_ITENS = "QTY_ITENS";
    public static final String RELATIVE_POSITION = "RELATIVE_POSITION";

    private int idResultSearch;
    private int mQtyItens;
    private int mRelativePosition;

    private float x1, y1, x2, y2;

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
        mQtyItens = intent.getIntExtra(QTY_ITENS, 0);
        mRelativePosition = intent.getIntExtra(RELATIVE_POSITION, -1);

        DetailCityFragment detailCityFragment = (DetailCityFragment)
              getSupportFragmentManager().findFragmentById(R.id.fragment_detail_city);

        detailCityFragment.setIdResultSearch(idResultSearch);
        detailCityFragment.setQtyItens(mQtyItens);
        detailCityFragment.setRelativePosition(mRelativePosition);
    }
}
