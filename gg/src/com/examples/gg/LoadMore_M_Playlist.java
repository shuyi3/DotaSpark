package com.examples.gg;

public class LoadMore_M_Playlist extends LoadMore_Base_UP {
	@Override
	public void Initializing() {
		// Give a title for the action bar
		abTitle = "Match";

		// Give API URLs
		API.add("https://gdata.youtube.com/feeds/api/users/beyondthesummittv/playlists?v=2&max-results=50&alt=json");
		API.add("https://gdata.youtube.com/feeds/api/users/joinDOTA/playlists?v=2&max-results=50&alt=json");

		// initialize the fragments in the Menu
		FragmentAll = new LoadMore_M_Subscription();
		FragmentUploader = new LoadMore_M_Uploader();
		FragmentPlaylist = new LoadMore_M_Playlist();

		// set a feed manager
		feedManager = new FeedManager_Playlist();

		// Show menu component
		setHasOptionsMenu(true);

	}

	// this method is used in the method "onListItemClick" to pass a API to the
	// next fragment
	@Override
	public void InitializingNextFragment() {

		mLoadMore = new LoadMore_M_L2(nextFragmentAPI);

	}
}
