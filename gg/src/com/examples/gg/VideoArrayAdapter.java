package com.examples.gg;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final ArrayList<String> values;
	private ArrayList<Video> videos;
	private LayoutInflater inflater;
 
	public VideoArrayAdapter(Context context, ArrayList<String> values, ArrayList<Video> videos) {
		super(context, R.layout.videolist, values);
		this.context = context;
		this.values = values;
		this.videos = videos;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
 
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.videolist, parent, false);
		
			holder = new ViewHolder();
		
			holder.titleView = (TextView) convertView.findViewById(R.id.videotitle);
			holder.imageView = (ImageView) convertView.findViewById(R.id.thumbnail);
			holder.countView = (TextView) convertView.findViewById(R.id.Desc);
			holder.videoLength = (TextView) convertView.findViewById(R.id.videolength);
//		ImageView uploaderView = (ImageView) rowView.findViewById(R.id.uploaderImage);
		
		//set the description
//		TextView descView = (TextView) rowView.findViewById(R.id.description);
//		descView.setText(videos.get(position).getVideoDesc());
		
		//set the author
			holder.authorView = (TextView) convertView.findViewById(R.id.videouploader);
		
		//set the update time
//		TextView timeView = (TextView) rowView.findViewById(R.id.updatetime);
//		timeView.setText(videos.get(position).getUpdateTime());
 
		// Change icon based on name
			
//        new DownloadImage(videos.get(position).getUploaderThumUrl()).execute(uploaderView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.titleView.setText(values.get(position));
		holder.authorView.setText(videos.get(position).getAuthor());
		holder.countView.setText(videos.get(position).getViewCount());
		holder.videoLength.setText(videos.get(position).getDuration());
		
		new DownloadImage(videos.get(position).getThumbnailUrl()).execute(holder.imageView );
 
		return convertView;
	}
	
    static class ViewHolder {
        TextView titleView;
        TextView authorView;
        TextView countView;
        TextView videoLength;
        ImageView imageView;
        
        
    }
	
	private class DownloadImage extends AsyncTask<Object, String, Bitmap> {
		private ImageView imageView;
		private Bitmap thumbnail = null;
		private String url = null;
		
		public DownloadImage (String url) {
			this.url = url;
		}

		@Override
		protected Bitmap doInBackground(Object... params) {
			// TODO Auto-generated method stub
			
			 imageView = (ImageView)  params[0];
			
			 try {
		            InputStream in = (InputStream) new URL(url).getContent();
		            thumbnail = BitmapFactory.decodeStream(in);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		   
			 return thumbnail;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
            if(result != null && imageView != null){
            	imageView.setVisibility(View.VISIBLE);
            	imageView.setImageBitmap(result);
            }else{
            	imageView.setVisibility(View.GONE);
            }
        }
		
		
		
	}
}