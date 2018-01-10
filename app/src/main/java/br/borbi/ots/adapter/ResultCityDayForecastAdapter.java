package br.borbi.ots.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;

import br.borbi.ots.DetailCityActivity;
import br.borbi.ots.R;
import br.borbi.ots.enums.WeatherType;
import br.borbi.ots.pojo.DayForecast;
import br.borbi.ots.utility.Utility;

/**
 * Created by Gabriela on 10/07/2015.
 */
public class ResultCityDayForecastAdapter extends BaseAdapter {

    private final LinkedList<DayForecast> forecasts;

    private String cityName;

    private int mQtyItens;

    private final Context mContext;

    public ResultCityDayForecastAdapter(LinkedList<DayForecast> list, Context mContext) {
        this.forecasts = list;
        this.mContext = mContext;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public int getCount() {
        if (forecasts == null) {
            return 0;
        }
        return forecasts.size();
    }

    @Override
    public Object getItem(int position) {
        if (forecasts == null) {
            return null;
        }
        return forecasts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_result_city, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final DayForecast dayForecast = (DayForecast) getItem(position);
        if (dayForecast == null) {
            viewHolder.dateTextView.setText("");
            viewHolder.minTemperatureTextView.setText("");
            viewHolder.maxTemperatureTextView.setText("");
            viewHolder.weatherImageView.setImageResource(R.drawable.no_border);
            viewHolder.layoutResultCityLinearLayout.setBackgroundResource(R.drawable.no_border);
            viewHolder.moreDetailsTextView.setText("");
            viewHolder.moreDetailsTextView.setBackgroundResource(R.drawable.no_border);
        } else {
            Integer minTemperature = Utility.roundCeil(dayForecast.getMinTemperature());
            Integer maxTemperature = Utility.roundCeil(dayForecast.getMaxTemperature());
            if (minTemperature.intValue() == maxTemperature.intValue()) {
                maxTemperature++;
            }

            if (Utility.usesFahrenheit(mContext)) {
                minTemperature = Utility.convertCelsiusToFarenheit(minTemperature);
                maxTemperature = Utility.convertCelsiusToFarenheit(maxTemperature);
            }

            viewHolder.dateTextView.setText(Utility.getFormattedDateMonth(dayForecast.getDate()));
            viewHolder.minTemperatureTextView.setText(mContext.getString(R.string.display_temperature, Integer.toString(minTemperature)));
            viewHolder.maxTemperatureTextView.setText(mContext.getString(R.string.display_temperature, Integer.toString(maxTemperature)));
            viewHolder.weatherImageView.setImageResource(Utility.getSmallArtResourceForWeatherCondition(WeatherType.getId(dayForecast.getWeatherType())));

            viewHolder.layoutResultCityLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DetailCityActivity.class);
                    intent.putExtra(DetailCityActivity.ID_RESULT_SEARCH, dayForecast.getId());
                    intent.putExtra(DetailCityActivity.CITY_NAME, cityName);
                    intent.putExtra(DetailCityActivity.QTY_ITENS, mQtyItens);
                    intent.putExtra(DetailCityActivity.RELATIVE_POSITION, dayForecast.getPosition());
                    mContext.startActivity(intent);
                }
            });
        }

        return convertView;
    }

    public static class ViewHolder {
        public final TextView dateTextView;
        public final TextView minTemperatureTextView;
        public final TextView maxTemperatureTextView;
        public final ImageView weatherImageView;
        public final LinearLayout layoutResultCityLinearLayout;
        public final TextView moreDetailsTextView;

        public ViewHolder(View view) {
            dateTextView = (TextView) view.findViewById(R.id.list_item_date_textview);
            minTemperatureTextView = (TextView) view.findViewById(R.id.list_item_min_temperature_textview);
            maxTemperatureTextView = (TextView) view.findViewById(R.id.list_item_max_temperature_textview);
            weatherImageView = (ImageView) view.findViewById(R.id.weather_imageview);
            layoutResultCityLinearLayout = (LinearLayout) view.findViewById(R.id.layout_result_search_city);
            moreDetailsTextView = (TextView)view.findViewById(R.id.list_item_more_details);
        }
    }

    public void setQtyItens(int mQtyItens) {
        this.mQtyItens = mQtyItens;
    }
}
