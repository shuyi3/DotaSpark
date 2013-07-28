package com.examples.gg;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class YoutubeActionBarActivity extends SherlockFragmentActivity implements YouTubePlayer.OnInitializedListener {

	private EditText et;
	private Video video;
	private String videoId;
	private TextView title;
	private TextView desc;
	private View myContent;
	private boolean isfullscreen;
	private boolean isFullscreenMode;
	private Activity sfa;
	private RelativeLayout playerHolder;
	private YouTubePlayerSupportFragment fragment;
	private ActionBar mActionBar;
	
	private static final int LANDSCAPE_ORIENTATION = Build.VERSION.SDK_INT < 9 ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
			: ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

	private static final int RECOVERY_DIALOG_REQUEST = 1;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sfa = this;
		
		mActionBar = getSupportActionBar();

		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);

		
		Intent intent = getIntent();
		if (isFullscreenMode = intent.getBooleanExtra("isfullscreen", false)) {
			videoId = intent.getStringExtra("videoId");

			setContentView(R.layout.fullscreenyoutube);

		} else {
			video = intent.getParcelableExtra("video");

			setContentView(R.layout.videoplayer);

			myContent = (View) findViewById(R.id.videoContent);

			title = (TextView) findViewById(R.id.videotitle);
			title.setText(video.getTitle());

			desc = (TextView) findViewById(R.id.videodesc);
			desc.setText(video.getVideoDesc());

		}
		
		playerHolder = (RelativeLayout) findViewById(R.id.youtubeplayer);

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		fragment = new YouTubePlayerSupportFragment();
		fragmentTransaction.add(R.id.youtubeplayer, fragment);
		fragmentTransaction.commit();
		
		fragment.initialize("AIzaSyAuEa3bIKbSYiXVWbHU_zueVzEBv9p2r_Y",this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			finish();
		}

		return super.onOptionsItemSelected(item);
	}


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

		if (!isFullscreenMode)
			doLayout();
	}

	@SuppressWarnings("deprecation")
	private void doLayout() {
		if (isfullscreen) {
			if (title != null)
				title.setVisibility(TextView.GONE);
			if (myContent != null)
				myContent.setVisibility(View.GONE);
			if (desc != null)
				desc.setVisibility(View.GONE);
			fragment.getView().setLayoutParams(new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			mActionBar.hide();
		} else {
			if (title != null)
				title.setVisibility(TextView.VISIBLE);
			if (myContent != null)
				myContent.setVisibility(View.VISIBLE);
			if (desc != null)
				desc.setVisibility(View.VISIBLE);
			fragment.getView().setLayoutParams(new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			mActionBar.show();
		}
	}

	  @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == RECOVERY_DIALOG_REQUEST) {
	      // Retry initialization if user performed a recovery action
	      getYouTubePlayerProvider().initialize("AIzaSyAuEa3bIKbSYiXVWbHU_zueVzEBv9p2r_Y", this);
	    }
	  }

	  public YouTubePlayer.Provider getYouTubePlayerProvider() {
		  return (YouTubePlayerSupportFragment) fragment;
	  }

		@Override
		public void onInitializationSuccess(Provider arg0,
				YouTubePlayer ytp, boolean wasRestored) {

				ytp.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
				ytp.setOnFullscreenListener(new OnFullscreenListener() {

				
					@Override
					public void onFullscreen(boolean _isFullScreen) {
						isfullscreen = _isFullScreen;
						if (isfullscreen)
							setRequestedOrientation(LANDSCAPE_ORIENTATION);
						else {
							if (isFullscreenMode)
								finish();
							setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
							doLayout();
						}

					}
				});
				Toast.makeText(sfa, "Initialization  Success",
						Toast.LENGTH_LONG).show();
				if (!wasRestored) {
					if (isFullscreenMode) {
						ytp.setFullscreen(true);
						ytp.cueVideo(videoId);
					} else
						ytp.cueVideo(video.getVideoId());
				}

			
		}

		  @Override
		  public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
		    if (errorReason.isUserRecoverableError()) {
		      errorReason.getErrorDialog(sfa, RECOVERY_DIALOG_REQUEST).show();
		    } else {
		      String errorMessage = String.format(getString(R.string.error_player), errorReason.toString());
		      Toast.makeText(sfa, errorMessage, Toast.LENGTH_LONG).show();
		    }
		  }

}
