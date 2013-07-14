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

	public VideoArrayAdapter(Context context, ArrayList<String> values,
			ArrayList<Video> videos) {
		super(context, R.layout.videolist, values);
		this.context = context;
		this.values = values;
		this.videos = videos;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		// if (convertView == null) {
		convertView = inflater.inflate(R.layout.videolist, parent, false);

		holder = new ViewHolder();

		holder.titleView = (TextView) convertView.findViewById(R.id.videotitle);
		holder.imageView = (ImageView) convertView.findViewById(R.id.thumbnail);
		holder.countView = (TextView) convertView.findViewById(R.id.Desc);
		holder.videoLength = (TextView) convertView
				.findViewById(R.id.videolength);
		// ImageView uploaderView = (ImageView)
		// rowView.findViewById(R.id.uploaderImage);

		// set the description
		// TextView descView = (TextView)
		// rowView.findViewById(R.id.description);
		// descView.setText(videos.get(position).getVideoDesc());

		// set the author
		holder.authorView = (TextView) convertView
				.findViewById(R.id.videouploader);

		// set the update time
		// TextView timeView = (TextView) rowView.findViewById(R.id.updatetime);
		// timeView.setText(videos.get(position).getUpdateTime());

		// Change icon based on name

		// new
		// DownloadImage(videos.get(position).getUploaderThumUrl()).execute(uploaderView);
		convertView.setTag(holder);
		// }else{
		// holder = (ViewHolder) convertView.getTag();
		// }

		holder.titleView.setText(values.get(position));
		holder.authorView.setText(videos.get(position).getAuthor());

		// values for time and view counts should not be null
		if (videos.get(position).getUpdateTime() != null
				&& videos.get(position).getViewCount() != null) {
			
			// For Youtube videos, showing update date and views
			holder.countView.setText(videos.get(position).getUpdateTime()
					+ " | " + videos.get(position).getViewCount());
		} else if(videos.get(position).getViewCount() != null){
			
			// For Twitch, only showing number of viewers
			holder.countView.setText(videos.get(position).getViewCount());
		} else{	
			
			holder.countView.setText(null);
		}
		holder.videoLength.setText(videos.get(position).getDuration());

		if (videos.get(position).getThumbnail() == null)
			new DownloadImage(videos.get(position).getThumbnailUrl(),
					videos.get(position), holder.imageView).execute();
		else
			holder.imageView
					.setImageBitmap(videos.get(position).getThumbnail());

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
		private Video mVideo;

		public DownloadImage(String url, Video video, ImageView imageView) {
			this.url = url;
			this.mVideo = video;
			this.imageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(Object... params) {
			// TODO Auto-generated method stub

			try {
				InputStream in = (InputStream) new URL(url).getContent();
				mVideo.thumbnail = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return mVideo.thumbnail;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			imageView.setImageBitmap(result);
		}

	}
}