package br.borbi.ots.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import br.borbi.ots.fragment.CitiesFragment;
import br.borbi.ots.R;
import br.borbi.ots.ResultCitiesActivity;
import br.borbi.ots.data.OTSContract;
import br.borbi.ots.data.OTSProvider;

/**
 * Created by Itamar on 16/06/2015.
 */
public class CitiesAdapter extends CursorAdapter {

    private static final String LOG_TAG= CitiesAdapter.class.getSimpleName();

    HashMap<Integer, LinkedList> tagsCity = new HashMap<Integer, LinkedList>();

    public CitiesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public static class ViewHolder {
        public final TextView cityNameTextView;
        public final TextView tagTextView;
        public final LinearLayout layoutCities;

        public ViewHolder(View view) {
            cityNameTextView = (TextView) view.findViewById(R.id.list_item_city_name_textview);
            tagTextView = (TextView) view.findViewById(R.id.list_item_tag_textview);
            layoutCities = (LinearLayout) view.findViewById(R.id.layout_search_city);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_city, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        final String strCountryName = cursor.getString(CitiesFragment.INDEX_COUNTRY_NAME);
        final String strCityName = cursor.getString(CitiesFragment.INDEX_CITY_NAME);
        final int idResultSearchCity = cursor.getInt(CitiesFragment.INDEX_REL_SEARCH_CITY_ID);
        final int idCity = cursor.getInt(CitiesFragment.INDEX_CITY_ID);

        final String strLabelLocal = strCityName + " - " + strCountryName;

        // Find TextView and set the city name on it
        viewHolder.cityNameTextView.setText(strLabelLocal);

        if (!tagsCity.containsKey(idCity)) {

            String selection = OTSProvider.FILTER_BY_CITY;
            String[] selectionArgs = new String[]{String.valueOf(idCity)};

            String sortOrder = OTSContract.RelCityLanguage.TABLE_NAME + "." + OTSContract.RelCityLanguage.COLUMN_NAME_NAME + " ASC";

            //Recupera as tags
            Cursor c = context.getContentResolver().query(
                    OTSContract.CONTENT_URI_LIST_TAGS_FROM_A_CITY,
                    CitiesFragment.TAG_COLUMNS,
                    selection,
                    selectionArgs,
                    sortOrder);

            LinkedList<String> tagNames = new LinkedList<String>();
            String tag = "";
            String resourceName = "";
            while (c.moveToNext()) {
                resourceName = c.getString(CitiesFragment.INDEX_RESOURCE_NAME);

                tag = context.getString(context.getResources().getIdentifier(resourceName, "string", context.getPackageName()));

                if (!tag.isEmpty()) {
                    tagNames.add(tag);
                }
            }
            tagsCity.put(idCity, tagNames);
        }


        final LinkedList<String> tags = tagsCity.get(idCity);
        String tagsItem = "";
        String separador = "";

        for(String tag:tags)  {
            tagsItem = tagsItem.concat(separador + tag);
            separador = " - ";
        }

        viewHolder.tagTextView.setText(tagsItem);

        viewHolder.layoutCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ResultCitiesActivity.class);

                //Vai gravar no SharedPreferences as informações do elemento clicado
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor spe = sp.edit();

                spe.putString(OTSContract.KEY_CITY_NAME, strLabelLocal);
                spe.putInt(OTSContract.KEY_REL_SEARCH_CITY, idResultSearchCity);

                Set<String> tagsSetString = new HashSet<String>(tags);

                spe.putStringSet(OTSContract.KEY_CITY_TAGS, tagsSetString );

                spe.apply();

                context.startActivity(intent);
            }
        });
    }
}
