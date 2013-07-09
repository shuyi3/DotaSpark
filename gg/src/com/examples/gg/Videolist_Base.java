package com.examples.gg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;



public class Videolist_Base extends SherlockListFragment{
private LoadMoreListView myLoadMoreListView;
private ArrayList<String> titles;
private ArrayList<String> videos;
private ArrayList<String> thumbList;
private ArrayList<Video> videolist;
private String query;
private boolean isMoreVideos;
protected InternetConnection ic;
protected SherlockFragmentActivity sfa;
private String theSource = "";
protected ActionBar ab;
protected String abTitle;
protected FeedManager_Base ytf;
protected Fragment mVideolist = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
		
		titling();
		ic = new InternetConnection(); 
		View view = inflater.inflate(R.layout.loadmore_list, null);
		sfa = this.getSherlockActivity();
		
		//get action bar
		ab = sfa.getSupportActionBar();


		savedInstanceState = this.getArguments();
		
		titles = new ArrayList<String>();
		videos = new ArrayList<String>();
		thumbList = new ArrayList<String>();
		
		videolist = savedInstanceState.getParcelableArrayList ("videolist");
		query = savedInstanceState.getString("query");
		//theSource = savedInstanceState.getString("source");
		
		//check whether there are more videos in the playlist
		if(query == null){
			isMoreVideos = false;
		}else isMoreVideos = true;
		
		
        for(Video v:videolist){
        	System.out.println("Title: " + v.getTitle());
        	System.out.println("video: " + v.getVideoId());
        	System.out.println("thumb: " + v.getThumbnailUrl());
        	System.out.println("duration: " + v.getDuration());
        	titles.add(v.getTitle());
        	videos.add(v.getVideoId());
        	thumbList.add(v.getThumbnailUrl());
        }
		
		for (String v: videos){
				
			System.out.println("ID: "+ v);
		}

		if(titles!=null){
			setListAdapter(new VideoArrayAdapter(inflater.getContext(), titles, videolist));
		}
		
					
		//this.getSherlockActivity().findViewById(R.id.content_frame).setVisibility(View.VISIBLE);
		
		//loading done
		this.getSherlockActivity().findViewById(R.id.fullscreen_loading_indicator).setVisibility(View.GONE);
		
		setHasOptionsMenu(true);
		return view;
	
	}
	
	
	protected void titling() {
		// TODO Auto-generated method stub
		this.abTitle = "Highlights";  
		
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
    }
    
    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item)
    {
    	
    	if(ic.isOnline(sfa)){
    		FragmentTransaction ft = getFragmentManager().beginTransaction();
    		
	        switch(item.getItemId())
	        {
	        case 11:
				ab.setTitle(abTitle);
				Fragment byAll = fragmentAll(); 
						//new Fragment_Base();		
				ft.replace(R.id.content_frame, byAll);
				break;
				
            case 12:
				ab.setTitle(abTitle);
				Fragment byUploader =  fragmentUploader();
						//new Fragment_Uploader();		
				ft.replace(R.id.content_frame, byUploader);
	        	break;
	        	
            case 13:
				ab.setTitle(abTitle);
				Fragment byPlaylist =  fragmentPlaylists(); 
						//new Fragment_Playlists();		
				ft.replace(R.id.content_frame, byPlaylist);
	        	break;
            default:
                  return super.onOptionsItemSelected(item);
	        }
	        ft.commit();
	        
    	}else{
    		ic.networkToast(this.getSherlockActivity());
    	}
    	
    	return true;
    }
	
	protected Fragment fragmentPlaylists() {
		// TODO Auto-generated method stub
		return new Fragment_Playlists();
	}
	protected Fragment fragmentUploader() {
		// TODO Auto-generated method stub
		return new Fragment_Uploader();
	}
	protected Fragment fragmentAll() {
		// TODO Auto-generated method stub
		return new Fragment_Base();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    super.onActivityCreated(savedInstanceState);
	    
		myLoadMoreListView =  (LoadMoreListView) this.getListView();
		myLoadMoreListView.setDivider(null);
		
		if(ic.isOnline(sfa)){
			if (isMoreVideos)
			myLoadMoreListView
			.setOnLoadMoreListener(new OnLoadMoreListener() {
				public void onLoadMore() {
					// Do the work to load more items at the end of list
					// hereru
					
					//checking network
					if(ic.isOnline(sfa)){
						//network ok
						if(isMoreVideos == true){
							new LoadMoreTask(theSource).execute(query);
						}
					}else{
						ic.networkToast(sfa);
						((LoadMoreListView) getListView()).onLoadMoreComplete();
					}
						
				}
			});
			
			else myLoadMoreListView.setOnLoadMoreListener(null);
			}
		else{
			ic.networkToast(sfa);
		}
	}

	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		//check network first	
		if(ic.isOnline(this.getSherlockActivity())){
			//get selected items
			String selectedValue = (String) getListAdapter().getItem(position);
			Toast.makeText(this.getSherlockActivity(), videos.get(position), Toast.LENGTH_SHORT).show();
			
	        Intent i = new Intent(this.getSherlockActivity(), VideoPlayer.class);
	        i.putExtra("video", videolist.get(position));
	        startActivity(i);
		}else{
			ic.networkToast(this.getSherlockActivity());
		}
		
	}	
	
	private class LoadMoreTask extends AsyncTask<String, String, String>{
	    private JSONObject feed;
	    private String source;
	    public LoadMoreTask(String s){
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
	        //Do anything with response..
	        //System.out.println(result);
	    	
	    	
	    	//ytf = switcher(ytf,result);
	    	initialize();
	    	ytf.setmJSON(result);
	        
	        List<Video> newVideos = ytf.getVideoPlaylist();
	        for(Video v:newVideos){
//	            System.out.println(v.getVideoId());
	        	titles.add(v.getTitle());
	        	videos.add(v.getVideoId());
	        	videolist.add(v);
	        }
	        try {
				query = ytf.getNextApi();
				if(query == null){
					isMoreVideos = false;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			((BaseAdapter) getListAdapter()).notifyDataSetChanged();

			// Call onLoadMoreComplete when the LoadMore task, has finished
			((LoadMoreListView) getListView()).onLoadMoreComplete();
			
			if (!isMoreVideos) {
				((LoadMoreListView) getListView()).onNoMoreItems();
				
				myLoadMoreListView.setOnLoadMoreListener(null);
			}

			super.onPostExecute(result);

	    }
	    
		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			((LoadMoreListView) getListView()).onLoadMoreComplete();
		}
	    

	    
	   
	}
	

	protected void initialize(){

		ytf = new FeedManager_Subscription();
		mVideolist = new Videolist_Base();
	}
	
//    //used to initialize different feed manager
//	protected FeedManager_Base switcher(FeedManager_Base fy, String result) {
//		// TODO Auto-generated method stub
//		try {
//			fy = new FeedManager_Base(result);
//		} catch (JSONException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		return fy;
//	}
	
 
}
