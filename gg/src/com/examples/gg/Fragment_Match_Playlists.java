package com.examples.gg;

import android.support.v4.app.Fragment;


public class Fragment_Match_Playlists extends Fragment_Playlists{
	@Override
	public void doRequest(){
		new GetRequest2("").execute("https://gdata.youtube.com/feeds/api/users/beyondthesummittv/playlists?v=2&max-results=50&alt=json");
	}
	
	//change the action bar title
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
