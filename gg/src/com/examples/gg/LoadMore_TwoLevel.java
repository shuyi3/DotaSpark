package com.examples.gg;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class LoadMore_TwoLevel extends LoadMore_Base{
//this is for Uploader and Playlist fragments
//has not been done yet
	//should only override the following method
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		//check network first	
		if(ic.isOnline(this.getSherlockActivity())){

		}else{
			ic.networkToast(this.getSherlockActivity());
		}
		
	}	
	
}
