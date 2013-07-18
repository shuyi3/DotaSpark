package com.examples.gg;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.examples.gg.LoadMore_Base.LoadMoreTask;

import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class LoadMore_Twitch extends LoadMore_Base {
	@Override
	public void Initializing() {
		// Give a title for the action bar
		abTitle = "Twitch Live";

		// Give API URLs
		API.add("https://api.twitch.tv/kraken/streams?game=Dota+2");

		// set a feed manager
		feedManager = new FeedManager_Twitch();

		// Show menu
		setHasOptionsMenu(true);
		setOptionMenu(true, false);

		// Set retry button listener
		setRetryButtonListener(new LoadMore_Twitch());
		
		// Clear fragment back stack
		clearFragmentStack();

	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
        menu.add("Refresh")
        .setIcon(R.drawable.ic_refresh)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
	}
	

}
