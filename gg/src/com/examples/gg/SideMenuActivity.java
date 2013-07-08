package com.examples.gg;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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
//	MenuListAdapter mMenuAdapter;
//	String[] title;
//	String[] subtitle;
//	int[] icon;

	ArrayList<Item> items = new ArrayList<Item>();
	ActionBar mActionBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_main);

		// Generate title
//		title = new String[] { "Title Fragment 1", "Title Fragment 2",
//				"Title Fragment 3" };
//
//		// Generate subtitle
//		subtitle = new String[] { "Subtitle Fragment 1", "Subtitle Fragment 2",
//				"Subtitle Fragment 3" };
//
//		// Generate icon
//		icon = new int[] { R.drawable.action_about, R.drawable.action_settings,
//				R.drawable.collections_cloud };

		// Locate DrawerLayout in drawer_main.xml
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// Locate ListView in drawer_main.xml
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// Set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// Pass results to MenuListAdapter Class
//		mMenuAdapter = new MenuListAdapter(this, title, subtitle, icon);
		
        items.add(new SectionItem("Latest Videos"));
        items.add(new EntryItem("Highlights", "Dota excitements", R.drawable.action_about));
        items.add(new EntryItem("Recent matches", "You don't wanna miss it", R.drawable.collections_cloud));
        
        
        items.add(new SectionItem("Lives"));
        items.add(new EntryItem("Twitch lives", "Battle begins!", R.drawable.collections_cloud));
        
        EntryAdapter adapter = new EntryAdapter(this, items);
        
//        setListAdapter(adapter);

		// Set the MenuListAdapter to the ListView
		mDrawerList.setAdapter(adapter);

		// Capture button clicks on side menu
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Enable ActionBar app icon to behave as action to toggle nav drawer
		mActionBar = getSupportActionBar();
		
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("Main Menu");

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

		if (savedInstanceState == null) {
			selectItem(0);
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
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		//check network
		InternetConnection ic = new InternetConnection(); 
		
		if(ic.isOnline(this)){
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			// Locate Position
			switch (position) {
			case 1:		
				mActionBar.setTitle("Highlights");
				Fragment byAll = new Fragment_Subscription();
				
				ft.replace(R.id.content_frame, byAll);
				break;
				
			case 2:
				mActionBar.setTitle("Recent Matches");
				Fragment byUploader = new Fragment_Uploader();
				ft.replace(R.id.content_frame, byUploader);
				break;
//			case 3:
//				mActionBar.setTitle("Recent Matches");
//				
//				Fragment fragment2 = new DummySectionFragment();
//				Bundle args2 = new Bundle();
//				args2.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
//				fragment2.setArguments(args2);
//	
//				ft.replace(R.id.content_frame, fragment2);
//				break;
			case 4:
//				mActionBar.setTitle("Twitch Lives");
//				Fragment twitchLive = new Fragment_Base();
//				Bundle uArgs3 = new Bundle();
//				uArgs3.putString("SECTION", "TWITCHLIVE");
//				twitchLive.setArguments(uArgs3);
//				ft.replace(R.id.content_frame, twitchLive);
				break;
			}
			ft.commit();
			mDrawerList.setItemChecked(position, true);
			// Close drawer
			mDrawerLayout.closeDrawer(mDrawerList);
		
		}else{
			ic.networkToast(this);
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
	

    
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
			TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
	}
}
