package com.examples.gg;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;

public class LoadMore_JD_News_Image extends LoadMore_Base {
	private ArrayList<News> mNews = new ArrayList<News>();

	private NewsArrayAdapter_Official mArrayAdatper;
	private getMatchInfo mgetMatchInfo;
	private int pageNum;
	private final String baseUri = "http://beta.na.leagueoflegends.com";

	@Override
	public void Initializing() {
		// Inflating view

		// Give a title for the action bar
		abTitle = "Latest News";

		// Give API URLs
		API.add("http://www.joindota.com/en/start");

		pageNum = 0;

		// Show menu
		setHasOptionsMenu(true);
		setOptionMenu(true, true);

		currentPosition = 0;

	}
	
	@Override
	protected void forceNoMore() {
		isMoreVideos = false;
	}
	
	@Override
	public void setDropdown() {
		if (hasDropDown) {

			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

			final String[] catagory = { "JD", "GG" };

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					mActionBar.getThemedContext(),
					R.layout.sherlock_spinner_item, android.R.id.text1,
					catagory);

			adapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

			mActionBar.setListNavigationCallbacks(adapter, this);

			mActionBar.setSelectedNavigationItem(currentPosition);

		} else {
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		}
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {

		if (firstTime) {
			firstTime = false;
			return true;
		}

		FragmentTransaction ft = getFragmentManager().beginTransaction();

		switch (itemPosition) {

		case 0:
			// Menu option 1
			ft.replace(R.id.content_frame, new LoadMore_JD_News_Image());
			break;

		case 1:
			// Menu option 2
			ft.replace(R.id.content_frame, new LoadMore_Gosu_News());
			break;

		}

		ft.commit();

		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void refreshFragment() {
		String firstApi = API.get(0);
		API.clear();
		API.add(firstApi);
		isMoreVideos = false;
		pageNum = 0;
		titles.clear();
		mNews.clear();
		setListView();
	}

	@Override
	public void setListView() {

		myLoadMoreListView = (LoadMoreListView) this.getListView();
		// myLoadMoreListView.setDivider(null);

		setBannerInHeader();

		mArrayAdatper = new NewsArrayAdapter_Official(sfa, titles, mNews,
				imageLoader);
		setListAdapter(mArrayAdatper);

		if (isMoreVideos) {
			// there are more videos in the list
			// set the listener for loading need
			myLoadMoreListView.setOnLoadMoreListener(new OnLoadMoreListener() {
				public void onLoadMore() {
					// Do the work to load more items at the end of
					// list
					// network ok
					if (isMoreVideos) {

						mgetMatchInfo = new getMatchInfo(
								getMatchInfo.LOADMORETASK, myLoadMoreListView,
								fullscreenLoadingView, mRetryView);
						mgetMatchInfo.execute(API.get(API.size() - 1));
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
			doRequest();
		}

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		String url = mNews.get(position - 1).getLink();
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
		// startActivity(Intent.createChooser(i, "Choose a browser"));

	}

	@Override
	protected void doRequest() {
		// TODO Auto-generated method stub

		// System.out.println("DO!!!!!");
		for (String s : API) {
			mgetMatchInfo = new getMatchInfo(getMatchInfo.INITTASK,
					myLoadMoreListView, fullscreenLoadingView, mRetryView);
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
		public void setRetryListener(final int type) {
			mRetryButton = (Button) retryView.findViewById(R.id.mRetryButton);

			mRetryButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					mgetMatchInfo = (getMatchInfo) new getMatchInfo(type,
							contentView, loadingView, retryView);
					mgetMatchInfo.DisplayView(loadingView, contentView,
							retryView);
					mgetMatchInfo.execute(API.get(API.size() - 1));

				}
			});

		}

		@Override
		public String doInBackground(String... uri) {

			super.doInBackground(uri[0]);

			if (!taskCancel && responseString != null) {
				try {
					pull(responseString);
				} catch (Exception e) {

				}
			}
			// pullNews();
			return responseString;
		}

		private void pull(String responseString) {
			Document doc = Jsoup.parse(responseString);
			// get all links
			Elements links = new Elements();
			links = doc.select("div.news_item");

			if (!links.isEmpty()) {
				String imageUri = "";
				String newsUri = "";
				String newsTitle = "";
				String newsSubtitle = "";
				String date = "";
				System.out.println("Number of News: " + links.size());

				for (Element link : links) {
					imageUri = link.select("img").first().attr("src");
					newsUri = link.select("h2.news_title_new").first()
							.select("a").first().attr("href");
					newsTitle = link.select("h2.news_title_new").first()
							.select("a").first().text();
					newsSubtitle = link.select("div.news_teaser_text.image")
							.first().text();
					date = link.select("span.maketip").first().text();
					News aNews = new News();
					aNews.setImageUri(imageUri);
					aNews.setLink(newsUri);
					aNews.setTitle(newsTitle);
					aNews.setSubTitle(newsSubtitle);
					aNews.setDate(date);
					
					titles.add(newsTitle);
					mNews.add(aNews);
				}
			}
		}

		@Override
		protected void onPostExecute(String result) {

			// Log.d("AsyncDebug", "Into onPostExecute!");

			if (!taskCancel && result != null) {

				mArrayAdatper.notifyDataSetChanged();

				// Call onLoadMoreComplete when the LoadMore task has finished
				((LoadMoreListView) myLoadMoreListView).onLoadMoreComplete();

				// loading done
				DisplayView(contentView, retryView, loadingView);

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
