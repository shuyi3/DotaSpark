package com.examples.gg;

import java.util.List;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class LoadMore_Twitch extends LoadMore_Base {
	@Override
	public void Initializing() {
		// Give a title for the action bar
		abTitle = "Twitch Dota2 Streams";

		// Give API URLs
		API.add("https://api.twitch.tv/kraken/streams?game=Dota+2");

		// set a feed manager
		feedManager = new FeedManager_Twitch();

		// Show menu
		setHasOptionsMenu(true);
		setOptionMenu(true, false);

		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
        menu.add("Refresh")
        .setIcon(R.drawable.ic_refresh)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

//			Toast.makeText(this.getSherlockActivity(), videos.get(position),
//					Toast.LENGTH_SHORT).show();

			if (check()){
				Intent i = new Intent(this.getSherlockActivity(), TwitchPlayer.class);
				i.putExtra("video", videolist.get(position).getVideoId());
				startActivity(i);

			}else{
				Intent i = new Intent(this.getSherlockActivity(), FlashInstallerActivity.class);
				startActivity(i);
			}
	}

	private boolean check() {
		PackageManager pm = sfa.getPackageManager();
		List<PackageInfo> infoList = pm
				.getInstalledPackages(PackageManager.GET_SERVICES);
		for (PackageInfo info : infoList) {
			if ("com.adobe.flashplayer".equals(info.packageName)) {
				return true;
			}
		}
		return false;
	}
}
