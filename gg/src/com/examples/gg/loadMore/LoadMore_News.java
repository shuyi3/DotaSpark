package com.examples.gg.loadMore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.examples.gg.R;
import com.examples.gg.SideMenuActivity;
import com.examples.gg.R.drawable;
import com.examples.gg.R.id;
import com.examples.gg.R.layout;
import com.examples.gg.adapters.VideoArrayAdapter;
import com.examples.gg.data.MyAsyncTask;
import com.examples.gg.feedManagers.FeedManager_Subscription;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import org.json.JSONException;
//import android.os.Message;

@SuppressLint("HandlerLeak")
public class LoadMore_News extends LoadMore_Base implements
		SearchView.OnQueryTextListener {

	private ImageView[] imageViews = null;
	private ImageView imageView = null;
	private ViewPager advPager = null;
	private AtomicInteger what = new AtomicInteger(0);
	private boolean isContinue = true;
	private boolean isEnd = false;
	private ViewGroup group;
	private ArrayList<String> matches = new ArrayList<String>();
	private ArrayList<String> results = new ArrayList<String>();
	private Elements links = new Elements();
	boolean isPagerSet = false;
	private getMatchInfo mMatchInfo;

	private View pagerContent;
	private View pagerLoading;
	private View pagerRetry;
	private View listLoading;
	private View listRetry;
	private String url = "http://www.gosugamers.net/dota2/gosubet";
	private int rand_1;
	private int rand_2;
	private AdvAdapter myAdvAdapter;

	private int position = 0;

	DisplayImageOptions options;

	private Random random;

	private SideMenuActivity sma;
	private final int[] myDrawables = new int[] { R.drawable.dota1,
			R.drawable.dota2, R.drawable.dota3, R.drawable.dota4,
			R.drawable.dota5, R.drawable.dota6, R.drawable.dota7,
			R.drawable.dota8, R.drawable.dota9, R.drawable.dota10,
			R.drawable.dota11, R.drawable.dota12, R.drawable.dota13,
			R.drawable.dota14, R.drawable.dota15, R.drawable.dota16,
			R.drawable.dota17, R.drawable.dota18, R.drawable.dota19,
			R.drawable.dota20, R.drawable.dota21, R.drawable.dota22,
			R.drawable.dota23, R.drawable.dota24, R.drawable.dota25,
			R.drawable.dota26, R.drawable.dota27, R.drawable.dota28,
			R.drawable.dota29, R.drawable.dota30, R.drawable.dota31,
			R.drawable.dota32, R.drawable.dota33, R.drawable.dota34,
			R.drawable.dota35, R.drawable.dota36, R.drawable.dota37,
			R.drawable.dota38, R.drawable.dota39, R.drawable.dota40,
			R.drawable.dota41, R.drawable.dota42, R.drawable.dota43,
			R.drawable.dota44, R.drawable.dota45, R.drawable.dota46,
			R.drawable.dota47, R.drawable.dota48, R.drawable.dota49,
			R.drawable.dota50, R.drawable.dota51, R.drawable.dota52,
			R.drawable.dota53, R.drawable.dota54, R.drawable.dota55,
			R.drawable.dota56, R.drawable.dota57, R.drawable.dota58,
			R.drawable.dota59, R.drawable.dota60, R.drawable.dota61,
			R.drawable.dota62, R.drawable.dota63, R.drawable.dota64,
			R.drawable.dota65, R.drawable.dota66, R.drawable.dota67,
			R.drawable.dota68, R.drawable.dota69, R.drawable.dota70,
			R.drawable.dota71, R.drawable.dota72, R.drawable.dota73,
			R.drawable.dota74, R.drawable.dota75, R.drawable.dota76,
			R.drawable.dota77, R.drawable.dota78, R.drawable.dota79,
			R.drawable.dota80, R.drawable.dota81, R.drawable.dota82,
			R.drawable.dota83, R.drawable.dota84, R.drawable.dota85,
			R.drawable.dota86, R.drawable.dota87, R.drawable.dota88,
			R.drawable.dota89, R.drawable.dota90, R.drawable.dota91,
			R.drawable.dota92, R.drawable.dota93, R.drawable.dota94,
			R.drawable.dota95, R.drawable.dota96, R.drawable.dota97,
			R.drawable.dota98, R.drawable.dota99, R.drawable.dota100,
			R.drawable.dota101, R.drawable.dota102, R.drawable.dota103 };

	private List<View> views = new ArrayList<View>();

	// private Thread myThread;

	@Override
	public void Initializing() {
		// Inflating view
		view = mInflater.inflate(R.layout.whatsnew, null);

		// Give a title for the action bar
		abTitle = "What's New";

		// Give API URLs
		API.add("https://gdata.youtube.com/feeds/api/users/cpGJHANGum7tFm0kg6fh7g/newsubscriptionvideos?max-results=10&alt=json");
//		API.add("http://youtube-rss.f-y.name/rss/55590a90-dcb3-11e3-a44f-0401096cca01/?alt=json");
		// set a feed manager
		feedManager = new FeedManager_Subscription();

		// Show menu
		setHasOptionsMenu(true);
		setOptionMenu(true, false);

		// Get sidemenuactivity
		sma = (SideMenuActivity) sfa;

		// if (!this.imageLoader.isInited()){
		// this.imageLoader.init(ImageLoaderConfiguration.createDefault(sfa));
		// }
		// // imageLoader=new ImageLoader(context.getApplicationContext());
		//
		// options = new DisplayImageOptions.Builder()
		// .showStubImage(R.drawable.loading)
		// .showImageForEmptyUri(R.drawable.loading)
		// .showImageOnFail(R.drawable.loading).cacheInMemory(true)
		// .cacheOnDisc(true)
		// // .displayer(new RoundedBitmapDisplayer(20))
		// .build();

		random = new Random();

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		SearchView searchView = new SearchView(sfa.getSupportActionBar()
				.getThemedContext());
		searchView.setQueryHint("Search Youtube");
		searchView.setOnQueryTextListener(this);

		menu.add(0, 20, 0, "Search")
				.setIcon(R.drawable.abs__ic_search)
				.setActionView(searchView)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

		menu.add(0, 0, 0, "Refresh")
				.setIcon(R.drawable.ic_refresh)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

	}

	@Override
	public void setListView() {

		pagerContent = sfa.findViewById(R.id.pageContent);
		pagerLoading = sfa.findViewById(R.id.pagerLoadingIndicator);
		pagerRetry = sfa.findViewById(R.id.pagerRetryView);
		listLoading = sfa.findViewById(R.id.listViewLoadingIndicator);
		listRetry = sfa.findViewById(R.id.ListViewRetryView);

		myLoadMoreListView = (LoadMoreListView) this.getListView();
		myLoadMoreListView.setDivider(null);

		// setBannerInHeader();

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

					if (isMoreVideos == true) {
						// new LoadMoreTask().execute(API.get(0));
						LoadMoreTask newTask = (LoadMoreTask) new LoadMoreTask(
								LoadMoreTask.LOADMORETASK, myLoadMoreListView,
								listLoading, listRetry);
						newTask.execute(API.get(API.size() - 1));
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
			// DisplayView(fullscreenLoadingView, myLoadMoreListView,
			// mRetryView) ;
			doRequest();
		}

	}

	private void setViewPager() {

		String[] matcharray = matches.toArray(new String[matches.size()]);
		String[] resultarray = results.toArray(new String[results.size()]);

		View v1 = new View(sfa);
		View v2 = new View(sfa);
		final LayoutInflater inflater = (LayoutInflater) sfa
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		v1 = inflater.inflate(R.layout.livetext, null, false);
		v1.setBackgroundResource(myDrawables[rand_1]);

		TextView liveTitle = (TextView) v1.findViewById(R.id.livetitle);
		TextView liveMatch1 = (TextView) v1.findViewById(R.id.lineup1);
		TextView liveMatch2 = (TextView) v1.findViewById(R.id.lineup2);
		TextView liveMatch3 = (TextView) v1.findViewById(R.id.lineup3);
		TextView live1 = (TextView) v1.findViewById(R.id.live1);
		TextView live2 = (TextView) v1.findViewById(R.id.live2);
		TextView live3 = (TextView) v1.findViewById(R.id.live3);

		liveTitle.setText("Upcoming Matches");

		if (matcharray.length >= 1) {
			if (matcharray[0].endsWith("Live")) {
				liveMatch1.setText(matcharray[0].substring(0,
						matcharray[0].length() - 4));
			} else {
				liveMatch1.setText(matcharray[0]);
				live1.setVisibility(View.GONE);
			}
		} else {
			liveMatch1.setVisibility(View.GONE);
			live1.setVisibility(View.GONE);
		}
		// System.out.println(matcharray[0]);

		if (matcharray.length >= 2) {
			if (matcharray[1].endsWith("Live")) {
				liveMatch2.setText(matcharray[1].substring(0,
						matcharray[1].length() - 4));
			} else {
				liveMatch2.setText(matcharray[1]);
				live2.setVisibility(View.GONE);
			}
		} else {
			liveMatch2.setVisibility(View.GONE);
			live2.setVisibility(View.GONE);
		}

		if (matcharray.length >= 3) {
			if (matcharray[2].endsWith("Live")) {
				liveMatch3.setText(matcharray[2].substring(0,
						matcharray[2].length() - 4));
			} else {
				liveMatch3.setText(matcharray[2]);
				live3.setVisibility(View.GONE);
			}
		} else {
			liveMatch3.setVisibility(View.GONE);
			live3.setVisibility(View.GONE);
		}

		v1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Set the drawer indicator in position "Upcoming Matches"
				sma.setDrawerIndicator(9);

				// Replacing the current fragment
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.content_frame, new LoadMore_UpcomingMatch());
				ft.commit();
			}
		});

		views.add(v1);

		v2 = inflater.inflate(R.layout.livetext, null, false);
		v2.setBackgroundResource(myDrawables[rand_2]);

		liveTitle = (TextView) v2.findViewById(R.id.livetitle);
		liveMatch1 = (TextView) v2.findViewById(R.id.lineup1);
		liveMatch2 = (TextView) v2.findViewById(R.id.lineup2);
		liveMatch3 = (TextView) v2.findViewById(R.id.lineup3);
		live1 = (TextView) v2.findViewById(R.id.live1);
		live2 = (TextView) v2.findViewById(R.id.live2);
		live3 = (TextView) v2.findViewById(R.id.live3);

		liveTitle.setText("Recent Results");

		if (resultarray.length >= 1) {
			liveMatch1.setText(resultarray[0]);
		} else {
			liveMatch1.setVisibility(View.GONE);
		}
		live1.setVisibility(View.GONE);

		if (resultarray.length >= 2) {
			liveMatch2.setText(resultarray[1]);
		} else {
			liveMatch2.setVisibility(View.GONE);
		}
		live2.setVisibility(View.GONE);

		if (resultarray.length >= 3) {
			liveMatch3.setText(resultarray[2]);
		} else {
			liveMatch3.setVisibility(View.GONE);
		}
		live3.setVisibility(View.GONE);

		v2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// Set the drawer indicator in position "Recent Result"
				sma.setDrawerIndicator(10);

				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.content_frame, new LoadMore_Result());
				ft.commit();
			}
		});

		views.add(v2);

	}

	private void initViewPager() {

		rand_1 = random.nextInt(myDrawables.length - 1);
		// System.out.println("New random:"+ rand_1);
		do {
			rand_2 = random.nextInt(myDrawables.length - 1);
		} while (rand_1 == rand_2);

		if (!isPagerSet) {
			advPager = (ViewPager) sfa.findViewById(R.id.adv_pager);
			group = (ViewGroup) sfa.findViewById(R.id.viewGroup);
		} else {
			views.clear();
		}

		setViewPager();

		if (!isPagerSet) {

			imageViews = new ImageView[views.size()];
			for (int i = 0; i < views.size(); i++) {
				imageView = new ImageView(sfa);
				imageView.setLayoutParams(new LayoutParams(20, 20));
				imageView.setPadding(5, 5, 5, 5);
				imageViews[i] = imageView;
				if (i == 0) {
					imageViews[i].setBackgroundResource(R.drawable.d2_selected);
				} else {
					imageViews[i]
							.setBackgroundResource(R.drawable.d2_unselected);
				}
				group.addView(imageViews[i]);
			}

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

			handler.postDelayed(runnable, 10000);

			// myThread = new Thread(new Runnable() {
			//
			// @Override
			// public void run() {
			// while (true) {
			// if (myThread.isInterrupted()) break;
			// if (isContinue) {
			// viewHandler.sendEmptyMessage(what.get());
			// whatOption();
			// }
			// }
			// }
			// });
			//
			// myThread.start();

			isPagerSet = true;
		}

		myAdvAdapter = new AdvAdapter();

		advPager.setAdapter(myAdvAdapter);

		advPager.setOnPageChangeListener(new GuidePageChangeListener());

		advPager.setCurrentItem(0);

		imageViews[0].setBackgroundResource(R.drawable.d2_selected);
		imageViews[1].setBackgroundResource(R.drawable.d2_unselected);

	}

	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
		public void run() {
			if (position == 0) {
				position = 1;
			} else {
				position = 0;
			}
			advPager.setCurrentItem(position, true);
			// refreshFragment();
			handler.postDelayed(runnable, 10000);
		}
	};

	// private void whatOption() {
	// what.incrementAndGet();
	// if (what.get() > imageViews.length - 1) {
	// what.getAndAdd(-4);
	// }
	// try {
	// Thread.sleep(5000);
	// } catch (InterruptedException e) {
	//
	// }
	// }
	//
	// private final Handler viewHandler = new Handler() {
	//
	// @Override
	// public void handleMessage(Message msg) {
	// advPager.setCurrentItem(msg.what);
	// super.handleMessage(msg);
	// }
	//
	// };

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
				imageViews[arg0].setBackgroundResource(R.drawable.d2_selected);
				if (arg0 != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.d2_unselected);
				}
			}

		}

	}

	private final class AdvAdapter extends PagerAdapter {
		// private List<View> views = null;

		public AdvAdapter() {
			// this.views = views;
		}

		@Override
		public void destroyItem(ViewGroup collection, int position, Object view) {
			// ((ViewPager) arg0).removeView(views.get(arg1));
			collection.removeView((View) view);
			view = null;
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public Object instantiateItem(ViewGroup collection, int position) {

			collection.addView(views.get(position), 0);
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

	private class getMatchInfo extends MyAsyncTask {

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

					mMatchInfo = (getMatchInfo) new getMatchInfo(type,
							contentView, loadingView, retryView);
					mMatchInfo.execute(url);
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
			return responseString;
		}

		private void pull(String responseString) {
			Document doc = Jsoup.parse(responseString);
			links = doc.select("tr:has(span.opp)");
			if (!links.isEmpty()) {

				for (Element link : links) {

					String match;

					match = link.select("span.opp").first().text().trim()
							+ " vs "
							+ link.select("span.opp").get(1).text().trim()
							+ " ";
					if (link.select("span.hidden").isEmpty()) {
						if (link.select("td").get(1).text().trim()
								.toLowerCase().matches("live")
								|| link.select("td").get(1).text().trim()
										.matches("")) {
							// Game is live, append "live" text to it
							match += "Live";
						} else {
							// Game is not live
							match += link.select("td").get(1).text().trim();
						}

						matches.add(match);
					} else {
						match += link.select("span.hidden").first().text()
								.trim();
						results.add(match);
					}
				}

			} else {
				handleCancelView();
			}
		}

		@Override
		protected void onPostExecute(String result) {

			// Log.d("AsyncDebug", "Into onPostExecute!");

			if (!taskCancel && result != null) {
				// Do anything with response..
				try {
					initViewPager();
				} catch (Exception e) {

				}
				DisplayView(contentView, retryView, loadingView);

			} else {
				handleCancelView();
			}

		}

	}

	// class LoadMoreTask_News extends LoadMoreTask {
	//
	// public LoadMoreTask_News(int type, View contentView, View loadingView,
	// View retryView) {
	// super(type, contentView, loadingView, retryView);
	// // TODO Auto-generated constructor stub
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// // Do anything with response..
	// // System.out.println(result);
	// //Log.d("AsyncDebug", "Into onPostExecute!");
	//
	// if (!taskCancel && result != null) {
	//
	// feedManager.setmJSON(result);
	//
	// List<Video> newVideos = feedManager.getVideoPlaylist();
	//
	// // adding new loaded videos to our current video list
	// for (Video v : newVideos) {
	// //System.out.println("new id: " + v.getVideoId());
	// if (needFilter) {
	// filtering(v);
	// // System.out.println("need filter!");
	// } else {
	// titles.add(v.getTitle());
	// videolist.add(v);
	// }
	// }
	// try {
	// // put the next API in the first place of the array
	// API.add(feedManager.getNextApi());
	// // nextAPI = feedManager.getNextApi();
	// if (API.get(API.size()-1) == null) {
	// // No more videos left
	// isMoreVideos = false;
	// }
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// //e.printStackTrace();
	// }
	// vaa.notifyDataSetChanged();
	//
	// ((LoadMoreListView) myLoadMoreListView).onLoadMoreComplete();
	//
	// DisplayView(contentView, retryView, loadingView);
	//
	// if (!isMoreVideos) {
	// ((LoadMoreListView) myLoadMoreListView).onNoMoreItems();
	//
	// myLoadMoreListView.setOnLoadMoreListener(null);
	// }
	//
	// } else {
	// handleCancelView();
	// }
	// }
	//
	// }

	@Override
	protected void doRequest() {
		// TODO Auto-generated method stub

		mMatchInfo = new getMatchInfo(getMatchInfo.INITTASK, pagerContent,
				pagerLoading, pagerRetry);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mMatchInfo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
		} else {
			mMatchInfo.execute(url);
		}

		for (String s : API) {
			LoadMoreTask newTask = new LoadMoreTask(LoadMoreTask.INITTASK,
					myLoadMoreListView, listLoading, listRetry);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				newTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, s);
			} else {
				newTask.execute(s);
			}

			mLoadMoreTasks.add(newTask);

		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDestroy() {

		super.onDestroy();

		views.clear();
		// advPager.removeAllViews();
		// group.removeAllViews();
		// advPager = null;
		isEnd = true;
		// group = null;
		// myThread.interrupt();

		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
		}

		if (mMatchInfo != null && mMatchInfo.getStatus() == Status.RUNNING)
			mMatchInfo.cancel(true);

	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// Toast.makeText(sfa, "You searched for: " + query, Toast.LENGTH_LONG)
		// .show();

		// starting search activity
		Intent intent = new Intent(sfa, LoadMore_Activity_Search_Youtube.class);
		intent.putExtra("query", query);
		startActivity(intent);
		return true;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}

}
