package com.examples.gg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.examples.gg.LoadMore_Base.LoadMoreTask;

public class Whatsnew_Fragment extends SherlockListFragment {

	private LoadMoreListView myLoadMoreListView;
	private ArrayList<String> titles;
	private ArrayList<String> videos;
	private ArrayList<Video> videolist;

	private boolean isMoreVideos;
	protected InternetConnection ic;
	protected SherlockFragmentActivity sfa;
	protected ActionBar ab;
	protected String abTitle;
	protected FeedManager_Base feedManager;
	protected Fragment nextFragment;
	private Fragment FragmentAll;
	private Fragment FragmentUploader;
	private Fragment FragmentPlaylist;
	private ArrayList<String> API;

	protected VideoArrayAdapter vaa;

	private ImageView[] imageViews = null;
	private ImageView imageView = null;
	private TextView textView = null;
	private ViewPager advPager = null;
	private AtomicInteger what = new AtomicInteger(0);
	private boolean isContinue = true;
	private ViewGroup group;
	private ArrayList<String> matches = new ArrayList();
	private Elements links;
	boolean isPagerSet = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// For check internet connection
		ic = new InternetConnection();

		// set the layout
		View view = inflater.inflate(R.layout.whatsnew, null);

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

		// check whether there are more videos in the playlist
		if (API.isEmpty())
			isMoreVideos = false;
		else if (API.get(0) != null)
			isMoreVideos = true;

		// set the adapter
		setListAdapter(vaa);

		// initViewPager();

		return view;

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
								// hereru

								// checking network
								if (ic.isOnline(sfa)) {

									// network ok
									if (isMoreVideos == true) {
										new LoadMoreTask("").execute(API.get(0));
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

		// show loading screen
		// sending Initial Get Request to Youtube
		sfa.findViewById(R.id.fullscreen_loading_indicator).setVisibility(
				View.VISIBLE);

		if (!API.isEmpty())
			doRequest();
	}

	//
	@SuppressWarnings("deprecation")
	private void initViewPager() {
		advPager = (ViewPager) sfa.findViewById(R.id.adv_pager);
		group = (ViewGroup) sfa.findViewById(R.id.viewGroup);

		List<View> advPics = new ArrayList<View>();
		FrameLayout flayout = new FrameLayout(sfa);
		TextView text = new TextView(sfa);
		text.setSingleLine(false);

		String Matchtext = "Today's match\n";
		String[] matcharray = matches.toArray(new String[matches.size()]);

		for (int i = 0; i < 3; i++) {
			Matchtext = Matchtext + matcharray[i] + "\n";
		}
		text.setText(Matchtext);
		text.setTextColor(Color.WHITE);
		ImageView img1 = new ImageView(sfa);
		img1.setBackgroundResource(R.drawable.bountyhunter);

		flayout.addView(img1, new FrameLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,
				Gravity.CENTER));
		flayout.addView(text, new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				Gravity.CENTER));
		advPics.add(flayout);

		ImageView img2 = new ImageView(sfa);
		img2.setBackgroundResource(R.drawable.centaurwarlord);
		advPics.add(img2);

		ImageView img3 = new ImageView(sfa);
		img3.setBackgroundResource(R.drawable.razor);
		advPics.add(img3);

		ImageView img4 = new ImageView(sfa);
		img4.setBackgroundResource(R.drawable.snk);
		advPics.add(img4);

		// 瀵筰mageviews杩涜濉厖
		imageViews = new ImageView[advPics.size()];
		// 灏忓浘鏍�
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

	protected void doRequest() {
		// TODO Auto-generated method stub
		for (String s : API)
			new LoadMoreTask("").execute(s);
	}

	public ArrayList<String> getTitles() {
		return titles;
	}

	public void setTitles(ArrayList<String> titles) {
		this.titles = titles;
	}

	public ArrayList<String> getVideos() {
		return videos;
	}

	public void setVideos(ArrayList<String> videos) {
		this.videos = videos;
	}

	public ArrayList<Video> getVideolist() {
		return videolist;
	}

	public void setVideolist(ArrayList<Video> videolist) {
		this.videolist = videolist;
	}

	public String getAbTitle() {
		return abTitle;
	}

	public void setAbTitle(String abTitle) {
		this.abTitle = abTitle;
	}

	public FeedManager_Base getFeedManager() {
		return feedManager;
	}

	public void setFeedManager(FeedManager_Base feedManager) {
		this.feedManager = feedManager;
	}

	public Fragment getNextFragment() {
		return nextFragment;
	}

	public void setNextFragment(Fragment nextFragment) {
		this.nextFragment = nextFragment;
	}

	public VideoArrayAdapter getVaa() {
		return vaa;
	}

	public void setVaa(VideoArrayAdapter vaa) {
		this.vaa = vaa;
	}

	public Fragment getFragmentAll() {
		return FragmentAll;
	}

	public void setFragmentAll(Fragment fragmentAll) {
		FragmentAll = fragmentAll;
	}

	public Fragment getFragmentUploader() {
		return FragmentUploader;
	}

	public void setFragmentUploader(Fragment fragmentUploader) {
		FragmentUploader = fragmentUploader;
	}

	public Fragment getFragmentPlaylist() {
		return FragmentPlaylist;
	}

	public void setFragmentPlaylist(Fragment fragmentPlaylist) {
		FragmentPlaylist = fragmentPlaylist;
	}

	public void setAPI(ArrayList<String> aPI) {
		API = aPI;
	}

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
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(views.get(arg1), 0);
			return views.get(arg1);
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

			if (!isPagerSet) {
				String url = "http://www.gosugamers.net/dota2/gosubet";
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
			}

			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			// Do anything with response..
			// System.out.println(result);

			// ytf = switcher(ytf,result);

			if (!isPagerSet) {
				for (Element link : links) {

					String match;

					match = link.select("span").first().text().trim() + " vs "
							+ link.select("span").get(2).text().trim() + " ";
					if (link.getElementsByClass("results").isEmpty())
						match += link.select("td").get(3).text().trim();
					else
						match += link.select("span.hidden").first().text()
								.trim();

					matches.add(match);
				}

				initViewPager();

				isPagerSet = true;
			}

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
			sfa.findViewById(R.id.fullscreen_loading_indicator).setVisibility(
					View.GONE);

			if (!isMoreVideos) {
				((LoadMoreListView) getListView()).onNoMoreItems();

				myLoadMoreListView.setOnLoadMoreListener(null);
			}

			super.onPostExecute(result);

		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			((LoadMoreListView) getListView()).onLoadMoreComplete();
		}

	}

}
