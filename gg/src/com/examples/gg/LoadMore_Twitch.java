package com.examples.gg;

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
		setHasOptionsMenu(false);

		// Set retry button listener
		mRetryButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// Continue to check network status
				networkHandler(new LoadMore_Twitch());

			}
		});

	}
}
