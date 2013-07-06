package com.examples.gg;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
 
public class Youtube extends SherlockListFragment {
  
	
	static ArrayList<String> MOBILE_OS;
	private ArrayList<String> titles;
	private ArrayList<Video> videolist;
	private String q2 = "https://gdata.youtube.com/feeds/api/users/dotacinema/playlists?v=2&max-results=50&alt=json";
	private String q3 = "https://gdata.youtube.com/feeds/api/users/noobfromua/playlists?v=2&max-results=50&alt=json";
	private VideoArrayAdapter vaa;
	private String section;
	private SherlockFragmentActivity sfa;
	
//	private PopupWindow mPop;
//	private View menuLayout;
	ArrayList<Item> items = new ArrayList<Item>();
	AlertDialog dialog;

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		
		sfa = this.getSherlockActivity();
		savedInstanceState = this.getArguments();
		section = savedInstanceState.getString("SECTION");
		String[] Options = new String[] {};
		titles = new ArrayList<String>();
		videolist  = new ArrayList<Video>();
		
		MOBILE_OS = new ArrayList<String>(Arrays.asList(Options));
		
		View view = inflater.inflate(android.R.layout.list_content, null);

	    ListView ls = (ListView) view.findViewById(android.R.id.list);
	    vaa = new VideoArrayAdapter(inflater.getContext(), titles, videolist);
	    //ls.setAdapter(new MobileArrayAdapter(inflater.getContext(), MOBILE_OS));
	    //we are in section which contains uploaders only
	    
	    if(section.equals("UPLOADER")){
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
	    	
	    }
	    
	    
	    
	    ls.setAdapter(vaa);
	    ls.setDivider(null);
	    ls.setDividerHeight(0);

		if(section.equals("PLAYLIST")){
			//sfa.findViewById(R.id.content_frame).setVisibility(View.GONE);
			sfa.findViewById(R.id.fullscreen_loading_indicator).setVisibility(View.VISIBLE);
			new GetRequest2("").execute(q2);
			new GetRequest2("").execute(q3);
		}
		
		if(section.equals("TWITCHLIVE")){
			sfa.findViewById(R.id.fullscreen_loading_indicator).setVisibility(View.VISIBLE);
			new GetRequest("Twitch").execute("https://api.twitch.tv/kraken/streams?game=Dota+2");
			System.out.println("It's twitch section");

		}
		
		setHasOptionsMenu(true);
		return view;
	}
	
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	
        SubMenu subMenu1 = menu.addSubMenu(0,1,0,"Action Item");
        subMenu1.add(0,11,0,"All(Default)");
        subMenu1.add(0,12,0,"Uploaders");
        subMenu1.add(0,13,0,"Playlists");

        MenuItem subMenu1Item = subMenu1.getItem();
        subMenu1Item.setTitle("Classify");
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

//        SubMenu subMenu2 = menu.addSubMenu("Overflow Item");
//        subMenu2.add("These");
//        subMenu2.add("Are");
//        subMenu2.add("Sample");
//        subMenu2.add("Items");
//
//        MenuItem subMenu2Item = subMenu2.getItem();
//        subMenu2Item.setIcon(R.drawable.ic_compose);

//        return super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item)
    {
        switch(item.getItemId())
        {
            case 12:
                  initPopWindow();
//                mPop.showAtLocation(menuLayout, 
//                        Gravity.CENTER, 0, 0);
                  dialog.show();
//                  Toast.makeText(getSherlockActivity(), item.getItemId(), Toast.LENGTH_SHORT).show();
                  return true;
            default:
                  return super.onOptionsItemSelected(item);
        }
    }
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
 
		//get selected items
		//String selectedValue = (String) getListAdapter().getItem(position);
		//Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
		//pd = ProgressDialog.show(appContext,"Loading","Dota spark is working hard to load videos for you!",true,false,null);



		//sfa.findViewById(R.id.content_frame).setVisibility(View.GONE);
		
		
		InternetConnection ic = new InternetConnection();
		if(ic.isOnline(sfa)){
			//internet is ok
			//start loading
			sfa.findViewById(R.id.fullscreen_loading_indicator).setVisibility(View.VISIBLE);
			new GetRequest("Youtube").execute(videolist.get(position).getPlaylistUrl());		
		}else{
			ic.networkToast(sfa);
		}

	
	}
	
	private void initPopWindow() {
		
		items.clear();
        items.add(new EntryItem("DotaCinema", "DC comment", R.drawable.action_about));
        items.add(new EntryItem("Noobfromua", "big noob", R.drawable.collections_cloud));
        
        EntryAdapter adapter = new EntryAdapter(getSherlockActivity(), items);

//        ContextThemeWrapper ctw = new ContextThemeWrapper(getSherlockActivity(), R.style.MyCustomThemeDialogVariant);
        AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());

        builder.setTitle("Select Option");
        ListView modeList = new ListView(getSherlockActivity()); 
        modeList.setBackgroundResource(R.drawable.my_theme_gradient);
        modeList.setAdapter(adapter);
        builder.setView(modeList);     
//        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
//
//               public void onClick(DialogInterface dialog, int which) {
//                     // TODO Auto-generated method stub
//
//               }
//        });
        dialog = builder.create();
		
    }
	

	private class GetRequest extends AsyncTask<String, String, String>{
	    private String source;
	    public GetRequest(String s){
	    	
	        this.source = s;
	    }
	    @Override
	    protected String doInBackground(String... uri) {
	    	
	    
	        HttpClient httpclient = new DefaultHttpClient();
	        HttpResponse response;
	        String responseString = null;
	        try {
	            response = httpclient.execute(new HttpGet(uri[0]));
	            StatusLine statusLine = response.getStatusLine();
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	            } else{
	                //Closes the connection.
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
	        } catch (ClientProtocolException e) {
	            //TODO Handle problems..
	        } catch (IOException e) {
	            //TODO Handle problems..
	        }
	        return responseString;
	    }

	    @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);
	        //Do anything with response..
	        //System.out.println(result);
	        

	        
	        List<String> titles=new ArrayList<String>();   
	        List<String> ids = new ArrayList<String>();
	        
	        FeedManager ytf = null;
	        
			try {
				ytf = new FeedManager(result, source);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        ArrayList<Video> videos = ytf.getVideoPlaylist();
	        for(Video v:videos){
//	            System.out.println(v.getVideoId());
	        	titles.add("Title: " + v.getTitle());
	        	ids.add("ID: " + v.getVideoId());

	        }
	        
	        String[] mStringArray = new String[titles.size()];
			mStringArray = titles.toArray(mStringArray);
			
	        String[] idsArray = new String[ids.size()];
	        idsArray = ids.toArray(idsArray);

			for(int i = 0; i < mStringArray.length ; i++){
			    System.out.println(mStringArray[i]);
			}
	        
	        //we can get more videos from this playlist if there are more
	        try
	        {
	            System.out.println("***************************Next api: "+ytf.getNextApi());
	        } catch (JSONException e)
	        {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        
	        //pd.dismiss();
			FragmentTransaction ft = getFragmentManager().beginTransaction();
		// Locate Position

//==========================================================================================================
			getSherlockActivity().getSupportActionBar().setTitle("Videolist");
			Fragment videolist = new videolist();
			
	        Bundle bundle = new Bundle();
	        
	        bundle.putParcelableArrayList("videolist", videos);
	        bundle.putString("source", this.source);
	        try {
	        	bundle.putString("query", ytf.getNextApi());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        videolist.setArguments(bundle);
			
			ft.replace(R.id.content_frame, videolist);
			ft.commit();
			
//=========================================================================================================


	    }
	    
	   
	}
	
	
	private class GetRequest2 extends GetRequest{

	    public GetRequest2(String s) {
			super(s);
			// TODO Auto-generated constructor stub
		}
	    @Override
	    protected void onPostExecute(String result) {
	        //Do anything with response..
	        //System.out.println(result);
	    	FeedManager ytf = null;
	        try
	        {   
	            ytf = new FeedManager(result, "Youtube");
	        } catch (JSONException e)
	        {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	                    
	        List<Video> newVideos = ytf.getVideoPlaylist2();
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