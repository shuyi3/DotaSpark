package com.examples.gg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;

import com.examples.gg.Fragment_Base.GetRequest;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class Fragment_Playlists extends Fragment_Base{
	protected final String dotacinemaAPI = "https://gdata.youtube.com/feeds/api/users/dotacinema/playlists?v=2&max-results=50&alt=json";
	protected final String noobfromuaAPI = "https://gdata.youtube.com/feeds/api/users/noobfromua/playlists?v=2&max-results=50&alt=json";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		ic = new InternetConnection();
		sfa = this.getSherlockActivity();
		
		ab = sfa.getSupportActionBar();
		//savedInstanceState = this.getArguments();
		//section = savedInstanceState.getString("SECTION");
		String[] Options = new String[] {};
		titles = new ArrayList<String>();
		videolist  = new ArrayList<Video>();
		
		MOBILE_OS = new ArrayList<String>(Arrays.asList(Options));
		
		View view = inflater.inflate(android.R.layout.list_content, null);

	    ListView ls = (ListView) view.findViewById(android.R.id.list);
	    vaa = new VideoArrayAdapter(inflater.getContext(), titles, videolist);
 
	    ls.setAdapter(vaa);
	    ls.setDivider(null);
	    ls.setDividerHeight(0);
	    
		sfa.findViewById(R.id.fullscreen_loading_indicator).setVisibility(View.VISIBLE);
		new GetRequest2("").execute(dotacinemaAPI);
		new GetRequest2("").execute(noobfromuaAPI);

		
		setHasOptionsMenu(true);
		return view;
	}
	
	@Override
    protected FeedManager_Base switcher(FeedManager_Base fy, String result){
		try {
			fy = new FeedManager_Base(result);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return fy;
    }
	

	class GetRequest2 extends GetRequest{

	    public GetRequest2(String s) {
			super(s);
			// TODO Auto-generated constructor stub
		}
	    @Override
	    protected void onPostExecute(String result) {
	        //Do anything with response..
	        //System.out.println("my json: "+ result);
	    	FeedManager_Playlist ytf = null;
	        try
	        {   
	            ytf = new FeedManager_Playlist(result);
	        } catch (JSONException e)
	        {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
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
