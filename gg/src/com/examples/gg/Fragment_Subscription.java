package com.examples.gg;



public class Fragment_Subscription extends Fragment_Base{
	protected String mySubscriptionAPI;
	@Override
	public void doRequest(){
		mySubscriptionAPI = "https://gdata.youtube.com/feeds/api/users/WK3QT_GLR3y_lSNYSRkMHw/newsubscriptionvideos?max-results=10&alt=json";
		new GetRequest(FeedManager_Base.SUBSCRIPTION).execute(mySubscriptionAPI);
	}

	@Override
	protected void initialize(){

		ytf = new FeedManager_Subscription();
		mVideolist = new Videolist_Base();
	}

}
