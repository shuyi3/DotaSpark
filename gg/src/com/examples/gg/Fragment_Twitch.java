package com.examples.gg;




public class Fragment_Twitch extends Fragment_Base{

	private String api = "https://api.twitch.tv/kraken/streams?game=Dota+2";
	@Override
	public void doRequest(){
		new GetRequest("").execute(api);
	}
	
	//change the action bar title
	@Override
	protected void titling(){
		abTitle = "Twitch Live";
	}

	@Override
	protected void initialize(){

		ytf = new FeedManager_Twitch();
		mVideolist = new Videolist_Twitch();
	}
	
    @Override
	protected void setHasMenu(){
		setHasOptionsMenu(false);
	}
	

}
