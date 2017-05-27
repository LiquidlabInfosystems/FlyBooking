package com.travel.common_handlers;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Random;

public class CommonFunctions {

	public CommonFunctions() {
	}

	public static String getRandomString(final int sizeOfRandomString) {
		// To generate random string of length : sizeOfRandomString
		final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnmABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final Random random = new Random();
		final StringBuilder sb = new StringBuilder(sizeOfRandomString);
		for (int i = 0; i < sizeOfRandomString; ++i)
			sb.append(ALLOWED_CHARACTERS.charAt(random
					.nextInt(ALLOWED_CHARACTERS.length())));
		return sb.toString();
	}


	public static String toHexString(byte[] bytes) {
		// Used to parse byte array and return as string
		StringBuilder sb = new StringBuilder(bytes.length * 2);

		Formatter formatter = new Formatter(sb);
		for (byte b : bytes) {
			formatter.format("%02x", b);
		}
		formatter.close();
		return sb.toString();
	}

	public static String SHA1Encrypt(String text)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// For encrypting a text
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] textBytes = text.getBytes("UTF-8");
		md.update(textBytes, 0, textBytes.length);
		byte[] sha1hash = md.digest();
		return toHexString(sha1hash);
	}

}
