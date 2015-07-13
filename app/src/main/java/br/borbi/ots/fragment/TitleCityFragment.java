package br.borbi.ots.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Set;

import br.borbi.ots.R;
import br.borbi.ots.data.OTSContract;

/**
 * Created by Itamar on 16/06/2015.
 */
public class TitleCityFragment extends Fragment {

    public static final String LOG_TAG = TitleCityFragment.class.getSimpleName();

    private String mStrCityName;
    private Set<String> mSetTags;

    private TextView mCityNameTextView;
    private TextView mTagsTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public TitleCityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mStrCityName = sp.getString(OTSContract.KEY_CITY_NAME, "");
        mSetTags = sp.getStringSet(OTSContract.KEY_CITY_TAGS, null);

        View rootView = inflater.inflate(R.layout.fragment_title_city, container, false);
        mCityNameTextView = (TextView) rootView.findViewById(R.id.city_name_textview);
        mTagsTextView = (TextView) rootView.findViewById(R.id.tags_textview);

        mCityNameTextView.setText(mStrCityName);

        String tagsItem = "";
        String separador = "";

        for(String tag:mSetTags)  {
            tagsItem = tagsItem.concat(separador + tag);
            separador = " - ";
        }

        mTagsTextView.setText(tagsItem);

        return rootView;
    }

}
