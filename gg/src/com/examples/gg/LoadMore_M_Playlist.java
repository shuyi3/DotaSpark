package com.examples.gg;

import android.view.View;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

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
		setOptionMenu(true, true);
		
		// Set retry button listener
		setRetryButtonListener(new LoadMore_M_Playlist());

	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		if (hasRefresh)
	        menu.add(0, 2, 0, "Refresh")
	        .setIcon(R.drawable.ic_refresh)
	        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

			if (hasDropDown){
				SubMenu subMenu1 = menu.addSubMenu(0, 1, 0, "Action Item");
				subMenu1.add(0, 11, 0, "All(Default)");
				subMenu1.add(0, 12, 0, "Uploaders");
				subMenu1.add(0, 13, 0, "Playlists");
		
				MenuItem subMenu1Item = subMenu1.getItem();
				subMenu1Item.setTitle("Playlists");
				subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
			}

	}
	
	@Override
	public void refreshFragment(){
		currentFragment = new LoadMore_M_Playlist();
	}

	// this method is used in the method "onListItemClick" to pass a API to the
	// next fragment
	@Override
	public void InitializingNextFragment() {

		mLoadMore = new LoadMore_M_L2(nextFragmentAPI);

	}
}
