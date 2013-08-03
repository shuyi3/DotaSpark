package com.examples.gg;

import java.util.ArrayList;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;

public class LoadMore_Activity_Channel extends LoadMore_Activity_Base
		implements OnNavigationListener {

	@Override
	public void Initializing(){
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		final String[] catagory = { "Recent", "Playlists" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				ab.getThemedContext(), R.layout.sherlock_spinner_item,
				android.R.id.text1, catagory);

		adapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

		ab.setListNavigationCallbacks(adapter, this);

		ab.setSelectedNavigationItem(currentPosition);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		// First check it is under which section
		switch (section) {
		case 0:
			// In "Recent"
			Intent i = new Intent(this, YoutubeActionBarActivity.class);
			i.putExtra("video", videolist.get(position));
			startActivity(i);
			break;
			
		case 1:
			// In "Playlists"
			Intent i1 = new Intent(this, LoadMore_Activity_Base.class);
			
			i1.putExtra("API", videolist.get(position).getRecentVideoUrl());
			i1.putExtra("PLAYLIST_API", videolist.get(position).getPlaylistsUrl());
			i1.putExtra("title", title);
			startActivity(i1);
			break;
			
		}

	}
	
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// set section indicator
		section = itemPosition;

		oneStepRefresh();
		return true;
	}


	
	@Override
	public void refreshActivity() {

		oneStepRefresh();

	}
	
	public void oneStepRefresh(){
		if (section == 0) {
			// Section "Recent"

			redoRequest(recentAPI, new FeedManager_Base());
		}

		if (section == 1) {
			// Section "Playlists"
			redoRequest(playlistAPI, new FeedManager_Playlist());

		}
	}

}
