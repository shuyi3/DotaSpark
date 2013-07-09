package com.examples.gg;

import java.util.ArrayList;
import java.util.List;

import android.view.View;


public class Fragment_Playlists extends Fragment_Base{
	protected String dotacinemaAPI = "https://gdata.youtube.com/feeds/api/users/dotacinema/playlists?v=2&max-results=50&alt=json";
	protected  String noobfromuaAPI = "https://gdata.youtube.com/feeds/api/users/noobfromua/playlists?v=2&max-results=50&alt=json";
	protected ArrayList<String> API;
	@Override
	public void doRequest(){
		API = new ArrayList<String>();
		new GetRequest2("").execute(dotacinemaAPI);
		new GetRequest2("").execute(noobfromuaAPI);
	}
	
	@Override
	protected void initialize(){

		ytf = new FeedManager_Base();
		mVideolist = new Videolist_Base();
	}

	
	public class GetRequest2 extends GetRequest{

	    public GetRequest2(String s) {
			super(s);
			// TODO Auto-generated constructor stub
		}
	    @Override
	    protected void onPostExecute(String result) {
	        //Do anything with response..
	        //System.out.println("my json: "+ result);
	        ytf = new FeedManager_Playlist();
			ytf.setmJSON(result);
	        List<Video> newVideos = ytf.getVideoPlaylist();
            
	        for(Video v:newVideos){
//	            System.out.println(v.getVideoId());
	        	String theTitle = v.getTitle();
	        	if((theTitle.toUpperCase().contains("DOTA") || theTitle.toUpperCase().contains("GAMEPLAY"))
	        			&& !theTitle.toUpperCase().contains("ASSASSIN")){
		        	titles.add(v.getTitle());
		        	//videos.add(v.getVideoId());
		        	
		        	videolist.add(v);
	        	}
	        }
	        
	        //notify the adapter that the data changed
	        vaa.notifyDataSetChanged();
	        
	        //Make the loading view invisible
	        sfa.findViewById(R.id.fullscreen_loading_indicator).setVisibility(View.GONE);

	    }
	}
}
