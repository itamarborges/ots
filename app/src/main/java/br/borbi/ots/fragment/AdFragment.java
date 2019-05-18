package br.borbi.ots.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import br.borbi.ots.R;
import br.borbi.ots.credentials.Credentials;
import br.borbi.ots.data.OTSContract;
import br.borbi.ots.utility.Utility;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AdFragment extends Fragment {
    @BindView(R.id.linearLayoutAd)
    ViewGroup mAdViewContainer;

    private AdView mAdmobAdView;
    private Double mLastLatitude;
    private Double mLastLongitude;

    public AdFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getApplication().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        mLastLatitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LATITUDE, Double.doubleToLongBits(0)));
        mLastLongitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LONGITUDE, Double.doubleToLongBits(0)));

        View rootView = inflater.inflate(R.layout.fragment_ad, container, false);
        ButterKnife.bind(this, rootView);

        if (Utility.isNetworkAvailable(this.getContext())) {
            initializeAdmob(this.getActivity());

            mAdViewContainer.addView(mAdmobAdView);
            loadAdMobAd();
        }

        return rootView;
    }

    private void initializeAdmob(Activity activity) {
        mAdmobAdView = new com.google.android.gms.ads.AdView(activity);
        mAdmobAdView.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
        mAdmobAdView.setAdUnitId(Credentials.getAdMob());
        mAdmobAdView.setAdListener(new com.google.android.gms.ads.AdListener() {

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });
    }

    private void loadAdMobAd() {
        Location location = new Location("");
        location.setLatitude(mLastLatitude);
        location.setLongitude(mLastLongitude);
        mAdmobAdView.loadAd(new AdRequest.Builder()
                .setLocation(location)
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdmobAdView != null) {
            mAdmobAdView.destroy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdmobAdView != null) {
            mAdmobAdView.destroy();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdmobAdView != null) {
            mAdmobAdView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdmobAdView != null) {
            mAdmobAdView.resume();
        }
    }
}
