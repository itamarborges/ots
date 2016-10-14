package br.borbi.ots;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;
import java.util.Properties;

import br.borbi.ots.utility.ImageUtils;

public class AboutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView versionTextView = (TextView) findViewById(R.id.versionTextView);

        versionTextView.setText(BuildConfig.VERSION_NAME);

        ImageView img = (ImageView) findViewById(R.id.ofinion_img);

        //img.setImageBitmap(ImageUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.logo_ofinion, getResources().getInteger(R.integer.max_width_logo), getResources().getInteger(R.integer.max_height_logo)));

        img.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=br.borbi.ofinion.free"));
                startActivity(intent);
            }
        });

//        ImageView imgOfinion = (ImageView) findViewById(R.id.imageOtsIcon);
//
//        imgOfinion.setImageBitmap(ImageUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.logo, getResources().getInteger(R.integer.max_width_logo), getResources().getInteger(R.integer.max_height_logo)));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
