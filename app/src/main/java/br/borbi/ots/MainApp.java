package br.borbi.ots;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import br.borbi.ots.credentials.Credentials;

/**
 * Created by Itamar on 30/07/2015.
 */
public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        boolean isTest= Boolean.valueOf(getString(R.string.app_in_test));
        if(!isTest) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.setLocalDispatchPeriod(1800);

            Tracker tracker = analytics.newTracker(Credentials.getAnalytics());
            tracker.enableExceptionReporting(true);
            tracker.enableAdvertisingIdCollection(true);
            tracker.enableAutoActivityTracking(true);
            tracker.enableAdvertisingIdCollection(true);
        }

        MobileAds.initialize(this, "ca-app-pub-7723898994387347~5164928919");
   }
}
