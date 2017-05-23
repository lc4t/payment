package noumena.payment.naver;

public class Base64
{
  private static final char[] INT_TO_BASE64 = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 
    'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 
    'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', 
    '6', '7', '8', '9', '+', '/' };

  public static String encodeBase64(byte[] src)
  {
    int aLen = src.length;
    int numFullGroups = aLen / 3;
    int numBytesInPartialGroup = aLen - 3 * numFullGroups;
    int resultLen = 4 * ((aLen + 2) / 3);
    StringBuffer result = new StringBuffer(resultLen);
    char[] intToAlpha = INT_TO_BASE64;

    int inCursor = 0;
    for (int i = 0; i < numFullGroups; i++) {
      int byte0 = src[(inCursor++)] & 0xFF;
      int byte1 = src[(inCursor++)] & 0xFF;
      int byte2 = src[(inCursor++)] & 0xFF;
      result.append(intToAlpha[(byte0 >> 2)]);
      result.append(intToAlpha[(byte0 << 4 & 0x3F | byte1 >> 4)]);
      result.append(intToAlpha[(byte1 << 2 & 0x3F | byte2 >> 6)]);
      result.append(intToAlpha[(byte2 & 0x3F)]);
    }

    if (numBytesInPartialGroup != 0) {
      int byte0 = src[(inCursor++)] & 0xFF;
      result.append(intToAlpha[(byte0 >> 2)]);
      if (numBytesInPartialGroup == 1) {
        result.append(intToAlpha[(byte0 << 4 & 0x3F)]);
        result.append("==");
      }
      else {
        int byte1 = src[(inCursor++)] & 0xFF;
        result.append(intToAlpha[(byte0 << 4 & 0x3F | byte1 >> 4)]);
        result.append(intToAlpha[(byte1 << 2 & 0x3F)]);
        result.append('=');
      }

    }

    return result.toString();
  }
}
