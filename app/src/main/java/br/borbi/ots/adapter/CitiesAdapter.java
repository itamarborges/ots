package br.borbi.ots.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import br.borbi.ots.R;
import br.borbi.ots.ResultCitiesActivity;
import br.borbi.ots.data.OTSContract;
import br.borbi.ots.data.OTSProvider;
import br.borbi.ots.fragment.CitiesFragment;
import br.borbi.ots.pojo.CityResultSearch;
import br.borbi.ots.utility.Utility;

/**
 * Created by Itamar on 16/06/2015.
 */
public class CitiesAdapter extends BaseAdapter{

    private static final String LOG_TAG= CitiesAdapter.class.getSimpleName();

    HashMap<Long, LinkedList> tagsCity = new HashMap<Long, LinkedList>();

    private LinkedList<CityResultSearch> mCities;
    private final Context mContext;

    public CitiesAdapter(LinkedList<CityResultSearch> cities, Context context) {
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
    public CityResultSearch getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_city, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        final CityResultSearch cityResultSearch = getItem(position);

        Integer distance  = cityResultSearch.getDistance();
        if(Utility.usesMiles(mContext)){
            distance = Utility.convertKilometersToMiles(distance);
        }

        final String strLabelLocal = cityResultSearch.getCity().getName() + " - " + cityResultSearch.getCity().getCountryName();

      //  String cityDisplayName = mContext.getString(mContext.getResources().getIdentifier(cityResultSearch.getCity().getTranslationFileKey(), "string", mContext.getPackageName()));

       // final String strLabelLocal = cityDisplayName  + " - " + cityResultSearch.getCity().getCountryName();

        // Find TextView and set the city name on it
        viewHolder.cityNameTextView.setText(strLabelLocal);

        int idCity = cityResultSearch.getCity().getId();
        if (!tagsCity.containsKey(idCity)) {

            String selection = OTSProvider.FILTER_BY_CITY;
            String[] selectionArgs = new String[]{String.valueOf(idCity)};

            String sortOrder = OTSContract.City.TABLE_NAME + "." + OTSContract.City.COLUMN_NAME_NAME_ENGLISH + " ASC";

            //Recupera as tags
            Cursor c = mContext.getContentResolver().query(
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

                tag = mContext.getString(mContext.getResources().getIdentifier(resourceName, "string", mContext.getPackageName()));

                if (!tag.isEmpty()) {
                    tagNames.add(tag);
                }
            }
            tagsCity.put((long) idCity, tagNames);

            if(c!= null){
                c.close();
            }
        }

        final LinkedList<String> tags = tagsCity.get((long) idCity);

        if (tags.size() > 0) {
            viewHolder.firstTagTextView.setVisibility(View.VISIBLE);
            viewHolder.firstTagTextView.setText(tags.get(0));
        } else {
            viewHolder.firstTagTextView.setVisibility(View.GONE);
        }

        if (tags.size() > 1) {
            viewHolder.secondTagTextView.setVisibility(View.VISIBLE);
            viewHolder.secondTagTextView.setText(tags.get(1));
        } else {
            viewHolder.secondTagTextView.setVisibility(View.GONE);
        }

        if (tags.size() > 2) {
            viewHolder.thirdTagTextView.setVisibility(View.VISIBLE);
            viewHolder.thirdTagTextView.setText(tags.get(2));
        } else {
            viewHolder.thirdTagTextView.setVisibility(View.GONE);
        }

        if(Utility.usesMiles(mContext)){
            viewHolder.distanceTextView.setText(mContext.getString(R.string.distance_miles, Integer.toString(distance)));
        }else{
            viewHolder.distanceTextView.setText(mContext.getString(R.string.distance_kilometers, Integer.toString(distance)));
        }

        if(cityResultSearch.isFirstCity()){
            viewHolder.youAreHereImageView.setVisibility(View.VISIBLE);
            viewHolder.distanceTextView.setVisibility(View.GONE);
            viewHolder.linearLayoutDistanceDistance.setVisibility(View.GONE);
        }else{
            viewHolder.youAreHereImageView.setVisibility(View.GONE);
            viewHolder.distanceTextView.setVisibility(View.VISIBLE);
            viewHolder.linearLayoutDistanceDistance.setVisibility(View.VISIBLE);
        }


        viewHolder.layoutCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ResultCitiesActivity.class);

                //Vai gravar no SharedPreferences as informações do elemento clicado
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor spe = sp.edit();

                spe.putString(OTSContract.KEY_CITY_NAME, strLabelLocal);
                spe.putInt(OTSContract.KEY_REL_SEARCH_CITY, cityResultSearch.getIdResultSearch());

                Set<String> tagsSetString = new HashSet<String>(tags);

                spe.putStringSet(OTSContract.KEY_CITY_TAGS, tagsSetString );

                spe.apply();

                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    public static class ViewHolder {
        public final TextView cityNameTextView;
        public final TextView firstTagTextView;
        public final TextView secondTagTextView;
        public final TextView thirdTagTextView;
        public final TextView distanceTextView;
        public final LinearLayout layoutCities;
        public final ImageView youAreHereImageView;
        public final LinearLayout linearLayoutDistanceDistance;

        public ViewHolder(View view) {
            cityNameTextView = (TextView) view.findViewById(R.id.list_item_city_name_textview);
            firstTagTextView = (TextView) view.findViewById(R.id.list_item_resul_first_tag_textview);
            secondTagTextView = (TextView) view.findViewById(R.id.list_item_resul_second_tag_textview);
            thirdTagTextView = (TextView) view.findViewById(R.id.list_item_resul_third_tag_textview);
            distanceTextView = (TextView) view.findViewById(R.id.list_item_distance_textview);
            layoutCities = (LinearLayout) view.findViewById(R.id.layout_search_city);
            youAreHereImageView = (ImageView) view.findViewById(R.id.image_view_you_are_here);
            linearLayoutDistanceDistance = (LinearLayout) view.findViewById(R.id.linear_layout_distance);
        }
    }


}
