package com.examples.gg;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class InternetConnection {
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

}
