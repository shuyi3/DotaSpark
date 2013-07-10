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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
 
public class Fragment_Base extends SherlockListFragment {
  
	
	static ArrayList<String> MOBILE_OS;
	protected ArrayList<String> titles;
	protected ArrayList<Video> videolist;
	protected VideoArrayAdapter vaa;
	protected SherlockFragmentActivity sfa;
	protected InternetConnection ic;
	protected ActionBar ab;
	protected String abTitle = "Highlights";
	protected FeedManager_Base ytf = new FeedManager_Base();
	protected Fragment mVideolist = new Videolist_Base();

	
//	private PopupWindow mPop;
//	private View menuLayout;
	ArrayList<Item> items = new ArrayList<Item>();
	AlertDialog dialog;

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		titling();
		//handles network connection;
		ic = new InternetConnection();
		
		
		sfa = this.getSherlockActivity();
		ab = sfa.getSupportActionBar();

		String[] Options = new String[] {};
		titles = new ArrayList<String>();
		videolist  = new ArrayList<Video>();
		
		MOBILE_OS = new ArrayList<String>(Arrays.asList(Options));
		
		View view = inflater.inflate(android.R.layout.list_content, null);

	    ListView ls = (ListView) view.findViewById(android.R.id.list);
	    vaa = new VideoArrayAdapter(inflater.getContext(), titles, videolist);
	    //ls.setAdapter(new MobileArrayAdapter(inflater.getContext(), MOBILE_OS));
	    //we are in section which contains uploaders only

	    ls.setAdapter(vaa);
	    ls.setDivider(null);
	    ls.setDividerHeight(0);
	    
		sfa.findViewById(R.id.fullscreen_loading_indicator).setVisibility(View.VISIBLE);
		
		
		doRequest();
		setHasMenu();
		//setHasOptionsMenu(true);
		return view;
	}
	
	
	protected void setHasMenu(){
		setHasOptionsMenu(true);
	}
	
	protected void titling() {
		// TODO Auto-generated method stub
		this.abTitle = "Highlights";  
		
	}
	
	//to be modified by it's subclasses
	public void doRequest(){
		
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
	public void onListItemClick(ListView l, View v, int position, long id) {
		
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
	
	protected class GetRequest extends AsyncTask<String, String, String>{
	    protected String source;
	    
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
	        
	        //Initializing feed manager and videolist fragment,  must do it....
	        initialize();
			ytf.setmJSON(result);
	        ArrayList<Video> videos = ytf.getVideoPlaylist();
	        
	        List<String> titles=new ArrayList<String>();   
	        List<String> ids = new ArrayList<String>();
	        
	        
	        
	        //**//
	        //ytf = switcher(ytf, result);


	        
	        
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
			
		// Locate Position

//==========================================================================================================
	        String nextAPI = null;
	        try {
	        	nextAPI = ytf.getNextApi();
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
	        FragmentTransaction ft = getFragmentManager().beginTransaction();
			getSherlockActivity().getSupportActionBar().setTitle(abTitle);
			
			
			
	        Bundle bundle = new Bundle();
	        
	        bundle.putParcelableArrayList("videolist", videos);

	    	bundle.putString("query", nextAPI);

	        mVideolist.setArguments(bundle);
			
			ft.replace(R.id.content_frame, mVideolist);
			ft.commit();
//=========================================================================================================


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
	
	protected void initialize(){

		ytf = new FeedManager_Base();

		mVideolist = new Videolist_Base();
	}
	
//    //used to initialize different feed manager
//	protected FeedManager_Base switcher(FeedManager_Base fy, String result) {
//		// TODO Auto-generated method stub
//		try {
//			fy = new FeedManager_Subscription(result);
//		} catch (JSONException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		return fy;
//	}
	
 
}