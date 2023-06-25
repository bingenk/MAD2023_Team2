package sg.edu.np.mad.mad2023_team2.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import sg.edu.np.mad.mad2023_team2.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        web =(WebView)findViewById(R.id.webView);
        web.loadUrl("https://doc-hosting.flycricket.io/travelwise-privacy-policy/61b76b67-fc7a-4c95-8163-5b8453d30ca8/privacy");
    }
}