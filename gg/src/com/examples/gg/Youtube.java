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
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

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
			new YoutubeGetRequest2().execute(q2);
			new YoutubeGetRequest2().execute(q3);
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
            case 11:
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
			new YoutubeGetRequest().execute(videolist.get(position).getPlaylistUrl());		
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
	

	private class YoutubeGetRequest extends AsyncTask<String, String, String>{
	    private JSONObject feed;
	    public YoutubeGetRequest(){
	        ;
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
	        
	        try
	        {   
	            processJSON(result);
	        } catch (JSONException e)
	        {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        
	        List<String> titles=new ArrayList<String>();   
	        List<String> ids = new ArrayList<String>();
	        
	        YoutubeFeed ytf = null;
			try {
				ytf = new YoutubeFeed(result);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        ArrayList<Video> videos = ytf.getVideoPlaylist();
	        for(Video v:videos){
//	            System.out.println(v.getVideoId());
	        	titles.add(v.getTitle());
	        	ids.add(v.getVideoId());

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
	    
	    private void processJSON(String json) throws JSONException{
	        JSONTokener jsonParser = new JSONTokener(json);  
	        // 髣�ｺｯ諡ｷ髞晄巳鬣ｯ髣�聴逖秘駁譁､阨画･よ搗蠖ｴ髞晏牽阨臥虫譌馴�讌ゐぎ遉�函譁､莠､髣��螽�妙蠢灘樵髞晄巳莠､髞滓巳莠､髣よ�驟ｱ髞��豬�on髣�ｾｾ諡ｷ髞晏牽豬�虫譌捺ｵ�函譁､豬�秩谺主ｯ碁駁遲ｹ蜒ｵ髣�沿谿ｾ髞晄巳貂先･ゐぎ遉�函譁､莠､髣��螽�･ゐ鮪諡ｷ髞晄巳譯ｨ髞滓巳螂夜羅蟄俶他髞晄巳闥矩翌譖ｪ蛹�函譁､豬�函譁､遉�函譁､蟋廱SONObject髣�ｿ捺狭髞晄巳辟ｦ讌ゐ�蜿ｮ髞晄巳蛛･髢ｭ遒芽ｮｲ髞滓巳豬�
	        // 髣�ョ闥狗ｻｻﾑ��髞晢ｽ丞▼髣�ｺｯ諡ｷ髞晄巳鬣ｯ髣�聴逖秘駁譁､阨画･ｱ謦�ｶｧ髞滓巳豬�函譁､闥矩函譁､莠､髣��螽�妙蠢灘樵髞晄巳豬�答繝ｯ隶ｲ髞滓巳豬�函譁､逍�淀貊仙ｻｺ髞滓巳蟒ｺ"name" : 髣よ欄諡ｷ髞晏牽豬�函譁､驛企脈謗�棆驤ｮ�よ｣秘駁遒画狭髞晉ｵ忸tValue髣�ｿ捺狭髞晄巳螂夜羅蟄俶他髞晄巳闥�yuanzhifei89"髞滓巳豬�函譁､豬㏄ring髞滓巳豬�函譁､豬�
	        JSONObject wholeJson = (JSONObject) jsonParser.nextValue();  
	        // 髣�沿谿ｾ髞晄巳貂宣翌譖ｪ蛹�灯逋ｸ閭ｶ迺腫あ蛟�函譁､諡ｷ髞晄巳諡ｷ髞晏ｸｮ諡ｷ髞晞″豈夐質謦�ｵ�駁逍婀ON髣�ｿ捺狭髞晄巳辟ｦ讌ゐ�蜿ｮ髞晄巳蛛･讌ｱ謦�ｶｧ髞滓巳豬�逐訷､諡ｷ骼ｶ蜍ｮ迴ｱ豬ｼ豌ｾ豬�駁迪ｴ諡ｷ髞晢ｿｽ
	        this.feed = wholeJson.getJSONObject("feed");
	        
	        
	    }
	    
	   
	}
	
	private class YoutubeGetRequest2 extends AsyncTask<String, String, String>{
	    private JSONObject feed;
	    public YoutubeGetRequest2(){
	        ;
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
	    	YoutubeFeed ytf = null;
	        try
	        {   
	            ytf = new YoutubeFeed(result);
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
	        
//	        for(String s:titles){
//	        	System.out.println(s);
//	        }

//	        ListFragment lFrag = (ListFragment) getFragmentManager().findFragmentById(android.R.id.list);
//	        BaseAdapter adapter = (BaseAdapter) lFrag.getListAdapter();
//	        adapter.notifyDataSetChanged();
	        vaa.notifyDataSetChanged();
	        
	        //Make the loading view invisible
	        sfa.findViewById(R.id.fullscreen_loading_indicator).setVisibility(View.GONE);
	        
	        //show content view
	        //sfa.findViewById(R.id.content_frame).setVisibility(View.VISIBLE);
	        //pd.dismiss();
	        

	        //rl.setVisibility(View.GONE);
			


	    }
	    
	    private void processJSON(String json) throws JSONException{
	        JSONTokener jsonParser = new JSONTokener(json);  

	        JSONObject wholeJson = (JSONObject) jsonParser.nextValue();  

	        this.feed = wholeJson.getJSONObject("feed");
	        
	        
	    }
	    
	   
	}
 
}