package br.borbi.ots.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;

import br.borbi.ots.MapsActivity;
import br.borbi.ots.R;
import br.borbi.ots.adapter.CitiesAdapter;
import br.borbi.ots.data.OTSContract;
import br.borbi.ots.model.CityResultSearchModel;
import br.borbi.ots.pojo.CityResultSearch;
import br.borbi.ots.utility.Utility;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Itamar on 16/06/2015.
 */
public class CitiesFragment extends Fragment {

    public static final String LOG_TAG = CitiesFragment.class.getSimpleName();

    private View mRootView;

    @BindView(R.id.listview_cities_empty) View mEmptyView;
    @BindView(R.id.listview_cities) ListView mListView;
    @BindView(R.id.btnSeeCities) Button mSeeCitiesBtn;
    
    public static final String[] TAG_COLUMNS = {
            OTSContract.Tag.TABLE_NAME + "." + OTSContract.Tag._ID,
            OTSContract.Tag.TABLE_NAME + "." + OTSContract.Tag.COLUMN_NAME_RESOURCE_NAME
    };

    public static final int INDEX_RESOURCE_NAME = 1;

    public CitiesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_cities, container, false);
        ButterKnife.bind(this, mRootView);
        
        final LinkedList<CityResultSearch> cities = CityResultSearchModel.list(getActivity());
        if(cities.size() > 0){
            CityResultSearch cityResultSearch = cities.get(0);
            if(cityResultSearch.getDistance() <=50){
                cityResultSearch.setIsFirstCity(true);
            }
        }

        fillAdapter(cities);

        if ((Utility.isGoogleMapsInstalled(getContext())) && (cities.size() > 0)) {

            mSeeCitiesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ArrayList<CityResultSearch> mList = new ArrayList<>(cities);

                    Intent mIntent = new Intent(getActivity(), MapsActivity.class);
                    mIntent.putExtra(MapsActivity.INDEX_LIST_CITIES, mList);
                    startActivity(mIntent);
                }
            });

        } else {
            mSeeCitiesBtn.setVisibility(View.GONE);
        }


        return mRootView;
    }

    private void fillAdapter(LinkedList<CityResultSearch> cities ){
        CitiesAdapter mMCitiesAdapter = new CitiesAdapter(cities, getActivity());

        mListView.setEmptyView(mEmptyView);
        mListView.setAdapter(mMCitiesAdapter);
    }
}
