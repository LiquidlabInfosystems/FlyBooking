package com.travel.flybooking.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.travel.flybooking.support.CommonFunctions;
import com.travel.flybooking.support.PixelUtil;
import com.travel.flybooking.support.StrikedTextView;
import com.travel.flybooking.FlightResultGroupActivity;
import com.travel.flybooking.R;
import com.travel.model.FlightResultItem;

public class FlightResultInflator {

	private Context context;
	private ArrayList<FlightResultItem> arrayFlightResult;
	private boolean isRoundTrip = false, isGroup = false;
	String strBaggageDetails = null;
	CommonFunctions cf;
	LayoutInflater mInflater;

	public FlightResultInflator(Context context,
			ArrayList<FlightResultItem> arrayFlightResult, boolean isRoundTrip,
			LinearLayout llResult, OnClickListener clicker) {
		this.context = context;
		this.arrayFlightResult = arrayFlightResult;
		this.isRoundTrip = isRoundTrip;
		showResult(llResult, clicker);
	}

	public FlightResultInflator(Context context,
			ArrayList<FlightResultItem> arrayFlightResult, boolean isRoundTrip,
			LinearLayout llResult, OnClickListener clicker, boolean isGroup) {
		this.context = context;
		this.arrayFlightResult = arrayFlightResult;
		this.isRoundTrip = isRoundTrip;
		this.isGroup = isGroup;
		showResult(llResult, clicker);
	}

	public void showResult(LinearLayout llResult, OnClickListener clicker) {
		mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		llResult.removeAllViews();
		int id = 0;
		View vFlightResult;
		
		for (final FlightResultItem fItem : arrayFlightResult) {
			vFlightResult = mInflater.inflate(
					R.layout.item_search_result_flight, null);
			cf = new CommonFunctions(context);
			JSONObject allJourney, listFlight;
			JSONArray jarray, listFlightArray;
			ImageView ivFlightLogo;
			InputStream ims;
			Drawable d;
			String flightName, stops;
			jarray = fItem.getJarray();

			StrikedTextView tvDiscount = (StrikedTextView) vFlightResult.findViewById(R.id.tv_flight_price_no_discount);
			
			if(fItem.getDiscount() > 0) {
				String price = String.format(new Locale("en"), "%.3f",
						(fItem.getDoubleFlightPrice() + fItem.getDiscount()));
				tvDiscount.setText(CommonFunctions.strCurrency + " " + price);
				tvDiscount.setVisibility(View.VISIBLE);
				tvDiscount.addStrike = true;
				tvDiscount.invalidate();
			} else
				tvDiscount.setVisibility(View.GONE);
			
			String price = String.format(new Locale("en"), "%.3f",
					Double.parseDouble(fItem.getStrDisplayRate()));
			((TextView) vFlightResult.findViewById(R.id.tv_flight_price))
					.setText(CommonFunctions.strCurrency + " " + price);

			/*if (fItem.getBlRefundType()) {
				((TextView) vFlightResult.findViewById(R.id.tv_refundable))
						.setText(context.getResources().getString(
								R.string.refund));
				((TextView) vFlightResult.findViewById(R.id.tv_refundable))
						.setTextColor(Color.parseColor("#008000"));
			} else {
				((TextView) vFlightResult.findViewById(R.id.tv_refundable))
						.setText(context.getResources().getString(
								R.string.non_refund));
				((TextView) vFlightResult.findViewById(R.id.tv_refundable))
						.setTextColor(Color.parseColor("#B71C1C"));
			}*/

			final LinearLayout llMore = ((LinearLayout) vFlightResult
					.findViewById(R.id.ll_more_options));

			final LinearLayout llFlightItem = ((LinearLayout) vFlightResult
					.findViewById(R.id.ll_flight_items));

			if (!isGroup) {
				if (fItem.samePriceCount > 3) {
					((TextView) vFlightResult.findViewById(R.id.tv_more_options))
							.setText(String.valueOf(fItem.samePriceCount - 2));
					llMore.setVisibility(View.VISIBLE);
					llMore.setTag(fItem.getStrDisplayRate());
					llMore.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent group = new Intent(context,
									FlightResultGroupActivity.class);
							group.putExtra("price", llMore.getTag().toString());
							group.putExtra("isRoundTrip", isRoundTrip);
							context.startActivity(group);
						}
					});
				} else {
					llMore.setVisibility(View.INVISIBLE);
				}
			} else
				llMore.setVisibility(View.INVISIBLE);

			try {
				for (int i = 0; i < jarray.length(); i++) {
					View vFlightItem = mInflater.inflate(
							R.layout.item_flight_result, null);
					allJourney = jarray.getJSONObject(i);
					listFlightArray = allJourney.getJSONArray("ListFlight");
					listFlight = listFlightArray.getJSONObject(0);
					if (i == 0) {
						ivFlightLogo = (ImageView) vFlightResult
								.findViewById(R.id.iv_flight_logo);
						try {
							// get input stream
							ims = context.getAssets().open(
									listFlight.getString("FlightLogo"));
							// load image as Drawable
							d = Drawable.createFromStream(ims, null);
							// set image to ImageView
							ivFlightLogo.setImageDrawable(d);
							ims.close();
							d = null;
						} catch (IOException ex) {
							ex.printStackTrace();
							ivFlightLogo
									.setImageResource(R.mipmap.ic_no_image);
						}

						flightName = listFlight.getString("FlightName");

						((TextView) vFlightResult
								.findViewById(R.id.tv_airline_name))
								.setText(flightName);
					}

					((TextView) vFlightItem.findViewById(R.id.tv_depart_time))
							.setText(listFlight
									.getString("DepartureTimeString"));

					listFlight = listFlightArray.getJSONObject(listFlightArray
							.length() - 1);
					((TextView) vFlightItem.findViewById(R.id.tv_arrival_time))
							.setText(listFlight.getString("ArrivalTimeString"));

					String totalDuration = allJourney
							.getString("TotalDuration");

					if (allJourney.getInt("stops") == 0)
						stops = context.getResources().getString(
								R.string.non_stop);
					else if (allJourney.getInt("stops") == 1)
						stops = allJourney.getInt("stops")
								+ " "
								+ context.getResources().getString(
										R.string.one_stop);
					else
						stops = allJourney.getInt("stops")
								+ " "
								+ context.getResources().getString(
										R.string.more_stop);

					((TextView) vFlightItem
							.findViewById(R.id.tv_flight_time_stop))
							.setText(totalDuration + " | " + stops);

					llFlightItem.addView(vFlightItem);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

			TextView tvFlightDetails = (TextView) vFlightResult
					.findViewById(R.id.tv_flight_details);
			final LinearLayout llFlightDetails = (LinearLayout) vFlightResult
					.findViewById(R.id.ll_flight_details);

			tvFlightDetails.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (llFlightDetails.getVisibility() == View.GONE) {
                        LinearLayout.LayoutParams lParams =
                                new LinearLayout.LayoutParams(PixelUtil.dpToPx(context, 45),
                                        PixelUtil.dpToPx(context, 45));
                        lParams.gravity = Gravity.RIGHT;

						ImageView ivClose = new ImageView(context);
						ivClose.setLayoutParams(lParams);
						ivClose.setImageResource(R.mipmap.close_btn);
						ivClose.setPadding(PixelUtil.dpToPx(context, 5),
								PixelUtil.dpToPx(context, 5),
								PixelUtil.dpToPx(context, 5),
								PixelUtil.dpToPx(context, 5));
						ivClose.setScaleType(ImageView.ScaleType.FIT_END);
						ivClose.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								llFlightDetails.removeAllViews();
								llFlightDetails.setVisibility(View.GONE);
							}
						});
                        llFlightDetails.addView(ivClose);
                        (new FlightDetailsInflator()).showFlightDetails(fItem.getJarray(), llFlightDetails, context);
                        llFlightDetails.setVisibility(View.VISIBLE);

					} else {
						llFlightDetails.removeAllViews();
						llFlightDetails.setVisibility(View.GONE);
					}
				}
			});

			Button btnBook = (Button) vFlightResult.findViewById(R.id.btn_book);

			btnBook.setOnClickListener(clicker);
			btnBook.setId(id);
			id++;

			if (id != 0) {
				View vDivider = new View(context);
				vDivider.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, 2));
				vDivider.setBackgroundColor(Color.BLACK);
				llResult.addView(vDivider);
			}

			llResult.addView(vFlightResult);
		}

	}
}
