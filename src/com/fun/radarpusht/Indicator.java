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
				11111,
				getSimple(
						ticker,
						title,
						context
				).build());
	}

	public static void hideServiceIndicator(Context context){
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
	}

	public static void showServiceIndicator(Context context){
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(22222, buildIndicator(context).build());
	}


	private static NotificationCompat.Builder getSimple(String ticker, String title, Context context) {

		String contentText = "click here to open app";

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		NotificationCompat.InboxStyle styled = new NotificationCompat.InboxStyle(builder);
		// set the default notification options (Vibrate / Sound / Light / All)
		builder.setDefaults(Notification.DEFAULT_ALL);
		// set the notification to be automatically cancelled when pressed on
		builder.setAutoCancel(true); // still need to set content intent in-order for this to work..
		Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.fun.radarpusht");
		PendingIntent pendingLaunchIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
		// define action when "deleting" (swiping / clearing) the notification
		Intent deleteIntent = new Intent(context, Indicator.class);
		deleteIntent.setAction("Delete intent");
		deleteIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingDeleteIntent = PendingIntent.getActivity(context, 0, deleteIntent, 0);
		builder.setDeleteIntent(pendingDeleteIntent);
		builder.setContentIntent(pendingLaunchIntent);
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
		builder.setPriority(Notification.PRIORITY_MAX);
		// add action button to a notification
		return builder;
	}



	private static NotificationCompat.Builder buildIndicator(Context context) {

		String contentText = "click here to open app";

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		NotificationCompat.InboxStyle styled = new NotificationCompat.InboxStyle(builder);
		// set the default notification options (Vibrate / Sound / Light / All)
		builder.setDefaults(Notification.DEFAULT_ALL);
		// set the notification to be automatically cancelled when pressed on
		builder.setOngoing(true);
		Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.fun.radarpusht");
		PendingIntent pendingLaunchIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
		// define action when "deleting" (swiping / clearing) the notification
		Intent deleteIntent = new Intent(context, Indicator.class);
		deleteIntent.setAction("Delete intent");
		deleteIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		builder.setContentIntent(pendingLaunchIntent);
		// when to show the notification?
		builder.setWhen(System.currentTimeMillis());
		// ticker text that runs in the tray for few seconds
		builder.setTicker("Pebble Cam Alert Service");
		// set the title
		builder.setContentTitle("Pebble Cam Alert - Servic Running");
		// only appears when specifically collapsed / on devices which don't have notification extending
		builder.setContentText(contentText);
		styled.setSummaryText(contentText);
		// the drawable icon
		builder.setSmallIcon(R.drawable.indicator);
		// set priority (for API level 16 devices)
		builder.setPriority(Notification.PRIORITY_LOW);
		// add action button to a notification
		return builder;
	}

}
