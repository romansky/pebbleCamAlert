package com.fun.radarpusht;

import android.content.Context;
import android.os.Environment;
import com.fun.radarpusht.utils.Decompress;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created: 6/22/13 7:18 PM
 */
public class CamsKmzFileParser {

	private static final String localKmz = "cams.kmz";

	public static List<CameraData> parseKMZ(Context context){

		String fileContent = Decompress.fileContentsFromZip(context, localKmz, "doc.kml");
		InputStream ens = null;
		try {
			ens = context.getAssets().open("english_names.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader enReader = new BufferedReader(new InputStreamReader(ens));
		List<CameraData> cameras = new ArrayList<CameraData>();

		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document camerasDoc = builder.parse(new ByteArrayInputStream(fileContent.getBytes()));
			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList nodes = (NodeList) xpath.evaluate("//Placemark", camerasDoc, XPathConstants.NODESET);

			for (Integer i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				CameraData camD = new CameraData(
						xpath.evaluate("name/text()", node, XPathConstants.STRING).toString(),
						xpath.evaluate("description/text()", node),
						enReader.readLine(),
						xpath.evaluate("Point/coordinates/text()", node).split(",")[0],
						xpath.evaluate("Point/coordinates/text()", node).split(",")[1]
				);
				cameras.add(camD);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return cameras;
	}

}
