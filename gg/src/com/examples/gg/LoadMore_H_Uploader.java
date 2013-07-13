package com.examples.gg;

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
		// API.add("https://gdata.youtube.com/feeds/api/users/WK3QT_GLR3y_lSNYSRkMHw/newsubscriptionvideos?max-results=10&alt=json");

		// initialize the fragments in the Menu
		FragmentAll = new LoadMore_H_Subscription();
		FragmentUploader = new LoadMore_H_Uploader();
		FragmentPlaylist = new LoadMore_H_Playlist();

		// set a feed manager
		feedManager = new FeedManager_Base();

		// we are in section which contains uploaders only
		titles.add("DotaCinema");
		titles.add("noobfromua");
		Video dotacinema = new Video();
		dotacinema.setAuthor("DotaCinema");
		dotacinema
				.setPlaylistUrl("http://gdata.youtube.com/feeds/api/users/dotacinema/uploads?start-index=1&max-results=10&v=2&alt=json");
		dotacinema
				.setThumbnailUrl("https://i1.ytimg.com/i/NRQ-DWUXf4UVN9L31Y9f3Q/1.jpg?v=5067cf3b");
		dotacinema.setTitle("Videos from DotaCinema");
		dotacinema
				.setUploaderThumUrl("https://i1.ytimg.com/i/NRQ-DWUXf4UVN9L31Y9f3Q/1.jpg?v=5067cf3b");

		Video noobfromua = new Video();
		noobfromua.setAuthor("noobfromua");
		noobfromua
				.setPlaylistUrl("http://gdata.youtube.com/feeds/api/users/noobfromua/uploads?start-index=1&max-results=10&v=2&alt=json");
		noobfromua
				.setThumbnailUrl("https://i1.ytimg.com/i/fsOfLvadg89Bx8Sv_6WERg/1.jpg?v=515d687f");
		noobfromua.setTitle("Videos from noobfromua");
		noobfromua
				.setUploaderThumUrl("https://i1.ytimg.com/i/fsOfLvadg89Bx8Sv_6WERg/1.jpg?v=515d687f");

		videolist.add(dotacinema);
		videolist.add(noobfromua);
		// vaa.notifyDataSetChanged();

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

		mLoadMore = new LoadMore_H_L2(nextFragmentAPI);

	}
}
