package br.borbi.ots;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Map;
import java.util.Properties;

public class AboutActivity extends ActionBarActivity {

    private static final String LOG_TAG= AboutActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView versionTextView = (TextView) findViewById(R.id.versionTextView);

        versionTextView.setText(BuildConfig.VERSION_NAME);
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
