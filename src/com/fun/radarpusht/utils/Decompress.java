package com.fun.radarpusht.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author jon
 */
public class Decompress {

	public static String fileContentsFromZip(Context context, String zipFile, String fileName){
		return getFileFromZip(context, zipFile,fileName);
	}


	private static String getFileFromZip(Context context,String zipFile, String fileName) {
		String result = "";
		try  {
			InputStream fin = context.getAssets().open(zipFile);
			ZipInputStream zin = new ZipInputStream(fin);
			ZipEntry ze;
			while ((ze = zin.getNextEntry()) != null) {
				Log.v(Decompress.class.getSimpleName(), "Unzipping " + ze.getName());

				if(ze.isDirectory()) {
					Log.i(Decompress.class.getSimpleName(), "directories are currently not supporeted");
//					_dirChecker(ze.getName());
				} else {
					if (ze.getName().equals(fileName)){
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						byte[] buffer = new byte[1024];
						int count;
						while ((count = zin.read(buffer)) != -1) {
							baos.write(buffer, 0, count);
						}
						result = baos.toString("UTF-8");
					}
				}
			}
			zin.close();
		} catch(Exception e) {
			Log.e("Decompress", "getFileFromZip", e);
		}
		return result;
	}

}
