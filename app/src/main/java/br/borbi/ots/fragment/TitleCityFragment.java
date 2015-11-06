package br.borbi.ots.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
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
    private TextView mFirstTagTextView;
    private TextView mSecondTagTextView;
    private TextView mThirdTagTextView;

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
        final Set<String> mTags  = sp.getStringSet(OTSContract.KEY_CITY_TAGS, null);

        View rootView = inflater.inflate(R.layout.fragment_title_city, container, false);
        mCityNameTextView = (TextView) rootView.findViewById(R.id.city_name_textview);
        mFirstTagTextView = (TextView) rootView.findViewById(R.id.fragment_city_first_tag_textview);
        mSecondTagTextView = (TextView) rootView.findViewById(R.id.fragment_city_second_tag_textview);
        mThirdTagTextView = (TextView) rootView.findViewById(R.id.fragment_city_third_tag_textview);

        mCityNameTextView.setText(mStrCityName);

        final LinkedList<String> tags = new LinkedList<String>();
        tags.addAll(mTags);

        if (tags.size() > 0) {
            mFirstTagTextView.setVisibility(View.VISIBLE);
            mFirstTagTextView.setText(tags.get(0));
        } else {
            mFirstTagTextView.setVisibility(View.GONE);
        }

        if (tags.size() > 1) {
            mSecondTagTextView.setVisibility(View.VISIBLE);
            mSecondTagTextView.setText(tags.get(1));
        } else {
            mSecondTagTextView.setVisibility(View.GONE);
        }

        if (tags.size() > 2) {
            mThirdTagTextView.setVisibility(View.VISIBLE);
            mThirdTagTextView.setText(tags.get(2));
        } else {
            mThirdTagTextView.setVisibility(View.GONE);
        }

        return rootView;
    }

}
