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
import android.widget.Button;

import com.amazon.device.ads.Ad;
import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdListener;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.AdTargetingOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import br.borbi.ots.R;
import br.borbi.ots.credentials.Credentials;
import br.borbi.ots.data.OTSContract;
import br.borbi.ots.utility.Utility;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AdFragment extends Fragment {
    private static String LOG_TAG = AdFragment.class.getSimpleName();

    private static final String AD_SOURCE_AMAZON = "amazon";
    private static final String AD_SOURCE_ADMOB = "admob";

    private  boolean mAmazonAdEnabled;

    @BindView(R.id.linearLayoutAd) ViewGroup mAdViewContainer;

    private AdLayout mAmazonAdView;
    private AdView mAdmobAdView;
    private Double mLastLatitude;
    private Double mLastLongitude;

    public AdFragment() {
        // Required empty public constructor
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
            setPrimaryAdSource(getActivity().getBaseContext());

            initializeAdAmazon(this.getActivity());
            initializeAdmob(this.getActivity());



            if(mAmazonAdEnabled){
                mAdViewContainer.addView(mAmazonAdView);
                loadAmazonAd();
            }else{
                mAdViewContainer.addView(mAdmobAdView);
                loadAdMobAd();
            }
        }

        return rootView;
    }

    private void setPrimaryAdSource(Context context){
        String primaryAdSource = context.getString(R.string.primary_ad_source);

        mAmazonAdEnabled = !AD_SOURCE_ADMOB.equalsIgnoreCase(primaryAdSource);
    }

    private void initializeAdAmazon(final Activity activity){
        boolean isTest= Boolean.valueOf(activity.getBaseContext().getString(R.string.app_in_test));
        AdRegistration.setAppKey(Credentials.getAmazon());
        AdRegistration.enableLogging(isTest);
        AdRegistration.enableTesting(isTest);

        mAmazonAdView = new AdLayout(activity);
        mAmazonAdView.setListener(new AdListener() {
            @Override
            public void onAdLoaded(Ad ad, AdProperties adProperties) {
                if (!mAmazonAdEnabled) {
                    mAmazonAdEnabled = true;
                    mAdViewContainer.removeView(mAdmobAdView);
                    mAdViewContainer.addView(mAmazonAdView);
                }
            }

            @Override
            public void onAdFailedToLoad(Ad ad, AdError adError) {
                // Call AdMob SDK for backfill
                if (mAmazonAdEnabled) {
                    mAmazonAdEnabled = false;
                    mAdViewContainer.removeView(mAmazonAdView);
                    mAdViewContainer.addView(mAdmobAdView);
                }
                if (Utility.isNetworkAvailable(getContext())) {
                    loadAdMobAd();
                } else {
                    mAdViewContainer.removeView(mAdmobAdView);
                }
            }

            @Override
            public void onAdExpanded(Ad ad) {

            }

            @Override
            public void onAdCollapsed(Ad ad) {

            }

            @Override
            public void onAdDismissed(Ad ad) {


            }
        });
    }

    private void initializeAdmob(Activity activity){
        mAdmobAdView = new com.google.android.gms.ads.AdView(activity);
        mAdmobAdView.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
        mAdmobAdView.setAdUnitId(Credentials.getAdMob());
        mAdmobAdView.setAdListener(new com.google.android.gms.ads.AdListener() {

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                if (!mAmazonAdEnabled) {
                    mAmazonAdEnabled = true;
                    mAdViewContainer.removeView(mAdmobAdView);
                    mAdViewContainer.addView(mAmazonAdView);
                }
                if (Utility.isNetworkAvailable(getContext())) {
                    loadAmazonAd();
                } else {
                    mAdViewContainer.removeView(mAmazonAdView);
                }
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mAmazonAdEnabled) {
                    mAmazonAdEnabled = false;
                    mAdViewContainer.removeView(mAmazonAdView);
                    mAdViewContainer.addView(mAdmobAdView);
                }
            }
        });
    }

    private void loadAmazonAd(){
        AdTargetingOptions opt = new AdTargetingOptions().enableGeoLocation(true);
        mAmazonAdView.loadAd(opt);
    }

    private void loadAdMobAd(){
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
        if(mAmazonAdView!=null) {
            mAmazonAdView.destroy();
        }
        if(mAdmobAdView!=null) {
            mAdmobAdView.destroy();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mAmazonAdView!=null) {
            mAmazonAdView.destroy();
        }
        if(mAdmobAdView!=null) {
            mAdmobAdView.destroy();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mAdmobAdView!=null) {
            mAdmobAdView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAdmobAdView!=null) {
            mAdmobAdView.resume();
        }
    }
}
