package com.examples.gg;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class FeedManager
{

    private JSONObject feed;
    private String theSource;
    private static final String YOUTUBE = "Youtube";
    private static final String TWITCH = "Twitch";
    private String twtichNextApi;


    public FeedManager(String json, String source) throws JSONException{
    	this.theSource = source;
    	if(source.equals(YOUTUBE))
    		processJSONYoutube(json);
    	else if(source.equals(TWITCH))
    		processJSONTwitch(json);
    }

    private void processJSONTwitch(String json) throws JSONException {
		// TODO Auto-generated method stub
    	twtichNextApi = null;
        JSONTokener jsonParser = new JSONTokener(json);  
        this.feed = (JSONObject) jsonParser.nextValue();  
        twtichNextApi = feed.getJSONObject("_links").getString("next");
	}

	public ArrayList<Video> getVideoPlaylist(){
        ArrayList<Video> videos = new ArrayList<Video>();
        
        try {
        	if(theSource.equals(YOUTUBE)){
	            //get title of the playlist
	            String plTitle = feed.getJSONObject("title").getString("$t");
	            //System.out.println(plTitle);
	            //get the playlist
	            JSONArray playlist = feed.getJSONArray("entry");
	            //System.out.println("Length: "+ playlist.length());
	            
	            for(int i=0;i<playlist.length();i++){
	                //get a video in the playlist           
	                JSONObject oneVideo = playlist.getJSONObject(i);
	                //get the title of this video
	                String videoTitle = oneVideo.getJSONObject("title").getString("$t");
	                String videoLink = oneVideo.getJSONObject("content").getString("src");
	                String videoId = videoLink.substring(videoLink.indexOf("/v/")+3, videoLink.indexOf("?"));
	                String videoDesc = oneVideo.getJSONObject("media$group").getJSONObject("media$description").getString("$t");
	                String thumbUrl = oneVideo.getJSONObject("media$group").getJSONArray("media$thumbnail").getJSONObject(0).getString("url");
	                String updateTime = oneVideo.getJSONObject("published").getString("$t");
	                String author = oneVideo.getJSONArray("author").getJSONObject(0).getJSONObject("name").getString("$t");
	                String vCount = oneVideo.getJSONObject("yt$statistics").getString("viewCount") + " views";
	                String inSecs =  oneVideo.getJSONObject("media$group").getJSONObject("yt$duration").getString("seconds");
	                String convertedDuration = formatSecondsAsTime(inSecs) + " HD";
	                
	                //System.out.println("date: " + updateTime.substring(0,updateTime.indexOf("T")));
	                //System.out.println("time: " + updateTime.substring(updateTime.indexOf("T")+1, updateTime.indexOf(".")));
	                updateTime = handleDate(updateTime);
	                
	                Video video = new Video();
	                

	                if(videoTitle.length()>36){
	                	video.setTitle(videoTitle.substring(0, 33) + "..");
	                } else{
	                	video.setTitle(videoTitle);
	                }
	                
	                
	                //System.out.println("converted duration: " + convertedDuration);
	                //System.out.println(videoDesc);
	                //store title and link
	                
	                
	                video.setVideoId(videoId);
	                video.setThumbnailUrl(thumbUrl);
	                video.setVideoDesc(videoDesc);
	                video.setUpdateTime(updateTime);
	                video.setAuthor(author);
	                video.setViewCount(vCount);
	                video.setDuration(convertedDuration);
	                //System.out.println(video.getTitle());
	                //push it to the list
	                videos.add(video);
	                //System.out.println(videoTitle+"***"+videoLink);
	                
	                
	            }
	            
	            
	            //check if there are more videos
	            //String result = getNextApi();
	            //System.out.println("Result: "+result);

        	}else if (theSource.equals(TWITCH)){
	          	//Json is coming from twitch
	    		JSONArray streams = feed.getJSONArray("streams");
	    		System.out.println("Total: " + streams.length());
	            for(int i=0;i<streams.length();i++){
	                //get a video in the playlist           
	                JSONObject oneVideo = streams.getJSONObject(i);
	                //get the title of this video
	                JSONObject videoChannel = oneVideo.getJSONObject("channel");
	                String videoTitle = videoChannel.getString("status");
	                String videoLink = oneVideo.getJSONObject("_links").getString("self");
	                String videoId = videoLink.substring(videoLink.indexOf("/streams/")+9, videoLink.length());
	                String videoDesc = "No Desc";
	                String thumbUrl = oneVideo.getJSONObject("preview").getString("small");
	                String updateTime = videoChannel.getString("updated_at");
	                String author = videoChannel.getString("display_name");
	                System.out.println("Stream ID: " + videoId);
	                //store title and link
	                
	                Video video = new Video();
	                video.setTitle(videoTitle);
	                video.setVideoId(videoId);
	                video.setAuthor(author);
	                video.setThumbnailUrl(thumbUrl);
	                video.setVideoDesc(videoDesc);
	                video.setUpdateTime(updateTime);
	                //video.setAuthor(author);
	                //System.out.println(video.getTitle());
	                //push it to the list
	                videos.add(video);
	                //System.out.println(videoTitle+"***"+videoLink);
	                
	                
	            }
	    	}
        } catch (JSONException ex) {  
            
            ex.printStackTrace();
        }  
        
        
        return videos;      
    }
    
    public ArrayList<Video> getVideoPlaylist2(){
        ArrayList<Video> videos = new ArrayList<Video>();
        
        try {

        	
            //get title of the playlist
            String plTitle = feed.getJSONObject("title").getString("$t");
            //System.out.println(plTitle);
            //get the playlist
            JSONArray playlist = feed.getJSONArray("entry");
            //System.out.println("Length: "+ playlist.length());
            
            
            for(int i=0;i<playlist.length();i++){
            	Video video = new Video();
                //get a video in the playlist            
                JSONObject oneVideo = playlist.getJSONObject(i);
                //get the title of this video
                String videoTitle = oneVideo.getJSONObject("title").getString("$t");
                String videoLink = oneVideo.getJSONObject("content").getString("src");
                String videoId = videoLink.substring(videoLink.indexOf("/v/")+3, videoLink.indexOf("?"));
                String author = oneVideo.getJSONArray("author").getJSONObject(0).getJSONObject("name").getString("$t");
                //String videoDesc = oneVideo.getJSONObject("media$group").getJSONObject("media$description").getString("$t");
                String videoDesc = oneVideo.getJSONObject("summary").getString("$t");
                String thumbUrl = oneVideo.getJSONObject("media$group").getJSONArray("media$thumbnail").getJSONObject(0).getString("url");
                String updateTime = oneVideo.getJSONObject("updated").getString("$t");
                
                if(author.toUpperCase().equals("DOTACINEMA")){
                	video.setUploaderThumUrl("https://i1.ytimg.com/i/NRQ-DWUXf4UVN9L31Y9f3Q/1.jpg?v=5067cf3b");
                }else if(author.toUpperCase().equals("NOOBFROMUA")){
                	video.setUploaderThumUrl("https://i1.ytimg.com/i/fsOfLvadg89Bx8Sv_6WERg/1.jpg?v=515d687f");
                }
                
                //System.out.println(thumbUrl);
               // System.out.println(videoDesc);
                //store title and link
                
                video.setTitle(videoTitle);
                video.setVideoId(videoId);
                video.setThumbnailUrl(thumbUrl);
                video.setAuthor(author);
                video.setPlaylistUrl(videoLink + "&start-index=1&max-results=10&orderby=published&alt=json");
                video.setVideoDesc(videoDesc);
                video.setUpdateTime(updateTime);
                //System.out.println(video.getTitle());
                //push it to the list
                videos.add(video);
                //System.out.println(videoTitle+"***"+videoLink);
                
                
            }
            
            
            //check if there are more videos
            //String result = getNextApi();
            //System.out.println("Result: "+result);

        } catch (JSONException ex) {  
        	ex.printStackTrace();
        }  
        
        
        return videos;      
    }
    
    public String getNextApi() throws JSONException{
    	if(theSource.equals(YOUTUBE)){
        JSONArray link = feed.getJSONArray("link");
        for(int i=0;i<link.length();i++){
            JSONObject jo = link.getJSONObject(i);
            //System.out.println(jo.getString("rel"));
            if(jo.getString("rel").equals("next")){
                //there are more videos in this playlist
                String nextUrl = jo.getString("href");
                return nextUrl;
            }
        }
    	}else if(theSource.equals(TWITCH)){
    		return twtichNextApi;
    		
    	}
		return null;
    }
    



	public JSONObject getFeed() {
		return feed;
	}




	public void setFeed(JSONObject feed) {
		this.feed = feed;
	}
    
	private String formatSecondsAsTime(String secs){
		int totalSecs = Integer.parseInt(secs);
		
		int hours = totalSecs/3600;
		int minutes = (totalSecs%3600)/60;
		int seconds = totalSecs%60;
		
		if(hours == 0){
			return twoDigitString(minutes) + ":" + twoDigitString(seconds);
		}else{
			return twoDigitString(hours) + ":" + twoDigitString(minutes) + ":" + twoDigitString(seconds);
		}
		
	}
	
	private String twoDigitString(int number) {

	    if (number == 0) {
	        return "00";
	    }

	    if (number / 10 == 0) {
	        return "0" + number;
	    }

	    return String.valueOf(number);
	}
	
	
	private String handleDate(String s){
        String temp =  s.replace("T", " ");
        String dateInString = temp.substring(0, temp.indexOf("."));
        //System.out.println("Date in String: " + dateInString);
        
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
        Date d1 = new Date();
        Date d2 = new Date();
        try {
			d2 = dateFormat.parse(dateInString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //System.out.println("Date 1: " + dateFormat.format(d1));
        //System.out.println("Date 2: " + dateFormat.format(d2));
        
        //System.out.println(calculateDateDifference(d1,d2));
		return calculateDateDifference(d1,d2);
		
	}
	private String calculateDateDifference(Date today, Date past){
		long diff = today.getTime() - past.getTime();
		//System.out.println("diff: " + diff);
		long diffSec = (diff/1000L) % 60L;
		long diffMin = (diff/(60L*1000L)) % 60L;
		long diffHour = (diff/(60L*60L*1000L)) % 24L;
		long diffDay = (diff/(24L*60L*60L*1000L)) % 30L;
		long diffMonth = (diff/(30L*24L*60L*60L*1000L)) % 12L;
		long diffYear = (diff/(12L*30L*24L*60L*60L*1000L));
		
		if(diffYear==1){
			return diffYear + " year ago";
		}else if(diffYear > 1){
			return diffYear + " years ago";
		}else{
			//less than 1 year
			if(diffMonth == 1){
				return diffMonth + " month ago";
			}else if(diffMonth > 1){
				return diffMonth + " months ago";
			}else{
				//less than 1 month
				if(diffDay == 1){
					return diffDay + " day ago";
				}else if(diffDay > 1){
					return diffDay + " days ago";
				}else{
					//less than 1 day
					if(diffHour == 1){
						return diffHour + " hour ago";
					}else if(diffHour > 1){
						return diffHour + " hours ago";
					}else{
						//less than 1 hour
						if(diffMin == 1){
							return diffMin + " minute ago";
						}else if(diffMin > 1){
							return diffMin + " minutes ago";
						}else{
							//less than 1 minute
							if(diffSec == 0 || diffSec == 1){
								return diffSec + " second ago";
							}else if(diffSec > 1){
								return diffSec + " seconds ago";
							}
						}
					}
				}
			}
		}
		
		
		//String result = "Years: " + diffYear + "\nMonths: " + diffMonth + "\nDays: " + diffDay + "\nHours: " + diffHour + "\nMinutes: " + diffMin + "\nSeconds: " + diffSec + "\n";
		
		return "";
	}
    private void processJSONYoutube(String json) throws JSONException{
        JSONTokener jsonParser = new JSONTokener(json);  
        JSONObject wholeJson = (JSONObject) jsonParser.nextValue();  
        this.feed = wholeJson.getJSONObject("feed");
    }

}
