package br.borbi.ots.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Locale;

import br.borbi.ots.R;
import br.borbi.ots.adapter.CitiesAdapter;
import br.borbi.ots.data.OTSContract;
import br.borbi.ots.data.OTSProvider;

/**
 * Created by Itamar on 16/06/2015.
 */
public class CitiesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = CitiesFragment.class.getSimpleName();
    private CitiesAdapter mCitiesAdapter;

    private ListView mListView;

    private static final int CITIES_LOADER = 0;

    private static final String[] CITIES_COLUMNS = {
            OTSContract.RelCountryLanguage.TABLE_NAME + "." + OTSContract.RelCountryLanguage.COLUMN_NAME_NAME,
            OTSContract.RelCityLanguage.TABLE_NAME + "." + OTSContract.RelCityLanguage.COLUMN_NAME_NAME,
            OTSContract.RelSearchCity.TABLE_NAME + "." + OTSContract.RelSearchCity.COLUMN_NAME_SEARCH_ID,
            OTSContract.RelSearchCity.TABLE_NAME + "." + OTSContract.RelSearchCity._ID,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City._ID
    };

    // these indices must match the projection
    public static final int INDEX_COUNTRY_NAME = 0;
    public static final int INDEX_CITY_NAME = 1;
    public static final int INDEX_SEARCH_ID = 2;
    public static final int INDEX_REL_SEARCH_CITY_ID = 3;
    public static final int INDEX_CITY_ID = 4;

    public static final String[] TAG_COLUMNS = {
            OTSContract.Tag.TABLE_NAME + "." + OTSContract.Tag._ID,
            OTSContract.Tag.TABLE_NAME + "." + OTSContract.Tag.COLUMN_NAME_RESOURCE_NAME
    };

    // these indices must match the projection
    public static final int INDEX_TAG_ID = 0;
    public static final int INDEX_RESOURCE_NAME = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public CitiesFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mCitiesAdapter = new CitiesAdapter(getActivity(), null, 0);


        View rootView = inflater.inflate(R.layout.fragment_cities, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(R.id.listview_cities);
        View emptyView = rootView.findViewById(R.id.listview_cities_empty);
        mListView.setEmptyView(emptyView);
        mListView.setAdapter(mCitiesAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CITIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            String sortOrder = OTSContract.RelCityLanguage.TABLE_NAME + "." + OTSContract.RelCityLanguage.COLUMN_NAME_NAME +" ASC";

            //pick the language used by the device
            String language = Locale.getDefault().getLanguage();
            language = "por";
            //String locationSetting = Utility.getPreferredLocation(getActivity());
            Uri uriSearchByCities = OTSContract.CONTENT_URI_LIST_CITIES_BY_SEARCH;

            String[] selectionArgs;
            String selection;

            selection = OTSProvider.FILTER_BY_LOCALE;
            selectionArgs = new String[]{language};

            return new CursorLoader(getActivity(),
                    uriSearchByCities,
                    CITIES_COLUMNS,
                    selection,
                    selectionArgs,
                    sortOrder);
        }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mCitiesAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
            mCitiesAdapter.swapCursor(null);
    }
}
