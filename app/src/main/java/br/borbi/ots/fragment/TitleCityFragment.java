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
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Itamar on 16/06/2015.
 */
public class TitleCityFragment extends Fragment {

    @BindView(R.id.city_name_textview) TextView mMCityNameTextView;
    @BindView(R.id.fragment_city_first_tag_textview) TextView mMFirstTagTextView;
    @BindView(R.id.fragment_city_second_tag_textview) TextView mMSecondTagTextView;
    @BindView(R.id.fragment_city_third_tag_textview) TextView mMThirdTagTextView;


    public static final String LOG_TAG = TitleCityFragment.class.getSimpleName();

    public TitleCityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String mMStrCityName = sp.getString(OTSContract.KEY_CITY_NAME, "");
        final Set<String> mTags  = sp.getStringSet(OTSContract.KEY_CITY_TAGS, null);

        View rootView = inflater.inflate(R.layout.fragment_title_city, container, false);

        ButterKnife.bind(this, rootView);

        mMCityNameTextView.setText(mMStrCityName);

        final LinkedList<String> tags = new LinkedList<>();
        tags.addAll(mTags);

        if (tags.size() > 0) {
            mMFirstTagTextView.setVisibility(View.VISIBLE);
            mMFirstTagTextView.setText(tags.get(0));
        } else {
            mMFirstTagTextView.setVisibility(View.GONE);
        }

        if (tags.size() > 1) {
            mMSecondTagTextView.setVisibility(View.VISIBLE);
            mMSecondTagTextView.setText(" / " + tags.get(1));
        } else {
            mMSecondTagTextView.setVisibility(View.GONE);
        }

        if (tags.size() > 2) {
            mMThirdTagTextView.setVisibility(View.VISIBLE);
            mMThirdTagTextView.setText(" / " + tags.get(2));
        } else {
            mMThirdTagTextView.setVisibility(View.GONE);
        }

        return rootView;
    }
}
