package br.borbi.ots.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import br.borbi.ots.R;
import br.borbi.ots.pojo.City;

/**
 * Created by Itamar on 16/06/2015.
 */
public class CitiesListAdapter extends BaseAdapter{

    private static final String LOG_TAG= CitiesListAdapter.class.getSimpleName();

    private LinkedList<City> mCities;
    private final Context mContext;

    public CitiesListAdapter(LinkedList<City> cities, Context context) {
        mCities = cities;
        mContext = context;
    }

    @Override
    public int getCount() {
        if(mCities == null){
            return 0;
        }
        return mCities.size();
    }

    @Override
    public City getItem(int position) {
        if(mCities == null){
            return null;
        }
        return mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_cities_list, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        final City city = getItem(position);

        final String strCityName = city.getName();

        // Find TextView and set the city name on it
        viewHolder.cityNameTextView.setText(strCityName);

        final List<String> tags = city.getTags();
        String tagsItem = "";
        String separador = "";

        for(String tag:tags)  {
            tagsItem = tagsItem.concat(separador + tag);
            separador = " - ";
        }

        viewHolder.tagTextView.setText(tagsItem);


        return convertView;
    }

    public static class ViewHolder {
        public final TextView cityNameTextView;
        public final TextView tagTextView;

        public ViewHolder(View view) {
            cityNameTextView = (TextView) view.findViewById(R.id.list_item_city_list_name_textview);
            tagTextView = (TextView) view.findViewById(R.id.list_item_city_list_tags_textview);
        }
    }


}