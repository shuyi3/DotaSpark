package com.examples.gg;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LoadMore_H_Playlist extends LoadMore_Base_UP {
	
    private TextView mSelected;
    private String[] mLocations;
    
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
		setOptionMenu(true, true);

		// Need filter noobfromua's playlists
		needFilter = true;
		
		currentPosition = 2;

	}

	@Override
	protected void filtering(Video v) {
		// Filter out some unrelated videos from noobfromua
		String theTitle = v.getTitle();

		if ((theTitle.toUpperCase().contains("DOTA") || theTitle.toUpperCase()
				.contains("GAMEPLAY"))
				&& !theTitle.toUpperCase().contains("ASSASSIN")) {
			titles.add(v.getTitle());
			videos.add(v.getVideoId());
			videolist.add(v);
		}
	}
	
	@Override
	public void refreshFragment(){
		currentFragment = new LoadMore_H_Playlist();
	}


	// this method is used in the method "onListItemClick" to pass API to the
	// next fragment
	@Override
	public void InitializingNextFragment() {

		mLoadMore = new LoadMore_H_L2(nextFragmentAPI, currentPosition);

	}
}
