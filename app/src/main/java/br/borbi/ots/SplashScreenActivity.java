package br.borbi.ots;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import br.borbi.ots.utility.ForwardUtility;
import br.borbi.ots.utility.LocationUtility;


public class SplashScreenActivity extends Activity  {

    private static final int TIME_SPLASH = 3000;

    private ImageView mImgSplash;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        LocationUtility.cleanSavedCoordinates(this);

        ForwardUtility.goToFilters(mContext,true);
    }
}
