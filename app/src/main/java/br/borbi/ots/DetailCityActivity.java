package br.borbi.ots;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import br.borbi.ots.fragment.DetailCityFragment;


public class DetailCityActivity extends ActionBarActivity {

    public static final String ID_RESULT_SEARCH = "ID_RESULT_SEARCH";
    public static final String CITY_NAME = "CITY_NAME";
    public static final String QTY_ITENS = "QTY_ITENS";
    public static final String RELATIVE_POSITION = "RELATIVE_POSITION";

    private int mIdResultSearch;

    public int getidResultSearch() {
        return mIdResultSearch;
    }

    public void setidResultSearch(int idResultSearch) {
        this.mIdResultSearch = idResultSearch;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_city);

        Intent intent = getIntent();
        mIdResultSearch = intent.getIntExtra(ID_RESULT_SEARCH, -1);
        int mMQtyItens = intent.getIntExtra(QTY_ITENS, 0);
        int mMRelativePosition = intent.getIntExtra(RELATIVE_POSITION, -1);

        DetailCityFragment newDetailCityFragment = new DetailCityFragment();

        newDetailCityFragment.setIdResultSearch(mIdResultSearch);
        newDetailCityFragment.setQtyItens(mMQtyItens);
        newDetailCityFragment.setRelativePosition(mMRelativePosition);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_layout, newDetailCityFragment);
        fragmentTransaction.commit();


    }
}
