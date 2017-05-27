package com.travel.flybooking;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.travel.flybooking.support.CommonFunctions;
import com.travel.flybooking.support.RangeSeekBar;
import com.travel.flybooking.support.RangeSeekBar.OnRangeSeekBarChangeListener;
import com.travel.datahandler.FlightDataHandler;
import com.travel.flybooking.adapter.FlightResultInflator;
import com.travel.common_handlers.HttpHandler;
import com.travel.model.FlightResultItem;

public class FlightResultActivity extends Activity {

	private Locale myLocale;
	String strFlightType, strFromCode, strToCode;
	String strFromDate, strToDate;
	public static int adultCount = 1, childCount = 0, infantCount = 0, tripNo;
	boolean isRoundTrip = false;
	public static String flightClass = null;
	public static boolean blChild;

	TextView tvProgressText; // , tvCurrency;
	// static ListView lvFlightResult;
	ScrollView svResult;
	static LinearLayout llSort, llResult;
	static ArrayList<FlightResultItem> flightResultItem, groupedResult;
	ArrayList<FlightResultItem> flightResultItemsTemp, filteredResult;
	static ArrayList<String> arrayAirline, checkedAirlines;
	static ArrayList<String> arrayAirports, checkedAirports;

	String str_url = "";
	String main_url = "";

	HashMap<String, Boolean> map;
	Boolean blNonStop = false, blOneStop = false, blMultiStop = false;
	Boolean blSortPrice = true, blSortDep = false, blSortArrival = false,
			blSortDuration = false, blSortAirName = false;
	String strSortPriceType = "Low", strSortDepType = null,
			strSortArrivalType = null, strSortDurationType = null,
			strSortAirNameType = null;

	public static Activity activityResult;

	// filter & sort
	String str12a6aFromOut, str6a12pFromOut, str12p6pFromOut, str6p12aFromOut,
			str12a6aToOut, str6a12pToOut, str12p6pToOut, str6p12aToOut,
			str12a6aFromRet, str6a12pFromRet, str12p6pFromRet, str6p12aFromRet,
			str12a6aToRet, str6a12pToRet, str12p6pToRet, str6p12aToRet;

	Double filterMinPrice = 0.0, filterMaxPrice = 0.0;
	Long filterDepLow, filterDepHigh, filterArrLow, filterArrHigh,
			filterLayLow, filterLayHigh;
	Long filterMinDep, filterMaxDep, filterMinArr, filterMaxArr, filterMinLay,
			filterMaxLay;
	Boolean blPriceFilter = false, blDepTimeFilter = false,
			blArrTimeFilter = false, blStopFilter = false,
			blNameFilter = false, blLayoverFilter = false,
			blLayAirportFilter = false, blFilterRefundable = false,
			blFilterNonRefundable = false;
	Boolean blOutbound = true, blReturn = false, bl12a6aFrom = false,
			bl6a12pFrom = false, bl12p6pFrom = false, bl6p12aFrom = false,
			bl12a6aTo = false, bl6a12pTo = false, bl12p6pTo = false,
			bl6p12aTo = false, blCurr = false;
	Dialog dialogSort, loaderDialog, curr, dialogFilter;
	CommonFunctions cf;

	public static String strSessionId = null;
	public static String strDetails = null;
	public static Bundle bundle;
	public static FlightResultItem selectedFItem;
	String[] sortText, sortHeading;

	OnClickListener resultClicker;

	FlightDataHandler flightDataHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		activityResult = this;

		cf = new CommonFunctions(this);
		flightDataHandler = new FlightDataHandler();

		loadLocale();

		setContentView(R.layout.activity_search_result_flight);

		initialize();
		setHeader();

		if (CommonFunctions.modify) {
			strSessionId = CommonFunctions.strSearchId;
			flightResultItem.addAll(CommonFunctions.flighResult);
			// lvFlightResult.setAdapter(new FlightResultAdapter(
			// FlightResultActivity.this, flightResultItem, isRoundTrip));
			new FlightResultInflator(FlightResultActivity.this, groupedResult,
					isRoundTrip, llResult, resultClicker);
			setDefaultValues();
			CommonFunctions.modify = false;
			CommonFunctions.strSearchId = null;
			CommonFunctions.flighResult.clear();
			arrayAirline = new FlightDataHandler()
			.getAirlineList(groupedResult);
		} else {

			new backMethod().execute("");
		}

	}

	private void initialize() {
		// TODO Auto-generated method stub
		// lvFlightResult = (ListView) findViewById(R.id.lv_flight_result);

		// tvCurrency = (TextView) findViewById(R.id.tv_currency);
		// tvCurrency.setText(CommonFunctions.strCurrency);

		loaderDialog = new Dialog(FlightResultActivity.this,
				android.R.style.Theme_Translucent);
		loaderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		loaderDialog.getWindow().setGravity(Gravity.TOP);
		loaderDialog.setContentView(R.layout.dialog_loader);
		loaderDialog.setCancelable(false);

		// ((ImageView) loaderDialog.findViewById(R.id.iv_loader))
		// .setImageResource(R.drawable.flight_loader);

		tvProgressText = (TextView) loaderDialog
				.findViewById(R.id.tv_progress_text);
		tvProgressText.setText(getResources().getString(
				R.string.searching_loader_text));
		tvProgressText.setVisibility(View.VISIBLE);

		dialogSort = new Dialog(FlightResultActivity.this,
				android.R.style.Theme_Translucent);
		dialogSort.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogSort.setContentView(R.layout.dialog_sort);

		svResult = (ScrollView) findViewById(R.id.sv_result);
		llResult = (LinearLayout) findViewById(R.id.ll_result);
		llSort = (LinearLayout) dialogSort.findViewById(R.id.ll_sort_options);
		sortText = getResources().getStringArray(R.array.sort_items_flight);
		sortHeading = getResources().getStringArray(
				R.array.sort_item_flight_heading);

		flightResultItem = new ArrayList<FlightResultItem>();
		flightResultItemsTemp = new ArrayList<FlightResultItem>();
		groupedResult = new ArrayList<FlightResultItem>();
		arrayAirline = new ArrayList<String>();
		checkedAirlines = new ArrayList<String>();
		arrayAirports = new ArrayList<String>();
		checkedAirports = new ArrayList<String>();

		main_url = CommonFunctions.main_url + CommonFunctions.lang
				+ "/FlightApi/FlightSearchApi?";
		new Handler();

		resultClicker = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (blPriceFilter || blDepTimeFilter || blArrTimeFilter
						|| blNonStop || blOneStop || blMultiStop
						|| blNameFilter || blLayoverFilter || bl12a6aFrom
						|| bl6a12pFrom || bl12p6pFrom || bl6p12aFrom
						|| bl12a6aTo || bl6a12pTo || bl12p6pTo || bl6p12aTo
						|| blLayAirportFilter || blFilterRefundable
						|| blFilterNonRefundable) {

				} else {
					selectedFItem = flightResultItemsTemp.get(v.getId());
				}
				new backService().execute(selectedFItem);
			}
		};
	}

	private void setHeader() {
		// TODO Auto-generated method stub
		bundle = getIntent().getExtras();

		LinearLayout llHeader = (LinearLayout) findViewById(R.id.ll_header_city);
		TextView tvFlightDates = (TextView) findViewById(R.id.tv_date);

		str_url = getIntent().getExtras().getString("url", "");
		str_url = str_url + "&searchID=";
		strFlightType = bundle.getString("flight_type", "");
		flightClass = bundle.getString("class", "");
		tripNo = bundle.getInt("trip_nos");

		Resources resources = getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		int px = 5 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		LayoutParams llParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		llParams.setMargins(px, px, px, px);

		if (!strFlightType.equalsIgnoreCase("Multicity")) {

			strFromCode = bundle.getString("from1", "");
			strToCode = bundle.getString("to1", "");

			strFromDate = bundle.getString("departure_date", "");
			strDetails = strFromDate; // .substring(4, strFromDate.length());
			strToDate = "";

			if (strFlightType.equalsIgnoreCase("RoundTrip")) {
				strToDate = bundle.getString("arrival_date", "");
				strDetails = strDetails + " - " + strToDate; // .substring(4,
																// strToDate.length());
				isRoundTrip = true;
			}

			TextView tvFrom = new TextView(this);
			tvFrom.setText(strFromCode);
			tvFrom.setTypeface(Typeface.create("sans-serif-condensed",
					Typeface.NORMAL));
			tvFrom.setTextColor(Color.WHITE);
			tvFrom.setTextSize(16f);

			TextView tv = new TextView(this);
			tv.setLayoutParams(llParams);
			tv.setText(getResources().getString(R.string.to));
			tv.setTypeface(Typeface.create("sans-serif-condensed",
					Typeface.NORMAL));
			tv.setTextColor(Color.WHITE);
			tv.setTextSize(16f);

			TextView tvTo = new TextView(this);
			tvTo.setText(strToCode);
			tvTo.setTypeface(Typeface.create("sans-serif-condensed",
					Typeface.NORMAL));
			tvTo.setTextColor(Color.WHITE);
			tvTo.setTextSize(16f);

			llHeader.addView(tvFrom);
			llHeader.addView(tv);
			llHeader.addView(tvTo);

			int padding = 40 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
			((LinearLayout) findViewById(R.id.ll_header)).setPadding(0, 0,
					padding, 0);

		} else {
			String temp1, temp2, temp3, temp4;
			temp1 = bundle.getString("from1", "");
			temp2 = bundle.getString("to1", "");
			temp3 = bundle.getString("from2", "");
			temp4 = bundle.getString("to2", "");
			if (tripNo == 2) {

				TextView tvFrom1 = new TextView(this);
				tvFrom1.setText(temp1);
				tvFrom1.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvFrom1.setTextColor(Color.WHITE);
				tvFrom1.setTextSize(16f);

				TextView tv1 = new TextView(this);
				tv1.setLayoutParams(llParams);
				tv1.setText(getResources().getString(R.string.to));
				tv1.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tv1.setTextColor(Color.WHITE);
				tv1.setTextSize(16f);

				TextView tvTo1 = new TextView(this);
				tvTo1.setText(temp2 + ",");
				tvTo1.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvTo1.setTextColor(Color.WHITE);
				tvTo1.setTextSize(16f);

				TextView tvFrom2 = new TextView(this);
				tvFrom2.setLayoutParams(llParams);
				tvFrom2.setText(temp3);
				tvFrom2.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvFrom2.setTextColor(Color.WHITE);
				tvFrom2.setTextSize(16f);

				TextView tv2 = new TextView(this);
				tv2.setText(getResources().getString(R.string.to));
				tv2.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tv2.setTextColor(Color.WHITE);
				tv2.setTextSize(16f);

				TextView tvTo2 = new TextView(this);
				tvTo2.setLayoutParams(llParams);
				tvTo2.setText(temp4);
				tvTo2.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvTo2.setTextColor(Color.WHITE);
				tvTo2.setTextSize(16f);

				llHeader.addView(tvFrom1);
				llHeader.addView(tv1);
				llHeader.addView(tvTo1);
				llHeader.addView(tvFrom2);
				llHeader.addView(tv2);
				llHeader.addView(tvTo2);

				strDetails = bundle.getString("date1", "")
				// .substring(4,
				// bundle.getString("date1", "").length())
						+ " - " + bundle.getString("date2", "");
				// .substring(4,
				// bundle.getString("date1", "").length());

				int margin = 40 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
				((LinearLayout) findViewById(R.id.ll_header)).setPadding(0, 0,
						margin, 0);

			} else if (tripNo == 3) {

				px = 3 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
				llParams.setMargins(px, px, px, px);

				TextView tvFrom1 = new TextView(this);
				tvFrom1.setText(temp1);
				tvFrom1.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvFrom1.setTextColor(Color.WHITE);
				tvFrom1.setTextSize(15f);

				TextView tv1 = new TextView(this);
				tv1.setLayoutParams(llParams);
				tv1.setText(getResources().getString(R.string.to));
				tv1.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tv1.setTextColor(Color.WHITE);
				tv1.setTextSize(16f);

				TextView tvTo1 = new TextView(this);
				tvTo1.setText(temp2 + ",");
				tvTo1.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvTo1.setTextColor(Color.WHITE);
				tvTo1.setTextSize(15f);

				TextView tvFrom2 = new TextView(this);
				tvFrom2.setLayoutParams(llParams);
				tvFrom2.setText(temp3);
				tvFrom2.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvFrom2.setTextColor(Color.WHITE);
				tvFrom2.setTextSize(15f);

				TextView tv2 = new TextView(this);
				tv2.setText(getResources().getString(R.string.to));
				tv2.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tv2.setTextColor(Color.WHITE);
				tv2.setTextSize(16f);

				TextView tvTo2 = new TextView(this);
				tvTo2.setLayoutParams(llParams);
				tvTo2.setText(temp4 + ",");
				tvTo2.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvTo2.setTextColor(Color.WHITE);
				tvTo2.setTextSize(15f);

				TextView tvFrom3 = new TextView(this);
				tvFrom3.setText(bundle.getString("from3", ""));
				tvFrom3.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvFrom3.setTextColor(Color.WHITE);
				tvFrom3.setTextSize(15f);

				TextView tv3 = new TextView(this);
				tv3.setLayoutParams(llParams);
				tv3.setText(getResources().getString(R.string.to));
				tv3.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tv3.setTextColor(Color.WHITE);
				tv3.setTextSize(16f);

				TextView tvTo3 = new TextView(this);
				tvTo3.setText(bundle.getString("to3", ""));
				tvTo3.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvTo3.setTextColor(Color.WHITE);
				tvTo3.setTextSize(15f);

				llHeader.addView(tvFrom1);
				llHeader.addView(tv1);
				llHeader.addView(tvTo1);
				llHeader.addView(tvFrom2);
				llHeader.addView(tv2);
				llHeader.addView(tvTo2);
				llHeader.addView(tvFrom3);
				llHeader.addView(tv3);
				llHeader.addView(tvTo3);

				strDetails = bundle.getString("date1", "")
				// .substring(4,
				// bundle.getString("date1", "").length())
						+ " - " + bundle.getString("date2", "")
						// .substring(4,
						// bundle.getString("date1", "").length())
						+ " - " + bundle.getString("date3", "");
				// .substring(4,
				// bundle.getString("date1", "").length());

				int margin = 15 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
				((LinearLayout) findViewById(R.id.ll_header)).setPadding(0, 0,
						margin, 0);

			} else {
				px = 2 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
				llParams.setMargins(px, px, px, px);

				TextView tvFrom1 = new TextView(this);
				tvFrom1.setText(temp1);
				tvFrom1.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvFrom1.setTextColor(Color.WHITE);
				tvFrom1.setTextSize(14f);

				TextView tv1 = new TextView(this);
				tv1.setLayoutParams(llParams);
				tv1.setText(getResources().getString(R.string.to));
				tv1.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tv1.setTextColor(Color.WHITE);
				tv1.setTextSize(16f);

				TextView tvTo1 = new TextView(this);
				tvTo1.setText(temp2 + ",");
				tvTo1.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvTo1.setTextColor(Color.WHITE);
				tvTo1.setTextSize(14f);

				TextView tvFrom2 = new TextView(this);
				tvFrom2.setLayoutParams(llParams);
				tvFrom2.setText(temp3);
				tvFrom2.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvFrom2.setTextColor(Color.WHITE);
				tvFrom2.setTextSize(14f);

				TextView tv2 = new TextView(this);
				tv2.setText(getResources().getString(R.string.to));
				tv2.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tv2.setTextColor(Color.WHITE);
				tv2.setTextSize(16f);

				TextView tvTo2 = new TextView(this);
				tvTo2.setLayoutParams(llParams);
				tvTo2.setText(temp4 + ",");
				tvTo2.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvTo2.setTextColor(Color.WHITE);
				tvTo2.setTextSize(14f);

				TextView tvFrom3 = new TextView(this);
				tvFrom3.setText(bundle.getString("from3", ""));
				tvFrom3.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvFrom3.setTextColor(Color.WHITE);
				tvFrom3.setTextSize(14f);

				TextView tv3 = new TextView(this);
				tv3.setLayoutParams(llParams);
				tv3.setText(getResources().getString(R.string.to));
				tv3.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tv3.setTextColor(Color.WHITE);
				tv3.setTextSize(16f);

				TextView tvTo3 = new TextView(this);
				tvTo3.setText(bundle.getString("to3", "") + ",");
				tvTo3.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvTo3.setTextColor(Color.WHITE);
				tvTo3.setTextSize(14f);

				TextView tvFrom4 = new TextView(this);
				tvFrom4.setLayoutParams(llParams);
				tvFrom4.setText(bundle.getString("from4", ""));
				tvFrom4.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvFrom4.setTextColor(Color.WHITE);
				tvFrom4.setTextSize(14f);

				TextView tv4 = new TextView(this);
				tv4.setText(getResources().getString(R.string.to));
				tv4.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tv4.setTextColor(Color.WHITE);
				tv4.setTextSize(16f);

				TextView tvTo4 = new TextView(this);
				tvTo4.setLayoutParams(llParams);
				tvTo4.setText(bundle.getString("to4", ""));
				tvTo4.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvTo4.setTextColor(Color.WHITE);
				tvTo4.setTextSize(14f);

				llHeader.addView(tvFrom1);
				llHeader.addView(tv1);
				llHeader.addView(tvTo1);
				llHeader.addView(tvFrom2);
				llHeader.addView(tv2);
				llHeader.addView(tvTo2);
				llHeader.addView(tvFrom3);
				llHeader.addView(tv3);
				llHeader.addView(tvTo3);
				llHeader.addView(tvFrom4);
				llHeader.addView(tv4);
				llHeader.addView(tvTo4);

				strDetails = bundle.getString("date1", "")
				// .substring(4,
				// bundle.getString("date1", "").length())
						+ " - " + bundle.getString("date2", "")
						// .substring(4,
						// bundle.getString("date1", "").length())
						+ " - " + bundle.getString("date3", "")
						// .substring(4,
						// bundle.getString("date1", "").length())
						+ " - " + bundle.getString("date4", "");
				// .substring(4,
				// bundle.getString("date1", "").length());

				int margin = 10 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
				((LinearLayout) findViewById(R.id.ll_header)).setPadding(0, 0,
						margin, 0);
			}

		}
		adultCount = getIntent().getIntExtra("adult_count", 1);
		childCount = getIntent().getIntExtra("child_count", 0);
		infantCount = getIntent().getIntExtra("infant_count", 0);

		blChild = childCount > 0 ? true : false;

		strDetails = strDetails + ", " + adultCount
				+ getResources().getString(R.string.adult);
		strDetails = childCount > 0 ? strDetails + ", " + childCount
				+ getResources().getString(R.string.children) : strDetails;
		strDetails = infantCount > 0 ? strDetails + ", " + infantCount
				+ getResources().getString(R.string.infant) : strDetails;

		tvFlightDates.setText(strDetails);

	}

	private class backMethod extends AsyncTask<String, String, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			groupedResult.clear();
			flightResultItem.clear();
			flightResultItemsTemp.clear();
			arrayAirline.clear();
			checkedAirlines.clear();
			loaderDialog.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (params[0].isEmpty()) {
				String resultString = makePostRequest(false, "");
				if (resultString != null)
					parseFlightResult(resultString);
			} else if (!params[0].equalsIgnoreCase(CommonFunctions.strCurrency)) {
				String resultString = makePostRequest(true, params[0]);
				if (resultString != null) {
					JSONObject jObj;
					try {
						CommonFunctions.strCurrency = params[0];
						jObj = new JSONObject(resultString);
						jObj = jObj.getJSONObject("data");
						parseFlightResult(jObj.getString("Item"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (groupedResult.size() > 0) {
				blCurr = false;
				// tvCurrency.setText(CommonFunctions.strCurrency);
				new FlightResultInflator(FlightResultActivity.this,
						groupedResult, isRoundTrip, llResult, resultClicker);
				// lvFlightResult.setAdapter(new FlightResultAdapter(
				// FlightResultActivity.this, groupedResult, isRoundTrip));
				setDefaultValues();
				svResult.scrollTo(0, 0);
				System.out
						.println("------------------Finished displaying-------------");
			} else {
				((LinearLayout) findViewById(R.id.ll_filter)).setEnabled(false);
			}
			if (loaderDialog.isShowing())
				loaderDialog.dismiss();
		}
	}

	public void setDefaultValues() {

		flightResultItemsTemp.addAll(groupedResult);

		double[] tempPrice = flightDataHandler.getMinMaxPrice(groupedResult);
		// setting selected filter vales
		filterMinPrice = tempPrice[0];
		filterMaxPrice = tempPrice[1];

		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa",
				new Locale("en"));
		Calendar cal = Calendar.getInstance();

		try {
			cal.setTime(dateFormat.parse("12:00 AM"));
			filterDepLow = cal.getTimeInMillis();
			filterArrLow = cal.getTimeInMillis();

			cal.setTime(dateFormat.parse("11:59 PM"));
			filterDepHigh = cal.getTimeInMillis();
			filterArrHigh = cal.getTimeInMillis();

		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String[] tempArray = flightDataHandler
				.getTimigsArrayOutbound24(groupedResult);
		str12a6aFromOut = tempArray[0];
		str6a12pFromOut = tempArray[1];
		str12p6pFromOut = tempArray[2];
		str6p12aFromOut = tempArray[3];
		str12a6aToOut = tempArray[4];
		str6a12pToOut = tempArray[5];
		str12p6pToOut = tempArray[6];
		str6p12aToOut = tempArray[7];

		if (strFlightType.equalsIgnoreCase("RoundTrip")) {

			tempArray = flightDataHandler.getTimigsArrayReturn24(groupedResult);
			str12a6aFromRet = tempArray[0];
			str6a12pFromRet = tempArray[1];
			str12p6pFromRet = tempArray[2];
			str6p12aFromRet = tempArray[3];
			str12a6aToRet = tempArray[4];
			str6a12pToRet = tempArray[5];
			str12p6pToRet = tempArray[6];
			str6p12aToRet = tempArray[7];
		}

		long[] temp = flightDataHandler.getMinMaxLayover(groupedResult);
		filterLayLow = temp[0];
		filterLayHigh = temp[1];

		map = flightDataHandler.getStopDetails(groupedResult);

		bl12a6aFrom = false;
		bl6a12pFrom = false;
		bl12p6pFrom = false;
		bl6p12aFrom = false;
		bl12a6aTo = false;
		bl6a12pTo = false;
		bl12p6pTo = false;
		bl6p12aTo = false;

		filterMinDep = filterDepLow;
		filterMaxDep = filterDepHigh;
		filterMinArr = filterArrLow;
		filterMaxArr = filterArrHigh;
		filterMinLay = filterLayLow;
		filterMaxLay = filterLayHigh;

	}

	public void clicker(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			finish();
			break;
		case R.id.ll_sort:
			showSortDialog();
			break;
		case R.id.ll_filter:
			showFilterDialog();
			break;
		 case R.id.ll_modify:
			 finish();
		 break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}

	private String makePostRequest(boolean blCurr, String strCurrency) {

		// making POST request.
		try {

			String requestUrl = null;
			if (blCurr) {
				requestUrl = CommonFunctions.main_url + CommonFunctions.lang
						+ "/FlightApi/CurrencyConvert?currency=" + strCurrency
						+ "&searchID=" + strSessionId;
			} else {
				strSessionId = CommonFunctions.getRandomString(6) + "_";
				str_url = str_url.replace("{0}", CommonFunctions.strCurrency);
				requestUrl = main_url + str_url + strSessionId;
			}

			String resultString = new HttpHandler().makeServiceCall(requestUrl);

			System.out.println("------------------Received result-------------"
					+ resultString);
			return resultString;
		} catch (SocketException e) {
			// TODO: handle exception
			e.printStackTrace();
			handler.sendEmptyMessage(1);
		} catch (NullPointerException e) {
			// Log exception
			e.printStackTrace();
			handler.sendEmptyMessage(3);
		} catch (IOException e) {
			// Log exception
			e.printStackTrace();
			handler.sendEmptyMessage(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handler.sendEmptyMessage(2);
		}
		return null;
	}

	private void parseFlightResult(String result) {
		try {

			if (result != null) {
				JSONArray jarray = null;
				// Parse String to JSON object
				jarray = new JSONArray(result);

				if (jarray.length() == 0) {
					handler.sendEmptyMessage(3);
				} else {
					flightResultItem = flightDataHandler
							.parseFlightResult(result);
					groupedResult = flightDataHandler.groupThreeResult(flightResultItem);
					
					arrayAirports = new FlightDataHandler()
							.getLayoverAirlineList(groupedResult);

					arrayAirline = new FlightDataHandler()
							.getAirlineList(groupedResult);

					int length = jarray.length();
					System.out.println("Result count = " + length);

				}

			}
			System.out
					.println("------------------Parsing finished-------------");

		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
			if (blCurr)
				handler.sendEmptyMessage(4);
			else
				handler.sendEmptyMessage(3);
		} catch (NullPointerException e) {
			e.printStackTrace();
			if (blCurr)
				handler.sendEmptyMessage(4);
			else
				handler.sendEmptyMessage(3);
		} catch (Exception e) {
			e.printStackTrace();
			if (blCurr)
				handler.sendEmptyMessage(4);
			else
				handler.sendEmptyMessage(2);
		}
	}

	private void showFilterDialog() {

		final Dialog dialogFilter = new Dialog(this,
				android.R.style.Theme_Translucent);
		dialogFilter.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogFilter.getWindow().setGravity(Gravity.TOP);
		dialogFilter.setContentView(R.layout.dialog_filter_flight);
		dialogFilter.setCancelable(false);

		final RelativeLayout rlBack;
		final LinearLayout llTabPrice, llTabStops, llTabAirline, llTabAirports, llTabTime;
		final ImageView ivTabPrice, ivTabStops, ivTabAirline, ivTabAirports, ivTabTime;
		final TextView tvTabPrice, tvTabStops, tvTabAirline, tvTabAirports, tvTabTime;
		final LinearLayout llPriceLayout, llStopsLayout, llAirlineLayout, llAirportLayout, llTimeLayout;
		final LinearLayout llPriceBar, llLayover;
		final TextView tvPriceMin, tvPriceMax, tvLayoverMin, tvLayoverMax, tvRefund, tvNonRefund, tvOneStop, tvNonStop, tvMultistop;

		final LinearLayout llArlineList, llAirportList;
		final LinearLayout llTabOutbound, llTabReturn;
		final LinearLayout ll12A6AFrom, ll6A12PFrom, ll12P6PFrom, ll6P12AFrom, ll12A6ATo, ll6A12PTo, ll12P6PTo, ll6P12Ato;
		final CheckBox cbTabOutbound, cbTabReturn; // cbCheckAll

		final LinearLayout llReset, llApply;

		rlBack = (RelativeLayout) dialogFilter.findViewById(R.id.rl_back);

		llTabPrice = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_tab_price);
		llTabStops = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_tab_stops);
		llTabAirline = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_tab_airline);
		llTabAirports = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_tab_airports);
		llTabTime = (LinearLayout) dialogFilter.findViewById(R.id.ll_tab_time);

		ivTabPrice = (ImageView) dialogFilter.findViewById(R.id.iv_tab_price);
		ivTabStops = (ImageView) dialogFilter.findViewById(R.id.iv_tab_stops);
		ivTabAirline = (ImageView) dialogFilter
				.findViewById(R.id.iv_tab_airline);
		ivTabAirports = (ImageView) dialogFilter
				.findViewById(R.id.iv_tab_airports);
		ivTabTime = (ImageView) dialogFilter.findViewById(R.id.iv_tab_time);

		tvTabPrice = (TextView) dialogFilter.findViewById(R.id.tv_tab_price);
		tvTabStops = (TextView) dialogFilter.findViewById(R.id.tv_tab_stops);
		tvTabAirline = (TextView) dialogFilter
				.findViewById(R.id.tv_tab_airline);
		tvTabAirports = (TextView) dialogFilter
				.findViewById(R.id.tv_tab_airports);
		tvTabTime = (TextView) dialogFilter.findViewById(R.id.tv_tab_time);

		llPriceLayout = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_price_layout);
		llStopsLayout = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_stops_layout);
		llAirlineLayout = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_airline_layout);
		llAirportLayout = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_layover_layout);
		llTimeLayout = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_timing_filter);

		llTabOutbound = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_tab_outbound);
		llTabReturn = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_tab_return);

		ll12A6AFrom = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_12a6a_from);
		ll6A12PFrom = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_6a12p_from);
		ll12P6PFrom = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_12p6p_from);
		ll6P12AFrom = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_6p12a_from);
		ll12A6ATo = (LinearLayout) dialogFilter.findViewById(R.id.ll_12a6a_to);
		ll6A12PTo = (LinearLayout) dialogFilter.findViewById(R.id.ll_6a12p_to);
		ll12P6PTo = (LinearLayout) dialogFilter.findViewById(R.id.ll_12p6p_to);
		ll6P12Ato = (LinearLayout) dialogFilter.findViewById(R.id.ll_6p12a_to);

		cbTabOutbound = (CheckBox) dialogFilter.findViewById(R.id.cb_outbound);
		cbTabReturn = (CheckBox) dialogFilter.findViewById(R.id.cb_return);

		llPriceBar = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_price_bar);
		tvPriceMin = (TextView) dialogFilter.findViewById(R.id.tv_range_min);
		tvPriceMax = (TextView) dialogFilter.findViewById(R.id.tv_range_max);

		tvRefund = (TextView) dialogFilter
				.findViewById(R.id.tv_filter_refundable);
		tvNonRefund = (TextView) dialogFilter
				.findViewById(R.id.tv_filter_non_refundable);

		llLayover = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_layover_bar);
		tvLayoverMin = (TextView) dialogFilter
				.findViewById(R.id.tv_layover_min);
		tvLayoverMax = (TextView) dialogFilter
				.findViewById(R.id.tv_layover_max);

		tvOneStop = (TextView) dialogFilter.findViewById(R.id.tv_one_stop);
		tvNonStop = (TextView) dialogFilter.findViewById(R.id.tv_non_stop);
		tvMultistop = (TextView) dialogFilter.findViewById(R.id.tv_multi_stop);

		// cbCheckAll = (CheckBox) dialogFilter.findViewById(R.id.cb_check_all);
		llArlineList = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_airline_list);
		llAirportList = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_layover_airport_list);

		llReset = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_reset_filter);

		llApply = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_apply_filter);

		llPriceBar.removeAllViews();
		llLayover.removeAllViews();
		llArlineList.removeAllViews();
		llAirportList.removeAllViews();

		blReturn = cbTabReturn.isChecked();
		blOutbound = cbTabOutbound.isChecked();

		final boolean bl12a6aFromTemp = bl12a6aFrom;
		final boolean bl6a12pFromTemp = bl6a12pFrom;
		final boolean bl12p6pFromTemp = bl12p6pFrom;
		final boolean bl6p12aFromTemp = bl6p12aFrom;

		final boolean bl12a6aToTemp = bl12a6aTo;
		final boolean bl6a12pToTemp = bl6a12pTo;
		final boolean bl12p6pToTemp = bl12p6pTo;
		final boolean bl6p12aToTemp = bl6p12aTo;

		final boolean blNonStopTmp = blNonStop, blOneStopTmp = blOneStop, blMultiStopTmp = blMultiStop;

		final boolean blPriceFilterTemp = blPriceFilter, blLayoverFilterTemp = blLayoverFilter, blNameFilterTemp = blNameFilter, blLayAirportFilterTemp = blLayAirportFilter;

		final boolean blFilterRefundableTemp = blFilterRefundable, blFilterNonRefundableTemp = blFilterNonRefundable;

		final Double minValuePrice = filterMinPrice, maxValuePrice = filterMaxPrice;
		final Long minDep = filterMinDep, maxDep = filterMaxDep, minArr = filterMinArr, maxArr = filterMaxArr;
		final Long minLay = filterMinLay, maxLay = filterMaxLay;

		if (strFlightType.equalsIgnoreCase("Multicity"))
			llTabTime.setVisibility(View.GONE);
		else if (strFlightType.equalsIgnoreCase("OneWay")) {
			((LinearLayout) dialogFilter
					.findViewById(R.id.ll_timing_filter_header))
					.setVisibility(View.GONE);
		}

		if (bl12a6aFrom || bl6a12pFrom || bl12p6pFrom || bl6p12aFrom
				|| bl12a6aTo || bl6a12pTo || bl12p6pTo || bl6p12aTo
				|| blNonStop || blOneStop || blMultiStop || blPriceFilter
				|| blLayoverFilter || blNameFilter || blLayAirportFilter
				|| blFilterRefundable || blFilterNonRefundable)
			llReset.setBackgroundResource(R.drawable.buttonshape);
		else
			llReset.setBackgroundColor(Color.TRANSPARENT);

		((TextView) dialogFilter.findViewById(R.id.tv_filter_from))
				.setText(strFromCode);
		((TextView) dialogFilter.findViewById(R.id.tv_filter_to))
				.setText(strToCode);

		OnClickListener tabClicker = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.ll_tab_price:
					llPriceLayout.setVisibility(View.VISIBLE);
					llStopsLayout.setVisibility(View.GONE);
					llAirlineLayout.setVisibility(View.GONE);
					llAirportLayout.setVisibility(View.GONE);
					llTimeLayout.setVisibility(View.GONE);

					tvTabPrice.setTextColor(Color.parseColor("#E6F36000"));
					tvTabStops.setTextColor(Color.BLACK);
					tvTabAirline.setTextColor(Color.BLACK);
					tvTabAirports.setTextColor(Color.BLACK);
					tvTabTime.setTextColor(Color.BLACK);

					ivTabPrice.setColorFilter(Color.parseColor("#E6F36000"));
					ivTabStops.setColorFilter(Color.BLACK);
					ivTabAirline.setColorFilter(Color.BLACK);
					ivTabAirports.setColorFilter(Color.BLACK);
					ivTabTime.setColorFilter(Color.BLACK);

					tvRefund.setBackgroundColor(blFilterRefundable ? Color
							.parseColor("#E0E0E0") : Color.TRANSPARENT);
					tvNonRefund
							.setBackgroundColor(blFilterNonRefundable ? Color
									.parseColor("#E0E0E0") : Color.TRANSPARENT);

					break;

				case R.id.ll_tab_stops:
					llPriceLayout.setVisibility(View.GONE);
					llStopsLayout.setVisibility(View.VISIBLE);
					llAirlineLayout.setVisibility(View.GONE);
					llAirportLayout.setVisibility(View.GONE);
					llTimeLayout.setVisibility(View.GONE);

					tvTabPrice.setTextColor(Color.BLACK);
					tvTabStops.setTextColor(Color.parseColor("#E6F36000"));
					tvTabAirline.setTextColor(Color.BLACK);
					tvTabAirports.setTextColor(Color.BLACK);
					tvTabTime.setTextColor(Color.BLACK);

					ivTabPrice.setColorFilter(Color.BLACK);
					ivTabStops.setColorFilter(Color.parseColor("#E6F36000"));
					ivTabAirline.setColorFilter(Color.BLACK);
					ivTabAirports.setColorFilter(Color.BLACK);
					ivTabTime.setColorFilter(Color.BLACK);

					tvNonStop.setBackgroundColor(blNonStop ? Color
							.parseColor("#E0E0E0") : Color.TRANSPARENT);
					tvOneStop.setBackgroundColor(blOneStop ? Color
							.parseColor("#E0E0E0") : Color.TRANSPARENT);
					tvMultistop.setBackgroundColor(blMultiStop ? Color
							.parseColor("#E0E0E0") : Color.TRANSPARENT);

					break;

				case R.id.ll_tab_airline:
					llPriceLayout.setVisibility(View.GONE);
					llStopsLayout.setVisibility(View.GONE);
					llAirlineLayout.setVisibility(View.VISIBLE);
					llAirportLayout.setVisibility(View.GONE);
					llTimeLayout.setVisibility(View.GONE);

					tvTabPrice.setTextColor(Color.BLACK);
					tvTabStops.setTextColor(Color.BLACK);
					tvTabAirline.setTextColor(Color.parseColor("#E6F36000"));
					tvTabAirports.setTextColor(Color.BLACK);
					tvTabTime.setTextColor(Color.BLACK);

					ivTabPrice.setColorFilter(Color.BLACK);
					ivTabStops.setColorFilter(Color.BLACK);
					ivTabAirline.setColorFilter(Color.parseColor("#E6F36000"));
					ivTabAirports.setColorFilter(Color.BLACK);
					ivTabTime.setColorFilter(Color.BLACK);

					break;

				case R.id.ll_tab_airports:
					llPriceLayout.setVisibility(View.GONE);
					llStopsLayout.setVisibility(View.GONE);
					llAirlineLayout.setVisibility(View.GONE);
					llAirportLayout.setVisibility(View.VISIBLE);
					llTimeLayout.setVisibility(View.GONE);

					tvTabPrice.setTextColor(Color.BLACK);
					tvTabStops.setTextColor(Color.BLACK);
					tvTabAirline.setTextColor(Color.BLACK);
					tvTabAirports.setTextColor(Color.parseColor("#E6F36000"));
					tvTabTime.setTextColor(Color.BLACK);

					ivTabPrice.setColorFilter(Color.BLACK);
					ivTabStops.setColorFilter(Color.BLACK);
					ivTabAirline.setColorFilter(Color.BLACK);
					ivTabAirports.setColorFilter(Color.parseColor("#E6F36000"));
					ivTabTime.setColorFilter(Color.BLACK);

					break;

				case R.id.ll_tab_time:
					llPriceLayout.setVisibility(View.GONE);
					llStopsLayout.setVisibility(View.GONE);
					llAirlineLayout.setVisibility(View.GONE);
					llAirportLayout.setVisibility(View.GONE);
					llTimeLayout.setVisibility(View.VISIBLE);

					tvTabPrice.setTextColor(Color.BLACK);
					tvTabStops.setTextColor(Color.BLACK);
					tvTabAirline.setTextColor(Color.BLACK);
					tvTabAirports.setTextColor(Color.BLACK);
					tvTabTime.setTextColor(Color.parseColor("#E6F36000"));

					ivTabPrice.setColorFilter(Color.BLACK);
					ivTabStops.setColorFilter(Color.BLACK);
					ivTabAirline.setColorFilter(Color.BLACK);
					ivTabAirports.setColorFilter(Color.BLACK);
					ivTabTime.setColorFilter(Color.parseColor("#E6F36000"));

					ll12A6AFrom.setBackgroundColor(bl12a6aFrom ? Color
							.parseColor("#E0E0E0") : Color.TRANSPARENT);
					ll6A12PFrom.setBackgroundColor(bl6a12pFrom ? Color
							.parseColor("#E0E0E0") : Color.TRANSPARENT);
					ll12P6PFrom.setBackgroundColor(bl12p6pFrom ? Color
							.parseColor("#E0E0E0") : Color.TRANSPARENT);
					ll6P12AFrom.setBackgroundColor(bl6p12aFrom ? Color
							.parseColor("#E0E0E0") : Color.TRANSPARENT);
					ll12A6ATo.setBackgroundColor(bl12a6aTo ? Color
							.parseColor("#E0E0E0") : Color.TRANSPARENT);
					ll6A12PTo.setBackgroundColor(bl6a12pTo ? Color
							.parseColor("#E0E0E0") : Color.TRANSPARENT);
					ll12P6PTo.setBackgroundColor(bl12p6pTo ? Color
							.parseColor("#E0E0E0") : Color.TRANSPARENT);
					ll6P12Ato.setBackgroundColor(bl6p12aTo ? Color
							.parseColor("#E0E0E0") : Color.TRANSPARENT);

					break;
				}
			}
		};

		llTabPrice.setOnClickListener(tabClicker);
		llTabStops.setOnClickListener(tabClicker);
		llTabAirline.setOnClickListener(tabClicker);
		llTabAirports.setOnClickListener(tabClicker);
		llTabTime.setOnClickListener(tabClicker);

		OnClickListener clikcr = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {

				case R.id.ll_tab_outbound:
					if (!cbTabOutbound.isChecked()) {
						cbTabOutbound.setChecked(true);
						cbTabReturn.setChecked(false);

						((TextView) dialogFilter
								.findViewById(R.id.tv_filter_from))
								.setText(strFromCode);
						((TextView) dialogFilter
								.findViewById(R.id.tv_filter_to))
								.setText(strToCode);

					}
					break;

				case R.id.ll_tab_return:
					if (!cbTabReturn.isChecked()) {
						cbTabReturn.setChecked(true);
						cbTabOutbound.setChecked(false);

						((TextView) dialogFilter
								.findViewById(R.id.tv_filter_from))
								.setText(strToCode);
						((TextView) dialogFilter
								.findViewById(R.id.tv_filter_to))
								.setText(strFromCode);
					}
					break;

				case R.id.ll_12a6a_from:
					if (bl12a6aFrom) {
						bl12a6aFrom = false;
					} else {
						bl12a6aFrom = true;
						bl6a12pFrom = false;
						bl12p6pFrom = false;
						bl6p12aFrom = false;
					}
					break;

				case R.id.ll_6a12p_from:
					if (bl6a12pFrom) {
						bl6a12pFrom = false;
					} else {
						bl12a6aFrom = false;
						bl6a12pFrom = true;
						bl12p6pFrom = false;
						bl6p12aFrom = false;
					}
					break;

				case R.id.ll_12p6p_from:
					if (bl12p6pFrom) {
						bl12p6pFrom = false;
					} else {
						bl12a6aFrom = false;
						bl6a12pFrom = false;
						bl12p6pFrom = true;
						bl6p12aFrom = false;
					}
					break;

				case R.id.ll_6p12a_from:
					if (bl6p12aFrom) {
						bl6p12aFrom = false;
					} else {
						bl12a6aFrom = false;
						bl6a12pFrom = false;
						bl12p6pFrom = false;
						bl6p12aFrom = true;
					}
					break;

				case R.id.ll_12a6a_to:
					if (bl12a6aTo) {
						bl12a6aTo = false;
					} else {
						bl12a6aTo = true;
						bl6a12pTo = false;
						bl12p6pTo = false;
						bl6p12aTo = false;
					}
					break;

				case R.id.ll_6a12p_to:
					if (bl6a12pTo) {
						bl6a12pTo = false;
					} else {
						bl12a6aTo = false;
						bl6a12pTo = true;
						bl12p6pTo = false;
						bl6p12aTo = false;
					}
					break;

				case R.id.ll_12p6p_to:
					if (bl12p6pTo) {
						bl12p6pTo = false;
					} else {
						bl12a6aTo = false;
						bl6a12pTo = false;
						bl12p6pTo = true;
						bl6p12aTo = false;
					}
					break;

				case R.id.ll_6p12a_to:
					if (bl6p12aTo) {
						bl6p12aTo = false;
					} else {
						bl12a6aTo = false;
						bl6a12pTo = false;
						bl12p6pTo = false;
						bl6p12aTo = true;
					}
					break;

				default:
					break;
				}

				ll12A6AFrom.setBackgroundColor(bl12a6aFrom ? Color
						.parseColor("#E0E0E0") : Color.TRANSPARENT);
				ll6A12PFrom.setBackgroundColor(bl6a12pFrom ? Color
						.parseColor("#E0E0E0") : Color.TRANSPARENT);
				ll12P6PFrom.setBackgroundColor(bl12p6pFrom ? Color
						.parseColor("#E0E0E0") : Color.TRANSPARENT);
				ll6P12AFrom.setBackgroundColor(bl6p12aFrom ? Color
						.parseColor("#E0E0E0") : Color.TRANSPARENT);
				ll12A6ATo.setBackgroundColor(bl12a6aTo ? Color
						.parseColor("#E0E0E0") : Color.TRANSPARENT);
				ll6A12PTo.setBackgroundColor(bl6a12pTo ? Color
						.parseColor("#E0E0E0") : Color.TRANSPARENT);
				ll12P6PTo.setBackgroundColor(bl12p6pTo ? Color
						.parseColor("#E0E0E0") : Color.TRANSPARENT);
				ll6P12Ato.setBackgroundColor(bl6p12aTo ? Color
						.parseColor("#E0E0E0") : Color.TRANSPARENT);

				if (bl12a6aFrom || bl6a12pFrom || bl12p6pFrom || bl6p12aFrom
						|| bl12a6aTo || bl6a12pTo || bl12p6pTo || bl6p12aTo
						|| blNonStop || blOneStop || blMultiStop
						|| blPriceFilter || blLayoverFilter || blNameFilter
						|| blLayAirportFilter || blFilterRefundable
						|| blFilterNonRefundable)
					llReset.setBackgroundResource(R.drawable.buttonshape);
				else
					llReset.setBackgroundColor(Color.TRANSPARENT);
			}
		};

		llTabOutbound.setOnClickListener(clikcr);
		llTabReturn.setOnClickListener(clikcr);
		ll12A6AFrom.setOnClickListener(clikcr);
		ll6A12PFrom.setOnClickListener(clikcr);
		ll12P6PFrom.setOnClickListener(clikcr);
		ll6P12AFrom.setOnClickListener(clikcr);
		ll12A6ATo.setOnClickListener(clikcr);
		ll6A12PTo.setOnClickListener(clikcr);
		ll12P6PTo.setOnClickListener(clikcr);
		ll6P12Ato.setOnClickListener(clikcr);

		if (!map.get("blHasNonStop"))
			tvNonStop.setVisibility(View.GONE);
		if (!map.get("blHasOneStop"))
			tvOneStop.setVisibility(View.GONE);
		if (!map.get("blHasMultStop"))
			tvMultistop.setVisibility(View.GONE);

		final RangeSeekBar<Double> priceBar = new RangeSeekBar<Double>(this);
		// Set the range
		priceBar.setRangeValues(
				(flightResultItem).get(0).getDoubleFlightPrice(),
				(flightResultItem).get(flightResultItem.size() - 1).getDoubleFlightPrice());
		priceBar.setSelectedMinValue(filterMinPrice);
		priceBar.setSelectedMaxValue(filterMaxPrice);
		String price = String.format(new Locale("en"), "%.3f", filterMinPrice);
		tvPriceMin.setText(CommonFunctions.strCurrency + " " + price);
		price = String.format(new Locale("en"), "%.3f", filterMaxPrice);
		tvPriceMax.setText(CommonFunctions.strCurrency + " " + price);

		final RangeSeekBar<Long> layoverBar = new RangeSeekBar<Long>(this);
		layoverBar.setRangeValues(filterLayLow, filterLayHigh);
		layoverBar.setSelectedMinValue(filterMinLay);
		layoverBar.setSelectedMaxValue(filterMaxLay);
		String minLayover = null, maxLayover = null;
		if (CommonFunctions.lang.equalsIgnoreCase("en")) {
			minLayover = filterMinLay / 60 % 24 + " Hrs :" + filterMinLay % 60
					+ " Mins";
			maxLayover = filterMaxLay / 60 % 24 + " Hrs :" + filterMaxLay % 60
					+ " Mins";
		} else {
			minLayover = filterMinLay / 60 % 24 + " ساعة :" + filterMinLay % 60
					+ " دقيقة";
			maxLayover = filterMaxLay / 60 % 24 + " ساعة :" + filterMaxLay % 60
					+ " دقيقة";
		}

		tvLayoverMin.setText(String.valueOf(minLayover));
		tvLayoverMax.setText(String.valueOf(maxLayover));

		priceBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Double>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
					Double minValue, Double maxValue) {
				// handle changed range values
				String price = String
						.format(new Locale("en"), "%.3f", minValue);
				tvPriceMin.setText(CommonFunctions.strCurrency + " " + price);
				price = String.format(new Locale("en"), "%.3f", maxValue);
				tvPriceMax.setText(CommonFunctions.strCurrency + " " + price);
				filterMinPrice = minValue;
				filterMaxPrice = maxValue;
				if (filterMinPrice.equals((flightResultItem).get(0).getDoubleFlightPrice())
						&& filterMaxPrice.equals((flightResultItem)
								.get(flightResultItem.size() - 1).getDoubleFlightPrice()))
					blPriceFilter = false;
				else
					blPriceFilter = true;

				if (bl12a6aFrom || bl6a12pFrom || bl12p6pFrom || bl6p12aFrom
						|| bl12a6aTo || bl6a12pTo || bl12p6pTo || bl6p12aTo
						|| blNonStop || blOneStop || blMultiStop
						|| blPriceFilter || blLayoverFilter || blNameFilter
						|| blLayAirportFilter || blFilterRefundable
						|| blFilterNonRefundable)
					llReset.setBackgroundResource(R.drawable.buttonshape);
				else
					llReset.setBackgroundColor(Color.TRANSPARENT);

			}
		});

		OnClickListener fundClicker = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.tv_filter_refundable:
					blFilterRefundable = blFilterRefundable ? false : true;
					break;
				case R.id.tv_filter_non_refundable:
					blFilterNonRefundable = blFilterNonRefundable ? false
							: true;
					break;
				default:
					break;
				}
				tvRefund.setBackgroundColor(blFilterRefundable ? Color
						.parseColor("#E0E0E0") : Color.TRANSPARENT);
				tvNonRefund.setBackgroundColor(blFilterNonRefundable ? Color
						.parseColor("#E0E0E0") : Color.TRANSPARENT);
				if (bl12a6aFrom || bl6a12pFrom || bl12p6pFrom || bl6p12aFrom
						|| bl12a6aTo || bl6a12pTo || bl12p6pTo || bl6p12aTo
						|| blNonStop || blOneStop || blMultiStop
						|| blPriceFilter || blLayoverFilter || blNameFilter
						|| blLayAirportFilter || blFilterRefundable
						|| blFilterNonRefundable)
					llReset.setBackgroundResource(R.drawable.buttonshape);
				else
					llReset.setBackgroundColor(Color.TRANSPARENT);
			}
		};
		tvRefund.setOnClickListener(fundClicker);
		tvNonRefund.setOnClickListener(fundClicker);

		layoverBar
				.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Long>() {
					@Override
					public void onRangeSeekBarValuesChanged(
							RangeSeekBar<?> bar, Long minValue, Long maxValue) {
						// handle changed range values
						String minlay = null, maxLay = null;
						if (CommonFunctions.lang.equalsIgnoreCase("en")) {
							minlay = minValue / 60 % 24 + " Hrs :" + minValue
									% 60 + " Mins";
							maxLay = maxValue / 60 % 24 + " Hrs :" + maxValue
									% 60 + " Mins";
						} else {
							minlay = minValue / 60 % 24 + " ساعة :" + minValue
									% 60 + " دقيقة";
							maxLay = maxValue / 60 % 24 + " ساعة :" + maxValue
									% 60 + " دقيقة";
						}

						tvLayoverMin.setText(String.valueOf(minlay));
						tvLayoverMax.setText(String.valueOf(maxLay));

						filterMinLay = minValue;
						filterMaxLay = maxValue;

						if (filterMinLay.equals(filterLayLow)
								&& filterMaxLay.equals(filterLayHigh))
							blLayoverFilter = false;
						else
							blLayoverFilter = true;

						if (bl12a6aFrom || bl6a12pFrom || bl12p6pFrom
								|| bl6p12aFrom || bl12a6aTo || bl6a12pTo
								|| bl12p6pTo || bl6p12aTo || blNonStop
								|| blOneStop || blMultiStop || blPriceFilter
								|| blLayoverFilter || blNameFilter
								|| blLayAirportFilter || blFilterRefundable
								|| blFilterNonRefundable)
							llReset.setBackgroundResource(R.drawable.buttonshape);
						else
							llReset.setBackgroundColor(Color.TRANSPARENT);
					}
				});

		priceBar.setNotifyWhileDragging(true);
		layoverBar.setNotifyWhileDragging(true);

		llPriceBar.addView(priceBar);
		llLayover.addView(layoverBar);

		if (arrayAirports.size() == 0) {
			llTabAirports.setVisibility(View.GONE);
			((LinearLayout) dialogFilter.findViewById(R.id.ll_layover))
					.setVisibility(View.GONE);
			((LinearLayout) dialogFilter.findViewById(R.id.ll_layover_airports))
					.setVisibility(View.GONE);
		}

		if (filterMinPrice == filterMaxPrice)
			priceBar.setEnabled(false);

		if (filterLayLow == filterLayHigh) {
			layoverBar.setEnabled(false);
		}

		OnClickListener stopListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.tv_one_stop:
					blOneStop = blOneStop ? false : true;
					break;
				case R.id.tv_non_stop:
					blNonStop = blNonStop ? false : true;
					break;
				case R.id.tv_multi_stop:
					blMultiStop = blMultiStop ? false : true;
					break;
				}
				tvNonStop.setBackgroundColor(blNonStop ? Color
						.parseColor("#E0E0E0") : Color.TRANSPARENT);
				tvOneStop.setBackgroundColor(blOneStop ? Color
						.parseColor("#E0E0E0") : Color.TRANSPARENT);
				tvMultistop.setBackgroundColor(blMultiStop ? Color
						.parseColor("#E0E0E0") : Color.TRANSPARENT);

				if (bl12a6aFrom || bl6a12pFrom || bl12p6pFrom || bl6p12aFrom
						|| bl12a6aTo || bl6a12pTo || bl12p6pTo || bl6p12aTo
						|| blNonStop || blOneStop || blMultiStop
						|| blPriceFilter || blLayoverFilter || blNameFilter
						|| blLayAirportFilter || blFilterRefundable
						|| blFilterNonRefundable)
					llReset.setBackgroundResource(R.drawable.buttonshape);
				else
					llReset.setBackgroundColor(Color.TRANSPARENT);
			}
		};

		tvNonStop.setOnClickListener(stopListener);
		tvOneStop.setOnClickListener(stopListener);
		tvMultistop.setOnClickListener(stopListener);

		if (arrayAirline.size() > 0) {
			for (int i = 0; i < arrayAirline.size(); ++i) {
				final View view = getLayoutInflater().inflate(
						R.layout.custom_check_box_list_item, null);
				((CheckBox) view.findViewById(R.id.cb_check_airline))
						.setClickable(false);
				((TextView) view.findViewById(R.id.tv_airline_name))
						.setText(arrayAirline.get(i).toString());
				if (checkedAirlines.contains(arrayAirline.get(i))) {
					view.setBackgroundColor(Color.parseColor("#E0E0E0"));
					((CheckBox) view.findViewById(R.id.cb_check_airline))
							.setChecked(true);
					// cbCheckAll.setChecked(true);
				}
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (((CheckBox) view
								.findViewById(R.id.cb_check_airline))
								.isChecked()) {
							((CheckBox) view
									.findViewById(R.id.cb_check_airline))
									.setChecked(false);
							view.setBackgroundColor(Color.TRANSPARENT);
						} else {
							((CheckBox) view
									.findViewById(R.id.cb_check_airline))
									.setChecked(true);
							view.setBackgroundColor(Color.parseColor("#E0E0E0"));
						}

						View view;
						for (int j = 0; j < arrayAirline.size(); ++j) {
							view = (View) llArlineList.findViewById(j);
							if (((CheckBox) view
									.findViewById(R.id.cb_check_airline))
									.isChecked()) {
								blNameFilter = true;
								llReset.setBackgroundResource(R.drawable.buttonshape);
								break;
							} else
								blNameFilter = false;
							if (bl12a6aFrom || bl6a12pFrom || bl12p6pFrom
									|| bl6p12aFrom || bl12a6aTo || bl6a12pTo
									|| bl12p6pTo || bl6p12aTo || blNonStop
									|| blOneStop || blMultiStop
									|| blPriceFilter || blLayoverFilter
									|| blNameFilter || blLayAirportFilter
									|| blFilterRefundable
									|| blFilterNonRefundable)
								llReset.setBackgroundResource(R.drawable.buttonshape);
							else
								llReset.setBackgroundColor(Color.TRANSPARENT);
						}
					}
				});
				view.setId(i);
				llArlineList.addView(view);
			}
		}

		if (arrayAirports.size() > 0) {
			for (int i = 0; i < arrayAirports.size(); ++i) {
				final View view = getLayoutInflater().inflate(
						R.layout.custom_check_box_list_item, null);
				((CheckBox) view.findViewById(R.id.cb_check_airline))
						.setClickable(false);
				((TextView) view.findViewById(R.id.tv_airline_name))
						.setText(arrayAirports.get(i).toString());
				if (checkedAirports.contains(arrayAirports.get(i))) {
					((CheckBox) view.findViewById(R.id.cb_check_airline))
							.setChecked(true);
					view.setBackgroundColor(Color.parseColor("#E0E0E0"));
				}
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (((CheckBox) view
								.findViewById(R.id.cb_check_airline))
								.isChecked()) {
							((CheckBox) view
									.findViewById(R.id.cb_check_airline))
									.setChecked(false);
							view.setBackgroundColor(Color.TRANSPARENT);
						} else {
							((CheckBox) view
									.findViewById(R.id.cb_check_airline))
									.setChecked(true);
							view.setBackgroundColor(Color.parseColor("#E0E0E0"));
						}

						View view;
						for (int j = 0; j < arrayAirports.size(); ++j) {
							view = (View) llAirportList.findViewById(j);
							if (((CheckBox) view
									.findViewById(R.id.cb_check_airline))
									.isChecked()) {
								blLayAirportFilter = true;
								llReset.setBackgroundResource(R.drawable.buttonshape);
								break;
							} else
								blLayAirportFilter = false;
							if (bl12a6aFrom || bl6a12pFrom || bl12p6pFrom
									|| bl6p12aFrom || bl12a6aTo || bl6a12pTo
									|| bl12p6pTo || bl6p12aTo || blNonStop
									|| blOneStop || blMultiStop
									|| blPriceFilter || blLayoverFilter
									|| blNameFilter || blLayAirportFilter
									|| blFilterRefundable
									|| blFilterNonRefundable)
								llReset.setBackgroundResource(R.drawable.buttonshape);
							else
								llReset.setBackgroundColor(Color.TRANSPARENT);
						}

					}
				});
				view.setId(i);
				llAirportList.addView(view);
			}
		}

		rlBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				blOneStop = blOneStopTmp;
				blNonStop = blNonStopTmp;
				blMultiStop = blMultiStopTmp;

				filterMinPrice = minValuePrice;
				filterMaxPrice = maxValuePrice;
				filterMinDep = minDep;
				filterMaxDep = maxDep;
				filterMinArr = minArr;
				filterMaxArr = maxArr;
				filterMinLay = minLay;
				filterMaxLay = maxLay;

				bl12a6aFrom = bl12a6aFromTemp;
				bl6a12pFrom = bl6a12pFromTemp;
				bl12p6pFrom = bl12p6pFromTemp;
				bl6p12aFrom = bl6p12aFromTemp;

				bl12a6aTo = bl12a6aToTemp;
				bl6a12pTo = bl6a12pToTemp;
				bl12p6pTo = bl12p6pToTemp;
				bl6p12aTo = bl6p12aToTemp;

				blPriceFilter = blPriceFilterTemp;
				blLayoverFilter = blLayoverFilterTemp;

				blNameFilter = blNameFilterTemp;
				blLayAirportFilter = blLayAirportFilterTemp;

				blFilterRefundable = blFilterRefundableTemp;
				blFilterNonRefundable = blFilterNonRefundableTemp;

				dialogFilter.dismiss();
			}
		});

		llApply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				blOutbound = cbTabOutbound.isChecked();
				blReturn = cbTabReturn.isChecked();

				checkedAirlines.clear();
				int i = 0;
				for (i = 0; i < arrayAirline.size(); ++i) {
					final View view = (View) llArlineList.findViewById(i);
					if (((CheckBox) view.findViewById(R.id.cb_check_airline))
							.isChecked())
						checkedAirlines.add(arrayAirline.get(i));
				}

				checkedAirports.clear();
				for (i = 0; i < arrayAirports.size(); ++i) {
					final View view = (View) llAirportList.findViewById(i);
					if (((CheckBox) view.findViewById(R.id.cb_check_airline))
							.isChecked())
						checkedAirports.add(arrayAirports.get(i));
				}

				blNameFilter = checkedAirlines.size() > 0 ? true : false;

				blLayAirportFilter = checkedAirports.size() > 0 ? true : false;

				new filter().execute();

				dialogFilter.dismiss();
			}
		});

		llReset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flightResultItemsTemp.clear();
				flightResultItemsTemp.addAll(groupedResult);

				filterMinPrice = (groupedResult).get(0).getDoubleFlightPrice();
				filterMaxPrice = (groupedResult).get(groupedResult.size() - 1).getDoubleFlightPrice();

				filterMinDep = filterDepLow;
				filterMaxDep = filterDepHigh;
				filterMinArr = filterArrLow;
				filterMaxArr = filterArrHigh;
				filterMinLay = filterLayLow;
				filterMaxLay = filterLayHigh;
				blOutbound = true;
				blReturn = false;
				bl12a6aFrom = false;
				bl6a12pFrom = false;
				bl12p6pFrom = false;
				bl6p12aFrom = false;
				bl12a6aTo = false;
				bl6a12pTo = false;
				bl12p6pTo = false;
				bl6p12aTo = false;
				blOneStop = false;
				blNonStop = false;
				blMultiStop = false;
				blPriceFilter = false;
				blDepTimeFilter = false;
				blArrTimeFilter = false;
				blLayoverFilter = false;
				blStopFilter = false;
				blNameFilter = false;
				blLayAirportFilter = false;
				blFilterRefundable = false;
				blFilterNonRefundable = false;
				checkedAirlines.clear();
				checkedAirports.clear();
				sortArrayList();

				dialogFilter.dismiss();

				// showFilterDialog();
			}
		});

		llTabPrice.performClick();
		dialogFilter.show();
	}

	private void showSortDialog() {
		llSort.removeAllViews();
		for (int i = 0; i < sortText.length; ++i) {
			final View view = getLayoutInflater().inflate(
					R.layout.item_sort_list, null);
			if (i % 2 == 0) {
				final TextView tvView = (TextView) getLayoutInflater().inflate(
						R.layout.tv_autocomplete, null);
				tvView.setText(sortHeading[i / 2]);
				llSort.addView(tvView);
			}
			final RadioButton rb = (RadioButton) view
					.findViewById(R.id.rb_sort_item);
			rb.setClickable(false);
			((TextView) view.findViewById(R.id.tv_sort_item_name))
					.setText(sortText[i]);

			switch (i) {
			case 0:
				rb.setChecked(blSortPrice
						&& strSortPriceType.equalsIgnoreCase("Low") ? true
						: false);
				break;
			case 1:
				rb.setChecked(blSortPrice
						&& strSortPriceType.equalsIgnoreCase("High") ? true
						: false);
				break;
			case 2:
				rb.setChecked(blSortDep
						&& strSortDepType.equalsIgnoreCase("Low") ? true
						: false);
				break;
			case 3:
				rb.setChecked(blSortDep
						&& strSortDepType.equalsIgnoreCase("High") ? true
						: false);
				break;
			case 4:
				rb.setChecked(blSortArrival
						&& strSortArrivalType.equalsIgnoreCase("Low") ? true
						: false);
				break;
			case 5:
				rb.setChecked(blSortArrival
						&& strSortArrivalType.equalsIgnoreCase("High") ? true
						: false);
				break;
			case 6:
				rb.setChecked(blSortDuration
						&& strSortDurationType.equalsIgnoreCase("Low") ? true
						: false);
				break;
			case 7:
				rb.setChecked(blSortDuration
						&& strSortDurationType.equalsIgnoreCase("High") ? true
						: false);
				break;
			case 8:
				rb.setChecked(blSortAirName
						&& strSortAirNameType.equalsIgnoreCase("Low") ? true
						: false);
				break;
			case 9:
				rb.setChecked(blSortAirName
						&& strSortAirNameType.equalsIgnoreCase("High") ? true
						: false);
				break;
			}

			view.setId(i);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!rb.isChecked()) {
						rb.setChecked(true);

						switch (view.getId()) {
						case 0:
							blSortPrice = true;
							blSortDep = false;
							blSortArrival = false;
							blSortDuration = false;
							blSortAirName = false;
							strSortPriceType = "Low";

							break;

						case 1:
							blSortPrice = true;
							blSortDep = false;
							blSortArrival = false;
							blSortDuration = false;
							blSortAirName = false;
							strSortPriceType = "High";
							break;

						case 2:
							blSortPrice = false;
							blSortDep = true;
							blSortArrival = false;
							blSortDuration = false;
							blSortAirName = false;
							strSortDepType = "Low";
							break;

						case 3:
							blSortPrice = false;
							blSortDep = true;
							blSortArrival = false;
							blSortDuration = false;
							blSortAirName = false;
							strSortDepType = "High";
							break;

						case 4:
							blSortPrice = false;
							blSortDep = false;
							blSortArrival = true;
							blSortDuration = false;
							blSortAirName = false;
							strSortArrivalType = "Low";
							break;

						case 5:
							blSortPrice = false;
							blSortDep = false;
							blSortArrival = true;
							blSortDuration = false;
							blSortAirName = false;
							strSortArrivalType = "High";
							break;

						case 6:
							blSortPrice = false;
							blSortDep = false;
							blSortArrival = false;
							blSortDuration = true;
							blSortAirName = false;
							strSortDurationType = "Low";
							break;

						case 7:
							blSortPrice = false;
							blSortDep = false;
							blSortArrival = false;
							blSortDuration = true;
							blSortAirName = false;
							strSortDurationType = "High";
							break;

						case 8:
							blSortPrice = false;
							blSortDep = false;
							blSortArrival = false;
							blSortDuration = false;
							blSortAirName = true;
							strSortAirNameType = "Low";
							break;

						case 9:
							blSortPrice = false;
							blSortDep = false;
							blSortArrival = false;
							blSortDuration = false;
							blSortAirName = true;
							strSortAirNameType = "High";
							break;

						default:
							break;
						}
						sortArrayList();
					}
					dialogSort.dismiss();
				}
			});
			llSort.addView(view);

		}
		dialogSort.show();

	}

	private void sortArrayList() {
		ArrayList<FlightResultItem> temp = new ArrayList<FlightResultItem>();
		if (blPriceFilter || blDepTimeFilter || blArrTimeFilter || blNonStop
				|| blOneStop || blMultiStop || blNameFilter || blLayoverFilter
				|| bl12a6aFrom || bl6a12pFrom || bl12p6pFrom || bl6p12aFrom
				|| bl12a6aTo || bl6a12pTo || bl12p6pTo || bl6p12aTo
				|| blLayAirportFilter || blFilterRefundable
				|| blFilterNonRefundable) {
			temp = flightDataHandler
					.sortFlightResult(filteredResult, blSortPrice, blSortDep,
							blSortArrival, blSortDuration, blSortAirName,
							strSortPriceType, strSortDepType,
							strSortArrivalType, strSortDurationType,
							strSortAirNameType);
		} else {
			temp = flightDataHandler
					.sortFlightResult(flightResultItemsTemp, blSortPrice,
							blSortDep, blSortArrival, blSortDuration,
							blSortAirName, strSortPriceType, strSortDepType,
							strSortArrivalType, strSortDurationType,
							strSortAirNameType);
		}

		new FlightResultInflator(FlightResultActivity.this, temp, isRoundTrip,
				llResult, resultClicker);
		svResult.scrollTo(0, 0);
	}

	private class filter extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			filteredResult = new ArrayList<FlightResultItem>();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub

			filteredResult = flightDataHandler.filterStops(
					flightResultItemsTemp, blNonStop, blOneStop, blMultiStop);

			if (!strFlightType.equalsIgnoreCase("Multicity"))
				filteredResult = flightDataHandler.filterFlightTiming24(
						filteredResult, blOutbound, blReturn, bl12a6aFrom,
						bl6a12pFrom, bl12p6pFrom, bl6p12aFrom, bl12a6aTo,
						bl6a12pTo, bl12p6pTo, bl6p12aTo, CommonFunctions.lang);

			if (blPriceFilter)
				filteredResult = flightDataHandler.filterPrice(filteredResult,
						filterMinPrice, filterMaxPrice);

			if (blDepTimeFilter)
				filteredResult = flightDataHandler.filterDepartTime24(
						filteredResult, filterMinDep, filterMaxDep,
						CommonFunctions.lang);

			if (blArrTimeFilter)
				filteredResult = flightDataHandler.filterArrivalTime24(
						filteredResult, filterMinArr, filterMaxArr,
						CommonFunctions.lang);

			if (blLayoverFilter)
				filteredResult = flightDataHandler.filterLayoverTime(
						filteredResult, filterMinLay, filterMaxLay);

			if (blNameFilter)
				filteredResult = flightDataHandler.filterAirlines(
						filteredResult, checkedAirlines);

			if (blLayAirportFilter)
				filteredResult = flightDataHandler.filterLayoverAirports(
						filteredResult, checkedAirports);

			if (blFilterRefundable || blFilterNonRefundable)
				filteredResult = flightDataHandler.filterRefundType(
						filteredResult, blFilterRefundable,
						blFilterNonRefundable);

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (filteredResult.size() > 0)
				sortArrayList();
			else {
				llResult.removeAllViews();
				// lvFlightResult.setAdapter(null);
				noResultAlert(true, getResources()
						.getString(R.string.no_result));
			}
			super.onPostExecute(result);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		groupedResult.clear();
		flightResultItem.clear();
		flightResultItemsTemp.clear();
		arrayAirline.clear();
		checkedAirlines.clear();
	}

	public class backService extends AsyncTask<FlightResultItem, Void, String> {

		FlightResultItem fItem;
		String sessionResult = "";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// loaderDialog.show();
			// tvProgressText.setText(getResources().getString(
			// R.string.checking_flight));
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(FlightResultItem... params) {
			// TODO Auto-generated method stub
			String reqUrl = null;
			try {
				reqUrl = CommonFunctions.main_url
						+ "en/FlightApi/AvailResult?tripId="
						+ params[0].getStrTripId() + "&searchID=" + strSessionId;
				sessionResult = new HttpHandler().makeServiceCall(reqUrl);
				System.out.println("result" + sessionResult);
				JSONObject json = new JSONObject(sessionResult);
				sessionResult = json.getString("Status");
				if (sessionResult.equalsIgnoreCase("true"))
					fItem = params[0];

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				// urlConnection.disconnect();
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return params[0].getStrDeepLink();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			// if (loaderDialog.isShowing())
			// loaderDialog.dismiss();
			if (sessionResult.equalsIgnoreCase("true")) {

				Intent details = new Intent(FlightResultActivity.this,
						FlightPaxActivity.class);
//				details.putExtra("sessionID", strSessionId);
				details.putExtra("isRound", isRoundTrip);
				details.putExtra("sID",
						strSessionId + CommonFunctions.getRandomString(4));
				// details.putExtra("url", result);
				// details.putExtra("type", "flight");
				details.putExtra("tripID", selectedFItem.getStrTripId());
				startActivity(details);
			} else {
				tvProgressText.setText(getResources().getString(
						R.string.flight_expired));
				new backMethod().execute("");
			}
			super.onPostExecute(result);
		}

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				if (loaderDialog.isShowing())
					loaderDialog.dismiss();
				noResultAlert(false,
						"There is a problem on your Network. Please try again later.");

			} else if (msg.what == 2) {

				if (loaderDialog.isShowing())
					loaderDialog.dismiss();
				noResultAlert(false,
						"There is a problem on your application. Please report it.");

			} else if (msg.what == 3) {
				if (loaderDialog.isShowing())
					loaderDialog.dismiss();
				noResultAlert(false,
						getResources().getString(R.string.no_result));
			} else if (msg.what == 4) {
				if (loaderDialog.isShowing())
					loaderDialog.dismiss();
				noResultAlert(false,
						"Something went wrong. Please try again later");
			}

		}
	};

	public void noResultAlert(final boolean filter, String msg) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		alertDialog.setMessage(msg);

		if (filter)
			alertDialog.setPositiveButton(
					getResources().getString(R.string.reset_filter),
					new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							flightResultItemsTemp.clear();
							flightResultItemsTemp.addAll(groupedResult);

							filterMinPrice = (groupedResult).get(0).getDoubleFlightPrice();
							filterMaxPrice = (groupedResult).get(groupedResult
									.size() - 1).getDoubleFlightPrice();

							filterMinDep = filterDepLow;
							filterMaxDep = filterDepHigh;
							filterMinArr = filterArrLow;
							filterMaxArr = filterArrHigh;
							filterMinLay = filterLayLow;
							filterMaxLay = filterLayHigh;
							blOutbound = true;
							blReturn = false;
							bl12a6aFrom = false;
							bl6a12pFrom = false;
							bl12p6pFrom = false;
							bl6p12aFrom = false;
							bl12a6aTo = false;
							bl6a12pTo = false;
							bl12p6pTo = false;
							bl6p12aTo = false;
							blOneStop = false;
							blNonStop = false;
							blMultiStop = false;
							blPriceFilter = false;
							blDepTimeFilter = false;
							blArrTimeFilter = false;
							blLayoverFilter = false;
							blStopFilter = false;
							blNameFilter = false;
							blLayAirportFilter = false;
							blFilterRefundable = false;
							blFilterNonRefundable = false;
							checkedAirlines.clear();
							checkedAirports.clear();
							sortArrayList();
						}
					});

		else {
			alertDialog.setPositiveButton(
					getResources().getString(R.string.ok),
					new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							if (!blCurr)
								finish();
						}
					});
		}
		alertDialog.setCancelable(false);
		alertDialog.show();
	}

	// private void dialogCurrency() {
	// curr = new Dialog(FlightResultActivity.this,
	// android.R.style.Theme_Translucent);
	// curr.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// curr.getWindow().setGravity(Gravity.TOP);
	// curr.setContentView(R.layout.dialog_currency);
	// curr.setCancelable(false);
	//
	// final ImageView close;
	// final LinearLayout llKWD, llINR, llUSD, llQAR, llEUR, llAED, llSAR,
	// llIQD, llGBP;
	// final LinearLayout llGEL, llBHD, llOMR;
	// close = (ImageView) curr.findViewById(R.id.iv_close);
	// llKWD = (LinearLayout) curr.findViewById(R.id.ll_KWD);
	// llINR = (LinearLayout) curr.findViewById(R.id.ll_INR);
	// llUSD = (LinearLayout) curr.findViewById(R.id.LL_USD);
	// llQAR = (LinearLayout) curr.findViewById(R.id.ll_QAR);
	// llEUR = (LinearLayout) curr.findViewById(R.id.ll_EUR);
	// llAED = (LinearLayout) curr.findViewById(R.id.LL_AED);
	// llSAR = (LinearLayout) curr.findViewById(R.id.LL_SAR);
	// llIQD = (LinearLayout) curr.findViewById(R.id.ll_IQD);
	// llGBP = (LinearLayout) curr.findViewById(R.id.ll_GBP);
	// llGEL = (LinearLayout) curr.findViewById(R.id.ll_GEL);
	// llBHD = (LinearLayout) curr.findViewById(R.id.ll_BHD);
	// llOMR = (LinearLayout) curr.findViewById(R.id.ll_OMR);
	//
	// // default
	//
	// if (CommonFunctions.strCurrency.equalsIgnoreCase("KWD"))
	// llKWD.setBackgroundResource(R.drawable.border_with_background);
	// else if (CommonFunctions.strCurrency.equalsIgnoreCase("INR"))
	// llINR.setBackgroundResource(R.drawable.border_with_background);
	// else if (CommonFunctions.strCurrency.equalsIgnoreCase("USD"))
	// llUSD.setBackgroundResource(R.drawable.border_with_background);
	// else if (CommonFunctions.strCurrency.equalsIgnoreCase("QAR"))
	// llQAR.setBackgroundResource(R.drawable.border_with_background);
	// else if (CommonFunctions.strCurrency.equalsIgnoreCase("EUR"))
	// llEUR.setBackgroundResource(R.drawable.border_with_background);
	// else if (CommonFunctions.strCurrency.equalsIgnoreCase("AED"))
	// llAED.setBackgroundResource(R.drawable.border_with_background);
	// else if (CommonFunctions.strCurrency.equalsIgnoreCase("SAR"))
	// llSAR.setBackgroundResource(R.drawable.border_with_background);
	// else if (CommonFunctions.strCurrency.equalsIgnoreCase("IQD"))
	// llIQD.setBackgroundResource(R.drawable.border_with_background);
	// else if (CommonFunctions.strCurrency.equalsIgnoreCase("GBP"))
	// llGBP.setBackgroundResource(R.drawable.border_with_background);
	// else if (CommonFunctions.strCurrency.equalsIgnoreCase("GEL"))
	// llGEL.setBackgroundResource(R.drawable.border_with_background);
	// else if (CommonFunctions.strCurrency.equalsIgnoreCase("OMR"))
	// llOMR.setBackgroundResource(R.drawable.border_with_background);
	// else
	// llBHD.setBackgroundResource(R.drawable.border_with_background);
	//
	// close.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// curr.dismiss();
	// }
	// });
	//
	// llKWD.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// llKWD.setBackgroundResource(R.drawable.border_with_background);
	// llINR.setBackgroundResource(R.drawable.border);
	// llUSD.setBackgroundResource(R.drawable.border);
	// llQAR.setBackgroundResource(R.drawable.border);
	// llEUR.setBackgroundResource(R.drawable.border);
	// llAED.setBackgroundResource(R.drawable.border);
	// llSAR.setBackgroundResource(R.drawable.border);
	// llIQD.setBackgroundResource(R.drawable.border);
	// llGBP.setBackgroundResource(R.drawable.border);
	// llGEL.setBackgroundResource(R.drawable.border);
	// llBHD.setBackgroundResource(R.drawable.border);
	//
	// blCurr = true;
	// new backMethod().execute("KWD");
	//
	// // tvCurrency.setText(CommonFunctions.strCurrency);
	// curr.dismiss();
	// }
	// });
	//
	// llINR.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// llKWD.setBackgroundResource(R.drawable.border);
	// llINR.setBackgroundResource(R.drawable.border_with_background);
	// llUSD.setBackgroundResource(R.drawable.border);
	// llQAR.setBackgroundResource(R.drawable.border);
	// llEUR.setBackgroundResource(R.drawable.border);
	// llAED.setBackgroundResource(R.drawable.border);
	// llSAR.setBackgroundResource(R.drawable.border);
	// llIQD.setBackgroundResource(R.drawable.border);
	// llGBP.setBackgroundResource(R.drawable.border);
	// llGEL.setBackgroundResource(R.drawable.border);
	// llBHD.setBackgroundResource(R.drawable.border);
	//
	// blCurr = true;
	// new backMethod().execute("INR");
	// // CommonFunctions.strCurrency = "INR";
	// // tvCurrency.setText(CommonFunctions.strCurrency);
	// curr.dismiss();
	// }
	// });
	//
	// llUSD.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// llKWD.setBackgroundResource(R.drawable.border);
	// llINR.setBackgroundResource(R.drawable.border);
	// llUSD.setBackgroundResource(R.drawable.border_with_background);
	// llQAR.setBackgroundResource(R.drawable.border);
	// llEUR.setBackgroundResource(R.drawable.border);
	// llAED.setBackgroundResource(R.drawable.border);
	// llSAR.setBackgroundResource(R.drawable.border);
	// llIQD.setBackgroundResource(R.drawable.border);
	// llGBP.setBackgroundResource(R.drawable.border);
	// llGEL.setBackgroundResource(R.drawable.border);
	// llBHD.setBackgroundResource(R.drawable.border);
	//
	// blCurr = true;
	// new backMethod().execute("USD");
	// // CommonFunctions.strCurrency = "USD";
	// // tvCurrency.setText(CommonFunctions.strCurrency);
	// curr.dismiss();
	// }
	// });
	//
	// llQAR.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// llKWD.setBackgroundResource(R.drawable.border);
	// llINR.setBackgroundResource(R.drawable.border);
	// llUSD.setBackgroundResource(R.drawable.border);
	// llQAR.setBackgroundResource(R.drawable.border_with_background);
	// llEUR.setBackgroundResource(R.drawable.border);
	// llAED.setBackgroundResource(R.drawable.border);
	// llSAR.setBackgroundResource(R.drawable.border);
	// llIQD.setBackgroundResource(R.drawable.border);
	// llGBP.setBackgroundResource(R.drawable.border);
	// llGEL.setBackgroundResource(R.drawable.border);
	// llBHD.setBackgroundResource(R.drawable.border);
	//
	// blCurr = true;
	// new backMethod().execute("QAR");
	// // CommonFunctions.strCurrency = "QAR";
	// // tvCurrency.setText(CommonFunctions.strCurrency);
	// curr.dismiss();
	// }
	// });
	//
	// llEUR.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// llKWD.setBackgroundResource(R.drawable.border);
	// llINR.setBackgroundResource(R.drawable.border);
	// llUSD.setBackgroundResource(R.drawable.border);
	// llQAR.setBackgroundResource(R.drawable.border);
	// llEUR.setBackgroundResource(R.drawable.border_with_background);
	// llAED.setBackgroundResource(R.drawable.border);
	// llSAR.setBackgroundResource(R.drawable.border);
	// llIQD.setBackgroundResource(R.drawable.border);
	// llGBP.setBackgroundResource(R.drawable.border);
	// llGEL.setBackgroundResource(R.drawable.border);
	// llBHD.setBackgroundResource(R.drawable.border);
	//
	// blCurr = true;
	// new backMethod().execute("EUR");
	// // CommonFunctions.strCurrency = "EUR";
	// // tvCurrency.setText(CommonFunctions.strCurrency);
	// curr.dismiss();
	// }
	// });
	//
	// llAED.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// llKWD.setBackgroundResource(R.drawable.border);
	// llINR.setBackgroundResource(R.drawable.border);
	// llUSD.setBackgroundResource(R.drawable.border);
	// llQAR.setBackgroundResource(R.drawable.border);
	// llEUR.setBackgroundResource(R.drawable.border);
	// llAED.setBackgroundResource(R.drawable.border_with_background);
	// llSAR.setBackgroundResource(R.drawable.border);
	// llIQD.setBackgroundResource(R.drawable.border);
	// llGBP.setBackgroundResource(R.drawable.border);
	// llGEL.setBackgroundResource(R.drawable.border);
	// llBHD.setBackgroundResource(R.drawable.border);
	//
	// blCurr = true;
	// new backMethod().execute("AED");
	// // CommonFunctions.strCurrency = "AED";
	// // tvCurrency.setText(CommonFunctions.strCurrency);
	// curr.dismiss();
	// }
	// });
	//
	// llSAR.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// llKWD.setBackgroundResource(R.drawable.border);
	// llINR.setBackgroundResource(R.drawable.border);
	// llUSD.setBackgroundResource(R.drawable.border);
	// llQAR.setBackgroundResource(R.drawable.border);
	// llEUR.setBackgroundResource(R.drawable.border);
	// llAED.setBackgroundResource(R.drawable.border);
	// llSAR.setBackgroundResource(R.drawable.border_with_background);
	// llIQD.setBackgroundResource(R.drawable.border);
	// llGBP.setBackgroundResource(R.drawable.border);
	// llGEL.setBackgroundResource(R.drawable.border);
	// llBHD.setBackgroundResource(R.drawable.border);
	//
	// blCurr = true;
	// new backMethod().execute("SAR");
	// // CommonFunctions.strCurrency = "SAR";
	// // tvCurrency.setText(CommonFunctions.strCurrency);
	// curr.dismiss();
	// }
	// });
	//
	// llIQD.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// llKWD.setBackgroundResource(R.drawable.border);
	// llINR.setBackgroundResource(R.drawable.border);
	// llUSD.setBackgroundResource(R.drawable.border);
	// llQAR.setBackgroundResource(R.drawable.border);
	// llEUR.setBackgroundResource(R.drawable.border);
	// llAED.setBackgroundResource(R.drawable.border);
	// llSAR.setBackgroundResource(R.drawable.border);
	// llIQD.setBackgroundResource(R.drawable.border_with_background);
	// llGBP.setBackgroundResource(R.drawable.border);
	// llGEL.setBackgroundResource(R.drawable.border);
	// llBHD.setBackgroundResource(R.drawable.border);
	//
	// blCurr = true;
	// new backMethod().execute("IQD");
	// // CommonFunctions.strCurrency = "IQD";
	// // tvCurrency.setText(CommonFunctions.strCurrency);
	// curr.dismiss();
	// }
	// });
	//
	// llGBP.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// llKWD.setBackgroundResource(R.drawable.border);
	// llINR.setBackgroundResource(R.drawable.border);
	// llUSD.setBackgroundResource(R.drawable.border);
	// llQAR.setBackgroundResource(R.drawable.border);
	// llEUR.setBackgroundResource(R.drawable.border);
	// llAED.setBackgroundResource(R.drawable.border);
	// llSAR.setBackgroundResource(R.drawable.border);
	// llIQD.setBackgroundResource(R.drawable.border);
	// llGBP.setBackgroundResource(R.drawable.border_with_background);
	// llGEL.setBackgroundResource(R.drawable.border);
	// llBHD.setBackgroundResource(R.drawable.border);
	//
	// blCurr = true;
	// new backMethod().execute("GBP");
	// // CommonFunctions.strCurrency = "GBP";
	// // tvCurrency.setText(CommonFunctions.strCurrency);
	// curr.dismiss();
	// }
	// });
	//
	// llGEL.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// llKWD.setBackgroundResource(R.drawable.border);
	// llINR.setBackgroundResource(R.drawable.border);
	// llUSD.setBackgroundResource(R.drawable.border);
	// llQAR.setBackgroundResource(R.drawable.border);
	// llEUR.setBackgroundResource(R.drawable.border);
	// llAED.setBackgroundResource(R.drawable.border);
	// llSAR.setBackgroundResource(R.drawable.border);
	// llIQD.setBackgroundResource(R.drawable.border);
	// llGBP.setBackgroundResource(R.drawable.border);
	// llGEL.setBackgroundResource(R.drawable.border_with_background);
	// llBHD.setBackgroundResource(R.drawable.border);
	//
	// blCurr = true;
	// new backMethod().execute("GEL");
	// // CommonFunctions.strCurrency = "GEL";
	// // tvCurrency.setText(CommonFunctions.strCurrency);
	// curr.dismiss();
	// }
	// });
	//
	// llBHD.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// llKWD.setBackgroundResource(R.drawable.border);
	// llINR.setBackgroundResource(R.drawable.border);
	// llUSD.setBackgroundResource(R.drawable.border);
	// llQAR.setBackgroundResource(R.drawable.border);
	// llEUR.setBackgroundResource(R.drawable.border);
	// llAED.setBackgroundResource(R.drawable.border);
	// llSAR.setBackgroundResource(R.drawable.border);
	// llIQD.setBackgroundResource(R.drawable.border);
	// llGBP.setBackgroundResource(R.drawable.border);
	// llGEL.setBackgroundResource(R.drawable.border);
	// llBHD.setBackgroundResource(R.drawable.border_with_background);
	//
	// blCurr = true;
	// new backMethod().execute("BHD");
	// // CommonFunctions.strCurrency = "BHD";
	// // tvCurrency.setText(CommonFunctions.strCurrency);
	// curr.dismiss();
	// }
	// });
	//
	// llOMR.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// llKWD.setBackgroundResource(R.drawable.border);
	// llINR.setBackgroundResource(R.drawable.border);
	// llUSD.setBackgroundResource(R.drawable.border);
	// llQAR.setBackgroundResource(R.drawable.border);
	// llEUR.setBackgroundResource(R.drawable.border);
	// llAED.setBackgroundResource(R.drawable.border);
	// llSAR.setBackgroundResource(R.drawable.border);
	// llIQD.setBackgroundResource(R.drawable.border);
	// llGBP.setBackgroundResource(R.drawable.border);
	// llGEL.setBackgroundResource(R.drawable.border);
	// llBHD.setBackgroundResource(R.drawable.border);
	// llOMR.setBackgroundResource(R.drawable.border_with_background);
	//
	// blCurr = true;
	// new backMethod().execute("OMR");
	// // CommonFunctions.strCurrency = "OMR";
	// // tvCurrency.setText(CommonFunctions.strCurrency);
	// curr.dismiss();
	// }
	// });
	//
	// curr.show();
	//
	// }

	private void loadLocale() {
		// TODO Auto-generated method stub
		SharedPreferences sharedpreferences = this.getSharedPreferences(
				"CommonPrefs", Context.MODE_PRIVATE);
		String lang = sharedpreferences.getString("Language", "en");
		System.out.println("Default lang: " + lang);
		if (lang.equalsIgnoreCase("ar")) {
			myLocale = new Locale(lang);
			saveLocale(lang);
			Locale.setDefault(myLocale);
			android.content.res.Configuration config = new android.content.res.Configuration();
			config.locale = myLocale;
			this.getBaseContext()
					.getResources()
					.updateConfiguration(
							config,
							this.getBaseContext().getResources()
									.getDisplayMetrics());
			CommonFunctions.lang = "ar";
		} else {
			myLocale = new Locale(lang);
			saveLocale(lang);
			Locale.setDefault(myLocale);
			android.content.res.Configuration config = new android.content.res.Configuration();
			config.locale = myLocale;
			this.getBaseContext()
					.getResources()
					.updateConfiguration(
							config,
							this.getBaseContext().getResources()
									.getDisplayMetrics());
			CommonFunctions.lang = "en";
		}
	}

	public void saveLocale(String lang) {
		CommonFunctions.lang = lang;
		String langPref = "Language";
		SharedPreferences prefs = this.getSharedPreferences("CommonPrefs",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(langPref, lang);
		editor.commit();
	}

}
