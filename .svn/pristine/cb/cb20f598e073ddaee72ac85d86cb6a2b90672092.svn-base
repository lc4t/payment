package noumena.payment.naver;

import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacUtil
{
  private static final String ALGORITHM = "HmacSHA1";
  private static final String UTF8 = "utf-8";
  private static final String MSGPAD = "msgpad=";
  private static final String MD = "&md=";
  private static final String QUESTION = "?";
  private static final String AMPERCENT = "&";
  private static final int MAX_MESSAGESIZE = 255;

  public static String getMessageDigest(String key, String message)
    throws Exception
  {
    Mac mac = getMac(key);
    return getMessageDigest(mac, message);
  }

  public static String getMessageDigest(Mac mac, String message)
  {
    byte[] rawHmac;
    synchronized (mac) {
      rawHmac = mac.doFinal(message.getBytes());
    }
    return Base64.encodeBase64(rawHmac);
  }

  public static Mac getMac(String key)
    throws NoSuchAlgorithmException, InvalidKeyException
  {
    SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
    Mac mac = Mac.getInstance("HmacSHA1");
    mac.init(signingKey);
    return mac;
  }

  public static String getMessage(String url, String msgpad)
  {
    String msgUrl = url.substring(0, Math.min(255, url.length()));

    StringBuilder sb = new StringBuilder();
    sb.setLength(0);
    sb.append(msgUrl).append(msgpad);

    return sb.toString();
  }

  public static String makeEncryptUrl(Mac mac, String url)
    throws Exception
  {
    StringBuilder sb = new StringBuilder();

    String curTime = String.valueOf(Calendar.getInstance().getTimeInMillis());
    String message = getMessage(url, curTime);

    String md = getMessageDigest(mac, message);
    md = URLEncoder.encode(md, "utf-8");

    sb.setLength(0);
    sb.append(url);

    if (url.contains("?"))
      sb.append("&");
    else {
      sb.append("?");
    }

    sb.append("msgpad=").append(curTime).append("&md=").append(md);
    return sb.toString();
  }
}
