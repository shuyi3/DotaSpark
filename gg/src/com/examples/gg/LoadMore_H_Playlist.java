package com.examples.gg;


public class LoadMore_H_Playlist extends LoadMore_Base_UP {
	@Override
	public void Initializing() {
		// Give a title for the action bar
		abTitle = "Highlights";
		
		// Set action bar title
		ab.setTitle(abTitle);

		// Give API URLs
		API.add("https://gdata.youtube.com/feeds/api/users/dotacinema/playlists?v=2&max-results=50&alt=json");
		API.add("https://gdata.youtube.com/feeds/api/users/noobfromua/playlists?v=2&max-results=50&alt=json");
		
		// initialize the fragments in the Menu
		FragmentAll = new LoadMore_H_Subscription();
		FragmentUploader = new LoadMore_H_Uploader();
		FragmentPlaylist = new LoadMore_H_Playlist();
		

		// set a feed manager
		feedManager = new FeedManager_Playlist();

		// Show menu component
		setHasOptionsMenu(true);

	}
	
	
	// this method is used in the method "onListItemClick" to pass API to the
	// next fragment
	@Override
	public void InitializingNextFragment() {

		mLoadMore = new LoadMore_H_L2(nextFragmentAPI);

	}
}
