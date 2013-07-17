package com.examples.gg;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

public class InternetConnection {
	private int networkError;


	public final static int fullscreenLoadingError = 1;
	public final static int loadingMoreError = 2;
	public final static int transitionToVideoPlayerError = 3;
	

	public boolean checkConnection(Activity sfa){
		if(isOnline(sfa)){
			sfa.findViewById(R.id.mRetry).setVisibility(
					View.GONE);
			return true;
		}else{
			// No network connection
//			sfa.findViewById(R.id.fullscreen_loading_indicator).setVisibility(
//					View.GONE);
			sfa.findViewById(R.id.mRetry).setVisibility(
					View.VISIBLE);
			
			return false;
		}
			
	}
	
	// Returns true if internet is ok
	public boolean isOnline(Activity a) {
		ConnectivityManager cm = (ConnectivityManager) a
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public void networkToast(Activity a) {
		Toast.makeText(a, "Please check your network connection!",
				Toast.LENGTH_SHORT).show();
	}
	
	public int getNetworkError() {
		return networkError;
	}

	public void setNetworkError(int networkError) {
		this.networkError = networkError;
	}

}
