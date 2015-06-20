package br.borbi.ots;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.borbi.ots.data.OTSContract;

/**
 * Created by Itamar on 16/06/2015.
 */
public class CitiesAdapter extends CursorAdapter {

    public CitiesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public static class ViewHolder {
        public final TextView cityNameTextView;
        public final LinearLayout layoutCities;

        public ViewHolder(View view) {
            cityNameTextView = (TextView) view.findViewById(R.id.list_item_city_name_textview);
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

        int indexCityName = cursor.getColumnIndex(OTSContract.RelCityLanguage.COLUMN_NAME_NAME);
        String strCityName = cursor.getString(indexCityName);

        int indexIdResultSearchCity = cursor.getColumnIndex(OTSContract.RelSearchCity._ID);
        final int idResultSearchCity = cursor.getInt(indexIdResultSearchCity);


        // Find TextView and set the city name on it
        viewHolder.cityNameTextView.setText(strCityName);

        viewHolder.layoutCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ResultCitiesActivity.class);
                intent.putExtra(ResultCitiesActivity.ID_REL_SEARCH_CITY, idResultSearchCity);

                context.startActivity(intent);
            }
        });





    }
}
