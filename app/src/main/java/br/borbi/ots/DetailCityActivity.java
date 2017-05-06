package br.borbi.ots;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import br.borbi.ots.fragment.DetailCityFragment;

public class DetailCityActivity extends AppCompatActivity {

    public static final String ID_RESULT_SEARCH = "ID_RESULT_SEARCH";
    public static final String CITY_NAME = "CITY_NAME";
    public static final String QTY_ITENS = "QTY_ITENS";
    public static final String RELATIVE_POSITION = "RELATIVE_POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_city);

        Intent intent = getIntent();
        int idResultSearch = intent.getIntExtra(ID_RESULT_SEARCH, -1);
        int qtyItens = intent.getIntExtra(QTY_ITENS, 0);
        int relativePosition = intent.getIntExtra(RELATIVE_POSITION, -1);

        DetailCityFragment newDetailCityFragment = new DetailCityFragment();

        newDetailCityFragment.setIdResultSearch(idResultSearch);
        newDetailCityFragment.setQtyItens(qtyItens);
        newDetailCityFragment.setRelativePosition(relativePosition);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_layout, newDetailCityFragment);
        fragmentTransaction.commit();
    }
}
