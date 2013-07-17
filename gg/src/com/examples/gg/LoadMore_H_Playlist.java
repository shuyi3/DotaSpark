package com.examples.gg;

import java.util.List;

import org.json.JSONException;

import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.costum.android.widget.LoadMoreListView;
import com.examples.gg.LoadMore_Base.LoadMoreTask;

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

		// Need filter noobfromua's playlists
		needFilter = true;

		// Set retry button listener
		mRetryButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Set retry button listener
				mRetryButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						
						// Continue to check network status
						networkHandler(new LoadMore_H_Playlist());

					}
				});

			}
		});

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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		SubMenu subMenu1 = menu.addSubMenu(0, 1, 0, "Action Item");
		subMenu1.add(0, 11, 0, "All(Default)");
		subMenu1.add(0, 12, 0, "Uploaders");
		subMenu1.add(0, 13, 0, "Playlists");

		MenuItem subMenu1Item = subMenu1.getItem();
		subMenu1Item.setTitle("Playlists");
		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	}

	// this method is used in the method "onListItemClick" to pass API to the
	// next fragment
	@Override
	public void InitializingNextFragment() {

		mLoadMore = new LoadMore_H_L2(nextFragmentAPI);

	}
}
