package br.borbi.ots;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import br.borbi.ots.utility.ForwardUtility;
import br.borbi.ots.utility.LocationUtility;

public class SplashScreenActivity extends Activity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context mMContext = this;

        LocationUtility.cleanSavedCoordinates(this);

        ForwardUtility.goToFilters(mMContext,true);
    }
}
