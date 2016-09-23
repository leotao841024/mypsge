package com.example.renrenstep;
import android.app.Activity;
import android.os.Bundle; 
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class WeekGameActivity extends Activity {
	private WebView webview;
	private LinearLayout linear_noweb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_week_game);
		initView();
		initData();
	}
	
	void initView(){
		webview = (WebView)findViewById(R.id.webview);
		linear_noweb = (LinearLayout)findViewById(R.id.linear_noweb);
		((LinearLayout)findViewById(R.id.linear_back)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
	void initData(){
		webview.getSettings().setJavaScriptEnabled(true); 
		webview.loadUrl("http://www.baidu.com");
		webview.setWebViewClient(new ClientWebView());
	}
	
	class ClientWebView extends WebViewClient{
		@Override
		public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
			linear_noweb.setVisibility(View.VISIBLE);
			webview.setVisibility(View.GONE);
		}
	}
}
