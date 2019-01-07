package br.borbi.ots;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivacyPolicyActivity extends AppCompatActivity {
    @BindView(R.id.privacy_policy_webview) WebView privacyPolicyWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        ButterKnife.bind(this);

        privacyPolicyWebView.loadUrl("file:///android_asset/privacy_policy.html");
    }

}
