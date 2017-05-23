package noumena.payment.newmycard;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test
{

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException
	{
		 String result="";
  	   String regEx = "[\u4e00-\u9fa5]";
			String aString="1500%超值禮盒I";
			for(int i=0;i<aString.length();i++){
				String tempString=aString.substring(i, i+1);
			//	System.out.println(tempString);
				String endString=tempString;
			if(!Character.isLetterOrDigit(tempString.charAt(0))){
				endString=URLEncoder.encode(tempString,"UTF-8");
			}else{
				Pattern pat = Pattern.compile(regEx);
				Matcher matcher = pat.matcher(tempString);
				if (matcher.find()){
					endString=URLEncoder.encode(tempString,"UTF-8").toLowerCase();
				}
			}
			result=result+endString;
			}
			System.out.println(result);

	}

}
