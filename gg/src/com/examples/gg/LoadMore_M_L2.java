package com.examples.gg;

import android.view.View;

public class LoadMore_M_L2 extends LoadMore_Base {
	private String mAPI;

	public LoadMore_M_L2(String url) {
		this.mAPI = url;
	}

	@Override
	public void Initializing() {
		// Give a title for the action bar
		abTitle = "Match";

		// API can be get from the previous fragment
		API.add(mAPI);

		// initialize the fragments in the Menu
		FragmentAll = new LoadMore_M_Subscription();
		FragmentUploader = new LoadMore_M_Uploader();
		FragmentPlaylist = new LoadMore_M_Playlist();

		// set a feed manager
		feedManager = new FeedManager_Base();

		// Show menu
		setHasOptionsMenu(false);
		
		// Set retry button listener
		setRetryButtonListener(new LoadMore_M_L2(mAPI));

	}
}
