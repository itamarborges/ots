package br.borbi.ots;

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
            OTSContract.RelCityLanguage.TABLE_NAME + "." + OTSContract.RelCityLanguage.COLUMN_NAME_NAME,
            OTSContract.RelSearchCity.TABLE_NAME + "." + OTSContract.RelSearchCity.COLUMN_NAME_SEARCH_ID,
            OTSContract.City.TABLE_NAME + "." + OTSContract.City._ID
    };

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
        // We'll call our MainActivity
/*        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    String locationSetting = Utility.getPreferredLocation(getActivity());
                    ((Callback) getActivity())
                            .onItemSelected(WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                                    locationSetting, cursor.getLong(COL_WEATHER_DATE)
                            ));
                }
                mPosition = position;
            }
        });

        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things.  It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // actually *lost*.
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        mCitiesAdapter.setUseTodayLayout(mUseTodayLayout);
*/
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
