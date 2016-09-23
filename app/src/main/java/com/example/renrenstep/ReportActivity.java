package com.example.renrenstep;

import comm.CommHelper;
import helper.BGHelper;
import helper.SPHelper;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import constant.Cons;
 

public class ReportActivity extends Activity {
	private WebView wv; 
	private ImageView imageView;  
	private LinearLayout ll_back; 
	private String url="";
	private TextView tv_title;
	private Dialog dialogpa;
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		//getActionBar().hide();
		setContentView(R.layout.report_menu); 
		Intent intent= getIntent();
		String pagenm= intent.getStringExtra("pagenm");
		String gender=intent.getStringExtra("gender");
		dialogpa=CommHelper.createLoadingDialog(this,"",gender);
		dialogpa.show();
		RelativeLayout layout=(RelativeLayout)findViewById(R.id.layout_actionbar);
		//layout.setBackgroundResource(BGHelper.setBackground(this, SPHelper.getDetailMsg(this,Cons.APP_SEX,"M")));
		imageView =(ImageView)layout.findViewById(R.id.iv_back);
		ll_back =(LinearLayout)layout.findViewById(R.id.ll_back);
		tv_title = (TextView)findViewById(R.id.tv_title);
		ll_back.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
				ReportActivity.this.finish();
			}
		}); 
	    url=intent.getStringExtra("url");
		layout.setBackgroundResource(BGHelper.setBackground(this, gender));
		tv_title.setText(pagenm); 
		initView(); 
		if(pagenm.equals(getResources().getString(R.string.notifycenter))){
			SPHelper.setDetailMsg(this, "oldnotifynum", SPHelper.getDetailMsg(this, "newnotifynum", 0));
		}
	}
	//改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(wv.canGoBack())
            {
                wv.goBack();//返回上一页面
                return true;
            }
            else
            {
                this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
	
	void initView(){
		wv = (WebView) findViewById(R.id.webview);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setScrollBarStyle(0);
		WebSettings webSettings = wv.getSettings();
		webSettings.setAllowFileAccess(true);
		webSettings.setBuiltInZoomControls(true);
		wv.loadUrl(url);
		//加载数据
		wv.setWebChromeClient(new WebChromeClient() {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
		if (newProgress == 100) {
			ReportActivity.this.setTitle("加载完成");
		} else {
			ReportActivity.this.setTitle("加载中.......");
			}
		}
		});
		//这个是当网页上的连接被点击的时候
		wv.setWebViewClient(new WebViewClient(){
			@Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            // TODO Auto-generated method stub
	               //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
	             view.loadUrl(url);
	            return true;
	        }
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub 
				super.onPageStarted(view, url, favicon); 
 
			}
			
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl); 
				if(dialogpa!=null&&dialogpa.isShowing())
					dialogpa.dismiss(); 
			} 
			
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url); 
				if(dialogpa!=null&&dialogpa.isShowing())
					dialogpa.dismiss(); 
			}
		});
	}  
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
