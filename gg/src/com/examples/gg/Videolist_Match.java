package com.examples.gg;

import android.support.v4.app.Fragment;

public class Videolist_Match extends Videolist_Base{
	
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
}
