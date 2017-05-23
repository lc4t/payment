package noumena.payment.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Post {
	
	public static String doPost(String path, String parm) throws Exception {
		String response = "";
		URL url = new URL(path);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true); 
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
		connection.setRequestProperty("Connection", "close");
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
		out.write(parm);
		out.flush();
		out.close();
		
		InputStream in = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		while ((line = reader.readLine()) != null) {
			response += line;
		}
		
		in.close();
		connection.disconnect();
		return response;
	}
	

}
