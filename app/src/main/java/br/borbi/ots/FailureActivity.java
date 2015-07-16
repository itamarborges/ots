package br.borbi.ots;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;

import java.util.Timer;
import java.util.TimerTask;

import br.borbi.ots.data.OTSContract;
import br.borbi.ots.utility.ForwardUtility;
import br.borbi.ots.utility.Utility;


public class FailureActivity extends ActionBarActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = FailureActivity.class.getName();

    private static final int WAIT_TIME = 5000;

    private Double lastLongitude;
    private Double lastLatitude;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    FrameLayout progressBarHolder;

    private Button tryToFindMeButton;
    private Button continueWithouLocationButton;

    private Context mContext;

    private Boolean mHasSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failure);

        mContext = this;

        AdView mAdView = null;
        Utility.initializeAd(mAdView, this);


        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        tryToFindMeButton = (Button) findViewById(R.id.btnTryToFindMe);
        continueWithouLocationButton = (Button) findViewById(R.id.buttonContinueWithoutLocation);
        if(hasSearch()){
            continueWithouLocationButton.setVisibility(View.VISIBLE);
        }

        getApplication().getSharedPreferences(OTSContract.SHARED_PREFERENCES, Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this);
    }

    public void tryToFindMeClick(View v) {

        if(lastLatitude == null || lastLongitude == null){
            tryToFindMeButton.setEnabled(false);
            if(hasSearch()){
                continueWithouLocationButton.setEnabled(false);
            }

            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);

            Timer timer = new Timer();
            timer.schedule(new MyTimerTask(), WAIT_TIME);

        }else{
            forwardActivity();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(OTSContract.SHARED_LATITUDE.equals(key)){
            this.lastLatitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LATITUDE, Double.doubleToLongBits(0)));
        }else if(OTSContract.SHARED_LONGITUDE.equals(key)){
            this.lastLongitude = Double.longBitsToDouble(sharedPreferences.getLong(OTSContract.SHARED_LONGITUDE, Double.doubleToLongBits(0)));
        }
    }

    public void continueWithoutMyLocation(View view) {
        forwardActivity();
    }

    private class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(lastLatitude == null || lastLongitude == null) {
                        outAnimation = new AlphaAnimation(1f, 0f);
                        outAnimation.setDuration(200);
                        progressBarHolder.setAnimation(outAnimation);
                        progressBarHolder.setVisibility(View.GONE);

                        tryToFindMeButton.setEnabled(true);
                        if(hasSearch()){
                            continueWithouLocationButton.setEnabled(true);
                        }

                        Toast.makeText(getApplicationContext(),R.string.location_not_found_yet, Toast.LENGTH_LONG).show();

                    }else{
                        forwardActivity();
                    }
                }
            });
        }
    }

    private void forwardActivity(){
        Toast.makeText(getApplicationContext(),R.string.location_found, Toast.LENGTH_LONG).show();

        Integer searchId = Utility.findSearchByDateAndCoordinates(lastLatitude, lastLongitude,mContext);

        if (searchId == null) {
            ForwardUtility.goToFilters(mContext);
        } else {
            ForwardUtility.goToResults(false, searchId,mContext);
        }
    }

    private boolean hasSearch(){
        if(mHasSearch == null){
            Integer searchId = Utility.findSearchByDate(mContext);
            if (searchId == null) {
                mHasSearch = false;
            }else{
                mHasSearch = true;
            }
        }
        return mHasSearch;
    }
}
