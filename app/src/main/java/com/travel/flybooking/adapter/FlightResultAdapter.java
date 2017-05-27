package com.travel.flybooking.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.travel.flybooking.support.CommonFunctions;
import com.travel.flybooking.FlightResultGroupActivity;
import com.travel.flybooking.R;
import com.travel.model.FlightResultItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FlightResultAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<FlightResultItem> flightResultItem;
	private boolean isRoundTrip = false, isGroup = false;
	String strBaggageDetails = null;
	CommonFunctions cf;

	public FlightResultAdapter(Context context,
			ArrayList<FlightResultItem> flightResultItem, boolean isRoundTrip) {
		this.context = context;
		this.flightResultItem = flightResultItem;
		this.isRoundTrip = isRoundTrip;
	}

	public FlightResultAdapter(Context context,
			ArrayList<FlightResultItem> flightResultItem, boolean isRoundTrip,
			boolean isGroup) {
		this.context = context;
		this.flightResultItem = flightResultItem;
		this.isRoundTrip = isRoundTrip;
		this.isGroup = isGroup;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub

		return flightResultItem.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return flightResultItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		boolean blStart = false;
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.item_search_result_flight,
					null);
			blStart = true;
		}

		final FlightResultItem fItem = flightResultItem.get(position);
		cf = new CommonFunctions(context);
		JSONObject allJourney, listFlight;
		JSONArray jarray, listFlightArray;
		ImageView ivFlightLogo;
		InputStream ims;
		Drawable d;
		String flightName, stops;
		jarray = fItem.getJarray();

		String price = String.format(new Locale("en"), "%.3f",
				Double.parseDouble(fItem.getStrDisplayRate()));
		((TextView) convertView.findViewById(R.id.tv_flight_price))
				.setText(CommonFunctions.strCurrency + " " + price);

		/*if (fItem.getBlRefundType()) {
			((TextView) convertView.findViewById(R.id.tv_refundable))
					.setText(context.getResources().getString(R.string.refund));
			((TextView) convertView.findViewById(R.id.tv_refundable))
					.setTextColor(Color.parseColor("#008000"));
		} else {
			((TextView) convertView.findViewById(R.id.tv_refundable))
					.setText(context.getResources().getString(
							R.string.non_refund));
			((TextView) convertView.findViewById(R.id.tv_refundable))
					.setTextColor(Color.parseColor("#B71C1C"));
		}*/

		final LinearLayout llMore = ((LinearLayout) convertView
				.findViewById(R.id.ll_more_options));

		final LinearLayout llFlightItem = ((LinearLayout) convertView
				.findViewById(R.id.ll_flight_items));

		if (!isGroup) {
			if (fItem.samePriceCount > 1) {
				((TextView) convertView.findViewById(R.id.tv_more_options))
						.setText(String.valueOf(fItem.samePriceCount - 1));
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
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			for (int i = 0; i < jarray.length(); i++) {
				View vFlightItem = null;
				if (blStart)
					vFlightItem = mInflater.inflate(
							R.layout.item_flight_result, null);
				else {
					vFlightItem = llFlightItem.getChildAt(i);
				}

				allJourney = jarray.getJSONObject(i);
				listFlightArray = allJourney.getJSONArray("ListFlight");
				listFlight = listFlightArray.getJSONObject(0);
				if (i == 0) {
					ivFlightLogo = (ImageView) convertView
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
						ivFlightLogo.setImageResource(R.mipmap.ic_no_image);
					}

					flightName = listFlight.getString("FlightName");

					((TextView) convertView.findViewById(R.id.tv_airline_name))
							.setText(flightName);
				}

				((TextView) vFlightItem.findViewById(R.id.tv_depart_time))
						.setText(listFlight.getString("DepartureTimeString"));

				listFlight = listFlightArray.getJSONObject(listFlightArray
						.length() - 1);
				((TextView) vFlightItem.findViewById(R.id.tv_arrival_time))
						.setText(listFlight.getString("ArrivalTimeString"));

				String totalDuration = allJourney.getString("TotalDuration");

				if (allJourney.getInt("stops") == 0)
					stops = context.getResources().getString(R.string.non_stop);
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

				((TextView) vFlightItem.findViewById(R.id.tv_flight_time_stop))
						.setText(totalDuration + " | " + stops);

				if (blStart)
					llFlightItem.addView(vFlightItem);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return convertView;
	}

}
