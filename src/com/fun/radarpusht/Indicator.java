package com.fun.radarpusht;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created: 6/6/13 9:27 PM
 */
public class Indicator {

	public static void showIndicator(Context context, String ticker, String title){
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(
				1,
				getSimple(
						ticker,
						title,
						context
				).build());
	}


	private static NotificationCompat.Builder getSimple(String ticker, String title, Context context) {

		String contentText = "click here to open app";

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		NotificationCompat.InboxStyle styled = new NotificationCompat.InboxStyle(builder);
		// set the default notification options (Vibrate / Sound / Light / All)
		builder.setDefaults(Notification.DEFAULT_ALL);
		// set the notification to be automatically cancelled when pressed on
		builder.setAutoCancel(true); // still need to set content intent in-order for this to work..
		// define action when "deleting" (swiping / clearing) the notification
		Intent deleteIntent = new Intent(context, Indicator.class);
		deleteIntent.setAction("Delete intent");
		deleteIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, deleteIntent, 0);
		builder.setDeleteIntent(pendingIntent);
		builder.setContentIntent(pendingIntent);
		// when to show the notification?
		builder.setWhen(System.currentTimeMillis());
		// ticker text that runs in the tray for few seconds
		builder.setTicker(ticker);
		// set the title
		builder.setContentTitle(title);
		// only appears when specifically collapsed / on devices which don't have notification extending
		builder.setContentText(contentText);
		styled.setSummaryText(contentText);
		// the drawable icon
		builder.setSmallIcon(R.drawable.indicator);
		// set priority (for API level 16 devices)
		builder.setPriority(Notification.PRIORITY_DEFAULT);
		// add action button to a notification
		return builder;
	}

}
