package com.examples.gg;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		NotificationManager mNM;

		mNM = (NotificationManager) context
				.getSystemService(context.NOTIFICATION_SERVICE);
		// Set the icon, scrolling text and timestamp
//		Log.i("debug", Integer.toString(intent.getIntExtra("rand", 1)));
		Notification notification = new Notification(R.drawable.ic_launcher,
				intent.getStringExtra("msg"), System.currentTimeMillis());
		notification.flags = Notification.DEFAULT_LIGHTS
				| Notification.FLAG_AUTO_CANCEL;
		try {
			Uri notificationURI = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Ringtone r = RingtoneManager.getRingtone(context, notificationURI);
			r.play();

			Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			// Vibrate for 500 milliseconds
			v.vibrate(500);
		} catch (Exception e) {
		}

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, SideMenuActivity.class), 0);
		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(context, "The game has started!",
				intent.getStringExtra("msg"), contentIntent);
		// Send the notification.
		// We use a layout id because it is a unique number. We use it later to
		// cancel.
		mNM.notify(intent.getIntExtra("rand", 1), notification);
	}
}
