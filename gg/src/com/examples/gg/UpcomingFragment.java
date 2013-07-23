package com.examples.gg;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.examples.gg.LoadMore_Base.LoadMoreTask;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UpcomingFragment extends LoadMore_Base {

	private Elements links;
	private ArrayList<Match> matchArray = new ArrayList<Match>();
	private MatchArrayAdapter mArrayAdatper;

	@Override
	public void refreshFragment() {
		currentFragment = new UpcomingFragment();
	}

	@Override
	public void Initializing() {
		// Inflating view

		// Give a title for the action bar
		abTitle = "Upcoming Matches";

		// Give API URLs
		API.add("http://www.gosugamers.net/dota2/gosubet");

		// initialize the fragments in the Menu
		// FragmentAll = new LoadMore_H_Subscription();
		// FragmentUploader = new LoadMore_H_Uploader();
		// FragmentPlaylist = new LoadMore_H_Playlist();

		// set a feed manager
		// feedManager = new FeedManager_Subscription();

		// Show menu
		setHasOptionsMenu(true);
		setOptionMenu(true, false);

		setRetryButtonListener(new UpcomingFragment());

	}

	@Override
	public void setListView() {

		myLoadMoreListView = (LoadMoreListView) this.getListView();
		myLoadMoreListView.setDivider(null);

		mArrayAdatper = new MatchArrayAdapter(sfa, matchArray, imageLoader);
		setListAdapter(mArrayAdatper);

		if (ic.checkConnection(sfa)) {
			if (isMoreVideos) {
				// there are more videos in the list
				// set the listener for loading need
				myLoadMoreListView
						.setOnLoadMoreListener(new OnLoadMoreListener() {
							public void onLoadMore() {
								// Do the work to load more items at the end of
								// list

								// checking network
								if (ic.checkConnection(sfa)) {

									// network ok
									if (isMoreVideos == true) {
										// new
										// LoadMoreTask().execute(API.get(0));
										// mLoadMoreTask = (LoadMoreTask) new
										// LoadMoreTask();
										// mLoadMoreTask.execute(API.get(0));
									}
								} else {

									// ic.networkToast(sfa);

									((LoadMoreListView) myLoadMoreListView)
											.onLoadMoreComplete();
								}

							}
						});

			} else {
				myLoadMoreListView.setOnLoadMoreListener(null);
			}

			// sending Initial Get Request to Youtube
			if (!API.isEmpty()) {
				// show loading screen
				fullscreenLoadingView.setVisibility(View.VISIBLE);
				doRequest();
			}

		} else {
			ic.setNetworkError(InternetConnection.fullscreenLoadingError);
		}

	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		// check network first
		if (ic.checkConnection(this.getSherlockActivity())) {
			// get selected items

			// String selectedValue = (String)
			// getListAdapter().getItem(position);

			Toast.makeText(this.getSherlockActivity(), matchArray.get(position).getGosuLink(),
					Toast.LENGTH_SHORT).show();

			Intent i = new Intent(this.getSherlockActivity(), MatchDetailsActivity.class);
			i.putExtra("link",  matchArray.get(position).getGosuLink());
			startActivity(i);
		} else {
			ic.setNetworkError(InternetConnection.transitionToVideoPlayerError);
		}

	}


	@Override
	protected void doRequest() {
		// TODO Auto-generated method stub

		System.out.println("DO!!!!!");
		for (String s : API) {
			getMatchInfo mLoadMore = new getMatchInfo();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				mLoadMore.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, s);
			} else {
				mLoadMore.execute(s);
			}
		}
	}

	private class getMatchInfo extends AsyncTask<String, Void, Elements> {

		@Override
		protected Elements doInBackground(String... params) {

			String url = params[0];
			Document doc;
			try {
				doc = Jsoup
						.connect(url)
						.header("User-Agent",
								"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
						.get();

				links = doc.select("tr:has(td.opp)");

				// for (String match: matches){
				// System.out.println(match);
				// }

			} catch (IOException e) {
				e.printStackTrace();
			}

			return links;
		}

		@Override
		protected void onPostExecute(Elements links) {

			if (ic.checkConnection(sfa)) {

				// Setting layout

				String baseUrl = "http://www.gosugamers.net";

				for (Element link : links) {

					if (link.getElementsByClass("results").isEmpty()) {

						Match newMatch = new Match();
						Element opp_1 = link.select("td.opp").first();
						Element opp_2 = link.select("td.opp").get(1);

						newMatch.setTeamName1(opp_1.select("span").first()
								.text().trim());
						newMatch.setTeamName2(opp_2.select("span").first()
								.text().trim());

						newMatch.setTeamIcon1(baseUrl
								+ opp_1.select("img").attr("src"));
						newMatch.setTeamIcon2(baseUrl
								+ opp_2.select("img").attr("src"));

						// if (link.getElementsByClass("results").isEmpty()){
						newMatch.setTime(link.select("td").get(3).text().trim());
						// }else{
						// newMatch.setTime(link.select("span.hidden").first().text().trim());
						//
						// }

						newMatch.setGosuLink(baseUrl
								+ opp_1.select("a[href]").attr("href"));

						matchArray.add(newMatch);

						// } else {
						// match +=
						// link.select("span.hidden").first().text().trim();
						// results.add(match);
						// }
					}

				}

				for (Match m : matchArray) {
					System.out.println(m.getTeamName1());
				}

				mArrayAdatper.notifyDataSetChanged();

				fullscreenLoadingView.setVisibility(View.GONE);

			} else {

				// No internet

				if (fullscreenLoadingView.getVisibility() == View.VISIBLE) {
					// Internet lost during fullscree loading

					ic.setNetworkError(InternetConnection.fullscreenLoadingError);
				} else {
					// Internet lost during loading more
					ic.setNetworkError(InternetConnection.loadingMoreError);
				}
			}

		}

	}

}
