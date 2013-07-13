package com.examples.gg;

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

	}
}
