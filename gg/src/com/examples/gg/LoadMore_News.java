package com.examples.gg;

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
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
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
import com.costum.android.widget.LoadMoreListView;
import com.examples.gg.LoadMore_Base.LoadMoreTask;

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
	private getMatchInfo mMatchInfo;
	
	private View pagerContent;
	private View pagerLoading;
	private View pagerRetry;
	private View listLoading;
	private View listRetry;
	private String url = "http://www.gosugamers.net/dota2/gosubet";


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


	}

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
	
	@Override
	public void setListView() {
		
		pagerContent = sfa.findViewById(R.id.pageContent);
		pagerLoading = sfa.findViewById(R.id.pagerLoadingIndicator);
		pagerRetry = sfa.findViewById(R.id.pagerRetryView);
		listLoading = sfa.findViewById(R.id.listViewLoadingIndicator);
		listRetry = sfa.findViewById(R.id.ListViewRetryView);

		super.setListView();
		
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

		String[] matcharray = matches.toArray(new String[matches.size()]);
		String[] resultarray = results.toArray(new String[results.size()]);

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
		
		v1.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.content_frame, new LoadMore_UpcomingMatch());
				ft.commit();
            }
        });

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
		
		v2.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.content_frame, new LoadMore_Result());
				ft.commit();
            }
        });

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

	private class getMatchInfo extends MyAsyncTask {


		public getMatchInfo(int type, View contentView, View loadingView,
				View retryView) {
			super(type, contentView, loadingView, retryView);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void setRetryListener(final int type){
			mRetryButton = (Button) retryView.findViewById(R.id.mRetryButton);
			
			mRetryButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					mMatchInfo = (getMatchInfo) new getMatchInfo(type, contentView, loadingView, retryView);
					mMatchInfo.execute(url);
				}
			});
			
		}


		@Override
		protected void onPostExecute(String result) {

			Log.d("AsyncDebug", "Into onPostExecute!");

			if (!taskCancel && result != null) {
				// Do anything with response..
				Document doc = Jsoup.parse(result);
				links = doc.select("tr:has(td.opp)");

				initViewPager();
				
				DisplayView(contentView, retryView, loadingView) ;


			} else {
				handleCancelView();
			}

		}
		


	}

	class LoadMoreTask_News extends LoadMoreTask {

		public LoadMoreTask_News(int type, View contentView, View loadingView,
				View retryView) {
			super(type, contentView, loadingView, retryView);
			// TODO Auto-generated constructor stub
		}

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
	protected void doRequest() {
		// TODO Auto-generated method stub
		for (String s : API) {
			LoadMoreTask newTask = new LoadMoreTask_News(LoadMoreTask.INITTASK, myLoadMoreListView, listLoading, listRetry);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				newTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, s);
			} else {
				newTask.execute(s);
			}
			
			mLoadMoreTasks.add(newTask);

		}

		mMatchInfo = new getMatchInfo(getMatchInfo.INITTASK, pagerContent, pagerLoading, pagerRetry);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mMatchInfo.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
		} else {
			mMatchInfo.execute(url);
		}
	}
	
//	public void handleCancelView(LoadMoreTask mTask,boolean isException) {
//		
//		((LoadMoreListView) myLoadMoreListView).onLoadMoreComplete();
//		
//		if (isException){
//			
//				if (mTask.type == LoadMoreTask.INITTASK)		
//					ic.setNetworkError(InternetConnection.fullscreenLoadingError);
//		
//				if (mTask.type == LoadMoreTask.LOADMORETASK)		
//					ic.setNetworkError(InternetConnection.loadingMoreError);
//				
//				callRetryView();
//		}
//	}


	@Override
	public void onDestroy() {

		super.onDestroy();

		if (mMatchInfo != null && mMatchInfo.getStatus() == Status.RUNNING)
			mMatchInfo.cancel(true);

	}

}
