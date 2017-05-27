package com.travel.common_handlers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import android.util.Log;
import android.webkit.CookieManager;

public class HttpHandler {

	public HttpHandler() {
	}

	public String makeServiceCall(String reqUrl) throws MalformedURLException,
			ProtocolException, IOException, Exception {
		// Method to request a api url
		
		String response = null;
		URL url = null;
		url = new URL(reqUrl);
		
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		String cookie = cookieManager.getCookie(url.toString());
		Log.i("url", url.toString());
		HttpURLConnection urlConnection = (HttpURLConnection) url
				.openConnection();
		// urlConnection.setReadTimeout(15000);
		urlConnection.setRequestProperty("Cookie", cookie);
		urlConnection.setConnectTimeout(25000);
		urlConnection.setRequestMethod("GET");

		// Get cookies from responses and save into the cookie manager
		List<String> cookieList = urlConnection.getHeaderFields().get(
				"Set-Cookie");
		if (cookieList != null) {
			for (String cookieTemp : cookieList) {
				cookieManager.setCookie(urlConnection.getURL().toString(),
						cookieTemp);
			}
		}

		InputStream in = new BufferedInputStream(
				urlConnection.getInputStream());
		response = convertStreamToString(in);
		return response;
	}

	public String makeServiceCallWithParams(String reqUrl, String urlParameters)
			throws MalformedURLException, ProtocolException, IOException,
			Exception {
		// Method to request a api url with params
		
		String response = null;
		URL url = null;
		url = new URL(reqUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(95000);
		conn.setConnectTimeout(10000);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");

		conn.setRequestProperty("Content-Length",
				"" + Integer.toString(urlParameters.getBytes().length));
		conn.setRequestProperty("Content-Language", "en-US");

		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);

		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		String cookie = cookieManager.getCookie(reqUrl);

		conn.setRequestProperty("Cookie", cookie);

		// Send request
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		// Get cookies from responses and save into the cookie manager
		List<String> cookieList = conn.getHeaderFields().get("Set-Cookie");
		if (cookieList != null) {
			for (String cookieTemp : cookieList) {
				cookieManager.setCookie(conn.getURL().toString(), cookieTemp);
			}
		}

		InputStream in = new BufferedInputStream(conn.getInputStream());
		response = convertStreamToString(in);

		return response;
	}

	private String convertStreamToString(InputStream is) {
		//To convert input stream to string
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}
}