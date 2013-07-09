package com.examples.gg;

import org.json.JSONException;

public class Fragment_Subscription extends Fragment_Base{


	@Override
	protected void initialize(){

		ytf = new FeedManager_Subscription();
		mVideolist = new Videolist_Base();
	}

}
