package com.travel.flybooking.support;

import java.util.ArrayList;
import java.util.Random;

import com.travel.flybooking.R;
import com.travel.model.FlightResultItem;
import com.travel.model.HotelResultItem;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.Toast;

public class CommonFunctions {

	public static boolean updateApp;
	public static boolean loggedIn = false;
	public static String lang = "en";
	public static String strCurrency = "KWD";
	public static String strSearchId = null;
	private Context _context;
//	 public static String main_url = "http://flybooking.caxita.ca/";
	 public static String main_url = "http://113.193.253.130/flybooking/";
	// public static String main_url = "http://113.193.253.130/royalline/";
//	public static String main_url = "http://113.193.253.130/alastora/";
	// public static String main_url = "http://192.168.1.100/royalline/";
	// public static String main_url = "http://www.flybooking.com/";

	public static boolean modify = false;
	public static ArrayList<FlightResultItem> flighResult;
	public static ArrayList<HotelResultItem> HotelResult;

	public static String strPhnNo = null;

	public CommonFunctions(Context context) {
		this._context = context;
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null)
				if (info.getState() == NetworkInfo.State.CONNECTED)
					return true;
		}
		return false;
	}

	public void showToast(String msg) {
		Toast.makeText(_context, msg, Toast.LENGTH_SHORT).show();
	}

	public static String getRandomString(final int sizeOfRandomString) {
		final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnmABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final Random random = new Random();
		final StringBuilder sb = new StringBuilder(sizeOfRandomString);
		for (int i = 0; i < sizeOfRandomString; ++i)
			sb.append(ALLOWED_CHARACTERS.charAt(random
					.nextInt(ALLOWED_CHARACTERS.length())));
		return sb.toString();
	}

	public void showAlertDialog(String msg) {
		Builder alertDialog = new Builder(_context);
		alertDialog.setMessage(msg);
		alertDialog.setPositiveButton(
				_context.getResources().getString(R.string.ok), null);

		alertDialog.setCancelable(false);
		alertDialog.show();
	}

	public void noInternetAlert() {
		Builder alertDialog = new Builder(_context);

		// Setting Dialog Title
		alertDialog.setTitle(_context.getResources().getString(
				R.string.error_no_internet_title));

		// Setting Dialog Message
		alertDialog.setMessage(_context.getResources().getString(
				R.string.error_no_internet_msg));

		// Setting OK Button
		alertDialog.setPositiveButton(
				_context.getResources().getString(
						R.string.error_no_internet_settings),
				new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// intent to move mobile settings
						_context.startActivity(new Intent(
								Settings.ACTION_WIRELESS_SETTINGS));
					}
				});
		alertDialog.setNegativeButton(
				_context.getResources().getString(
						R.string.error_no_internet_close_app),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						// _context.
						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						_context.startActivity(intent);
						// System.exit(0);
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

}
