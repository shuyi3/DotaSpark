package com.examples.gg;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoPlayer extends YouTubeFailureRecoveryActivity implements
		YouTubePlayer.OnInitializedListener, YouTubePlayer.OnFullscreenListener {

	private YouTubePlayerView ytpv;
	private YouTubePlayer ytp;
	private EditText et;
	private Video video;
	private String videoId;
	private TextView title;
	private TextView desc;
	private View myContent;
	private boolean isfullscreen;
	private boolean isFullscreenMode;

	private static final int LANDSCAPE_ORIENTATION = Build.VERSION.SDK_INT < 9 ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
			: ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		System.out.println("Inside VideoPlayer");
		
		Intent intent = getIntent();
		if (isFullscreenMode = intent.getBooleanExtra("isfullscreen", false)){
			videoId = intent.getStringExtra("videoId");
			
			setContentView(R.layout.fullscreenyoutube);

		}
		else{
		video = intent.getParcelableExtra("video");

			setContentView(R.layout.videoplayer);
			
			myContent = (View) findViewById(R.id.videoContent);
	
			title = (TextView) findViewById(R.id.videotitle);
			title.setText(video.getTitle());
	
			desc = (TextView) findViewById(R.id.videodesc);
			desc.setText(video.getVideoDesc());
		
		}

		ytpv = (YouTubePlayerView) findViewById(R.id.youtubeplayer);
		ytpv.initialize("AIzaSyAuEa3bIKbSYiXVWbHU_zueVzEBv9p2r_Y", this);
		doLayout();
	}

	@Override
	public void onInitializationSuccess(Provider provider,
			YouTubePlayer player, boolean wasRestored) {
		ytp = player;
		Toast.makeText(this, "Initialization  Success", Toast.LENGTH_LONG)
				.show();
		ytp.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
		ytp.setOnFullscreenListener(this);
		if (!wasRestored) {
			if (isFullscreenMode){
				ytp.setFullscreen(true);
				player.cueVideo(videoId);
			}
			else
				player.cueVideo(video.getVideoId());
		}
	}

	@Override
	public void onFullscreen(boolean isFullscreen) {
		System.out.println("change!!!!!!!!!!!!!!!!!!!!!!!!" + isFullscreen);
		isfullscreen = isFullscreen;
		if (isfullscreen)
			setRequestedOrientation(LANDSCAPE_ORIENTATION);
		else{
			if (isFullscreenMode) finish();
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			doLayout();
		}
	}

	// Check screen orientation or screen rotate event here
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (isfullscreen) {
			// Checks the orientation of the screen for landscape and portrait
			// and set portrait mode always
			System.out.println("FULL!!!!!!!!!!!!!!!!!!!!!!!!");
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else
			System.out.println("NOT FULL!!!!!!!!!!!!!!!!!!!!!!!!");

		if (!isFullscreenMode) doLayout();
	}

	@Override
	protected Provider getYouTubePlayerProvider() {
		// TODO Auto-generated method stub

		return null;
	}

	@SuppressWarnings("deprecation")
	private void doLayout() {
		if (isfullscreen) {
			if (title != null) title.setVisibility(TextView.GONE);
			if (myContent != null) myContent.setVisibility(View.GONE);
			if (desc != null) desc.setVisibility(View.GONE);
			ytpv.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		} else {
			if (title != null) title.setVisibility(TextView.VISIBLE);
			if (myContent != null) myContent.setVisibility(View.VISIBLE);
			if (desc != null) desc.setVisibility(View.VISIBLE);
			ytpv.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}

}