package com.travel.flybooking.adapter;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travel.flybooking.support.CommonFunctions;
import com.travel.flybooking.R;

public class FlightDetailsInflator {

	public FlightDetailsInflator() { }
	
	public void showFlightDetails(JSONArray jarray,
			LinearLayout llDetailsHolder, Context context) {
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

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		View vFlightDetails = mInflater.inflate(
				R.layout.activity_flight_details, null);

		LinearLayout llFlight_2 = (LinearLayout) vFlightDetails
				.findViewById(R.id.ll_flight_2);
		LinearLayout llFlight_3 = (LinearLayout) vFlightDetails
				.findViewById(R.id.ll_flight_3);
		LinearLayout llFlight_4 = (LinearLayout) vFlightDetails
				.findViewById(R.id.ll_flight_4);

		LinearLayout llFlightDetails_1 = (LinearLayout) vFlightDetails
				.findViewById(R.id.ll_flight_details_1);
		LinearLayout llFlightDetails_2 = (LinearLayout) vFlightDetails
				.findViewById(R.id.ll_flight_details_2);
		LinearLayout llFlightDetails_3 = (LinearLayout) vFlightDetails
				.findViewById(R.id.ll_flight_details_3);
		LinearLayout llFlightDetails_4 = (LinearLayout) vFlightDetails
				.findViewById(R.id.ll_flight_details_4);

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

					final View view = mInflater.inflate(
							R.layout.flight_details_item, null);
					ImageView ivFlightLogo = (ImageView) view
							.findViewById(R.id.iv_flight_logo);
					InputStream ims;
					Drawable d;
					try {
						// get input stream
						ims = context.getAssets().open(flightLogo);
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
										.setText(context.getResources()
												.getString(R.string.adult)
												+ ": " + baggageAdult);
							else
								((TextView) view
										.findViewById(R.id.tv_baggage_info_adult))
										.setText(baggageAdult
												+ " :"
												+ context.getResources()
														.getString(
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
										.setText(context.getResources()
												.getString(R.string.children)
												+ ": " + baggageChild);
							} else {
								((TextView) view
										.findViewById(R.id.tv_baggage_info_child))
										.setText(baggageChild
												+ " :"
												+ context
														.getResources()
														.getString(
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
										.setText(context.getResources()
												.getString(R.string.infant)
												+ ": " + baggageInfant);
							} else {
								((TextView) view
										.findViewById(R.id.tv_baggage_info_infant))
										.setText(baggageInfant
												+ " :"
												+ context
														.getResources()
														.getString(
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
									.setText(context.getResources().getString(
											R.string.plane_change)
											+ transitTime);
						else
							((TextView) view.findViewById(R.id.tv_transit_time))
									.setText(transitTime
											+ " :"
											+ context.getResources().getString(
													R.string.plane_change));
					}

					if (j == 0) {
						from = departureCityCode;
						depDate = departureDate;
					}

					switch (i) {
					case 0:
						((TextView) vFlightDetails.findViewById(R.id.tv_from_1))
								.setText(from);
						((TextView) vFlightDetails.findViewById(R.id.tv_to_1))
								.setText(to);
						((TextView) vFlightDetails.findViewById(R.id.tv_date_1))
								.setText(depDate);
						((TextView) vFlightDetails
								.findViewById(R.id.tv_total_time_1))
								.setText(allJourney.getString("TotalDuration"));

						if (Integer.parseInt(allJourney.getString("stops")) == 0)
							stops = context.getResources().getString(
									R.string.non_stop);
						else if (Integer
								.parseInt(allJourney.getString("stops")) == 1)
							stops = allJourney.getString("stops")
									+ " "
									+ context.getResources().getString(
											R.string.one_stop);
						else
							stops = allJourney.getString("stops")
									+ " "
									+ context.getResources().getString(
											R.string.more_stop);

						((TextView) vFlightDetails
								.findViewById(R.id.tv_stops_1)).setText(stops);
						llFlightDetails_1.addView(view);
						break;
					case 1:
						((TextView) vFlightDetails.findViewById(R.id.tv_from_2))
								.setText(from);
						((TextView) vFlightDetails.findViewById(R.id.tv_to_2))
								.setText(to);
						((TextView) vFlightDetails.findViewById(R.id.tv_date_2))
								.setText(depDate);
						((TextView) vFlightDetails
								.findViewById(R.id.tv_total_time_2))
								.setText(allJourney.getString("TotalDuration"));

						if (Integer.parseInt(allJourney.getString("stops")) == 0)
							stops = context.getResources().getString(
									R.string.non_stop);
						else if (Integer
								.parseInt(allJourney.getString("stops")) == 1)
							stops = allJourney.getString("stops")
									+ " "
									+ context.getResources().getString(
											R.string.one_stop);
						else
							stops = allJourney.getString("stops")
									+ " "
									+ context.getResources().getString(
											R.string.more_stop);

						((TextView) vFlightDetails
								.findViewById(R.id.tv_stops_2)).setText(stops);
						llFlight_2.setVisibility(View.VISIBLE);
						llFlightDetails_2.addView(view);
						break;
					case 2:
						((TextView) vFlightDetails.findViewById(R.id.tv_from_3))
								.setText(from);
						((TextView) vFlightDetails.findViewById(R.id.tv_to_3))
								.setText(to);
						((TextView) vFlightDetails.findViewById(R.id.tv_date_3))
								.setText(depDate);
						((TextView) vFlightDetails
								.findViewById(R.id.tv_total_time_3))
								.setText(allJourney.getString("TotalDuration"));

						if (Integer.parseInt(allJourney.getString("stops")) == 0)
							stops = context.getResources().getString(
									R.string.non_stop);
						else if (Integer
								.parseInt(allJourney.getString("stops")) == 1)
							stops = allJourney.getString("stops")
									+ " "
									+ context.getResources().getString(
											R.string.one_stop);
						else
							stops = allJourney.getString("stops")
									+ " "
									+ context.getResources().getString(
											R.string.more_stop);

						((TextView) vFlightDetails
								.findViewById(R.id.tv_stops_3)).setText(stops);
						llFlight_3.setVisibility(View.VISIBLE);
						llFlightDetails_3.addView(view);
						break;
					case 3:
						((TextView) vFlightDetails.findViewById(R.id.tv_from_4))
								.setText(from);
						((TextView) vFlightDetails.findViewById(R.id.tv_to_4))
								.setText(to);
						((TextView) vFlightDetails.findViewById(R.id.tv_date_4))
								.setText(depDate);
						((TextView) vFlightDetails
								.findViewById(R.id.tv_total_time_4))
								.setText(allJourney.getString("TotalDuration"));

						if (Integer.parseInt(allJourney.getString("stops")) == 0)
							stops = context.getResources().getString(
									R.string.non_stop);
						else if (Integer
								.parseInt(allJourney.getString("stops")) == 1)
							stops = allJourney.getString("stops")
									+ " "
									+ context.getResources().getString(
											R.string.one_stop);
						else
							stops = allJourney.getString("stops")
									+ " "
									+ context.getResources().getString(
											R.string.more_stop);

						((TextView) vFlightDetails
								.findViewById(R.id.tv_stops_4)).setText(stops);
						llFlight_4.setVisibility(View.VISIBLE);
						llFlightDetails_4.addView(view);
						break;
					}
				}

			}

			llDetailsHolder.addView(vFlightDetails);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
