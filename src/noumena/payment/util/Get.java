package noumena.payment.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class Get {
	public static String  doGet(String path, String[] keys, String[] values) throws IOException{
		HttpClient client = new HttpClient();
		client.getParams().setBooleanParameter("http.protocol.expect-continue", false);   
//		System.out.println("Get "+path);
		GetMethod get = new GetMethod(path);
//		get.addRequestHeader("Accept", "application/json"); 
//		get.addRequestHeader("Connection", "close");
		for (int i = 0 ; i < keys.length ; i++)
		{
			get.addRequestHeader(keys[i], values[i]);
		}
		client.executeMethod(get);
		int statusCode = get.getStatusCode();
		
		System.out.println("doGet->" + path + "(" + statusCode + ")");
		return get.getResponseBodyAsString();
		
	}
	
	public static String  doPost(String path, String[] keys, String[] values) throws IOException{
		HttpClient client = new HttpClient();
		client.getParams().setBooleanParameter("http.protocol.expect-continue", false);   
//		System.out.println("Post "+path);
		PostMethod post = new PostMethod(path);
//		post.addRequestHeader("Accept", "application/json"); 
//		post.addRequestHeader("Connection", "close"); 
		for (int i = 0 ; i < keys.length ; i++)
		{
			post.addRequestHeader(keys[i], values[i]);
		}
		client.executeMethod(post);
		int statusCode = post.getStatusCode();

		System.out.println("doPost->" + path + "(" + statusCode + ")");
		return post.getResponseBodyAsString();
		
	}
	
	public static GetMethod getGetMethod(String path) throws IOException{
		HttpClient client = new HttpClient();
		client.getParams().setBooleanParameter("http.protocol.expect-continue", false);   
		GetMethod get = new GetMethod(path);
		get.addRequestHeader("Connection", "close"); 
		client.executeMethod(get);
		int statusCode = get.getStatusCode();
		
		if(statusCode != 200) return null;
		
		
		return get;
		
	}
	
	public static void doGetTest() throws IOException{
		HttpClient client = new HttpClient();
		client.getParams().setBooleanParameter("http.protocol.expect-continue", false);   
		GetMethod get = new GetMethod("http://appshopper.com/iphone/all");
		get.addRequestHeader("Connection", "close"); 
		client.executeMethod(get);
//		System.out.println(get.getResponseBodyAsString());
		//HtmlParser.parse(get.getResponseBodyAsString(),"","");
		
		System.out.println(get.getStatusCode());
		System.out.println("");
	}
	
	public static void main(String[] args){
//		String url = "http://ma.mkhoj.com/downloads/trackerV1?adv_id=5138b74c7eb940488c86ef98729516c1&udid=12345678901234565538&app_id=533075567&platform=iphone&timestamp=2012-07-18 10:07:17";
//		try {
//			String result = doGet(url);
////			getGetMethod(url);
//			System.out.println(result);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
}
