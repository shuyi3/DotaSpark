package com.examples.gg;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

public class mygifview extends ImageView {
	Movie movie, movie1;
	InputStream is;
	long moviestart;

	public mygifview(Context context, InputStream stream) {
		super(context);

		playGif(stream);
	}

	private boolean mIsPlayingGif = false;

	private GifDecoder mGifDecoder;

	private Bitmap mTmpBitmap;

	final Handler mHandler = new Handler();

	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			if (mTmpBitmap != null && !mTmpBitmap.isRecycled()) {
				mygifview.this.setImageBitmap(mTmpBitmap);
			}
		}
	};

	private void playGif(InputStream stream) {
		mGifDecoder = new GifDecoder();
		mGifDecoder.read(stream);
		mIsPlayingGif = true;
		new Thread(new Runnable() {
			public void run() {
				final int n = mGifDecoder.getFrameCount();
				final int ntimes = mGifDecoder.getLoopCount();
				int repetitionCounter = 0;
				do {
					for (int i = 0; i < n; i++) {
						mTmpBitmap = mGifDecoder.getFrame(i);
						final int t = mGifDecoder.getDelay(i);
						mHandler.post(mUpdateResults);
						try {
							Thread.sleep(t);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (ntimes != 0) {
						repetitionCounter++;
					}
				} while (mIsPlayingGif && (repetitionCounter <= ntimes));
			}
		}).start();

	}

	//
}
