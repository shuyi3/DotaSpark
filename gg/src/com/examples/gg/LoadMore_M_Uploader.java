package com.examples.gg;

import android.view.View;

public class LoadMore_M_Uploader extends LoadMore_Base_UP {
	@Override
	public void Initializing() {
		// show loading screen
		sfa.findViewById(R.id.fullscreen_loading_indicator).setVisibility(
				View.VISIBLE);

		// Give a title for the action bar
		abTitle = "Match";

		// Give API URLs
		// API.add("https://gdata.youtube.com/feeds/api/users/WK3QT_GLR3y_lSNYSRkMHw/newsubscriptionvideos?max-results=10&alt=json");

		// initialize the fragments in the Menu
		FragmentAll = new LoadMore_M_Subscription();
		FragmentUploader = new LoadMore_M_Uploader();
		FragmentPlaylist = new LoadMore_M_Playlist();

		// set a feed manager
		feedManager = new FeedManager_Base();

		// we are in section which contains uploaders only
		titles.add("BeyondTheSummitTV");

		Video aVideo = new Video();
		aVideo.setAuthor("BeyondTheSummitTV");
		aVideo.setPlaylistUrl("http://gdata.youtube.com/feeds/api/users/beyondthesummittv/uploads?start-index=1&max-results=10&v=2&alt=json");
		aVideo.setThumbnailUrl("https://i1.ytimg.com/i/QfAxSNTJvLISaFNJ0Dmg8w/1.jpg?v=51b5504b");
		aVideo.setTitle("Videos from BeyondTheSummitTV");
		aVideo.setUploaderThumUrl("https://i1.ytimg.com/i/QfAxSNTJvLISaFNJ0Dmg8w/1.jpg?v=51b5504b");

		videolist.add(aVideo);

		// loading done
		sfa.findViewById(R.id.fullscreen_loading_indicator).setVisibility(
				View.GONE);

		// Show menu
		setHasOptionsMenu(true);

	}

	// this method is used in the method "onListItemClick" to pass API to the
	// next fragment
	@Override
	public void InitializingNextFragment() {

		mLoadMore = new LoadMore_M_L2(nextFragmentAPI);

	}
}
