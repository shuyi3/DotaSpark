package com.examples.gg;

import org.json.JSONException;

public class Videolist_Subscription extends Videolist_Base{
	
	@Override
	protected FeedManager_Base switcher(FeedManager_Base fy, String result) {
		// TODO Auto-generated method stub
		try {
			fy = new FeedManager_Subscription(result);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return fy;
	}
}
