package noumena.payment.cmgc;

import java.math.BigInteger;

/**
 * 
 * @author Emile
 * 
 */
public class NumbericUtils {
	// 任意进制转换为10进制
	private static BigInteger toDecimal(String input, int base) {
		BigInteger Bigtemp = BigInteger.ZERO, temp = BigInteger.ONE;
		int len = input.length();
		for (int i = len - 1; i >= 0; i--) {
			if (i != len - 1)
				temp = temp.multiply(BigInteger.valueOf(base));
			int num = changeDec(input.charAt(i));
			Bigtemp = Bigtemp.add(temp.multiply(BigInteger.valueOf(num)));
		}
		// System.out.println(Bigtemp);
		return Bigtemp;
		// this.setToDecimalResult(Bigtemp);
	}

	// 十进制转换中把字符转换为数
	static int changeDec(char ch) {
		int num = 0;
		if (ch >= 'A' && ch <= 'Z')
			num = ch - 'A' + 10;
		else if (ch >= 'a' && ch <= 'z')
			num = ch - 'a' + 36;
		else
			num = ch - '0';
		return num;
	}

	// 数字转换为字符
	static char changToNum(BigInteger temp) {
		int n = temp.intValue();

		if (n >= 10 && n <= 35)
			return (char) (n - 10 + 'A');

		else if (n >= 36 && n <= 61)
			return (char) (n - 36 + 'a');

		else
			return (char) (n + '0');
	}

	// 十进制转换为任意进制
	private static String toAnyConversion(BigInteger Bigtemp, BigInteger base) {
		String ans = "";
		while (Bigtemp.compareTo(BigInteger.ZERO) != 0) {
			BigInteger temp = Bigtemp.mod(base);
			Bigtemp = Bigtemp.divide(base);
			char ch = changToNum(temp);
			ans = ch + ans;
		}
		return ans;
	}

	public static String anyToAny(String inputNum, int scouceBase, BigInteger targetBase) {
		toDecimal(inputNum, scouceBase);
		return toAnyConversion(toDecimal(inputNum, scouceBase), targetBase);
	}
}

