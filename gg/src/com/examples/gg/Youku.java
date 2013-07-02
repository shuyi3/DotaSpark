package com.examples.gg;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class Youku extends ListActivity{
	ImageButton youtube;
	ImageButton youku;
	GridView gridView;
	 
	static final String[] MOBILE_OS = new String[] { 
		"WoDota", "Top10","Danteng", "Dashen" };
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //	setListAdapter(new MobileArrayAdapter(this, MOBILE_OS));
    }


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
 
		//get selected items
		String selectedValue = (String) getListAdapter().getItem(position);
		Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
		ProgressDialog pd;
	
		if(selectedValue=="DotaCinema"){
			pd = ProgressDialog.show(Youku.this,"This is the title","This is the detail text",true,false,null);

	    	
			//new YoutubeFeed2(getApplicationContext(),pd).execute("https://gdata.youtube.com/feeds/api/playlists/PL981BABEC1803C00D?start-index=1&amp&max-results=5&amp&v=2&alt=json");
						
				}
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
