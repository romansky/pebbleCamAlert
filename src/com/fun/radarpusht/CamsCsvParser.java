package com.fun.radarpusht;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created: 6/9/13 6:42 PM
 */
public class CamsCsvParser {

	public static List<CameraData> parseFile(Context context){
		try {
			InputStream is = context.getResources().getAssets().open("09_06_13_cams.csv");
			return parseStream(is);
		} catch (IOException e) {
			return null;
		}
	}

	public static List<CameraData> parseStream(InputStream is){

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line;
		List<CameraData> cdl = new ArrayList<CameraData>();
		try {
			// remove csv headers..
			while ((line = reader.readLine()) != null){
				String[] lineData = line.split(",");
				Boolean isDataLine = false;
				try { Double.parseDouble(lineData[0]); isDataLine = true; } catch (Exception ignore) {}
				if (lineData.length > 12 && isDataLine){
					cdl.add(
						new CameraData(
							lineData[11],
							lineData[12],
							lineData[1],
							lineData[2]
						)
					);
				}

			}
		} catch (Exception e) {
			Log.e(CamsCsvParser.class.getSimpleName(),"Error while reading lines from file",e);
			e.printStackTrace();
		}
		return cdl;
	}

}
