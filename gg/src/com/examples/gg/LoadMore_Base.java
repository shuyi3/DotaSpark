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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
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
import com.nostra13.universalimageloader.core.ImageLoader;

public class LoadMore_Base extends SherlockListFragment {
	protected LoadMoreListView myLoadMoreListView;
	protected ArrayList<String> titles;
	protected ArrayList<String> videos;
	protected ArrayList<Video> videolist;

	protected boolean isMoreVideos;
	protected InternetConnection ic;
	protected SherlockFragmentActivity sfa;
	protected ActionBar ab;
	protected String abTitle;
	protected FeedManager_Base feedManager;
	protected Fragment nextFragment;
	protected Fragment FragmentAll;
	protected Fragment FragmentUploader;
	protected Fragment FragmentPlaylist;
	protected ArrayList<String> API;
	protected View view;
	protected LayoutInflater mInflater;
	protected VideoArrayAdapter vaa;
	protected ArrayList<LoadMoreTask> mLoadMoreTasks = new ArrayList<LoadMoreTask>();
	protected Button mRetryButton;
	protected View mRetryView;
	protected boolean needFilter;
	protected FragmentManager fm;
	protected View fullscreenLoadingView;
	protected boolean hasRefresh;
	protected boolean hasDropDown;
	protected Fragment currentFragment;
	public boolean isBusy = false;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Get the current activity
		sfa = this.getSherlockActivity();

		// Get loading view
		fullscreenLoadingView = sfa
				.findViewById(R.id.fullscreen_loading_indicator);

		// default no filter for videos
		needFilter = false;

		mInflater = inflater;

		// For check internet connection
		ic = new InternetConnection();

		// set the layout
		view = inflater.inflate(R.layout.loadmore_list, null);

		// Initial fragment manager
		fm = sfa.getSupportFragmentManager();

		// Get the button view in retry.xml
		mRetryButton = (Button) sfa.findViewById(R.id.mRetryButton);

		// Get Retry view
		mRetryView = sfa.findViewById(R.id.mRetry);

		// get action bar
		ab = sfa.getSupportActionBar();

		// Initilizing the empty arrays
		titles = new ArrayList<String>();
		videos = new ArrayList<String>();
		videolist = new ArrayList<Video>();
		// thumbList = new ArrayList<String>();

		// set adapter
		// vaa = new VideoArrayAdapter(inflater.getContext(), titles, videolist,
		// this);

		API = new ArrayList<String>();

		// Initializing important variables
		Initializing();

		// Set action bar title
		ab.setTitle(abTitle);

		// check whether there are more videos in the playlist
		if (API.isEmpty())
			isMoreVideos = false;
		else if (API.get(0) != null)
			isMoreVideos = true;

		// set the adapter
		// setListAdapter(vaa);

		return view;

	}

	public void setOptionMenu(boolean hasRefresh, boolean hasDropDown) {
		this.hasRefresh = hasRefresh;
		this.hasDropDown = hasDropDown;
	}

	public void refreshFragment() {
		currentFragment = new LoadMore_Base();
	}

	public void setListView() {
		myLoadMoreListView = (LoadMoreListView) this.getListView();
		myLoadMoreListView.setDivider(null);

		vaa = new VideoArrayAdapter(sfa, titles, videolist, imageLoader);
		setListAdapter(vaa);

		// Why check internet here?
		// if (ic.checkConnection(sfa)) {
		if (isMoreVideos) {
			// there are more videos in the list
			// set the listener for loading need
			myLoadMoreListView.setOnLoadMoreListener(new OnLoadMoreListener() {
				public void onLoadMore() {
					// Do the work to load more items at the end of
					// list

					// checking network
					// if (ic.checkConnection(sfa)) {

					// network ok
					if (isMoreVideos == true) {
						// new LoadMoreTask().execute(API.get(0));
						LoadMoreTask newTask = (LoadMoreTask) new LoadMoreTask(LoadMoreTask.LOADMORETASK, myLoadMoreListView, fullscreenLoadingView, mRetryView);
						newTask.execute(API.get(0));
						mLoadMoreTasks.add(newTask);
					}

				}
			});

		} else {
			myLoadMoreListView.setOnLoadMoreListener(null);
		}

		// sending Initial Get Request to Youtube
		if (!API.isEmpty()) {
			// show loading screen
//			DisplayView(fullscreenLoadingView, myLoadMoreListView, mRetryView) ;
			doRequest();
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		setListView();

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		if (hasRefresh)
			menu.add(0, 0, 0, "Refresh")
					.setIcon(R.drawable.ic_refresh)
					.setShowAsAction(
							MenuItem.SHOW_AS_ACTION_IF_ROOM
									| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		if (hasDropDown) {
			SubMenu subMenu1 = menu.addSubMenu(0, 1, 0, "Action Item");
			subMenu1.add(0, 11, 0, "All(Default)");
			subMenu1.add(0, 12, 0, "Uploaders");
			subMenu1.add(0, 13, 0, "Playlists");

			MenuItem subMenu1Item = subMenu1.getItem();
			subMenu1Item.setTitle("All(Default)");
			subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
					| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		}
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {

		// do nothing if no network
			FragmentTransaction ft = getFragmentManager().beginTransaction();

			switch (item.getItemId()) {

			case 0:
				// Menu option 1
				Toast.makeText(sfa, "Refreshing", Toast.LENGTH_SHORT).show();
				refreshFragment();
				ft.replace(R.id.content_frame, currentFragment);
				break;

			case 11:
				// Menu option 1
				ft.replace(R.id.content_frame, FragmentAll);
				break;

			case 12:
				// Menu option 2
				ft.replace(R.id.content_frame, FragmentUploader);
				break;

			case 13:
				// Menu option 3
				ft.replace(R.id.content_frame, FragmentPlaylist);
				break;
			default:
				return super.onOptionsItemSelected(item);
			}
			ft.commit();

		return true;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		// check network first
		// if (ic.checkConnection(this.getSherlockActivity())) {
		// get selected items

		Toast.makeText(this.getSherlockActivity(), videos.get(position),
				Toast.LENGTH_SHORT).show();

		Intent i = new Intent(this.getSherlockActivity(), VideoPlayer.class);
		i.putExtra("video", videolist.get(position));
		startActivity(i);

	}

	class LoadMoreTask extends MyAsyncTask {
		
		public LoadMoreTask(int type, View contentView, View loadingView, View retryView){
			super(type, contentView, loadingView, retryView);
		}
		
		@Override
		public void handleCancelView(){
			((LoadMoreListView) myLoadMoreListView).onLoadMoreComplete();
			
			if (isException){
						
					DisplayView(retryView, contentView, loadingView) ;
			}

		}

		@Override
		protected void onPostExecute(String result) {
			// Do anything with response..
			// System.out.println(result);

			Log.d("AsyncDebug", "Into onPostExecute!");

			if (!taskCancel && result != null) {
				// Do anything with response..

				feedManager.setmJSON(result);

				List<Video> newVideos = feedManager.getVideoPlaylist();

				// adding new loaded videos to our current video list
				for (Video v : newVideos) {
					System.out.println("new id: " + v.getVideoId());
					if (needFilter) {
						filtering(v);
						// System.out.println("need filter!");
					} else {
						titles.add(v.getTitle());
						videos.add(v.getVideoId());
						videolist.add(v);
					}
				}
				try {
					// put the next API in the first place of the array
					API.set(0, feedManager.getNextApi());
					// nextAPI = feedManager.getNextApi();
					if (API.get(0) == null) {
						// No more videos left
						isMoreVideos = false;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				vaa.notifyDataSetChanged();

				// Call onLoadMoreComplete when the LoadMore task, has
				// finished
				((LoadMoreListView) myLoadMoreListView).onLoadMoreComplete();

				// loading done
				DisplayView(contentView, retryView, loadingView) ;
				if (!isMoreVideos) {
					((LoadMoreListView) myLoadMoreListView).onNoMoreItems();

					((LoadMoreListView)myLoadMoreListView).setOnLoadMoreListener(null);
				}

			} else {
				handleCancelView();
			}

		}

	}

	// sending the http request
	protected void doRequest() {
		// TODO Auto-generated method stub
		for (String s : API) {
			LoadMoreTask newTask = new LoadMoreTask(LoadMoreTask.INITTASK, myLoadMoreListView, fullscreenLoadingView, mRetryView);;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				newTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, s);
			} else {
				newTask.execute(s);
			}

			mLoadMoreTasks.add(newTask);
		}
	}

	public void Initializing() {

	}

//	public void handleCancelView(LoadMoreTask mTask,boolean isException) {
//		
//		((LoadMoreListView) myLoadMoreListView).onLoadMoreComplete();
//		
//		if (isException){
//					
//				DisplayView(mRetryView, myLoadMoreListView, fullscreenLoadingView) ;
//		}
//	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (sfa.isTaskRoot()) {
			Log.d("UniversalImageLoader", "It's task root!");
			imageLoader.clearDiscCache();
			imageLoader.clearMemoryCache();
		}
		// check the state of the task
		cancelAllTask();
		hideAllViews();
		
	}

	public void cancelAllTask() {

		for (LoadMoreTask mTask : mLoadMoreTasks)
			if (mTask != null && mTask.getStatus() == Status.RUNNING) {
				mTask.cancel(true);

				Log.d("AsyncDebug", "Task cancelled!!!!!!!!");
			} else
				Log.d("AsyncDebug", "Task cancellation failed!!!!");

	}

	protected void filtering(Video v) {
		// TODO Auto-generated method stub

	}

	public void networkHandler(Fragment mFragment) {

//		if (ic.isOnline(sfa)) {
			// fullscreenLoadingView.setVisibility(View.GONE);
			switch (ic.getNetworkError()) {
			case 1:
				// network lost during fullscreen loading
				Toast.makeText(sfa, "network lost during fullscreen loading",
						Toast.LENGTH_SHORT).show();

				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.content_frame, mFragment);
				ft.commit();

				break;
			case 2:
				// Internet lost during loading more
				// This is for loadMoreTask, Should not reload the fragment,
				// only hide the view of retry
				Toast.makeText(sfa, "Internet lost during loading more",
						Toast.LENGTH_SHORT).show();

				// Continue previous loading
				if (isMoreVideos == true) {
					// new LoadMoreTask().execute(API.get(0));
					LoadMoreTask newTask = (LoadMoreTask) new LoadMoreTask(LoadMoreTask.LOADMORETASK, myLoadMoreListView, fullscreenLoadingView, mRetryView);
					newTask.execute(API.get(0));
					mLoadMoreTasks.add(newTask);
				}
				break;

//			case 3:
//				// Internet lost during transition to video player
//				// Hide the retry view if it is online
//				Toast.makeText(sfa,
//						"Internet lost during transition to video player",
//						Toast.LENGTH_SHORT).show();
//
//				break;

			default:
				Toast.makeText(sfa, "Other unknown internet error!",
						Toast.LENGTH_SHORT).show();
				break;

			}
			// Clear the network error
			ic.setNetworkError(0);
//			DisplayView(fullscreenLoadingView, myLoadMoreListView, mRetryView) ;
//		}
	}

	// set a listener for "Retry" button
	public void setRetryButtonListener(final Fragment mFragment) {

		mRetryButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Continue to check network status
//				networkHandler(mFragment);
				LoadMoreTask newTask = (LoadMoreTask) new LoadMoreTask(LoadMoreTask.LOADMORETASK, myLoadMoreListView, fullscreenLoadingView, mRetryView);
				newTask.execute(API.get(0));
				mLoadMoreTasks.add(newTask);
//				DisplayView(fullscreenLoadingView, myLoadMoreListView, mRetryView) ;


			}
		});
	}

	// Clear fragment back stack
	public void clearFragmentStack() {
		fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
	
//	public void DisplayView(mRetryView, myLoadMoreListView, fullscreenLoadingView) {
//		if (mRetryView != null)
//			mRetryView.setVisibility(View.VISIBLE);
//		if (myLoadMoreListView != null)
//			myLoadMoreListView.setVisibility(View.GONE);
//		if (fullscreenLoadingView != null)
//			fullscreenLoadingView.setVisibility(View.GONE);
//	}
//	
//	public void DisplayView(myLoadMoreListView, , mRetryView, fullscreenLoadingView) {
//		if (myLoadMoreListView != null)
//			myLoadMoreListView.setVisibility(View.VISIBLE);
//		if (mRetryView != null)
//			mRetryView.setVisibility(View.GONE);
//		if (fullscreenLoadingView != null)
//			fullscreenLoadingView.setVisibility(View.GONE);
//	}
//	
//	public void DisplayView(fullscreenLoadingView, myLoadMoreListView, mRetryView) {
//		if (fullscreenLoadingView != null)
//			fullscreenLoadingView.setVisibility(View.VISIBLE);
//		if (myLoadMoreListView != null)
//			myLoadMoreListView.setVisibility(View.GONE);
//		if (mRetryView != null)
//			mRetryView.setVisibility(View.GONE);
//	}
	
	public void hideAllViews(){
		if (fullscreenLoadingView != null)
			fullscreenLoadingView.setVisibility(View.GONE);
		if (myLoadMoreListView != null)
			myLoadMoreListView.setVisibility(View.GONE);
		if (mRetryView != null)
			mRetryView.setVisibility(View.GONE);
	}
}
