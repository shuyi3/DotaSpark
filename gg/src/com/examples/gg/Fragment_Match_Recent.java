package com.examples.gg;

import android.support.v4.app.Fragment;


public class Fragment_Match_Recent extends Fragment_Subscription{
	@Override
	public void doRequest(){
		String mySubscriptionAPI = "https://gdata.youtube.com/feeds/api/users/GJoABYYxwoGsl6TuP0DGnw/newsubscriptionvideos?max-results=10&alt=json";
		new GetRequest("").execute(mySubscriptionAPI);
	}

	@Override
	protected void titling(){
		abTitle = "Matches";
	}
	@Override
	protected Fragment fragmentPlaylists() {
		// TODO Auto-generated method stub
		return new Fragment_Match_Playlists();
	}
	@Override
	protected Fragment fragmentUploader() {
		// TODO Auto-generated method stub
		return new Fragment_Match_Uploader();
	}
	@Override
	protected Fragment fragmentAll() {
		// TODO Auto-generated method stub
		return new Fragment_Match_Recent();
	}
	
	@Override
	protected void initialize(){
		super.initialize();
		mVideolist = new Videolist_Match();
	}
}
