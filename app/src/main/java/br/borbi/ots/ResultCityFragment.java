package br.borbi.ots;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;

import br.borbi.ots.data.OTSContract;

/**
 * Created by Itamar on 16/06/2015.
 */
public class ResultCityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = ResultCityFragment.class.getSimpleName();
    private static final int RESULT_CITY_LOADER = 0;

    private static final String[] RESULT_CITY_COLUMNS = {
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_DATE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_MINIMUM_TEMPERATURE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_MAXIMUM_TEMPERATURE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_WEATHER_TYPE,
            OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch._ID
    };

    private ResultCityAdapter mResultCityAdapter;

    private int idRelSearchCity;

    private String strCityName;

    private ListView mListView;

    public String getStrCityName() {
        return strCityName;
    }

    public void setStrCityName(String strCityName) {
        this.strCityName = strCityName;
        if (mResultCityAdapter != null) {
            mResultCityAdapter.setStrCityName(strCityName);
        }
    }

    public int getIdRelSearchCity() {
        return idRelSearchCity;
    }

    public void setIdRelSearchCity(int idRelSearchCity) {
        this.idRelSearchCity = idRelSearchCity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ResultCityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mResultCityAdapter = new ResultCityAdapter(getActivity(), null, 0);
        mResultCityAdapter.setStrCityName(strCityName);

        View rootView = inflater.inflate(R.layout.fragment_result_city, container, false);
        View emptyView = rootView.findViewById(R.id.listview_result_city_empty);

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(mResultCityAdapter);
        gridview.setEmptyView(emptyView);


        // Get a reference to the ListView, and attach this adapter to it.
        /*
        mListView = (ListView) rootView.findViewById(R.id.listview_result_city);
        mListView.setEmptyView(emptyView);
        mListView.setAdapter(mResultCityAdapter);
*/


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

        mResultCityAdapter.setUseTodayLayout(mUseTodayLayout);
*/
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(RESULT_CITY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            String sortOrder = OTSContract.ResultSearch.TABLE_NAME + "." + OTSContract.ResultSearch.COLUMN_NAME_DATE +" ASC";

            //String locationSetting = Utility.getPreferredLocation(getActivity());
            Uri uriResultCity = OTSContract.ResultSearch.CONTENT_URI;

            String[] selectionArgs;
            String selection;

            selection = OTSContract.ResultSearch.COLUMN_NAME_REL_SEARCH_CITY_ID + " = ? ";
            selectionArgs = new String[]{String.valueOf(idRelSearchCity)};

            return new CursorLoader(getActivity(),
                    uriResultCity,
                    RESULT_CITY_COLUMNS,
                    selection,
                    selectionArgs,
                    sortOrder);
        }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mResultCityAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
            mResultCityAdapter.swapCursor(null);
    }

}
