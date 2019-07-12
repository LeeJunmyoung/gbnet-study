package kr.co.bookking.util;

import org.apache.commons.codec.digest.DigestUtils;

public class Md5PassWordUtil {

	public static String md5Encript(String input) {
		String md5Hex = DigestUtils
			      .md5Hex(input).toUpperCase();
		// ref site  : https://www.baeldung.com/java-md5
		return md5Hex;
	}
	
}
