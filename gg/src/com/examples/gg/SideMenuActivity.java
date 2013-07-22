package com.examples.gg;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class SideMenuActivity extends SherlockFragmentActivity {

	// Declare Variable
	DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;
	// MenuListAdapter mMenuAdapter;
	// String[] title;
	// String[] subtitle;
	// int[] icon;

	ArrayList<Item> items = new ArrayList<Item>();
	ActionBar mActionBar;
	EntryAdapter eAdapter;
	
	private Activity sfa;
	private int currentDrawerFragmentId = 1;
	private Button retryButton;
	private View mRetryView;
	private FragmentManager fm;
	private InternetConnection ic;
	private View fullscreenLoadingView;
//	public View row;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_main);
		sfa = this;
		
		// Initial fragment manager
		fm = this.getSupportFragmentManager();
		// Generate title
		// title = new String[] { "Title Fragment 1", "Title Fragment 2",
		// "Title Fragment 3" };
		//
		// // Generate subtitle
		// subtitle = new String[] { "Subtitle Fragment 1",
		// "Subtitle Fragment 2",
		// "Subtitle Fragment 3" };
		//
		// // Generate icon
		// icon = new int[] { R.drawable.action_about,
		// R.drawable.action_settings,
		// R.drawable.collections_cloud };
		
		ic = new InternetConnection();
        
		fullscreenLoadingView = sfa.findViewById(R.id.fullscreen_loading_indicator);
		
		// Locate DrawerLayout in drawer_main.xml
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// Locate ListView in drawer_main.xml
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		
		// Do not allow list view to scroll over
		mDrawerList.setOverScrollMode(ListView.OVER_SCROLL_NEVER);

		// Set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// Pass results to MenuListAdapter Class
		// mMenuAdapter = new MenuListAdapter(this, title, subtitle, icon);
		items.add(new SectionItem("Everyday's Feed"));
		items.add(new EntryItem("What's new", "Fresh meat!",
				R.drawable.action_about));

		items.add(new SectionItem("Latest Videos"));
		items.add(new EntryItem("Highlights", "Dota excitements",
				R.drawable.action_about));
		items.add(new EntryItem("Matches", "You don't wanna miss it",
				R.drawable.collections_cloud));

		items.add(new SectionItem("Lives"));
		items.add(new EntryItem("Twitch Streams", "Battle begins!",
				R.drawable.collections_cloud));
		
		items.add(new SectionItem("Match Table"));
		items.add(new EntryItem("Upcoming Matches", "matches approaching!",
				R.drawable.collections_cloud));
		items.add(new EntryItem("Recent Results", "It's in the bag!",
				R.drawable.collections_cloud));

	    eAdapter = new EntryAdapter(this, items);

		mRetryView = sfa.findViewById(R.id.mRetry);
	    
		// setListAdapter(adapter);

		// Set the MenuListAdapter to the ListView
		mDrawerList.setAdapter(eAdapter);

		// Capture button clicks on side menu
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Enable ActionBar app icon to behave as action to toggle nav drawer
		mActionBar = getSupportActionBar();

		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		//mActionBar.setTitle("Main Menu");

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				// TODO Auto-generated method stub
				super.onDrawerClosed(view);
			}

			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				super.onDrawerOpened(drawerView);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// set a listener for retry button
        retryButton = (Button) findViewById(R.id.mRetryButton);
        setRetryListener();
		
		if (savedInstanceState == null) {
			selectItem(1);
			
		}
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {

			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
		}

		return super.onOptionsItemSelected(item);
	}

	// The click listener for ListView in the navigation drawer
	public class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
//		    if (row != null) {
//		        row.setBackgroundColor(Color.WHITE);
//		    }
//		    row = view;
//		    view.setBackgroundColor(Color.YELLOW);
//		    		    
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// check network
		

		if (ic.checkConnection(this)) {
			for (Item i : items)
			    i.setUnchecked();
			items.get(position).setChecked();
			eAdapter.notifyDataSetChanged();

			
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			// Locate Position
			ArrayList<String> al = new ArrayList<String>();
			
			// Clear the fragment stack first
			clearFragmentStack();
			
			mDrawerList.setItemChecked(position, true);
//			mDrawerList.setSelection(position);
			// Close drawer
//			mDrawerLayout.closeDrawer(mDrawerList);
			
		    Handler handler = new Handler(); 
		    handler.postDelayed(new Runnable() { 
		         public void run() { 
		 			mDrawerLayout.closeDrawer(mDrawerList);
		         } 
		    }, 0);
			
			switch (position) {

			case 1:
				// News
				ft.replace(R.id.content_frame, new LoadMore_News());
				break;

			case 3:
				// Highlight section
				ft.replace(R.id.content_frame, new LoadMore_H_Subscription());
				break;

			case 4:
				// Match section
				ft.replace(R.id.content_frame, new LoadMore_M_Subscription());
				break;

			case 6:
				// Twitch section
				ft.replace(R.id.content_frame, new LoadMore_Twitch());
				break;
				
			case 8:
				// Twitch section
				ft.replace(R.id.content_frame, new UpcomingFragment());
				break;
			}
			
			ft.commit();


		}else{
			// All errors should be full screen loading error
			// Reset listener since listener may be changed by other fragments
			setRetryListener();
			currentDrawerFragmentId = position;
			
			// Close drawer
			mDrawerLayout.closeDrawer(mDrawerList);
			
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	public void retry(View v){
		this.findViewById(R.id.mRetry).setVisibility(
				View.GONE);
		
	}
	
	public void setRetryListener(){
        retryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(ic.isOnline(sfa)){
            		//fullscreenLoadingView.setVisibility(View.GONE);
	                // Perform action on click
	            	mRetryView.setVisibility(View.GONE);
	            	// Going to News section by default
	            	selectItem(currentDrawerFragmentId);
            	}
            }
        });
	}
	// Clear fragment back stack
	public void clearFragmentStack(){
		fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

}
