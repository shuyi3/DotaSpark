package com.examples.gg;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.examples.gg.LoadMore_Base.LoadMoreTask;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

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

		// Set retry button listener
		setRetryButtonListener(new LoadMore_Twitch());
		

	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
        menu.add("Refresh")
        .setIcon(R.drawable.ic_refresh)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
	}
	
	@Override
	public void refreshFragment(){
		currentFragment = new LoadMore_Twitch();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		// check network first
		if (ic.checkConnection(this.getSherlockActivity())) {
			// get selected items

			// String selectedValue = (String)
			// getListAdapter().getItem(position);

			Toast.makeText(this.getSherlockActivity(), videos.get(position),
					Toast.LENGTH_SHORT).show();

			Intent i = new Intent(this.getSherlockActivity(), TwitchPlayer.class);
			i.putExtra("video", videolist.get(position).getVideoId());
			startActivity(i);
		} else {
			ic.setNetworkError(InternetConnection.transitionToVideoPlayerError);
		}

	}

}
