package br.borbi.ots;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.utility.Utility;

/**
 * Created by Itamar on 16/06/2015.
 */
public class ResultCityAdapter extends CursorAdapter {

    public ResultCityAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public static class ViewHolder {
        public final TextView dateTextView;
        public final TextView minTemperatureTextiView;
        public final TextView maxTemperatureTextView;
        public final ImageView weatherImageView;

        public ViewHolder(View view) {
            dateTextView = (TextView) view.findViewById(R.id.list_item_date_textview);
            minTemperatureTextiView = (TextView) view.findViewById(R.id.list_item_min_temperature_textview);
            maxTemperatureTextView = (TextView) view.findViewById(R.id.list_item_max_temperature_textview);
            weatherImageView = (ImageView) view.findViewById(R.id.weather_imageview);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_result_city, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int indexDate = cursor.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_DATE);
        Long date = cursor.getLong(indexDate);

        int indexMinTemperature = cursor.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_MINIMUM_TEMPERATURE);
        Double minTemperature = cursor.getDouble(indexMinTemperature);

        int indexMaxTemperature = cursor.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_MAXIMUM_TEMPERATURE);
        Double maxTemperature = cursor.getDouble(indexMaxTemperature);

        int indexIdWeatherType = cursor.getColumnIndex(OTSContract.ResultSearch.COLUMN_NAME_WEATHER_TYPE);
        final int idWeatherType = cursor.getInt(indexIdWeatherType);

        viewHolder.dateTextView.setText(Utility.getFormattedDate(date));
        viewHolder.minTemperatureTextiView.setText(context.getString(R.string.display_temperature, Integer.toString(Utility.roundCeil(minTemperature))));
        viewHolder.maxTemperatureTextView.setText(context.getString(R.string.display_temperature, Integer.toString(Utility.roundCeil(maxTemperature))));
        viewHolder.weatherImageView.setImageResource(Utility.getArtResourceForWeatherCondition(idWeatherType));

    }
}
