package com.travel.flybooking;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.travel.flybooking.support.CommonFunctions;
import com.travel.flybooking.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.webkit.CookieManager;

public class SplashScreen extends Activity {

	private static int SPLASH_TIME_OUT = 2000;
	private Locale myLocale;
	CommonFunctions cd;
	boolean flag = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadLocale();
		setContentView(R.layout.activity_splash_screen);
		cd=new CommonFunctions(SplashScreen.this);
		
		if (cd.isConnectingToInternet())
			new backUpdateCheck().execute();
		else
			noInternetAlert();
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		
		if(flag) {
			flag = false;
			if (cd.isConnectingToInternet())
				new backUpdateCheck().execute();
			else
				noInternetAlert();
		}
		super.onRestart();
	}
	
	public void splash()
	{
		if(cd.isConnectingToInternet())
		{
			new Handler().postDelayed(new Runnable() {
				
	            /*
	             * Showing splash screen with a timer. This will be useful when you
	             * want to show case your app logo / company
	             */
	 
	            @Override
	            public void run() {
	                // This method will be executed once the timer is over
	                // Start your app main activity
	            	
	            	Intent i = new Intent(SplashScreen.this, MainActivity.class);
	                startActivity(i);
	                // close this activity
	                finish();
	            }
			}, SPLASH_TIME_OUT);
		}
		else
		{
			noInternetAlert();
		}
	}
	
	public void noInternetAlert()
	{
		AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.error_no_internet_title));

        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.error_no_internet_msg));

        // Setting OK Button
        alertDialog.setPositiveButton(getResources().getString(R.string.error_no_internet_settings), new AlertDialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//intent to move mobile settings
				dialog.dismiss();
				flag = true;
				startActivity(new Intent(Settings.ACTION_SETTINGS));
			}});
        alertDialog.setNegativeButton(getResources().getString(R.string.error_no_internet_close_app), new DialogInterface.OnClickListener() {
    		
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
    			// TODO Auto-generated method stub
    			dialog.dismiss();
    			finish();
    			Intent intent = new Intent(Intent.ACTION_MAIN);
    			intent.addCategory(Intent.CATEGORY_HOME);
    			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			startActivity(intent);
    			//System.exit(0);
    		}
    	});

        // Showing Alert Message
        alertDialog.setCancelable(false);
        alertDialog.show();
	}
	
	private void loadLocale() {
		// TODO Auto-generated method stub
		SharedPreferences sharedpreferences  = this.getSharedPreferences("CommonPrefs", Context.MODE_PRIVATE);
		String lang = sharedpreferences.getString("Language", "en");
		System.out.println("Default lang: "+lang);
		if(lang.equalsIgnoreCase("ar"))
		{
			myLocale = new Locale(lang);
			saveLocale(lang);
			Locale.setDefault(myLocale);
			android.content.res.Configuration config = new android.content.res.Configuration();
			config.locale = myLocale;
			this.getBaseContext().getResources().updateConfiguration(config, this.getBaseContext().getResources().getDisplayMetrics());
			CommonFunctions.lang = "ar";
		}
		else{
			myLocale = new Locale(lang);
			saveLocale(lang);
			Locale.setDefault(myLocale);
			android.content.res.Configuration config = new android.content.res.Configuration();
			config.locale = myLocale;
			this.getBaseContext().getResources().updateConfiguration(config, this.getBaseContext().getResources().getDisplayMetrics());
			CommonFunctions.lang = "en";
		}
	}
	
	public void saveLocale(String lang)
	{
		CommonFunctions.lang = lang;
		String langPref = "Language";
		SharedPreferences prefs = this.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(langPref, lang);
		editor.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}
	
	private class backUpdateCheck extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			CommonFunctions.strPhnNo = getResources().getString(R.string.contact_number);
			makePostRequest();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			splash();
			super.onPostExecute(result);
		}

	}

	HttpURLConnection urlConnection = null;

	private void makePostRequest() {
		// making POST request.
		try {
			URL url = new URL("http://www.flybooking.com/version_file.json");
			CookieManager cookieManager = CookieManager.getInstance();
			String cookie = cookieManager.getCookie(url.toString());
			Log.i("url", url.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Cookie", cookie);
			urlConnection.setConnectTimeout(15000);
			urlConnection.setRequestMethod("GET");
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			String resultString = convertStreamToString(in);
			urlConnection.disconnect();
			parseResult(resultString);
		} catch (SocketTimeoutException e) {
			// Log exception
			e.printStackTrace();
			urlConnection.disconnect();
		} catch (NullPointerException e) {
			// Log exception
			e.printStackTrace();
			urlConnection.disconnect();
		} catch (IOException e) {
			// Log exception
			e.printStackTrace();
			urlConnection.disconnect();
		}
	}

	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
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

	private void parseResult(String result) {
		// Parse String to JSON object
		try {
			JSONObject json = new JSONObject(result);
			String status = json.getString("version_android");

			String versionName = null;
			try {
				versionName = getPackageManager().getPackageInfo(
						getPackageName(), 0).versionName;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Double onlineVersion, appVersion;

			onlineVersion = Double.parseDouble(status);

			appVersion = Double.parseDouble(versionName);

			if (onlineVersion > appVersion) {
				CommonFunctions.updateApp = true;
			} else {
				CommonFunctions.updateApp = false;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
