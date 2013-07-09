package com.examples.gg;

import android.support.v4.app.Fragment;
import android.view.View;

public class Fragment_Match_Uploader extends Fragment_Uploader{

	@Override
	public void doRequest(){
    	titles.add("BeyondTheSummitTV");

    	Video dotacinema = new Video();
    	dotacinema.setAuthor("BeyondTheSummitTV");
    	dotacinema.setPlaylistUrl("http://gdata.youtube.com/feeds/api/users/beyondthesummittv/uploads?start-index=1&max-results=10&v=2&alt=json");
    	dotacinema.setThumbnailUrl("https://i1.ytimg.com/i/QfAxSNTJvLISaFNJ0Dmg8w/1.jpg?v=51b5504b");
    	dotacinema.setTitle("Videos from BeyondTheSummitTV");
    	dotacinema.setUploaderThumUrl("https://i1.ytimg.com/i/QfAxSNTJvLISaFNJ0Dmg8w/1.jpg?v=51b5504b");
    	   	
    	
    	videolist.add(dotacinema);
    	vaa.notifyDataSetChanged();
		//loading done
		this.getSherlockActivity().findViewById(R.id.fullscreen_loading_indicator).setVisibility(View.GONE);
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
