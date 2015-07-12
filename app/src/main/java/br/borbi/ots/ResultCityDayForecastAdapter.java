package br.borbi.ots;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

import br.borbi.ots.enums.WeatherType;
import br.borbi.ots.pojo.DayForecast;
import br.borbi.ots.utility.Utility;

/**
 * Created by Gabriela on 10/07/2015.
 */
public class ResultCityDayForecastAdapter extends BaseAdapter{

    private static final String LOG_TAG= ResultCityDayForecastAdapter.class.getSimpleName();

    LinkedList<DayForecast> forecasts;

    private String strCityName;

    private final Context mContext;

    public ResultCityDayForecastAdapter(LinkedList<DayForecast> list, Context mContext) {
        this.forecasts = list;
        this.mContext = mContext;
    }

    public String getStrCityName() {
        return strCityName;
    }

    public void setStrCityName(String strCityName) {
        this.strCityName = strCityName;
    }

    @Override
    public int getCount() {
        if(forecasts== null){
            return 0;
        }
        return forecasts.size();
    }

    @Override
    public Object getItem(int position) {
        if(forecasts== null) {
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

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_result_city, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        final DayForecast dayForecast = (DayForecast)getItem(position);
        if(dayForecast == null){
            viewHolder.dateTextView.setText("");
            viewHolder.minTemperatureTextiView.setText("");
            viewHolder.weatherImageView.setImageResource(-1);
            viewHolder.detailsButton.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.dateTextView.setText(Utility.getFormattedDateMonth(dayForecast.getDate()));
            viewHolder.minTemperatureTextiView.setText(mContext.getString(R.string.display_min_max_temperature, Integer.toString(Utility.roundCeil(dayForecast.getMinTemperature())), Integer.toString(Utility.roundCeil(dayForecast.getMaxTemperature()))));
            viewHolder.weatherImageView.setImageResource(Utility.getSmallArtResourceForWeatherCondition(WeatherType.getId(dayForecast.getWeatherType())));

            viewHolder.layoutResultCityLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DetailCityActivity.class);
                    intent.putExtra(DetailCityActivity.ID_RESULT_SEARCH, dayForecast.getId());
                    intent.putExtra(DetailCityActivity.CITY_NAME, strCityName);

                    mContext.startActivity(intent);

                    Toast.makeText(mContext, "Clicou em" + dayForecast.getId(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        return convertView;
    }

    public static class ViewHolder {
        public final TextView dateTextView;
        public final TextView minTemperatureTextiView;
        public final ImageView weatherImageView;
        public final Button detailsButton;
        public final LinearLayout layoutResultCityLinearLayout;

        public ViewHolder(View view) {
            dateTextView = (TextView) view.findViewById(R.id.list_item_date_textview);
            minTemperatureTextiView = (TextView) view.findViewById(R.id.list_item_min_temperature_textview);
            weatherImageView = (ImageView) view.findViewById(R.id.weather_imageview);
            layoutResultCityLinearLayout = (LinearLayout) view.findViewById(R.id.layout_result_search_city);
            detailsButton = (Button) view.findViewById(R.id.list_item_details_button);
        }
    }



}
