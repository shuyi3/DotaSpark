package com.examples.gg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.AsyncTask.Status;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.costum.android.widget.LoadMoreListView;
import com.examples.gg.LoadMore_Base.LoadMoreTask;
import com.examples.gg.R.id;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MatchDetailsActivity extends SherlockListActivity {

	private ActionBar mActionBar;
	private Match match;
	private String baseUrl = "http://www.gosugamers.net";
	protected View fullscreenLoadingView;
	private InternetConnection ic;
	private Activity mActivity;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	DisplayImageOptions options;
	private ArrayList<String> lives = new ArrayList<String>();
	private Button mRetryButton;
	private View mRetryView;
	private TextView myTimer;
	private View contentLayout;
	private getMatchDetails mMatchDetails;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.matchdisplay);

		Intent intent = getIntent();
		match = intent.getParcelableExtra("match");

		mActionBar = getSupportActionBar();

		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		
		contentLayout = findViewById(R.id.contentLayout);

		fullscreenLoadingView = findViewById(R.id.fullscreen_loading_indicator);

		callLoadingIndicator();
		// imageLoader.init(ImageLoaderConfiguration.createDefault(activityContext));
		//
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.loading)
				.showImageForEmptyUri(R.drawable.loading)
				.showImageOnFail(R.drawable.loading).cacheInMemory(true)
				.cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(20))
				.build();

		ic = new InternetConnection();

		mActivity = this;

		// Get the button view in retry.xml
		mRetryButton = (Button) mActivity.findViewById(R.id.mRetryButton);

		// Get Retry view
		mRetryView = mActivity.findViewById(R.id.mRetry);

		// Set a listener for the button Retry
		setRetryButtonListener();

		mMatchDetails = new getMatchDetails();

		mMatchDetails.execute(match.getGosuLink());
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, 0, 0, "Refresh")
				.setIcon(R.drawable.ic_refresh)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			finish();
		}

		if (item.getItemId() == 0) {
			refreshActivity();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);

//		if (ic.isOnline(mActivity)) {
			Intent i = new Intent(this, TwitchPlayer.class);
			i.putExtra("video", lives.get(position));
			startActivity(i);
//		}

	}

	public void initMatchView() {
		

	}

	private class getMatchDetails extends AsyncTask<String, Void, String> {
		
		protected String responseString = null;
		protected boolean taskCancel = false;

//		@Override
//		protected Elements doInBackground(String... params) {
//
//			String url = params[0];
//			Document doc;
//			try {
//				doc = Jsoup
//						.connect(url)
//						.header("User-Agent",
//								"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
//						.get();
//
//				Element opp_1 = doc.select("div.opponent").first();
//
//				Element opp_2 = doc.select("div.opponent").get(1);
//				
//				Element scoreDiv_1 = doc.select("div.score draw").first();
//				
//				Element scoreDiv_2 = doc.select("div.score draw").get(1);
//
//				Element details = doc.select("table#match-details").first();
//
//				contents.add(opp_1);
//				contents.add(opp_2);
//				contents.add(details);
//
//				Elements flash = doc.select("object");
//				if (!flash.isEmpty())
//					for (Element f : flash) {
//						if (!flash.isEmpty()) {
//							String data = f.attr("data");
//							String mData = data.substring(
//									data.indexOf("=") + 1, data.length());
//							// String pattern = "(.*?)=(.*?)";
//							// data.replace(pattern, "$2");
//							lives.add(mData);
//						}
//					}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//			return contents;
//		}
		
		@Override
		protected String doInBackground(String... uri) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;

//			if (!ic.isOnline(mActivity)) {
//				Log.d("AsyncDebug", "Ic not online!");
//
//				cancel(true);
//				taskCancel = true;
//			} else
				try {
					HttpGet myGet = new HttpGet(uri[0]);
					// myGet.setParams(httpParameters);
					response = httpclient.execute(myGet);
					StatusLine statusLine = response.getStatusLine();
					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

						Log.d("AsyncDebug", "200 OK!");

						ByteArrayOutputStream out = new ByteArrayOutputStream();
						response.getEntity().writeTo(out);
						out.close();
						responseString = out.toString();
					} else {

						Log.d("AsyncDebug", "Not 200 OK!");

						// Closes the connection.
						response.getEntity().getContent().close();
						// throw new IOException(statusLine.getReasonPhrase());

						cancel(true);
						taskCancel = true;

						throw new IOException(statusLine.getReasonPhrase());

					}
				} catch (Exception e) {
					// throw new IOException(statusLine.getReasonPhrase());

					Log.d("AsyncDebug", e.toString());

					cancel(true);
					taskCancel = true;

				} finally {

					Log.d("AsyncDebug", "shutdown");

					httpclient.getConnectionManager().shutdown();
					Log.d("AsyncDebug", "Do in background finished!");

				}
			return responseString;
		}


		@Override
		protected void onPostExecute(String result) {
			// Checking network status first
			Log.d("AsyncDebug", "Into onPostExecute!");

			if (!taskCancel && result != null) {

				Document doc = Jsoup.parse(result);
				
				Element opp_1 = doc.select("div.opponent").first();

				Element opp_2 = doc.select("div.opponent").get(1);
				
				Element scoreDiv_1 = doc.select("div[class=center-column]").first().select("div").get(1);
				
				Element scoreDiv_2 = doc.select("div[class=center-column]").first().select("div").get(2);

				Element details = doc.select("table#match-details").first();
				
				Elements flash = doc.select("object");
				if (!flash.isEmpty())
					for (Element f : flash) {
						if (!flash.isEmpty()) {
							String data = f.attr("data");
							String mData = data.substring(
									data.indexOf("=") + 1, data.length());
							// String pattern = "(.*?)=(.*?)";
							// data.replace(pattern, "$2");
							lives.add(mData);
						}
					}

				myTimer = (TextView) findViewById(R.id.myTimer);

				ImageView icon_1 = (ImageView) findViewById(R.id.icon1);
				ImageView icon_2 = (ImageView) findViewById(R.id.icon2);

				TextView teamName_1 = (TextView) findViewById(R.id.tName1);
				TextView teamName_2 = (TextView) findViewById(R.id.tName2);
				
				TextView team1score = (TextView) findViewById(R.id.team1score);
				TextView team2score = (TextView) findViewById(R.id.team2score);

				TextView tournamentName = (TextView) findViewById(R.id.tournamentName);
				TextView format = (TextView) findViewById(R.id.format);
				TextView startTime = (TextView) findViewById(R.id.startTime);
				TextView liveLabel = (TextView) findViewById(R.id.livelabel);
				TextView noLive = (TextView) findViewById(R.id.nolive);
				TextView liveStatus = (TextView) findViewById(R.id.liveStatus);
				
				teamName_1.setText(opp_1.select("a").first().text().trim());
				teamName_2.setText(opp_2.select("a").first().text().trim());
				
				if (scoreDiv_1.className().trim().endsWith("winner")){
					team1score.setTextColor(Color.RED);
				}
				team1score.setText(scoreDiv_1.text().trim());

				if (scoreDiv_2.className().trim().endsWith("winner")){
					team2score.setTextColor(Color.RED);
				}
				team2score.setText(scoreDiv_2.text().trim());

				
				imageLoader.displayImage(baseUrl
						+ opp_1.select("img").first().attr("src"), icon_1,
						options, animateFirstListener);

				imageLoader.displayImage(baseUrl
						+ opp_2.select("img").first().attr("src"), icon_2,
						options, animateFirstListener);

				Elements detailTd = details.select("td");
				
				if (detailTd.size() == 4){
				
					tournamentName.setText("Tournament: "
							+ detailTd.get(2).text().trim());
					format.setText("Format: Best of "
							+ detailTd.get(3).text().trim());
					String dateInString = detailTd.first().text()
							.trim();
					startTime.setText("Start Time: " + processDate(dateInString)
							+ " (Local)");
								
				}else{
					tournamentName.setText("Tournament: "
							+ detailTd.get(1).text().trim());
					format.setText("Format: Best of "
							+ detailTd.get(2).text().trim());
					startTime.setText("Start Time: ");			
				}
				
				if (match.getMatchStatus() == Match.LIVE){
					liveStatus.setVisibility(View.VISIBLE);
				}else if (match.getMatchStatus() == Match.NOTSTARTED){
					myTimer.setText(match.getTime());
					myTimer.setVisibility(View.VISIBLE);
				}

				if (lives.isEmpty()) {
					liveLabel.setVisibility(View.GONE);
					noLive.setVisibility(View.VISIBLE);
				} else {

					ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity,
							android.R.layout.simple_list_item_1, lives);

					setListAdapter(adapter);
				}		

				callLayout();
					
			}else{
				
				cancelSingleTask(this);

			}
		}
		
		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			Log.d("AsyncDebug", "Into OnCancelled!");
			cancelSingleTask(this);
		}

	}

	@SuppressLint("SimpleDateFormat")
	public String processDate(String s) {

		Date date = new Date();
		// Calendar c = new Calendar();
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' HH:mm");
		sdf.setTimeZone(TimeZone.getTimeZone("CET"));

		try {
			date = sdf.parse(s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sdf = new SimpleDateFormat("MMMM d 'at' hh:mm a");
		sdf.setTimeZone(TimeZone.getDefault());
		// date.setHours(Integer.parseInt(hourInString));
		return sdf.format(date);
		
	}

	public void refreshActivity() {
		// Destroy current activity
		finish();

		Toast.makeText(mActivity, "Refreshing", Toast.LENGTH_SHORT).show();

		// Start a new activity
		Intent i = new Intent(mActivity, MatchDetailsActivity.class);
		i.putExtra("match", match);
		startActivity(i);

	}
	
	public void cancelSingleTask(getMatchDetails mTask) {

			ic.setNetworkError(InternetConnection.fullscreenLoadingError);
			callRetryView();

	}


	// set a listener for "Retry" button
	public void setRetryButtonListener() {

		mRetryButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				if (ic.isOnline(mActivity)) {
					refreshActivity();
//				}

			}
		});
	}
	
	public void callRetryView(){
		if (mRetryView != null)
			mRetryView.setVisibility(View.VISIBLE);
		if (contentLayout != null)
			contentLayout.setVisibility(View.GONE);
		if (fullscreenLoadingView != null)
			fullscreenLoadingView.setVisibility(View.GONE);
	}
	
	public void callLayout(){
		if (contentLayout != null)
			contentLayout.setVisibility(View.VISIBLE);
		if (mRetryView != null)
			mRetryView.setVisibility(View.GONE);
		if (fullscreenLoadingView != null)
			fullscreenLoadingView.setVisibility(View.GONE);
	}
	
	public void callLoadingIndicator(){
		if (fullscreenLoadingView != null)
			fullscreenLoadingView.setVisibility(View.VISIBLE);
		if (contentLayout != null)
			contentLayout.setVisibility(View.GONE);
		if (mRetryView != null)
			mRetryView.setVisibility(View.GONE);
	}
	
	public void hideAllViews(){
		if (fullscreenLoadingView != null)
			fullscreenLoadingView.setVisibility(View.GONE);
		if (contentLayout != null)
			contentLayout.setVisibility(View.GONE);
		if (mRetryView != null)
			mRetryView.setVisibility(View.GONE);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();

			Log.d("UniversalImageLoader", "clear from MatchDetail");
			imageLoader.clearDiscCache();
			imageLoader.clearMemoryCache();
		
		// check the state of the task
			cancelAllTask();
			hideAllViews();
		
	}
	
	public void cancelAllTask() {

			if (mMatchDetails != null && mMatchDetails.getStatus() == Status.RUNNING) {
				mMatchDetails.cancel(true);

				Log.d("AsyncDebug", "Task cancelled!!!!!!!!");
			} else
				Log.d("AsyncDebug", "Task cancellation failed!!!!");

	}



}
