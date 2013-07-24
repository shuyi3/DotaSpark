package com.examples.gg;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class MatchDetailsActivity extends SherlockListActivity{
	
	private ActionBar mActionBar;
	private String matchLink;
	private String baseUrl = "http://www.gosugamers.net";
	protected View fullscreenLoadingView;
	private Elements contents = new Elements();
	private InternetConnection ic;
	private Activity activityContext;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	DisplayImageOptions options;
	private ArrayList<String> lives = new ArrayList<String>();

	
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
		
//		imageLoader.init(ImageLoaderConfiguration.createDefault(activityContext));
//		
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.loading)
		.showImageForEmptyUri(R.drawable.loading)
		.showImageOnFail(R.drawable.loading).cacheInMemory(true)
		.cacheOnDisc(true)
		.displayer(new RoundedBitmapDisplayer(20))
		.build();

		
		ic = new InternetConnection();
		
		activityContext = this;
		
		getMatchDetails mMatchDetails = new getMatchDetails();
		mMatchDetails.execute(matchLink);

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
        menu.add(0, 0, 0, "Refresh")
        .setIcon(R.drawable.ic_refresh)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
        return true;
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
		      finish();
		}

		return super.onOptionsItemSelected(item);
	}
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
          
          super.onListItemClick(l, v, position, id);
  
			Intent i = new Intent(this, TwitchPlayer.class);
			i.putExtra("video", lives.get(position));
			startActivity(i);
             
    }


	public void initMatchView(){
		
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

		imageLoader.displayImage(baseUrl+contents.get(0).select("img").first().attr("src"),
				icon_1, options, animateFirstListener);

		imageLoader.displayImage(baseUrl+contents.get(1).select("img").first().attr("src"),
				icon_2, options, animateFirstListener);

		tournamentName.setText("Tournament: " + contents.get(2).select("td").get(1).text().trim());
		format.setText("Format: " + contents.get(2).select("td").get(3).text().trim());
		String dateInString = contents.get(2).select("td").first().text().trim();
		startTime.setText("Start Time: " + dateInString);
		processDate(dateInString);
		
		
		
		
		if (lives.isEmpty()){
			liveLabel.setVisibility(View.GONE);
			noLive.setVisibility(View.VISIBLE);
		}else{
		
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
					for (Element f: flash){
						if (!flash.isEmpty()){
							String data = f.attr("data");
							String mData = data.substring(data.indexOf("=")+1, data.length());
//							String pattern = "(.*?)=(.*?)";
//							data.replace(pattern, "$2"); 
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

//			if (ic.checkConnection(activityContext)) {

				// Setting layout

//				for (Element content : contents) {

//					if (link.getElementsByClass("results").isEmpty()) {
//
//						Match newMatch = new Match();
//						Element opp_1 = link.select("td.opp").first();
//						Element opp_2 = link.select("td.opp").get(1);
//
//						newMatch.setTeamName1(opp_1.select("span").first()
//								.text().trim());
//						newMatch.setTeamName2(opp_2.select("span").first()
//								.text().trim());
//
//						newMatch.setTeamIcon1(baseUrl
//								+ opp_1.select("img").attr("src"));
//						newMatch.setTeamIcon2(baseUrl
//								+ opp_2.select("img").attr("src"));
//
//						// if (link.getElementsByClass("results").isEmpty()){
//						newMatch.setTime(link.select("td").get(3).text().trim());
//						// }else{
//						// newMatch.setTime(link.select("span.hidden").first().text().trim());
//						//
//						// }
//
//						newMatch.setGosuLink(baseUrl
//								+ opp_1.select("a[href]").attr("href"));
//
//						matchArray.add(newMatch);
//
//						// } else {
//						// match +=
//						// link.select("span.hidden").first().text().trim();
//						// results.add(match);
//						// }
//					}
					
//					contents.get(0).select("a").first().text().trim();
//					
//					contents.get(0).select("img").first().attr("src");
//					
//					contents.get(1).select("a").first().text().trim();
//					contents.get(1).select("img").first().attr("src");
//					
//					contents.get(2).select("td").first().text().trim();
//					contents.get(2).select("td").get(2).text().trim();
//					contents.get(2).select("td").get(3).text().trim();
//
//				}
//
//				for (Match m : matchArray) {
//					System.out.println(m.getTeamName1());
//				}
//
//				mArrayAdatper.notifyDataSetChanged();
				initMatchView();

				fullscreenLoadingView.setVisibility(View.GONE);

//			} else {
//
//				// No internet
//
//				if (fullscreenLoadingView.getVisibility() == View.VISIBLE) {
//					// Internet lost during fullscree loading
//
//					ic.setNetworkError(InternetConnection.fullscreenLoadingError);
//				} else {
//					// Internet lost during loading more
//					ic.setNetworkError(InternetConnection.loadingMoreError);
//				}
//			}

		}

	}
	
	public void processDate(String s){
		
		String dateInString = s.substring(0, s.indexOf("at")-1);
		String hourInString = s.substring(s.indexOf(":")-2, s.indexOf(":"));
		String minuteInString = s.substring(s.indexOf(":")+1, s.length());
		System.out.println("Date: " + dateInString);
		System.out.println("Hour: " + hourInString);
		System.out.println("Minute: " + minuteInString);
		Date date = null;
		//Calendar c = new Calendar();
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy at HH:mm"); 
//		try {
//			date = (Date) sdf.parse(s);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		date.setHours(Integer.parseInt(hourInString));
		//return date;
	}

	
}
