package com.examples.gg;

import java.util.List;

import org.json.JSONException;

import android.view.View;

import com.costum.android.widget.LoadMoreListView;
import com.examples.gg.LoadMore_Base.LoadMoreTask;

public class LoadMore_H_Playlist extends LoadMore_Base_UP {
	@Override
	public void Initializing() {
		// Give a title for the action bar
		abTitle = "Highlights";

		// Set action bar title
		ab.setTitle(abTitle);

		// Give API URLs
		API.add("https://gdata.youtube.com/feeds/api/users/dotacinema/playlists?v=2&max-results=50&alt=json");
		API.add("https://gdata.youtube.com/feeds/api/users/noobfromua/playlists?v=2&max-results=50&alt=json");

		// initialize the fragments in the Menu
		FragmentAll = new LoadMore_H_Subscription();
		FragmentUploader = new LoadMore_H_Uploader();
		FragmentPlaylist = new LoadMore_H_Playlist();

		// set a feed manager
		feedManager = new FeedManager_Playlist();

		// Show menu component
		setHasOptionsMenu(true);

	}

	// this method is used in the method "onListItemClick" to pass API to the
	// next fragment
	@Override
	public void InitializingNextFragment() {

		mLoadMore = new LoadMore_H_L2(nextFragmentAPI);

	}

	@Override
	// sending the http request
	protected void doRequest() {
		// TODO Auto-generated method stub
		for (String s : API)
			new LoadMoreTask_Playlist().execute(s);
	}

	public class LoadMoreTask_Playlist extends LoadMoreTask {

		@Override
		protected void onPostExecute(String result) {
			// Do anything with response..
			// System.out.println(result);

			// ytf = switcher(ytf,result);
			if (!taskCancel || result == null) {
				feedManager.setmJSON(result);

				List<Video> newVideos = feedManager.getVideoPlaylist();

				// adding new loaded videos to our current video list
				for (Video v : newVideos) {
					System.out.println("new id: " + v.getVideoId());
					String theTitle = v.getTitle();

					// Filter out some unrelated videos from noobfromua
					if ((theTitle.toUpperCase().contains("DOTA") || theTitle
							.toUpperCase().contains("GAMEPLAY"))
							&& !theTitle.toUpperCase().contains("ASSASSIN")) {
						titles.add(v.getTitle());
						videos.add(v.getVideoId());
						videolist.add(v);
					}

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
			}
			// super.onPostExecute(result);

		}
	}
}
