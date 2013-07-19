package com.examples.gg;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final ArrayList<String> values;
	private ArrayList<Video> videos;
	private LayoutInflater inflater;
	private Animation fadeAnimation;
	private ImageView mImageView;
	private TextView titleView;
	private TextView authorView;
	private TextView countView;
	private TextView videoLength;
	ImageView imageView;

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
		InternetConnection ic = new InternetConnection();

		convertView = inflater.inflate(R.layout.videolist, parent, false);

		titleView = (TextView) convertView.findViewById(R.id.videotitle);
		imageView = (ImageView) convertView.findViewById(R.id.thumbnail);
		countView = (TextView) convertView.findViewById(R.id.Desc);
		videoLength = (TextView) convertView.findViewById(R.id.videolength);
		authorView = (TextView) convertView.findViewById(R.id.videouploader);

		titleView.setText(values.get(position));
		authorView.setText(videos.get(position).getAuthor());

		// values for time and view counts should not be null
		if (videos.get(position).getUpdateTime() != null
				&& videos.get(position).getViewCount() != null) {

			// For Youtube videos, showing update date and views
			countView.setText(videos.get(position).getUpdateTime() + " | "
					+ videos.get(position).getViewCount());
		} else if (videos.get(position).getViewCount() != null) {

			// For Twitch, only showing number of viewers
			countView.setText(videos.get(position).getViewCount());
		} else {

			countView.setText(null);
		}
		videoLength.setText(videos.get(position).getDuration());

		if (videos.get(position).getThumbnail() == null) {
			if (ic.isOnline((Activity) context))
				new DownloadImage(videos.get(position).getThumbnailUrl(),
						videos.get(position), imageView).execute();
		} else {
			imageView.setImageBitmap(videos.get(position).getThumbnail());
		}
		return convertView;
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
			setImageBitmapWithFade(imageView, result);
		}

	}

	// This is for image animation
	public static void setImageBitmapWithFade(final ImageView imageView,
			final Bitmap bitmap) {
		Resources resources = imageView.getResources();
		BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, bitmap);
		setImageDrawableWithFade(imageView, bitmapDrawable);
	}

	public static void setImageDrawableWithFade(final ImageView imageView,
			final Drawable drawable) {
		Drawable currentDrawable = imageView.getDrawable();
		if (currentDrawable != null) {
			Drawable[] arrayDrawable = new Drawable[2];
			arrayDrawable[0] = currentDrawable;
			arrayDrawable[1] = drawable;
			TransitionDrawable transitionDrawable = new TransitionDrawable(
					arrayDrawable);
			transitionDrawable.setCrossFadeEnabled(true);
			imageView.setImageDrawable(transitionDrawable);
			transitionDrawable.startTransition(250);
		} else {
			imageView.setImageDrawable(drawable);
		}
	}
}