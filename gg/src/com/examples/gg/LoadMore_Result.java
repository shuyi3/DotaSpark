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
import android.os.AsyncTask.Status;
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

public class LoadMore_Result extends LoadMore_Base {

	private Elements links;
	private ArrayList<Match> matchArray = new ArrayList<Match>();
	private MatchArrayAdapter mArrayAdatper;
	private getMatchInfo mgetMatchInfo;
	private int pageNum;
	private String baseUrl = "http://www.gosugamers.net";

	@Override
	public void refreshFragment() {
		currentFragment = new LoadMore_Result();
	}

	@Override
	public void Initializing() {
		// Inflating view

		// Give a title for the action bar
		abTitle = "Upcoming Matches";

		// Give API URLs
		API.add("http://www.gosugamers.net/dota2/gosubet");

		pageNum = 1;

		// Show menu
		setHasOptionsMenu(true);
		setOptionMenu(true, false);

		setRetryButtonListener(new LoadMore_Result());

	}

	@Override
	public void setListView() {

		myLoadMoreListView = (LoadMoreListView) this.getListView();
		myLoadMoreListView.setDivider(null);

		mArrayAdatper = new MatchArrayAdapter(sfa, matchArray, imageLoader, true);
		setListAdapter(mArrayAdatper);

			if (isMoreVideos) {
				// there are more videos in the list
				// set the listener for loading need
				myLoadMoreListView
						.setOnLoadMoreListener(new OnLoadMoreListener() {
							public void onLoadMore() {
								// Do the work to load more items at the end of
								// list
									// network ok
									if (isMoreVideos) {

										mgetMatchInfo = new getMatchInfo(getMatchInfo.INITTASK, myLoadMoreListView, fullscreenLoadingView, mRetryView);
										mgetMatchInfo.execute(API.get(0));
									}
									else {

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
				doRequest();
			}

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

			Toast.makeText(this.getSherlockActivity(),
					matchArray.get(position).getGosuLink(), Toast.LENGTH_SHORT)
					.show();

			Intent i = new Intent(this.getSherlockActivity(),
					MatchDetailsActivity.class);
			i.putExtra("match", matchArray.get(position));
			startActivity(i);

	}

	@Override
	protected void doRequest() {
		// TODO Auto-generated method stub

		System.out.println("DO!!!!!");
		for (String s : API) {
			mgetMatchInfo = new getMatchInfo(getMatchInfo.INITTASK, myLoadMoreListView, fullscreenLoadingView, mRetryView);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				mgetMatchInfo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
						s);
			} else {
				mgetMatchInfo.execute(s);
			}
		}
	}

	private class getMatchInfo extends LoadMoreTask {

		public getMatchInfo(int type, View contentView, View loadingView,
				View retryView) {
			super(type, contentView, loadingView, retryView);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPostExecute(String result) {

			Log.d("AsyncDebug", "Into onPostExecute!");

			if (!taskCancel && result != null) {

				Document doc = Jsoup.parse(result);
				
				Element box = doc.select("div.box").get(2);

				links = box.select("tr:has(td.opp)");

				Element paginator = box.select("div.paginator").first();
				
				if (paginator == null) {
					isMoreVideos = false;
				} else {
					if (paginator.select("a").last().hasAttr("class")) {
						isMoreVideos = false;
					} else {
						isMoreVideos = true;
						pageNum++;
						API.set(0,
								"http://www.gosugamers.net/dota2/gosubet?r-page="
										+ pageNum);
					}
				}

				// Setting layout

				for (Element link : links) {

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

						// if
						// (link.getElementsByClass("results").isEmpty()){
						newMatch.setScore(link.select("span.hidden").first().text().trim());
						// }else{
						// newMatch.setTime(link.select("span.hidden").first().text().trim());
						//
						// }

						newMatch.setGosuLink(baseUrl
								+ opp_1.select("a[href]").attr("href"));
						
						newMatch.setMatchStatus(newMatch.ENDED);

						matchArray.add(newMatch);

						// } else {
						// match +=
						// link.select("span.hidden").first().text().trim();
						// results.add(match);
						// }
					
				}

				for (Match m : matchArray) {
					System.out.println(m.getTeamName1());
				}

				mArrayAdatper.notifyDataSetChanged();

				// Call onLoadMoreComplete when the LoadMore task has
				// finished
				((LoadMoreListView) myLoadMoreListView).onLoadMoreComplete();

				// loading done
				DisplayView(contentView, retryView, loadingView) ;
				
				if (!isMoreVideos) {
					((LoadMoreListView) myLoadMoreListView).onNoMoreItems();

					myLoadMoreListView.setOnLoadMoreListener(null);
				}

			} else {

				handleCancelView();
			}

		}

	}

	@Override
	public void onDestroy() {

		super.onDestroy();

		if (mgetMatchInfo != null
				&& mgetMatchInfo.getStatus() == Status.RUNNING)
			mgetMatchInfo.cancel(true);

	}

}
