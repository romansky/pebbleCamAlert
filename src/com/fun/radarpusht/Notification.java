package com.fun.radarpusht;

import android.content.Context;
import android.content.Intent;

import android.content.Intent;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;

/**
 * Created: 6/6/13 3:43 PM
 */
public class Notification {

	private static final String PEBBLE_INTENT_NAME = "com.getpebble.action.SEND_NOTIFICATION";

	public static void notifyPebble(Context context, String sender, String title,String body){

		final Intent i = new Intent(PEBBLE_INTENT_NAME);

		final Map data = new HashMap();
		data.put("title", title);
		data.put("body", body);
		final JSONObject jsonData = new JSONObject(data);
		final String notificationData = new JSONArray().put(jsonData).toString();

		i.putExtra("messageType", "PEBBLE_ALERT");
		i.putExtra("sender", sender);
		i.putExtra("notificationData", notificationData);

		Log.d(Notification.class.getSimpleName(), "About to send a modal alert to Pebble: " + notificationData);
		context.sendBroadcast(i);

	}



}
