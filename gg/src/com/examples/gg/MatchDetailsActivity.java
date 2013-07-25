package com.examples.gg;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.examples.gg.R.id;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MatchDetailsActivity extends SherlockListActivity {

	private ActionBar mActionBar;
	private String matchLink;
	private String baseUrl = "http://www.gosugamers.net";
	protected View fullscreenLoadingView;
	private Elements contents = new Elements();
	private InternetConnection ic;
	private Activity sfa;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	DisplayImageOptions options;
	private ArrayList<String> lives = new ArrayList<String>();
	private Button mRetryButton;
	private View mRetryView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.matchdisplay);

		Intent intent = getIntent();
		matchLink = intent.getStringExtra("link");

		System.out.println(matchLink);

		mActionBar = getSupportActionBar();

		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);

		fullscreenLoadingView = findViewById(R.id.fullscreen_loading_indicator);

		fullscreenLoadingView.setVisibility(View.VISIBLE);

		// imageLoader.init(ImageLoaderConfiguration.createDefault(activityContext));
		//
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.loading)
				.showImageForEmptyUri(R.drawable.loading)
				.showImageOnFail(R.drawable.loading).cacheInMemory(true)
				.cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(20))
				.build();

		ic = new InternetConnection();

		sfa = this;

		// Get the button view in retry.xml
		mRetryButton = (Button) sfa.findViewById(R.id.mRetryButton);

		// Get Retry view
		mRetryView = sfa.findViewById(R.id.mRetry);

		// Set a listener for the button Retry
		setRetryButtonListener();

		getMatchDetails mMatchDetails = new getMatchDetails();
		if (ic.checkConnection(sfa)) {
			mMatchDetails.execute(matchLink);
		}

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

		if (ic.checkConnection(sfa)) {
			Intent i = new Intent(this, TwitchPlayer.class);
			i.putExtra("video", lives.get(position));
			startActivity(i);
		}

	}

	public void initMatchView() {

		ImageView icon_1 = (ImageView) findViewById(R.id.icon1);
		ImageView icon_2 = (ImageView) findViewById(R.id.icon2);

		TextView teamName_1 = (TextView) findViewById(R.id.tName1);
		TextView teamName_2 = (TextView) findViewById(R.id.tName2);

		TextView tournamentName = (TextView) findViewById(R.id.tournamentName);
		TextView format = (TextView) findViewById(R.id.format);
		TextView startTime = (TextView) findViewById(R.id.startTime);
		TextView liveLabel = (TextView) findViewById(R.id.livelabel);
		TextView noLive = (TextView) findViewById(R.id.nolive);

		teamName_1.setText(contents.get(0).select("a").first().text().trim());
		teamName_2.setText(contents.get(1).select("a").first().text().trim());
		
		imageLoader.displayImage(baseUrl
				+ contents.get(0).select("img").first().attr("src"), icon_1,
				options, animateFirstListener);

		imageLoader.displayImage(baseUrl
				+ contents.get(1).select("img").first().attr("src"), icon_2,
				options, animateFirstListener);

		Elements detailTd = contents.get(2).select("td");
		
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

		if (lives.isEmpty()) {
			liveLabel.setVisibility(View.GONE);
			noLive.setVisibility(View.VISIBLE);
		} else {

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, lives);

			setListAdapter(adapter);
		}

	}

	private class getMatchDetails extends AsyncTask<String, Void, Elements> {

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

				Element opp_1 = doc.select("div.opponent").first();

				Element opp_2 = doc.select("div.opponent").get(1);

				Element details = doc.select("table#match-details").first();

				contents.add(opp_1);
				contents.add(opp_2);
				contents.add(details);

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
			} catch (IOException e) {
				e.printStackTrace();
			}

			return contents;
		}

		@Override
		protected void onPostExecute(Elements contents) {
			// Checking network status first
			if (ic.checkConnection(sfa)) {
				if (contents != null) {
					
					initMatchView();

					fullscreenLoadingView.setVisibility(View.GONE);
				}else{
					// may be network problem, let user retry
					mRetryView.setVisibility(View.VISIBLE);
				}
			}
		}

	}

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

		Toast.makeText(sfa, "Refreshing", Toast.LENGTH_SHORT).show();

		// Start a new activity
		Intent i = new Intent(sfa, MatchDetailsActivity.class);
		i.putExtra("link", matchLink);
		startActivity(i);

	}

	// set a listener for "Retry" button
	public void setRetryButtonListener() {

		mRetryButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ic.isOnline(sfa)) {
					refreshActivity();
					mRetryView.setVisibility(View.GONE);
				}

			}
		});
	}
}
