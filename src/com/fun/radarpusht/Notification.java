package com.fun.radarpusht;

import android.content.Context;
import android.content.Intent;

import android.content.Intent;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created: 6/6/13 3:43 PM
 */
public class Notification {

	public static void notifyPebble(Context context, String sender, String title,String body){
		Notification.Builder nb = Notification.builder();
		nb.setSender("BRAZIL ASS");

		nb.setNotificationData("zTitle" ,"zBody");
		Intent intent = nb.build().createIntent();
		context.sendBroadcast(intent);
	}

	public static final String ACTION = "com.getpebble.action.SEND_NOTIFICATION";

	private final String messageType;
	private final String sender;
	private final String notificationData;

	private Notification(String messageType, String sender, String notificationData) {
		this.messageType = messageType;
		this.sender = sender;
		this.notificationData = notificationData;
	}

	private Intent createIntent() {
		Intent intent = new Intent(ACTION);
		intent.putExtra("messageType", messageType);
		intent.putExtra("sender", sender);
		intent.putExtra("notificationData", notificationData);
		return intent;
	}

	private static Builder builder() {
		return new Builder();
	}

	private static class Builder {

		private String messageType = "PEBBLE_ALERT";
		private String sender;
		private String notificationData = "{}";

		private Builder() {}

		public Notification build() {
			return new Notification(messageType, sender, notificationData);
		}

		/**
		 * @param messageType The only acceptable value documented on
		 * http://developer.getpebble.com/android/intents is {@code "PEBBLE_ALERT"}
		 */
		public Builder setMessageType(String messageType) {
			this.messageType = messageType;
			return this;
		}

		public Builder setSender(String sender) {
			this.sender = sender;
			return this;
		}

		public Builder setNotificationData(String title, String body) {

			JsonObject json = new JsonObject();
			json.addProperty("title", title);
			json.addProperty("body", body);

			JsonArray array = new JsonArray();
			array.add(json);

			notificationData = array.toString();
			return this;
		}
	}
}
