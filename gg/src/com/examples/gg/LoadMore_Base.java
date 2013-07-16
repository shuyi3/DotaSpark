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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class LoadMore_Base extends SherlockListFragment {
	private LoadMoreListView myLoadMoreListView;
	protected ArrayList<String> titles;
	private ArrayList<String> videos;
	protected ArrayList<Video> videolist;

	private boolean isMoreVideos;
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
	protected String nextAPI;
	protected boolean taskCancel = false;

	protected VideoArrayAdapter vaa;
	protected LoadMoreTask mLoadMoreTask = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// For check internet connection
		ic = new InternetConnection();

		// set the layout
		View view = inflater.inflate(R.layout.loadmore_list, null);

		// Get the current activity
		sfa = this.getSherlockActivity();

		// get action bar
		ab = sfa.getSupportActionBar();

		// Initilizing the empty arrays
		titles = new ArrayList<String>();
		videos = new ArrayList<String>();
		videolist = new ArrayList<Video>();
		// thumbList = new ArrayList<String>();

		// set adapter
		vaa = new VideoArrayAdapter(inflater.getContext(), titles, videolist);

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
		setListAdapter(vaa);

		return view;

	}

	public void setOptionMenu(boolean b) {
		setHasOptionsMenu(b);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		myLoadMoreListView = (LoadMoreListView) this.getListView();
		myLoadMoreListView.setDivider(null);

		if (ic.isOnline(sfa)) {
			if (isMoreVideos) {
				// there are more videos in the list
				// set the listener for loading need
				myLoadMoreListView
						.setOnLoadMoreListener(new OnLoadMoreListener() {
							public void onLoadMore() {
								// Do the work to load more items at the end of
								// list

								// checking network
								if (ic.isOnline(sfa)) {

									// network ok
									if (isMoreVideos == true) {
										mLoadMoreTask = (LoadMoreTask) new LoadMoreTask(
												"");
										mLoadMoreTask.execute(API.get(0));
									}
								} else {
									ic.networkToast(sfa);
									((LoadMoreListView) getListView())
											.onLoadMoreComplete();
								}

							}
						});

			} else
				myLoadMoreListView.setOnLoadMoreListener(null);

		} else {
			ic.networkToast(sfa);
		}

		// sending Initial Get Request to Youtube
		if (!API.isEmpty()) {
			// show loading screen
			sfa.findViewById(R.id.fullscreen_loading_indicator).setVisibility(
					View.VISIBLE);
			doRequest();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		SubMenu subMenu1 = menu.addSubMenu(0, 1, 0, "Action Item");
		subMenu1.add(0, 11, 0, "All(Default)");
		subMenu1.add(0, 12, 0, "Uploaders");
		subMenu1.add(0, 13, 0, "Playlists");

		MenuItem subMenu1Item = subMenu1.getItem();
		subMenu1Item.setTitle("Classify");
		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {

		if (ic.isOnline(sfa)) {

			FragmentTransaction ft = getFragmentManager().beginTransaction();

			// Putting the current fragment into stack for later call back
			// ft.addToBackStack(null);

			switch (item.getItemId()) {
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

		} else {
			ic.networkToast(this.getSherlockActivity());
		}

		return true;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		// check network first
		if (ic.isOnline(this.getSherlockActivity())) {
			// get selected items

			// String selectedValue = (String)
			// getListAdapter().getItem(position);

			Toast.makeText(this.getSherlockActivity(), videos.get(position),
					Toast.LENGTH_SHORT).show();

			Intent i = new Intent(this.getSherlockActivity(), VideoPlayer.class);
			i.putExtra("video", videolist.get(position));
			startActivity(i);
		} else {
			ic.networkToast(this.getSherlockActivity());
		}

	}

	class LoadMoreTask extends AsyncTask<String, String, String> {
		public LoadMoreTask(String s) {
		}

		@Override
		protected String doInBackground(String... uri) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				response = httpclient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				// TODO Handle problems..
			} catch (IOException e) {
				// TODO Handle problems..
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {

			if (!taskCancel || result == null) {
				// Do anything with response..
				// System.out.println(result);

				// ytf = switcher(ytf,result);

				feedManager.setmJSON(result);

				List<Video> newVideos = feedManager.getVideoPlaylist();

				// adding new loaded videos to our current video list
				
				for (Video v : newVideos) {
					System.out.println("new id: " + v.getVideoId());
					titles.add(v.getTitle());
					videos.add(v.getVideoId());
					videolist.add(v);
				}
				try {
					// put the next API in the first place of the array
					API.set(0, feedManager.getNextApi());
					if (API.get(0) == null) {
						// No more videos left
						isMoreVideos = false;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				vaa.notifyDataSetChanged();

				// Call onLoadMoreComplete when the LoadMore task, has finished
				((LoadMoreListView) getListView()).onLoadMoreComplete();

				// loading done
				sfa.findViewById(R.id.fullscreen_loading_indicator)
						.setVisibility(View.GONE);

				if (!isMoreVideos) {
					((LoadMoreListView) getListView()).onNoMoreItems();

					myLoadMoreListView.setOnLoadMoreListener(null);
				}

				super.onPostExecute(result);

			}

		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			if (!taskCancel)
			((LoadMoreListView) getListView()).onLoadMoreComplete();
		}

	}

	// sending the http request
	protected void doRequest() {
		// TODO Auto-generated method stub
		for (String s : API)
			new LoadMoreTask("").execute(s);
	}

	public void Initializing() {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// check the state of the task
		if (mLoadMoreTask != null
				&& mLoadMoreTask.getStatus() == Status.RUNNING)
			mLoadMoreTask.cancel(true);
			taskCancel = true;
			System.out.println("Task canceled!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}

}
