package sg.edu.np.mad.mad2023_team2.ui.Recommendations;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import sg.edu.np.mad.mad2023_team2.R;

public class RecommendationsWeb extends AppCompatActivity {

    private FloatingActionButton back;
    private ProgressBar pb;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations_web);

        String url = getIntent().getStringExtra("url");
        webView = findViewById(R.id.recco_webview);
        back = findViewById(R.id.recco_web_back);
        pb = findViewById(R.id.recco_web_pb);

        if (url != null)
        {
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    pb.setVisibility(View.GONE);
                    return true;
                }
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    pb.setVisibility(View.GONE);
                }
            });
            webView.loadUrl(url);
            webView.getSettings().setJavaScriptEnabled(true);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.stopLoading();
                webView.destroy();
                finish();
            }
        });
    }
}