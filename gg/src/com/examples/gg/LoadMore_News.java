package com.examples.gg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.AsyncTask.Status;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.examples.gg.R.color;

@SuppressLint("HandlerLeak")
public class LoadMore_News extends LoadMore_Base {

	private ImageView[] imageViews = null;
	private ImageView imageView = null;
	private ViewPager advPager = null;
	private AtomicInteger what = new AtomicInteger(0);
	private boolean isContinue = true;
	private ViewGroup group;
	private ArrayList<String> matches = new ArrayList();
	private ArrayList<String> results = new ArrayList();
	private Elements links;
	boolean isPagerSet = false;
	boolean matchRequestDone = false;
	boolean loadMoreRequestDone = false;
	private getMatchInfo mMatchInfo;

	@Override
	public void Initializing() {
		// Inflating view
		view = mInflater.inflate(R.layout.whatsnew, null);

		// Give a title for the action bar
		abTitle = "News";

		// Give API URLs
		API.add("https://gdata.youtube.com/feeds/api/users/cpGJHANGum7tFm0kg6fh7g/newsubscriptionvideos?max-results=10&alt=json");

		// initialize the fragments in the Menu
		// FragmentAll = new LoadMore_H_Subscription();
		// FragmentUploader = new LoadMore_H_Uploader();
		// FragmentPlaylist = new LoadMore_H_Playlist();

		// set a feed manager
		feedManager = new FeedManager_Subscription();

		// Show menu
		setHasOptionsMenu(true);
		setOptionMenu(true, false);

		setRetryButtonListener(new LoadMore_News());

	}

	// @Override
	// public void onActivityCreated(Bundle savedInstanceState) {
	// // TODO Auto-generated method stub
	// super.onActivityCreated(savedInstanceState);
	//
	// myLoadMoreListView = (LoadMoreListView) this.getListView();
	// myLoadMoreListView.setDivider(null);
	//
	// if (ic.isOnline(sfa)) {
	// if (isMoreVideos) {
	// // there are more videos in the list
	// // set the listener for loading need
	// myLoadMoreListView
	// .setOnLoadMoreListener(new OnLoadMoreListener() {
	// public void onLoadMore() {
	// // Do the work to load more items at the end of
	// // list
	// // hereru
	//
	// // checking network
	// if (ic.checkConnection(sfa)) {
	//
	// // network ok
	// if (isMoreVideos == true) {
	// new LoadMoreTask_News().execute(API
	// .get(0));
	// }
	// } else {
	// ic.networkToast(sfa);
	// ((LoadMoreListView) myLoadMoreListView)
	// .onLoadMoreComplete();
	// }
	//
	// }
	// });
	//
	// } else
	// myLoadMoreListView.setOnLoadMoreListener(null);
	//
	// } else {
	// ic.networkToast(sfa);
	// }
	//
	// // show loading screen
	// // sending Initial Get Request to Youtube
	// sfa.findViewById(R.id.fullscreen_loading_indicator).setVisibility(
	// View.VISIBLE);
	//
	// if (!API.isEmpty())
	// doRequest();
	//
	// }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		menu.add("Refresh")
				.setIcon(R.drawable.ic_refresh)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

	}

	@Override
	public void refreshFragment() {
		currentFragment = new LoadMore_News();
	}

	@SuppressWarnings("deprecation")
	private void initViewPager() {

		for (Element link : links) {

			String match;

			match = link.select("span").first().text().trim() + " vs "
					+ link.select("span").get(2).text().trim() + " ";
			if (link.getElementsByClass("results").isEmpty()) {
				match += link.select("td").get(3).text().trim();
				matches.add(match);
			} else {
				match += link.select("span.hidden").first().text().trim();
				results.add(match);
			}
		}

		advPager = (ViewPager) sfa.findViewById(R.id.adv_pager);
		group = (ViewGroup) sfa.findViewById(R.id.viewGroup);

		List<View> advPics = new ArrayList<View>();
		// FrameLayout flayout = new FrameLayout(sfa);
		// LinearLayout lLayout = new LinearLayout(sfa);
		// lLayout.setOrientation(LinearLayout.VERTICAL);
		// TextView title = new TextView(sfa);
		// title.setTextColor(Color.WHITE);
		// title.setTextSize(24);
		// title.setText("Today's matches");
		// LinearLayout.LayoutParams titleLayout = new
		// LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
		// LinearLayout.LayoutParams.WRAP_CONTENT);
		// titleLayout.setMargins(20, 10, 0, 0);
		// LinearLayout.LayoutParams matchLayout = new
		// LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
		// LinearLayout.LayoutParams.WRAP_CONTENT);
		// matchLayout.setMargins(20, 20, 0, 0);
		//
		// TextView matchtable = new TextView(sfa);
		// matchtable.setSingleLine(false);
		//
		// String Matchtext = "";
		String[] matcharray = matches.toArray(new String[matches.size()]);
		String[] resultarray = results.toArray(new String[results.size()]);

		// for (String s : matcharray){
		// System.out.println(s);
		// }
		//
		// for (String s : resultarray){
		// System.out.println(s);
		// }

		// matchtable.setTextColor(Color.WHITE);
		//
		// for (int i = 0; i < 3; i++) {
		// // if (matcharray[i].endsWith("m"))
		// // {
		// // final LinearLayout liveline = (LinearLayout)
		// sfa.findViewById(R.id.liveview);
		// // lLayout.addView(liveline,0);
		// // }
		// Matchtext = Matchtext + matcharray[i] + "\n";
		// }
		// matchtable.setText(Matchtext);
		// matchtable.setTextSize(18);
		// ImageView img1 = new ImageView(sfa);
		// img1.setBackgroundResource(R.drawable.bountyhunter);
		//
		// flayout.addView(img1, new FrameLayout.LayoutParams(
		// LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,
		// Gravity.CENTER));
		//
		// lLayout.addView(title, titleLayout);
		// lLayout.addView(matchtable, matchLayout);
		// flayout.addView(lLayout, new FrameLayout.LayoutParams(
		// LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		//
		// advPics.add(flayout);
		//
		// ImageView img2 = new ImageView(sfa);
		// img2.setBackgroundResource(R.drawable.centaurwarlord);
		// advPics.add(img2);
		//
		// ImageView img3 = new ImageView(sfa);
		// img3.setBackgroundResource(R.drawable.razor);
		// advPics.add(img3);
		//
		// ImageView img4 = new ImageView(sfa);
		// img4.setBackgroundResource(R.drawable.snk);
		// advPics.add(img4);

		View v1 = new View(sfa);
		View v2 = new View(sfa);
		final LayoutInflater inflater = (LayoutInflater) sfa
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		v1 = inflater.inflate(R.layout.livetext, null, false);
		v1.setBackgroundResource(R.drawable.bountyhunter);

		TextView liveTitle = (TextView) v1.findViewById(R.id.livetitle);
		TextView liveMatch1 = (TextView) v1.findViewById(R.id.lineup1);
		TextView liveMatch2 = (TextView) v1.findViewById(R.id.lineup2);
		TextView liveMatch3 = (TextView) v1.findViewById(R.id.lineup3);
		TextView live1 = (TextView) v1.findViewById(R.id.live1);
		TextView live2 = (TextView) v1.findViewById(R.id.live2);
		TextView live3 = (TextView) v1.findViewById(R.id.live3);

		liveTitle.setText("Upcoming Matches");
		if (matcharray[0].endsWith("Live")) {
			liveMatch1.setText(matcharray[0].substring(0,
					matcharray[0].length() - 4));
		} else {
			liveMatch1.setText(matcharray[0]);
			live1.setVisibility(View.GONE);
		}
		System.out.println(matcharray[0]);

		if (matcharray[1].endsWith("Live")) {
			liveMatch2.setText(matcharray[1].substring(0,
					matcharray[1].length() - 4));
		} else {
			liveMatch2.setText(matcharray[1]);
			live2.setVisibility(View.GONE);
		}
		System.out.println(matcharray[1]);

		if (matcharray[2].endsWith("Live")) {
			liveMatch3.setText(matcharray[2].substring(0,
					matcharray[2].length() - 4));
		} else {
			liveMatch3.setText(matcharray[2]);
			live3.setVisibility(View.GONE);
		}
		System.out.println(matcharray[2]);

		advPics.add(v1);

		v2 = inflater.inflate(R.layout.livetext, null, false);
		v2.setBackgroundResource(R.drawable.centaurwarlord);

		liveTitle = (TextView) v2.findViewById(R.id.livetitle);
		liveMatch1 = (TextView) v2.findViewById(R.id.lineup1);
		liveMatch2 = (TextView) v2.findViewById(R.id.lineup2);
		liveMatch3 = (TextView) v2.findViewById(R.id.lineup3);
		live1 = (TextView) v2.findViewById(R.id.live1);
		live2 = (TextView) v2.findViewById(R.id.live2);
		live3 = (TextView) v2.findViewById(R.id.live3);

		liveTitle.setText("Resent Result");

		liveMatch1.setText(resultarray[0]);

		live1.setVisibility(View.GONE);

		liveMatch2.setText(resultarray[1]);

		live2.setVisibility(View.GONE);

		liveMatch3.setText(resultarray[2]);

		live3.setVisibility(View.GONE);

		advPics.add(v2);

		imageViews = new ImageView[advPics.size()];
		for (int i = 0; i < advPics.size(); i++) {
			imageView = new ImageView(sfa);
			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageView.setPadding(5, 5, 5, 5);
			imageViews[i] = imageView;
			if (i == 0) {
				imageViews[i].setBackgroundResource(R.drawable.selected);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.unselected);
			}
			group.addView(imageViews[i]);
		}

		advPager.setAdapter(new AdvAdapter(advPics));
		advPager.setOnPageChangeListener(new GuidePageChangeListener());
		advPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					isContinue = false;
					break;
				case MotionEvent.ACTION_UP:
					isContinue = true;
					break;
				default:
					isContinue = true;
					break;
				}
				return false;
			}
		});
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (isContinue) {
						viewHandler.sendEmptyMessage(what.get());
						whatOption();
					}
				}
			}

		}).start();

		isPagerSet = true;

		fullscreenLoadingView.setVisibility(View.GONE);
	}

	private void whatOption() {
		what.incrementAndGet();
		if (what.get() > imageViews.length - 1) {
			what.getAndAdd(-4);
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {

		}
	}

	private final Handler viewHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			advPager.setCurrentItem(msg.what);
			super.handleMessage(msg);
		}

	};

	private final class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			what.getAndSet(arg0);
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0].setBackgroundResource(R.drawable.selected);
				if (arg0 != i) {
					imageViews[i].setBackgroundResource(R.drawable.unselected);
				}
			}

		}

	}

	private final class AdvAdapter extends PagerAdapter {
		private List<View> views = null;

		public AdvAdapter(List<View> views) {
			this.views = views;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public Object instantiateItem(View collection, int position) {

			((ViewPager) collection).addView(views.get(position), 0);
			return views.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

	}

	private class getMatchInfo extends LoadMoreTask {

		// @Override
		// protected Elements doInBackground(Void... params) {
		//
		// String url = "http://www.gosugamers.net/dota2/gosubet";
		// Document doc;
		// try {
		// doc = Jsoup
		// .connect(url)
		// .header("User-Agent",
		// "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
		// .get();
		//
		// links = doc.select("tr:has(td.opp)");
		//
		// // for (String match: matches){
		// // System.out.println(match);
		// // }
		//
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		//
		// return links;
		// }

		@Override
		protected void onPostExecute(String result) {

			// if (ic.checkConnection(sfa)) {
			//
			// matchRequestDone = true;
			//
			// if (loadMoreRequestDone && !isPagerSet) {
			// initViewPager();
			// Log.d("initViewpager", "pagerInitialized!");
			// } else {
			// Log.d("initViewpager",
			// "in getMatchInfo loadMoreRequestDone = "
			// + loadMoreRequestDone
			// + "and matchRequestDone = "
			// + matchRequestDone);
			// }
			//
			// } else {
			//
			// // No internet
			//
			// if (fullscreenLoadingView.getVisibility() == View.VISIBLE) {
			// // Internet lost during fullscree loading
			//
			// ic.setNetworkError(InternetConnection.fullscreenLoadingError);
			// } else {
			// // Internet lost during loading more
			// ic.setNetworkError(InternetConnection.loadingMoreError);
			// }
			// }

			Log.d("AsyncDebug", "Into onPostExecute!");

			if (!taskCancel && result != null) {
				// Do anything with response..
				Document doc = Jsoup.parse(result);
				links = doc.select("tr:has(td.opp)");

				matchRequestDone = true;

				if (loadMoreRequestDone && !isPagerSet) {
					initViewPager();
					Log.d("initViewpager", "pagerInitialized!");
				} else {
					Log.d("initViewpager",
							"in getMatchInfo loadMoreRequestDone = "
									+ loadMoreRequestDone
									+ "and matchRequestDone = "
									+ matchRequestDone);
				}

			} else {
				cancelSingleTask(this);
			}

		}

	}

	class LoadMoreTask_News extends LoadMoreTask {

		@Override
		protected void onPostExecute(String result) {
			// Do anything with response..
			// System.out.println(result);
			Log.d("AsyncDebug", "Into onPostExecute!");

			if (!taskCancel && result != null) {

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

				((LoadMoreListView) myLoadMoreListView).onLoadMoreComplete();

				if (!isMoreVideos) {
					((LoadMoreListView) myLoadMoreListView).onNoMoreItems();

					myLoadMoreListView.setOnLoadMoreListener(null);
				}

				loadMoreRequestDone = true;

				if (matchRequestDone && !isPagerSet) {
					initViewPager();
					Log.d("initViewpager", "pagerInitialized!");
				} else {
					Log.d("initViewpager", "in LoadMore loadMoreRequestDone = "
							+ loadMoreRequestDone + "and matchRequestDone = "
							+ matchRequestDone);

				}

			} else {
				cancelSingleTask(this);
			}
		}

	}

	@Override
	protected void doRequest() {
		// TODO Auto-generated method stub
		for (String s : API) {
			LoadMoreTask newTask = new LoadMoreTask_News();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				newTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, s);
			} else {
				newTask.execute(s);
			}
		}

		mMatchInfo = new getMatchInfo();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mMatchInfo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			String url = "http://www.gosugamers.net/dota2/gosubet";
			mMatchInfo.execute(url);
		}
	}

	@Override
	public void onDestroy() {

		super.onDestroy();

		if (mMatchInfo != null && mMatchInfo.getStatus() == Status.RUNNING)
			mMatchInfo.cancel(true);

	}

}
