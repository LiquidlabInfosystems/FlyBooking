package com.travel.flybooking;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.travel.flybooking.support.CommonFunctions;
import com.travel.model.FlightResultItem;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FlightDetails extends Activity {

	private Locale myLocale;
	FlightResultItem fItem;
	LinearLayout llFlight_2, llFlight_3, llFlight_4;
	LinearLayout llFlightDetails_1, llFlightDetails_2, llFlightDetails_3,
			llFlightDetails_4;
	TextView tvPrice;
	Button btnBook;
	JSONArray jarray = null;
	String strSessionId = null;
	static Activity activityFlightDetails;
	boolean isGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activityFlightDetails = this;
		loadLocale();
		setContentView(R.layout.activity_flight_details);
		getActionBar().hide();
//		setHeader();

		strSessionId = getIntent().getExtras().getString("sessionID", "");
		isGroup = getIntent().getBooleanExtra("isGroup", false);
		fItem = new FlightResultItem();
		fItem = isGroup ? FlightResultGroupActivity.selectedFItem
				: FlightResultActivity.selectedFItem;

		jarray = fItem.getJarray();

		llFlight_2 = (LinearLayout) findViewById(R.id.ll_flight_2);
		llFlight_3 = (LinearLayout) findViewById(R.id.ll_flight_3);
		llFlight_4 = (LinearLayout) findViewById(R.id.ll_flight_4);

		llFlightDetails_1 = (LinearLayout) findViewById(R.id.ll_flight_details_1);
		llFlightDetails_2 = (LinearLayout) findViewById(R.id.ll_flight_details_2);
		llFlightDetails_3 = (LinearLayout) findViewById(R.id.ll_flight_details_3);
		llFlightDetails_4 = (LinearLayout) findViewById(R.id.ll_flight_details_4);

//		tvPrice = (TextView) findViewById(R.id.tv_price);
//
//		String price = String.format(new Locale("en"), "%.3f",
//				Double.parseDouble(fItem.strDisplayRate));
//		tvPrice.setText(CommonFunctions.strCurrency + " " + price);

		llFlightDetails_1.removeAllViews();

		parseRoundtripResult();

	}

//	private void setHeader() {
//		// TODO Auto-generated method stub
//		Bundle bundle = FlightResultActivity.bundle;
//
//		LinearLayout llHeader = (LinearLayout) findViewById(R.id.ll_header_city);
//		TextView tvFlightDates = (TextView) findViewById(R.id.tv_date);
//
//		String strFlightType = bundle.getString("flight_type", "");
//		int tripNo = bundle.getInt("trip_nos");
//
//		Resources resources = getResources();
//		DisplayMetrics metrics = resources.getDisplayMetrics();
//		int px = 5 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
//		LayoutParams llParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT);
//		llParams.setMargins(px, px, px, px);
//
//		if (!strFlightType.equalsIgnoreCase("Multicity")) {
//
//			String strFromCode = bundle.getString("from1", "");
//			String strToCode = bundle.getString("to1", "");
//
//			TextView tvFrom = new TextView(this);
//			tvFrom.setText(strFromCode);
//			tvFrom.setTypeface(Typeface.create("sans-serif-condensed",
//					Typeface.NORMAL));
//			tvFrom.setTextColor(Color.WHITE);
//			tvFrom.setTextSize(16f);
//
//			TextView tv = new TextView(this);
//			tv.setLayoutParams(llParams);
//			tv.setText(getResources().getString(R.string.to));
//			tv.setTypeface(Typeface.create("sans-serif-condensed",
//					Typeface.NORMAL));
//			tv.setTextColor(Color.WHITE);
//			tv.setTextSize(16f);
//
//			TextView tvTo = new TextView(this);
//			tvTo.setText(strToCode);
//			tvTo.setTypeface(Typeface.create("sans-serif-condensed",
//					Typeface.NORMAL));
//			tvTo.setTextColor(Color.WHITE);
//			tvTo.setTextSize(16f);
//
//			llHeader.addView(tvFrom);
//			llHeader.addView(tv);
//			llHeader.addView(tvTo);
//
//			int padding = 30 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
//			((LinearLayout) findViewById(R.id.ll_header)).setPadding(0, 0,
//					padding, 0);
//
//		} else {
//			String temp1, temp2, temp3, temp4;
//			temp1 = bundle.getString("from1", "");
//			temp2 = bundle.getString("to1", "");
//			temp3 = bundle.getString("from2", "");
//			temp4 = bundle.getString("to2", "");
//			if (tripNo == 2) {
//
//				TextView tvFrom1 = new TextView(this);
//				tvFrom1.setText(temp1);
//				tvFrom1.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tvFrom1.setTextColor(Color.WHITE);
//				tvFrom1.setTextSize(16f);
//
//				TextView tv1 = new TextView(this);
//				tv1.setLayoutParams(llParams);
//				tv1.setText(getResources().getString(R.string.to));
//				tv1.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tv1.setTextColor(Color.WHITE);
//				tv1.setTextSize(16f);
//
//				TextView tvTo1 = new TextView(this);
//				tvTo1.setText(temp2 + ",");
//				tvTo1.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tvTo1.setTextColor(Color.WHITE);
//				tvTo1.setTextSize(16f);
//
//				TextView tvFrom2 = new TextView(this);
//				tvFrom2.setLayoutParams(llParams);
//				tvFrom2.setText(temp3);
//				tvFrom2.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tvFrom2.setTextColor(Color.WHITE);
//				tvFrom2.setTextSize(16f);
//
//				TextView tv2 = new TextView(this);
//				tv2.setText(getResources().getString(R.string.to));
//				tv2.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tv2.setTextColor(Color.WHITE);
//				tv2.setTextSize(16f);
//
//				TextView tvTo2 = new TextView(this);
//				tvTo2.setLayoutParams(llParams);
//				tvTo2.setText(temp4);
//				tvTo2.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tvTo2.setTextColor(Color.WHITE);
//				tvTo2.setTextSize(16f);
//
//				llHeader.addView(tvFrom1);
//				llHeader.addView(tv1);
//				llHeader.addView(tvTo1);
//				llHeader.addView(tvFrom2);
//				llHeader.addView(tv2);
//				llHeader.addView(tvTo2);
//
//				int margin = 30 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
//				((LinearLayout) findViewById(R.id.ll_header)).setPadding(0, 0,
//						margin, 0);
//
//			} else if (tripNo == 3) {
//
//				px = 3 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
//				llParams.setMargins(px, px, px, px);
//
//				TextView tvFrom1 = new TextView(this);
//				tvFrom1.setText(temp1);
//				tvFrom1.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tvFrom1.setTextColor(Color.WHITE);
//				tvFrom1.setTextSize(15f);
//
//				TextView tv1 = new TextView(this);
//				tv1.setLayoutParams(llParams);
//				tv1.setText(getResources().getString(R.string.to));
//				tv1.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tv1.setTextColor(Color.WHITE);
//				tv1.setTextSize(16f);
//
//				TextView tvTo1 = new TextView(this);
//				tvTo1.setText(temp2 + ",");
//				tvTo1.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tvTo1.setTextColor(Color.WHITE);
//				tvTo1.setTextSize(15f);
//
//				TextView tvFrom2 = new TextView(this);
//				tvFrom2.setLayoutParams(llParams);
//				tvFrom2.setText(temp3);
//				tvFrom2.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tvFrom2.setTextColor(Color.WHITE);
//				tvFrom2.setTextSize(15f);
//
//				TextView tv2 = new TextView(this);
//				tv2.setText(getResources().getString(R.string.to));
//				tv2.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tv2.setTextColor(Color.WHITE);
//				tv2.setTextSize(16f);
//
//				TextView tvTo2 = new TextView(this);
//				tvTo2.setLayoutParams(llParams);
//				tvTo2.setText(temp4 + ",");
//				tvTo2.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tvTo2.setTextColor(Color.WHITE);
//				tvTo2.setTextSize(15f);
//
//				TextView tvFrom3 = new TextView(this);
//				tvFrom3.setText(bundle.getString("from3", ""));
//				tvFrom3.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tvFrom3.setTextColor(Color.WHITE);
//				tvFrom3.setTextSize(15f);
//
//				TextView tv3 = new TextView(this);
//				tv3.setLayoutParams(llParams);
//				tv3.setText(getResources().getString(R.string.to));
//				tv3.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tv3.setTextColor(Color.WHITE);
//				tv3.setTextSize(16f);
//
//				TextView tvTo3 = new TextView(this);
//				tvTo3.setText(bundle.getString("to3", ""));
//				tvTo3.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tvTo3.setTextColor(Color.WHITE);
//				tvTo3.setTextSize(15f);
//
//				llHeader.addView(tvFrom1);
//				llHeader.addView(tv1);
//				llHeader.addView(tvTo1);
//				llHeader.addView(tvFrom2);
//				llHeader.addView(tv2);
//				llHeader.addView(tvTo2);
//				llHeader.addView(tvFrom3);
//				llHeader.addView(tv3);
//				llHeader.addView(tvTo3);
//
//				int margin = 10 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
//				((LinearLayout) findViewById(R.id.ll_header)).setPadding(0, 0,
//						margin, 0);
//
//			} else {
//				px = 2 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
//				llParams.setMargins(px, px, px, px);
//
//				TextView tvFrom1 = new TextView(this);
//				tvFrom1.setText(temp1);
//				tvFrom1.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tvFrom1.setTextColor(Color.WHITE);
//				tvFrom1.setTextSize(14f);
//
//				TextView tv1 = new TextView(this);
//				tv1.setLayoutParams(llParams);
//				tv1.setText(getResources().getString(R.string.to));
//				tv1.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tv1.setTextColor(Color.WHITE);
//				tv1.setTextSize(16f);
//
//				TextView tvTo1 = new TextView(this);
//				tvTo1.setText(temp2 + ",");
//				tvTo1.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tvTo1.setTextColor(Color.WHITE);
//				tvTo1.setTextSize(14f);
//
//				TextView tvFrom2 = new TextView(this);
//				tvFrom2.setLayoutParams(llParams);
//				tvFrom2.setText(temp3);
//				tvFrom2.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tvFrom2.setTextColor(Color.WHITE);
//				tvFrom2.setTextSize(14f);
//
//				TextView tv2 = new TextView(this);
//				tv2.setText(getResources().getString(R.string.to));
//				tv2.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tv2.setTextColor(Color.WHITE);
//				tv2.setTextSize(16f);
//
//				TextView tvTo2 = new TextView(this);
//				tvTo2.setLayoutParams(llParams);
//				tvTo2.setText(temp4 + ",");
//				tvTo2.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tvTo2.setTextColor(Color.WHITE);
//				tvTo2.setTextSize(14f);
//
//				TextView tvFrom3 = new TextView(this);
//				tvFrom3.setText(bundle.getString("from3", ""));
//				tvFrom3.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tvFrom3.setTextColor(Color.WHITE);
//				tvFrom3.setTextSize(14f);
//
//				TextView tv3 = new TextView(this);
//				tv3.setLayoutParams(llParams);
//				tv3.setText(getResources().getString(R.string.to));
//				tv3.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tv3.setTextColor(Color.WHITE);
//				tv3.setTextSize(16f);
//
//				TextView tvTo3 = new TextView(this);
//				tvTo3.setText(bundle.getString("to3", "") + ",");
//				tvTo3.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tvTo3.setTextColor(Color.WHITE);
//				tvTo3.setTextSize(14f);
//
//				TextView tvFrom4 = new TextView(this);
//				tvFrom4.setLayoutParams(llParams);
//				tvFrom4.setText(bundle.getString("from4", ""));
//				tvFrom4.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tvFrom4.setTextColor(Color.WHITE);
//				tvFrom4.setTextSize(14f);
//
//				TextView tv4 = new TextView(this);
//				tv4.setText(getResources().getString(R.string.to));
//				tv4.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tv4.setTextColor(Color.WHITE);
//				tv4.setTextSize(16f);
//
//				TextView tvTo4 = new TextView(this);
//				tvTo4.setLayoutParams(llParams);
//				tvTo4.setText(bundle.getString("to4", ""));
//				tvTo4.setTypeface(Typeface.create("sans-serif-condensed",
//						Typeface.NORMAL));
//				tvTo4.setTextColor(Color.WHITE);
//				tvTo4.setTextSize(14f);
//
//				llHeader.addView(tvFrom1);
//				llHeader.addView(tv1);
//				llHeader.addView(tvTo1);
//				llHeader.addView(tvFrom2);
//				llHeader.addView(tv2);
//				llHeader.addView(tvTo2);
//				llHeader.addView(tvFrom3);
//				llHeader.addView(tv3);
//				llHeader.addView(tvTo3);
//				llHeader.addView(tvFrom4);
//				llHeader.addView(tv4);
//				llHeader.addView(tvTo4);
//
//				int margin = 5 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
//				((LinearLayout) findViewById(R.id.ll_header)).setPadding(0, 0,
//						margin, 0);
//			}
//
//		}
//		String details = FlightResultActivity.strDetails;
//
//		tvFlightDates.setText(details);
//
//	}

	public void clicker(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			finish();
			break;

//		case R.id.btn_book:
//			Intent pax = new Intent(this, FlightPaxActivity.class);
//			pax.putExtra("sID",
//					strSessionId + CommonFunctions.getRandomString(4));
//			pax.putExtra("tripID", fItem.strTripId);
//			pax.putExtra("isGroup", isGroup);
//			startActivity(pax);
		default:
			break;
		}
	}

	private void parseRoundtripResult() {
		String flightLogo = null;
		String flightCode = null;
		String flightName = null;
		String flightNumber = null;
		String departureCityCode = null;
		// String departureCityName = null;
		String arrivalCityCode = null;
		// String arrivalCityName = null;
		String departureDate = null;
		String arrivalDate = null;
		String departureTime = null;
		String arrivalTime = null;
		String departureAirportName = null;
		String arrivalAirportName = null;
		String durationPerLeg = null;
		String transitTime = null;
		String equipmentNumber = null;
		String bookingCode = null;
		String mealCode = null;
		String baggageAdult = null;
		String baggageChild = null;
		String baggageInfant = null;

		String flight_info = null;
		String from = null;
		String to = null;
		String depDate = null;
		String stops = null;

		JSONObject allJourney, listFlight, segment;
		JSONArray listFlightArray, segmentArray;

		try {
			for (int i = 0; i < jarray.length(); i++) {
				allJourney = jarray.getJSONObject(i);
				listFlightArray = allJourney.getJSONArray("ListFlight");
				for (int j = 0; j < listFlightArray.length(); ++j) {
					listFlight = listFlightArray.getJSONObject(j);
					flightLogo = listFlight.getString("FlightLogo");
					flightCode = listFlight.getString("FlightCode");
					flightName = listFlight.getString("FlightName");
					flightNumber = listFlight.getString("FlightNumber");
					departureCityCode = listFlight
							.getString("DepartureCityCode");
					// departureCityName =
					// listFlight.getString("DepartureCityName");
					arrivalCityCode = listFlight.getString("ArrivalCityCode");
					// arrivalCityName =
					// listFlight.getString("ArrivalCityName");
					departureDate = listFlight.getString("DepartureDateString");
					arrivalDate = listFlight.getString("ArrivalDateString");
					departureTime = listFlight.getString("DepartureTimeString");
					arrivalTime = listFlight.getString("ArrivalTimeString");
					departureAirportName = listFlight
							.getString("DepartureAirportName");
					arrivalAirportName = listFlight
							.getString("ArrivalAirportName");
					durationPerLeg = listFlight.getString("DurationPerLeg");
					durationPerLeg = durationPerLeg.replace("Day[s],", "d");
					durationPerLeg = durationPerLeg.replace("Hrs", "h");
					durationPerLeg = durationPerLeg.replace(" : ", " ");
					durationPerLeg = durationPerLeg.replace("Mins", "m");
					transitTime = listFlight.getString("TransitTime");
					equipmentNumber = listFlight.getString("EquipmentNumber");
					bookingCode = listFlight.getString("BookingCode");
					mealCode = listFlight.getString("MealCode");

					if (CommonFunctions.lang.equalsIgnoreCase("en"))
						flight_info = "Booking Class: " + bookingCode + " | "
								+ equipmentNumber + " | Meals: " + mealCode;
					else
						flight_info = mealCode + " : وجبات الطعام | "
								+ equipmentNumber + " | " + bookingCode
								+ " : الدرجة";

					final View view = getLayoutInflater().inflate(
							R.layout.flight_details_item, null);
					ImageView ivFlightLogo = (ImageView) view
							.findViewById(R.id.iv_flight_logo);
					InputStream ims;
					Drawable d;
					try {
						// get input stream
						ims = getAssets().open(flightLogo);
						// load image as Drawable
						d = Drawable.createFromStream(ims, null);
						// set image to ImageView
						ivFlightLogo.setImageDrawable(d);
						ims.close();
						d = null;
					} catch (IOException ex) {
						ex.printStackTrace();
						ivFlightLogo.setImageResource(R.mipmap.ic_no_image);
					}
					((TextView) view.findViewById(R.id.tv_flight_name))
							.setText(flightName);
					((TextView) view.findViewById(R.id.tv_flight_number))
							.setText(flightCode + "-" + flightNumber);
					((TextView) view.findViewById(R.id.tv_depart_code))
							.setText(departureCityCode);
					((TextView) view.findViewById(R.id.tv_arrival_code))
							.setText(arrivalCityCode);
					((TextView) view.findViewById(R.id.tv_depart_time))
							.setText(departureTime);
					((TextView) view.findViewById(R.id.tv_arrival_time))
							.setText(arrivalTime);
					((TextView) view.findViewById(R.id.tv_depart_date))
							.setText(departureDate);
					((TextView) view.findViewById(R.id.tv_arrival_date))
							.setText(arrivalDate);
					((TextView) view.findViewById(R.id.tv_depart_airport))
							.setText(departureAirportName);
					((TextView) view.findViewById(R.id.tv_arrival_airport))
							.setText(arrivalAirportName);
					((TextView) view.findViewById(R.id.tv_travel_time))
							.setText(durationPerLeg);
					((TextView) view.findViewById(R.id.tv_flight_info))
							.setText(flight_info);

					segmentArray = listFlight.getJSONArray("SegmentDetails");
					for (int k = 0; k < segmentArray.length(); ++k) {
						segment = segmentArray.getJSONObject(k);
						switch (k) {
						case 0:
							baggageAdult = segment.getString("Baggage");
							if (CommonFunctions.lang.equalsIgnoreCase("en"))
								((TextView) view
										.findViewById(R.id.tv_baggage_info_adult))
										.setText(getResources().getString(
												R.string.adult)
												+ ": " + baggageAdult);
							else
								((TextView) view
										.findViewById(R.id.tv_baggage_info_adult))
										.setText(baggageAdult
												+ " :"
												+ getResources().getString(
														R.string.adult));
							break;
						case 1:
							baggageChild = segment.getString("Baggage");
							((TextView) view
									.findViewById(R.id.tv_baggage_info_child))
									.setVisibility(View.VISIBLE);
							if (CommonFunctions.lang.equalsIgnoreCase("en")) {
								((TextView) view
										.findViewById(R.id.tv_baggage_info_child))
										.setText(getResources().getString(
												R.string.children)
												+ ": " + baggageChild);
							} else {
								((TextView) view
										.findViewById(R.id.tv_baggage_info_child))
										.setText(baggageChild
												+ " :"
												+ getResources().getString(
														R.string.children));
							}
							break;
						case 2:
							baggageInfant = segment.getString("Baggage");
							((TextView) view
									.findViewById(R.id.tv_baggage_info_infant))
									.setVisibility(View.VISIBLE);
							if (CommonFunctions.lang.equalsIgnoreCase("en")) {
								((TextView) view
										.findViewById(R.id.tv_baggage_info_infant))
										.setText(getResources().getString(
												R.string.infant)
												+ ": " + baggageInfant);
							} else {
								((TextView) view
										.findViewById(R.id.tv_baggage_info_infant))
										.setText(baggageInfant
												+ " :"
												+ getResources().getString(
														R.string.infant));
							}
							break;
						}
					}

					if (j == listFlightArray.length() - 1) {
						((TextView) view.findViewById(R.id.tv_transit_time))
								.setVisibility(View.GONE);
						to = arrivalCityCode;
					} else {
						if (CommonFunctions.lang.equalsIgnoreCase("en"))
							((TextView) view.findViewById(R.id.tv_transit_time))
									.setText(getResources().getString(
											R.string.plane_change)
											+ transitTime);
						else
							((TextView) view.findViewById(R.id.tv_transit_time))
									.setText(transitTime
											+ " :"
											+ getResources().getString(
													R.string.plane_change));
					}

					if (j == 0) {
						from = departureCityCode;
						depDate = departureDate;
					}

					switch (i) {
					case 0:
						((TextView) findViewById(R.id.tv_from_1)).setText(from);
						((TextView) findViewById(R.id.tv_to_1)).setText(to);
						((TextView) findViewById(R.id.tv_date_1))
								.setText(depDate);
						((TextView) findViewById(R.id.tv_total_time_1))
								.setText(allJourney.getString("TotalDuration"));

						if (Integer.parseInt(allJourney.getString("stops")) == 0)
							stops = getResources().getString(R.string.non_stop);
						else if (Integer
								.parseInt(allJourney.getString("stops")) == 1)
							stops = allJourney.getString("stops")
									+ " "
									+ getResources().getString(
											R.string.one_stop);
						else
							stops = allJourney.getString("stops")
									+ " "
									+ getResources().getString(
											R.string.more_stop);

						((TextView) findViewById(R.id.tv_stops_1))
								.setText(stops);
						llFlightDetails_1.addView(view);
						break;
					case 1:
						((TextView) findViewById(R.id.tv_from_2)).setText(from);
						((TextView) findViewById(R.id.tv_to_2)).setText(to);
						((TextView) findViewById(R.id.tv_date_2))
								.setText(depDate);
						((TextView) findViewById(R.id.tv_total_time_2))
								.setText(allJourney.getString("TotalDuration"));

						if (Integer.parseInt(allJourney.getString("stops")) == 0)
							stops = getResources().getString(R.string.non_stop);
						else if (Integer
								.parseInt(allJourney.getString("stops")) == 1)
							stops = allJourney.getString("stops")
									+ " "
									+ getResources().getString(
											R.string.one_stop);
						else
							stops = allJourney.getString("stops")
									+ " "
									+ getResources().getString(
											R.string.more_stop);

						((TextView) findViewById(R.id.tv_stops_2))
								.setText(stops);
						llFlight_2.setVisibility(View.VISIBLE);
						llFlightDetails_2.addView(view);
						break;
					case 2:
						((TextView) findViewById(R.id.tv_from_3)).setText(from);
						((TextView) findViewById(R.id.tv_to_3)).setText(to);
						((TextView) findViewById(R.id.tv_date_3))
								.setText(depDate);
						((TextView) findViewById(R.id.tv_total_time_3))
								.setText(allJourney.getString("TotalDuration"));

						if (Integer.parseInt(allJourney.getString("stops")) == 0)
							stops = getResources().getString(R.string.non_stop);
						else if (Integer
								.parseInt(allJourney.getString("stops")) == 1)
							stops = allJourney.getString("stops")
									+ " "
									+ getResources().getString(
											R.string.one_stop);
						else
							stops = allJourney.getString("stops")
									+ " "
									+ getResources().getString(
											R.string.more_stop);

						((TextView) findViewById(R.id.tv_stops_3))
								.setText(stops);
						llFlight_3.setVisibility(View.VISIBLE);
						llFlightDetails_3.addView(view);
						break;
					case 3:
						((TextView) findViewById(R.id.tv_from_4)).setText(from);
						((TextView) findViewById(R.id.tv_to_4)).setText(to);
						((TextView) findViewById(R.id.tv_date_4))
								.setText(depDate);
						((TextView) findViewById(R.id.tv_total_time_4))
								.setText(allJourney.getString("TotalDuration"));

						if (Integer.parseInt(allJourney.getString("stops")) == 0)
							stops = getResources().getString(R.string.non_stop);
						else if (Integer
								.parseInt(allJourney.getString("stops")) == 1)
							stops = allJourney.getString("stops")
									+ " "
									+ getResources().getString(
											R.string.one_stop);
						else
							stops = allJourney.getString("stops")
									+ " "
									+ getResources().getString(
											R.string.more_stop);

						((TextView) findViewById(R.id.tv_stops_4))
								.setText(stops);
						llFlight_4.setVisibility(View.VISIBLE);
						llFlightDetails_4.addView(view);
						break;
					}
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

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
