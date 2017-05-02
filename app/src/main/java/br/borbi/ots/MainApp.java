package br.borbi.ots;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import br.borbi.ots.credentials.Credentials;

/**
 * Created by Itamar on 30/07/2015.
 */
public class MainApp extends Application {
    private static GoogleAnalytics analytics;
    private static Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        boolean isTest= Boolean.valueOf(getString(R.string.app_in_test));
        if(!isTest) {
            analytics = GoogleAnalytics.getInstance(this);
            analytics.setLocalDispatchPeriod(1800);

            tracker = analytics.newTracker(Credentials.getAnalytics());
            tracker.enableExceptionReporting(true);
            tracker.enableAdvertisingIdCollection(true);
            tracker.enableAutoActivityTracking(true);
            tracker.enableAdvertisingIdCollection(true);
        }
   }
}
