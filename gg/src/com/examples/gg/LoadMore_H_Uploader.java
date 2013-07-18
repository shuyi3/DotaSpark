package com.examples.gg;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import android.view.View;


public class LoadMore_H_Uploader extends LoadMore_Base_UP {
	@Override
	public void Initializing() {
		// show loading screen
		sfa.findViewById(R.id.fullscreen_loading_indicator).setVisibility(
				View.VISIBLE);

		// Give a title for the action bar
		abTitle = "Highlights";

		// Give API URLs
		API.add("https://gdata.youtube.com/feeds/api/users/WK3QT_GLR3y_lSNYSRkMHw/subscriptions?v=2&max-results=10&alt=json");

		// initialize the fragments in the Menu
		FragmentAll = new LoadMore_H_Subscription();
		FragmentUploader = new LoadMore_H_Uploader();
		FragmentPlaylist = new LoadMore_H_Playlist();

		// set a feed manager
		feedManager = new FeedManager_Uploader();

		// Show menu
		setHasOptionsMenu(true);
		setOptionMenu(true, true);
		
		// Set retry button listener
		setRetryButtonListener(new LoadMore_H_Uploader());

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
				subMenu1Item.setTitle("Uploaders");
				subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
			}

	}

	// this method is used in the method "onListItemClick" to pass API to the
	// next fragment
	@Override
	public void InitializingNextFragment() {

		mLoadMore = new LoadMore_H_L2(nextFragmentAPI);

	}
}
