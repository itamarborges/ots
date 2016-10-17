package br.borbi.ots;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Timer;
import java.util.TimerTask;

import br.borbi.ots.utility.ForwardUtility;
import br.borbi.ots.utility.LocationUtility;
import br.borbi.ots.utility.Utility;


public class SplashScreenActivity extends Activity  {

    private static final int TIME_SPLASH = 3000;

    private ImageView mImgSplash;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mContext = this;

        LocationUtility.cleanSavedCoordinates(this);

        mImgSplash = (ImageView) findViewById(R.id.imageView);

        int img = ((int) (Math.random() * 10)) ;

        if (img % 2 == 0) {
            mImgSplash.setBackgroundResource(R.drawable.logo_novo_par); ;
        } else {
            mImgSplash.setBackgroundResource(R.drawable.logo_novo_impar);
        }

        new Timer().schedule(new TimerTask() {

            public void run() {
                ForwardUtility.goToFilters(mContext,true);
            }
        }, TIME_SPLASH);
    }
}
