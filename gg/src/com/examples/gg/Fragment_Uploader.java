package com.examples.gg;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONException;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.examples.gg.Fragment_Base.GetRequest;

public class Fragment_Uploader extends Fragment_Base{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		//handles network connection;
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
	    
	    //we are in section which contains uploaders only
    	titles.add("DotaCinema");
    	titles.add("noobfromua");
    	Video dotacinema = new Video();
    	dotacinema.setAuthor("DotaCinema");
    	dotacinema.setPlaylistUrl("http://gdata.youtube.com/feeds/api/users/dotacinema/uploads?start-index=1&max-results=10&v=2&alt=json");
    	dotacinema.setThumbnailUrl("https://i1.ytimg.com/i/NRQ-DWUXf4UVN9L31Y9f3Q/1.jpg?v=5067cf3b");
    	dotacinema.setTitle("Videos from DotaCinema");
    	dotacinema.setUploaderThumUrl("https://i1.ytimg.com/i/NRQ-DWUXf4UVN9L31Y9f3Q/1.jpg?v=5067cf3b");
    	
    	Video noobfromua = new Video();
    	noobfromua.setAuthor("noobfromua");
    	noobfromua.setPlaylistUrl("http://gdata.youtube.com/feeds/api/users/noobfromua/uploads?start-index=1&max-results=10&v=2&alt=json");
    	noobfromua.setThumbnailUrl("https://i1.ytimg.com/i/fsOfLvadg89Bx8Sv_6WERg/1.jpg?v=515d687f");
    	noobfromua.setTitle("Videos from noobfromua");
    	noobfromua.setUploaderThumUrl("https://i1.ytimg.com/i/fsOfLvadg89Bx8Sv_6WERg/1.jpg?v=515d687f");
    	
    	
    	videolist.add(dotacinema);
    	videolist.add(noobfromua);
	    	
	    
	    ls.setAdapter(vaa);
	    ls.setDivider(null);
	    ls.setDividerHeight(0);

		
		setHasOptionsMenu(true);
		return view;
	}
	
	@Override
	protected void initialize(){

		ytf = new FeedManager_Base();
		mVideolist = new Videolist_Base();
	}
}
