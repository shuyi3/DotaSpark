package com.examples.gg;

public class LoadMore_Search extends LoadMore_Base{
	
	@Override
	public void Initializing() {
		// Give a title for the action bar
		abTitle = "Search";

		// Give API URLs
		API.add("https://gdata.youtube.com/feeds/api/users/WK3QT_GLR3y_lSNYSRkMHw/newsubscriptionvideos?max-results=10&alt=json");

		// set a feed manager
		feedManager = new FeedManager_Subscription();

		// Show menu		
		setHasOptionsMenu(true);


	}
}
