package com.examples.gg;

import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.os.Message;

@SuppressLint("SetJavaScriptEnabled")
public class TwitchPlayer extends Activity {

	private FrameLayout mFullscreenContainer;
	private FrameLayout mContentView;
	private LinearLayout mSideView;
	private View mCustomView = null;
	private WebView mWebView;
	private WebView mWebChat;
	private ToggleButton lockButton;
	private String video;
	private String ua;
	private View loadingIndicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitchplayer);

		loadingIndicator = findViewById(R.id.fullscreen_loading_indicator);
//		loadingIndicator.setVisibility(View.VISIBLE);

		Intent intent = getIntent();
		video = intent.getStringExtra("video");

		initViews();
		initWebView();

		if (getPhoneAndroidSDK() >= 14) {
			getWindow().setFlags(0x1000000, 0x1000000);
		}

		String stream = "";
		stream = "<html>"
				+ "<style>"
				+ "html, body { height: 100%}"
				+ "</style>"
				+ "<body style='margin:0; padding:0;'>"
				+ "<object type=\"application/x-shockwave-flash\" id=\"videoPlayer\" height='100%' width=\"100%\">"
				+ "<param name=\"allowFullScreen\" value=\"false\" />"
				+ "<param name=\"allowScriptAccess\" value=\"never\" />"
				+ "<param name=\"allowNetworking\" value=\"internal\" />"
				+ "<param name=\"movie\" value=\"http://www.twitch.tv/widgets/live_embed_player.swf\" />"
				+ "<param name=\"flashvars\" value=\"hostname=www.twitch.tv&channel="
				+ video + "&auto_play=true&start_volume=25\" />" + "</object>"
				+ "</body>" + "</html>";

		String chat = "";
		chat = "<html>" + "<body style=\"margin:0; padding:0\">"
				+ "<iframe width=\"400\" height=\"500\" scrolling=\"yes\""
				+ "src=\"http://www.justin.tv/chat/embed?channel=" + video
				+ "\">" + "</iframe>" + "</body>" + "</html>";

		// mWebChat.loadUrl("http://www.justin.tv/chat/embed?channel=beyondthesummit&hide_chat=myspace,facebook&default_chat=jtv");
		mWebChat.loadData(chat, "text/html", "UTF-8");
		mWebChat.getSettings().setUserAgentString(ua);
		// mWebChat.loadUrl("file:///android_asset/chat.html");

		// mWebChat.loadUrl("http://www.twitch.tv/chat/embed?channel=beyondthesummit&popout_chat=true");
		// mWebView.loadData(stream, "text/html", null);
		// mWebView.loadUrl("file:///android_asset/stream.html");
		mWebView.loadUrl("http://www.twitch.tv/" + video + "/popout");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {

			if (mSideView.getVisibility() == View.VISIBLE) {
				mSideView.setVisibility(View.GONE);
			} else
				mSideView.setVisibility(View.VISIBLE);

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initViews() {
		mFullscreenContainer = (FrameLayout) findViewById(R.id.fullscreen_custom_content);
		mContentView = (FrameLayout) findViewById(R.id.main_content);
		mWebView = (WebView) findViewById(R.id.webview_player);
		mWebChat = (WebView) findViewById(R.id.webview_chat);
		mSideView = (LinearLayout) findViewById(R.id.side_content);
		lockButton = (ToggleButton) findViewById(R.id.lockButton);
	}

	@SuppressWarnings("deprecation")
	private void initWebView() {

		WebSettings chatSettings = mWebChat.getSettings();
		chatSettings.setJavaScriptEnabled(true);
		chatSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		chatSettings.setPluginState(PluginState.ON);
		chatSettings.setSupportMultipleWindows(true);
		chatSettings.setPluginsEnabled(true);
		chatSettings.setAllowFileAccess(true);
		// chatSettings.setUseWideViewPort(true);
		chatSettings.setLoadWithOverviewMode(true);
		// chatSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		ua = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
		chatSettings.setUserAgentString(ua);
		mWebChat.setWebViewClient(new MyWebViewClient());
		mWebChat.setWebChromeClient(new WebChromeClient());
		mWebChat.setHorizontalScrollBarEnabled(false);
		mWebChat.setOnTouchListener(new View.OnTouchListener() {
			float m_downX;

			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					// save the x
					m_downX = event.getX();
				}
					break;

				case MotionEvent.ACTION_MOVE:
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP: {
					// set x so that it doesn't move
					event.setLocation(m_downX, event.getY());
				}
					break;

				}

				return false;
			}
		});

		WebSettings streamSettings = mWebView.getSettings();
		streamSettings.setJavaScriptEnabled(true);
		streamSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		streamSettings.setPluginState(PluginState.ON);
		streamSettings.setPluginsEnabled(true);
		streamSettings.setAllowFileAccess(true);
		streamSettings.setLoadWithOverviewMode(true);

		mWebView.setWebChromeClient(new MyWebChromeClient());
		mWebView.setWebViewClient(new MyWebViewClient());

		lockButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (isChecked) {
					mWebView.setOnTouchListener(new View.OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							return true;
						}

					});

				} else {

					mWebView.setOnTouchListener(new View.OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							return false;
						}

					});
				}

			}
		});

	}

	class MyWebChromeClient extends WebChromeClient {

		private CustomViewCallback mCustomViewCallback;
		private int mOriginalOrientation = 1;

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			// TODO Auto-generated method stub
			onShowCustomView(view, mOriginalOrientation, callback);
			super.onShowCustomView(view, callback);

		}

		public void onShowCustomView(View view, int requestedOrientation,
				WebChromeClient.CustomViewCallback callback) {
			if (mCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}
			if (getPhoneAndroidSDK() >= 14) {
				mFullscreenContainer.addView(view);
				mCustomView = view;
				mCustomViewCallback = callback;
				mOriginalOrientation = getRequestedOrientation();
				mContentView.setVisibility(View.INVISIBLE);
				// mSideView.setVisibility(View.GONE);
				mFullscreenContainer.setVisibility(View.VISIBLE);
				mFullscreenContainer.bringToFront();

				setRequestedOrientation(mOriginalOrientation);
			}

		}

		public void onHideCustomView() {
			mContentView.setVisibility(View.VISIBLE);
			// mSideView.setVisibility(View.VISIBLE);
			if (mCustomView == null) {
				return;
			}
			mCustomView.setVisibility(View.GONE);
			mFullscreenContainer.removeView(mCustomView);
			mCustomView = null;
			mFullscreenContainer.setVisibility(View.GONE);
			try {
				mCustomViewCallback.onCustomViewHidden();
			} catch (Exception e) {
			}
			// Show the content view.

			setRequestedOrientation(mOriginalOrientation);
		}

	}

	class MyWebViewClient extends WebViewClient {
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			// view.loadUrl(url);
			// return super.shouldOverrideUrlLoading(view, url);
			return true;
		}

		@SuppressLint("NewApi")
		public WebResourceResponse shouldInterceptRequest(WebView mWebView,
				String keyString) {
			WebResourceResponse localWebResourceResponse = null;
			if ((keyString.contains("ImaAds.swf"))
					|| (keyString.contains("Ads"))
					|| (keyString.contains("quantserve"))
					|| (keyString.contains("ImaAds.swf"))
					|| (keyString.contains("adwords"))
					|| (keyString.contains("live_embed_click"))
					|| (keyString.contains("advertisements"))
					|| (keyString.contains("googleadservice"))
					|| (keyString.contains("liverail"))
					|| (keyString.contains("scorecardresearch"))
					|| (keyString.contains("googletagservices"))
					|| (keyString.contains("googlesynd"))
					|| (keyString.contains("adsense"))
					|| (keyString.contains("liftdna")))
				localWebResourceResponse = new WebResourceResponse("text",
						"utf-8", null);
			return localWebResourceResponse;
		}

		@Override
		public void onPageFinished(WebView view, String url) {

			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					loadingIndicator.setVisibility(View.GONE);
				}
			}, 2000);
			// loadingIndicator.setVisibility(View.GONE);
		}

	}

	public static int getPhoneAndroidSDK() {
		// TODO Auto-generated method stub
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return version;

	}

	@Override
	public void onPause() {
		super.onPause();
		mWebView.onPause();
		mWebChat.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mWebView.onResume();
		mWebChat.onResume();
	}
}
