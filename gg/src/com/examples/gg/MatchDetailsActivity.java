package com.examples.gg;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

public class MatchDetailsActivity extends SherlockListActivity{
	
	private ActionBar mActionBar;
	private String matchLink;
	private String baseUrl = "http://www.gosugamers.net";
	protected View fullscreenLoadingView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matchdisplay);
        
		Intent intent = getIntent();
		matchLink = intent.getStringExtra("link");
        
		mActionBar = getSupportActionBar();

		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		
		fullscreenLoadingView = findViewById(R.id.fullscreen_loading_indicator);
		
		fullscreenLoadingView.setVisibility(View.VISIBLE);

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

//	private class getMatchDetails extends AsyncTask<String, Void, Elements> {
//
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
//				links = doc.select("tr:has(td.opp)");
//
//				// for (String match: matches){
//				// System.out.println(match);
//				// }
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//			return links;
//		}
//
//		@Override
//		protected void onPostExecute(Elements links) {
//
//			if (ic.checkConnection(sfa)) {
//
//				// Setting layout
//
//				String baseUrl = "http://www.gosugamers.net";
//
//				for (Element link : links) {
//
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
//
//				}
//
//				for (Match m : matchArray) {
//					System.out.println(m.getTeamName1());
//				}
//
//				mArrayAdatper.notifyDataSetChanged();
//
//				fullscreenLoadingView.setVisibility(View.GONE);
//
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
//
//		}
//
//	}

	
}
