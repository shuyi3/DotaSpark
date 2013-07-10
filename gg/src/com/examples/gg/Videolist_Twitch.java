package com.examples.gg;



public class Videolist_Twitch extends Videolist_Base {
	//change the action bar title
	@Override
	protected void titling(){
		abTitle = "Twitch Live";
	}
	
    @Override
	protected void setHasMenu(){
		setHasOptionsMenu(false);
	}
    
	@Override
	protected void initialize(){
		ytf = new FeedManager_Twitch();
		mVideolist = new Videolist_Twitch();
	}
}
